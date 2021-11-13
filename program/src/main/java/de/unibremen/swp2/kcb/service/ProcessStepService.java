package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.persistence.ProcessStepRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle ProcessSteps.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
public class ProcessStepService implements Service<ProcessStep> {

    /**
     * Logger object of the ProcessStepService class
     */
    private static final Logger logger = LogManager.getLogger(ProcessStepService.class);

    /**
     * Injected instance of {@link ProcessStepValidator} to validate provided {@link ProcessStep}s.
     */
    @Inject
    private ProcessStepValidator processStepValidator;

    /**
     * Injected instance of {@link CarrierTypeValidator} to validate provided {@link CarrierType}s.
     */
    @Inject
    private CarrierTypeValidator carrierTypeValidator;

    /**
     * Injected instance of {@link ProcessStepRepository} to query database for {@link ProcessStep}s.
     */
    @Inject
    private ProcessStepRepository processStepRepository;

    /**
     * Injected instance of {@link ParameterValidator} to validate provided {@link Parameter}s.
     */
    @Inject
    private ParameterValidator parameterValidator;

    /**
     * Injected instance of {@link WorkstationValidator} to validate provided {@link Workstation}s.
     */
    @Inject
    private WorkstationValidator workstationValidator;

    /**
     * Injected instance of {@link StateMachineValidator} to validate provided {@link StateMachine}s.
     */
    @Inject
    private StateMachineValidator stateMachineValidator;

    /**
     * Injected instance of {@link UserService}
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of {@link ProcessChainService}
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return ProcessStep that has been created and stored
     * @see Validator
     */
    @RequiresAuthentication
    @Transactional
    @Override
    public ProcessStep create(final ProcessStep entity) throws CreationException, EntityAlreadyExistingException {

        //Validating processStep
        boolean valid;
        try {
            logger.trace("Attempting to validate processStep \"{}\" ...", entity);
            valid = processStepValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during processStep validation. Can't create processStep.");
            throw new CreationException("Can't create processStep: " + e.getMessage());
        }
        logger.trace("Validation of processStep \"{}\" completed without exceptions.", entity);

        //Checking, if processStep is valid
        if (!valid) {
            final String message = "Workstation \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Workstation \"{}\" is valid.", entity);

        //Checking, if workstation is already stored in database
        final List<ProcessStep> storedProcessStep = processStepRepository.findByName(entity.getName());
        if (!storedProcessStep.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create workstation: Entity is already stored in datasource");
        }

        //Saving processStep
        ProcessStep repoEntity;
        try {
            logger.trace("Attempting to save processStep \"{}\" ...", entity);
            repoEntity = processStepRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting processStep \"{}\". Can't create processStep.", entity);
            throw new CreationException("Can't create processStep: " + e.getMessage());
        }

