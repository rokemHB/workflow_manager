package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.parameter.ParameterRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ParameterValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service class to handle Parameter.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@ApplicationScoped
@Transactional
public class ParameterService implements Service<Parameter> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(ParameterService.class);

    /**
     * Injected instance of parameterValidator
     */
    @Inject
    private ParameterValidator parameterValidator;

    /**
     * Injected instance of parameterRepository
     */
    @Inject
    private ParameterRepository parameterRepository;

    /**
     * Injected instance of processStepService
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Injected instance of valueService
     */
    @Inject
    private ValueService valueService;

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
     * @return Parameter that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public Parameter create(final Parameter entity) throws CreationException, EntityAlreadyExistingException {

        //Validating parameter
        boolean valid;
        try {
            logger.trace("Attempting to validate parameter \"{}\" ...", entity);
            valid = parameterValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during parameter validation. Can't create parameter.");
            throw new CreationException("Can't create parameter: " + e.getMessage());
        }
        logger.trace("Validation of parameter \"{}\" completed without exceptions.", entity);

        //Checking, if parameter is valid
        if (!valid) {
            final String message = "Parameter \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Parameter \"{}\" is valid.", entity);

        //Checking, if parameter is already stored in database
        final List<Parameter> storedParameter = parameterRepository.findByField(entity.getField());
        if (!storedParameter.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create parameter: Entity is already stored in datasource");
        }

        //Saving parameter
        Parameter repoEntity;
        try {
            logger.trace("Attempting to save parameter \"{}\" ...", entity);
            repoEntity = parameterRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting parameter \"{}\". Can't create parameter.", entity);
            throw new CreationException("Can't create parameter: " + e.getMessage());
        }

        logger.trace("Saving of parameter \"{}\" completed without exceptions.", entity);
        logger.info("Create parameter \"{}\" - triggered by: {}", entity.getField(), userService.getExecutingUser().getUsername());
        logger.trace("Returning parameter \"{}\"", entity);
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
    public Parameter update(final Parameter entity) throws UpdateException {

        //Validating parameter
        boolean valid;
        try {
            logger.trace("Attempting to validate parameter \"{}\" ...", entity);
            valid = parameterValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating parameter \"{}\". Can't update parameter", entity);
            throw new UpdateException("Can't update parameter: " + e.getMessage());
        }
        logger.trace("Validation of parameter \"{}\" completed without exceptions.", entity);

        //Checking, if parameter is valid
        if (!valid) {
            final String message = "Parameter \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Parameter \"{}\" is valid.", entity);

        //Checking, if parameter is stored in database
        final Parameter storedParameter = parameterRepository.findBy(entity.getId());
        if (storedParameter == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving parameter
        Parameter repoEntity;
        try {
            logger.trace("Attempting to save new parameter \"{}\" ...", entity);
            repoEntity = parameterRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting parameter \"{}\". Can't update parameter.", entity);
            throw new UpdateException("Can't update parameter: " + e.getMessage());
        }

        logger.trace("Saving of parameter \"{}\" completed without exceptions.", entity);
        logger.info("Update parameter \"{}\" - triggered by: {}",entity.getField(), userService.getExecutingUser().getUsername());
        logger.trace("Returning parameter \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    public void delete(final Parameter entity) throws DeletionException {

        //Checking, if parameter is stored in database
        final Parameter storedParameter = parameterRepository.findBy(entity.getId());
        if (storedParameter == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting parameter
        try {
            logger.trace("Attempting to delete parameter \"{}\" ...", entity);
            parameterRepository.attachAndRemove(entity);
            logger.info("Delete parameter \"{}\" triggered by: {}", entity.getField(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting parameter \"{}\". Can't delete parameter.", entity);
            throw new DeletionException("Can't delete parameter: " + e.getMessage());
        }
    }


    /**
     * Return all Parameter with a given field.
     *
     * @param field The field
     * @return Collection of Parameter with the same field.
     * @throws FindByException if findBy fails
     */
    public Collection<Parameter> getByField(String field) throws FindByException {
        if (field == null) {
            throw new FindByException("Can't find null alloy.");
        }

        return parameterRepository.findByField(field);
    }

    /**
     * Return all Parameter with a given id.
     *
     * @param id The id
     * @return Collection of Parameter with the same field.
     */
    public Parameter getById(String id) throws InvalidIdException {
        if (id == null) {
            throw new InvalidIdException("Can't find Parameter.");
        }

        logger.debug("Querying Parameter by ID");
        return parameterRepository.findBy(id);
    }

    /**
     * Return all existing parameteres.
     *
     * @return Collection of all existing parameters.
     */
    public List<Parameter> getAll() {
        logger.debug("Trying to query all stored Parameters");
        return parameterRepository.findAll();
    }

    /**
     * Get all Parameter with one of the given ids each.
     *
     * @param ids to get steps for
     * @return List of all Parameter with the given ids
     */
    public List<Parameter> getByIds(List<String> ids) {
        logger.debug("Fetching parameters by ids.");
        if (ids == null) {
            logger.debug("Attempting to fetch parameters with ids = null. Aborting.");
            return new ArrayList<>();
        }
        if (ids.size() < 1) {
            logger.debug("Attempting to fetch parameters with empty ids. Aborting.");
            return new ArrayList<>();
        }

        ArrayList<Parameter> result = new ArrayList<>();

        // Fetch parameters for every given id and skip if the id is invalid.
        for (String id : ids) {
            try {
                result.add(this.getById(id));
            } catch (InvalidIdException e) {
                logger.debug("Parameter id: {}  was found invalid. Skipping...", id);
            }
        }

        return result;
    }

    /**
     * Returns if a given parameter can be deleted.
     *
     * @param parameter the parameter
     * @return the boolean
     */
    public boolean canDelete(Parameter parameter) {
        List<ProcessStep> processSteps = null;
        List<Value> values = null;
        try {
            processSteps = processStepService.getByParameter(parameter);
            values = valueService.getByParameter(parameter);
        } catch (FindByException e) {
            return false;
        }

        return processSteps == null || processSteps.isEmpty() && values == null || values.isEmpty();
    }
}