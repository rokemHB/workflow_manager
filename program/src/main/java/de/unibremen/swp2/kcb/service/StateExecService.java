package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.persistence.statemachine.StateExecRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service class to handle StateExecs
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
public class StateExecService implements Service<StateExec> {

    /**
     * Logger object of the StateExec Service class
     */
    private static final Logger logger = LogManager.getLogger(StateExecService.class);

    /**
     * Injected verison of stateExecRepository
     */
    @Inject
    private StateExecRepository stateExecRepository;

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * Method is not implemented.
     * @param entity to be created and persisted
     * @return nothing
     */
    @Override
    public StateExec create(StateExec entity) throws CreationException {
        return null;
    }

    /**
     * Method is not implemented.
     * @param entity to be updated and persisted
     * @return nothing
     */
    @Override
    public StateExec update(StateExec entity) throws UpdateException {
        return null;
    }

    /**
     * Method is not implemented.
     * @param entity to be deleted
     */
    @Override
    public void delete(StateExec entity) throws DeletionException {
        throw new UnsupportedOperationException("method should never be called");
    }

    /**
     * Calculates the transition time
     *
     * @param stateExec the stateExec
     */
    public int calculateTransitionTime(StateExec stateExec) {
        if (stateExec == null || stateExec.getStartedAt() == null)
            return -1;
        return (int) ChronoUnit.MINUTES.between(stateExec.getStartedAt(), LocalDateTime.now());
    }

    /**
     * Return the StateExec with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific StateExec
     * @throws InvalidIdException if the given ID was invalid
     */
    public StateExec getById(String id) throws InvalidIdException {

        //Validating StateExecID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "StateExecID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("StateExecID \"{}\" is valid.", id);

        //Finding stateExec by ID
        final StateExec entity;
        try {
            logger.trace("Attempting to find stateExec by ID \"{}\" ...", id);
            entity = stateExecRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding stateExec by ID \"{}\". Can't find stateExec.", id);
            throw new InvalidIdException("Can't find stateExec: " + e.getMessage());
        }

        logger.trace("Finding of stateExec by ID \"{}\" completed without exceptions.", id);
        logger.info("Find stateExec by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning stateExec by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return all existing StateExecs.
     *
     * @return List of all existing StateExecs.
     */
    public List<StateExec> getAll() {
        logger.debug("Trying to query all stored StateExecs.");
        return stateExecRepository.findAll();
    }

}
