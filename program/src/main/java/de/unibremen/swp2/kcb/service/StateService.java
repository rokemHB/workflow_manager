package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.persistence.statemachine.StateRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.StateValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle States.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
public class StateService implements Service<State> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(StateService.class);

    /**
     * Injected instance of {@link StateValidator} to validate provided {@link State}s.
     */
    @Inject
    private StateValidator stateValidator;

    /**
     * Injected instance of {@link StateRepository} to validate provided {@link State}s.
     */
    @Inject
    private StateRepository stateRepository;

    /**
     * Injected instance of {@link ProcedureService}
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Injected instance of {@link StateMachineService}
     */
    @Inject
    private StateMachineService stateMachineService;

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
     * @return State that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public State create(final State entity) throws CreationException, EntityAlreadyExistingException {

        //Validating state
        boolean valid;
        try {
            logger.trace("Attempting to validate state \"{}\" ...", entity);
            valid = stateValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during state validation. Can't create state.");
            throw new CreationException("Can't create state: " + e.getMessage());
        }
        logger.trace("Validation of state \"{}\" completed without exceptions.", entity);

        //Checking, if state is valid
        if (!valid) {
            final String message = "State \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("State \"{}\" is valid.", entity);

        //Checking, if workstation is already stored in database
        final List<State> storedState = stateRepository.findByName(entity.getName());
        if (!storedState.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create workstation: Entity is already stored in datasource");
        }

        //Saving state
        State repoEntity;
        try {
            logger.trace("Attempting to save state \"{}\" ...", entity);
            repoEntity = stateRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting state \"{}\". Can't create state.", entity);
            throw new CreationException("Can't create state: " + e.getMessage());
        }

        logger.trace("Saving of state \"{}\" completed without exceptions.", entity);
        logger.info("Create state \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning state \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public State update(final State entity) throws UpdateException {

        //Validating state
        boolean valid;
        try {
            logger.trace("Attempting to validate state \"{}\" ...", entity);
            valid = stateValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating state \"{}\". Can't update state", entity);
            throw new UpdateException("Can't update state: " + e.getMessage());
        }
        logger.trace("Validation of state \"{}\" completed without exceptions.", entity);

        //Checking, if state is valid
        if (!valid) {
            final String message = "State \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("State \"{}\" is valid.", entity);

        //Checking, if state is stored in database
        final State storedState = stateRepository.findBy(entity.getId());
        if (storedState == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving state
        State repoEntity;
        try {
            logger.trace("Attempting to save new state \"{}\" ...", entity);
            repoEntity = stateRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting state \"{}\". Can't update state.", entity);
            throw new UpdateException("Can't update state: " + e.getMessage());
        }

        logger.trace("Saving of state \"{}\" completed without exceptions.", entity);
        logger.info("Update state \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning state \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    public void delete(final State entity) throws DeletionException {

        //Checking, if state is stored in database
        final State storedState = stateRepository.findBy(entity.getId());
        if (storedState == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting state
        try {
            logger.trace("Attempting to delete state \"{}\" ...", entity);
            stateRepository.attachAndRemove(entity);
            logger.info("Delete state \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting state \"{}\". Can't delete state.", entity);
            throw new DeletionException("Can't delete state: " + e.getMessage());
        }
    }

    /**
     * Returns if a given state is the last executable state in a given stateMachine.
     *
     * @param state        the state
     * @param stateMachine the stateMachine
     * @return if its the last executable state
     */
    public boolean isLastExecutableState(State state, StateMachine stateMachine) {
        if (state == null ||stateMachine == null)
            return false;

        List<State> states = stateMachine.getStateList();

        if (state.getName().equals("Transport"))
            return false;

        if (states.indexOf(state) == states.size() - 1)
            return true;

        return states.indexOf(state) == states.size() - 2 &&
                states.get(states.size() - 1).getName().equals("Transport");
    }

    /**
     * Is last state boolean.
     *
     * @param state        the state
     * @param stateMachine the state machine
     * @return the boolean
     */
    public boolean isLastState(State state, StateMachine stateMachine) {
        if (state == null ||stateMachine == null)
            return false;

        List<State> states = stateMachine.getStateList();
        return states.indexOf(state) == states.size() - 1;
    }

    /**
     * Returns if the state of a procedure is complete.
     * @param state the state
     * @param procedure the procedure
     * @return if complete
     */
    public boolean isComplete(State state, Procedure procedure) {
        if(state == null || procedure == null)
            return false;

        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();

        State currentState = procedureService.getCurrentState(procedure);

        if (currentState == null)
            return false;

        StateHistory stateHistory = procedure.getStateHistory();

        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return false;

        int indexOfState = states.indexOf(state);
        int indexOfCurrentState = states.indexOf(currentState);

        if (stateExecs.get(indexOfCurrentState).getFinishedAt() != null)
            return true;

        return (indexOfState < indexOfCurrentState);
    }

    /**
     * Returns if the state of a procedure is actually pending.
     * @param state the state
     * @param procedure the procedure
     * @return if pending
     */
    public boolean isPending(State state, Procedure procedure) {
        if(state == null || procedure == null)
            return false;

        StateHistory stateHistory = procedure.getStateHistory();

        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null || stateExecs.isEmpty())
            return false;

        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();
        State currentState = procedureService.getCurrentState(procedure);

        if (currentState == null)
            return false;

        return (states.indexOf(state) == states.indexOf(currentState) &&
                stateExecs.get(stateExecs.size() - 1).getStartedAt() != null);
    }

    /**
     * Return all existing States.
     *
     * @return Collection of all existing States.
     */
    public List<State> getAll() {
        logger.debug("Trying to query all stored States.");
        return stateRepository.findAll();
    }

    public List<State> getAllWithoutTransport() {
        logger.debug("Trying to query all stored States.");
        List<State> allStatesWithoutTransport = stateRepository.findAll();

        List<State> transportStates = null;
        try {
            transportStates = this.getByName("Transport");
        } catch (FindByException e) {
            return null;
        }
        if (transportStates == null || transportStates.size() != 1)
            return null;

        allStatesWithoutTransport.remove(transportStates.get(0));

        return allStatesWithoutTransport;
    }

    /**
     * Return the {@link State} with the given id.
     *
     * @param id of the State
     * @return State with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public State getById(String id) throws InvalidIdException {

        //Validating stateID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "StateID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("StateID \"{}\" is valid.", id);

        //Finding state by ID
        final State entity;
        try {
            logger.trace("Attempting to find state by ID \"{}\" ...", id);
            entity = stateRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding state by ID \"{}\". Can't find state.", id);
            throw new InvalidIdException("Can't find state: " + e.getMessage());
        }

        logger.trace("Finding of state by ID \"{}\" completed without exceptions.", id);
        logger.info("Find state by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning state by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return the {@link State}s with the given name.
     * If no States exists with the given name, null will be returned.
     *
     * @param name the name of return states.
     * @return States with given name.
     * @throws FindByException If findBy fails.
     */
    public List<State> getByName(final String name) throws FindByException {
        if (name == null) {
            logger.warn("Name can't be null.");
            throw new FindByException("Name can't be null");
        }
        logger.trace("Returning states by name.");
        return stateRepository.findByName(name);
    }


    /**
     * Return Collection of {@link State}s that block a {@link Workstation}.
     *
     * @param blocking The boolean.
     * @return Collection of {@link State}s that block a {@link Workstation}.
     */
    public List<State> getByBlocking(final Boolean blocking) throws FindByException {
        if (blocking == null) {
            logger.warn("Blocking can't be null.");
            throw new FindByException("Blocking can't be null.");
        }
        logger.trace("Returning states by blocking.");
        return stateRepository.findByBlocking(blocking);
    }

    /**
     * Returns if the state is currently not in use and can be deleted.
     *
     * @param state the carrier
     * @return the boolean
     */
    public boolean canDelete(State state) {
        List<StateMachine> stateMachines = stateMachineService.getByState(state);

        return stateMachines == null || stateMachines.isEmpty();
    }
}