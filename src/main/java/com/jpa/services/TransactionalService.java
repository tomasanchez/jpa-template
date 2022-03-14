package com.jpa.services;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public abstract class TransactionalService
        implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

}
