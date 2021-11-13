package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.service.JobService;
import de.unibremen.swp2.kcb.service.serviceExceptions.ExportException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.GetAllActiveJobsForCurrentTechnologeException;
import de.unibremen.swp2.kcb.service.serviceExceptions.ProtocolGenerationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Class to handle a collection of {@link Job}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@RequestScoped
@Named("jobsController")
public class JobsController extends OverviewController<Job> {

    /**
     * Logger object of the JobsController class
     */
    private static final Logger logger = LogManager.getLogger(JobsController.class);

    /**
     * Injected instance of {@link JobService} to handle business logic
     */
    @Inject
    private JobService jobService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of of all {@link Job}s.
     */
    @Override
    public void refresh() {
        List<Job> jobs = jobService.getAll();

        if (jobs == null || jobs.isEmpty()) {
            logger.debug("Jobs couldn't be loaded. Couldn't find any Jobs");
            this.entities = new ArrayList<>();
            super.displayMessageFromResource("error.summary.empty-jobs",
                    "error.detail.empty-jobs", FacesMessage.SEVERITY_ERROR);
        } else {
            this.entities = jobs;
        }
    }

    /**
     * Get Job by ID
     *
     * @param id the ID
     * @return Job with that ID
     */
    @Override
    public Job getById(String id) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Job by provided ID");
            return jobService.getById(id);
        } catch (Exception e) {
            logger.debug("Couldn't find Job by provided ID");
            final String summary = localeController.formatString("error.summary.jobs-no-active");
            final String detail = localeController.formatString("error.detail.jobs-no-active");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Return a collection of all active {@link Job}s.
     *
     * @return All active {@link Job}s.
     */
    public List<Job> getActive() {
        this.entities = new ArrayList<>();
        logger.debug("Querying all active Jobs");
        this.entities = jobService.getActive();
        return this.entities;
    }

    /**
     * Gets all active jobs for current technologe.
     *
     * @return the all active jobs for current technologe
     */
    public List<Job> getAllActiveJobsForCurrentTechnologe() {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying all active Jobs for current Technologe.");
            this.entities = jobService.getAllActiveJobsForCurrentTechnologe();
        } catch (GetAllActiveJobsForCurrentTechnologeException e) {
            logger.debug("Couldn't find for the active Technologe");
            final String summary = localeController.formatString("error.summary.jobs-not-found-by-technologe");
            final String detail = localeController.formatString("error.detail.jobs-not-found-by-technologe");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        return this.entities;
    }

    /**
     * Gets jobs that need transport.
     *
     * @return the jobs that need transport
     */
    public List<Job> getJobsThatNeedTransport() {
        return jobService.getJobsThatNeedTransport();
    }

    /**
     * Return a collection of {@link Job}s, that are processed on a given {@link ProcessChain}.
     *
     * @param processChain {@link ProcessChain} to which the collection of {@link Job}s refers.
     * @return Collection of {@link Job}s, that are processed on a given {@link ProcessChain}.
     */
    public List<Job> getByProcessChain(ProcessChain processChain) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Jobs by provided ProcessChain");
            this.entities = jobService.getByProcessChain(processChain);
        } catch (FindByException e) {
            logger.debug("Couldn't find Jobs by provided ProcessChain");
            final String summary = localeController.formatString("error.summary.jobs-not-found-by-chain");
            final String detail = localeController.formatString("error.detail.jobs-not-found-by-chain");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        return this.entities;
    }

    /**
     * Return a collection of {@link Job}s, that are currently waiting for verification by {@link de.unibremen.swp2.kcb.model.Role#LOGISTIKER}.
     *
     * @return a collection of {@link Job}s, that are currently waiting for verification.
     */
    public List<Job> getPending() {
        this.entities = new ArrayList<>();

        logger.debug("Querying pending Jobs");
        this.entities = jobService.getPending();
        this.entities.sort((j1, j2) -> j2.getPriority().getValue() - j1.getPriority().getValue());
        return this.entities;
    }

    /**
     * Generate a json protocol of a given job and return it as StreamedContent.
     * This can be used to get a download file e.g. for PrimeFaces
     *
     * @param job to return protocol for
     * @return StreamedContent of protocol for given job.
     */
    public StreamedContent generateProtocol(final Job job) {

        if (job == null) {
            super.displayMessageFromResource("error.summary.protocol-failed", "error.detail.protocol-failed", FacesMessage.SEVERITY_ERROR);
            logger.debug("Cannot generate protocol for null job.");
            return null;
        }

        File tmpFile = null;
        try {
            tmpFile = this.jobService.generateProtocol(job);
            final String downloadName = "protocol_" + job.getName().toLowerCase() + ".json";
            return new DefaultStreamedContent(new FileInputStream(tmpFile), "application/json", downloadName);
        } catch (FileNotFoundException e) {
            if (tmpFile != null)
                logger.debug("FileNotFoundException occurred for {}", tmpFile.getName());
            else
                logger.debug("FileNotFoundException occurred. File was null. {}", e.getMessage());
            super.displayMessageFromResource("error.summary.download-failed", "error.detail.download-failed", FacesMessage.SEVERITY_ERROR);
        } catch (ProtocolGenerationException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.protocol-failed", "error.detail.protocol-failed", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Export the given Job as a file containing json in the
     * format the sfb uses.
     *
     * @param job to export parameters for
     * @return StreamedContent of parameters for given job.
     */
    public StreamedContent exportParameters(final Job job) {

        String expFail = "error.summary.export-failed";
        String expDetail = "error.detail.export-failed";

        try {
            File tmpFile = this.jobService.exportParameters(job);
            final String downloadName = "parameters.json";

            if (tmpFile == null) {
                super.displayMessageFromResource(expFail, expDetail, FacesMessage.SEVERITY_ERROR);
                return null;
            }

            return new DefaultStreamedContent(new FileInputStream(tmpFile), "application/json", downloadName);

        } catch (ExportException | FileNotFoundException e) {
            logger.debug(e);
            super.displayMessageFromResource(expFail, expDetail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Return URL to the currently active workstation for a given job.
     *
     * @param job to get workstation of
     * @return url to currently active workstation or '#' if workstation is null;
     */
    public String getWorkstationRedirectURL(final Job job) {
        Procedure activeProcedure = jobService.getCurrentProcedure(job);
        if (activeProcedure == null) return "#";
        Workstation activeWorkstation = activeProcedure.getProcessStep() != null ? activeProcedure.getProcessStep().getWorkstation() : null;
        return activeWorkstation != null ? "workstation.xhtml?id=" + activeWorkstation.getId() : "#";
    }

    /**
     * Gets all active jobs for current technologe that are not currently being transported.
     *
     * @return all active jobs for current technologe
     */
    public List<Job> getAllActiveJobForCurrentTechnologeNotBeingTransported() {
        try {
            logger.debug("Querying all active Jobs for current Technologe.");
            this.entities = jobService.getAllActiveJobForCurrentTechnologeNotBeingTransported();
        } catch (GetAllActiveJobsForCurrentTechnologeException e) {
            logger.debug("Couldn't find Jobs for Technologe for transport");
            final String summary = localeController.formatString("error.summary.jobs-not-found-by-technologe");
            final String detail = localeController.formatString("error.detail.jobs-not-found-by-technologe");
            super.displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities.sort((j1, j2) -> j2.getPriority().getValue() - j1.getPriority().getValue());
        return this.entities;
    }

    /**
     * Returns 3 jobs with the highest priority.
     *
     * @return the three most important jobs
     */
    public List<Job> getThreeMostImportantJobs() {
        return jobService.getThreeMostImportantJobs();
    }
}