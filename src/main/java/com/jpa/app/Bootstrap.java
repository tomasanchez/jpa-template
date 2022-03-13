package com.jpa.app;

import com.jpa.app.seeder.AuthSeeder;
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
        new AuthSeeder().seed();
    }
}
