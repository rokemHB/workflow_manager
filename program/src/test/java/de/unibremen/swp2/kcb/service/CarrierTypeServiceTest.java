package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.CarrierTypeRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.CarrierTypeValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class to test {@link AssemblyService}
 *
 * @author Robin
 */
public class CarrierTypeServiceTest {

    /**
     * Injected instance of carrerTypeService
     */
    @InjectMocks
    private CarrierTypeService service;

    /**
     * Mocked version of CarrierTypeRepository
     */
    @Mock
    private CarrierTypeRepository repository;

    /**
     * Mocked version of CarrierTypeValidator
     */
    @Mock
    private CarrierTypeValidator validator;

    /**
     * Mocked version of CarrierType
     */
    @Mock
    private CarrierType carrierType;

    /**
     * Mocked version of UserService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of User
     */
    @Mock
    private User user;

    /**
     * Mocked version of CarrierService
     */
    @Mock
    private CarrierService carrierService;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * SetUp method for create update and delete methods
     *
     * @throws ValidationException in case it fails
     */
    private void setUpCreateUpdateDelete() throws ValidationException {
        when(validator.validate(carrierType)).thenReturn(true);
        when(repository.findBy(carrierType.getId())).thenReturn(carrierType);
        when(repository.save(carrierType)).thenReturn(carrierType);
    }

    /**
     * Sets up an existing User
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Tests, if creation of a null object throws a CreationException.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws CreationException If creation of the carrierType fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.create(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests, if creation of an carrierType returns the carrierType.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws CreationException If creation of the carrierType fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateCarrierType() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();
        when(repository.findByName(any())).thenThrow(NoResultException.class);

        assertEquals(carrierType, service.create(carrierType));
    }

    /**
     * Tests whether creation of a carrierType throws a CreationException when validation fails.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws CreationException If creation of the carrierType fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testInvalidCreate() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(any())).thenReturn(false);
        when(carrierType.getId()).thenReturn("1.1");
        service.create(carrierType);
    }

    /**
     * Test fail when save in repo.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the deletion exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = EntityAlreadyExistingException.class)
    public void testCreateSaveInRepoFail()
            throws ValidationException, CreationException, EntityAlreadyExistingException {
        carrierType.setId("1.1");
        doThrow(PersistenceException.class).when(repository).save(any());
        when(validator.validate(any())).thenReturn(true);
        service.create(carrierType);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws UpdateException If updating of the carrierType fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFails() throws ValidationException, UpdateException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.update(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests whether creation of a carrierType throws a CreationException when validation fails.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws UpdateException If update of the carrierType fails.
     */
    @Test(expected = UpdateException.class)
    public void testInvalidUpdate() throws ValidationException, UpdateException {
        when(validator.validate(any())).thenReturn(false);
        when(carrierType.getId()).thenReturn("1.1");
        service.update(carrierType);
    }

    /**
     * Test fail when save in repo.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveInRepoFail()
            throws ValidationException, UpdateException {
        carrierType.setId("1.1");
        doThrow(PersistenceException.class).when(repository).save(any());
        when(validator.validate(any())).thenReturn(true);
        when(repository.findBy(any())).thenReturn(carrierType);
        service.update(carrierType);
    }

    /**
     * Tests, if updating of an carrierType returns the new carrierType.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws UpdateException If updating of the carrierType fails.
     */
    @Test
    public void testUpdateCarrierType() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.update(carrierType);
        verify(repository, times(1)).save(carrierType);
    }

    /**
     * Tests, if updating of an carrierType, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws UpdateException If updating of the carrierType fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(validator.validate(carrierType)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(null);

        service.update(carrierType);
    }

    /**
     * Test, if updating a non existing carrierType throws an UpdateException.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws UpdateException If updating of the carrierType fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateNonExistingEntity() throws ValidationException, UpdateException {
        when(validator.validate(carrierType)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(carrierType.getId())).thenReturn(null);

        service.update(carrierType);
    }

    /**
     * Tests, if deleting an carrierType works without exceptions.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws DeletionException If deletion of the carrierType fails.
     */
    @Test
    public void testDeleteCarrierType() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(carrierType);
    }

    /**
     * Tests, if deleting of an carrierType, without an executing user, throws a DeletionException.
     *
     * @throws DeletionException If deleting of the carrierType fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.delete(carrierType);
    }

    /**
     * Test fail when save in repo.
     *
     * @throws ValidationException the validation exception
     * @throws DeletionException the update exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFromRepoFail()
            throws ValidationException, DeletionException {
        carrierType.setId("1.1");
        doThrow(PersistenceException.class).when(repository).attachAndRemove(any());
        when(validator.validate(any())).thenReturn(true);
        when(repository.findBy(any())).thenReturn(carrierType);
        service.delete(carrierType);
    }

    /**
     * Tests, if deleting an non existing carrierType throws a DeletionException.
     *
     * @throws ValidationException If validation of the carrierType fails.
     * @throws DeletionException If deletion of the carrierType fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteCarrierTypeFails() throws ValidationException, DeletionException {
        setUpExecutingUser();

        when(validator.validate(carrierType)).thenReturn(true);
        when(repository.findBy(carrierType.getId())).thenReturn(null);

        service.delete(carrierType);
    }

    /**
     * Tests, if getByName returns a list of all carrierTypes referring to a name.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByNameNormal() throws FindByException {
        when(repository.findByName("TestName")).thenReturn(carrierType);

        assertEquals(carrierType, service.getByName("TestName"));
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
     * Test get by name fails repo null.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByNameFailsRepoNull() throws FindByException {
        when(repository.findByName("TestName")).thenReturn(null);
        service.getByName("TestName");
    }

    /**
     * Test get all fail.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetAllFail() throws FindByException {
        List<CarrierType> result = new ArrayList<>();
        when(repository.findAll()).thenReturn(result);
        assertEquals(service.getAll(), result);
    }

    /**
     * Test get all.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testGetAll() throws FindByException {
        List<CarrierType> result = new ArrayList<>();
        result.add(new CarrierType());
        when(repository.findAll()).thenReturn(result);
        assertEquals(service.getAll(), result);
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        CarrierType carrierType = new CarrierType();
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(repository.findBy("testId")).thenReturn(carrierType);
        assertEquals(service.getById("testId"), carrierType);
        verify(repository, times(1)).findBy(any());
    }

    /**
     * Test get by id invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidId() throws InvalidIdException {
        service.getById(null);
    }

    /**
     * Test get by id repo fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdRepoFail() throws InvalidIdException {
        doThrow(PersistenceException.class).when(repository).findBy(any());
        service.getById("testId");
    }


    /**
     * Test can delete.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testCanDelete() throws FindByException {
        List<Carrier> carrierList = new ArrayList<>();
        when(carrierService.getByCarrierType(new CarrierType())).thenReturn(carrierList);
        assertTrue(service.canDelete(new CarrierType()));
    }

    /**
     * Test can delete throw.
     *
     * @throws FindByException the find by exception
     */
    @Test
    public void testCanDeleteThrow() throws FindByException {
        doThrow(FindByException.class).when(carrierService).getByCarrierType(any());
        assertFalse(service.canDelete(new CarrierType()));
    }

    /**
     * Test can delete null.
     */
    @Test
    public void testCanDeleteNull() {
        assertFalse(service.canDelete(null));
    }
}