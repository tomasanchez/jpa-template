package com.jpa.services;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

/**
 * Convenience superclass for Transactional methods.
 * 
 * <br>
 * </br>
 * 
 * It is recommended to develop the business-logic in a service wich extends this convenient super
 * class.
 * 
 * @author Tomás Sánchez
 */
public abstract class TransactionalService
                implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

}
