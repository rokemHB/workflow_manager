package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.service.CarrierTypeService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class CarrierTypeController
 *
 * @author Marc
 * @author Marius
 */
@Named
@ViewScoped
public class CarrierTypeController extends SingleController<CarrierType> {

    /**
     * Logger object of the CarrierTypeController class
     */
    private static final Logger logger = LogManager.getLogger(CarrierTypeController.class);

    /**
     * CarrierType Service to handle actions
     */
    @Inject
    private CarrierTypeService carrierTypeService;

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            carrierTypeService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occured during CarrierType creation.", e);
            final String carrierTypeName = this.getSaveName(entity);
            final String summary = localeController.formatString("error.summary.carriertype-creation");
            final String detail = localeController.formatString("error.detail.carriertype-creation");
            final String updatedDetail = detail != null ? detail.replace("<carrierTypeName>", carrierTypeName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during CarrierType creation.", e);
            final String carrierTypeName = this.getSaveName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", carrierTypeName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            carrierTypeService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occured during CarrierType deletion.", e);
            final String carrierTypeName = this.getSaveName(entity);
            final String summary = localeController.formatString("error.summary.carriertype-deletion");
            final String detail = localeController.formatString("error.detail.carriertype-deletion");
            final String updatedDetail = detail != null ? detail.replace("<carrierTypeName>", carrierTypeName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            carrierTypeService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occured during CarrierType update.", e);
            final String carrierTypeName = this.getSaveName(entity);
            final String summary = localeController.formatString("error.summary.carriertype-update");
            final String detail = localeController.formatString("error.detail.carriertype-update");
            final String updatedDetail = detail != null ? detail.replace("<carrierTypeName>", carrierTypeName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * reset method of CarrierTypeController
     */
    public void reset() {
        this.entity = new CarrierType();
    }

    /**
     * updates the entity of carrierTypeController by a given carrier
     * @param entity the carrierType
     */
    public void updateEntity(CarrierType entity) {
        this.entity = entity;
        super.render("carriertype_edit_form");
    }

    /**
     * Get the id of the given entity and check for null references while doing so.
     *
     * @param entity to get id of
     * @return id if entity has one, 'null' otherwise
     */
    private String getSaveName(final CarrierType entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Returns if the CarrierType is currently not in use and can be deleted.
     *
     * @param carrierType the carrierType
     * @return the boolean
     */
    public boolean canDelete(CarrierType carrierType) {
        return carrierTypeService.canDelete(carrierType);
    }
}
