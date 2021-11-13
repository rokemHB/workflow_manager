package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.persistence.ProcessChainRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.ProcessChainValidator;
import de.unibremen.swp2.kcb.validator.backend.ProcessStepValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.ValidationNullPointerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle ProcessChains.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@ApplicationScoped
@Transactional
public class ProcessChainService implements Service<ProcessChain> {

    /**
     * Logger object of the ProcessChainService class
     */
    private static final Logger logger = LogManager.getLogger(ProcessChainService.class);

    /**
     * Injected instance of {@link ProcessChainValidator} to validate provided {@link ProcessChain}s.
     */
    @Inject
    private ProcessChainValidator processChainValidator;

    /**
     * Injected instance of {@link ProcessStepValidator} to validate provided {@link ProcessStep}s.
     */
    @Inject
    private ProcessStepValidator processStepValidator;

    /**
     * Injected instance of {@link ProcessChainRepository} to query database for {@link ProcessChain}s.
     */
    @Inject
    private ProcessChainRepository processChainRepository;

    /**
     * Injected instance of {@link JobService}.
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link UserService}
     */
    @Inject
    private UserService userService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return ProcessChain that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @RequiresAuthentication
    @Override
    public ProcessChain create(final ProcessChain entity) throws CreationException, EntityAlreadyExistingException {

        //Validating processChain
        boolean valid;
        try {
            logger.trace("Attempting to validate processChain \"{}\" ...", entity);
            valid = processChainValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during processChain validation. Can't create processChain.");
            throw new CreationException("Can't create processChain: " + e.getMessage());
        }
        logger.trace("Validation of processChain \"{}\" completed without exceptions.", entity);

        //Checking, if processChain is valid
        if (!valid) {
            final String message = "ProcessChain \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("ProcessChain \"{}\" is valid.", entity);

        //Checking, if ProcessChain is already stored in database
        try {
            processChainRepository.findByName(entity.getName());

            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create ProcessChain: Entity is already stored in datasource");
        }
        catch (NoResultException ex) {  // If Entity does not exist
            //Saving processChain
            ProcessChain repoEntity;
            try {
                logger.trace("Attempting to save processChain \"{}\" ...", entity);
                repoEntity = processChainRepository.save(entity);
            } catch (PersistenceException e) {
                logger.debug("Error occurred while persisting processChain \"{}\". Can't create processChain.", entity);
                throw new CreationException("Can't create processChain: " + e.getMessage());
            }

            logger.trace("Saving of processChain \"{}\" completed without exceptions.", entity);
            logger.info("Create processChain \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
            logger.trace("Returning processChain \"{}\"", entity);
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
    @RequiresAuthentication
    @Override
    public ProcessChain update(final ProcessChain entity) throws UpdateException {

        //Validating processChain
        boolean valid;
        try {
            logger.trace("Attempting to validate processChain \"{}\" ...", entity);
            valid = processChainValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating processChain \"" + entity + "\". Can't update processChain");
            throw new UpdateException("Can't update processChain: " + e.getMessage());
        }
        logger.trace("Validation of processChain \"{}\" completed without exceptions.", entity);

        //Checking, if processChain is valid
        if (!valid) {
            final String message = "ProcessChain \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("ProcessChain \"{}\" is valid.", entity);

        //Checking, if processChain is stored in database
        final ProcessChain storedProcessChain = processChainRepository.findBy(entity.getId());
        if (storedProcessChain == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving processChain
        ProcessChain repoEntity;
        try {
            logger.trace("Attempting to save new processChain \"{}\" ...", entity);
            repoEntity = processChainRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting processChain \"" + entity + "\". Can't update processChain.");
            throw new UpdateException("Can't update processChain: " + e.getMessage());
        }

        logger.trace("Saving of processChain \"{}\" completed without exceptions.", entity);
        logger.info("Update processChain \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning processChain \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @RequiresAuthentication
    @Override
    public void delete(final ProcessChain entity) throws DeletionException {

        //Checking, if processChain is stored in database
        final ProcessChain storedProcessChain = processChainRepository.findBy(entity.getId());
        if (storedProcessChain == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting processChain
        try {
            logger.trace("Attempting to delete processChain \"{}\" ...", entity);
            processChainRepository.attachAndRemove(entity);
            logger.info("Delete processChain \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting processChain \"{}\". Can't delete processChain.", entity);
            throw new DeletionException("Can't delete processChain: " + e.getMessage());
        }
    }

    /**
     * Return the {@link ProcessChain} with the given id.
     *
     * @param id of the ProcessChain
     * @return ProcessChain with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public ProcessChain getById(String id) throws InvalidIdException {

        //Validating processChainID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "ProcessChainID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("ProcessChainID \"{}\" is valid.", id);

        //Finding processChain by ID
        final ProcessChain entity;
        try {
            logger.trace("Attempting to find processChain by ID \"{}\" ...", id);
            entity = processChainRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding processChain by ID \"{}\". Can't find processChain.", id);
            throw new InvalidIdException("Can't find processChain: " + e.getMessage());
        }

        logger.trace("Finding of processChain by ID \"{}\" completed without exceptions.", id);
        logger.info("Find processChain by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning processChain by ID \"{}\"", id);
        return entity;
    }

    /**
     * Return ProcessChain with given name
     *
     * @param name of the ProcessChain
     * @return ProcessChain with given name if exists. Null if ProcessChain is not found.
     */
    public ProcessChain getByName(final String name) throws FindByException {
        logger.debug("Verifying provided name...");

        if (name == null || name.equals("")) {
            logger.debug("Provided name was null or empty. Returning null.");
            throw new FindByException("Provided name was null or empty.");
        }
        logger.debug("Trying to query ProcessChain {}.", name);

        ProcessChain result = processChainRepository.findByName(name);
        if (result == null) {
            logger.info("Something went wrong. ProcessChain can't be found.");
            throw new FindByException("Can't find ProcessChain " + name + ".");
        } else {
            return result;
        }
    }

    public List<ProcessChain> getByChain(List<ProcessStep> chain) throws FindByException {
        logger.trace("Verifying provided ProcessSteps (chain)...");

        if (chain == null) {
            logger.trace("Provided chain was null.");
            throw new FindByException("Provided chain was null.");
        }

        for (ProcessStep p : chain) {
            if (p == null) {
                logger.trace("Provided chain was found corrupted! - A ProcessStep was null.");
                throw new FindByException("Provided chain was found corrupted!");
            }

            try {   //check ID of every Step separately
                Service.super.checkId(p.getId());
            } catch (InvalidIdException e) {
                logger.trace("A ProcessStep in the provided chain has a corrupted ID!");
                throw new FindByException("A ProcessStep in the provided chain has a corrupted ID!");
            }
        }

        return processChainRepository.findByChain(chain);
    }

    /**
     * Return a List of all active ProcessChains. Active ProcessChains are ProcessChain that are currently executed
     * by a Job.
     *
     * @return List of all active ProcessChains.
     * @see de.unibremen.swp2.kcb.model.Job
     */
    public List<ProcessChain> getActive() {

        List<ProcessChain> result = new ArrayList<>();

        for (Job job : jobService.getAll()) {
            ProcessChain processChain = job.getProcessChain();
            if (!result.contains(processChain))
                result.add(processChain);
        }
        return result;
    }

    /**
     * Returns if the first processStep of the processChain creates an assembly
     *
     * @param processChain the processChain
     * @return if the processChain creates an assembly
     */
    public boolean isCreating(ProcessChain processChain) {
        return processChain.getChain().get(0).isCreates();
    }

    /**
     * Returns if the last processStep of the processChain deletes an assembly
     *
     * @param processChain the processChain
     * @return if the processChain deletes an assembly
     */
    public boolean isDeleting(ProcessChain processChain) {
        List<ProcessStep> processSteps = processChain.getChain();

        if (processSteps == null || processSteps.isEmpty())
            return false;

        return processSteps.get(processSteps.size() - 1).isDeletes();
    }

    /**
     * Returns if any processStep of the processChain modifies an assembly
     *
     * @param processChain the processChain
     * @return if the processChain modifies an assembly
     */
    public boolean isModifying(ProcessChain processChain) {
        List<ProcessStep> processSteps = processChain.getChain();

        if (processSteps == null || processSteps.isEmpty())
            return false;

        for (ProcessStep processStep : processSteps) {
            if (processStep.isModifies())
                return true;
        }

        return false;
    }

    /**
     * Return the estimated processing duration of the given ProcessChain.
     *
     * @param processChain to get the processing duration of
     * @return processing duration of the given ProcessChain.
     */
    public int getDuration(final ProcessChain processChain) throws FindByException {
        logger.debug("Validating provided ProcessChain.");

        int result = 0;

        try {
            processChainValidator.validate(processChain);
            logger.debug("Successfully validated provided ProcessChain.");
        } catch (ValidationException e) {
            logger.info("Could not validate provided ProcessChain. Returning -1.");
            throw new FindByException("Can't find provided ProcessChain.");
        }

        List<ProcessStep> stepsInChain = processChain.getChain();

        if (stepsInChain == null) {
            logger.info("Something went wrong. Can't find ProcessSteps in provided Chain.");
            throw new FindByException("Can't find ProcessSteps.");
        } else {
            for (ProcessStep p : stepsInChain) {
                result += p.getEstDuration();   // Estimated Duration jedes Steps aufaddieren
            }
        }
        return result;
    }

    /**
     * Return all ProcessChains the given ProcessStep is a part of.
     *
     * @param processStep to get ProcessChains of.
     * @return Collection of ProcessChains the ProcessStep is associated with.
     */
    public List<ProcessChain> getByProcessStep(final ProcessStep processStep) throws FindByException {
        logger.debug("Validating provided ProcessStep.");

        try {
            processStepValidator.validate(processStep);
            logger.debug("Successfully validated provided ProcessStep.");
        } catch (ValidationNullPointerException e) {
            logger.info("Could not validate provided ProcessStep. Returning null.");
            throw new FindByException("Can't find ProcessChain.");
        }

        List<ProcessChain> allChains = processChainRepository.findAll();
        List<ProcessChain> result = new ArrayList<>();

        for (ProcessChain p : allChains) {
            if (p.getChain().contains(processStep)) {    //Wenn der übergeben Step ein Teil der Chain ist -> Chain in result übernehmen
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Return all existing ProcessChains.
     *
     * @return Collection of all existing ProcessChains.
     */
    public List<ProcessChain> getAll() {
        logger.debug("Trying to query all stored ProcessChains.");
        return processChainRepository.findAll();
    }

    /**
     * Returns if the processChain can be deleted.
     *
     * @param processChain the process chain
     * @return the boolean
     */
    public boolean canDelete(ProcessChain processChain) {
        List<ProcessChain> allActiveChains = this.getActive();

        return !allActiveChains.contains(processChain);
    }
}