        logger.trace("Saving of processStep \"{}\" completed without exceptions.", entity);
        logger.info("Create processStep \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning processStep \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Updates the stored entity with the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be updated
     * @return entity with updated properties
     * @see Validator
     */
    @RequiresAuthentication
    @Transactional
    @Override
    public ProcessStep update(final ProcessStep entity) throws UpdateException {

        //Validating processStep
        boolean valid;
        try {
            logger.trace("Attempting to validate processStep \"{}\" ...", entity);
            valid = processStepValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating processStep \"{}\". Can't update processStep", entity);
            throw new UpdateException("Can't update processStep: " + e.getMessage());
        }
        logger.trace("Validation of processStep \"{}\" completed without exceptions.", entity);

        //Checking, if processStep is valid
        if (!valid) {
            final String message = "ProcessStep \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("ProcessStep \"{}\" is valid.", entity);

        //Checking, if processStep is stored in database
        final ProcessStep storedProcessStep = processStepRepository.findBy(entity.getId());
        if (storedProcessStep == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving processStep
        ProcessStep repoEntity;
        try {
            logger.trace("Attempting to save new processStep \"{}\" ...", entity);
            repoEntity = processStepRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting processStep \"{}\". Can't update processStep.", entity);
            throw new UpdateException("Can't update processStep: " + e.getMessage());
        }

        logger.trace("Saving of processStep \"{}\" completed without exceptions.", entity);
        logger.info("Update processStep \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning processStep \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted.
     */
    @RequiresAuthentication
    @Transactional
    @Override
    public void delete(final ProcessStep entity) throws DeletionException {

        //Checking, if processStep is stored in database
        final ProcessStep storedProcessStep = processStepRepository.findBy(entity.getId());
        if (storedProcessStep == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting processStep
        try {
            logger.trace("Attempting to delete processStep \"{}\" ...", entity);
            processStepRepository.attachAndRemove(entity);
            logger.info("Delete processStep \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting processStep \"{}\". Can't delete processStep.", entity);
            throw new DeletionException("Can't delete processStep: " + e.getMessage());
        }
    }

    /**
     * Return processStep with given Id.
     *
     * @param id of the processStep.
     * @return processStep with given id.
     * @throws InvalidIdException if the given ID was invalid
     */
    public ProcessStep getById(String id) throws InvalidIdException {

        //Validating processStepID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "ProcessStepID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("ProcessStepID \"{}\" is valid.", id);

        //Finding processStep by ID
        final ProcessStep entity;
        try {
            logger.trace("Attempting to find processStep by ID \"{}\" ...", id);
            entity = processStepRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding processStep by ID \"{}\". Can't find processStep.", id);
            throw new InvalidIdException("Can't find processStep: " + e.getMessage());
        }

        logger.trace("Finding of processStep by ID \"{}\" completed without exceptions.", id);
        logger.info("Find processStep by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning processStep by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return the ProcessStep with the given name.
     * If there is no {@link ProcessStep} with the given name, null will be returned.
     *
     * @param name of the {@link ProcessStep} to be returned.
     * @return the by name.
     */
    public List<ProcessStep> getByName(final String name) throws FindByException {
        logger.debug("Validating provided name for ProcessStep.");
        if (name == null || name.equals("")) {
            throw new FindByException("Name is invalid");
        }
        logger.debug("Querying ProcessStep by name {}.", name);
        return processStepRepository.findByName(name);
    }

    /**
     * Return all {@link ProcessStep}s within a range of the given estimated Duration.
     *
     * @param estDuration The estimated Duration.
     * @return List of {@link ProcessStep}s with given estimated Duration.
     */
    public List<ProcessStep> getByEstDuration(int estDuration)
            throws FindByException {

        int range = 5; // set this value to change the range of durations, in which processSteps will be returned.

        logger.debug("Validating provided estimatedDuration for ProcessStep within the default range.");
        if (estDuration < 0) {
            throw new FindByException("EstimatedDuration can't be negative");
        }
        logger.debug("Querying ProcessSteps by estDuration {} within the range of {}.", estDuration, range);

        List<ProcessStep> result = new ArrayList<>();
        result.addAll(processStepRepository.findByEstDuration(estDuration)); // outside loop so results are not added twice
        for (int i = 1; i <= range; i++) { //starting at 1 so 0 won't be added twice.
            result.addAll(processStepRepository.findByEstDuration(estDuration + i));
            if (estDuration - i > 0) {  // estDuration of 0 or less will be ignored
                result.addAll(processStepRepository.findByEstDuration(estDuration - i));
            }
        }

        return result;
    }

    /**
     * Return all {@link ProcessStep}s at a given {@link StateMachine}.
     *
     * @param stateMachine The {@link StateMachine}.
     * @return Collection of {@link ProcessStep}s at a given {@link StateMachine}.
     */
    public List<ProcessStep> getByStateMachine(StateMachine stateMachine)
            throws FindByException {
        logger.debug("Validating provided stateMachine for ProcessStep.");
        try {
            stateMachineValidator.validate(stateMachine);
        } catch (ValidationException e) {
            throw new FindByException("StateMachine is invalid");
        }
        logger.debug("Querying ProcessStep by stateMachine {}.", stateMachine.getName());
        return processStepRepository.findByStateMachine(stateMachine);
    }

    /**
     * Return all {@link ProcessStep}s that are planned to be executed at a given {@link Workstation}.
     * If the Workstation has no associated ProcessSteps, an empty Collection will be returned.
     *
     * @param workstation the returned ProcessSteps are executed at.
     * @return the by processStep.
     */
    public List<ProcessStep> getByWorkstation(final Workstation workstation)
            throws FindByException {
        logger.debug("Validating provided processStep for ProcessStep.");
        try {
            workstationValidator.validate(workstation);
        } catch (ValidationException e) {
            throw new FindByException("Workstation is invalid");
        }
        logger.debug("Querying ProcessStep by processStep {}.", workstation.getName());
        return processStepRepository.findByWorkstation(workstation);
    }

    /**
     * Return all {@link ProcessStep}s that are executed with a given {@link Parameter}.
     * If the Parameter has no associated ProcessSteps, an empty Collection will be returned.
     *
     * @param parameter of the returned Process.
     * @return the by parameter.
     */
    public List<ProcessStep> getByParameter(final Parameter parameter) throws FindByException {
        logger.debug("Validating parameter {}.", parameter.getField());
        try {
            parameterValidator.validate(parameter);
        } catch (ValidationException e) {
            throw new FindByException("Parameter " + parameter.getField() + " invalid: " + e.getMessage());
        }
        logger.debug("Querying ProcessStep by parameter {}.", parameter.getField());
        return processStepRepository.findByParameter(parameter);
    }

    /**
     * Return all existing {@link ProcessStep}s.
     *
     * @return Collection of all existing ProcessSteps.
     */
    public List<ProcessStep> getAll() {
        logger.debug("Querying all stored ProcessSteps");
        return processStepRepository.findAll();
    }

    /**
     * Gets active {@link ProcessStep}s.
     *
     * @return the active
     */
    public List<ProcessStep> getActive() {
        logger.debug("Querying all active ProcessSteps");
        List<ProcessChain> allActiveProcChains = processChainService.getActive();
        List<ProcessStep> allActiveSteps = new ArrayList<>();

        for(ProcessChain pc : allActiveProcChains) {
            allActiveSteps.addAll(pc.getChain());
        }

        return allActiveSteps;
    }

    /**
     * Return all {@link ProcessStep}s with a given output-Carriertype.
     *
     * @param output The Output.
     * @return Collection of {@link ProcessStep}s with given output.
     */
    public List<ProcessStep> getByOutputCarrierType(CarrierType output)
            throws FindByException {
        logger.debug("Validating output-carrierType {}.", output.getName());
        try {
            carrierTypeValidator.validate(output);
        } catch (ValidationException e) {
            throw new FindByException("CarrierType " + output.getName() + " invalid: " + e.getMessage());
        }
        logger.debug("Querying ProcessStep by Output-CarrierType {}.", output.getName());
        return processStepRepository.findByOutputCarrierType(output);
    }

    /**
     * Return all {@link ProcessStep}s with a given preparation-CarrierType.
     *
     * @param preparation The preparation.
     * @return Collection of {@link ProcessStep}s with given preparation.
     */
    public List<ProcessStep> getByPreparationCarrierType(CarrierType preparation)
            throws FindByException {
        logger.debug("Validating preparation-carrierType {}.", preparation.getName());
        try {
            carrierTypeValidator.validate(preparation);
        } catch (ValidationException e) {
            throw new FindByException("CarrierType " + preparation.getName() + " invalid: " + e.getMessage());
        }
        logger.debug("Querying ProcessStep by preparation-CarrierType {}.",preparation.getName());
        return processStepRepository.findByPreparationCarrierType(preparation);
    }

    /**
     * Returns if a given processStep can be deleted.
     *
     * @param processStep the process step
     * @return the boolean
     */
    public boolean canDelete(ProcessStep processStep) {
        List<ProcessChain> chains = processChainService.getActive();

        for(ProcessChain pc : chains) {
            if(pc.getChain().contains(processStep)) {
                return false;
            }
        }

        return true;
    }
}
