package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.service.LocationService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Class LocationController
 *
 * @author Marc
 */
@Named
@ViewScoped
public class LocationController extends SingleController<Location> {

    /**
     * Injected instance of locationService
     */
    @Inject
    private LocationService locationService;

    /**
     * create method not implemented
     */
    @Override
    public void create() {
        throw new UnsupportedOperationException();
    }

    /**
     * delete method not implemented
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }

    /**
     * update method not implemented
     */
    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets all assemblies by its location.
     *
     * @param location the location
     * @return the all assemblies
     * @throws FindByException the find by exception
     */
    public List<Assembly> getAllAssemblies(Location location) throws FindByException {
        return locationService.getAllAssembliesAt(location);
    }
}
