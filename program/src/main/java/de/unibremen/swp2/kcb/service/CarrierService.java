package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.persistence.CarrierRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service class to handle Carriers.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
public class CarrierService implements Service<Carrier> {

    /**
     * Logger object of the CarrierService class
     */
    private static final Logger logger = LogManager.getLogger(CarrierService.class);

    /**
     * Injected instance of {@link CarrierValidator} to validate provided {@link Carrier}s.
     */
    @Inject
    private CarrierValidator carrierValidator;

    /**
     * Injected instance of {@link CarrierTypeValidator} to validate provided {@link CarrierType}s.
     */
    @Inject
    private CarrierTypeValidator carrierTypeValidator;

    /**
     * Injected instance of {@link LocationValidator} to validate provided {@link Location}s.
     */
    @Inject
    private LocationValidator locationValidator;

    /**
     * Injected instance of {@link CarrierRepository} to query database for {@link Carrier}s.
     */
    @Inject
    private CarrierRepository carrierRepository;

    /**
     * Injected instance of {@link UserService}
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of {@link TransportService}
     */
    @Inject
    private TransportService transportService;

    /**
     * Injected instance of {@link AssemblyService}
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Injected instance of {@link StateHistoryService}
     */
    @Inject
    private StateHistoryService stateHistoryService;

