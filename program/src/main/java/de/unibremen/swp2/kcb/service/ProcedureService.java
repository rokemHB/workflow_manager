package de.unibremen.swp2.kcb.service;

import com.google.gson.*;
import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.persistence.ProcedureRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.*;
import de.unibremen.swp2.kcb.validator.backend.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle Procedure.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Transactional
@ApplicationScoped
public class ProcedureService implements Service<Procedure> {

    /**
     * Logger object of the Procedure class
     */
    private static final Logger logger = LogManager.getLogger(ProcedureService.class);

    /**
     * Injected instance of {@link ProcedureValidator} to validate provided {@link Procedure}s.
     */
    @Inject
    private ProcedureValidator procedureValidator;

    /**
     * Injected instance of {@link UserValidator} to validate provided {@link User}s.
     */
    @Inject
    private UserValidator userValidator;

    /**
     * Injected instance of {@link WorkstationValidator} to validate provided {@link Workstation}s.
     */
    @Inject
    private WorkstationValidator workstationValidator;

    /**
     * Injected instance of {@link ProcessStepValidator} to validate provided {@link ProcessStep}s.
     */
    @Inject
    private ProcessStepValidator processStepValidator;

    /**
     * Injected instance of {@link StateValidator} to validate provided {@link State}s.
     */
    @Inject
    private StateValidator stateValidator;

    /**
     * Injected instance of {@link ValueValidator} to validate provided {@link Value}s.
     */
    @Inject
    private ValueValidator valueValidator;

    /**
     * Injected instance of {@link ProcedureRepository} to query database for {@link Procedure}s.
     */
    @Inject
    private ProcedureRepository procedureRepository;

    /**
     * Injected instance of {@link StateHistoryService}.
     */
    @Inject
    private StateHistoryService stateHistoryService;

    /**
     * Injected instance of {@link StateService}.
     */
    @Inject
    private StateService stateService;

    /**
     * Injected instance of {@link StateExecService}.
     */
    @Inject
    private StateExecService stateExecService;

    /**
     * Injected instance of {@link JobService}.
     */
    @Inject
    private JobService jobService;

    /**
     * Injected instance of {@link AssemblyService}.
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Injected instance of {@link UserService}.
     */
    @Inject
    private UserService userService;

    /**
     * Injected instance of {@link UserService}.
     */
    @Inject
    private ValueService valueService;

    /**
     * Stores the provided entity.
     * Performs validation using the backend validation module.
     *
     * @param entity to be created and persisted
     * @return Procedure that has been created and stored
     * @throws CreationException thrown if validating given entity fails
     * @see de.unibremen.swp2.kcb.validator.backend.Validator
     */
    @Override
    public Procedure create(final Procedure entity) throws CreationException {

        //Validating procedure
        boolean valid;
        try {
            logger.trace("Attempting to validate procedure \"{}\" ...", entity);
            valid = procedureValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred during procedure validation. Can't create procedure.");
            throw new CreationException("Can't create procedure: " + e.getMessage());
        }
        logger.trace("Validation of procedure \"{}\" completed without exceptions.", entity);

        //Checking, if procedure is valid
        if (!valid) {
            final String message = "Procedure \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new CreationException(message);
        } else logger.trace("Procedure \"{}\" is valid.", entity);

        //Saving procedure
        Procedure repoEntity;
        try {
            logger.trace("Attempting to save procedure \"{}\" ...", entity);
            repoEntity = procedureRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting procedure \"{}\". Can't create procedure.", entity);
            throw new CreationException("Can't create procedure: " + e.getMessage());
        }

