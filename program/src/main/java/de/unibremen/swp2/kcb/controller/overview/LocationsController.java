package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.service.LocationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link Location}s.
 *
 * @author Marc
 */
@Named
@RequestScoped
public class LocationsController extends OverviewController<Location> {

    /**
     * Logger object of the WorkstationsController class
     */
    private static final Logger logger = LogManager.getLogger(LocationsController.class);

    /**
     * Injected instance of {@link LocationService} to handle business logic
     */
    @Inject
    private LocationService locationService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh method of locationsController
     */
    @Override
    public void refresh() {
        this.entities = new ArrayList<>();

        try {
            this.entities = locationService.getAll();
        } catch (FindByException e) {
            logger.debug("Could not refresh Locations.");
            return;
        }
        if (this.entities == null || this.entities.isEmpty()) {
            logger.debug("Locations couldn't be loaded. LocationsController is empty.");
            super.displayMessageFromResource("error.summary.empty-locations",
                    "error.detail.empty-locations", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Gets all stocks and workstations in LocationsController.
     *
     * @return the all stocks and workstations
     */
    public List<Location> getAllStocksAndWorkstations() {
        return locationService.getAllStocksAndWorkstations();
    }

    /**
     * gets the location by its ID
     * @param id the id
     * @return the location
     */
    @Override
    public Location getById(String id) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Location by provided ID");
            return locationService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.location-by-id-not-found",
                    "error.summary.location-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }
}
