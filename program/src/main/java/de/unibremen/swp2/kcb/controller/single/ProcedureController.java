package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.service.ProcedureService;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a single {@link Procedure}.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("procedureController")
public class ProcedureController extends SingleController<Procedure> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(ProcedureController.class);

    /**
     * Procedure Service to handle actions
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * Execute the currently selected {@link Procedure}.
     *
     * @param procedure the procedure
     */
    public void doExec(Procedure procedure) {
        try {
            procedureService.doExec(procedure);
        } catch (ExecutionException e) {
            logger.debug("Error occurred during Procedure execution.", e);
            final String uuid = this.getSaveUUID(entity);
            final String summary = localeController.formatString("error.summary.procedure-exec");
            final String detail = localeController.formatString("error.detail.procedure-exec");
            final String updatedDetail = detail != null ? detail.replace("<uuid>", uuid) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Returns if the procedure is currently in the last executable state and creates a new assembly.
     * This method is needed for frontend purposes.
     *
     * @param procedure the procedure
     * @return the boolean
     */
    public boolean isInLastExecutableStateAndCreates(Procedure procedure) {
        return procedureService.isInLastExecutableStateAndCreates(procedure);
    }

    /**
     * Do finish.
     *
     * @param procedure the procedure
     */
    public void doFinish(Procedure procedure, Integer transitionTime) {
        try {
            procedureService.doFinish(procedure, transitionTime);
        } catch (ExecutionException e) {
            logger.debug(e.getMessage());
            e.getMessage();
        }
    }

    /**
     * Can execute boolean.
     *
     * @param procedure the procedure
     * @return the boolean
     */
    public boolean canExecute(Procedure procedure) {
        if (procedure == null) return false;
        return procedureService.canExecute(procedure);
    }

    /**
     * Can finish boolean.
     *
     * @param procedure the procedure
     * @return the boolean
     */
    public boolean canFinish(Procedure procedure) {
        if (procedure == null) return false;
        return procedureService.canFinish(procedure);
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            procedureService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during Procedure creation.", e);
            final String uuid = this.getSaveUUID(entity);
            final String summary = localeController.formatString("error.summary.procedure-creation");
            final String detail = localeController.formatString("error.detail.procedure-creation");
            final String updatedDetail = detail != null ? detail.replace("<uuid>", uuid) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            procedureService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during Procedure deletion.", e);
            final String uuid = this.getSaveUUID(entity);
            final String summary = localeController.formatString("error.summary.procedure-deletion");
            final String detail = localeController.formatString("error.detail.procedure-deletion");
            final String updatedDetail = detail != null ? detail.replace("<uuid>", uuid) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            procedureService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during Procedure update.", e);
            final String uuid = this.getSaveUUID(this.entity);
            final String summary = localeController.formatString("error.summary.procedure-update");
            final String detail = localeController.formatString("error.detail.procedure-update");
            final String updatedDetail = detail != null ? detail.replace("<uuid>", uuid) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * gets the save UUID for the procedure
     * @param entity the procedure
     * @return the uuid
     */
    private String getSaveUUID(final Procedure entity) {
        if (entity == null) return "null";
        if (entity.getId() == null) return "null";
        return entity.getId();
    }

    /**
     * Updates the entity with a given Procedure
     *
     * @param entity the Procedure to be updated
     */
    public void updateEntity(Procedure entity) {
        this.entity = entity;
        super.render("add_assembly_for_job_form");
        super.render("view_parameters_form");
    }

    /**
     * Gets by workstation.
     *
     * @param workstation the workstation
     * @return the by workstation
     */
    public List<Procedure> getByWorkstation(final Workstation workstation) {
        try {
            return procedureService.getByWorkstation(workstation);
        } catch (FindByException e) {
            logger.debug(e.getMessage());
            e.getMessage();
        }
        return null;
    }

    /**
     * Gets active by workstation.
     *
     * @param workstation the workstation
     * @return the active by workstation
     */
    public List<Procedure> getActiveByWorkstation(final Workstation workstation) {
        try {
            return procedureService.getActiveByWorkstation(workstation);
        } catch (FindByException e) {
            logger.debug(e.getMessage());
            e.getMessage();
        }
        return new ArrayList<>();
    }
}