        logger.trace("Saving of procedure \"{}\" completed without exceptions.", entity);
        logger.info("Create procedure \"{}\" - triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        logger.trace("Returning procedure \"{}\"", entity);
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
    public Procedure update(final Procedure entity) throws UpdateException {

        //Validating procedure
        boolean valid;
        try {
            logger.trace("Attempting to validate procedure \"{}\" ...", entity);
            valid = procedureValidator.validate(entity);
        } catch (ValidationException e) {
            logger.debug("Error occurred while validating procedure \"{}\". Can't update procedure", entity);
            throw new UpdateException("Can't update procedure: " + e.getMessage());
        }
        logger.trace("Validation of procedure \"{}\" completed without exceptions.", entity);

        //Checking, if procedure is valid
        if (!valid) {
            final String message = "Procedure \"" + entity + "\" is not valid.";
            logger.debug(message);
            throw new UpdateException(message);
        } else logger.trace("Procedure \"{}\" is valid.", entity);

        //Checking, if procedure is stored in database
        final Procedure storedProcedure = procedureRepository.findBy(entity.getId());
        if (storedProcedure == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Saving procedure
        Procedure repoEntity;
        try {
            logger.trace("Attempting to save new procedure \"{}\" ...", entity);
            repoEntity = procedureRepository.saveAndFlushAndRefresh(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting procedure \"{}\". Can't update procedure.", entity);
            throw new UpdateException("Can't update procedure: " + e.getMessage());
        }

        logger.trace("Saving of procedure \"{}\" completed without exceptions.", entity);
        logger.info("Update procedure \"{}\" - triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        logger.trace("Returning procedure \"{}\"", entity);
        return repoEntity;
    }

    /**
     * Deletes the provided entity.
     *
     * @param entity to be deleted
     * @throws DeletionException thrown if validating given entity fails
     */
    @Override
    public void delete(final Procedure entity) throws DeletionException {

        //Checking, if procedure is stored in database
        final Procedure storedProcedure = procedureRepository.findBy(entity.getId());
        if (storedProcedure == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new DeletionException(message);
        }

        //Deleting procedure
        try {
            logger.trace("Attempting to delete procedure \"{}\" ...", entity);
            procedureRepository.attachAndRemove(entity);
            logger.info("Delete procedure \"{}\" triggered by: {}", entity.getId(), userService.getExecutingUser().getUsername());
        } catch (PersistenceException e) {
            logger.debug("Error occurred while deleting procedure \"{}\". Can't delete procedure.", entity);
            throw new DeletionException("Can't delete procedure: " + e.getMessage());
        }
    }

    /**
     * Switches to the next state in the stateMachine for a given procedure. Creates a new StateExec.
     *
     * @param procedure the procedure
     */
    public void startNextState(Procedure procedure) throws SetNextStateException {
        if (procedure == null) {
            throw new SetNextStateException("Can't set next state: Procedure is already finished");
        }
        if (this.hasNextState(procedure)) {
            StateExec stateExec = new StateExec();
            stateExec.setState(this.getNextState(procedure));
            stateExec.setTransitionAt(LocalDateTime.now());
            procedure.getStateHistory().getStateExecs().add(stateExec);
        } else {
            throw new SetNextStateException("Can't set next state: Procedure is already finished");
        }
    }

    /**
     * Returns the current state of the procedure.
     *
     * @param procedure the procedure
     * @return the current state
     */
    public State getCurrentState(Procedure procedure) {
        StateHistory stateHistory = procedure.getStateHistory();
        List<StateExec> stateExecs = stateHistory.getStateExecs();
        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();

        if (stateExecs == null || stateExecs.isEmpty())
            return null;

        return states.get(stateExecs.size() - 1);
    }

    /**
     * Returns the next state of the procedure.
     *
     * @param procedure the procedure
     * @return the next state
     */
    public State getNextState(Procedure procedure) {
        StateHistory stateHistory = procedure.getStateHistory();
        List<StateExec> stateExecs = stateHistory.getStateExecs();
        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();

        if (this.hasNextState(procedure))
            return states.get(stateExecs.size());

        return null;
    }

    /**
     * Returns if the procedure has a next state.
     *
     * @param procedure the procedure
     * @return if the procedure has a next state
     */
    public boolean hasNextState(Procedure procedure) {
        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();
        State currentState = this.getCurrentState(procedure);

        return states.indexOf(currentState) < states.size() - 1;
    }

    /**
     * Perform an execution of a procedure. This will update the {@link StateHistory}
     * of the given {@link Procedure}.
     *
     * @param procedure to be updated and execution
     */
    public void doExec(final Procedure procedure) throws ExecutionException {
        logger.debug("Call to ProcedureService#doExec with '{}'. ", procedure);
        if (procedure == null) {
            throw new ExecutionException("Could not execute procedure: Parameter can't be null");
        } else if (!stateHistoryService.canExecute(procedure.getStateHistory())) {
            throw new ExecutionException("Could not execute procedure: Another execution is still running");
        } else if (this.needsCollection(procedure)) {
            throw new ExecutionException("Couldn't finish procedure: Has to be collected");
        }

        stateHistoryService.getCurrentStateExec(procedure.getStateHistory()).setTrigger(userService.getExecutingUser());
        stateHistoryService.getCurrentStateExec(procedure.getStateHistory()).setStartedAt(LocalDateTime.now());

        try {
            jobService.update(jobService.getByProcedure(procedure));
        } catch (UpdateException | FindByException e) {
            throw new ExecutionException("Can't execute procedure: " + e.getMessage());
        }
    }

    /**
     * Finishes a execution of a procedure. This will update the {@link StateHistory}
     * of the given {@link Procedure}.
     *
     * @param procedure to be updated and execution
     */
    public void doFinish(final Procedure procedure, final Integer transitionTime) throws ExecutionException {
        logger.debug("Call to ProcedureService#doExec with '{}'. ", procedure);

        if (procedure == null) {
            throw new ExecutionException("Couldn't finish procedure: Parameter can't be null");
        }
        StateHistory stateHistory = procedure.getStateHistory();
        if (!stateHistoryService.canFinish(stateHistory)) {
            throw new ExecutionException("Couldn't finish procedure: Execution is already completed");
        } else if (this.needsCollection(procedure)) {
            throw new ExecutionException("Couldn't finish procedure: Has to be collected");
        }

        stateHistoryService.getCurrentStateExec(procedure.getStateHistory()).setFinishedAt(LocalDateTime.now());

        //Set transitionTime in stateExec
        StateExec currentStateExec = stateHistoryService.getCurrentStateExec(stateHistory);
        int calculatedTransitionTime = stateExecService.calculateTransitionTime(currentStateExec);
        if (transitionTime == null || transitionTime == 0) {
            currentStateExec.setTransitionTime(calculatedTransitionTime);
        } else {
            currentStateExec.setTransitionTime(transitionTime);
        }

        State currentState = this.getCurrentState(procedure);
        StateMachine stateMachine = procedure.getProcessStep().getStateMachine();
        boolean isDeletes = procedure.getProcessStep().isDeletes();
        boolean isModifies = procedure.getProcessStep().isModifies();
        if (currentState == null)
            throw new ExecutionException("Can't finish procedure: Current state of procedure is null");
        Job job;
        try {
            job = jobService.getByProcedure(procedure);
        } catch (FindByException e) {
            throw new ExecutionException("Can't finish procedure: " + e.getMessage());
        }

        //Wenn processStep.deletes == true.
        if (stateService.isLastExecutableState(currentState, stateMachine) && isDeletes) {
            List<Assembly> assemblies = job.getAssemblies();
            if (assemblies == null)
                throw new ExecutionException("Can't finish procedure: Jobs assemblies are null");

            for (Assembly assembly : assemblies) {
                try {
                    assemblyService.delete(assembly);
                } catch (DeletionException e) {
                    logger.debug("Can't finish procedure: {}", e.getMessage());
                    throw new ExecutionException("Can't finish procedure: " + e.getMessage());
                }
            }
            job.setAssemblies(null);
            job.setJobState(JobState.FINISHED);
        }

        //Wenn processStep.modifies == true.
        if (stateService.isLastExecutableState(currentState, stateMachine) && isModifies) {
            List<Assembly> assemblies = job.getAssemblies();
            if (assemblies == null)
                throw new ExecutionException("Can't finish procedure: Jobs assemblies are null");

            for (Assembly assembly : assemblies) {
                assembly.getModifications().add(procedure);
                try {
                    assemblyService.update(assembly);
                } catch (UpdateException e) {
                    logger.debug("Can't finish procedure: {}", e.getMessage());
                    throw new ExecutionException("Can't finish procedure: " + e.getMessage());
                }
            }
        }

        try {
            this.startNextState(procedure);
        } catch (SetNextStateException e) {
            throw new ExecutionException("Can't finish procedure: Can't start next state: " + e.getMessage());
        }


        try {
            jobService.update(job);
        } catch (UpdateException e) {
            throw new ExecutionException("Can't finish procedure: " + e.getMessage());
        }
    }

    /**
     * Returns if the procedure is currently in the last executable state and creates a new assembly.
     * This method is needed for frontend purposes.
     *
     * @param procedure the procedure
     * @return the boolean
     */
    public boolean isInLastExecutableStateAndCreates(Procedure procedure) {
        if (procedure == null)
            return false;

        State currentState = this.getCurrentState(procedure);

        if (currentState == null)
            return false;

        StateMachine stateMachine = procedure.getProcessStep().getStateMachine();
        boolean creates = procedure.getProcessStep().isCreates();

        return stateService.isLastExecutableState(currentState, stateMachine) && creates;
    }



    /**
     * Returns if the current procedure can be executed.
     *
     * @param procedure the procedure
     * @return if the current procedure can be executed
     */
    public boolean canExecute(Procedure procedure) {
        if (procedure == null)
            return false;

        StateHistory stateHistory = procedure.getStateHistory();
        State currentState = this.getCurrentState(procedure);

        if (stateHistory == null)
            return false;

        return stateHistoryService.canExecute(stateHistory) &&
                !currentState.getName().equals("Transport");
    }

    /**
     * Returns if the current procedure can be finished.
     *
     * @param procedure the procedure
     * @return if the current procedure can be finished
     */
    public boolean canFinish(Procedure procedure) {
        if (procedure == null)
            return false;

        StateHistory stateHistory = procedure.getStateHistory();
        State currentState = this.getCurrentState(procedure);

        if (stateHistory == null)
            return false;

        return stateHistoryService.canFinish(stateHistory) &&
                !currentState.getName().equals("Transport");
    }

    /**
     * Returns if a given procedure is completed.
     *
     * @param procedure the procedure
     * @return whether a procedure is completed
     */
    public boolean isComplete(Procedure procedure) {
        if (procedure == null)
            return false;

        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();
        StateHistory stateHistory = procedure.getStateHistory();

        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return false;

        boolean isCompleteStateHistory = stateHistoryService.isComplete(stateHistory);

        return states.size() == stateExecs.size() && isCompleteStateHistory;
    }

    /**
     * Returns if a given procedure needs delivery.
     *
     * @param procedure the procedure
     * @return if the procedure needs delivery
     */
    public boolean needsDelivery(Procedure procedure) {
        if (procedure == null)
            return false;

        StateHistory stateHistory = procedure.getStateHistory();

        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return false;

        return stateExecs.isEmpty();
    }

    /**
     * Returns if a given procedure is collectable.
     *
     * @param procedure the procedure
     * @return if the procedure is collectable
     */
    public boolean needsCollection(Procedure procedure) {
        if (procedure == null)
            return false;

        List<State> states = procedure.getProcessStep().getStateMachine().getStateList();
        StateHistory stateHistory = procedure.getStateHistory();

        if (stateHistory == null)
            return false;

        List<StateExec> stateExecs = stateHistory.getStateExecs();

        if (stateExecs == null)
            return false;

        boolean isCompleteStateHistory = stateHistoryService.isComplete(stateHistory);
        State currentState = this.getCurrentState(procedure);

        if (currentState == null)
            return false;

        return states.size() == stateExecs.size() &&
                !isCompleteStateHistory &&
                currentState.getName().equals("Transport");
    }

    /**
     * Return all Procedures whose execution is finished and that can be picked
     * up by {@link Transport}.
     *
     * @return Collection of Procedures whose execution is finished.
     */
    public List<Procedure> getCollectable() {
        List<Procedure> collectables = new ArrayList<>();

        for (Procedure procedure : this.getAll()) {
            if (this.needsCollection(procedure)) {
                collectables.add(procedure);
            }
        }

        return collectables;
    }

    /**
     * Return the given Procedure as a JsonObject in the format used by the sfb
     *
     * @param procedure to be parsed to sfb valid json
     * @return JsonObject representation of the given Procedure
     */
    @RequiresAuthentication
    public JsonObject getAsSFBJSON(final Procedure procedure) {
        JsonObject result = new JsonObject();
        if (procedure == null) return result;
        ProcessStep step = procedure.getProcessStep();
        if (step == null) return result;
        result.add("step", new JsonPrimitive(step.getName()));
        JsonArray parameters = valueService.getAsSFBJSON(procedure.getValues());

        result.add("parameters", parameters);

        return result;
    }

    /**
     * Export parameters of job json file in the format used by the sfb.
     *
     * @param procedure to export parameters of
     * @return File containing sfb valid json of parameters for given job.
     */
    @RequiresAuthentication
    public File exportParameters(final Procedure procedure) throws ExportException {
        JsonArray result = new JsonArray();
        // One Procedure only executes on process step, so
        // the result will always contain exactly one entry
        result.add(getAsSFBJSON(procedure));
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return createTmpFile(gson.toJson(result), "parameters", ".json");
        } catch (IOException e) {
            logger.warn("IOException occurred during parameter export: {}", e.getMessage());
            throw new ExportException("IOException occurred during parameter export. Aborting.");
        }
    }

