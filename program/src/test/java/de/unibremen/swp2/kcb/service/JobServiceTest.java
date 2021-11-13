package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.persistence.JobRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.JobValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test Class to Test {@link JobService}
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
public class JobServiceTest {

    /**
     * Injected instance of JobService
     */
    @InjectMocks
    private JobService service;

    /**
     * Mocked version of JobValidator
     */
    @Mock
    private JobValidator validator;

    /**
     * Mocked version of JobRepository
     */
    @Mock
    private JobRepository repository;

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
     * Mocked version of ProcessChain
     */
    @Mock
    private ProcessChain processChain;

    /**
     * Mocked version of Procedure
     */
    @Mock
    private Procedure procedure;

    /**
     * Mocked version of StateHistory
     */
    @Mock
    private StateHistory stateHistory;

    /**
     * Mocked version of StateHistoryController
     */
    @Mock
    private StateHistoryService stateHistoryService;

    /**
     * Mocked version of ProcedureService
     */
    @Mock
    private ProcedureService procedureService;

    /**
     * Mocked version of Assembly, two instances
     */
    @Mock
    private Assembly assembly1, assembly2;

    /**
     * Mocked version of the job entity
     */
    @Mock
    private Job job;

    /**
     * Mocked version of Transport
     */
    @Mock
    private Transport transport;

    /**
     * Sets up method.
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
        when(validator.validate(job)).thenReturn(true);
        when(repository.findBy(job.getId())).thenReturn(job);
        when(repository.save(job)).thenReturn(job);
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
     * @throws ValidationException If validation of the job fails.
     * @throws CreationException If creation of the job fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateValidationFails() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.create(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests, if creation of an job returns the job.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws CreationException If creation of the job fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test
    public void testCreateJob() throws ValidationException, CreationException, EntityAlreadyExistingException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        assertEquals(job, service.create(job));
    }

    /**
     * Tests whether creation of a job throws a CreationException when validation fails.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws CreationException If creation of the job fails.
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testInvalidCreate() throws ValidationException, CreationException, EntityAlreadyExistingException {
        when(validator.validate(any())).thenReturn(false);
        when(job.getId()).thenReturn("1.1");
        service.create(job);
    }

    /**
     * Test fail when save in repo.
     *
     * @throws ValidationException the validation exception
     * @throws CreationException the deletion exception
     * @throws EntityAlreadyExistingException the entity already existing exception
     */
    @Test(expected = CreationException.class)
    public void testCreateSaveInRepoFail()
            throws ValidationException, CreationException, EntityAlreadyExistingException {
        job.setId("1.1");
        doThrow(PersistenceException.class).when(repository).save(any());
        when(validator.validate(any())).thenReturn(true);
        service.create(job);
    }

    /**
     * Tests, if updating of a null object throws a UpdateException.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws UpdateException If updating of the job fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateValidationFails() throws ValidationException, UpdateException {
        when(validator.validate(null)).thenThrow(new ValidationException("Entity was null."));
        service.update(null);
        verify(validator, times(1)).validate(null);
    }

    /**
     * Tests whether creation of a job throws a CreationException when validation fails.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws UpdateException If update of the job fails.
     */
    @Test(expected = UpdateException.class)
    public void testInvalidUpdate() throws ValidationException, UpdateException {
        when(validator.validate(any())).thenReturn(false);
        when(job.getId()).thenReturn("1.1");
        service.update(job);
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
        job.setId("1.1");
        doThrow(PersistenceException.class).when(repository).saveAndFlushAndRefresh(any());
        when(validator.validate(any())).thenReturn(true);
        when(repository.findBy(any())).thenReturn(job);
        service.update(job);
    }

