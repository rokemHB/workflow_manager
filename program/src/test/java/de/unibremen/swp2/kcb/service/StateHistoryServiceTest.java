package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.statemachine.StateHistoryRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link StateHistoryService}
 *
 * @author Arvid
 */
public class StateHistoryServiceTest {

    /**
     * Injected Instance of StateHistoryService
     */
    @InjectMocks
    private StateHistoryService stateHistoryService;

    /**
     * Mocked version of stateHistory
     */
    @Mock
    private StateHistory stateHistory;

    /**
     * Mocked version of stateExec
     */
    @Mock
    private StateExec stateExec;

    /**
     * Mocked version of stateHistoryRepository
     */
    @Mock
    private StateHistoryRepository stateHistoryRepository;

    /**
     * Mocked version of ArrayList
     */
    @Mock
    private ArrayList<StateExec> mockArrayList;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of user
     */
    @Mock
    private User user;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Sets up the executing user
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Setup method for create, update and delete methods
     */
    private void setUpCreateUpdateDelete() {
        when(stateHistoryRepository.save(any())).thenReturn(stateHistory);
        when(stateHistoryRepository.findBy(any())).thenReturn(stateHistory);
    }

    /**
     * Tests, if creation of a stateHistory returns the stateHistory.
     *
     * @throws CreationException If creation of the stateHistory fails.
     * @throws PersistenceException If persisting of the stateHistory fails.
     */
    @Test
    public void testCreateStateHistory() throws CreationException, PersistenceException {
        setUpExecutingUser();
        when(stateHistoryRepository.save(any())).thenReturn(stateHistory);

        assertEquals(stateHistory, stateHistoryService.create(stateHistory));
    }

    /**
     * Test fail when persisting the stateHistory.
     *
     * @throws CreationException the creation exception
     * @throws PersistenceException If persisting of the stateHistory fails.
     */
    @Test(expected = CreationException.class)
    public void testCreateStateHistoryFailPersistence() throws CreationException, PersistenceException {
        doThrow(PersistenceException.class).when(stateHistoryRepository).save(any());

        stateHistoryService.create(stateHistory);
    }

    /**
     * Tests, if update of a stateHistory returns the stateHistory.
     *
     * @throws UpdateException If update of the stateHistory fails.
     * @throws PersistenceException the persistence exception
     */
    @Test
    public void testUpdateStateHistory() throws UpdateException, PersistenceException {
        setUpExecutingUser();
        when(stateHistoryRepository.findBy(any())).thenReturn(stateHistory);
        when(stateHistoryRepository.saveAndFlushAndRefresh(any())).thenReturn(stateHistory);

        assertEquals(stateHistory, stateHistoryService.update(stateHistory));
    }

