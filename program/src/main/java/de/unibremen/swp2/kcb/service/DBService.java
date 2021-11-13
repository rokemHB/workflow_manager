package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.serviceExceptions.BackupException;
import de.unibremen.swp2.kcb.util.EntityManagerProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Class DBService
 *
 * @author Robin
 * @author Marius
 */
@Transactional
@KCBSecure
public class DBService implements Serializable {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(DBService.class);

    /**
     * EntityManagerProducer to get EntityManager from
     */
    @Inject
    private EntityManagerProducer entityManagerProducer;

    /**
     * EntityManager to access Persistence Unit
     */
    private EntityManager entityManager;

    /**
     * Setup DBService and initialize EntityManager
     */
    @PostConstruct
    public void setUp() {
        this.entityManager = entityManagerProducer.getEntityManager();
    }

    /**
     * TearDown DBService
     */
    @PreDestroy
    public void tearDown() {
        this.entityManagerProducer.closeEntityManager(this.entityManager);
    }

    /**
     * Create a backup of the database
     *
     * @throws BackupException if backup fails
     */
    @RequiresAuthentication
    public File backup() throws BackupException {
        try {
            File tempFile = File.createTempFile("backup", ".zip");
            logger.info("Creating temp file for backup: " + tempFile.getAbsolutePath());
            this.entityManager.createNativeQuery("BACKUP TO ?").setParameter(1, tempFile.getAbsolutePath()).executeUpdate();
            return tempFile;
        } catch (PersistenceException e) {
            logger.debug(e);
            logger.warn("DB Backup failed.");
            throw new BackupException("Couldn't create Backup " + e.getMessage());
        } catch (IOException e) {
            logger.debug(e);
            logger.warn("DB Backup failed. Tempfile couldn't be created.");
            throw new BackupException("Couldn't create Backup " + e.getMessage());
        }
    }
}
