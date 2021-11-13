package de.unibremen.swp2.kcb.service;

import com.google.gson.*;
import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.JobRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.JobValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.primefaces.model.UploadedFile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle Job.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Transactional
@ApplicationScoped
public class JobService implements Service<Job> {

    /**
     * Logger object of the JobService class
     */
    private static final Logger logger = LogManager.getLogger(JobService.class);

    /**
     * Injected instance of JobValidator
     */
    @Inject
    private JobValidator jobValidator;

    /**
     * Injected instance of JobRepository
     */
    @Inject
    private JobRepository jobRepository;

    /**
     * Injected instance of UserService
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of ProcedureService
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Injected instance of ProcessChainService
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * Injected instance of globalConfigService
     */
    @Inject
    private GlobalConfigService globalConfigService;

    /**
     * Injected instance of stateHistoryService
     */
    @Inject
    private StateHistoryService stateHistoryService;

    /**
     * Injected instance of AssemblyService
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Injected instance of WorkstationService
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * Injected instance of ParameterService
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Injected instance of CarrierService
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Injected instance of StockService
     */
    @Inject
    private StockService stockService;

    /**
     * Injected instance of TransportService
     */
    @Inject
    private TransportService transportService;

    /**
     * Injected instance of ValueService
     */
    @Inject
    private ValueService valueService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Job that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public Job create(Job entity) throws CreationException, EntityAlreadyExistingException {
        //Validating job
        boolean valid;
        try {
            logger.trace("Attempting to validate job \"{}\" ...", entity);
            valid = jobValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during job validation. Can't create job.");
            throw new CreationException("Can't create job: " + e.getMessage());
        }
        logger.trace("Validation of job \"{}\" completed without exceptions.", entity);
        entity = this.parseValues(entity);
        //Checking, if job is valid
        if (!valid) {
            final String message = "Job \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Job \"{}\" is valid.", entity);

        //Checking, if job is already stored in database
        final List<Job> storedJob = jobRepository.findByName(entity.getName());
        if (!storedJob.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create job: Entity is already stored in datasource");
        }

        //Saving job
        Job repoEntity;
        try {
            logger.trace("Attempting to save job \"{}\" ...", entity);
            repoEntity = jobRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting job \"{}\". Can't create job.", entity);
            throw new CreationException("Can't create job: " + e.getMessage());
        }

        logger.trace("Saving of job \"{}\" completed without exceptions.", entity);
        logger.info("Create job \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning job \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @throws UpdateException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    public Job update(Job entity) throws UpdateException {
        //Validating job
        boolean valid;
        try {
            logger.trace("Attempting to validate job \"{}\" ...", entity);
            valid = jobValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating job \"{}\". Can't update job", entity);
            throw new UpdateException("Can't update job: " + e.getMessage());
        }
        logger.trace("Validation of job \"{}\" completed without exceptions.", entity);
        entity = this.parseValues(entity);
        //Checking, if job is valid
        if (!valid) {
            final String message = "Job \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Job \"{}\" is valid.", entity);

        //Checking, if job is stored in database
        final Job storedJob = jobRepository.findBy(entity.getId());
        if (storedJob == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving job
        Job repoEntity;
        try {
            logger.trace("Attempting to save new job \"{}\" ...", entity);
            repoEntity = jobRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting job \"{}\". Can't update job.", entity);
            throw new UpdateException("Can't update job: " + e.getMessage());
        }

