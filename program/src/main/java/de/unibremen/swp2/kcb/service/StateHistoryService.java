package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.persistence.statemachine.StateHistoryRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class to handle StateHistories.
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
@Transactional
public class StateHistoryService implements Service<StateHistory> {

    /**
     * Logger object of the StateHistory Service class
     */
    private static final Logger logger = LogManager.getLogger(StateHistoryService.class);

    /**
     * Injected instance of {@link StateHistoryRepository}
     */
    @Inject
    private StateHistoryRepository stateHistoryRepository;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Workstation}s.
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return StateHistory that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @Transactional
    public StateHistory create(final StateHistory entity) throws CreationException {

        //Saving stateHistory
        StateHistory repoEntity;
        try {
            logger.trace("Attempting to save stateHistory \"{}\" ...", entity);
            repoEntity = stateHistoryRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stateHistory \"{}\". Can't create stateHistory.", entity);
            throw new CreationException("Can't create stateHistory: " + e.getMessage());
        }

        logger.trace("Saving of stateHistory \"{}\" completed without exceptions.", entity);
        logger.info("Create stateHistory \"{}\" - triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stateHistory \"{}\"", entity);
        return repoEntity;
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
    public StateHistory update(final StateHistory entity) throws UpdateException {

        //Checking, if stateHistory is stored in database
        final StateHistory storedStateHistory = stateHistoryRepository.findBy(entity.getId());
        if (storedStateHistory == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving stateHistory
        StateHistory repoEntity;
        try {
            logger.trace("Attempting to save new stateHistory \"{}\" ...", entity);
            repoEntity = stateHistoryRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stateHistory \"{}\". Can't update stateHistory.", entity);
            throw new UpdateException("Can't update stateHistory: " + e.getMessage());
        }

        logger.trace("Saving of stateHistory \"{}\" completed without exceptions.", entity);
        logger.info("Update stateHistory \"{}\" - triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stateHistory \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    public void delete(final StateHistory entity) throws DeletionException {

        //Checking, if stateHistory is stored in database
        final StateHistory storedStateHistory = stateHistoryRepository.findBy(entity.getId());
        if (storedStateHistory == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting stateHistory
        try {
            logger.trace("Attempting to delete stateHistory \"{}\" ...", entity);
            stateHistoryRepository.attachAndRemove(entity);
            logger.info("Delete stateHistory \"{}\" triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting stateHistory \"{}\". Can't delete stateHistory.",entity);
            throw new DeletionException("Can't delete stateHistory: " + e.getMessage());
        }
    }

    /**
     * Returns if a given stateHistories last state is finished.
     *
     * @param stateHistory the stateHistory
     * @return whether a stateHistories last state is finished
     */
    public boolean isComplete(StateHistory stateHistory) {
        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return false;

        return !stateExecs.isEmpty() &&
                stateExecs.get(stateExecs.size() - 1).getFinishedAt() != null;
    }

    /**
     * Can execute boolean.
     *
     * @param stateHistory the state history
     * @return the boolean
     */
    public boolean canExecute(StateHistory stateHistory) {
        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null || stateExecs.isEmpty())
            return false;

        LocalDateTime startedAt = stateExecs.get(stateExecs.size() - 1).getStartedAt();
        LocalDateTime finishedAt = stateExecs.get(stateExecs.size() - 1).getFinishedAt();

        return (startedAt == null && finishedAt == null);
    }

    /**
     * Can finish boolean.
     *
     * @param stateHistory the state history
     * @return the boolean
     */
    public boolean canFinish(StateHistory stateHistory) {
        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null || stateExecs.isEmpty())
            return false;

        LocalDateTime startedAt = stateExecs.get(stateExecs.size() - 1).getStartedAt();
        LocalDateTime finishedAt = stateExecs.get(stateExecs.size() - 1).getFinishedAt();

        return (finishedAt == null && startedAt != null);
    }

    /**
     * Returns the latest StateExec in the stateHistory.
     *
     * @param stateHistory the stateHistory
     * @return the latest stateExec
     */
    public StateExec getCurrentStateExec(StateHistory stateHistory) {
        if (stateHistory == null)
            return null;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null || stateExecs.isEmpty())
            return null;

        return stateExecs.get(stateExecs.size() - 1);
    }

    /**
     * Return all existing StateHistories.
     *
     * @return List of all existing StateHistories.
     */
    public List<StateHistory> getAll() {
        logger.debug("Trying to query all stored StateHistories.");
        return stateHistoryRepository.findAll();
    }

    /**
     * Return the StateHistory with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific StateHistory
     * @throws InvalidIdException if the given ID was invalid
     */
    @RequiresAuthentication
    public StateHistory getById(String id) throws InvalidIdException {

        //Validating stateHistoryID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "StateHistoryID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("StateHistoryID \"{}\" is valid.", id);

        //Finding stateHistory by ID
        final StateHistory entity;
        try {
            logger.trace("Attempting to find stateHistory by ID \"{}\" ...", id);
            entity = stateHistoryRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding stateHistory by ID \"{}\". Can't find stateHistory.", id);
            throw new InvalidIdException("Can't find stateHistory: " + e.getMessage());
        }

        logger.trace("Finding of stateHistory by ID \"{}\" completed without exceptions.", id);
        logger.info("Find stateHistory by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning stateHistory by ID \"{}\"", id);
        return entity;
    }
}
