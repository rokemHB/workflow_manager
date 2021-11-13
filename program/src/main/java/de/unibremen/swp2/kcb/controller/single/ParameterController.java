package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ParameterService;
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
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.parameter.Parameter}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@Named("parameterController")
@ViewScoped
public class ParameterController extends SingleController<Parameter> {


    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(ParameterController.class);

    /**
     * Value Service to handle actions
     */
    @Inject
    private ParameterService parameterService;

    /**
     * Sets up.
     */
    @PostConstruct
    public void setUp() {
        this.reset();
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            parameterService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during parameter creation.", e);
            final String parameter = this.getSaveField(entity);
            final String summary = localeController.formatString("error.summary.parameter-creation");
            final String detail = localeController.formatString("error.detail.parameter-creation");
            final String updatedDetail = detail != null ? detail.replace("<parameter>", parameter) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Parameter creation.", e);
            final String parameterField = this.getSaveField(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", parameterField) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            parameterService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during parameter deletion.", e);
            final String parameter = this.getSaveField(entity);
            final String summary = localeController.formatString("error.summary.parameter-deletion");
            final String detail = localeController.formatString("error.detail.parameter-deletion");
            final String updatedDetail = detail != null ? detail.replace("<parameter>", parameter) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            parameterService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occured during parameter update.", e);
            final String parameter = this.getSaveField(entity);
            final String summary = localeController.formatString("error.summary.parameter-update");
            final String detail = localeController.formatString("error.detail.parameter-update");
            final String updatedDetail = detail != null ? detail.replace("<parameter>", parameter) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Resets the entity with an empty ProcessStep
     */
    public void reset() {
        this.entity = new Parameter();
    }

    /**
     * Get the field of the given entity and check for null references while doing so.
     *
     * @param entity to get field of
     * @return field if entity has one, 'null' otherwise
     */
    private String getSaveField(final Parameter entity) {
        if (entity == null) return "null";
        if (entity.getField() == null) return "null";
        return entity.getField();
    }

    /**
     * Updates the entity with a given parameter
     *
     * @param entity the parameter to be updated
     */
    public void updateEntity(Parameter entity) {
        this.entity = entity;
        super.render("parameter_edit_form");
    }

    /**
     * Returns if a given parameter can be deleted.
     *
     * @param parameter the parameter
     * @return the boolean
     */
    public boolean canDelete(Parameter parameter) {
        return parameterService.canDelete(parameter);
    }
}