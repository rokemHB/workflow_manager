package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.service.ValueService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.parameter.Value}.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@Named
public class ValueController extends SingleController<Value> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(ValueController.class);

    /**
     * Value Service to handle actions
     */
    @Inject
    private ValueService valueService;


    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            valueService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during value creation.", e);
            final String value = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.value-creation");
            final String detail = localeController.formatString("error.detail.value-creation");
            final String updatedDetail = detail != null ? detail.replace("<value>", value) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            valueService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during value deletion.", e);
            final String value = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.value-deletion");
            final String detail = localeController.formatString("error.detail.value-deletion");
            final String updatedDetail = detail != null ? detail.replace("<value>", value) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            valueService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occured during value update.", e);
            final String value = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.value-update");
            final String detail = localeController.formatString("error.detail.value-update");
            final String updatedDetail = detail != null ? detail.replace("<value>", value) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get value of
     * @return value if entity has one, 'null' otherwise
     */
    private String getSaveValue(final Value entity) {
        if (entity == null) return "null";
        if (entity.getValue() == null) return "null";
        return entity.getValue();
    }

    public boolean isCardinalValue(final Value value) {
        return value instanceof CardinalValue;
    }
}