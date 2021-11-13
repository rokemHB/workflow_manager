package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.statemachine.StateRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.StateValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link StateService}
 *
 * @author Robin
 * @author Arvid
 */
public class StateServiceTest {

    /**
     * Injected instance of service
     */
    @InjectMocks
    private StateService service;

    /**
     * Mocked version of validator
     */
    @Mock
    private StateValidator validator;

    /**
     * Mocked version of repository
     */
    @Mock
    private StateRepository repository;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of states
     */
    @Mock
    private List<State> states;

    /**
     * Mocked version of stateExecs
     */
    @Mock
    private List<StateExec> stateExecs;

    /**
     * Mocked version of stateExec
     */
    @Mock
    private StateExec stateExec;

    /**
     * Mocked version of procedureService
     */
    @Mock
    private ProcedureService procedureService;

    /**
     * Mocked version of stateMachine
     */
    @Mock
    private StateMachine stateMachine;

    /**
     * Mocked version of stateHistory
     */
    @Mock
    private StateHistory stateHistory;

    /**
     * Mocked version of processStep
     */
    @Mock
    private ProcessStep processStep;

    /**
     * Mocked version of procedure
     */
    @Mock
    private Procedure procedure;

    /**
     * Mocked version of user
     */
    @Mock
    private User user;

    /**
     * Mocked version of the state entity
     */
    @Mock
    private State state;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * setup method of stateServiceTest
     * @throws ValidationException when validation fails
     */
    private void setUpCreateUpdateDelete() throws ValidationException {
        when(validator.validate(state)).thenReturn(true);
        when(repository.findBy(state.getId())).thenReturn(state);
        when(repository.save(state)).thenReturn(state);
    }

