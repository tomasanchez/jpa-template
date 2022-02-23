package com.jpa.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.jpa.model.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class UserRepositoryTest implements WithGlobalEntityManager {


    private User user;
    private UserRepository repository;

    @BeforeEach
    void startTransaction() {
        entityManager().getTransaction().begin();
        user = new User();
        user.setUname("admin");
        user.setPassword("password");
        user.toJSON();
        repository = new UserRepository();
    }

    @AfterEach
    void endTransaction() {
        entityManager().getTransaction().rollback();
    }

    @Test
    void userShouldBePersisted() {
        repository.createEntity(user);
        assertSame(user, entityManager().find(User.class, user.getId()));
    }


    @Test
    void userShouldBeRetrieved() {
        repository.createEntity(user);
        entityManager().flush();
        assertEquals(user.getId(), repository.getEntity(user.getId()).getId());
    }

    @Test
    void userShouldBeNullWhenIdDoesNotExists() {
        assertNull(repository.getEntity("-1L"));
    }


    @Test
    void userShouldBeUpdated() {
        String newUname = "new username";
        repository.createEntity(user);
        entityManager().flush();
        user.setUname(newUname);
        repository.updateEntity(user);
        entityManager().flush();
        assertEquals(newUname, repository.getEntity(user.getId()).getUname());
    }

    @Test
    void userShouldBeRemoved() {
        int previousSize = repository.getEntitySet().size();
        repository.createEntity(user);
        entityManager().flush();
        repository.deleteEntity(user);
        entityManager().flush();
        assertEquals(previousSize, repository.getEntitySet().size());
    }

    @Test
    void entitySetIsRetrieved() {
        int previousSize = repository.getEntitySet().size();

        repository.createEntity(user);

        repository.createEntity(new User("test", "test"));

        entityManager().flush();
        assertEquals(previousSize + 2, repository.getEntitySet().size());
    }


    @Test
    void userIsRetrievedWithCredentials() {
        repository.createEntity(user);
        entityManager().flush();
        assertEquals(user.getId(),
                repository.getEntity(user.getUname(), user.getPassword()).getId());
    }

    @Test
    void userIsNullWhenCredentialsAreInvalid() {
        repository.createEntity(user);
        entityManager().flush();
        assertNull(repository.getEntity(user.getUname(), user.getPassword().toUpperCase()));
    }


}
