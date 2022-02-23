package com.jpa.core.database;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.NoResultException;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

/**
 * Convenience Class for creating Repository.
 */
@MappedSuperclass
public abstract class PersistentEntitySet<T> implements WithGlobalEntityManager {

    /**
     * Retrieves Table name (class name).
     * 
     * ? Example: PersistentEntitySet<User> => Table name is User
     * 
     * @return the table name
     */
    @SuppressWarnings("unchecked")
    protected String getTableName() {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]).getSimpleName();
    }

    /**
     * Obtains all entities in the database.
     * 
     * @return an entity list.
     */
    @SuppressWarnings("unchecked")
    public List<T> getEntitySet() {
        return entityManager().createQuery("from " + getTableName()).getResultList();
    }

    /**
     * Persists an entity in database.
     * 
     * @param entity to be persisted
     * @return persisted entity
     */
    public T createEntity(T entity) {
        entityManager().persist(entity);
        return entity;
    }

    /**
     * Obtains a single entity.
     * 
     * @param id the entity unique id
     * @return entity or null.
     */
    @SuppressWarnings("unchecked")
    public T getEntity(String id) {

        try {
            return (T) entityManager()
                    .createQuery("FROM " + getTableName() + " T WHERE T.id LIKE :id")
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    /**
     * Updates the database with the entity.
     * 
     * @param entity to be updated
     * @return the updated entity
     */
    public T updateEntity(T entity) {
        return entityManager().merge(entity);
    }

    /**
     * Updates the database with the entity.
     * 
     * @param id the entity's unique id
     * @return the updated entity
     */
    public T updateEntity(String id) {
        return updateEntity(getEntity(id));
    }

    /**
     * Removes an entity from database.
     * 
     * @param entity the entity to be deleted
     */
    public void deleteEntity(T entity) {
        entityManager().remove(entity);
    }

    /**
     * Removes an entity from database.
     * 
     * @param id the entity unique id
     */
    public void deleteEntity(String id) {
        deleteEntity(getEntity(id));
    }
}
