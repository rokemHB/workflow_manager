package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.GlobalConfig;
import de.unibremen.swp2.kcb.persistence.GlobalConfigRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle Configuration.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
public class GlobalConfigService implements Service<GlobalConfig> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(GlobalConfigService.class);

    /**
     * Injected instance of globalConfigRepository
     */
    @Inject
    private GlobalConfigRepository globalConfigRepository;

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Configuration that has been created and stored
     * @throws CreationException if creation fails
     */
    @Override
    public GlobalConfig create(final GlobalConfig entity) throws CreationException {

        //Saving globalConfig
        GlobalConfig repoEntity;
        try {
            logger.trace("Attempting to save globalConfig \" {} \" ...", entity);
            repoEntity = globalConfigRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting globalConfig \" {} \". Can't create globalConfig.", entity);
            throw new CreationException("Can't create globalConfig: " + e.getMessage());
        }

        logger.trace("Saving of globalConfig \"{}\" completed without exceptions.", entity);
        logger.info("Create globalConfig \" {} \" - triggered by: {}", entity.getKey(), userService.getExecutingUser().getUsername());
        logger.trace("Returning globalConfig \" {} \"", entity);
        return repoEntity;
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @throws UpdateException If update fails
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    public GlobalConfig update(final GlobalConfig entity) throws UpdateException {

        //Checking, if globalConfig is stored in database
        final GlobalConfig storedGlobalConfig = globalConfigRepository.findBy(entity.getKey());
        if (storedGlobalConfig == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving globalConfig
        GlobalConfig repoEntity;
        try {
            logger.trace("Attempting to save new globalConfig \"{}\" ...", entity);
            repoEntity = globalConfigRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting globalConfig \"{}\". Can't update globalConfig.", entity);
            throw new UpdateException("Can't update globalConfig: " + e.getMessage());
        }

        logger.trace("Saving of globalConfig \"{}\" completed without exceptions.", entity);
        logger.info("Update globalConfig \"{}\" - triggered by: {}", entity.getKey(),  userService.getExecutingUser().getUsername());
        logger.trace("Returning globalConfig \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException if deletion of the user fails
     */
    @Override
    public void delete(final GlobalConfig entity) throws DeletionException {

        //Checking, if globalConfig is stored in database
        final GlobalConfig storedGlobalConfig = globalConfigRepository.findBy(entity.getKey());
        if (storedGlobalConfig == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting globalConfig
        try {
            logger.trace("Attempting to delete globalConfig \"{}\" ...", entity);
            globalConfigRepository.attachAndRemove(entity);
            logger.info("Delete globalConfig \"{}\" - triggered by: {}", entity.getKey(),  userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting globalConfig \"{}\". Can't delete globalConfig.", entity);
            throw new DeletionException("Can't delete globalConfig: " + e.getMessage());
        }
    }

    /**
     * Return the configuration data associated with the given key.
     *
     * @param key of the configuration setting to be returned
     * @return {@link GlobalConfig} with the associated key or null if no configuration is found.
     * @throws FindByException if findBy fails
     */
    public GlobalConfig getByKey(final String key) throws FindByException {
        if (key == null)
            throw new FindByException("Can't find null key.");

        return globalConfigRepository.findByKey(key).get(0);
    }

    /**
     * Return all existing {@link GlobalConfig}
     *
     * @return Collection of all existing GlobalConfigs.
     */
    public List<GlobalConfig> getAll() {
        return globalConfigRepository.findAll();
    }

    /**
     * Gets old active job hardcoded from the database and returns the value as int.
     *
     * @return the old active job time in minutes as int
     */
    public int getOldActiveJobAsInt() {
        int result = 0;
        try {
            GlobalConfig oldActiveJobString = this.getByKey("oldActiveJob");
            result = Integer.parseInt(oldActiveJobString.getValue());
        } catch (FindByException e) {
            logger.info("OldActiveJob entry not found.");
        }
        return result;
    }

    /**
     * Takes minutes from this.getOldActiveJobsAsInt() and returns the equivalent number in days
     * @return number of days within the minutes
     */
    public int getDays() {
        int minutes = this.getOldActiveJobAsInt();
        return minutes / 1440;
    }

    /**
     * Takes minutes from this.getOldActiveJobsAsInt() and returns the equivalent number in hours
     * @return number of hours within the minutes
     */
    public int getHours() {
        int minutes = this.getOldActiveJobAsInt();
        int hours = minutes % 1440;
        return hours / 60;
    }

    /**
     * Takes minutes from this.getOldActiveJobsAsInt() and returns the equivalent number in minutes
     * after days and hours are cut off
     * @return the minutes below 60
     */
    public int getMinutes() {
        int total = this.getOldActiveJobAsInt();
        int hours = total % 1440;
        return hours % 60;
    }

    /**
     * Sets old active jobs value.
     * Transforms the number auf days hours and minutes to minutes and saves them as String within the globalConfig oldActiveJob
     * @param days the days
     * @param hours the hours
     * @param minutes the minutes
     */
    public void setOldActiveJobs(int days, int hours, int minutes) throws UpdateException {
        GlobalConfig oldActiveJob = new GlobalConfig();
        oldActiveJob.setKey("oldActiveJob");
        int result = 1440*days + 60*hours + minutes;
        oldActiveJob.setValue(String.valueOf(result));
        try {
            this.update(oldActiveJob);
        } catch (UpdateException e) {
            throw new UpdateException("oldActiveJob could not be updated");
        }
        logger.info("OldActiveJob value was set.");
    }
}