    /**
     * sets up existing user
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Tests, if creation of a null object throws a CreationException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws CreationException If creation of the state fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.create(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFails() throws ValidationException, UpdateException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.update(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationInvalid() throws ValidationException, UpdateException {
        when(validator.validate(state)).thenReturn(false);
        service.update(state);
    }


    /**
     * Test, if updating a non existing state throws an UpdateException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStateFails() throws ValidationException, UpdateException {
        when(validator.validate(state)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(state.getId())).thenReturn(null);

        service.update(state);
    }


    /**
     * Test, if persisting a state while updating throws an PersistenceException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     * @throws PersistenceException If persisting of the state fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStateFailsPersistence() throws ValidationException, UpdateException, PersistenceException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();
        doThrow(PersistenceException.class).when(repository).saveAndFlushAndRefresh(state);

        service.update(state);
    }

    /**
     * Tests, if deleting an non existing state throws a DeletionException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws DeletionException If deletion of the state fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStateFails() throws ValidationException, DeletionException {
        setUpExecutingUser();

        when(validator.validate(state)).thenReturn(true);
        when(repository.findBy(state.getId())).thenReturn(null);

        service.delete(state);
    }

    /**
     * Tests, if updating of an state, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(validator.validate(state)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(null);

        service.update(state);
    }

    /**
     * Tests, if deleting of a state, without an executing user, throws a DeletionException.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws DeletionException If deleting of the state fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws ValidationException, DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.delete(state);
    }

    /**
     * Tests, if creation of an state returns the state.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws CreationException If creation of the state fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateState() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assertEquals(state, service.create(state));
    }

    /**
     * Tests, if updating of an state returns the new state.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws UpdateException If updating of the state fails.
     */
    @Test
    public void testUpdateState() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.update(state);
        verify(repository, times(1)).saveAndFlushAndRefresh(state);
    }

    /**
     * Tests, if deleting an state works without exceptions.
     *
     * @throws ValidationException If validation of the state fails.
     * @throws DeletionException If deletion of the state fails.
     */
    @Test
    public void testDeleteState() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(state);
    }

    /**
     * Tests, if getByName returns a list of all states referring to a name.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByName() throws FindByException {
        List<State> stateCollection = new ArrayList<>();
        stateCollection.add(state);
        when(repository.findByName("TestName")).thenReturn(stateCollection);

        assertEquals(stateCollection, service.getByName("TestName"));
    }

    /**
     * Tests, if getByName throws an exception if name is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByNameFails() throws FindByException {
        service.getByName(null);
    }

    /**
     * Tests, if getByBlocking returns a list of states that block a workstation.
     *
     * @throws FindByException If findBy fails.
     */
    @Test
    public void testGetByBlocking() throws FindByException {
        List<State> stateCollection = new ArrayList<>();
        stateCollection.add(state);
        when(repository.findByBlocking(true)).thenReturn(stateCollection);

        assertEquals(stateCollection, service.getByBlocking(true));
    }

    /**
     * Tests, if getByBlocking throws an exception if blocking is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByBlockingFails() throws FindByException {
        service.getByBlocking(null);
    }

    /**
     * Test fails if a given state is not the last executable state in a given stateMachine.
     */
    @Test
    public void testIsLastExecutableStateFalse() {
        when(stateMachine.getStateList()).thenReturn(states);
        when(states.indexOf(state)).thenReturn(8);
        when(states.size()).thenReturn(10);
        when(state.getName()).thenReturn("Transport");
        when(states.get(9)).thenReturn(state);
        when(states.get(9).getName()).thenReturn("Transport");

        assertEquals(false, service.isLastExecutableState(state, stateMachine));
    }

    /**
     * Test fails if a given state is null.
     */
    @Test
    public void testIsLastExecutableStateNull() {
        state = null;

        assertEquals(false, service.isLastExecutableState(null, stateMachine));
    }

    /**
     * Tests, if a given State is the last state of the stateMachine.
     */
    @Test
    public void testIsLastState() {
        when(stateMachine.getStateList()).thenReturn(states);
        when(states.indexOf(state)).thenReturn(10);
        when(states.size()).thenReturn(11);

        assertEquals(true, service.isLastState(state, stateMachine));
    }

    /**
     * Test fails if a given state is null.
     */
    @Test
    public void testIsLastStateFalse() {
        state = null;

        assertEquals(false, service.isLastState(null, stateMachine));
    }

    /**
     * Tests, if a given State of a procedure is completed.
     */
    @Test
    public void testIsComplete() {
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(state);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(states.indexOf(state)).thenReturn(9).thenReturn(10);
        when(stateExecs.size()).thenReturn(10);
        int indexOfCurrentState = 10;
        when(stateExecs.get(indexOfCurrentState)).thenReturn(stateExec);
        when(stateExec.getFinishedAt()).thenReturn(null);

        assertEquals(true, service.isComplete(state, procedure));
    }

    /**
     * Tests, if a given State of a procedure is completed.
     */
    @Test
    public void testIsCompleteTrue() {
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(state);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(states.indexOf(state)).thenReturn(9).thenReturn(10);
        when(stateExecs.size()).thenReturn(10);
        int indexOfCurrentState = 10;
        when(stateExecs.get(indexOfCurrentState)).thenReturn(stateExec);
        when(stateExec.getFinishedAt()).thenReturn(LocalDateTime.now());

        assertEquals(true, service.isComplete(state, procedure));
    }

    /**
     * Test returns false, because stateExecs is null.
     */
    @Test
    public void testIsCompleteStateExecsNull() {
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(state);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(null);

        assertEquals(false, service.isComplete(state, procedure));
    }

    /**
     * Test returns false, because stateHistory is null.
     */
    @Test
    public void testIsCompleteStateHistoryNull() {
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(state);
        when(procedure.getStateHistory()).thenReturn(null);

        assertEquals(false, service.isComplete(state, procedure));
    }

    /**
     * Test returns false, because state is null.
     */
    @Test
    public void testIsCompleteCurrentStateNull() {
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(null);

        assertEquals(false, service.isComplete(state, procedure));
    }

    /**
     * Test returns false, because state is null.
     */
    @Test
    public void testIsCompleteStateNull() {
        state = null;

        assertEquals(false, service.isComplete(null, procedure));
    }

    /**
     * Tests, if a given State of a procedure is pending.
     */
    @Test
    public void testIsPending() {
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(state);
        when(states.indexOf(state)).thenReturn(9);
        State currentState = state;
        when(stateExecs.size()).thenReturn(10);
        when(stateExecs.get(stateExecs.size() - 1)).thenReturn(stateExec);
        when(stateExec.getStartedAt()).thenReturn(LocalDateTime.now());

        assertEquals(true, service.isPending(state, procedure));
    }

    /**
     * Test returns false when current state is null.
     */
    @Test
    public void testIsPendingCurrentStateNull() {
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(procedure.getProcessStep()).thenReturn(processStep);
        when(processStep.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getStateList()).thenReturn(states);
        when(procedureService.getCurrentState(procedure)).thenReturn(null);

        assertEquals(false, service.isPending(state, procedure));
    }

    /**
     * Test returns false when List of stateExecs is null.
     */
    @Test
    public void testIsPendingStateExecsNull() {
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistory.getStateExecs()).thenReturn(null);

        assertEquals(false, service.isPending(state, procedure));
    }

    /**
     * Test returns false when stateHistory is null.
     */
    @Test
    public void testIsPendingStateHistoryNull() {
        when(procedure.getStateHistory()).thenReturn(null);

        assertEquals(false, service.isPending(state, procedure));
    }

    /**
     * Test returns false when state is null.
     */
    @Test
    public void testIsPendingStateNull() {
        state = null;

        assertEquals(false, service.isPending(null, procedure));
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        assertEquals(repository.findAll(), service.getAll());
    }
}
