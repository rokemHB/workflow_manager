package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.service.CardinalValueService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.parameter.CardinalValue}.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
public class CardinalValueController extends SingleController<CardinalValue> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(CardinalValueController.class);

    /**
     * Value Service to handle actions
     */
    @Inject
    private CardinalValueService cardinalValueService;

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            cardinalValueService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during cardinalValue creation.", e);
            final String cardinalValue = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.cardinalValue-creation");
            final String detail = localeController.formatString("error.detail.cardinalValue-creation");
            final String updatedDetail = detail != null ? detail.replace("<cardinalValue>", cardinalValue) : null;
            displayMessage(summary, updatedDetail, cardinalValue, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            cardinalValueService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during cardinalValue deletion.", e);
            final String cardinalValue = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.cardinalValue-deletion");
            final String detail = localeController.formatString("error.detail.cardinalValue-deletion");
            final String updatedDetail = detail != null ? detail.replace("<cardinalValue>", cardinalValue) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            cardinalValueService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occured during cardinalValue update.", e);
            final String cardinalValue = this.getSaveValue(entity);
            final String summary = localeController.formatString("error.summary.cardinalValue-update");
            final String detail = localeController.formatString("error.detail.cardinalValue-update");
            final String updatedDetail = detail != null ? detail.replace("<cardinalValue>", cardinalValue) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Get the value of the given entity and check for null references while doing so.
     *
     * @param entity to get value of
     * @return value if entity has one, 'null' otherwise
     */
    private String getSaveValue(final CardinalValue entity) {
        if (entity == null) return "null";
        if (entity.getValue() == null) return "null";
        return entity.getValue();
    }

    /**
     * Get CardinalValue by ID
     *
     * @param id the ID
     * @return CardinalValue with that ID
     */
    public CardinalValue getById(String id) {
        CardinalValue result = null;

        try {
            result = cardinalValueService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.cardinalvalue-by-id-not-found",
                    "error.detail.cardinalvalue-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }
        return result;
    }
}