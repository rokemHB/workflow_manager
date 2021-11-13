package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.persistence.statemachine.StateMachineRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.StateMachineValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle StateMachines.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
@ApplicationScoped
public class StateMachineService implements Service<StateMachine> {

    /**
     * Logger object of the StateMachineService class
     */
    private static final Logger logger = LogManager.getLogger(StateMachineService.class);

    /**
     * Injected instance of {@link StateMachineValidator} to validate provided {@link StateMachine}s.
     */
    @Inject
    private StateMachineValidator stateMachineValidator;

    /**
     * Injected instance of {@link StateMachineRepository} to query database for {@link StateMachine}s.
     */
    @Inject
    private StateMachineRepository stateMachineRepository;

    /**
     * Injected instance of {@link StateService}
     */
    @Inject
    private StateService stateService;

    /**
     * Injected instance of {@link ProcessStepService}
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Injected instance of {@link UserService}
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return StateMachine that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @RequiresAuthentication
    @Override
    public StateMachine create(final StateMachine entity) throws CreationException, EntityAlreadyExistingException {

        //Validating stateMachine
        boolean valid;
        try {
            logger.trace("Attempting to validate stateMachine \"{}\" ...", entity);
            valid = stateMachineValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during stateMachine validation. Can't create stateMachine.");
            throw new CreationException("Can't create stateMachine: " + e.getMessage());
        }
        logger.trace("Validation of stateMachine \"{}\" completed without exceptions.", entity);

        //Checking, if stateMachine is valid
        if (!valid) {
            final String message = "StateMachine \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("StateMachine \"{}\" is valid.", entity);

        //Checking, if StateMachine is already stored in database
        final List<StateMachine> storedStateMachine = stateMachineRepository.findByName(entity.getName());
        if (!storedStateMachine.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create stateMachine: Entity is already stored in datasource");
        }

        //Appending transport state
        List<State> transportStates;
        try {
            transportStates = stateService.getByName("Transport");
        } catch (FindByException e) {
            throw new CreationException("Can't create stateMachine: " + e.getMessage());
        }
        if (transportStates == null || transportStates.size() != 1)
            throw new CreationException("Can't create stateMachine: Can't find 'Transport' state");
        entity.getStateList().add(transportStates.get(0));

        //Saving stateMachine
        StateMachine repoEntity;
        try {
            logger.trace("Attempting to save stateMachine \"{}\" ...", entity);
            repoEntity = stateMachineRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stateMachine \"{}\". Can't create stateMachine.", entity);
            throw new CreationException("Can't create stateMachine: " + e.getMessage());
        }

        logger.trace("Saving of stateMachine \"{}\" completed without exceptions.", entity);
        logger.info("Create stateMachine \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stateMachine \"{}\"", entity);
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
    @RequiresAuthentication
    @Override
    public StateMachine update(final StateMachine entity) throws UpdateException {

        //Validating stateMachine
        boolean valid;
        try {
            logger.trace("Attempting to validate stateMachine \"{}\" ...", entity);
            valid = stateMachineValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating stateMachine \"{}\". Can't update stateMachine", entity);
            throw new UpdateException("Can't update stateMachine: " + e.getMessage());
        }
        logger.trace("Validation of stateMachine \"{}\" completed without exceptions.", entity);

        //Checking, if stateMachine is valid
        if (!valid) {
            final String message = "StateMachine \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("StateMachine \"{}\" is valid.", entity);

        //Checking, if stateMachine is stored in database
        final StateMachine storedStateMachine = stateMachineRepository.findBy(entity.getId());
        if (storedStateMachine == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving stateMachine
        StateMachine repoEntity;
        try {
            logger.trace("Attempting to save new stateMachine \"{}\" ...", entity);
            repoEntity = stateMachineRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting stateMachine \"{}\". Can't update stateMachine.", entity);
            throw new UpdateException("Can't update stateMachine: " + e.getMessage());
        }

        logger.trace("Saving of stateMachine \"{}\" completed without exceptions.", entity);
        logger.info("Update stateMachine \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning stateMachine \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @RequiresAuthentication
    @Override
    public void delete(final StateMachine entity) throws DeletionException {

        //Checking, if stateMachine is stored in database
        final StateMachine storedStateMachine = stateMachineRepository.findBy(entity.getId());
        if (storedStateMachine == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting stateMachine
        try {
            logger.trace("Attempting to delete stateMachine \"{}\" ...", entity);
            stateMachineRepository.attachAndRemove(entity);
            logger.info("Delete stateMachine \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting stateMachine \"{}\". Can't delete stateMachine.", entity);
            throw new DeletionException("Can't delete stateMachine: " + e.getMessage());
        }
    }

    /**
     * Return all existing {@link StateMachine}s.
     *
     * @return Collection of all existing StateMachines.
     */
    public List<StateMachine> getAll() {
        logger.debug("Querying all stored StateMachines.");
        return stateMachineRepository.findAll();
    }

    /**
     * Return the {@link StateMachine} with the given id.
     *
     * @param id of the StateMachine
     * @return StateMachine with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public StateMachine getById(String id) throws InvalidIdException {

        //Validating stateMachineID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "StateMachineID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("StateMachineID \"{}\" is valid.", id);

        //Finding stateMachine by ID
        final StateMachine entity;
        try {
            logger.trace("Attempting to find stateMachine by ID \"{}\" ...", id);
            entity = stateMachineRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding stateMachine by ID \"{}\". Can't find stateMachine.", id);
            throw new InvalidIdException("Can't find stateMachine: " + e.getMessage());
        }

        logger.trace("Finding of stateMachine by ID \"{}\" completed without exceptions.", id);
        logger.info("Find stateMachine by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning stateMachine by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return the {@link StateMachine}s with the given name.
     *
     * @param name of the StateMachines to be returned.
     */
    public List<StateMachine> getByName(final String name) throws FindByException {
        if (name == null || name.equals("")) {
            logger.debug("Can't query StateMachine: provided name is empty or null.");
            throw new FindByException("Provided name is empty or null");
        }
        logger.debug("Trying to query StateMachine {} from datasource.", name);
        return stateMachineRepository.findByName(name);
    }

    /**
     * Returns a list of StateMachines containing a given state.
     *
     * @param state the state
     * @return the by state
     */
    public List<StateMachine> getByState(final State state) {
        return stateMachineRepository.findByState(state);
    }

    /**
     * Returns if a given stateMachine can be deleted.
     *
     * @param stateMachine the state machine
     * @return the boolean
     */
    public boolean canDelete(StateMachine stateMachine) {
        List<ProcessStep> allActiveSteps = processStepService.getActive();

        for(ProcessStep ps : allActiveSteps) {
            if(ps.getStateMachine().equals(stateMachine))
                return false;
        }

        return true;
    }
}