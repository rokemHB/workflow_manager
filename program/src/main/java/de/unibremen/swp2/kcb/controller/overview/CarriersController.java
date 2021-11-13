package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.service.CarrierService;
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
import java.util.List;

/**
 * Controller Class to handle a collection of {@link Carrier}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("carriersController")
public class CarriersController extends OverviewController<Carrier> {

    /**
     * Logger object of the CarriersController class
     */
    private static final Logger logger = LogManager.getLogger(CarriersController.class);

    /**
     * Injected instance of {@link CarrierService} to handle business logic
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link Carrier}s.
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();
        List<Carrier> carriers = carrierService.getAll();
        if (carriers == null || carriers.isEmpty()) {
            logger.debug("Carriers couldn't be loaded. CarriersController is empty.");
            return;
        }
        this.entities = carriers;
    }

    /**
     * Get Carrier by ID
     *
     * @param id the ID
     * @return Carrier with that ID
     */
    @Override
    public Carrier getById(String id) {
        try {
            return carrierService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.carrier-by-id-not-found",
                    "error.detail.carrier-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }

    /**
     * Find all Carriers with a certain CarrierType
     *
     * @param carrierType the CarrierType to be searched for
     * @return all Carriers with that CarrierType
     * @see CarrierType
     */
    List<Carrier> getByCarrierType(CarrierType carrierType) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Carriers by CarrierType");
            this.entities = carrierService.getByCarrierType(carrierType);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.carrier-by-type-not-found",
                    "error.detail.carrier-by-type-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Find all Carriers at a certain Location
     *
     * @param location the Location to be searched at
     * @return all Carriers at that Location
     * @see Location
     */
    List<Carrier> getByLocation(Location location) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Carriers by Location");
            this.entities = carrierService.getByLocation(location);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.carrier-by-location-not-found", //
                    "error.detail.carrier-by-location-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Find all Carriers with a certain CarrierType at certain Location
     *
     * @param carrierType the CarrierType to be searched for
     * @param location    the Location to be searched at
     * @return all Carriers with that CarrierType at that Location
     */
    List<Carrier> getByCarrierTypeAndLocation(CarrierType carrierType, Location location) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Carriers by CarrierType and Location");
            this.entities = carrierService.getByCarrierTypeAndLocation(carrierType, location);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.carrier-by-both-not-found",
                    "error.detail.carrier-by-both-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Gets unused carriers.
     *
     * @return the unused carriers
     */
    public List<Carrier> getUnusedCarriers() {
        return carrierService.getUnusedCarriers();
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    public List<Carrier> getAll() {
        return carrierService.getAll();
    }
}