    /**
     * Gets active procedures by workstation.
     *
     * @param workstation the workstation
     * @return the active by workstation
     * @throws FindByException the find by exception
     */
    public List<Procedure> getActiveByWorkstation(final Workstation workstation) throws FindByException {
        logger.debug("Validating provided Workstation...");

        try {
            workstationValidator.validate(workstation);
        } catch (ValidationException e) {
            logger.debug("Provided Workstation could not be validated.");
            throw new FindByException("Can't validate provided Workstation.");
        }

        logger.debug("Trying to query active Procedure(s) by Workstation {}.", workstation.getName());
        return procedureRepository.findActiveByWorkstation(workstation);
    }

    /**
     * Return all Procedures that are currently located at a given {@link Workstation}.
     *
     * @param workstation the {@link Procedure}s are located at
     * @return Collection of procedures at the given {@link Workstation}
     */
    public List<Procedure> getByWorkstation(final Workstation workstation) throws FindByException {
        logger.debug("Validating provided Workstation...");

        try {
            workstationValidator.validate(workstation);
        } catch (ValidationException e) {
            logger.debug("Provided Workstation could not be validated.");
            throw new FindByException("Can't validate provided Workstation.");
        }

        logger.debug("Trying to query Procedure(s) by Workstation {}.", workstation.getName());
        return procedureRepository.findByWorkstation(workstation);
    }

