package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.service.JobService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a single {@link Job}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@Named
@ViewScoped
public class JobController extends SingleController<Job> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(JobController.class);

    /**
     * Job Service to handle actions
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of ProcessChainController
     */
    @Inject
    private ProcessChainController processChainController;

    /**
     * Injected instance of AssemblyController
     */
    @Inject
    private AssemblyController assemblyController;

    /**
     * Injected instance of ProcedureController
     */
    @Inject
    private ProcedureController procedureController;

    /**
     * Injected instance of StateExecController
     */
    @Inject
    private StateExecController stateExecController;

    /**
     * Part attribute of JobController class
     */
    @Getter
    @Setter
    private Part parameterImportFile;

    /**
     * Stop the currently selected {@link Job}.
     */
    public void stop() {
        try {
            jobService.stop(this.entity);
        } catch (StopException e) {
            logger.debug("Stop exception occurred: {}", e.getMessage());
        }
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            jobService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during Job creation.", e);
            final String jobName = this.getSaveJobName(entity);
            final String summary = localeController.formatString("error.summary.job-creation");
            final String detail = localeController.formatString("error.detail.job-creation");
            final String updatedDetail = detail != null ? detail.replace("<jobName>", jobName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Job creation.", e);
            final String jobName = this.getSaveJobName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", jobName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            jobService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during Job deletion.", e);
            final String jobName = this.getSaveJobName(entity);
            final String summary = localeController.formatString("error.summary.job-deletion");
            final String detail = localeController.formatString("error.detail.job-deletion");
            final String updatedDetail = detail != null ? detail.replace("<jobName>", jobName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            jobService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during Job update.", e);
            final String jobName = this.getSaveJobName(this.entity);
            final String summary = localeController.formatString("error.summary.job-update");
            final String detail = localeController.formatString("error.detail.job-update");
            final String updatedDetail = detail != null ? detail.replace("<jobName>", jobName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Returns the index of the current procedure.
     *
     * @param job the job
     * @return the current progress in chain
     */
    public int getCurrentProgressInChain(Job job) {
        return jobService.getCurrentProgressInChain(job);
    }

    /**
     * Returns the maximum index of a procedure.
     *
     * @param job the job
     * @return the max progress in chain
     */
    public int getRemainingProgressInChain(Job job) {
        return jobService.getRemainingProgressInChain(job);
    }

    /**
     * Gets progress in process chain.
     *
     * @param job the job
     * @return the progress in process chain
     */
    public float getProgressInProcessChain(Job job) {
        return jobService.getProgressInChain(job);
    }

    /**
     * Returns if the first processStep of the processChain of the job creates an assembly
     *
     * @param job the job
     * @return if the job creates an assembly
     */
    public boolean isCreating(Job job) {
        return jobService.isCreating(job);
    }

    /**
     * Returns if the last processStep of the processChain of the job deletes an assembly
     *
     * @param job the job
     * @return if the job creates an assembly
     */
    public boolean isDeleting(Job job) {
        return jobService.isDeleting(job);
    }

    /**
     * Returns if any processStep of the processChain of the job modifies an assembly
     *
     * @param job the job
     * @return if the job modifies an assembly
     */
    public boolean isModifying(Job job) {
        return jobService.isModifying(job);
    }

    /**
     * Needs collection boolean.
     *
     * @param job the job
     * @return the boolean
     */
    public boolean needsCollection(Job job) {
        return jobService.needsCollection(job);
    }

    /**
     * Needs delivery boolean.
     *
     * @param job the job
     * @return the boolean
     */
    public boolean needsDelivery(Job job) {
        return jobService.needsDelivery(job);
    }

    /**
     * Collect jobs assembly.
     */
    public void collectJobsAssemblies() {
        try {
            jobService.collectJobsAssembly(this.entity);
        } catch (CollectingException e) {
            logger.debug("collecting jobAssemblies failed");
        }
    }

    /**
     * Deliver jobs assemblies.
     */
    public void deliverJobsAssemblies() {
        try {
            jobService.deliverJobsAssembly(this.entity);
        } catch (DeliveringException e) {
            logger.debug("delivery of jobAssemblies failed");
        }
    }

    /**
     * Add assembly.
     *
     * @param procedure the procedure
     * @param assembly the assembly
     */
    public void addAssembly(Procedure procedure, Assembly assembly) {
        try {
            jobService.addAssembly(procedure, assembly);
        } catch (FindByException e) {
            logger.debug(e.getMessage());
            e.getMessage();
        }
    }

    /**
     * Create and add and execute.
     *
     * @param assembly the assembly
     * @param procedure the procedure
     * @param transitionTime the transition time
     */
    public void createAndAddAndExecute(Assembly assembly, Procedure procedure, Integer transitionTime) {
        try {
            jobService.createAndAddAndExecute(assembly, procedure, transitionTime);
        } catch (CreateAndAddAndExecuteException e) {
            e.getMessage();
        }
    }

    /**
     * Returns the preparation for the current procedure in a given job.
     *
     * @param job the job
     * @return the preparation
     */
    public CarrierType getCurrentPreparation(Job job) {
        return jobService.getCurrentPreparation(job);
    }

    /**
     * Gets a Job by its procedure.
     *
     * @param procedure the procedure
     * @return the by procedure
     */
    public Job getByProcedure(Procedure procedure) {
        try {
            return jobService.getByProcedure(procedure);
        } catch (FindByException e) {
            logger.debug("collecting jobAssemblies failed: ", e.getMessage());
            return null;
        }
    }

    /**
     * Gets next position.
     *
     * @param job the job
     * @return the next position
     */
    public String getNextPosition(Job job) {
        return jobService.getNextPosition(job);
    }

    /**
     * gets all active jobs
     * @return list of all active jobs
     */
    public List<Job> getActive() {
        return jobService.getActive();
    }

    /**
     * Updates an existing entity, adds an Assembly and changes the JobState to {@link de.unibremen.swp2.kcb.model.JobState#PROCESSING}.
     */
    public void start() {
        try {
            jobService.start(this.entity);
        } catch (StartJobException e) {
            logger.debug("Error occurred when trying to start Job.", e);
            final String jobName = this.getSaveJobName(this.entity);
            final String summary = localeController.formatString("error.summary.job-update");
            final String detail = localeController.formatString("error.detail.job-update");
            final String updatedDetail = detail != null ? detail.replace("<jobName>", jobName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates the entity with a given Job
     *
     * @param entity the Job to be updated
     */
    public void updateEntity(Job entity) {
        this.entity = entity;
        super.render("job_edit_form");
        super.render("job_edit_form_logistiker");
        super.render("job_view_form");
        super.render("add_assembly_form");
    }

    public void addAssemblyToEntity(Job entity) {
        this.entity = entity;
        if (jobService.isCreating(entity)) super.render("start_creating_job_confirm_form");
        else super.render("add_assembly_form");
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get jobname of
     * @return name if entity has one, 'null' otherwise
     */
    private String getSaveJobName(final Job entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }


    /**
     * Populates the private Job entity with Attributes defined in the given processChain
     *
     * @param processChain to populate job with
     */
    private void populateEntity(ProcessChain processChain) {
        List<Procedure> emptyProcedures = new ArrayList<>();
        for (ProcessStep processStep : processChain.getChain()) {
            Procedure procedure = new Procedure();
            StateHistory stateHistory = new StateHistory();
            stateHistory.setStateExecs(new ArrayList<>());
            procedure.setProcessStep(processStep);
            procedure.setValues(new ArrayList<>());
            procedure.setStateHistory(stateHistory);
            List<Value> values = new ArrayList<>();
            for (Parameter parameter : processStep.getParameters()) {
                Value value = new Value();
                value.setParameter(parameter);
                values.add(value);
            }
            procedure.setValues(values);
            emptyProcedures.add(procedure);
        }
        this.entity = new Job();
        this.entity.setProcedures(emptyProcedures);
        this.entity.setProcessChain(processChain);
    }

    /**
     * Starts a processChain
     *
     * @param processChain the process chain
     * @return the job
     */
    public Job startChain(ProcessChain processChain) {
        processChainController.updateEntity(processChain);
        this.populateEntity(processChain);
        return this.entity;
    }

    /**
     * Test whether a given job is an old active Job relative to the value 'oldActiveJob'
     * in the globalConfig which can be set by the admin.
     *
     * @return the old active jobs, or false in case of an error
     */
    public boolean isOldActiveJob(Job job) {
        try {
            return jobService.isOldActiveJob(job);
        } catch (OldActiveJobException e) {
            logger.debug("Error occurred when isOldActiveJob is called in JobService.", e);
            return false;
        }
    }

    /**
     * Reset the InputFile of the controller.
     */
    public void resetInputFile() {
        this.parameterImportFile = null;
    }

    /**
     * Upload parameters to the given process step
     */
    public void upload(final ProcessChain chain) {
        if (this.parameterImportFile == null) {
            displayMessageFromResource("error.summary.upload-failed", "error.detail.wrong-mime-type", FacesMessage.SEVERITY_ERROR);
            return;
        }

        this.populateEntity(chain);

        final String contentType = parameterImportFile.getContentType();

        if (contentType == null || !contentType.equals("application/json")) {
            displayMessageFromResource("error.summary.wrong-mime-type", "error.detail.wrong-mime-type", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            final byte[] content = parameterImportFile.getInputStream().readAllBytes();
            this.entity = this.jobService.updateValuesFromJson(this.entity, new String(content));
            this.resetInputFile();
            super.render("processChain_edit_form");
            super.render("processChain_start_form");
            super.render("parameter_loop");
            this.processChainController.updateEntity(chain);
        } catch (JSONFormatException e) {
            logger.debug(e);
            displayMessageFromResource("error.summary.invalid-json", "error.detail.invalid-json", FacesMessage.SEVERITY_ERROR);
        } catch (IOException e) {
            logger.debug(e);
            displayMessageFromResource("error.summary.invalid-json", "error.detail.invalid-json", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Export the given Job as a file containing json in the
     * format the sfb uses.
     *
     * @return StreamedContent of parameters for given job.
     */
    public StreamedContent exportParameters() {
        try {
            File tmpFile = this.jobService.exportParameters(this.entity);
            final String downloadName = "parameters.json";

            if (tmpFile == null) {
                super.displayMessageFromResource("error.summary.export-failed", "error.detail.export-failed", FacesMessage.SEVERITY_ERROR);
                return null;
            }

            return new DefaultStreamedContent(new FileInputStream(tmpFile), "application/json", downloadName);

        } catch (ExportException | FileNotFoundException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.export-failed", "error.detail.export-failed", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }
}
