package com.example.jpa;


import com.example.jpa.model.Employee;
import com.example.jpa.service.EmployeeService;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws SQLException, LiquibaseException, ClassNotFoundException {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.user", "admin");
        properties.put("javax.persistence.jdbc.password", "admin");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:test");
        properties.put("hibernate.hbm2ddl.auto", "update");
        // properties.put("hibernate.temp.use_jdbc_metadata_defaults","false");
        //properties.put("show_sql", "true");



        //Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "admin", "admin");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase(Application.class.getClassLoader().getResource("db/db_test.xml").getPath(),
                                            new FileSystemResourceAccessor(),
                                            database);
        liquibase.update("test");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeService", properties);
        EntityManager em = emf.createEntityManager();
        EmployeeService service = new EmployeeService(em);

        List<Employee> norbson = service.findAllEmployees();
        //System.out.print("Found employee: " + "name: " + norbson.getName() + ", salary: " + norbson.getSalary());

        em.getTransaction().begin();
        Employee emp = service.createEmployee(158, "John Doe", 45000);
        em.getTransaction().commit();
        System.out.println("Persisted " + emp);

        emp = service.findEmployee(158);
        System.out.println("Found " + emp);

        List<Employee> employees = service.findAllEmployees();
        for (Employee e : employees)
            System.out.println("Found employee: " + e);

        em.getTransaction().begin();
        emp = service.raiseEmployeeSalary(158, 1000);
        em.getTransaction().commit();
        System.out.println("Update " + emp);

        em.getTransaction().begin();
        service.removeEmployee(158);
        em.getTransaction().commit();
        System.out.println("Removed Employee 158");

        em.close();
        emf.close();
    }
}