    /**
     * Tests, fail if update of a stateHistory fails.
     *
     * @throws UpdateException If update of the stateHistory fails.
     * @throws PersistenceException the persistence exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStateHistoryFailUnstored() throws UpdateException, PersistenceException {
        setUpExecutingUser();
        when(stateHistoryRepository.findBy(any())).thenReturn(null);

        stateHistoryService.update(stateHistory);
    }

    /**
     * Tests, fail if update of a stateHistory fails.
     *
     * @throws UpdateException If update of the stateHistory fails.
     * @throws PersistenceException the persistence exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStateHistoryFailPersistence() throws UpdateException, PersistenceException {
        setUpExecutingUser();
        when(stateHistoryRepository.findBy(any())).thenReturn(stateHistory);
        doThrow(PersistenceException.class).when(stateHistoryRepository).saveAndFlushAndRefresh(any());

        stateHistoryService.update(stateHistory);
    }

    /**
     * Tests, if deleting a stateHistory works without exceptions.
     *
     * @throws DeletionException If deletion of the stateHistory fails.
     */
    @Test
    public void testDeleteStateHistory() throws DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        stateHistoryService.delete(stateHistory);
        verify(stateHistoryRepository, times(1)).attachAndRemove(stateHistory);
    }

    /**
     * Tests, fail of deleting stateHistory
     *
     * @throws DeletionException If deletion of the stateHistory fails.
     * @throws PersistenceException the persistence exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStateHistoryFailPersistence() throws DeletionException, PersistenceException {
        setUpExecutingUser();
        when(stateHistoryRepository.findBy(any())).thenReturn(stateHistory);
        doThrow(PersistenceException.class).when(stateHistoryRepository).attachAndRemove(any());

        stateHistoryService.delete(stateHistory);
    }

    /**
     * Tests, fail of deleting stateHistory
     *
     * @throws DeletionException If deletion of the stateHistory fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStateHistoryFailUnstored() throws DeletionException {
        setUpExecutingUser();
        when(stateHistoryRepository.findBy(any())).thenReturn(null);

        stateHistoryService.delete(stateHistory);
    }

    /**
     * Test is completed true.
     */
    @Test
    public void testIsCompletedTrue() {
        List<StateExec> stateExecList = new ArrayList<>();
        stateExecList.add(stateExec);

        when(stateExec.getFinishedAt()).thenReturn(LocalDateTime.now());
        when(stateHistory.getStateExecs()).thenReturn(stateExecList);

        assertEquals(true, stateHistoryService.isComplete(stateHistory));
    }

    /**
     * Test is completed false.
     */
    @Test
    public void testIsCompletedFalse() {
        List<StateExec> stateExecList = new ArrayList<>();
        stateExecList.add(stateExec);

        when(stateExec.getFinishedAt()).thenReturn(null);
        when(stateHistory.getStateExecs()).thenReturn(stateExecList);

        assertEquals(false, stateHistoryService.isComplete(stateHistory));
    }

    /**
     * Test is completed empty.
     */
    @Test
    public void testIsCompletedEmpty() {
        List<StateExec> stateExecList = new ArrayList<>();

        when(stateHistory.getStateExecs()).thenReturn(stateExecList);

        assertEquals(false, stateHistoryService.isComplete(stateHistory));
    }

    /**
     * Test is completed history null.
     */
    @Test
    public void testIsCompletedHistoryNull() {
        List<StateExec> stateExecList = new ArrayList<>();
        stateExecList.add(stateExec);
        stateHistory = null;

        assertEquals(false, stateHistoryService.isComplete(null));
    }

    /**
     * Test is completed execs null.
     */
    @Test
    public void testIsCompletedExecsNull() {
        when(stateHistory.getStateExecs()).thenReturn(null);

        assertEquals(false, stateHistoryService.isComplete(stateHistory));
    }

    /**
     * Test can finish.
     */
    @Test
    public void testCanFinish() {
        List<StateExec> stateExecs = mockArrayList;
        StateExec mockedStateExec = mock(StateExec.class);

        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(stateExecs.size()).thenReturn(2);
        when(mockedStateExec.getStartedAt()).thenReturn(LocalDateTime.now());
        when(mockedStateExec.getFinishedAt()).thenReturn(null);
        when(stateExecs.get(stateExecs.size() - 1)).thenReturn(mockedStateExec);

        assertEquals(true, stateHistoryService.canFinish(stateHistory));
    }

    /**
     * Test can finish history null.
     */
    @Test
    public void testCanFinishHistoryNull() {
        stateHistory = null;

        assertEquals(false, stateHistoryService.canFinish(null));
    }

    /**
     * Test can finish list empty.
     */
    @Test
    public void testCanFinishListEmpty() {
        when(stateHistory.getStateExecs()).thenReturn(new ArrayList<>());

        assertEquals(false, stateHistoryService.canFinish(stateHistory));
    }

    /**
     * Test can execute.
     */
    @Test
    public void testCanExecute() {
        List<StateExec> stateExecs = mockArrayList;
        StateExec mockedStateExec = mock(StateExec.class);

        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(stateExecs.size()).thenReturn(2);
        when(mockedStateExec.getStartedAt()).thenReturn(null);
        when(mockedStateExec.getFinishedAt()).thenReturn(null);
        when(stateExecs.get(stateExecs.size() - 1)).thenReturn(mockedStateExec);

        assertEquals(true, stateHistoryService.canExecute(stateHistory));
    }

    /**
     * Test can execute history null.
     */
    @Test
    public void testCanExecuteHistoryNull() {
        stateHistory = null;

        assertEquals(false, stateHistoryService.canExecute(null));
    }

    /**
     * Test can execute list empty.
     */
    @Test
    public void testCanExecuteListEmpty() {
        when(stateHistory.getStateExecs()).thenReturn(new ArrayList<>());

        assertEquals(false, stateHistoryService.canExecute(stateHistory));
    }

    /**
     * Test get current state exec.
     */
    @Test
    public void testGetCurrentStateExec() {
        List<StateExec> stateExecs = mockArrayList;
        StateExec mockedStateExec = mock(StateExec.class);

        when(stateExecs.size()).thenReturn(2);
        when(stateHistory.getStateExecs()).thenReturn(stateExecs);
        when(stateExecs.get(stateExecs.size() - 1)).thenReturn(stateExec);


        assertEquals(stateExec, stateHistoryService.getCurrentStateExec(stateHistory));
    }

    /**
     * Test get current state exec history null.
     */
    @Test
    public void testGetCurrentStateExecHistoryNull() {
        stateHistory = null;

        assertEquals(null, stateHistoryService.getCurrentStateExec(null));
    }

    /**
     * Test get current state exec list empty.
     */
    @Test
    public void testGetCurrentStateExecListEmpty() {
        when(stateHistory.getStateExecs()).thenReturn(new ArrayList<>());

        assertEquals(null, stateHistoryService.getCurrentStateExec(stateHistory));
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        assertEquals(stateHistoryRepository.findAll(), stateHistoryService.getAll());
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        when(stateHistory.getId()).thenReturn("12345");
        when(stateHistoryRepository.findBy(anyString())).thenReturn(stateHistory);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(stateHistory, stateHistoryService.getById("12345"));
        verify(stateHistoryRepository, times(1)).findBy("12345");
    }

    /**
     * Test get by id fails.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdFails() throws InvalidIdException {

        when(stateHistory.getId()).thenReturn("12345");
        when(stateHistoryRepository.findBy(anyString())).thenThrow(PersistenceException.class);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(stateHistory, stateHistoryService.getById("12345"));
        verify(stateHistoryRepository, times(1)).findBy("12345");
    }
}
