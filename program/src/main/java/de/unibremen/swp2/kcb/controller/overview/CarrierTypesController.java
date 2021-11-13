package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.service.CarrierTypeService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;

/**
 * Controller Class to handle a collection of {@link CarrierType}s.
 *
 * @author Marc
 * @author Arvid
 */
@Named
@ViewScoped
public class CarrierTypesController extends OverviewController<CarrierType> {

    /**
     * Logger object of the CarriersController class
     */
    private static final Logger logger = LogManager.getLogger(CarrierTypesController.class);

    /**
     * Injected instance of {@link CarrierTypeService} to handle business logic
     */
    @Inject
    private CarrierTypeService carrierTypeService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link CarrierType}s.
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();
        try {
            this.entities = carrierTypeService.getAll();
        } catch (FindByException e) {
            logger.debug("Could not refresh carrierTypes.");
            return;
        }
        if (this.entities == null || this.entities.isEmpty()) {
            logger.debug("CarrierTypes couldn't be loaded. CarrierTypesController is empty.");
            super.displayMessageFromResource("error.summary.empty-carrierTypes",
                    "error.detail.empty-carrierTypes", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Get CarrierType by ID
     *
     * @param id the ID
     * @return CarrierType with that ID
     */
    @Override
    public CarrierType getById(String id) {
        try {
            return carrierTypeService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.carrierType-by-id-not-found",
                    "error.detail.carrierType-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }

    /**
     * Get CarrierType by name
     *
     * @param name the name
     * @return CarrierType with that name
     */
    public CarrierType getByName(String name) {
        try {
            logger.debug("Querying carrierType by provided name");
            return carrierTypeService.getByName(name);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.carrierType-by-name-not-found",
                    "error.summary.carrierType-by-name-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }
}