    /**
     * Injected instance of {@link JobService}
     */
    @Inject
    private JobService jobService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Carrier that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see Validator
     */
    @RequiresAuthentication
    @Override
    public Carrier create(final Carrier entity) throws CreationException, EntityAlreadyExistingException {

        //Validating carrier
        boolean valid;
        try {
            logger.trace("Attempting to validate carrier \"{}\" ...", entity);
            valid = carrierValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during carrier validation. Can't create carrier.");
            throw new CreationException("Can't create carrier: " + e.getMessage());
        }
        logger.trace("Validation of carrier \"{}}\" completed without exceptions.", entity);

        //Checking, if carrier is valid
        if (!valid) {
            final String message = "Carrier \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Carrier \"{}\" is valid.", entity);

        //Checking, if carrier is already stored in database
        try {
            carrierRepository.findByCarrierID(entity.getCarrierID());

            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create Carrier: Entity is already stored in datasource");
        }
        catch (NoResultException ex) {  // If Entity does not exist
            //Saving carrier
            Carrier repoEntity;
            try {
                logger.trace("Attempting to save carrier \"{}\" ...", entity);
                repoEntity = carrierRepository.save(entity);
            } catch (PersistenceException e) {
                logger.debug("Error occurred while persisting carrier \"{}\". Can't create carrier.", entity);
                throw new CreationException("Can't create carrier: " + e.getMessage());
            }

            logger.trace("Saving of carrier \"{}\" completed without exceptions.", entity);
            logger.info("Create carrier \"{}\" - triggered by: {}", entity.getCarrierID(), userService.getExecutingUser().getUsername());
            logger.trace("Returning carrier \"{}\"", entity);
            return repoEntity;
        }
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @throws UpdateException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    @RequiresAuthentication
    public Carrier update(final Carrier entity) throws UpdateException {

        //Validating carrier
        boolean valid;
        try {
            logger.trace("Attempting to validate carrier \"{}\" ...", entity);
            valid = carrierValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating carrier \"{}\". Can't update carrier", entity);
            throw new UpdateException("Can't update carrier: " + e.getMessage());
        }
        logger.trace("Validation of carrier \"{}\" completed without exceptions.", entity);

        //Checking, if carrier is valid
        if (!valid) {
            final String message = "Carrier \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Carrier \"{}\" is valid.", entity);

        //Checking, if carrier is stored in database
        final Carrier storedCarrier = carrierRepository.findBy(entity.getId());
        if (storedCarrier == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving carrier
        Carrier repoEntity;
        try {
            logger.trace("Attempting to save new carrier \"{}\" ...", entity);
            repoEntity = carrierRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting carrier \"{}\". Can't update carrier.", entity);
            throw new UpdateException("Can't update carrier: " + e.getMessage());
        }

        logger.trace("Saving of carrier \"{}\" completed without exceptions.", entity);
        logger.info("Update carrier \"{}\" - triggered by: {}", entity.getCarrierID(), userService.getExecutingUser().getUsername());
        logger.trace("Returning carrier \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    @RequiresAuthentication
    public void delete(final Carrier entity) throws DeletionException {

        //Checking, if carrier is stored in database
        final Carrier storedCarrier = carrierRepository.findBy(entity.getId());
        if (storedCarrier == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting carrier
        try {
            logger.trace("Attempting to delete carrier \"{}\" ...", entity);
            carrierRepository.attachAndRemove(entity);
            logger.info("Delete carrier \"{}\" triggered by: {}", entity.getCarrierID(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting carrier \"{}\". Can't delete carrier.", entity);
            throw new DeletionException("Can't delete carrier: " + e.getMessage());
        }
    }

    /**
     * Return the {@link Carrier} with the given id.
     *
     * @param id of the Carrier
     * @return Carrier with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Carrier getById(String id) throws InvalidIdException {

        //Validating carrierID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "CarrierID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("CarrierID \"{}\" is valid.", id);

        //Finding carrier by ID
        final Carrier entity;
        try {
            logger.trace("Attempting to find carrier by ID \"{}\" ...", id);
            entity = carrierRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding carrier by ID \"{}\". Can't find carrier.", id);
            throw new InvalidIdException("Can't find carrier: " + e.getMessage());
        }

        logger.trace("Finding of carrier by ID \"{}\" completed without exceptions.", id);
        logger.info("Find carrier by ID \"{}\" - triggered by: {}", entity.getCarrierID(), userService.getExecutingUser().getUsername());
        logger.trace("Returning carrier by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return all Carriers with a given {@link CarrierType}.
     *
     * @param carrierType of the Carriers to be returned.
     * @return Collection of Carriers with the given type.
     * @throws FindByException thrown if validating given parameters fails
     */
    public List<Carrier> getByCarrierType(final CarrierType carrierType)
            throws FindByException {
        logger.debug("Validating provided CarrierType...");
        boolean valid;

        try {
            valid = carrierTypeValidator.validate(carrierType);
        } catch (ValidationException e) {
            logger.debug("Provided CarrierType could not be validated.");
            throw new FindByException("Can't validate provided CarrierType.");
        }

        //Checking, if carrier is valid
        if (!valid) {
            final String message = "CarrierType \"" + carrierType.getName() + "\" is not valid.";
            logger.debug(message);
            throw new FindByException(message);
        } else logger.trace("Carrier \"{}\" is valid.", carrierType.getName());

        logger.debug("Trying to query Carrier(s) by CarrierType {}.",  carrierType.getName());
        return carrierRepository.findByCarrierType(carrierType);
    }

    /**
     * Return all carriers at a given {@link Location}.
     *
     * @param location of the Carriers to be returned.
     * @return Collection of Carriers at given Location.
     * @throws FindByException thrown if validating parameters fails
     */
    public List<Carrier> getByLocation(final Location location) throws FindByException {
        logger.debug("Validating provided Location...");

        try {
            locationValidator.validate(location);
        } catch (ValidationException e) {
            logger.debug("Provided Location could not be validated. Returning null.");
            throw new FindByException("Can't validate provided Location.");
        }
        logger.debug("Trying to query Carrier(s) by Location {}.", location.getPosition());
        return carrierRepository.findByLocation(location);
    }

    /**
     * Return all carriers at given {@link CarrierType} and {@link Location}.
     *
     * @param carrierType of the Carriers to be returned
     * @param location    of the Carriers to be returned
     * @return Collection of Carriers with given CarrierType and Location
     * @throws FindByException thrown if validating parameters fails
     */
    public List<Carrier> getByCarrierTypeAndLocation(CarrierType carrierType, Location location) throws FindByException {
        logger.debug("Validating provided CarrierType...");
        try {
            carrierTypeValidator.validate(carrierType);
            logger.debug("Successfully validated CarrierType");
        } catch (ValidationException e) {
            logger.debug("Provided CarrierType could not be validated. Returning null.");
            throw new FindByException("Can't validate provided CarrierType.");
        }

        logger.debug("Validating provided Location...");
        try {
            locationValidator.validate(location);
            logger.debug("Successfully validated Location");
        } catch (ValidationException e) {
            logger.debug("Provided Location could not be validated. Returning null.");
            throw new FindByException("Can't validate provided Location.");
        }

        logger.debug("Trying to query Carrier(s) by Location {} and CarrierType {}.", location.getPosition(), carrierType.getName());
        return carrierRepository.findByCarrierTypeAndLocation(carrierType, location);
    }

    /**
     * Return a List of carriers with for the List of given carrier ids.
     *
     * @param ids of the carriers to be fetched
     * @return list of carriers with given ids
     */
    public List<Carrier> getByIds(Collection<String> ids) {
        logger.debug("Fetching carriers by ids.");
        if (ids == null) {
            logger.debug("Attempting to fetch carriers with ids = null. Aborting.");
            return new ArrayList<>();
        }
        if (ids.isEmpty()) {
            logger.debug("Attempting to fetch carriers with empty ids. Aborting.");
            return new ArrayList<>();
        }

        ArrayList<Carrier> result = new ArrayList<>();

        // Fetch carrier for every given id and skip if the id is invalid.
        for (String id : ids) {
            try {
                result.add(this.getById(id));
            } catch (InvalidIdException e) {
                logger.debug("Carrier id: {} was found invalid. Skipping...", id);
            }
        }

        return result;
    }

    /**
     * Get unused carriers list.
     *
     * @return the list
     */
    public List<Carrier> getUnusedCarriers(){
        return carrierRepository.findUnusedCarriers();
    }

    /**
     * Return all existing carriers.
     *
     * @return Collection of all existing carriers.
     */
    public List<Carrier> getAll() {
        logger.debug("Trying to query all stored Carriers");
        return carrierRepository.findAll();
    }

    /**
     * Returns whether a carrier is collectable
     *
     * @param carrier the carrier
     * @return whether a carrier is collectable
     */
    public boolean isCollectable(Carrier carrier) {
        return assemblyService.isCollectable(this.getAssemblies(carrier).get(0));
    }

    /**
     * Gets assemblies.
     *
     * @param carrier the carrier
     * @return the assemblies
     */
    public List<Assembly> getAssemblies(Carrier carrier) {
        List<Assembly> result = new ArrayList<>();
        for (Assembly assembly : assemblyService.getAll()) {
            if (assembly.getCarriers().contains(carrier)) {
                result.add(assembly);
            }
        }
        return result;
    }

    /**
     * Can collect boolean.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canCollect(Carrier carrier) {
        return !transportService.getAll().contains(carrier.getLocation());
    }

    /**
     * Can deliver boolean.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canDeliver(Carrier carrier) {
        return transportService.getAll().contains(carrier.getLocation());
    }

    /**
     * Update {@link Carrier} State to be collected by the {@link Transport}.
     *
     * @param carrier   to be collected
     * @param transport collection the Carrier
     */
    public void collect(final Carrier carrier, final Transport transport) throws CollectingException {
        if (carrier == null || transport == null) {
            throw new CollectingException("Parameters can't be null");
        }

        carrier.setLocation(transport);
        try {
            this.update(carrier);
        } catch (UpdateException e) {
            throw new CollectingException("Couldn't update carrier: " + e.getMessage());
        }
    }

    /**
     * Update {@link Carrier} State to be delivered at a {@link Location}.
     *
     * @param carrier  to be delivered
     * @param location the carrier is delivered at
     */
    public void deliver(final Carrier carrier, final Location location) throws DeliveringException {
        if (carrier == null || location == null) {
            throw new DeliveringException("Parameters can't be null");
        }

        carrier.setLocation(location);
        try {
            this.update(carrier);
        } catch (UpdateException e) {
            throw new DeliveringException("Couldn't update carrier: " + e.getMessage());
        }
    }

    /**
     * Returns if the carrier is currently not in use and can be deleted.
     *
     * @param carrier the carrier
     * @return the boolean
     */
    public boolean canDelete(Carrier carrier) {
        List<Assembly> assemblies = assemblyService.getByCarrier(carrier);

       return assemblies == null || assemblies.isEmpty();
    }
}
