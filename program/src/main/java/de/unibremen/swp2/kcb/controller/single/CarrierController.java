package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.service.CarrierService;
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
 * Controller class to handle single {@link de.unibremen.swp2.kcb.model.Carrier}.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("carrierController")
public class CarrierController extends SingleController<Carrier> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(CarrierController.class);

    /**
     * Carrier Service to handle actions
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            carrierService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occured during Carrier creation.", e);
            final String carrierID = this.getSaveId(entity);
            final String summary = localeController.formatString("error.summary.carrier-creation");
            final String detail = localeController.formatString("error.detail.carrier-creation");
            final String updatedDetail = detail != null ? detail.replace("<carrierID>", carrierID) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("Error occurred during Carrier creation.", e);
            final String carrierID = this.getSaveId(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", carrierID) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            carrierService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occured during Carrier deletion.", e);
            final String carrierID = this.getSaveId(entity);
            final String summary = localeController.formatString("error.summary.carrier-deletion");
            final String detail = localeController.formatString("error.detail.carrier-deletion");
            final String updatedDetail = detail != null ? detail.replace("<carrierID>", carrierID) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            carrierService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occured during Carrier update.", e);
            final String carrierID = this.getSaveId(entity);
            final String summary = localeController.formatString("error.summary.carrier-update");
            final String detail = localeController.formatString("error.detail.carrier-update");
            final String updatedDetail = detail != null ? detail.replace("<carrierID>", carrierID) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Can collect boolean.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canCollect(Carrier carrier) {
        return carrierService.canCollect(carrier);
    }

    /**
     * Can deliver boolean.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canDeliver(Carrier carrier) {
        return carrierService.canDeliver(carrier);
    }

    /**
     * resets the entity of carrierController
     */
    public void reset() {
        this.entity = new Carrier();
    }

    /**
     * updates the entity of carrierController by a given carrier
     * @param entity the carrier
     */
    public void updateEntity(Carrier entity) {
        this.entity = entity;
        super.render("carrier_edit_form");
    }

    /**
     * Get the id of the given entity and check for null references while doing so.
     *
     * @param entity to get id of
     * @return id if entity has one, 'null' otherwise
     */
    private String getSaveId(final Carrier entity) {
        if (entity == null) return "null";
        if (entity.getCarrierID() == null) return "null";
        return entity.getCarrierID();
    }

    /**
     * Returns if the carrier is currently not in use and can be deleted.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canDelete(Carrier carrier) {
        return carrierService.canDelete(carrier);
    }
}