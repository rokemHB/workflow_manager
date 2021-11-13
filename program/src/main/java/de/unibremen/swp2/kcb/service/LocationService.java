package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.persistence.locations.StockRepository;
import de.unibremen.swp2.kcb.persistence.locations.TransportRepository;
import de.unibremen.swp2.kcb.persistence.locations.WorkstationRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle locations
 *
 * @author Marc
 * @author Robin
 */
@Transactional
public class LocationService implements Service<Location> {

    /**
     * Injected instance of workstationRepository
     */
    @Inject
    private WorkstationRepository workstationRepository;

    /**
     * Injected instance of transportRepository
     */
    @Inject
    private TransportRepository transportRepository;

    /**
     * Injected instance of stockRepository
     */
    @Inject
    private StockRepository stockRepository;

    /**
     * Injected instance of assemblyService
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * create method of locationService
     * @param entity to be created and persisted
     * @return nothing
     * @throws CreationException just in case
     */
    @Override
    public Location create(Location entity) throws CreationException {
        return null;
    }

    /**
     * Update method of locationService
     * @param entity to be updated
     * @return nothing
     * @throws UpdateException just in case
     */
    @Override
    public Location update(Location entity) throws UpdateException {
        return null;
    }

    /**
     * Injected instance of
     * @param entity to be deleted
     * @throws DeletionException just in case
     */
    @Override
    public void delete(Location entity) throws DeletionException {
        throw new UnsupportedOperationException("method should never be called");
    }

    /**
     * Returns a List containing all stored Locations
     * @return all stored Locations
     * @throws FindByException if something went wrong
     */
    public List<Location> getAll() throws FindByException {
        List<Location> result = new ArrayList<>();

        result.addAll(workstationRepository.findAll());
        result.addAll(transportRepository.findAll());
        result.addAll(stockRepository.findAll());

        return result;
    }

    /**
     * Returns a List containing all stores Stocks and Workstations
     * @return all stored Stocks and Workstations
     */
    public List<Location> getAllStocksAndWorkstations() {
        List<Location> result = new ArrayList<>();

        result.addAll(workstationRepository.findAll());
        result.addAll(stockRepository.findAll());

        return result;
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     * @throws InvalidIdException the invalid id exception
     */
    public Location getById(String id) throws InvalidIdException {
        Location result = stockRepository.findBy(id);

        if(result == null)
            result = workstationRepository.findBy(id);

        if(result == null)
            result = transportRepository.findBy(id);

        return result;
    }

    /**
     * Finds all Assemblies at a given Location
     * @param location to be searched at
     * @return A List of all Assemblies on the provided Location
     */
    public List<Assembly> getAllAssembliesAt(Location location) throws FindByException {
        List<Assembly> assembliesAtGivenLocation = new ArrayList<>();

        for(Assembly a : assemblyService.getAll()) {
            for(Carrier c : a.getCarriers()) {
                if(c.getLocation().equals(location)) {
                    assembliesAtGivenLocation.add(a);
                }
            }
        }

        return assembliesAtGivenLocation;
    }
}
