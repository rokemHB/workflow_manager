package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.statemachine.StateMachineRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.StateMachineValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link StateMachineService}
 */
public class StateMachineServiceTest {

    /**
     * Inject Instance of StateMachineService
     */
    @InjectMocks
    private StateMachineService stateMachineService;

    /**
     * Mocked version of stateMachine
     */
    @Mock
    private StateMachine stateMachine;

    /**
     * Mocked version of stateMachineValidator
     */
    @Mock
    private StateMachineValidator stateMachineValidator;

    /**
     * Mocked version of stateMachineRepository
     */
    @Mock
    private StateMachineRepository stateMachineRepository;

    /**
     * Mocked version of StateList
     */
    @Mock
    private List<State> stateList;

    /**
     * Mocked version of StateMachineList
     */
    @Mock
    private List<StateMachine> stateMachines;

    /**
     * Mocked version of stateService
     */
    @Mock
    private StateService stateService;

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
     * Tests, if creation of a stateMachine returns the stateMachine.
     *
     * @throws CreationException If creation of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     * @throws FindByException the find by exception
     */
    @Test
    public void testCreate() throws CreationException, ValidationException, PersistenceException, EntityAlreadyExistingException, FindByException {
        setUpExecutingUser();
        when(stateMachineValidator.validate(any())).thenReturn(true);
        when(stateMachineRepository.save(any())).thenReturn(stateMachine);
        when(stateService.getByName(any())).thenReturn(stateList);
        when(stateList.size()).thenReturn(1);
        assertEquals(stateMachine, stateMachineService.create(stateMachine));
    }

    /**
     * Test fail when persisting the stateMachine.
     *
     * @throws CreationException If creation of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateFails() throws CreationException, ValidationException, EntityAlreadyExistingException {
        doThrow(ValidationException.class).when(stateMachineValidator).validate(any());

        stateMachineService.create(stateMachine);
    }

    /**
     * Test fail when persisting the stateMachine.
     *
     * @throws CreationException If creation of the stateMachine fails.
     * @throws ValidationException the validation exception
     * @throws PersistenceException the persistence exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateFailsInvalid() throws CreationException, ValidationException, PersistenceException, EntityAlreadyExistingException {
        when(stateMachineValidator.validate(any())).thenReturn(false);

        stateMachineService.create(stateMachine);
    }

    /**
     * Test fail when persisting the stateMachine.
     *
     * @throws CreationException If creation of the stateMachine fails.
     * @throws ValidationException the validation exception
     * @throws PersistenceException If persisting of the stateMachine fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateFailsPersistence() throws CreationException, ValidationException, PersistenceException, EntityAlreadyExistingException {
        setUpExecutingUser();
        when(stateMachineValidator.validate(any())).thenReturn(true);
        doThrow(PersistenceException.class).when(stateMachineRepository).save(any());

        stateMachineService.create(stateMachine);
    }

    /**
     * Tests, if update of a stateMachine returns the stateMachine.
     *
     * @throws UpdateException If update of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test
    public void testUpdate() throws UpdateException, ValidationException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineValidator.validate(any())).thenReturn(true);
        when(stateMachineRepository.findBy(any())).thenReturn(stateMachine);
        when(stateMachineRepository.saveAndFlushAndRefresh(stateMachine)).thenReturn(stateMachine);

        assertEquals(stateMachine, stateMachineService.update(stateMachine));
    }

    /**
     * Test fail when update stateMachine.
     *
     * @throws UpdateException If update of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateFails() throws UpdateException, ValidationException, PersistenceException {
        doThrow(ValidationException.class).when(stateMachineValidator).validate(any());

        stateMachineService.update(stateMachine);
    }

    /**
     * Test fail when update stateMachine.
     *
     * @throws UpdateException If update of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateFailsInvalid() throws UpdateException, ValidationException, PersistenceException {
        when(stateMachineValidator.validate(any())).thenReturn(false);

        stateMachineService.update(stateMachine);
    }

    /**
     * Test fail when update stateMachine.
     *
     * @throws UpdateException If update of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateFailsUnstored() throws UpdateException, ValidationException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineValidator.validate(any())).thenReturn(true);
        when(stateMachineRepository.findBy(any())).thenReturn(null);

        stateMachineService.update(stateMachine);
    }

    /**
     * Test fail when update stateMachine.
     *
     * @throws UpdateException If update of the stateMachine fails.
     * @throws ValidationException If validation of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateFailsPersistence() throws UpdateException, ValidationException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineValidator.validate(any())).thenReturn(true);
        when(stateMachineRepository.findBy(any())).thenReturn(stateMachine);
        doThrow(PersistenceException.class).when(stateMachineRepository).saveAndFlushAndRefresh(stateMachine);

        stateMachineService.update(stateMachine);
    }

    /**
     * Tests, if delete of a stateMachine returns the stateMachine.
     *
     * @throws DeletionException If delete of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test
    public void testDelete() throws DeletionException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineRepository.findBy(any())).thenReturn(stateMachine);

        stateMachineService.delete(stateMachine);
        verify(stateMachineRepository, times(1)).attachAndRemove(any());
    }

    /**
     * Tests, if delete of a stateMachine returns the stateMachine.
     *
     * @throws DeletionException If delete of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFailsUnstored() throws DeletionException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineRepository.findBy(any())).thenReturn(null);

        stateMachineService.delete(stateMachine);
    }

    /**
     * Tests, if delete of a stateMachine returns the stateMachine.
     *
     * @throws DeletionException If delete of the stateMachine fails.
     * @throws PersistenceException If persisting of the stateMachine fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFailsPersistence() throws DeletionException, PersistenceException {
        setUpExecutingUser();
        when(stateMachineRepository.findBy(any())).thenReturn(stateMachine);
        doThrow(PersistenceException.class).when(stateMachineRepository).attachAndRemove(any());

        stateMachineService.delete(stateMachine);
    }

    /**
     * Gets all.
     */
    @Test
    public void getAll() {
        assertEquals(stateMachineRepository.findAll(), stateMachineService.getAll());
    }

    /**
     * Gets by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void getById() throws InvalidIdException {
        when(stateMachine.getId()).thenReturn("12345");
        when(stateMachineRepository.findBy(anyString())).thenReturn(stateMachine);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(stateMachine, stateMachineService.getById("12345"));
        verify(stateMachineRepository, times(1)).findBy("12345");
    }

    /**
     * Gets by id fails.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void getByIdFails() throws InvalidIdException {
        when(stateMachine.getId()).thenReturn("12345");
        when(stateMachineRepository.findBy(anyString())).thenThrow(PersistenceException.class);
        when(userService.getExecutingUser()).thenReturn(user);

        assertEquals(stateMachine, stateMachineService.getById("12345"));
        verify(stateMachineRepository, times(1)).findBy("12345");
    }

    /**
     * Gets by name.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void getByName() throws FindByException {
        String name = "name";
        when(stateMachineRepository.findByName(name)).thenReturn(stateMachines);

        assertEquals(stateMachines, stateMachineService.getByName(name));
    }

    /**
     * Gets by name fails.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void getByNameFails() throws FindByException {
        String name = "";

        assertEquals(stateMachines, stateMachineService.getByName(name));
    }
}
