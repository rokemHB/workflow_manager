package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.locations.WorkstationRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.WorkstationValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link WorkstationService}
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class WorkstationServiceTest {

    /**
     * Injected instance of service
     */
    @InjectMocks
    private WorkstationService service;

    /**
     * Mocked version of validator
     */
    @Mock
    private WorkstationValidator validator;

    /**
     * Mocked version of repository
     */
    @Mock
    private WorkstationRepository repository;

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
     * Mocked version of the workstation entity
     */
    @Mock
    private Workstation workstation;

    /**
     * Mocked version of superService
     */
    @Mock
    private Service superService;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * setup method for CUD methods
     * @throws ValidationException when validation fails
     */
    private void setUpCreateUpdateDelete() throws ValidationException {
        when(validator.validate(workstation)).thenReturn(true);
        when(repository.findBy(workstation.getId())).thenReturn(workstation);
        when(repository.save(workstation)).thenReturn(workstation);
    }

    /**
     * setup method for existing user
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * setup method of persistence fail
     */
    private void setUpPersistenceFail() {
        doThrow(PersistenceException.class).when(repository).save(workstation);
        doThrow(PersistenceException.class).when(repository).attachAndRemove(workstation);
    }

    /**
     * Tests, if creation of a null object throws a CreationException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws CreationException If creation of the workstation fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.create(null);
    }

    /**
     * Tests, if creation of an workstation returns the workstation.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws CreationException If creation of the workstation fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateWorkstation() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();
        assertEquals(workstation, service.create(workstation));
    }

    /**
     * Tests, if creation of an invalid workstation throws a CreationException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws CreationException If creation of the workstation fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateNotValid() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(any())).thenReturn(false);
        service.create(workstation);
    }

    /**
     * Tests, if create throws a creationException if saving the workstation fails.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws CreationException If creation of the workstation fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreatePersistenceFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpPersistenceFail();
        when(validator.validate(any())).thenReturn(true);
        service.create(workstation);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws UpdateException If updating of the workstation fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFail() throws ValidationException, UpdateException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.update(null);
    }

    /**
     * Tests, if updating of an workstation returns the new workstation.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws UpdateException If updating of the workstation fails.
     */
    @Test
    public void testUpdateWorkstation() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.update(workstation);
        verify(repository, times(1)).saveAndFlushAndRefresh(workstation);
    }

    /**
     * Tests, if updating of an workstation, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws UpdateException If updating of the workstation fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateNotValid() throws ValidationException, UpdateException {
        when(validator.validate(any())).thenReturn(false);
        service.update(workstation);
    }

    /**
     * Tests, if update throws a updateException if saving the workstation fails.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws UpdateException If creation of the workstation fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdatePersistenceFail() throws ValidationException, UpdateException {
        when(validator.validate(workstation)).thenReturn(true);
        when(repository.findBy(workstation.getId())).thenReturn(workstation);
        doThrow(PersistenceException.class).when(repository).saveAndFlushAndRefresh(workstation);
        service.update(workstation);
    }

    /**
     * Test, if updating a non existing workstation throws an UpdateException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws UpdateException If updating of the workstation fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateNonExistingEntity() throws ValidationException, UpdateException {
        when(validator.validate(workstation)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(workstation.getId())).thenReturn(null);

        service.update(workstation);
    }

    /**
     * Tests, if deleting an workstation works without exceptions.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws DeletionException If deletion of the workstation fails.
     */
    @Test
    public void testDeleteWorkstation() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(workstation);
    }

    /**
     * Tests, if deleting of an workstation, without an executing user, throws a DeletionException.
     *
     * @throws DeletionException If deleting of the workstation fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeletePersistenceFail() throws DeletionException {
        when(repository.findBy(workstation.getId())).thenReturn(workstation);
        doThrow(PersistenceException.class).when(repository).attachAndRemove(any());
        service.delete(workstation);
    }

    /**
     * Tests, if deleting an non existing workstation throws a DeletionException.
     *
     * @throws ValidationException If validation of the workstation fails.
     * @throws DeletionException If deletion of the workstation fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteNonExistingWorkstation() throws ValidationException, DeletionException {
        setUpExecutingUser();
        when(validator.validate(workstation)).thenReturn(true);
        when(repository.findBy(workstation.getId())).thenReturn(null);
        service.delete(workstation);
    }

    /**
     * Tests, if getById with a empty id throws an exception.
     *
     * @throws InvalidIdException if id is invalid.
     * @throws UnauthorizedException the unauthorized exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdValidationFail() throws InvalidIdException, UnauthorizedException {
        service.getById("");
    }

    /**
     * Tests, if getById throws an exception if finding fails.
     *
     * @throws InvalidIdException if id is invalid.
     * @throws UnauthorizedException the unauthorized exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdPersistenceFail() throws InvalidIdException, UnauthorizedException {
        doNothing().when(superService).checkId("TestID");
        doThrow(PersistenceException.class).when(repository).findBy(anyString());
        service.getById("TestID");
    }
}