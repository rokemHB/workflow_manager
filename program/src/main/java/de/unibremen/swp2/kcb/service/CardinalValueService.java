package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.persistence.parameter.CardinalValueRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.Validator;
import de.unibremen.swp2.kcb.validator.backend.ValueValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Service class to handle CardinalValues
 *
 * @author Robin
 * @author Arvid
 */
public class CardinalValueService implements Service<CardinalValue> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(CardinalValueService.class);

    /**
     * Injected instance of ValueValidator
     */
    @Inject
    private ValueValidator valueValidator;

    /**
     * Injected instance of CardinalValueRepository
     */
    @Inject
    private CardinalValueRepository cardinalValueRepository;

    /**
     * Injected instance of userService
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return CardinalValue that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see Validator
     */
    @Override
    public CardinalValue create(final CardinalValue entity) throws CreationException {

        //Validating cardinalValue
        boolean valid;
        try {
            logger.trace("Attempting to validate cardinalValue \"{}\" ...", entity);
            valid = valueValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during cardinalValue validation. Can't create cardinalValue.");
            throw new CreationException("Can't create cardinalValue: " + e.getMessage());
        }
        logger.trace("Validation of cardinalValue \"{}\" completed without exceptions.", entity);

        //Checking, if cardinalValue is valid
        if (!valid) {
            final String message = "CardinalValue \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("CardinalValue \"{}\" is valid.", entity);

        //Saving cardinalValue
        CardinalValue repoEntity;
        try {
            logger.trace("Attempting to save cardinalValue \"{}\" ...", entity);
            repoEntity = cardinalValueRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting cardinalValue \"{}\". Can't create cardinalValue.", entity);
            throw new CreationException("Can't create cardinalValue: " + e.getMessage());
        }

        logger.trace("Saving of cardinalValue \"{}\" completed without exceptions.", entity);
        logger.info("Create cardinalValue \"{}\" - triggered by: {}", entity.getValue(), userService.getExecutingUser().getUsername());
        logger.trace("Returning cardinalValue \"{}\"", entity);
        return repoEntity;
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
    public CardinalValue update(final CardinalValue entity) throws UpdateException {

        //Validating cardinalValue
        boolean valid;
        try {
            logger.trace("Attempting to validate cardinalValue \"{}\" ...", entity);
            valid = valueValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating cardinalValue \"{}\". Can't update cardinalValue", entity);
            throw new UpdateException("Can't update cardinalValue: " + e.getMessage());
        }
        logger.trace("Validation of cardinalValue \"{}\" completed without exceptions.", entity);

        //Checking, if cardinalValue is valid
        if (!valid) {
            final String message = "CardinalValue \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("CardinalValue \"{}\" is valid.", entity);

        //Checking, if cardinalValue is stored in database
        final CardinalValue storedCardinalValue = cardinalValueRepository.findBy(entity.getId());
        if (storedCardinalValue == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving cardinalValue
        CardinalValue repoEntity;
        try {
            logger.trace("Attempting to save new cardinalValue \"{}\" ...", entity);
            repoEntity = cardinalValueRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting cardinalValue \"{}\". Can't update cardinalValue.", entity);
            throw new UpdateException("Can't update cardinalValue: " + e.getMessage());
        }

        logger.trace("Saving of cardinalValue \"{}\" completed without exceptions.", entity);
        logger.info("Update cardinalValue \"{}\" - triggered by: {}", entity.getValue(), userService.getExecutingUser().getUsername());
        logger.trace("Returning cardinalValue \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    public void delete(final CardinalValue entity) throws DeletionException {

        //Checking, if cardinalValue is stored in database
        final CardinalValue storedCardinalValue = cardinalValueRepository.findBy(entity.getId());
        if (storedCardinalValue == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting cardinalValue
        try {
            logger.trace("Attempting to delete cardinalValue \"{}\" ...", entity);
            cardinalValueRepository.attachAndRemove(entity);
            logger.info("Delete cardinalValue \"{}\" triggered by: {}", entity.getValue(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting cardinalValue \"{}\". Can't delete cardinalValue.", entity);
            throw new DeletionException("Can't delete cardinalValue: " + e.getMessage());
        }
    }

    /**
     * Return the CardinalValue with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific CardinalValue
     * @throws InvalidIdException if the given ID was invalid
     */
    public CardinalValue getById(String id) throws InvalidIdException {

        //Validating cardinalValueID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "CardinalValueID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("CaridnalValueID \"{}\" is valid.", id);

        //Finding cardinalValue by ID
        final CardinalValue entity;
        try {
            logger.trace("Attempting to find cardinalValue by ID \"{}\" ...", id);
            entity = cardinalValueRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding cardinalValue by ID \"{}\". Can't find cardinalValue.", id);
            throw new InvalidIdException("Can't find caridnalValue: " + e.getMessage());
        }

        logger.trace("Finding of cardinalValue by ID \"{}\" completed without exceptions.", id);
        logger.info("Find cardinalValue by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning cardinalValue by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return all existing CardinalValues.
     *
     * @return Collection of all existing CardinalValues.
     */
    public List<CardinalValue> getAll() {
        logger.debug("Trying to query all stored CardinalValues.");
        return cardinalValueRepository.findAll();
    }
}
