package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.persistence.AssemblyRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.AssemblyValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class to test {@link AssemblyService}
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class AssemblyServiceTest {

    /**
     * Injected instance of assemblyService
     */
    @InjectMocks
    private AssemblyService assemblyService;

    /**
     * Mocked version of assemblyValidator
     */
    @Mock
    private AssemblyValidator assemblyValidator;

    /**
     * Mocked version of assemblyRepository
     */
    @Mock
    private AssemblyRepository assemblyRepository;

    /**
     * Mocked version of userService
     */
    @Mock
    private UserService userService;

    /**
     * Mocked version of jobService
     */
    @Mock
    private JobService jobService;

    /**
     * Mocked version of user
     */
    @Mock
    private User user;

    /**
     * Mocked version of the assembly entity
     */
    @Mock
    private Assembly assembly;

    /**
     * Sets up tests.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Sets up tests for splitting
     */
    private void setUpSplitting() {
        when(assembly.getAssemblyID()).thenReturn("abc.1.2.3");
        when(assembly.getSampleCount()).thenReturn(10);
    }

    /**
     * Setup method for create, update and delete methods
     *
     * @throws ValidationException         when validation fails
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    private void setUpCreateUpdateDelete() throws ValidationException, OccupiedAssemblyIdException {
        when(assemblyValidator.validate(assembly)).thenReturn(true);
        when(assemblyRepository.findBy(assembly.getId())).thenReturn(assembly);
        when(assemblyRepository.save(assembly)).thenReturn(assembly);
    }

    /**
     * Sets up the executing user
     */
    private void setUpExecutingUser() {
        when(userService.getExecutingUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("TestUser");
    }

    /**
     * Tests, if creation of a null object throws a CreationException.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws CreationException If creation of the assembly fails.
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException {
        when(assemblyValidator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        assemblyService.create(null);
        verify(assemblyValidator, times(1)).validate(null);
    }

    /**
     * Tests, if creation of an assembly returns the assembly.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws CreationException If creation of the assembly fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testCreateAssembly() throws ValidationException, CreationException, OccupiedAssemblyIdException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();
        when(assembly.getAssemblyID()).thenReturn("1.1");
        when(assemblyRepository.saveAndFlushAndRefresh(any())).thenReturn(assembly);

        assertEquals(assembly, assemblyService.create(assembly));
    }

    /**
     * Tests whether creation of an assembly throws a CreationException when validation fails.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws CreationException If creation of the assembly fails.
     */
    @Test(expected = CreationException.class)
    public void testInvalidCreate() throws ValidationException, CreationException {
        when(assemblyValidator.validate(any())).thenReturn(false);
        when(assembly.getAssemblyID()).thenReturn("1.1");
        assemblyService.create(assembly);
    }

    /**
     * Test fail when save in repo.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the deletion exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveInRepoFail()
            throws ValidationException, CreationException {
        Assembly ass = new Assembly();
        ass.setAssemblyID("1.1");
        doThrow(PersistenceException.class).when(assemblyRepository).saveAndFlushAndRefresh(any());
        when(assemblyValidator.validate(any())).thenReturn(true);

        assemblyService.create(ass);
    }

    /**
     * Tests whether update of an assembly throws an Exception when validation fails.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws UpdateException If creation of the assembly fails.
     */
    @Test(expected = UpdateException.class)
    public void testInvalidUpdate() throws ValidationException, UpdateException {
        when(assemblyValidator.validate(any())).thenReturn(false);
        when(assembly.getAssemblyID()).thenReturn("1.1");

        assemblyService.update(assembly);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws UpdateException If updating of the assembly fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFails() throws ValidationException, UpdateException {
        when(assemblyValidator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        assemblyService.update(null);
        verify(assemblyValidator, times(1)).validate(null);
    }

    /**
     * Tests, if updating of an assembly returns the new assembly.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws UpdateException If updating of the assembly fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testUpdateAssembly() throws ValidationException, UpdateException, OccupiedAssemblyIdException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();
        when(assembly.getAssemblyID()).thenReturn("1.1");

        assemblyService.update(assembly);
        verify(assemblyRepository, times(1)).saveAndFlushAndRefresh(assembly);
    }

    /**
     * Tests, if updating of an assembly, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws UpdateException If updating of the assembly fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(assemblyValidator.validate(assembly)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(null);
        when(assembly.getAssemblyID()).thenReturn("1.1");

        assemblyService.update(assembly);
    }

    /**
     * Test, if updating a non existing assembly throws an UpdateException.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws UpdateException If updating of the assembly fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateNonExistingEntity() throws ValidationException, UpdateException {
        when(assemblyValidator.validate(assembly)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(assemblyRepository.findBy(assembly.getId())).thenReturn(null);
        when(assembly.getAssemblyID()).thenReturn("1.1");

        assemblyService.update(assembly);
    }

    /**
     * Test delete from repo fails.
     *
     * @throws DeletionException the deletion exception
     */
    @Test(expected = DeletionException.class)
    public void testDeleteFromRepoFail() throws DeletionException {
        Assembly ass = new Assembly();
        doThrow(PersistenceException.class).when(assemblyRepository).attachAndRemove(any());
        when(assemblyRepository.findBy(any())).thenReturn(new Assembly());
        assemblyService.delete(ass);
    }

    /**
     * Tests, if deleting an assembly works without exceptions.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws DeletionException If deletion of the assembly fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testDeleteAssembly() throws ValidationException, DeletionException, OccupiedAssemblyIdException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assemblyService.delete(assembly);
    }

    /**
     * Tests, if deleting of an assembly, without an executing user, throws a DeletionException.
     *
     * @throws DeletionException If deleting of the assembly fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws
            DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        assemblyService.delete(assembly);
    }

    /**
     * Tests, if deleting an non existing assembly throws a DeletionException.
     *
     * @throws ValidationException If validation of the assembly fails.
     * @throws DeletionException If deletion of the assembly fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteAssemblyFails() throws ValidationException, DeletionException {
        setUpExecutingUser();

        when(assemblyValidator.validate(assembly)).thenReturn(true);
        when(assemblyRepository.findBy(assembly.getId())).thenReturn(null);

        assemblyService.delete(assembly);
    }


    /**
     * Tests, if splitBySize with a null assembly throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitBySizeAssemblyNull() throws SplitException {
        assemblyService.splitBySize(null, 1);
    }

    /**
     * Tests, if splitByParts with a null assembly throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitByPartsAssemblyNull() throws SplitException {
        assemblyService.splitByParts(null, 2);
    }

    /**
     * Tests, if splitBySize, with size = 0, throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitBySizeSizeZero() throws SplitException {
        assemblyService.splitBySize(assembly, 0);
    }

    /**
     * Tests, if splitByParts, with parts = 0, throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitByPartsZeroPart() throws SplitException {
        assemblyService.splitByParts(assembly, 0);
    }

    /**
     * Tests, if splitBySize, with size greater than sampleCount, throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitBySizeGreaterSize() throws SplitException {
        setUpSplitting();
        assemblyService.splitBySize(assembly, 11);
    }

    /**
     * Tests, if splitByParts, with size greater than sampleCount, throws a SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitByPartsGreaterParts() throws SplitException {
        setUpSplitting();
        assemblyService.splitByParts(assembly, 11);
    }

    /**
     * Test, if splitBySize and splitByParts, with wrong formatted assemblyID, throw a
     * SplitException.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test(expected = SplitException.class)
    public void testSplitBySizeWrongAssemblyID() throws SplitException {
        when(assembly.getAssemblyID()).thenReturn("abc.1.2.def");
        when(assembly.getSampleCount()).thenReturn(10);

        assemblyService.splitBySize(assembly, 1);
    }

    /**
     * Tests, if splitBySize, with size = sampleCount (max size), returns the initial assembly.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test
    public void testSplitBySizeCountIsSize() throws SplitException {
        setUpSplitting();
        Assembly resultAssembly = assemblyService.splitBySize(assembly, 10).iterator().next();

        assertEquals(1, assemblyService.splitBySize(assembly, 10).size());
        assertEquals(assembly, resultAssembly);
    }

    /**
     * Tests, if splitByParts, with parts = 1 (min parts), returns the initial assembly.
     *
     * @throws SplitException If splitting of assembly fails.
     */
    @Test
    public void testSplitByPartsPartsIsOne() throws SplitException {
        setUpSplitting();
        Collection<Assembly> result = assemblyService.splitByParts(assembly, 1);
        Assembly resultAssembly = result.iterator().next();

        assertEquals(1, result.size());
        assertEquals(assembly, resultAssembly);
    }

    /**
     * Test, if splitBySize produces assemblies with correct AssemblyIDs.
     *
     * @throws SplitException If splitting of assembly fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSplitBySizeAssemblyID() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();

        Collection<Assembly> result = assemblyService.splitBySize(assembly, 3);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }
        assertEquals("abc.1.2.3", first.getAssemblyID());
        assertEquals("abc.1.2.6", second.getAssemblyID());
        assertEquals("abc.1.2.12", Objects.requireNonNull(last).getAssemblyID());
    }

    /**
     * Test, if splitByParts produces assemblies with correct AssemblyIDs.
     *
     * @throws SplitException if splitting of assembly fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSplitByPartsAssemblyID() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();

        Collection<Assembly> result = assemblyService.splitByParts(assembly, 4);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }
        assertEquals("abc.1.2.3", first.getAssemblyID());
        assertEquals("abc.1.2.6", second.getAssemblyID());
        assertEquals("abc.1.2.12", Objects.requireNonNull(last).getAssemblyID());
    }

    /**
     * Tests, if splitBySize produces assemblies with correct sampleCount.
     *
     * @throws SplitException if splitting of assembly fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSplitBySizeSampleCount() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();

        Collection<Assembly> result = assemblyService.splitBySize(assembly, 3);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }

        assertEquals(3, first.getSampleCount());
        assertEquals(3, second.getSampleCount());
        assertEquals(1, Objects.requireNonNull(last).getSampleCount());
    }

    /**
     * Tests, if splitByParts produces assemblies with correct sampleCount.
     *
     * @throws SplitException if splitting of assembly fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSplitByPartsSampleCount() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();

        Collection<Assembly> result = assemblyService.splitByParts(assembly, 4);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }

        assertEquals(3, first.getSampleCount());
        assertEquals(3, second.getSampleCount());
        assertEquals(1, Objects.requireNonNull(last).getSampleCount());
    }


    /**
     * Tests, if getAll returns a list of all assemblies.
     */
    @Test
    public void testGetAllNormal() {
        List<Assembly> assemblyCollection = new ArrayList<>();
        assemblyCollection.add(assembly);
        when(assemblyRepository.findAll()).thenReturn(assemblyCollection);

        assertEquals(assemblyCollection, assemblyService.getAll());
    }

    /**
     * Tests, if getBySampleCount finds a collection of matching assemblies.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetBySampleCountNormal() throws FindByException {
        List<Assembly> assemblyCollection = new ArrayList<>();
        assemblyCollection.add(assembly);
        when(assemblyRepository.findBySampleCount(1)).thenReturn(assemblyCollection);

        assertEquals(assemblyCollection, assemblyService.getBySampleCount(1));
    }

    /**
     * Tests, if getBySampleCount, with a negative sampleCount, throws a FindByException
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetBySampleCountFail() throws FindByException {
        assemblyService.getBySampleCount(-1);
    }

    /**
     * Tests, if getByAlloy finds a collection of matching assemblies.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByAlloyNormal() throws FindByException {
        List<Assembly> assemblyCollection = new ArrayList<>();
        assemblyCollection.add(assembly);
        when(assemblyRepository.findByAlloy("alloy")).thenReturn(assemblyCollection);

        assertEquals(assemblyCollection, assemblyService.getByAlloy("alloy"));
    }

    /**
     * Tests, if getByAlloy, with a null string, throws a FindByException
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByAlloyFail() throws FindByException {
        assemblyService.getByAlloy(null);
    }

    /**
     * Tests, if separate produces the amount of assemblies equal to the initial assemblies
     * sampleCount.
     *
     * @throws SplitException If splitting fails.
     * @throws ValidationException the validation exception
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSeparateNormal() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();

        Collection<Assembly> result = assemblyService.separate(assembly);

        assertEquals(10, result.size());
    }

    /**
     * Tests, if separate produces assemblies with correct assemblyIDs.
     *
     * @throws SplitException If splitting fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException the occupied assembly id exception
     */
    @Test
    public void testSeparateAssemblyID() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        Collection<Assembly> result = assemblyService.separate(assembly);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }

        assertEquals("abc.1.2.3", first.getAssemblyID());
        assertEquals("abc.1.2.4", second.getAssemblyID());
        assertEquals("abc.1.2.12", Objects.requireNonNull(last).getAssemblyID());
    }

    /**
     * Tests, if separate produces assemblies with correct sampleCounts.
     *
     * @throws SplitException If splitting fails.
     * @throws ValidationException If validation fails.
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test
    public void testSeparateSampleCount() throws SplitException, ValidationException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        Collection<Assembly> result = assemblyService.separate(assembly);
        Iterator<Assembly> assemblyIterator = result.iterator();

        //Get first assembly from collection
        Assembly first = assemblyIterator.next();
        //Get second assembly form collection
        Assembly second = assemblyIterator.next();

        //Get last assembly from collection
        Assembly last = null;
        for (Assembly a : result) {
            last = a;
        }

        assertEquals(1, first.getSampleCount());
        assertEquals(1, second.getSampleCount());
        assertEquals(1, Objects.requireNonNull(last).getSampleCount());
    }

    /**
     * Test split with no executing user null
     *
     * @throws ValidationException the validation exception
     * @throws SplitException the split exception
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test(expected = SplitException.class)
    public void testExecutingUserNull() throws ValidationException, SplitException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        when(this.assembly.getSampleCount()).thenReturn(20);
        when(userService.getExecutingUser()).thenReturn(null);
        assemblyService.splitBySize(assembly, 10);
    }

    /**
     * Test split with no executing user null
     *
     * @throws ValidationException the validation exception
     * @throws SplitException the split exception
     * @throws OccupiedAssemblyIdException If assemblyId range is already occupied.
     */
    @Test(expected = SplitException.class)
    public void testLockingExceptions() throws ValidationException, SplitException, OccupiedAssemblyIdException {
        setUpSplitting();
        setUpCreateUpdateDelete();
        setUpExecutingUser();
        when(this.assembly.getSampleCount()).thenReturn(20);
        doThrow(new OptimisticLockException()).when(assemblyRepository).attachAndRemove(assembly);
        assemblyService.splitBySize(assembly, 10);
    }

    /**
     * Test getProcessing method of assembly service
     */
    @Test
    public void testGetProcessingNoCarriers() {
        ArrayList<Assembly> assemblies = new ArrayList<>();
        Job job = mock(Job.class);
        for (int i = 0; i < 10; i++) {
            Assembly assembly = mock(Assembly.class);
            assemblies.add(assembly);
            when(jobService.getByAssembly(assembly)).thenReturn(job);
        }
        when(jobService.isActive(any())).thenReturn(false);
        when(assemblyRepository.findAll()).thenReturn(assemblies);
        List<Assembly> result = assemblyService.getProcessing();
        assertEquals(0, result.size());
    }

    /**
     * Test getProcessing method of assembly service
     */
    @Test
    public void testGetProcessingNormal() {
        ArrayList<Assembly> assemblies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Job job = mock(Job.class);
            Assembly assembly = mock(Assembly.class);
            assemblies.add(assembly);
            when(jobService.getByAssembly(assembly)).thenReturn(job);
            // half of the jobs are active
            when(jobService.isActive(job)).thenReturn(i % 2 == 0);
        }
        when(assemblyRepository.findAll()).thenReturn(assemblies);
        List<Assembly> result = assemblyService.getProcessing();
        assertEquals(5, result.size());
    }

    /**
     * Test getStored with no carriers
     */
    @Test
    public void testGetStoredNoCarriers() {
        ArrayList<Assembly> assemblies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Assembly assembly = mock(Assembly.class);
            when(assembly.getCarriers()).thenReturn(new ArrayList<>());
        }
        when(assemblyRepository.findAll()).thenReturn(assemblies);
        List<Assembly> result = assemblyService.getInStock();
        assertEquals(0, result.size());
    }

    /**
     * Test getStored with null carriers
     */
    @Test
    public void testGetStoredNullCarriers() {
        ArrayList<Assembly> assemblies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Assembly assembly = mock(Assembly.class);
            when(assembly.getCarriers()).thenReturn(null);
        }
        when(assemblyRepository.findAll()).thenReturn(assemblies);
        List<Assembly> result = assemblyService.getInStock();
        assertEquals(0, result.size());
    }

    /**
     * Test getStored with no stored assemblies
     */
    @Test
    public void testGetStoredNoLocation() {
        ArrayList<Assembly> assemblies = new ArrayList<>();
        ArrayList<Carrier> carriers = new ArrayList<Carrier>();
        for (int i = 0; i < 10; i++) {
            Assembly assembly = mock(Assembly.class);
            Carrier carrier = mock(Carrier.class);
            when(carrier.getLocation()).thenReturn(null);
            when(assembly.getCarriers()).thenReturn(carriers);
            assemblies.add(assembly);
            carriers.add(carrier);
        }
        when(assemblyRepository.findAll()).thenReturn(assemblies);
        List<Assembly> result = assemblyService.getInStock();
        assertEquals(0, result.size());
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        Assembly resAssembly = new Assembly();
        User testUser = new User();
        testUser.setUsername("HansWurscht");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(assemblyRepository.findBy("testId")).thenReturn(resAssembly);
        assertEquals(assemblyService.getById("testId"), resAssembly);
        verify(assemblyRepository, times(1)).findBy(any());
    }

    /**
     * Test get by id invalid id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdInvalidId() throws InvalidIdException {
        assemblyService.getById(null);
    }

    /**
     * Test get by id repo fail.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test(expected = InvalidIdException.class)
    public void testGetByIdRepoFail() throws InvalidIdException {
        doThrow(PersistenceException.class).when(assemblyRepository).findBy(any());
        assemblyService.getById("testId");
    }

    /**
     * Test verify assembly id.
     *
     * @throws OccupiedAssemblyIdException the occupied assembly id exception
     */
    @Test
    public void testVerifyAssemblyId() throws OccupiedAssemblyIdException {
        setUpSplitting();
        List<Assembly> assemblyList = new ArrayList<>();
        assemblyList.add(assembly);
        when(assemblyRepository.findAssemblyByAssemblyID(any())).thenReturn(assemblyList);

        assemblyService.verifyAssemblyId(assembly);
    }
}