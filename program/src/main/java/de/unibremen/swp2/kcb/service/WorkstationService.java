package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.persistence.locations.WorkstationRepository;
import de.unibremen.swp2.kcb.security.WorkstationAccessProvider;
import de.unibremen.swp2.kcb.security.authz.KCBSecure;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.UserValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import de.unibremen.swp2.kcb.validator.backend.WorkstationValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class to handle Workstations.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Transactional
@KCBSecure
public class WorkstationService implements Service<Workstation> {

    /**
     * Logger object of the Workstation Service class
     */
    private static final Logger logger = LogManager.getLogger(WorkstationService.class);

    /**
     * Injected instance of {@link WorkstationRepository}
     */
    @Inject
    private WorkstationRepository workstationRepository;

    /**
     * Injected instance of {@link WorkstationValidator}
     */
    @Inject
    private WorkstationValidator workstationValidator;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Workstation}s.
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of {@link ProcessStepService}
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Injected instance of {@link JobService}
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link UserValidator}
     */
    @Inject
    private UserValidator userValidator;

    /**
     * Injected instance of {@link WorkstationAccessProvider}
     */
    @Inject
    private WorkstationAccessProvider workstationAccessProvider;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Workstation that has been created and stored
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @RequiresAuthentication
    public Workstation create(final Workstation entity) throws CreationException, EntityAlreadyExistingException {

        //Validating workstation
        boolean valid;
        try {
            logger.trace("Attempting to validate workstation \"{}\" ...", entity);
            valid = workstationValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during workstation validation. Can't create workstation.");
            throw new CreationException("Can't create workstation: " + e.getMessage());
        }
        logger.trace("Validation of workstation \"{}\" completed without exceptions.", entity);

        //Checking, if workstation is valid
        if (!valid) {
            final String message = "Workstation \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Workstation \"{}\" is valid.", entity);

        //Checking, if workstation is already stored in database
        final List<Workstation> storedWorkstation = workstationRepository.findByName(entity.getName());
        if (!storedWorkstation.isEmpty()) {
            final String message = "Entity \"{}\" is already stored in  datasource.";
            logger.debug(message, entity);
            throw new EntityAlreadyExistingException("Can't create workstation: Entity is already stored in datasource");
        }

        //Saving workstation
        Workstation repoEntity;
        try {
            logger.trace("Attempting to save workstation \"{}\" ...", entity);
            repoEntity = workstationRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting workstation \"{}\". Can't create workstation.", entity);
            throw new CreationException("Can't create workstation: " + e.getMessage());
        }

        logger.trace("Saving of workstation \"{}\" completed without exceptions.", entity);
        logger.info("Create workstation \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning workstation \"{}\"", entity);
        return repoEntity;
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
    public Workstation update(final Workstation entity) throws UpdateException {

        //Validating workstation
        boolean valid;
        try {
            logger.trace("Attempting to validate workstation \"{}\" ...", entity);
            valid = workstationValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating workstation \"{}\". Can't update workstation", entity);
            throw new UpdateException("Can't update workstation: " + e.getMessage());
        }
        logger.trace("Validation of workstation \"{}\" completed without exceptions.", entity);

        //Checking, if workstation is valid
        if (!valid) {
            final String message = "Workstation \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Workstation \"{}\" is valid.", entity);

        //Checking, if workstation is stored in database
        final Workstation storedWorkstation = workstationRepository.findBy(entity.getId());
        if (storedWorkstation == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving workstation
        Workstation repoEntity;
        try {
            logger.trace("Attempting to save new workstation \"{}\" ...", entity);
            repoEntity = workstationRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting workstation \"{}\". Can't update workstation.", entity);
            throw new UpdateException("Can't update workstation: " + e.getMessage());
        }

        logger.trace("Saving of workstation \"{}\" completed without exceptions.", entity);
        logger.info("Update workstation \"{}\" - triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        logger.trace("Returning workstation \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     */
    @Override
    @RequiresAuthentication
    public void delete(final Workstation entity) throws DeletionException {

        //Checking, if workstation is stored in database
        final Workstation storedWorkstation = workstationRepository.findBy(entity.getId());
        if (storedWorkstation == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting workstation
        try {
            logger.trace("Attempting to delete workstation \"{}\" ...", entity);
            workstationRepository.attachAndRemove(entity);
            logger.info("Delete workstation \"{}\" triggered by: {}", entity.getName(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting workstation \"{}\". Can't delete workstation.", entity);
            throw new DeletionException("Can't delete workstation: " + e.getMessage());
        }
    }

    /**
     * Return the Workstation with the provided unique ID
     *
     * @param id to be searched for
     * @return one specific Workstation
     * @throws InvalidIdException if the given ID was invalid
     */
    @RequiresAuthentication
    public Workstation getById(String id) throws InvalidIdException {

        //Validating workstationID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "WorkstationID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("WorkstationID \"{}}\" is valid.", id);

        //Finding workstation by ID
        final Workstation entity;
        try {
            logger.trace("Attempting to find workstation by ID \"{}\" ...", id);
            entity = workstationRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding workstation by ID \"{}\". Can't find workstation.", id);
            throw new InvalidIdException("Can't find workstation: " + e.getMessage());
        }

        // Check if user got access to given workstation
        if (!workstationAccessProvider.checkAccess(entity, userService.getExecutingUser()))
            throw new UnauthorizedException("Not authorized for current workstation.");

        logger.trace("Finding of workstation by ID \"{}\" completed without exceptions.", id);
        logger.info("Find workstation by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning workstation by ID \"{}\"", id);
        return entity;
    }


    /**
     * Return the {@link Workstation} with the given name.
     * If no Workstation is found with the given name, null will be returned.
     *
     * @param name of the Workstation to be returned
     * @return the by name
     */
    @RequiresAuthentication
    public Workstation getByName(final String name) throws FindByException {
        if (name == null || name.equals("")) {
            logger.error("Can't query Workstation: Workstation name empty/null");
            throw new FindByException("Can't query Workstation: Workstation name empty/null");
        }

        logger.info("Trying to query Workstation named {} from database.", name);

        Workstation result = (Workstation) workstationRepository.findByName(name);

        if (result == null) {
            logger.error("Query error: Workstation could not be queried from database.");
            throw new FindByException("Query error: Workstation could not be queried from database.");
        }

        logger.info("Successfully queried Workstation {} from database.", name);
        return result;
    }

    /**
     * Return all {@link Workstation}s that are currently active.
     * Active means that they are not broken.
     *
     * @return Collection Workstation that are currently active.
     */
    @RequiresAuthentication
    public List<Workstation> getActive() throws FindByException {
        logger.info("Querying database for all active Workstations.");

        List<Workstation> activeWorkstations;
        activeWorkstations = workstationRepository.findByActive(true);

        if (activeWorkstations.isEmpty()) {
            logger.debug("No active Workstations found.");
            throw new FindByException("No active Workstations found.");
        }

        logger.info("Successful query: Found {} active Workstations.", activeWorkstations.size());
        return activeWorkstations;
    }

    /**
     * Return all {@link Workstation}s that are currently inactive/broken.
     *
     * @return Collection Workstation that are currently inactive.
     */
    @RequiresAuthentication
    public List<Workstation> getInactive() throws FindByException {
        logger.info("Querying database for all inactive Workstations.");

        List<Workstation> inactiveWorkstations;
        inactiveWorkstations = workstationRepository.findByActive(false);

        if (inactiveWorkstations.isEmpty()) {
            logger.debug("No inactive Workstations found.");
            throw new FindByException("No inactive Workstations found.");
        }

        logger.info("Successful query: Found {} inactive Workstations.",  inactiveWorkstations.size());
        return inactiveWorkstations;
    }

    /**
     * Return all existing {@link Workstation}s
     *
     * @return Collection of all existing Workstations.
     */
    @RequiresAuthentication
    public List<Workstation> getAll() {
        logger.info("Querying database for all existing Workstations.");

        List<Workstation> allWorkstations;
        allWorkstations = workstationRepository.findAll();

        if (allWorkstations.isEmpty()) {
            logger.debug("No Workstations found.");
        }

        logger.info("Successful query: Found {} total Workstations.", allWorkstations.size());
        return allWorkstations;
    }

    /**
     * Return duration of all Procedures that are currently located at the {@link Workstation}.
     * The duration will be returned as an int, describing the amount of minutes the {@link Workstation}
     * will be working on processing it's {@link de.unibremen.swp2.kcb.model.Procedure}s.
     *
     * @param workstation to calculate
     * @return minutes left until Workstation is done with its current Job
     */
    @RequiresAuthentication
    public int getWorkload(final Workstation workstation)
            throws ValidationException, FindByException {
        if (workstationValidator.validate(workstation)) {
            logger.error("Validating Workstation failed.");
            throw new FindByException("Validating Workstation failed.");
        }
        logger.info("Trying to query all ProcessSteps for Workstation {}.", workstation.getName());

        List<ProcessStep> stepsByWorkstation = processStepService.getByWorkstation(workstation);

        if (stepsByWorkstation == null || stepsByWorkstation.isEmpty()) {
            logger.error("Couldn't query ProcessSteps by Workstation or none found.");
            throw new FindByException("Couldn't query ProcessSteps by Workstation or none found.");
        }
        logger.info("Successfully queried ProcessSteps for Workstation {}", workstation.getName());

        int totalDuration = 0;
        for (ProcessStep p : stepsByWorkstation) {
            totalDuration += p.getEstDuration();
        }

        if (totalDuration <= 0) {
            logger.error("Could not calculate total duration/Workload. Returning 0.");
            throw new FindByException("Could not calculate total duration/Workload. Returning 0.");
        }

        return totalDuration;
    }

    /**
     * Return all workstations the given user is allowed to work on.
     *
     * @param user to get workstations for
     * @return Collection of Workstations the {@link User} is allowed to work on.
     */
    @RequiresAuthentication
    public List<Workstation> getByUser(final User user) throws FindByException {
        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            logger.error("Validating given User failed.");
            throw new FindByException("Validating given User failed.");
        }

        logger.info("Querying database for all Workstations.");

        List<Workstation> workstationsOperableByUser = new ArrayList<>();
        List<Workstation> allWorkstations;
        allWorkstations = workstationRepository.findAll();
        logger.info("Searching for Workstations operable by {}.", user.getUsername());

        for (Workstation w : allWorkstations) {
            for (User u : w.getUsers()) {
                if (u.equals(user)) {
                    workstationsOperableByUser.add(w);
                }
            }
        }

        logger.info("Successfully sorted out Workstations operable by asked user.");
        return workstationsOperableByUser;
    }

    /**
     * Toggles the state of a {@link Workstation}.
     * A workstation can either be active or inactive. This Method switches the state to the opposite one.
     *
     * @param workstation to toggle the state of
     */
    @RequiresAuthentication
    public void toggleActive(final Workstation workstation) throws FindByException {
        try {
            workstationValidator.validate(workstation);
        } catch (ValidationException e) {
            logger.error("Error validation for this Workstation");
            throw new FindByException("Error validation given this Workstation");
        }

        logger.info("Trying to change active status of Workstation {}.", workstation.getName());
        workstation.setActive(!workstation.getActive());
        logger.info("Successfully changed active status of Workstation {}.", workstation.getName());
    }

    /**
     * Return Collection of {@link Workstation} at a given position.
     *
     * @param position The position.
     * @return Collection of {@link Workstation} at a given position.
     */
    @RequiresAuthentication
    public List<Workstation> getByPosition(String position) throws FindByException {
        if (position == null || position.equals("")) {
            logger.error("Can't query Workstation: given Workstation position empty/null");
            throw new FindByException("Can't query Workstation: given Workstation position empty/null");
        }

        logger.info("Trying to query all Workstation located at {} from database.", position);
        List<Workstation> workstationsOnGivenPosition = workstationRepository.findByPosition(position);

        if (workstationsOnGivenPosition == null || workstationsOnGivenPosition.isEmpty()) {
            logger.debug("Could not find any Workstation located at {}", position);
            throw new FindByException("Could not find any Workstation located at ");
        }

        logger.info("Successfully queried all Workstations located at {}", position);
        return workstationsOnGivenPosition;
    }

    /**
     * Return a Collection of all {@link de.unibremen.swp2.kcb.model.Role#TECHNOLOGE} working on provided Workstation
     *
     * @param workstation The Workstation whose Technologen shall be found
     * @return A Collection of all Technologen
     */
    @RequiresAuthentication
    public List<User> getAllTechnologen(Workstation workstation) throws FindByException {
        try {
            workstationValidator.validate(workstation);
        } catch (ValidationException e) {
            logger.error("Error validation given Workstation");
            throw new FindByException("Error validation for given Workstation");
        }
        logger.info("Filtering all Technologen working on given Workstation");

        List<User> technologenOnWorkstation = new ArrayList<>();

        for (User u : workstation.getUsers()) {
            if (u.getRoles().contains(Role.TECHNOLOGE)) {
                technologenOnWorkstation.add(u);
            }
        }
        logger.info("Done filtering Users. Found {} Technologen on {}.", technologenOnWorkstation.size(), workstation.getName());
        return technologenOnWorkstation;
    }

    /**
     * Gets active job count for a given workstation.
     *
     * @param workstation the workstation
     * @return the active job count
     */
    public int getActiveJobCount(Workstation workstation) {
        if (workstation == null)
            return 0;

        int count = 0;
        for (Job job : jobService.getActive()) {
            Procedure currentProcedure = jobService.getCurrentProcedure(job);

            if (currentProcedure == null)
                continue;

            if (currentProcedure.getProcessStep().getWorkstation().equals(workstation))
                count++;
        }

        return count;
    }

    /**
     * Gets active job workload for a given workstation..
     *
     * @param workstation the workstation
     * @return the duration of the current workstation
     */
    public int getActiveJobWorkload(Workstation workstation) {
        if (workstation == null)
            return 0;

        List<Job> currentJobs = jobService.getCurrentlyRunningByWorkstation(workstation);

        if (currentJobs == null || currentJobs.isEmpty())
            return 0;

        int duration = 0;
        for (Job job : currentJobs) {
            Procedure currentProcedure = jobService.getCurrentProcedure(job);

            if (currentProcedure == null)
                continue;

            if (currentProcedure.getProcessStep().getWorkstation().equals(workstation))
                duration += currentProcedure.getProcessStep().getEstDuration();
        }
        return duration;
    }

    /**
     * Gets active job assemblies count for a given workstation.
     *
     * @param workstation the workstation
     * @return the active job assemblies count
     */
    public int getActiveJobAssembliesCount(Workstation workstation) {
        if (workstation == null)
            return 0;

        List<Job> currentJobs = jobService.getCurrentlyRunningByWorkstation(workstation);

        if (currentJobs == null || currentJobs.isEmpty())
            return 0;

        List<Assembly> assemblies = null;

        int sampleCount = 0;
        for (Job job : currentJobs) {
            Procedure currentProcedure = jobService.getCurrentProcedure(job);

            if (currentProcedure == null)
                continue;

            if (currentProcedure.getProcessStep().getWorkstation().equals(workstation))
                assemblies = job.getAssemblies();

            if (assemblies == null)
                continue;

            for (Assembly assembly : assemblies) {
                sampleCount += assembly.getSampleCount();
            }
        }
        return sampleCount;
    }

    /**
     * Gets upcoming jobs for workstation. This excludes current jobs at the workstation.
     *
     * @param workstation the workstation
     * @return the upcoming jobs for workstation
     */
    public List<Job> getUpcomingJobsForWorkstation(Workstation workstation) {
        if (workstation == null)
            return null;

        Set<Job> upcomingJobs = new HashSet<>();

        for (Job job : jobService.getActive()) {
            Procedure currentProcedure = jobService.getCurrentProcedure(job);

            if (currentProcedure == null)
                return null;

            //Check if upcoming procedures for job are executed on given workstation
            int indexOfCurrentProcedure = job.getProcedures().indexOf(currentProcedure) + 1;
            int lastIndex = job.getProcedures().size();
            if (indexOfCurrentProcedure >= lastIndex)
                continue;

            List<Procedure> upcomingProcedures = job.getProcedures().subList(indexOfCurrentProcedure, lastIndex);

            for (Procedure procedure : upcomingProcedures) {
                if (procedure.getProcessStep().getWorkstation().equals(workstation))
                    upcomingJobs.add(job);
            }
        }

        return new ArrayList<>(upcomingJobs);
    }

    /**
     * Returns if a workstation can be deleted.
     *
     * @param workstation the workstation
     * @return the boolean
     */
    public boolean canDelete(Workstation workstation) {
        List<ProcessStep> processSteps = null;
        try {
            processSteps = processStepService.getByWorkstation(workstation);
        } catch (FindByException e) {
            logger.debug("could not query the workstation");
        }

        return processSteps == null || processSteps.isEmpty();
    }
}