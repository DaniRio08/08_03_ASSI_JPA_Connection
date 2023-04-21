package pruebajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Function;

public class JpaService {

    private static JpaService instance;

    private EntityManagerFactory entityManagerFactory;
    private JpaService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("JPA_Connection-local");
    }

    public void shutDown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public static synchronized JpaService getInstance() {
        return instance == null ? instance = new JpaService() : instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /* Runs the specified function inside a transaction boundary. The function has
	 * access to a ready to use {@link EntityManager} and can return any type of
	 * value ({@code T}).*/
    public <T> T runInTransaction(Function<EntityManager, T> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        /* Hay que asegurarse de que la transacción solo haga commit cuando todos los datos
         * introducidos sean correctos, por lo que crearemos una variable que en caso de ser falsa hará un rollback */
        boolean success = false;
        transaction.begin();
        System.out.println("Starting transaction...");

        try {
            T returnValue = function.apply(entityManager);
            success = true;
            return returnValue;

        } finally {
            if (success) {
                transaction.commit();
                System.out.println("Transaction committed");
            } else {
                transaction.rollback();
                System.out.println("Transaction rollback");
            }
        }
    }
}
