package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;

import java.util.List;

public class App {
    public static void main(String[] args) {
        // Configure Hibernate
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata md = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory sf = md.getSessionFactoryBuilder().build();

        // Open a session
        Session session = sf.openSession();
        // Begin transaction
        Transaction tr = session.beginTransaction();

        try {
            // Insert Customer records
            Customer c1 = new Customer();
            c1.setName("Alice Johnson");
            c1.setEmail("alice.johnson@example.com");
            c1.setAge(28);
            c1.setLocation("New York");

            Customer c2 = new Customer();
            c2.setName("Michael Brown");
            c2.setEmail("michael.brown@example.com");
            c2.setAge(35);
            c2.setLocation("Chicago");

            Customer c3 = new Customer();
            c3.setName("Sarah Davis");
            c3.setEmail("sarah.davis@example.com");
            c3.setAge(22);
            c3.setLocation("Los Angeles");

            session.save(c1);
            session.save(c2);
            session.save(c3);

            // Commit transaction
            tr.commit();

            // Start a new transaction for querying
            tr = session.beginTransaction();

            // Query 1: Customers located in "New York"
            Criteria criteriaEqual = session.createCriteria(Customer.class);
            criteriaEqual.add(Restrictions.eq("location", "New York"));
            List<Customer> customersInNewYork = criteriaEqual.list();
            System.out.println("Customers in New York:");
            customersInNewYork.forEach(c -> System.out.println(c.getName()));

            // Query 2: Customers not in "Los Angeles"
            Criteria criteriaNotEqual = session.createCriteria(Customer.class);
            criteriaNotEqual.add(Restrictions.ne("location", "Los Angeles"));
            List<Customer> customersNotInLA = criteriaNotEqual.list();
            System.out.println("Customers not in Los Angeles:");
            customersNotInLA.forEach(c -> System.out.println(c.getName()));

            // Query 3: Customers older than 25
            Criteria criteriaGreaterThan = session.createCriteria(Customer.class);
            criteriaGreaterThan.add(Restrictions.gt("age", 25));
            List<Customer> customersOlderThan25 = criteriaGreaterThan.list();
            System.out.println("Customers older than 25:");
            customersOlderThan25.forEach(c -> System.out.println(c.getName()));

            // Query 4: Customers aged between 20 and 30
            Criteria criteriaBetween = session.createCriteria(Customer.class);
            criteriaBetween.add(Restrictions.between("age", 20, 30));
            List<Customer> customersBetween20And30 = criteriaBetween.list();
            System.out.println("Customers aged between 20 and 30:");
            customersBetween20And30.forEach(c -> System.out.println(c.getName()));

            // Query 5: Customers with email ending in "example.com"
            Criteria criteriaLike = session.createCriteria(Customer.class);
            criteriaLike.add(Restrictions.like("email", "%example.com"));
            List<Customer> customersWithEmail = criteriaLike.list();
            System.out.println("Customers with email ending in 'example.com':");
            customersWithEmail.forEach(c -> System.out.println(c.getName()));

            // Commit transaction after querying
            tr.commit();
        } catch (Exception e) {
            // Rollback transaction in case of an error
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        } finally {
            // Close session and factory
            session.close();
            sf.close();
        }
    }
}
