package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Role;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.JobService;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.WorkstationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link Workstation}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@Named
@RequestScoped
public class WorkstationsController extends OverviewController<Workstation> {

    /**
     * Logger object of the WorkstationsController class
     */
    private static final Logger logger = LogManager.getLogger(WorkstationsController.class);

    /**
     * Injected instance of {@link WorkstationService} to handle business logic
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * Injected instance of {@link JobService} to handle business logic
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link UserService} to handle business logic
     */
    @Inject
    private UserService userService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Return a collection of all active {@link Workstation}s.
     *
     * @return Collection of active {@link Workstation}s.
     */
    public List<Workstation> getActive() {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying all active Workstations.");
            this.entities = workstationService.getActive();
        } catch (FindByException e) {
            logger.info("Could not query all active Workstations.");
            return this.entities;
        }

        return this.entities;
    }

    /**
     * Return a collection of all inactive {@link Workstation}s.
     *
     * @return Collection of inactive {@link Workstation}s.
     */
    public List<Workstation> getInactive() {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying all inactive Workstations.");
            this.entities = workstationService.getInactive();
        } catch (FindByException e) {
            logger.info("Could not query all inactive Workstations.");
            return this.entities;
        }

        return this.entities;
    }

    /**
     * Return a collection of all {@link Workstation}s the current User (TECHNOLOGE) is assigned to.
     *
     * @return Collection of assigned {@link Workstation}s
     */
    public List<Workstation> getAllForCurrentTechnologe() {
        this.entities = new ArrayList<>();
        User currentUser = userService.getExecutingUser();

        // Admin should have access to all workstations
        if (currentUser.getRoles().contains(Role.ADMIN))
            return this.workstationService.getAll();

        // User isn't admin or technologe, shouldn't have access to workstation
        if (!currentUser.getRoles().contains(Role.TECHNOLOGE)) {
            logger.trace("Current User is not a Technologe!");
            return new ArrayList<>();
        }

        try {
            logger.debug("Querying all assigned Workstations.");
            this.entities = workstationService.getByUser(currentUser);
        } catch (FindByException e) {
            logger.info("Could not query all assigned Workstations.");
            return this.entities;
        }

        return this.entities;
    }

    /**
     * Refresh the collection of all {@link Workstation}s.
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();


        this.entities = workstationService.getAll();

        if (this.entities == null || this.entities.isEmpty()) {
            logger.debug("Workstations couldn't be loaded. WorkstationsController is empty.");
        }
    }

    /**
     * Get Workstation by ID
     *
     * @param id the ID
     * @return Workstation with that ID
     */
    @Override
    public Workstation getById(String id) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Workstation by provided ID");
            return workstationService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.workstation-by-id-not-found",
                    "error.summary.workstation-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        } catch (UnauthorizedException e) {
            HttpServletResponse response = WebUtils.getHttpResponse(SecurityUtils.getSubject());
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IOException ex) {
                logger.debug(ex);
            }
        }

        return null;
    }

    /**
     * Find Workstations by position
     *
     * @param position of the workstation
     * @return All Workstations at given position
     */
    List<Workstation> getByPosition(String position) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Workstation by provided Location");
            this.entities = workstationService.getByPosition(position);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.workstation-by-location-not-found",
                    "error.summary.workstation-by-location-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Gets all active jobs currently running on a workstation.
     *
     * @param workstation the workstation
     * @return the active job for workstation
     */
    public List<Job> getActiveJobForWorkstation(Workstation workstation) {
        return jobService.getCurrentlyRunningByWorkstation(workstation);
    }

    /**
     * Gets upcoming jobs for workstation. This excludes current jobs at the workstation.
     *
     * @param workstation the workstation
     * @return the upcoming jobs for workstation
     */
    public List<Job> getUpcomingJobsForWorkstation(Workstation workstation) {
        return workstationService.getUpcomingJobsForWorkstation(workstation);
    }
}