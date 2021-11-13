package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.WorkstationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Controller class to handle a single {@link Workstation}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("workstationController")
public class WorkstationController extends SingleController<Workstation> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(WorkstationController.class);

    /**
     * Workstation Service to handle actions
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * setUp method of workstationController
     */
    @PostConstruct
    public void setUp() {
        final String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id != null && !id.isEmpty()) {
            try {
                this.entity = workstationService.getById(id);
            } catch (InvalidIdException e) {
                HttpServletResponse response = WebUtils.getHttpResponse(SecurityUtils.getSubject());
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } catch (IOException ex) {
                    logger.debug(ex);
                    this.entity = new Workstation();
                    entity.setBroken(false);
                    entity.setActive(true);
                }
            }
        } else {
            this.entity = new Workstation();
            entity.setBroken(false);
            entity.setActive(true);
        }
    }

    /**
     * Return a collection of all {@link de.unibremen.swp2.kcb.model.Role#TECHNOLOGE}, who are assigned to the currently
     * selected {@link Workstation}.
     *
     * @return Collection of all {@link de.unibremen.swp2.kcb.model.Role#TECHNOLOGE} assigned to current {@link Workstation}.
     */
    public Collection<User> getAllTechnologen() {
        List<User> technologen = new ArrayList<>();

        try {
            technologen = workstationService.getAllTechnologen(this.entity);
        } catch (FindByException e) {
            logger.error("Error occurred during validating Workstation.");
            final String summary = localeController.formatString("error.summary.workstation-getalltechnologen");
            displayMessage(summary, null, FacesMessage.SEVERITY_ERROR);
        }

        return technologen;
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            workstationService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during Workstation creation.", e);
            final String workstationName = this.getSaveWorkstationName(entity);
            final String summary = localeController.formatString("error.summary.workstation-creation");
            final String detail = localeController.formatString("error.detail.workstation-creation");
            final String updatedDetail = detail != null ? detail.replace("<workstationName>", workstationName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Workstation creation.", e);
            final String workstationName = this.getSaveWorkstationName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", workstationName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            workstationService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during Workstation deletion.", e);
            final String workstationName = this.getSaveWorkstationName(entity);
            final String summary = localeController.formatString("error.summary.workstation-deletion");
            final String detail = localeController.formatString("error.detail.workstation-deletion");
            final String updatedDetail = detail != null ? detail.replace("<workstationName>", workstationName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            workstationService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during Workstation update.", e);
            final String workstationName = this.getSaveWorkstationName(entity);
            final String summary = localeController.formatString("error.summary.workstation-update");
            final String detail = localeController.formatString("error.detail.workstation-update");
            final String updatedDetail = detail != null ? detail.replace("<workstationName>", workstationName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get name of
     * @return name of Workstation if entity has one, 'null' otherwise
     */
    private String getSaveWorkstationName(final Workstation entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }


    /**
     * Reset method of WorkstationController.
     */
    public void reset() {
        this.setUp();
    }

    /**
     * setEntity of WorkstationController
     * @param entity
     */
    public void setEntity(Workstation entity) {
        this.entity = entity;
    }

    /**
     * Handle f:selectItems from workstations Facelet.
     *
     * @param ids of selected users
     */
    public void setUsers(String[] ids) {
        this.entity.setUsers(userService.getByIds(Arrays.asList(ids)));
    }

    /**
     * Method is needed to prevent flame from jsf.
     *
     * @return the users as array
     */
    public String[] getUsers() {
        // LEAVE ME HERE :)
        return new String[0];
    }

    /**
     * Updates this entity by a given workstation entity
     * @param entity the workstation
     */
    public void updateEntity(Workstation entity) {
        this.entity = entity;
        super.render("workstation_edit_form");
    }

    /**
     * Gets active job count.
     *
     * @param workstation the workstation
     * @return the active job count
     */
    public int getActiveJobCount(Workstation workstation) {
        return workstationService.getActiveJobCount(workstation);
    }

    /**
     * Gets active job workload.
     *
     * @param workstation the workstation
     * @return the active job workload
     */
    public int getActiveJobWorkload(Workstation workstation) {
        return workstationService.getActiveJobWorkload(workstation);
    }

    /**
     * Gets active job assemblies count.
     *
     * @param workstation the workstation
     * @return the active job assemblies count
     */
    public int getActiveJobAssembliesCount(Workstation workstation) {
        return workstationService.getActiveJobAssembliesCount(workstation);
    }

    /**
     * Toggle the active state of a given workstation.
     */
    public void toggleActive() {
        this.entity.setActive(!this.entity.getActive());
        try {
            this.workstationService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.change-failed", "error.detail.change-failed", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Toggle the active state of a given workstation.
     */
    public void toggleBroken() {
        this.entity.setBroken(!this.entity.getBroken());
        try {
            this.workstationService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.change-failed", "error.detail.change-failed", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Handle single page select for a given ValueChangeEvent.
     *
     * @param e the e
     */
    public void handleSinglePageSelect(ValueChangeEvent e) {
        if (!((e.getNewValue()) instanceof Workstation))
            super.redirect("workstation");

        Workstation selected = (Workstation) e.getNewValue();
        if (selected != null && selected.getId() != null)
            super.redirect("workstation.xhtml", selected.getId());
        else
            super.redirect("workstation.xhtml");
    }

    /**
     * Returns if a workstation can be deleted.
     *
     * @param workstation the workstation
     * @return the boolean
     */
    public boolean canDelete(Workstation workstation) {
        return workstationService.canDelete(workstation);
    }
}