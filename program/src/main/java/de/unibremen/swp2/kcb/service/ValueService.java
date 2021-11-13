package de.unibremen.swp2.kcb.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.parameter.ValueRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ParameterValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.ValueValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class to handle Values
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
public class ValueService implements Service<Value> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(ValueService.class);

    /**
     * Injected instance of ValueValidator
     */
    @Inject
    private ValueValidator valueValidator;

    /**
     * Injected instance of parameterValidator
     */
    @Inject
    private ParameterValidator parameterValidator;

    /**
     * Injected instance of valueRepository
     */
    @Inject
    private ValueRepository valueRepository;

    /**
     * Injected instance Userservice
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Value that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public Value create(final Value entity) throws CreationException {

        //Validating value
        boolean valid;
        try {
            logger.trace("Attempting to validate value \" {} \" ...", entity);
            valid = valueValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during value validation. Can't create value.");
            throw new CreationException("Can't create value: " + e.getMessage());
        }
        logger.trace("Validation of value \"{}\" completed without exceptions.", entity);

        //Checking, if value is valid
        if (!valid) {
            final String message = "Value \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Value \"{}\" is valid.", entity);

        //Saving value
        Value repoEntity;
        try {
            logger.trace("Attempting to save value \" {} \" ...", entity);
            repoEntity = valueRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting value \" {} \". Can't create value.", entity);
            throw new CreationException("Can't create value: " + e.getMessage());
        }

        logger.trace("Saving of value \"{}\" completed without exceptions.", entity);
        logger.info("Create value \"{}\" - triggered by: {}", entity.getValue(), userService.getExecutingUser().getUsername());
        logger.trace("Returning value \"{}\"", entity);
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
    public Value update(final Value entity) throws UpdateException {

        //Validating value
        boolean valid;
        try {
            logger.trace("Attempting to validate value \" {} \" ...", entity);
            valid = valueValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating value \" {} \". Can't update value", entity);
            throw new UpdateException("Can't update value: " + e.getMessage());
        }
        logger.trace("Validation of value \" {} \" completed without exceptions.", entity);

        //Checking, if value is valid
        if (!valid) {
            final String message = "Value \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Value \"{}\" is valid.", entity);

        //Checking, if value is stored in database
        final Value storedValue = valueRepository.findBy(entity.getId());
        if (storedValue == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving value
        Value repoEntity;
        try {
            logger.trace("Attempting to save new value \" {} \" ...", entity);
            repoEntity = valueRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting value \" {} \". Can't update value.", entity);
            throw new UpdateException("Can't update value: " + e.getMessage());
        }

        logger.trace("Saving of value \" {} \" completed without exceptions.", entity);
        logger.info("Update value \" {} \" - triggered by: {}",entity.getValue(), userService.getExecutingUser().getUsername());
        logger.trace("Returning value \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    public void delete(final Value entity) throws DeletionException {

        //Checking, if value is stored in database
        final Value storedValue = valueRepository.findBy(entity.getId());
        if (storedValue == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting value
        try {
            logger.trace("Attempting to delete value \" {} \" ...", entity);
            valueRepository.attachAndRemove(entity);
            logger.info("Delete value \"{}\" triggered by: {}", entity.getValue(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting value \"{}\". Can't delete value.", entity);
            throw new DeletionException("Can't delete value: " + e.getMessage());
        }
    }

    /**
     * Return the given values in a json format, used by the sfb
     *
     * @param values to be formatted to json
     * @return JsonObject in sfb format containing all values provided as parameter
     */
    public JsonArray getAsSFBJSON(final List<Value> values) {
        final JsonArray sfbParameters = new JsonArray();

        for (final Value value : values) {
            JsonObject element = new JsonObject();

            final String field = value.getParameter() != null ? value.getParameter().getField() : null;
            final String valueString = value.getValue();

            element.add("field", field != null ? new JsonPrimitive(field) : null);
            element.add("value", valueString != null ? new JsonPrimitive(valueString) : null);

            // Only cardinal values have a unit attribute
            if (value instanceof CardinalValue && ((CardinalValue) value).getUnit() != null)
                element.add("unit", new JsonPrimitive(((CardinalValue) value).getUnit()));

            sfbParameters.add(element);
        }
        return sfbParameters;
    }

    /**
     * Return the {@link Value} with the given id.
     *
     * @param id of the Value
     * @return Value with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Value getById(String id) throws InvalidIdException {

        //Validating valueID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "ValueID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("ValueID \"{}\" is valid.", id);

        //Finding value by ID
        final Value entity;
        try {
            logger.trace("Attempting to find value by ID \" {} \" ...", id);
            entity = valueRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding value by ID \"{}\". Can't find value.", id);
            throw new InvalidIdException("Can't find value: " + e.getMessage());
        }

        logger.trace("Finding of value by ID \"{}\" completed without exceptions.", id);
        logger.info("Find value by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning value by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return all existing Assemblies.
     *
     * @return List of all existing Assemblies.
     */
    public List<Value> getAll() {
        logger.trace("Returning all values");
        return valueRepository.findAll();
    }

    /**
     * Return all Values that are executed with a given {@link Parameter}.
     * If the Parameter has no associated Values, an empty Collection will be returned.
     *
     * @param parameter of the returned Process.
     * @return the by parameter.
     */
    public List<Value> getByParameter(final Parameter parameter) throws FindByException {
        logger.debug("Validating parameter {}.", parameter.getField());
        try {
            parameterValidator.validate(parameter);
        } catch (ValidationException e) {
            throw new FindByException("Parameter " + parameter.getField() + " invalid: " + e.getMessage());
        }
        logger.debug("Querying Value by parameter {}.", parameter.getField());
        return valueRepository.findByParameter(parameter);
    }
}
