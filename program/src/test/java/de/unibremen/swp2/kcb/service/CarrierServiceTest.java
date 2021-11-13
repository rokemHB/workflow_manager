package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.CarrierRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link CarrierService}
 *
 * @author Robin
 * @author Arvid
 */
public class CarrierServiceTest {

    /**
     * Mocked verison of carrierTypeValidator
     */
    @Mock
    CarrierTypeValidator carrierTypeValidator;
    /**
     * Injected instance of carrierService
     */
    @InjectMocks
    private CarrierService carrierService;
    /**
     * Mocked version of carrierRepository
     */
    @Mock
    private CarrierRepository carrierRepository;
    /**
     * Mocked version of CarrierValidator
     */
    @Mock
    private CarrierValidator carrierValidator;
    /**
     * Mocked version of LocationValidator
     */
    @Mock
    private LocationValidator locationValidator;
    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;
    /**
     * Mocked version of userService
     */
    @Mock
    private TransportService transportService;
    /**
     * Mocked version of userService
     */
    @Mock
    private AssemblyService assemblyService;
    /**
     * Mocked version of userService
     */
    @Mock
    private Carrier carrier;

    /**
     * Mocked version of User
     */
    @Mock
    private User user;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreate() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(carrierRepository.findByCarrierID(any())).thenThrow(NoResultException.class);
        carrierService.create(carrier);
        verify(carrierRepository, times(1)).save(carrier);
    }

    /**
     * Test invalid create.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationException() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(carrierValidator.validate(any())).thenThrow(ValidationException.class);
        carrierService.create(new Carrier());
    }

    /**
     * Test create invalid.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateInvalid() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(carrierValidator.validate(any())).thenReturn(false);
        carrierService.create(new Carrier());
    }

    /**
     * Test create save in repo fail.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the creation exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = EntityAlreadyExistingException.class)
    public void testCreateSaveInRepoFail() throws ValidationException, CreationException, EntityAlreadyExistingException {
        Carrier carrier = new Carrier();
        doThrow(new PersistenceException("create failed")).when(carrierRepository).save(carrier);
        when(carrierValidator.validate(any())).thenReturn(true);
        carrierService.create(carrier);
    }

    /**
     * Test update.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test
    public void testUpdate() throws ValidationException, UpdateException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        carrierService.update(carrier);
        verify(carrierRepository, times(1)).saveAndFlushAndRefresh(carrier);
    }

    /**
     * Test update validation exception.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationException() throws ValidationException, UpdateException {
        when(carrierValidator.validate(any())).thenThrow(ValidationException.class);
        carrierService.update(new Carrier());
    }

    /**
     * Test update invalid.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateInvalid() throws UpdateException, ValidationException {
        when(carrierValidator.validate(any())).thenReturn(false);
        carrierService.update(new Carrier());
    }

    /**
     * Test update save in repo fail.
     *
     * @throws UpdateException the update exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateSaveInRepoFail() throws UpdateException, ValidationException {
        Carrier carrier = new Carrier();
        doThrow(new PersistenceException("create failed")).when(carrierRepository).saveAndFlushAndRefresh(carrier);
        when(carrierValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findBy(any())).thenReturn(new Carrier());
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
        carrierService.update(carrier);
    }

    /**
     * Test update stored value fail.
     *
     * @throws ValidationException the validation exception
     * @throws UpdateException the update exception
     */
    @Test(expected = UpdateException.class)
    public void testUpdateStoredValueFail() throws ValidationException, UpdateException {
        when(carrierValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findBy(any())).thenReturn(null);
        carrierService.update(new Carrier());
    }

    /**
     * Test delete.
     *
     * @throws DeletionException the deletion exception
     */
    @Test
    public void testDelete() throws DeletionException {
        User testUser = new User();
        testUser.setUsername("HansWurscht");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(carrierRepository.findBy(any())).thenReturn(new Carrier());
        doNothing().when(carrierRepository).attachAndRemove(any());
        carrierService.delete(new Carrier());
        verify(carrierRepository, times(1)).attachAndRemove(any());
    }

    /**
     * Test delete stored null.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteStoredNull() throws DeletionException {
        when(carrierRepository.findBy(any())).thenReturn(null);
        carrierService.delete(new Carrier());
    }

    /**
     * Test delete from repo fail.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFromRepoFail() throws DeletionException {
        when(carrierRepository.findBy(any())).thenReturn(new Carrier());
        doThrow(new PersistenceException("deleteFailed")).when(carrierRepository).attachAndRemove(any());
        carrierService.delete(new Carrier());
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Carrier carrier = new Carrier();
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(carrierRepository.findBy("testId")).thenReturn(carrier);
        assertEquals(carrierService.getById("testId"), carrier);
        verify(carrierRepository, times(1)).findBy(any());
    }

    /**
     * Test get by id invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidId() throws InvalidIdException {
        carrierService.getById(null);
    }

    /**
     * Test get by id repo fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdRepoFail() throws InvalidIdException {
        doThrow(PersistenceException.class).when(carrierRepository).findBy(any());
        carrierService.getById("testId");
    }

    /**
     * Test get by carrier type.
     *
     * @throws FindByException the find by exception
     * @throws ValidationException the validation exception
     * @throws FindByException     the find by exception
     */
    @Test
    public void testGetByCarrierType() throws FindByException, ValidationException {
        CarrierType carrierType = new CarrierType();
        carrierType.setName("carrierTypeNameTest");
        Carrier carrier = new Carrier();
        List<Carrier> result = new ArrayList<>();
        result.add(carrier);
        when(carrierTypeValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findByCarrierType(any())).thenReturn(result);
        assertEquals(carrierService.getByCarrierType(carrierType), result);
    }

    /**
     * Test get by carrier type validate exception.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByCarrierTypeValidateException() throws ValidationException,
            FindByException {
        when(carrierTypeValidator.validate(any())).thenThrow(ValidationException.class);
        carrierService.getByCarrierType(new CarrierType());
    }

    /**
     * Test get by carrier type invalid.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByCarrierTypeInvalid() throws ValidationException, FindByException {
        CarrierType carrierType = new CarrierType();
        carrierType.setName("testname");
        when(carrierTypeValidator.validate(carrierType)).thenReturn(false);
        carrierService.getByCarrierType(carrierType);
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<Carrier> result = new ArrayList<>();
        when(carrierRepository.findAll()).thenReturn(result);
        assertEquals(carrierService.getAll(), result);
    }

    /**
     * Test get by location.
     *
     * @throws ValidationNullPointerException the validation null pointer exception
     * @throws FindByException                the find by exception
     */
    @Test
    public void testGetByLocation() throws ValidationNullPointerException, FindByException {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        List<Carrier> carriers = new ArrayList<>();
        Workstation loc = new Workstation();
        when(locationValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findByLocation(loc)).thenReturn(carriers);

        assertEquals(carrierService.getByLocation(loc), carriers);
        verify(locationValidator, times(1)).validate(loc);
    }

    /**
     * Test get by location validation fail.
     *
     * @throws ValidationNullPointerException the validation null pointer exception
     * @throws FindByException                the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByLocationValidationFail() throws ValidationNullPointerException, FindByException {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        List<Carrier> carriers = new ArrayList<>();
        Workstation loc = new Workstation();
        when(locationValidator.validate(any())).thenThrow(ValidationNullPointerException.class);
        when(carrierRepository.findByLocation(loc)).thenReturn(carriers);

        assertEquals(carrierService.getByLocation(loc), carriers);
        verify(locationValidator, times(1)).validate(loc);
    }

    /**
     * Test get by carrier type and location.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException     the find by exception
     */
    @Test
    public void testGetByCarrierTypeAndLocation() throws ValidationException, FindByException {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        CarrierType carrierType = new CarrierType();
        List<Carrier> carriers = new ArrayList<>();
        Workstation loc = new Workstation();
        when(locationValidator.validate(any())).thenReturn(true);
        when(carrierTypeValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findByCarrierTypeAndLocation(carrierType, loc)).thenReturn(carriers);

        assertEquals(carrierService.getByCarrierTypeAndLocation(carrierType, loc), carriers);
        verify(locationValidator, times(1)).validate(loc);
    }

    /**
     * Test get by carrier type and location validation fail one.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException     the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByCarrierTypeAndLocationValidationFailOne() throws ValidationException, FindByException {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        CarrierType carrierType = new CarrierType();
        List<Carrier> carriers = new ArrayList<>();
        Workstation loc = new Workstation();
        when(locationValidator.validate(any())).thenReturn(true);
        when(carrierTypeValidator.validate(any())).thenThrow(ValidationNullPointerException.class);
        when(carrierRepository.findByCarrierTypeAndLocation(carrierType, loc)).thenReturn(carriers);

        assertEquals(carrierService.getByCarrierTypeAndLocation(carrierType, loc), carriers);
        verify(locationValidator, times(1)).validate(loc);
    }

    /**
     * Test get by carrier type and location validation fail two.
     *
     * @throws ValidationException the validation exception
     * @throws FindByException     the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByCarrierTypeAndLocationValidationFailTwo() throws ValidationException, FindByException {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        CarrierType carrierType = new CarrierType();
        List<Carrier> carriers = new ArrayList<>();
        Workstation loc = new Workstation();
        when(locationValidator.validate(any())).thenThrow(ValidationNullPointerException.class);
        when(carrierTypeValidator.validate(any())).thenReturn(true);
        when(carrierRepository.findByCarrierTypeAndLocation(carrierType, loc)).thenReturn(carriers);

        assertEquals(carrierService.getByCarrierTypeAndLocation(carrierType, loc), carriers);
        verify(locationValidator, times(1)).validate(loc);
    }

    /**
     * Test get unused.
     */
    @Test
    public void testGetUnused() {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        List<Carrier> carriers = new ArrayList<>();
        carriers.add(carrier);
        carriers.add(carrier2);
        when(carrierRepository.findUnusedCarriers()).thenReturn(carriers);
        assertEquals(carrierService.getUnusedCarriers(), carriers);
    }

    /**
     * Test get allb.
     */
    @Test
    public void testGetAllb() {
        Carrier carrier = new Carrier();
        Carrier carrier2 = new Carrier();
        List<Carrier> carriers = new ArrayList<>();
        carriers.add(carrier);
        carriers.add(carrier2);
        when(carrierRepository.findAll()).thenReturn(carriers);
        assertEquals(carrierService.getAll(), carriers);
    }

    /**
     * Test get assemblies.
     */
    @Test
    public void testGetAssemblies() {
        Carrier carrier = mock(Carrier.class);
        Carrier carrier2 = mock(Carrier.class);
        Assembly assembly = mock(Assembly.class);
        List<Carrier> carriers = new ArrayList<>();
        carriers.add(carrier);
        carriers.add(carrier2);
        List<Assembly> assemblies = new ArrayList<>();
        assemblies.add(assembly);
        when(assembly.getCarriers()).thenReturn(carriers);
        when(assemblyService.getAll()).thenReturn(assemblies);
        assertEquals(carrierService.getAssemblies(carrier), assemblies);
    }

    /**
     * Test can collect.
     */
    @Test
    public void testCanCollect() {
        when(carrier.getLocation()).thenReturn(new Workstation());
        when(transportService.getAll()).thenReturn(new ArrayList<>());
        assertTrue(carrierService.canCollect(carrier));
    }

    /**
     * Test can deliver.
     */
    @Test
    public void testCanDeliver() {
        when(carrier.getLocation()).thenReturn(new Workstation());
        when(transportService.getAll()).thenReturn(new ArrayList<>());
        assertFalse(carrierService.canDeliver(carrier));
    }

    /**
     * Test collect.
     *
     * @throws CollectingException the collecting exception
     * @throws ValidationException the validation exception
     * @throws UpdateException     the update exception
     */
    @Test
    public void testCollect() throws CollectingException, ValidationException, UpdateException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.collect(carrier, new Transport());
        verify(carrierRepository, times(1)).saveAndFlushAndRefresh(carrier);
    }

    /**
     * Test collect carrier null.
     *
     * @throws CollectingException the collecting exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = CollectingException.class)
    public void testCollectCarrierNull() throws CollectingException, ValidationException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.collect(null, new Transport());
    }

    /**
     * Test collect transport null.
     *
     * @throws CollectingException the collecting exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = CollectingException.class)
    public void testCollectTransportNull() throws CollectingException, ValidationException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.collect(carrier, null);
    }

    /**
     * Test collect update fail.
     *
     * @throws CollectingException the collecting exception
     * @throws ValidationException the validation exception
     */
    @Test(expected = CollectingException.class)
    public void testCollectUpdateFail() throws CollectingException, ValidationException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenThrow(ValidationException.class);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.collect(carrier, new Transport());
    }

    /**
     * Test deliver.
     *
     * @throws ValidationException the validation exception
     * @throws DeliveringException the delivering exception
     */
    @Test
    public void testDeliver() throws ValidationException, DeliveringException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.deliver(carrier, new Transport());
        verify(carrierRepository, times(1)).saveAndFlushAndRefresh(carrier);
    }

    /**
     * Test deliver carrier null.
     *
     * @throws ValidationException the validation exception
     * @throws DeliveringException the delivering exception
     */
    @Test(expected = DeliveringException.class)
    public void testDeliverCarrierNull() throws ValidationException, DeliveringException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.deliver(null, new Transport());
    }

    /**
     * Test deliver location null.
     *
     * @throws ValidationException the validation exception
     * @throws DeliveringException the delivering exception
     */
    @Test(expected = DeliveringException.class)
    public void testDeliverLocationNull() throws ValidationException, DeliveringException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenReturn(true);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        when(carrierRepository.save(carrier)).thenReturn(carrier);
        carrierService.deliver(carrier, null);
    }

    /**
     * Test deliver update fail.
     *
     * @throws ValidationException the validation exception
     * @throws DeliveringException the delivering exception
     */
    @Test(expected = DeliveringException.class)
    public void testDeliverUpdateFail() throws ValidationException, DeliveringException {
        when(carrierValidator.validate(any())).thenReturn(true);
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        Carrier carrier = new Carrier();
        when(carrierRepository.findBy(any())).thenReturn(carrier);
        when(carrierValidator.validate(carrier)).thenThrow(ValidationException.class);
        when(carrierRepository.findBy(carrier.getId())).thenReturn(carrier);
        //when(carrierRepository.saveAndFlushAndRefresh(carrier)).thenThrow(UpdateException.class);
        carrierService.deliver(carrier, new Transport());
    }

    /**
     * Test can delete.
     */
    @Test
    public void testCanDelete() {
        List<Assembly> carriers = new ArrayList<>();
        carriers.add(new Assembly());
        when(assemblyService.getByCarrier(carrier)).thenReturn(carriers);
        assertFalse(carrierService.canDelete(carrier));
    }

    /**
     * Test can delete null.
     */
    @Test
    public void testCanDeleteNull() {
        when(assemblyService.getByCarrier(carrier)).thenReturn(null);
        assertTrue(carrierService.canDelete(carrier));
    }

    /**
     * Test can delete empty.
     */
    @Test
    public void testCanDeleteEmpty() {
        when(assemblyService.getByCarrier(carrier)).thenReturn(Collections.emptyList());
        assertTrue(carrierService.canDelete(carrier));
    }
}
