package com.jpa.app;

import com.jpa.core.security.crypto.BCryptPasswordEncoder;
import com.jpa.model.user.User;
import com.jpa.repositories.UserRepository;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

/**
 * Bootstrap data initilizates.
 */
public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) {
        new Bootstrap().bootstrapData();
    }

    /**
     * Initializates an admin user.
     */
    public void bootstrapData() {
        User admin = new User("admin", new BCryptPasswordEncoder().encode("admin"));

        UserRepository repository = new UserRepository();

        try {
            withTransaction(() -> {
                repository.createEntity(admin);
            });
        } catch (Exception e) {
            System.out.println("User alredy exists");
        }

    }
}
