package de.unibremen.swp2.kcb.util;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * This class is providing the EntityManager organisation for all database queries.
 *
 *
 * @see EntityManager
 *
 * @author Marc
 */
@ApplicationScoped
public class EntityManagerProducer
{
    /**
     * the injected EntityManagerFactory is given the PersistenceUnitName
     */
    @Inject
    @PersistenceUnitName("prototypePU")
    private EntityManagerFactory entityManagerFactory;

    /**
     * return the EntityManager
     *
     * @return the EntityManager
     */
    @Produces
    @Default
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * closes the EntityManager
     * @param entityManager the EntityManager to be closed
     */
    public void closeEntityManager(@Disposes @Default EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

    /**
     * closes the EntityManagerFactory
     */
    @PreDestroy
    public void closeFactory() {
        if(entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}