package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.persistence.AssemblyRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.util.EmailUtil;
import de.unibremen.swp2.kcb.validator.backend.AssemblyValidator;
import de.unibremen.swp2.kcb.validator.backend.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service class to handle Assemblies.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
@ApplicationScoped
public class AssemblyService implements Service<Assembly> {

    /**
     * Logger object of the UserService class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyService.class);

    /**
     * Injected instance of {@link AssemblyValidator} to validate provided {@link Assembly}s.
     */
    @Inject
    private AssemblyValidator assemblyValidator;

    /**
     * Injected instance of {@link AssemblyRepository} to validate provided {@link Assembly}s.
     */
    @Inject
    private AssemblyRepository assemblyRepository;

    /**
     * Injected instance of {@link UserService} to validate provided {@link Assembly}s.
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of {@link JobService} to validate provided {@link Assembly}s.
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link ProcedureService}
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Injected instance of {@link EmailUtil}
     */
    @Inject
    private EmailUtil emailUtil;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Assembly that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    @Transactional
    public Assembly create(final Assembly entity) throws CreationException {

        //Validating assembly
        boolean valid;
        try {
            logger.trace("Attempting to validate assembly \" {} \" ...", entity);
            valid = assemblyValidator.validate(entity);
            logger.trace("Checking assemblyId for assembly \" {} \" ...", entity);
            verifyAssemblyId(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during assembly validation. Can't create assembly.");
            throw new CreationException("Can't create assembly: " + e.getMessage());
        } catch (OccupiedAssemblyIdException e) {
            logger.debug("Error occurred during assembly validation. Can't create assembly. AssemblyId already occupied");
            throw new CreationException("Can't create assembly: " + e.getMessage());
        }
        logger.trace("Validation of assembly \" {} \" completed without exceptions.", entity);

        //Checking, if assembly is valid
        if (!valid) {
            final String message = "Assembly \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Assembly \" {} \" is valid.", entity);

        //Saving assembly
        Assembly repoEntity;
        try {
            logger.trace("Attempting to save assembly \" {} \" ...", entity);
            repoEntity = assemblyRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting assembly \" {} \". Can't create assembly.", entity);
            throw new CreationException("Can't create assembly: " + e.getMessage());
        }

        logger.trace("Saving of assembly \" {} \" completed without exceptions.", entity);
        logger.info("Create assembly \" {} \" - triggered by: {}", entity.getAssemblyID(), userService.getExecutingUser().getUsername());
        logger.trace("Returning assembly \" {} \"", entity);
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
    @Transactional
    public Assembly update(final Assembly entity) throws UpdateException {

        //Validating assembly
        boolean valid;
        try {
            logger.trace("Attempting to validate assembly \" {} \" ...", entity);
            valid = assemblyValidator.validate(entity);
            logger.trace("Checking assemblyId for assembly \" {} \" ...", entity);
            verifyAssemblyId(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating assembly \" {} \". Can't update assembly", entity);
            throw new UpdateException("Can't update assembly: " + e.getMessage());
        } catch (OccupiedAssemblyIdException e) {
            logger.debug("Error occurred during assembly validation. Can't create assembly. AssemblyId already occupied");
            throw new UpdateException("Can't create assembly: " + e.getMessage());
        }
        logger.trace("Validation of assembly \" {} \" completed without exceptions.", entity);

        //Checking, if assembly is valid
        if (!valid) {
            final String message = "Assembly \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Assembly \" {} \" is valid.", entity);

        //Checking, if assembly is stored in database
        final Assembly storedAssembly = assemblyRepository.findBy(entity.getId());
        if (storedAssembly == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving assembly
        Assembly repoEntity;
        try {
            logger.trace("Attempting to save new assembly \" {} \" ...", entity);
            repoEntity = assemblyRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting assembly \" {} \". Can't update assembly.", entity);
            throw new UpdateException("Can't update assembly: " + e.getMessage());
        }

        logger.trace("Saving of assembly \" {} \" completed without exceptions.", entity);
        logger.info("Update assembly \" {} \" - triggered by: {}", entity.getAssemblyID(), userService.getExecutingUser().getUsername());
        logger.trace("Returning assembly \" {} \"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    @Transactional
    public void delete(final Assembly entity) throws DeletionException {

        //Checking, if assembly is stored in database
        final Assembly storedAssembly = assemblyRepository.findBy(entity.getId());
        if (storedAssembly == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting assembly
        try {
            logger.trace("Attempting to delete assembly \" {} \" ...", entity);
            assemblyRepository.attachAndRemove(entity);
            logger.info("Delete assembly \" {} \" - triggered by: {}", entity.getAssemblyID(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting assembly \" {} \". Can't delete assembly.", entity);
            throw new DeletionException("Can't delete assembly: " + e.getMessage());
        }
    }

    /**
     * Return all existing Assemblies.
     *
     * @return List of all existing Assemblies.
     */
    public List<Assembly> getAll() {
        logger.trace("Returning all assemblies");
        return assemblyRepository.findAll();
    }

    /**
     * Return the {@link Assembly} with the given id.
     *
     * @param id of the Assembly
     * @return Assembly with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Assembly getById(String id) throws InvalidIdException {

        //Validating assemblyID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "AssemblyID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("AssemblyID \" {} \" is valid.", id);

        //Finding assembly by ID
        final Assembly entity;
        try {
            logger.trace("Attempting to find assembly by ID \" {} \" ...", id);
            entity = assemblyRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding assembly by ID \" {} \". Can't find assembly.", id);
            throw new InvalidIdException("Can't find assembly: " + e.getMessage());
        }

        logger.trace("Finding of assembly by ID \" {} \" completed without exceptions.", id);
        logger.info("Find assembly by ID \" {} \" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning assembly by ID \" {} \"", id);
        return entity;
    }

    /**
     * Job is collectable boolean.
     *
     * @param assembly the assembly
     * @return the boolean
     */
    public boolean isCollectable(Assembly assembly) {
        return (procedureService.needsCollection(jobService.getCurrentProcedure(jobService.getByAssembly(assembly))));
    }

    /**
     * Returns all Assemblies ready for transport list.
     *
     * @return List of all existing Assemblies.
     */
    public List<Assembly> getCollectable() {
        List<Procedure> collectableProcedures = procedureService.getCollectable();
        List<Assembly> collectableAssemblies = new ArrayList<>();
        for (Assembly assembly : this.getAll()) {
            if (jobService.getByAssembly(assembly) == null)
                continue;
            if (collectableProcedures.contains(jobService.getCurrentProcedure(jobService.getByAssembly(assembly)))) {
                collectableAssemblies.add(assembly);
            }
            if (jobService.getByAssembly(assembly).getJobState() == JobState.PROCESSING &&
                    assembly.getCarriers().get(0).getLocation().getPosition().equals("Lager"))
                collectableAssemblies.add(assembly);
        }

        return collectableAssemblies;
    }

    /**
     * Return Assemblies that are currently being processed.
     *
     * @return List of Assemblies currently being processed.
     */
    public List<Assembly> getProcessing() {
        List<Assembly> assemblies = assemblyRepository.findAll();
        List<Assembly> result = new ArrayList<>();
        for (Assembly assembly : assemblies) {
            final Job job = jobService.getByAssembly(assembly);
            if (jobService.isActive(job)) result.add(assembly);
        }
        return result;
    }

    /**
     * Return Assemblies that are stored in {@link Stock}
     * and are available for processing.
     *
     * @return List of Assemblies currently located at {@link Stock}.
     */
    public List<Assembly> getInStock() {
        List<Assembly> assemblies = assemblyRepository.findAll();
        List<Assembly> result = new ArrayList<>();
        for (Assembly assembly : assemblies) {
            final List<Carrier> carriers = assembly.getCarriers();
            if (carriers == null || carriers.size() == 0) continue;
            // All carriers of a given assembly need to be at the same location
            Location location = carriers.get(0).getLocation();
            if (location instanceof Stock) result.add(assembly);
        }
        return result;
    }

    /**
     * Gets next position for active assembly.
     *
     * @param assembly the assembly
     * @return the next position for active assembly
     */
    public String getNextPositionForActiveAssembly(Assembly assembly) {
        if (jobService.getByAssembly(assembly) == null)
            return null;

        Procedure currentProcedure = jobService.getCurrentProcedure(jobService.getByAssembly(assembly));
        Job job = jobService.getByAssembly(assembly);
        if (job.getProcedures().indexOf(currentProcedure) == job.getProcedures().size() - 1)
            return "Lager";
        else
            return currentProcedure.getProcessStep().getWorkstation().getName();
    }

    /**
     * Separate a given {@link Assembly} in many Assemblies containing only one Sample.
     * All attributes of the given assembly are transferred to all new assemblies.
     *
     * @param assembly to be separated
     * @return Collection of {@link Assembly} with only one Sample and all attributes from the given assembly.
     */
    public List<Assembly> separate(final Assembly assembly) throws SplitException {
        return splitBySize(assembly, 1);
    }

    /**
     * Split a given {@link Assembly} into parts, each containing an equal amount of samples.
     * If the samples cannot be distributed evenly, one Assembly will contain less samples.
     *
     * @param assembly to be split into parts
     * @param parts    the {@link Assembly} will be split in.
     * @return Collection of Assemblies containing the samples from the original Assembly.
     * @throws SplitException if splitting fails.
     */
    public List<Assembly> splitByParts(final Assembly assembly, final int parts) throws SplitException {
        if (assembly == null)
            throw new SplitException("Can't split null Assembly.");
        if (parts <= 0)
            throw new SplitException("Parts can't be zero or negative.");
        if (parts > assembly.getSampleCount())
            throw new SplitException("Parts can't be greater than sampleCount.");

        return splitBySize(assembly, (int) Math.ceil((double) assembly.getSampleCount() / parts));
    }

    /**
     * Split a given {@link Assembly} into parts, each containing an equal amount of samples.
     * If the samples cannot be distributed evenly, one Assembly will contain less samples.
     *
     * @param assembly to be split into parts
     * @param size     the size of each part.
     * @return Collection of Assemblies containing the samples from the original Assembly.
     * @throws SplitException if splitting fails.
     */
    public List<Assembly> splitBySize(final Assembly assembly, final int size) throws SplitException {
        if (assembly == null)
            throw new SplitException("Can't split null Assembly.");
        if (size <= 0)
            throw new SplitException("Size can't be zero or negative.");
        if (size > assembly.getSampleCount())
            throw new SplitException("Size can't be greater than sampleCount.");

        List<Assembly> splitAssembly = new ArrayList<>();

        if (assembly.getSampleCount() <= size) {
            splitAssembly.add(assembly);
            logger.debug("Parameter size is higher or equal to sampleCount. Returning initial assembly...");
            return splitAssembly;
        }

        //Calculate the parts, the assembly should be split into with given size
        int parts = (int) Math.ceil((double) assembly.getSampleCount() / size);
        String[] assemblyIDParts = assembly.getAssemblyID().split("\\.");

        int length = 0;

        for (int i = 0; i < parts; i++) {
            try {
                assemblyIDParts[assemblyIDParts.length - 1] = Integer.toString(Integer.parseInt(assemblyIDParts[assemblyIDParts.length - 1]) + length);
            } catch (NumberFormatException e) {
                logger.warn("Error occurred while splitting the assembly {}. Can't read AssemblyID.", assembly,  e);
                throw new SplitException("Can't read AssemblyID: " + e.toString());
            }
            String assemblyID = String.join(".", assemblyIDParts);

            //This part calculates the size of the assembly, so all split assemblies match the total sampleCount of the initial assembly
            int start = i * size;
            length = Math.min(assembly.getSampleCount() - start, size);

            //Transfer data from old assembly to all new assemblies.
            Assembly a = new Assembly();
            a.setAssemblyID(assemblyID);
            a.setSampleCount(length);
            a.setAlloy(assembly.getAlloy());
            a.setModifications(assembly.getModifications());
            a.setPositionAtCarrier(assembly.getPositionAtCarrier());
            a.setComment(assembly.getComment());
            a.setCarriers(assembly.getCarriers());

            //Add new assembly to collection.
            splitAssembly.add(a);
        }

        final User executingUser = userService.getExecutingUser();
        if (executingUser == null) {
            logger.debug("Something went wrong. Executing User can't be found.");
            throw new SplitException("Something went wrong. Executing user can't be found");
        }

        //Creating new assemblies and deleting old assembly
        try {
            for (Assembly a : splitAssembly) {
                assemblyRepository.save(a);
            }
            assemblyRepository.attachAndRemove(assembly);
        } catch (OptimisticLockException e) {
            logger.debug("Assembly was locked. Will rollback transaction.");

            throw new SplitException("Assembly was locked: " + e.toString());
        }

        logger.info("Split assembly: {} triggered by: {}", assembly, executingUser.getUsername());
        return splitAssembly;
    }

    /**
     * Returns all assemblies that are on carrier of a given carrierType.
     *
     * @param carrierType the carrierType
     * @return list of assemblies on that carrierType
     */
    public List<Assembly> getByCarrierType(CarrierType carrierType) {
        if (carrierType == null)
            return assemblyRepository.findAll();
        List<Assembly> result = new ArrayList<>();
        for (Assembly assembly : assemblyRepository.findAll()) {
            if (assembly.getCarriers().get(0).getCarrierType().equals(carrierType)) result.add(assembly);
        }
        return result;
    }

    /**
     * Gets by carrier.
     *
     * @param carrier the carrier
     * @return the by carrier
     */
    public List<Assembly> getByCarrier(Carrier carrier) {
        if (carrier == null)
            return null;

        return assemblyRepository.findAssemblyByCarrier(carrier);
    }

    /**
     * Return all Assemblies with a given count of samples.
     *
     * @param sampleCount The sample count.
     * @return Collection of Assemblies with the same sample count.
     * @throws FindByException if findBy fails
     */
    public List<Assembly> getBySampleCount(int sampleCount) throws FindByException {
        if (sampleCount < 0) {
            throw new FindByException("Can't find negative sampleCount.");
        }

        return assemblyRepository.findBySampleCount(sampleCount);
    }

    /**
     * Return all Assemblies with a given alloy.
     *
     * @param alloy The alloy
     * @return Collection of Assemblies with the same alloy.
     * @throws FindByException if findBy fails
     */
    public List<Assembly> getByAlloy(String alloy) throws FindByException {
        if (alloy == null) {
            throw new FindByException("Can't find null alloy.");
        }

        return assemblyRepository.findByAlloy(alloy);
    }

    /**
     * Checks if the given assemblyID violates another assemblies assemblyID range.
     *
     * @param assembly the assemblyID
     * @throws OccupiedAssemblyIdException when the range is occupied
     */
    public void verifyAssemblyId(Assembly assembly) throws OccupiedAssemblyIdException {
        String assemblyID = assembly.getAssemblyID();

        String[] paramAssemblyIdParts = assemblyID.split("\\.");
        int paramLastId = Integer.parseInt(paramAssemblyIdParts[paramAssemblyIdParts.length - 1]);
        String[] paramAssemblyIdPartsNew = Arrays.copyOf(paramAssemblyIdParts, paramAssemblyIdParts.length - 1);
        String preParamAssemblyId = String.join(".", paramAssemblyIdPartsNew) + ".";

        List<Assembly> assemblyList = assemblyRepository.findAssemblyByAssemblyID(preParamAssemblyId + "%");

        for (Assembly a : assemblyList) {
            if (a.equals(assembly)) continue;

            String[] repoAssemblyIdParts = a.getAssemblyID().split("\\.");
            int repoLastId = Integer.parseInt(repoAssemblyIdParts[repoAssemblyIdParts.length - 1]);

            if (paramLastId >= repoLastId && paramLastId < repoLastId + a.getSampleCount() || paramLastId +
                    assembly.getSampleCount() - 1 >= repoLastId && paramLastId + assembly.getSampleCount() - 1 < repoLastId + a.getSampleCount())
                throw new OccupiedAssemblyIdException("The space is already reserved for another assembly.");
        }
    }

    /**
     * Sets all Jobs related to the given Assembly to {@link JobState#CANCELLED} and sends Mail to all {@link Role#ADMIN}s.
     * @param entity Assembly to be searched for
     * @throws NotifyLossException when there was no Job found
     */
    public void notifyLoss(Assembly entity) throws NotifyLossException {
         List<Job> jobs = jobService.getAllByAssembly(entity);

        if(jobs == null || jobs.isEmpty()) {
            logger.debug("Found no Job for given Assembly.");
        } else {
            for(Job job : jobs) {
                job.setJobState(JobState.CANCELLED);
                job.setAssemblies(null);
            }
        }

        try {
            emailUtil.sendAssemblyLostMessage(entity);
        } catch (MessagingException e) {
            throw new NotifyLossException("E-Mail couldn't be sent");
        }
    }

    /**
     * Filters all jobs and checks which are noch in active.
     *
     * @param assemblies the assemblies
     * @return the list
     * @throws FilterAssembliesException the filter assemblies exception
     */
    public List<Assembly> filterNotInActiveJob(List<Assembly> assemblies)  throws FilterAssembliesException {
        List<Assembly> assembliesToDelete = new ArrayList<>();

        for(Assembly assembly : assemblies) {
            Job job = jobService.getByAssembly(assembly);

            if(job == null) continue;

            assembliesToDelete.addAll(job.getAssemblies());
        }

        assemblies.removeAll(assembliesToDelete);
        return assemblies;
    }

    /**
     * checks whether a given job is in use boolean.
     *
     * @param assembly the assembly
     * @return the boolean
     */
    public boolean isInUse(Assembly assembly) {
        Job job = jobService.getByAssembly(assembly);

        return job != null;
    }
}