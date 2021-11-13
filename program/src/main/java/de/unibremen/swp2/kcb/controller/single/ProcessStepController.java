package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.ParameterService;
import de.unibremen.swp2.kcb.service.ProcessStepService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.ProcessStep}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@Named
@ViewScoped
public class ProcessStepController extends SingleController<ProcessStep> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(ProcessStepController.class);

    /**
     * ProcessStep Service to handle actions
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Procedure Service to handle actions
     */
    @Inject
    private ParameterService parameterService;

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            processStepService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during ProcessStep creation.", e);
            final String processStepName = this.getProcessStepName(entity);
            final String summary = localeController.formatString("error.summary.processstep-creation");
            final String detail = localeController.formatString("error.detail.processstep-creation");
            final String updatedDetail = detail != null ? detail.replace("<processStepName>", processStepName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("ProcessStep with that name already exists.", e);
            final String processChainName = this.getProcessStepName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", processChainName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            processStepService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during ProcessStep deletion.", e);
            final String processStepName = this.getProcessStepName(entity);
            final String summary = localeController.formatString("error.summary.processstep-deletion");
            final String detail = localeController.formatString("error.detail.processstep-deletion");
            final String updatedDetail = detail != null ? detail.replace("<processStepName>", processStepName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            processStepService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during ProcessStep update.", e);
            final String processStepName = this.getProcessStepName(this.entity);
            final String summary = localeController.formatString("error.summary.processstep-update");
            final String detail = localeController.formatString("error.detail.processstep-update");
            final String updatedDetail = detail != null ? detail.replace("<processStepName>", processStepName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Sets up.
     */
    @PostConstruct
    public void setUp() {
        this.reset();
    }

    /**
     * Resets the entity with an empty ProcessStep
     */
    public void reset() {
        this.entity = new ProcessStep();
    }

    /**
     * Updates the entity with a given processStep
     *
     * @param entity the processStep to be updated
     */
    public void updateEntity(ProcessStep entity) {
        this.entity = entity;
        super.render("processStep_edit_form");
        super.render("processStep_view_form");
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get name of
     * @return name if entity has one, 'null' otherwise
     */
    private String getProcessStepName(final ProcessStep entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Method is needed to prevent flame from jsf.
     *
     * @return
     */
    public List<String> getParameters() {
        return new ArrayList<>();
    }

    /**
     * Handle f:selectItems from workstations Facelet.
     *
     * @param ids of selected users
     */
    public void setParameters(List<String> ids) {
        super.entity.setParameters(parameterService.getByIds(ids));
    }

    /**
     * Returns if a given processStep can be deleted.
     *
     * @param processStep the process step
     * @return the boolean
     */
    public boolean canDelete(ProcessStep processStep) {
        return processStepService.canDelete(processStep);
    }
}