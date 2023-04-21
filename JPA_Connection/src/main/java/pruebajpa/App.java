package pruebajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class App {
    private static JpaService jpaService = JpaService.getInstance();
    public static void main(String[] args) {
        try {
            createStudents();
            printStudents();
        } finally {
            jpaService.shutDown();
            System.out.println("Connection closed");
        }
    }
        private static void createStudents() {
            jpaService.runInTransaction(entityManager -> {
                entityManager.persist(new Students(
                        "Dani",
                        "Río",
                        "drioarizti@cifpfbmoll.eu",
                        "2001-09-29",
                        21));
                entityManager.persist(new Students(
                        "Raúl",
                        "Velásquez",
                        "rvelasquezvega@cifpfbmoll.eu",
                        "1999-06-09",
                        23));
                entityManager.persist(new Students(
                        "David",
                        "Ramirez",
                        "dramirezruiz@cifpfbmoll.eu",
                        "2000-07-06",
                        22));

                return null;
            });
        }
        private static void printStudents(){
            List<Students> resutlList = jpaService.runInTransaction(entityManager ->
                entityManager.createQuery(
                        "select st from Students st",
                        Students.class).getResultList());
            System.out.println("This are the current students:");
            System.out.println("");
            resutlList.stream()
                    .map(st -> "id: " + st.getStudent_id() + " | first_name: " + st.getFirst_name()
                    + " | last_name: " + st.getLast_name() + " | email: " + st.getEmail()
                    + " | birth date: " + st.getBirthdate() + " | age: " + st.getAge()).forEach(System.out::println);
        }
}