package com.demo.hungnguyendev.databases.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (isTableEmpty()) {

            String passwordEncoded = passwordEncoder.encode("password");
            entityManager.createNativeQuery("INSERT INTO users (user_catalogue_id, name, email, password, phone, address, image) VALUES (1, 'John Doe', 'john.doe@example.com', :password, '1234567890', '123 Main St', 'default.png')")
                         .setParameter("password", passwordEncoded)
                         .executeUpdate();

            System.out.println("Users Seeding ...");
        }
    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(u.id) FROM User u").getSingleResult();
        return count == 0;
    }
}
