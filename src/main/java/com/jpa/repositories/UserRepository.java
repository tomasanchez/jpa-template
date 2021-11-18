package com.jpa.repositories;

import javax.persistence.NoResultException;
import com.jpa.core.database.PersistentEntitySet;
import com.jpa.model.user.User;

public class UserRepository extends PersistentEntitySet<User> {

    /**
     * Obtains an user from database that matches the given username and password.
     * 
     * @param username to match
     * @param password to validate
     * @return An authenticated user
     */
    public User getEntity(String username, String password) {
        try {
            return (User) entityManager()
                    .createQuery("FROM ".concat(getTableName())
                            .concat(" U WHERE U.uname LIKE :uname AND U.password LIKE :password"))
                    .setParameter("uname", username).setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