        logger.trace("Saving of job \"{}\" completed without exceptions.", entity);
        logger.info("Update job \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning job \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    public void delete(final Job entity) throws DeletionException {

        //Checking, if job is stored in database
        final Job storedJob = jobRepository.findBy(entity.getId());
        if (storedJob == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting job
        try {
            logger.trace("Attempting to delete job \"{}\" ...", entity);
            jobRepository.attachAndRemove(entity);
            logger.info("Delete job \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting job \"{}\". Can't delete job.", entity);
            throw new DeletionException("Can't delete job: " + e.getMessage());
        }
    }

    /**
     * Needs collection boolean.
     *
     * @param job the job
     * @return the boolean
     */
    public boolean needsCollection(Job job) {
        if (job == null) return false;
        if (job.getJobState() != JobState.PROCESSING) return false;
        return procedureService.needsCollection(this.getCurrentProcedure(job));
    }

    /**
     * Needs delivery boolean.
     *
     * @param job the job
     * @return the boolean
     */
    public boolean needsDelivery(Job job) {
        if (job == null)
            return false;

        if (job.getJobState() != JobState.PROCESSING)
            return false;

        Location locationOfCarrier = job.getAssemblies().get(0).getCarriers().get(0).getLocation();
        boolean onTransport = transportService.getAll().contains(locationOfCarrier);

        Procedure currentProcedure = this.getCurrentProcedure(job);

        return onTransport || procedureService.needsDelivery(currentProcedure);
    }

    /**
     * Gets jobs that need transport.
     *
     * @return the jobs that need transport
     */
    public List<Job> getJobsThatNeedTransport() {
        List<Job> transferableJobs = new ArrayList<>();

        for (Job job : this.getActive()) {
            if (this.needsCollection(job) || this.needsDelivery(job))
                transferableJobs.add(job);
        }
        for (Job job : this.getCancelled()) {
            if (this.isCancelledAndAssemblyNotInStock(job))
                transferableJobs.add(job);
        }

        return transferableJobs;
    }

    /**
     * Returns the position of workstation for the next procedure in a given job.
     *
     * @param job the job
     * @return the next position
     */
    public String getNextPosition(Job job) {
        if (job == null)
            return null;

        String lager = "Lager";

        if (this.isComplete(job) || this.isCancelledAndAssemblyNotInStock(job))
            return lager;

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return null;

        StateHistory stateHistory = currentProcedure.getStateHistory();

        if (stateHistory == null)
            return null;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return null;

        String currentPosition = currentProcedure.getProcessStep().getWorkstation().getPosition();

        if (!this.isComplete(job) && stateExecs.isEmpty())
            return currentPosition;

        Procedure nextProcedure = null;
        try {
            nextProcedure = this.getNextProcedure(job);
        } catch (GetNextException e) {
            return lager;
        }

        return nextProcedure.getProcessStep().getWorkstation().getPosition();
    }

    /**
     * Collect jobs assembly.
     *
     * @param job the job
     * @throws CollectingException the collecting exception
     */
    public void collectJobsAssembly(Job job) throws CollectingException {
        logger.debug("Call to JobService#collectJobsAssembly with '{}'. ", job);
        if (job == null)
            throw new CollectingException("Can't collect assembly: job is null");

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            throw new CollectingException("Can't collect assembly: current procedure is null");

        StateHistory stateHistory = currentProcedure.getStateHistory();

        if (stateHistory == null)
            throw new CollectingException("Can't collect assembly: stateHistory of current procedure is null");

        StateExec currentStateExec = stateHistoryService.getCurrentStateExec(stateHistory);

        //Finish last stateExec of assemblies procedure
        if (this.needsCollection(job))
            currentStateExec.setFinishedAt(LocalDateTime.now());

        List<Carrier> carriers = job.getAssemblies().get(0).getCarriers();
        Transport transport = transportService.getTransportByUser(userService.getExecutingUser()).get(0);

        for (Carrier carrier : carriers) {
            carrierService.collect(carrier, transport);
        }
    }

    /**
     * Deliver jobs assembly.
     *
     * @param job the job
     * @throws DeliveringException the delivering exception
     */
    public void deliverJobsAssembly(Job job) throws DeliveringException {
        logger.debug("Call to JobService#deliverJobsAssembly with '{}'. ", job);
        if (job == null)
            throw new DeliveringException("Can't deliver assemblies: job is null");

        try {
            this.startNextProcedure(job);
        } catch (StartNextProcedureException e) {
            if (this.isComplete(job)){
                job.setJobState(JobState.FINISHED);
            }
        }

        List<Carrier> carriers = job.getAssemblies().get(0).getCarriers();
        Location stock = stockService.getAll().get(0);

        if (this.isComplete(job) || this.isCancelledAndAssemblyNotInStock(job)) {
            for (Carrier carrier : carriers) {
                carrierService.deliver(carrier, stock);
            }
            job.setAssemblies(null);
            return;
        }

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            throw new DeliveringException("Can't deliver assembly: current procedure is null");

        Workstation workstation = currentProcedure.getProcessStep().getWorkstation();

        for (Carrier carrier : carriers) {
            carrierService.deliver(carrier, workstation);
        }
    }

    /**
     * Return {@link Job}s that are currently waiting for verification by {@link de.unibremen.swp2.kcb.model.Role#LOGISTIKER}.
     *
     * @return Collection of Jobs waiting for verification
     */
    public List<Job> getPending() {
        List<Job> pendingJobs = new ArrayList<>();

        for (Job job : jobRepository.findAll()) {
            if (this.isPending(job)) pendingJobs.add(job);
        }

        return pendingJobs;
    }

    /**
     * Returns, if a {@link Job} is currently waiting for verification by {@link de.unibremen.swp2.kcb.model.Role#LOGISTIKER}.
     *
     * @return Boolean if Job is waiting for verification
     */
    public Boolean isPending(Job job) {
        if (job == null)
            return false;

        return (job.getAssemblies() == null) || (job.getJobState() == JobState.PENDING);
    }

    /**
     * Return all {@link Job}s that are currently being executed.
     *
     * @return Collection of Jobs currently being executed.
     */
    public List<Job> getActive() {
        return jobRepository.findActiveJobs();
    }

    /**
     * Return if the given {@link Job} is currently active. Active Jobs are currently being executed.
     *
     * @param job the job
     * @return if job is active
     */
    public boolean isActive(final Job job) {
        if (job == null)
            return false;

        JobState jobState = job.getJobState();

        if (jobState != JobState.PROCESSING)
            return false;

        List<Procedure> procedureList = job.getProcedures();
        for (Procedure procedure : procedureList) {
            if (!procedureService.isComplete(procedure))
                return true;
        }

        return false;
    }

    /**
     * Gets cancelled Jobs from repo.
     *
     * @return the cancelled
     */
    public List<Job> getCancelled() {
        List<Job> cancelledJobs = new ArrayList<>();
        for (Job job : this.getAll()) {
            if (job.getJobState() == JobState.CANCELLED)
                cancelledJobs.add(job);
        }
        return cancelledJobs;
    }

    /**
     * Returns if job is cancelled and its assemblies are not in stock.
     *
     * @param job the job
     * @return the boolean
     */
    public boolean isCancelledAndAssemblyNotInStock (final Job job) {
        if (job == null)
            return false;

        JobState jobState = job.getJobState();

        List<Assembly> assemblies = job.getAssemblies();

        if (assemblies == null || assemblies.isEmpty())
            return false;

        List<Carrier> carriers = assemblies.get(0).getCarriers();

        if (carriers == null || carriers.isEmpty())
            return false;

        Location location = carriers.get(0).getLocation();

        if (location == null)
            return false;

        String position = location.getPosition();

        return (jobState == JobState.CANCELLED && !position.equals("Lager"));
    }

    /**
     * Check if the given {@link Job} is complete.
     *
     * @param job the job
     * @return if job is complete
     */
    public boolean isComplete(final Job job) {
        if (job == null)
            return false;

        List<Procedure> procedureList = job.getProcedures();
        for (Procedure procedure : procedureList) {
            if (!procedureService.isComplete(procedure)) return false;
        }

        return true;
    }

    /**
     * Returns if the first processStep of the processChain of the job creates an assembly
     *
     * @param job the job
     * @return if the job creates an assembly
     */
    public boolean isCreating(Job job) {
        if (job == null)
            return false;

        return processChainService.isCreating(job.getProcessChain());
    }

    /**
     * Returns if the last processStep of the processChain of the job deletes an assembly
     *
     * @param job the job
     * @return if the job creates an assembly
     */
    public boolean isDeleting(Job job) {
        if (job == null)
            return false;

        return processChainService.isDeleting(job.getProcessChain());
    }

    /**
     * Returns if any processStep of the processChain of the job modifies an assembly
     *
     * @param job the job
     * @return if the job modifies an assembly
     */
    public boolean isModifying(Job job) {
        if (job == null)
            return false;

        return processChainService.isModifying(job.getProcessChain());
    }


    /**
     * Returns the index of the current procedure.
     *
     * @param job the job
     * @return the current progress in chain
     */
    public int getCurrentProgressInChain(Job job) {
        if (job == null)
            return 0;

        List<Procedure> procedures = job.getProcedures();

        if (procedures == null)
            return 0;

        if (job.getJobState() == JobState.FINISHED)
            return procedures.size();

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return 0;

        return procedures.indexOf(currentProcedure);
    }

    /**
     * Returns the maximum index of a procedure.
     *
     * @param job the job
     * @return the max progress in chain
     */
    public int getRemainingProgressInChain(Job job) {
        if (job == null)
            return 0;

        List<Procedure> procedures = job.getProcedures();

        if (procedures == null)
            return 0;

        return procedures.size() - this.getCurrentProgressInChain(job);
    }

    /**
     * Gets progress of given job in processChain in percent.
     *
     * @param job the job
     * @return the progress in chain
     */
    public float getProgressInChain(Job job) {
        if (job == null)
            return 0;

        if (job.getJobState() == JobState.FINISHED)
            return 100;

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return 0;

        List<Procedure> procedures = job.getProcedures();

        if (procedures == null)
            return 0;

        float current = procedures.indexOf(currentProcedure);
        float max = procedures.size();

        return (current / max) * 100;
    }

    /**
     * Returns 3 jobs with the highest priority.
     *
     * @return the three most important jobs
     */
    public List<Job> getThreeMostImportantJobs() {
        List<Job> jobs = jobRepository.findAllOrderByPriorityValueDesc();

        if (jobs == null)
            return new ArrayList<>();

        if (jobs.size() > 2) {
            return jobs.subList(0, 3);
        } else if (jobs.isEmpty()) {
            return jobs;
        } else {
            return jobs.subList(0, jobs.size());
        }
    }

    /**
     * Start the execution of the given {@link Job}.
     *
     * @param entity to start the execution of
     * @throws StartJobException if starting the job fails
     */
    public void start(Job entity) throws StartJobException {
        logger.debug("Call to JobService#start with '{}'. ", entity);

        List<Assembly> assemblies;
        if (entity == null) {
            logger.debug("Job is null");
            throw new StartJobException("Can't start Job: Job is null");
        }

        assemblies = entity.getAssemblies();

        if (!this.isCreating(entity) && (assemblies == null || assemblies.isEmpty())) {
            logger.debug("Assemblies are null and job is not creating");
            throw new StartJobException("Can't start Job: Assemblies are null and job is not creating");
        } else if (entity.getJobState() != JobState.PENDING) {
            logger.debug("Job is not pending");
            throw new StartJobException("Can't start Job: Job is not pending");
        } else {
            entity.setJobState(JobState.PROCESSING);
            entity.setAssemblies(assemblies);
        }

        if (this.isCreating(entity)) {
            try {
                this.startNextProcedure(entity);
            } catch (StartNextProcedureException e) {
                throw new StartJobException("Can't start job: " + e.getMessage());
            }
        }
    }

    /**
     * Force the execution of a {@link Job} to be stopped immediately.
     *
     * @param job to be stopped.
     * @throws StopException if stopping fails
     */
    public void stop(final Job job) throws StopException {
        if (job == null) {
            logger.error("Parameter can't be null");
            throw new StopException("Parameter can't be null");
        }

        JobState jobState = job.getJobState();

        logger.debug("Attempting to start job: {}", job);
        if (jobState == JobState.PROCESSING) {
            job.setJobState(JobState.CANCELLED);
            logger.info("Job successfully stopped");
        } else if (jobState == JobState.PENDING) {
            logger.error("Job is pending.");
            throw new StopException("Job is pending.");
        } else if (jobState == JobState.FINISHED) {
            logger.error("Job is already finished.");
            throw new StopException("Job is already finished.");
        } else if (jobState == JobState.CANCELLED) {
            logger.error("Job is already cancelled.");
            throw new StopException("Job is already cancelled.");
        } else {
            logger.error("Job can't be stopped");
            throw new StopException("Job can't be stopped");
        }
    }

    /**
     * Returns the preparation for the current procedure in a given job.
     *
     * @param job the job
     * @return the preparation
     */
    public CarrierType getCurrentPreparation(Job job) {
        if (job == null)
            return null;

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return null;

        return currentProcedure.getProcessStep().getPreparation();
    }

    /**
     * Returns the current active procedure of the job.
     *
     * @param job the job
     * @return the current active procedure
     */
    public Procedure getCurrentProcedure(Job job) {
        if (job == null)
            return null;

        for (Procedure procedure : job.getProcedures()) {
            if (!procedureService.isComplete(procedure)) {
                return procedure;
            }
        }

        return null;
    }

    /**
     * Returns the next procedure of the job.
     *
     * @param job the job
     * @return the next procedure
     * @throws GetNextException if there is no next procedure
     */
    public Procedure getNextProcedure(Job job) throws GetNextException {
        if (job == null)
            throw new GetNextException("Can't get next procedure: job is null");

        List<Procedure> procedures = job.getProcedures();
        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            throw new GetNextException("Can't get next procedure: current procedure is null");

        if (this.hasNextProcedure(job))
            return procedures.get(procedures.indexOf(currentProcedure) + 1);

        throw new GetNextException("Can't get next procedure: there is no next procedure");

    }

    /**
     * Returns if the job has another procedure after the current one.
     *
     * @param job the job
     * @return whether the job has a next procedure
     */
    public boolean hasNextProcedure(Job job) {
        if (job == null)
            return false;

        if (this.isComplete(job))
            return false;

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return false;

        List<Procedure> procedures = job.getProcedures();

        return procedures.indexOf(currentProcedure) < procedures.size() - 1;
    }

    /**
     * Sets next procedure for a given job.
     *
     * @param job the job
     * @throws StartNextProcedureException the set next procedure exception
     */
    public void startNextProcedure(Job job) throws StartNextProcedureException {
        if (job == null)
            throw new StartNextProcedureException("Can't start next procedure: job is null");

        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            throw new StartNextProcedureException("Can't start next procedure: current procedure is null");

        try {
            procedureService.startNextState(currentProcedure);
        } catch (SetNextStateException e) {
            throw new StartNextProcedureException("Can't start next procedure: " + e.getMessage());
        }
    }

    /**
     * Add assembly, part of create and add and execute method.
     *
     * @param procedure the procedure
     * @param assembly the assembly
     * @throws FindByException the find by exception
     */
    public void addAssembly(Procedure procedure, Assembly assembly) throws FindByException {
            logger.debug("Call to JobService#addAssembly with '{}', '{}'. ", procedure, assembly);
            Job job = this.getByProcedure(procedure);
            List<Assembly> assemblies = new ArrayList<>();
            assemblies.add(assembly);

            job.setAssemblies(assemblies);
    }

    /**
     * Create and add and execute method by Arvid.
     *
     * @param assembly the assembly
     * @param procedure the procedure
     * @param transitionTime the transition time
     * @throws CreateAndAddAndExecuteException the create and add and execute exception
     */
    public void createAndAddAndExecute(Assembly assembly, Procedure procedure, Integer transitionTime) throws CreateAndAddAndExecuteException {
        if (assembly == null || procedure == null)
            throw new CreateAndAddAndExecuteException("Can't execute operation: Parameters are null");

        Assembly createdAssembly;
        try {
            createdAssembly = assemblyService.create(assembly);
        } catch (CreationException e) {
            throw new CreateAndAddAndExecuteException("Can't create assembly: " + e.getMessage());
        }

        try {
            this.addAssembly(procedure, createdAssembly);
        } catch (FindByException e) {
            throw new CreateAndAddAndExecuteException("Can't add assembly to job: " + e.getMessage());
        }

        try {
            procedureService.doFinish(procedure, transitionTime);
        } catch (ExecutionException e) {
            throw new CreateAndAddAndExecuteException("Can't finish procedure: " + e.getMessage());
        }
    }

    /**
     * Generates JSON protocol file for the PKP role.
     *
     * @param job the job for which the protocol should be created
     * @return the protocol file
     * @throws ProtocolGenerationException the protocol generation exception
     */
    @RequiresAuthentication
    public File generateProtocol(final Job job) throws ProtocolGenerationException {

        if (job == null) {
            final String message = "Attempting to generate Protocol for empty Job. Aborting..";
            logger.warn(message);
            throw new ProtocolGenerationException(message);
        }

        FileOutputStream outputStream = null;

        try {
            final String prefix = "protocol_" + job.getName().toLowerCase();
            File tmpFile = File.createTempFile(prefix, ".json");

            final String jobJson = job.toJSON();

            outputStream = new FileOutputStream(tmpFile);

            outputStream.write(jobJson.getBytes());
            outputStream.flush();
            return tmpFile;
        } catch (IOException e) {
            logger.debug(e);
            throw new ProtocolGenerationException("Couldn't create temp File for protocol generation of job " + job.getName());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.warn("OutputStream for protocol generation couldn't be closed. {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Check if the json object contains the requires attributes
     *
     * @param jsonObject to check
     * @throws JSONFormatException if object doesn't contain the required attributes
     */
    private void checkJSONUploadObject(final JsonObject jsonObject) throws JSONFormatException {
        if (!jsonObject.has("step"))
            throw new JSONFormatException("Json Object doesn't contain step.");
        if (!jsonObject.has("parameters"))
            throw new JSONFormatException("Json Object doesn't contain parameters");
    }

    /**
     * method checks JSON parameter values
     * @param jsonObject the JSON objcet
     * @throws JSONFormatException when json object dont have field or value
     */
    private void checkJSONParameterValue(final JsonObject jsonObject) throws JSONFormatException {
        if (!jsonObject.has("field"))
            throw new JSONFormatException("Json Object doesn't contain field.");
        if (!jsonObject.has("value"))
            throw new JSONFormatException("Json Object doesn't contain value.");
    }

    /**
     * Return the procedure executing a specific processStep inside of a job.
     *
     * @param job         to get procedure for
     * @param processStep to get procedure for
     * @return Procedure executing the given processtep for the given job
     */
    private Procedure getProcedureForJobAndProcessStep(final Job job, final ProcessStep processStep) {
        if (processStep == null || job == null) return null;
        List<Procedure> procedures = job.getProcedures();
        for (Procedure procedure : procedures) {
            if (processStep.equals(procedure.getProcessStep()))
                return procedure;
        }
        return null;
    }

    /**
     * Return the parameter in a given processStep, where the field is equal to the given field.
     *
     * @param step  to get parameter for
     * @param field of parameter to be returned
     * @return Parameter of given ProcessStep where field is = field or null if no such parameter exists
     */
    private Parameter getParameterForProcessStepAndField(final ProcessStep step, final String field) {
        if (step == null || field == null) return null;
        List<Parameter> parameters = step.getParameters();
        for (Parameter param : parameters) {
            if (param.getField().equals(field)) return param;
        }
        return null;
    }

    /**
     * Create a Value / CardinalValue from a json String. This includes persisting the value in the configured
     * Datasource. Will return CardinalValue when json contains 'unit' Attribute.
     *
     * @param json - to unmarshal into Value
     * @return parsed Value of given JSON String
     * @throws CreationException if Value creation fails
     */
    @Transactional
    public Job updateValuesFromJson(Job job, final String json) throws JSONFormatException {
        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        JsonArray parsedJson = gson.fromJson(json, JsonArray.class);

        try {
            for (JsonElement e : parsedJson) {
                if (e == null) continue;
                JsonObject obj = (JsonObject) e;
                checkJSONUploadObject(obj);
                // Try to fetch ProcessStep with given name
                final String processStepName = obj.get("step").getAsString();
                List<ProcessStep> processSteps = processStepService.getByName(processStepName);
                // Name of a ProcessStep should be unique
                final ProcessStep processStep = processSteps.get(0);
                if (processSteps.size() != 1)
                    throw new JSONFormatException("Multiple process steps found for " + obj.get("step").getAsString());
                Procedure procedure = getProcedureForJobAndProcessStep(job, processStep);

                if (procedure == null)
                    throw new JSONFormatException("No Procedure found for processStep " + processSteps.get(0).getName());

                // Reset Procedure values
                procedure.setValues(new ArrayList<>());

                final JsonArray parameters = obj.get("parameters").getAsJsonArray();

                for (JsonElement parameterPlusValue : parameters) {
                    JsonObject parameterValueObj = (JsonObject) parameterPlusValue;
                    this.checkJSONParameterValue(parameterValueObj);
                    final String field = parameterValueObj.get("field").getAsString();
                    final String valueString = parameterValueObj.get("value").getAsString();

                    String unit = parameterValueObj.has("unit") ?
                            parameterValueObj.get("unit").getAsString() : null;

                    final Parameter parameter = this.getParameterForProcessStepAndField(processStep, field);

                    if (parameter == null) throw new JSONFormatException("Parameter not found for field" + field);

                    Value value;

                    if (unit == null) {
                        // No unit -> Value is normal Value
                        value = new Value();
                        value.setParameter(parameter);
                        value.setValue(valueString);
                    } else {
                        // Unit is specified -> Value is CardinalValue
                        value = new CardinalValue();
                        value.setParameter(parameter);
                        value.setValue(valueString);
                        ((CardinalValue) value).setUnit(unit);
                    }

                    // Update values of Procedure
                    List<Value> values = procedure.getValues();
                    values.add(value);
                    procedure.setValues(values);
                }
                try {
                    procedureService.update(procedure);
                } catch (UpdateException ex) {
                    try {
                        procedureService.create(procedure);
                    } catch (CreationException exc) {
                        logger.debug(e);
                        throw new JSONFormatException("Couldn't create/update procedure");
                    }
                }
            }
        } catch (FindByException e) {
            logger.debug(e);
            throw new JSONFormatException("Invalid ProcessStep provided.");
        }
        return job;
    }

    /**
     * Test whether a given job is an old active Job relative to the value 'oldActiveJob'
     * in the globalConfig which can be set by the admin.
     *
     * @return the old active jobs
     * @throws OldActiveJobException when global config value is not found.
     */
    public boolean isOldActiveJob(Job job) throws OldActiveJobException {
        LocalDateTime currentTime = LocalDateTime.now();
        Procedure currentProcedure = this.getCurrentProcedure(job);

        if (currentProcedure == null)
            return false;

        if (job.getJobState() != JobState.PROCESSING)
            return false;
        if (stateHistoryService.getCurrentStateExec(currentProcedure.getStateHistory()) == null) {
            return false;
        }
        StateExec stateExec = stateHistoryService.getCurrentStateExec(currentProcedure.getStateHistory());
        int value;
        try {
            value = Integer.parseInt(globalConfigService.getByKey("oldActiveJob").getValue());
        } catch (FindByException e) {
            throw new OldActiveJobException("oldActiveJob value coult not be loaded from the global config");
        }

        return stateExec.getStartedAt() == null ?
                stateExec.getTransitionAt().until(currentTime,
                        ChronoUnit.MINUTES) > value :
                stateExec.getStartedAt().until(currentTime,
                        ChronoUnit.MINUTES) > value;
    }


    /**
     * Export parameters of job json file in the format used by the sfb.
     *
     * @param job to export parameters of
     * @return File containing sfb valid json of parameters for given job.
     */
    @RequiresAuthentication
    public File exportParameters(final Job job) throws ExportException {
        List<Procedure> procedures = job.getProcedures();
        JsonArray result = new JsonArray();

        for (final Procedure procedure : procedures) {
            result.add(procedureService.getAsSFBJSON(procedure));
        }

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return createTmpFile(gson.toJson(result), "parameters", ".json");
        } catch (IOException e) {
            logger.warn("IOException occurred during parameter export: {}", e.getMessage());
            throw new ExportException("IOException occurred during parameter export. Aborting.");
        }

    }

    /**
     * Updates the values of the given job. Will check if the value contains a space character and
     * treat the last substring as the unit if it does.
     *
     * @param entity to update values of
     */
    public Job parseValues(final Job entity) {
        List<Procedure> procedures = entity.getProcedures();

        for (final Procedure procedure : procedures) {
            List<Value> values = procedure.getValues();

            List<Value> result = new ArrayList<>();

            for (final Value value : values) {
                if (!(value instanceof CardinalValue) && value != null && value.getValue() != null) {
                    // Value is no Cardinal Value - check if there is a unit
                    String[] split = value.getValue().split(" ");
                    // The last item in the split string will be treated as the unit.
                    if (split.length > 1) {
                        final CardinalValue updatedValue = new CardinalValue();
                        final String unit = split[split.length-1];
                        updatedValue.setUnit(unit);
                        updatedValue.setValue(value.getValue().replace(unit, "").strip());
                        updatedValue.setParameter(value.getParameter());
                        result.add(updatedValue);
                        continue;
                    }
                }
                result.add(value);
            }
            procedure.setValues(result);
        }
        entity.setProcedures(procedures);
        return entity;
    }

    /**
     * Return all {@link Job}s.
     *
     * @return Collection of all Jobs.
     */
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    /**
     * Return the {@link Job} with the given id.
     *
     * @param id of the Job
     * @return Job with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Job getById(String id) throws InvalidIdException {

        //Validating jobID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "JobID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("JobID \"{}\" is valid.", id);

        //Finding job by ID
        final Job entity;
        try {
            logger.trace("Attempting to find job by ID \"{}\" ...", id);
            entity = jobRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding job by ID \"{}\". Can't find job.", id);
            throw new InvalidIdException("Can't find job: " + e.getMessage());
        }

        logger.trace("Finding of job by ID \"{}\" completed without exceptions.", id);
        logger.info("Find job by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning job by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return the {@link Job}s with the given name.
     *
     * @param name of the Job.
     * @return Jobs with the given name if exists. Null if Job does not exist.
     * @throws FindByException If findBy fails.
     */
    public List<Job> getByName(final String name) throws FindByException {
        if (name == null) {
            String message = "Name can't be null.";
            logger.warn(message);
            throw new FindByException(message);
        }

        logger.trace("Returning jobs by name.");
        return jobRepository.findByName(name);
    }

    /**
     * Return all {@link Job} executing a given {@link ProcessChain}.
     *
     * @param processChain to get executing jobs for
     * @return Collection of Jobs executing the given ProcessChain.
     * @throws FindByException If findBy fails.
     */
    public List<Job> getByProcessChain(final ProcessChain processChain) throws FindByException {
        if (processChain == null) {
            logger.warn("ProcessChain can't be null.");
            throw new FindByException("ProcessChain can't be null");
        }

        logger.trace("Returning jobs by processChain.");
        return jobRepository.findByProcessChain(processChain);
    }

    /**
     * Return {@link Job} of a given {@link Procedure}.
     *
     * @param procedures to get Job for
     * @return Job associated with the given procedure
     * @throws FindByException If findBy fails.
     */
    public List<Job> getByProcedures(final List<Procedure> procedures) throws FindByException {
        if (procedures == null) {
            logger.warn("Procedures can't be null.");
            throw new FindByException("Procedures can't be null");
        }

        logger.trace("Returning jobs by procedures.");
        return jobRepository.findByProcedures(procedures);
    }

    /**
     * Return {@link Job} of a given {@link Procedure}.
     *
     * @param procedure to get Job for
     * @return Job associated with the given procedure
     * @throws FindByException If findBy fails.
     */
    public Job getByProcedure(Procedure procedure) throws FindByException {
        if (procedure == null) {
            logger.warn("Procedure can't be null.");
            throw new FindByException("Procedure can't be null");
        }

        logger.trace("Returning job by procedure.");
        return jobRepository.findByProcedure(procedure).get(0);
    }

    /**
     * Returns a list of jobs with a given priority.
     *
     * @param priority the priority
     * @return the by priority
     */
    public List<Job> getByPriority (Priority priority) {
        return jobRepository.findByPriority(priority);
    }

    /**
     * Return Job that is currently processing the given assembly.
     *
     * @param assembly to get job for
     * @return Job that is processing the given assembly
     */
    public Job getByAssembly(final Assembly assembly) {
        if (assembly == null) {
            throw new IllegalArgumentException("Assembly can't be null");
        }
        List<Job> jobs = jobRepository.findByAssembly(assembly);
        if (jobs == null) {
            logger.debug("No jobs found for assembly: {}", assembly);
            return null;
        }
        // Assemblies should only be part of one Job at a given time.
        if (jobs.size() > 1)
            logger.warn("Multiple jobs found for assembly: {}", assembly);
        if (jobs.isEmpty()) {
            logger.debug("No jobs could be found for assembly: {}", assembly);
            return null;
        }

        Job result = null;

        // Return the Job that contains the given Assembly AND is currently PROCESSING
        for(Job j : jobs) {
            if(j.getJobState().equals(JobState.PROCESSING)) {
                if(result == null) {
                    result = j;
                } else {
                    logger.warn("More than one Job found currently PROCESSING the given Assembly. Returning first found.");
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns a list of jobs whose current procedure is currently running on a given workstation.
     *
     * @param workstation the workstation
     * @return list of jobs
     */
    public List<Job> getCurrentlyRunningByWorkstation(Workstation workstation) {
        return jobRepository.findCurrentlyRunningByWorkstation(workstation);
    }

    /**
     * Gets all active jobs for current technologe.
     *
     * @return all active jobs for current technologe
     * @throws GetAllActiveJobsForCurrentTechnologeException if finding fails exception
     */
    public List<Job> getAllActiveJobsForCurrentTechnologe () throws GetAllActiveJobsForCurrentTechnologeException {
        List<Job> result = new ArrayList<>();
        for (Job job : this.getActive()) {
            Procedure currentProcedure = this.getCurrentProcedure(job);
            if (currentProcedure == null)
                continue;

            try {
                if (workstationService.getByUser(userService.getExecutingUser()).contains(currentProcedure.getProcessStep().getWorkstation()))
                    result.add(job);
            } catch (FindByException e) {
                throw new GetAllActiveJobsForCurrentTechnologeException("Couldn't get all active jobs for current Technologe: {}" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * Gets all active jobs for current technologe that are not currently being transported.
     *
     * @return all active jobs for current technologe
     * @throws GetAllActiveJobsForCurrentTechnologeException if finding fails exception
     */
    public List<Job> getAllActiveJobForCurrentTechnologeNotBeingTransported() throws GetAllActiveJobsForCurrentTechnologeException {
        List<Job> allForCurrentTechnologe = this.getAllActiveJobsForCurrentTechnologe();
        List<Job> result = new ArrayList<>();
        for (Job job : allForCurrentTechnologe) {
            if (!isInTransport(job)) result.add(job);
        }
        return result;
    }

    /**
     * Check if the given job is currently being transported
     *
     * @param job to be checked
     * @return is the given job currently being transported
     */
    public boolean isInTransport(final Job job) {
        return this.needsCollection(job) || this.needsDelivery(job) || this.isCancelledAndAssemblyNotInStock(job);
    }


    /**
     * Return all Jobs that are currently processing the given assembly.
     *
     * @param assembly to get jobs for
     * @return Jobs that are processing the given assembly
     */
    public List<Job> getAllByAssembly(final Assembly assembly) {
        if (assembly == null) {
            throw new IllegalArgumentException("Assembly can't be null");
        }
        List<Job> jobs = jobRepository.findByAssembly(assembly);
        if (jobs == null) {
            logger.debug("No jobs were found for assembly: {}", assembly);
            return null;
        }

        if (jobs.isEmpty()) {
            logger.debug("No jobs have been found for assembly: {}", assembly);
            return null;
        }
        return jobs;
    }
}
