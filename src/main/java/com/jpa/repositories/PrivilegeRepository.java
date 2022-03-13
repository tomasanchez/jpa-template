package com.jpa.repositories;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import com.jpa.core.database.PersistentEntitySet;
import com.jpa.model.user.Privilege;

public class PrivilegeRepository extends PersistentEntitySet<Privilege> {

    /**
     * Looks for a Privielge with a given name.
     * 
     * @param name to look for in role table.
     * @return a Privielge or empty
     * @throws EntityNotFoundException when no role was found
     */
    public Optional<Privilege> findByName(String name) {
        try {
            return Optional.of((Privilege) entityManager()
                    .createQuery(
                            String.format("FROM %s P WHERE P.name LIKE :name ", getTableName()))
                    .setParameter("name", name).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
