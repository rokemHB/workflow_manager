package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.persistence.CarrierTypeRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.CarrierTypeValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle CarrierTypes
 *
 * @author Marc
 * @author Robin
 * @author Arvid
 */
@Transactional
public class CarrierTypeService implements Service<CarrierType> {

    /**
     * Logger object of the CarrierType class
     */
    private static final Logger logger = LogManager.getLogger(CarrierTypeService.class);

    /**
     * Injected instance of {@link CarrierTypeRepository} to query database for {@link CarrierType}s.
     */
    @Inject
    private CarrierTypeRepository carrierTypeRepository;

    /**
     * Injected instance of {@link CarrierTypeValidator} to validate {@link CarrierType}s.
     */
    @Inject
    private CarrierTypeValidator carrierTypeValidator;

    /**
     * Injected instance of {@link CarrierService}.
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Injected instance of {@link UserService} to validate provided {@link CarrierType}s.
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return CarrierType that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @RequiresAuthentication
    public CarrierType create(CarrierType entity) throws CreationException, EntityAlreadyExistingException {

        //Validating carrierType
        boolean valid;
        try {
            logger.trace("Attempting to validate carrierType \"{}\" ...", entity);
            valid = carrierTypeValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during carrierType validation. Can't create carrierType.");
            throw new CreationException("Can't create carrierType: " + e.getMessage());
        }
        logger.trace("Validation of carrierType \"{}\" completed without exceptions.", entity);

        //Checking, if carrierType is valid
        if (!valid) {
            final String message = "CarrierType \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("CarrierType \"{}\" is valid.", entity);

        //Checking, if carrierType is already stored in database
        try {
            carrierTypeRepository.findByName(entity.getName());

            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create CarrierType: Entity is already stored in datasource");
        }
        catch (NoResultException ex) {  // If Entity does not exist
            //Saving carrierType
            CarrierType repoEntity;
            try {
                logger.trace("Attempting to save carrierType \"{}\" ...", entity);
                repoEntity = carrierTypeRepository.save(entity);
            } catch (PersistenceException e) {
                logger.debug("Error occurred while persisting carrierType \"{}\". Can't create carrierType.", entity);
                throw new CreationException("Can't create carrierType: " + e.getMessage());
            }

            logger.trace("Saving of carrierType \"{}\" completed without exceptions.", entity);
            logger.info("Create carrierType \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
            logger.trace("Returning carrierType \"{}\"", entity);
            return repoEntity;
        }
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @see de.unibremen.swp2.kcb.validator.backend
     */
    @Override
    @RequiresAuthentication
    public CarrierType update(CarrierType entity) throws UpdateException {

        //Validating carrierType
        boolean valid;
        try {
            logger.trace("Attempting to validate carrierType \"{}\" ...", entity);
            valid = carrierTypeValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating carrierType \"{}\". Can't update carrierType", entity);
            throw new UpdateException("Can't update carrierType: " + e.getMessage());
        }
        logger.trace("Validation of carrierType \"{}\" completed without exceptions.", entity);

        //Checking, if carrierType is valid
        if (!valid) {
            final String message = "CarrierType \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("CarrierType \"{}\" is valid.", entity);

        //Checking, if carrierType is stored in database
        final CarrierType storedCarrierType = carrierTypeRepository.findBy(entity.getId());
        if (storedCarrierType == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving carrierType
        CarrierType repoEntity;
        try {
            logger.trace("Attempting to save new carrierType \"{}\" ...", entity);
            repoEntity = carrierTypeRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting carrierType \"{}\". Can't update carrierType.", entity);
            throw new UpdateException("Can't update carrierType: " + e.getMessage());
        }

        logger.trace("Saving of carrierType \"{}\" completed without exceptions.", entity);
        logger.info("Update carrierType \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning carrierType \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    @RequiresAuthentication
    public void delete(CarrierType entity) throws DeletionException {

        //Checking, if carrierType is stored in database
        final CarrierType storedCarrierType = carrierTypeRepository.findBy(entity.getId());
        if (storedCarrierType == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting carrierType
        try {
            logger.trace("Attempting to delete carrierType \"{}\" ...", entity);
            carrierTypeRepository.attachAndRemove(entity);
            logger.info("Delete carrierType \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting carrierType \"{}\". Can't delete carrierType.", entity);
            throw new DeletionException("Can't delete carrierType: " + e.getMessage());
        }
    }

    /**
     * Return all existing {@link CarrierType}s
     *
     * @return Collection of all existing CarrierTypes.
     */
    public List<CarrierType> getAll() throws FindByException {
        logger.info("Querying database for all existing CarrierTypes.");

        List<CarrierType> allCarrierTypes;
        allCarrierTypes = carrierTypeRepository.findAll();

        if (allCarrierTypes.isEmpty()) {
            logger.debug("No CarrierTypes found.");
            throw new FindByException("No CarrierTypes found.");
        }

        logger.info("Successful query: Found {} total CarrierTypes.", allCarrierTypes.size());
        return allCarrierTypes;
    }

    /**
     * Return the {@link CarrierType} with the given name.
     * If no CarrierType is found with the given name, null will be returned.
     *
     * @param name of the CarrierType to be returned
     * @return the by name
     */
    public CarrierType getByName(String name) throws FindByException {
        if (name == null || name.equals("")) {
            logger.error("Can't query CarrierType: CarrierType name empty/null");
            throw new FindByException("Can't query CarrierType: CarrierType name empty/null");
        }

        logger.info("Trying to query CarrierType named {} from database.", name);

        CarrierType result = carrierTypeRepository.findByName(name);

        if (result == null) {
            logger.error("Query error: CarrierType could not be queried from database.");
            throw new FindByException("Query error: CarrierType could not be queried from database.");
        }

        logger.info("Successfully queried CarrierType {} from database.", name);
        return result;
    }

    /**
     * Return the CarrierType with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific CarrierType
     * @throws InvalidIdException if the given ID was invalid
     */
    public CarrierType getById(String id) throws InvalidIdException {

        //Validating carrierTypeID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "CarrierTypeID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("CarrierTypeID \"{}\" is valid.", id);

        //Finding carrierType by ID
        final CarrierType entity;
        try {
            logger.trace("Attempting to find carrierType by ID \"{}\" ...", id);
            entity = carrierTypeRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding carrierType by ID \"{}\". Can't find carrierType.", id);
            throw new InvalidIdException("Can't find carrierType: " + e.getMessage());
        }

        logger.trace("Finding of carrierType by ID \"{}\" completed without exceptions.", id);
        logger.info("Find carrierType by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning carrierType by ID \"{}\"", id);
        return entity;
    }

    /**
     * Returns if the CarrierType is currently not in use and can be deleted.
     *
     * @param carrierType the carrierType
     * @return the boolean
     */
    public boolean canDelete(CarrierType carrierType) {
        if (carrierType == null)
            return false;

        List<Carrier> carriers;
        try {
            carriers = carrierService.getByCarrierType(carrierType);
        } catch (FindByException e) {
            return false;
        }

        return carriers == null || carriers.isEmpty();
    }
}
