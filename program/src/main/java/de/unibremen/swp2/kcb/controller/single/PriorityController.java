package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.service.PriorityService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller Class to handle a single {@link Priority}.
 *
 * @author Marius
 * @author Arvid
 */
@ViewScoped
@Named("priorityController")
public class PriorityController extends SingleController<Priority> {

    /**
     * Logger object of the PriorityController class
     */
    private static final Logger logger = LogManager.getLogger(PriorityController.class);

    @Inject
    private PriorityService priorityService;

    @PostConstruct
    public void setUp() {
        this.entity = new Priority();
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            priorityService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during priority creation.", e);
            final String priority = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.priority-creation");
            final String detail = localeController.formatString("error.detail.priority-creation");
            final String updatedDetail = detail != null ? detail.replace("<priority>", priority) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Priority creation.", e);
            final String priorityName = this.getSavePriorityName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", priorityName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            priorityService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during priority deletion.", e);
            final String priority = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.priority-deletion");
            final String detail = localeController.formatString("error.detail.priority-deletion");
            final String updatedDetail = detail != null ? detail.replace("<priority>", priority) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            priorityService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during priority deletion.", e);
            final String priority = entity != null ? entity.toString() : "";
            final String summary = localeController.formatString("error.summary.priority-update");
            final String detail = localeController.formatString("error.detail.priority-update");
            final String updatedDetail = detail != null ? detail.replace("<priority>", priority) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * reset method of PriorityController
     */
    public void reset() {
        this.setUp();
    }

    /**
     * updates the entity of PriorityController by the given Priority
     * @param entity the prio
     */
    public void updateEntity(Priority entity) {
        this.entity = entity;
        super.render("priority_edit_form");
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get name of
     * @return name of Priority if entity has one, 'null' otherwise
     */
    private String getSavePriorityName(final Priority entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Returns if the priority is unused and can be deleted.
     *
     * @param priority the priority
     * @return the boolean
     */
    public boolean canDelete(Priority priority) {
        return priorityService.canDelete(priority);
    }
}