    /**
     * Return all Procedures that are executing a specific {@link ProcessStep}.
     *
     * @param processStep the returned Procedures are executing
     * @return Collection of {@link ProcessStep} that are executed at the given {@link Workstation}.
     */
    public List<Procedure> getByProcessStep(final ProcessStep processStep) throws FindByException {
        logger.debug("Validating provided ProcessStep...");

        try {
            processStepValidator.validate(processStep);
        } catch (ValidationException e) {
            logger.debug("Provided ProcessStep could not be validated.");
            throw new FindByException("Can't validate provided ProcessStep.");
        }

        logger.debug("Trying to query Procedure(s) by ProcessStep {}", processStep.getName());
        return procedureRepository.findByProcessStep(processStep);
    }

    /**
     * Return all {@link Procedure}s that are currently in a given {@link State}.
     *
     * @param state the returned {@link Procedure} are currently in
     * @return Collection of Procedures that are currently in the given {@link State}
     */
    public List<Procedure> getByState(final State state) throws FindByException {
        logger.debug("Validating provided State...");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            logger.debug("Provided State could not be validated.");
            throw new FindByException("Can't validate provided State.");
        }

        logger.debug("Trying to query Procedure(s) by State {}.", state.getName());
        return procedureRepository.findByState(state);
    }

    /**
     * Return all {@link Procedure}s with a given {@link StateHistory}.
     *
     * @param stateHistory to search for
     * @return Collection of Procedures with the given {@link StateHistory}
     */
    public List<Procedure> getByStateHistory(final StateHistory stateHistory) throws FindByException {
        logger.debug("Trying to query Procedure(s) by StateHistory {}.", stateHistory.getId());
        return procedureRepository.findByStateHistory(stateHistory);
    }

    /**
     * Return all {@link Procedure}s that have a given {@link Value}.
     *
     * @param value the given value
     * @return Collection of {@link Procedure}s that have a given {@link Value}.
     */
    public List<Procedure> getByValue(Value value) throws FindByException {
        logger.debug("Validating provided Value...");

        try {
            valueValidator.validate(value);
        } catch (ValidationException e) {
            logger.debug("Provided Value could not be validated.");
            throw new FindByException("Can't validate provided Value.");
        }

        logger.debug("Trying to query Procedure(s) by Value {}.", value.getId());
        return procedureRepository.findByValue(value);
    }

    /**
     * Return all existing Procedures.
     *
     * @return Collection of all existing Procedures.
     */
    public List<Procedure> getAll() {
        logger.debug("Trying to query all stored Procedures.");
        return procedureRepository.findAll();
    }

    /**
     * Return the {@link Procedure} with the given id.
     *
     * @param id of the Procedure
     * @return Procedure with the given id
     * @throws InvalidIdException thrown if validating given ID fails
     */
    public Procedure getById(String id) throws InvalidIdException {

        //Validating procedureID
        try {
            Service.super.checkId(id);
        } catch (InvalidIdException e) {
            final String message = "ProcedureID \"" + id + "\" is not valid. ";
            logger.debug(message);
            throw new InvalidIdException(message + e.getMessage());
        }
        logger.trace("ProcedureID \"{}\" is valid.", id);

        //Finding procedure by ID
        final Procedure entity;
        try {
            logger.trace("Attempting to find procedure by ID \"{}\" ...", id);
            entity = procedureRepository.findBy(id);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while finding procedure by ID \"{}\". Can't find procedure.", id);
            throw new InvalidIdException("Can't find procedure: " + e.getMessage());
        }

        logger.trace("Finding of procedure by ID \"{}\" completed without exceptions.", id);
        logger.info("Find procedure by ID \"{}\" - triggered by: {}", id, userService.getExecutingUser().getUsername());
        logger.trace("Returning procedure by ID \"{}\"", id);
        return entity;
    }
}