    /**
     * Tests, if updating of an job returns the new job.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws UpdateException If updating of the job fails.
     */
    @Test
    public void testUpdateJob() throws ValidationException, UpdateException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.update(job);
        verify(repository, times(1)).saveAndFlushAndRefresh(job);
    }

    /**
     * Tests, if updating of an job, without an executing user, throws a UpdateException.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws UpdateException If updating of the job fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateMissingUser() throws ValidationException, UpdateException {
        when(validator.validate(job)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(null);

        service.update(job);
    }

    /**
     * Test, if updating a non existing job throws an UpdateException.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws UpdateException If updating of the job fails.
     */
    @Test(expected = UpdateException.class)
    public void testUpdateNonExistingEntity() throws ValidationException, UpdateException {
        when(validator.validate(job)).thenReturn(true);
        when(userService.getExecutingUser()).thenReturn(user);
        when(repository.findBy(job.getId())).thenReturn(null);

        service.update(job);
    }

    /**
     * Tests, if deleting an job works without exceptions.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws DeletionException If deletion of the job fails.
     */
    @Test
    public void testDeleteJob() throws ValidationException, DeletionException {
        setUpExecutingUser();
        setUpCreateUpdateDelete();

        service.delete(job);
    }

    /**
     * Tests, if deleting of an job, without an executing user, throws a DeletionException.
     *
     * @throws DeletionException If deleting of the job fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteMissingUser() throws DeletionException {
        when(userService.getExecutingUser()).thenReturn(null);

        service.delete(job);
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
        job.setId("1.1");
        doThrow(PersistenceException.class).when(repository).attachAndRemove(any());
        when(validator.validate(any())).thenReturn(true);
        when(repository.findBy(any())).thenReturn(job);
        service.delete(job);
    }

    /**
     * Tests, if deleting an non existing job throws a DeletionException.
     *
     * @throws ValidationException If validation of the job fails.
     * @throws DeletionException If deletion of the job fails.
     */
    @Test(expected = DeletionException.class)
    public void testDeleteJobFails() throws ValidationException, DeletionException {
        setUpExecutingUser();

        when(validator.validate(job)).thenReturn(true);
        when(repository.findBy(job.getId())).thenReturn(null);

        service.delete(job);
    }

    /**
     * Tests, if getByName returns a list of all jobs referring to a name.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByNameNormal() throws FindByException {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(repository.findByName("TestName")).thenReturn(jobList);

        assertEquals(jobList, service.getByName("TestName"));
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
     * Tests, if getByProcessChain returns a list of all jobs referring to a processChain.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByProcessChainNormal() throws FindByException {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(repository.findByProcessChain(processChain)).thenReturn(jobList);

        assertEquals(jobList, service.getByProcessChain(processChain));
    }

    /**
     * Tests, if getByProcessChain throws an exception if processChain is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByProcessChainFails() throws FindByException {
        service.getByProcessChain(null);
    }

    /**
     * Tests, if getByProcedures returns a list of all jobs referring to a list of procedures.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test
    public void testGetByProceduresNormal() throws FindByException {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(repository.findByProcedures(procedureList)).thenReturn(jobList);

        assertEquals(jobList, service.getByProcedures(procedureList));
    }

    /**
     * Tests, if getByProcedures throws an exception if procedures is null.
     *
     * @throws FindByException If FindBy fails.
     */
    @Test(expected = FindByException.class)
    public void testGetByProceduresFails() throws FindByException {
        service.getByProcedures(null);
    }

    /**
     * Tests, if is Active returns true when the given Job is active.
     */
    @Test
    public void testIsActiveTrue() {
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(job.getProcedures()).thenReturn(procedureList);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(procedureService.isComplete(procedure)).thenReturn(false);
        when(job.getJobState()).thenReturn(JobState.PROCESSING);

        assertTrue(service.isActive(job));
    }

    @Test
    public void testIsActiveElse() {
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(job.getProcedures()).thenReturn(procedureList);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(procedureService.isComplete(procedure)).thenReturn(true);
        when(job.getJobState()).thenReturn(JobState.PROCESSING);

        assertFalse(service.isActive(job));
    }

    /**
     * Tests, if isActive returns false when the given Job is not active.
     */
    @Test
    public void testIsActiveFalse() {
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(job.getProcedures()).thenReturn(procedureList);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(procedureService.isComplete(procedure)).thenReturn(true);

        assertFalse(service.isActive(job));
    }

    /**
     * Test is active null.
     */
    @Test
    public void testIsActiveNull() {
        assertFalse(service.isActive(null));
    }

    /**
     * Tests, if returns a list of active jobs.
     */
    @Test
    public void testGetActiveNormal() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getProcedures()).thenReturn(procedureList);
        when(procedureService.isComplete(any())).thenReturn(false);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistoryService.isComplete(stateHistory)).thenReturn(false);
        when(job.getJobState()).thenReturn(JobState.PROCESSING);
        when(repository.findActiveJobs()).thenReturn(jobList);

        assertEquals(jobList, service.getActive());
    }

    /**
     * Tests, if getActive returns an empty List, if there are no active Jobs.
     */
    @Test
    public void testGetActiveEmpty() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Procedure> procedureList = new ArrayList<>();
        procedureList.add(procedure);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getProcedures()).thenReturn(procedureList);
        when(procedureService.isComplete(any())).thenReturn(true);
        when(procedure.getStateHistory()).thenReturn(stateHistory);
        when(stateHistoryService.isComplete(stateHistory)).thenReturn(true);
        when(job.getJobState()).thenReturn(JobState.PENDING);

        assertEquals(new ArrayList<>(), service.getActive());
    }

    /**
     * Test get pending normal.
     */
    @Test
    public void testGetPendingNormal() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Assembly> assemblyList = new ArrayList<>();
        assemblyList.add(assembly1);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getAssemblies()).thenReturn(assemblyList);
        when(job.getAssemblies()).thenReturn(null);

        assertEquals(jobList, service.getPending());
    }

    /**
     * Test is pending null.
     */
    @Test
    public void testIsPendingNull() {
        assertFalse(service.isPending(null));
    }

    /**
     * Test get pending multiple.
     */
    @Test
    public void testGetPendingMultiple() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Assembly> assemblyList = new ArrayList<>();
        assemblyList.add(assembly1);
        assemblyList.add(assembly2);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getAssemblies()).thenReturn(assemblyList);

        assertEquals(new ArrayList<>(), service.getPending());
    }

    /**
     * Test get pending existing id.
     */
    @Test
    public void testGetPendingExistingId() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Assembly> assemblyList = new ArrayList<>();
        assemblyList.add(assembly1);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getAssemblies()).thenReturn(assemblyList);
        when(job.getAssemblies().get(0).getAssemblyID()).thenReturn("TestID");

        assertEquals(new ArrayList<>(), service.getPending());
    }

    /**
     * Test get pending existing id and multiple.
     */
    @Test
    public void testGetPendingExistingIdAndMultiple() {
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        List<Assembly> assemblyList = new ArrayList<>();
        assemblyList.add(assembly1);
        assemblyList.add(assembly2);

        when(repository.findAll()).thenReturn(jobList);
        when(job.getAssemblies()).thenReturn(assemblyList);
        when(job.getAssemblies().get(0).getId()).thenReturn("TestID");

        assertEquals(new ArrayList<>(), service.getPending());
    }

    /**
     * Test start fail.
     *
     * @throws StartJobException the start job exception
     */
    @Test(expected = StartJobException.class)
    public void testStartFail() throws StartJobException {
        service.start(null);
    }

    /**
     * Test stop fail null.
     *
     * @throws StopException the stop exception
     */
    @Test(expected = StopException.class)
    public void testStopFailNull() throws StopException {
        service.stop(null);
    }

    /**
     * Test stop fail finished.
     *
     * @throws StopException the stop exception
     */
    @Test(expected = StopException.class)
    public void testStopFailFinished() throws StopException {
        when(job.getJobState()).thenReturn(JobState.FINISHED);

        service.stop(job);
    }

    /**
     * Test stop fail cancelled.
     *
     * @throws StopException the stop exception
     */
    @Test(expected = StopException.class)
    public void testStopFailCancelled() throws StopException {
        when(job.getJobState()).thenReturn(JobState.CANCELLED);

        service.stop(job);
    }

    /**
     * Test stop fail null state.
     *
     * @throws StopException the stop exception
     */
    @Test(expected = StopException.class)
    public void testStopFailNullState() throws StopException {
        when(job.getJobState()).thenReturn(null);

        service.stop(job);
    }

    /**
     * Test get all.
     */
    @Test
    public void testGetAll() {
        List<Job> result = new ArrayList<>();
        when(repository.findAll()).thenReturn(result);
        assertEquals(service.getAll(), result);
    }

    /**
     * Test needs collection false.
     */
    @Test
    public void testNeedsCollectionFalse() {
        assertFalse(service.needsCollection(null));
    }

    /**
     * Test needs collection.
     */
    @Test
    public void testNeedsCollection() {
        when(job.getJobState()).thenReturn(JobState.PROCESSING);
        when(procedureService.needsCollection(any())).thenReturn(true);
        assertTrue(service.needsCollection(job));
    }

    /**
     * Test needs delivery null.
     */
    @Test
    public void testNeedsDeliveryNull() {
        assertFalse(service.needsDelivery(null));
    }

    /**
     * Test needs delivery not processing.
     */
    @Test
    public void testNeedsDeliveryNotProcessing() {
        when(job.getJobState()).thenReturn(null);
        assertFalse(service.needsDelivery(job));
    }

    /**
     * Test get by id.
     *
     * @throws InvalidIdException the invalid id exception
     */
    @Test
    public void testGetById() throws InvalidIdException {
        User testUser = new User();
        testUser.setUsername("testUser");
        when(userService.getExecutingUser()).thenReturn(testUser);
        when(repository.findBy("testId")).thenReturn(job);
        assertEquals(service.getById("testId"), job);
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
     * Test get by priority.
     */
    @Test
    public void testGetByPriority() {
        service.getByPriority(new Priority());
        verify(repository,times(1)).findByPriority(any());
    }

    /**
     * Test get by procedure null.
     *
     * @throws FindByException the find by exception
     */
    @Test(expected = FindByException.class)
    public void testGetByProcedureNull() throws FindByException {
        assertNull(service.getByProcedure(null));
    }

    /**
     * Test by assembly null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetByAssemblyNull() {
        service.getByAssembly(null);
    }

    /**
     * Test get by assembly jobs null.
     */
    @Test
    public void testGetByAssemblyJobsNull() {
        when(repository.findByAssembly(assembly1)).thenReturn(null);
        assertNull(service.getByAssembly(assembly1));
    }

    /**
     * Test get currently running by workstation.
     */
    @Test
    public void testGetCurrentlyRunningByWorkstation() {
        service.getCurrentlyRunningByWorkstation(any());
        verify(repository, times(1)).findCurrentlyRunningByWorkstation(any());
    }

    /**
     * Test get current preparation.
     */
    @Test
    public void testGetCurrentPreparation() {
        assertNull(service.getCurrentPreparation(null));
    }

    /**
     * Test is cancelled and assembly not in stock.
     */
    @Test
    public void testIsCancelledAndAssemblyNotInStock() {
        assertFalse(service.isCancelledAndAssemblyNotInStock(null));
    }

    /**
     * Test is cancelled and assembly not in stock assembly null.
     */
    @Test
    public void testIsCancelledAndAssemblyNotInStockAssemblyNull() {
        when(job.getJobState()).thenReturn(JobState.PROCESSING);
        when(job.getAssemblies()).thenReturn(null);
        assertFalse(service.isCancelledAndAssemblyNotInStock(job));
    }

}
