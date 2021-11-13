package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.persistence.PriorityRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.PriorityValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle Priorities
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
@Transactional
public class PriorityService implements Service<Priority> {

    /**
     * Logger object of the Priority Service class
     */
    private static final Logger logger = LogManager.getLogger(PriorityService.class);

    /**
     * Injected instance of {@link PriorityRepository}
     */
    @Inject
    private PriorityRepository priorityRepository;

    /**
     * Injected instance of {@link PriorityValidator}
     */
    @Inject
    private PriorityValidator priorityValidator;

    /**
     * Injected instance of {@link JobService}
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Priority}s.
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Priority that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public Priority create(final Priority entity) throws CreationException, EntityAlreadyExistingException {

        if(entity.getValue() <= 0) {
            logger.debug("Priority can't be less than or equal zero!");
            throw new CreationException("Priority can't be less than or equal zero!");
        }

        //Validating priority
        boolean valid;
        try {
            logger.trace("Attempting to validate priority \"{}\" ...", entity);
            valid = priorityValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during priority validation. Can't create priority.");
            throw new CreationException("Can't create priority: " + e.getMessage());
        }
        logger.trace("Validation of priority \"{}\" completed without exceptions.", entity);

        //Checking, if priority is valid
        if (!valid) {
            final String message = "Priority \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Priority \"{}\" is valid.", entity);

        //Checking, if Priority is already stored in database
        try {
            priorityRepository.findByName(entity.getName());

            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create Priority: Entity is already stored in datasource");
        }
        catch (NoResultException ex) {  // If Entity does not exist
            //Saving priority
            Priority repoEntity;
            try {
                logger.trace("Attempting to save priority \"{}\" ...", entity);
                repoEntity = priorityRepository.save(entity);
            } catch (PersistenceException e) {
                logger.debug("Error occurred while persisting priority \"{}\". Can't create priority.", entity);
                throw new CreationException("Can't create priority: " + e.getMessage());
            }

            logger.trace("Saving of priority \"{}\" completed without exceptions.", entity);
            logger.info("Create priority \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
            logger.trace("Returning priority \"{}\"", entity);
            return repoEntity;
        }
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    public Priority update(final Priority entity) throws UpdateException {

        //Validating priority
        boolean valid;
        try {
            logger.trace("Attempting to validate priority \"{}\" ...", entity);
            valid = priorityValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating priority \"" + entity + "\". Can't update priority");
            throw new UpdateException("Can't update priority: " + e.getMessage());
        }
        logger.trace("Validation of priority \"{}\" completed without exceptions.", entity);

        //Checking, if priority is valid
        if (!valid) {
            final String message = "Priority \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Priority \"{}\" is valid.", entity);

        //Checking, if priority is stored in database
        final Priority storedPriority = priorityRepository.findBy(entity.getId());
        if (storedPriority == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving priority
        Priority repoEntity;
        try {
            logger.trace("Attempting to save new priority \"{}\" ...", entity);
            repoEntity = priorityRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting priority \"{}\". Can't update priority.", entity);
            throw new UpdateException("Can't update priority: " + e.getMessage());
        }

        logger.trace("Saving of priority \"{}\" completed without exceptions.", entity);
        logger.info("Update priority \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning priority \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    public void delete(final Priority entity) throws DeletionException {

        //Checking, if priority is stored in database
        final Priority storedPriority = priorityRepository.findBy(entity.getId());
        if (storedPriority == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting priority
        try {
            logger.trace("Attempting to delete priority \"{}\" ...", entity);
            priorityRepository.attachAndRemove(entity);
            logger.info("Delete priority \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting priority \"{}\". Can't delete priority.", entity);
            throw new DeletionException("Can't delete priority: " + e.getMessage());
        }
    }

    /**
     * Return the Priority with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific Priority
     * @throws InvalidIdException if the given ID was invalid
     */
    public Priority getById(String id) throws InvalidIdException {

        //Validating priorityID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "PriorityID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("PriorityID \"{}\" is valid.", id);

        //Finding priority by ID
        final Priority entity;
        try {
            logger.trace("Attempting to find priority by ID \"{}\" ...", id);
            entity = priorityRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding priority by ID \"{}\". Can't find priority.", id);
            throw new InvalidIdException("Can't find priority: " + e.getMessage());
        }

        logger.trace("Finding of priority by ID \"{}\" completed without exceptions.", id);
        logger.info("Find priority by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning priority by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return the {@link Priority} with the given name.
     * If no Priority is found with the given name, null will be returned.
     *
     * @param name of the Priority to be returned
     * @return the by name
     */
    public Priority getByName(final String name) throws FindByException {
        if (name == null || name.equals("")) {
            logger.error("Can't query Priority: Priority name empty/null");
            throw new FindByException("Can't query Priority: Priority name empty/null");
        }

        logger.info("Trying to query Priority named {} from database.", name);

        Priority result = priorityRepository.findByName(name);

        if (result == null) {
            logger.error("Query error: Priority could not be queried from database.");
            throw new FindByException("Query error: Priority could not be queried from database.");
        }

        logger.info("Successfully queried Priority {} from database.", name);
        return result;
    }

    /**
     * Return all existing Priorities.
     *
     * @return Collection of all existing Priorities.
     */
    public List<Priority> getAll() {
        logger.debug("Trying to query all stored Priorities.");
        return priorityRepository.findAll();
    }

    /**
     * Returns the unique hexCode color for the given priority
     *
     * @param priority to calculate the color for
     * @return a String containing RGB-Values
     */
    public String getColor(Priority priority) {
        List<Priority> priorityList = priorityRepository.findAllOrderByValueAsc();
        float length = priorityList.size();
        float index = priorityList.indexOf(priority);

        if (index == 0) return "rgb(0.0, 255, 100)";
        if (index < (length / 2)) {
            index++;
            int greenChannel = 255;
            float redChannel = index / (length / 2) * 255;
            return "rgb(" + redChannel + ", " + greenChannel + ", 100)";
        } else {
            index++;
            int greenChannel = 255 - (int) ((index / (length / 2)) * 255) % 256;
            float redChannel = 255;
            return "rgb(" + redChannel + ", " + greenChannel + ", 100)";
        }
    }

    /**
     * Returns if the priority is unused and can be deleted.
     *
     * @param priority the priority
     * @return the boolean
     */
    public boolean canDelete(Priority priority) {
        if (priority == null)
            return false;

        List<Job> jobs = jobService.getByPriority(priority);

        return jobs == null || jobs.isEmpty();
    }
}
