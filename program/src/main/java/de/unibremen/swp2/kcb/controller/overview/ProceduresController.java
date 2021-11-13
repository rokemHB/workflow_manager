package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import de.unibremen.swp2.kcb.service.ProcedureService;
import de.unibremen.swp2.kcb.service.serviceExceptions.ExecutionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.ExportException;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller Class to handle a collection of {@link Procedure}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@Named
@ViewScoped
public class ProceduresController extends OverviewController<Procedure> {

    /**
     * Logger object of the CarriersController class
     */
    private static final Logger logger = LogManager.getLogger(ProceduresController.class);

    /**
     * Injected instance of {@link ProcedureService} to handle business logic
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Return a collection of {@link Procedure}s, that are performed at a given {@link Workstation}.
     *
     * @param workstation {@link Workstation} to which the List of {@link Procedure}s refers.
     * @return List of {@link Procedure}s, that are performed at a given {@link Workstation}.
     */
    public List<Procedure> getByWorkstation(Workstation workstation) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Procedure(s) by Workstation");
            this.entities = procedureService.getByWorkstation(workstation);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.procedure-by-workstation-not-found",
                    "error.detail.procedure-by-workstation-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Returns a collection of {@link Procedure}s, that are finished and produced a {@link Assembly},
     * which is ready to be collected.
     *
     * @return Collection of {@link Procedure}s, that are finished.
     */
    public List<Procedure> getCollectable() {
        this.entities = new ArrayList<>();

        List<Procedure> collectable = procedureService.getCollectable();

        logger.debug("Querying Procedure(s) by collectable state");
        this.entities = collectable;
        if (collectable.isEmpty()) {
            super.displayMessageFromResource("error.summary.procedure-collectables-not-found",
                    "error.detail.procedure-collectables-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Executes a {@link Procedure}.
     *
     * @param procedure      {@link Procedure} to execute.
     */
    public void doExec(Procedure procedure) {

        try {
            procedureService.doExec(procedure);
        } catch (ExecutionException e) {
            super.displayMessageFromResource("error.summary.procedure-exec-fail",
                    "error.detail.procedure-exec-fail", FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Refresh the collection of all {@link Procedure}s.
     */
    @Override
    public void refresh() {
        List<Procedure> procedures = procedureService.getAll();

        if (procedures == null || procedures.isEmpty()) {
            logger.debug("Procedures couldn't be loaded. ProceduresController is empty.");
            this.entities = new ArrayList<>();
            super.displayMessageFromResource("error.summary.empty-procedures",
                    "error.detail.empty-procedures", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.entities = procedures;
    }

    /**
     * Get Procedure by ID
     *
     * @param id the ID
     * @return Procedure with that ID
     */
    @Override
    public Procedure getById(String id) {
        this.entities = new ArrayList<>();

        try {
            return procedureService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.procedure-by-id-not-found",
                    "error.detail.procedure-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return null;
    }

    /**
     * Find all Procedures with a certain Value
     *
     * @param value the Value to be searched for
     * @return all Procedures with that Value
     * @see Value
     */
    List<Procedure> getByValue(Value value) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Procedure(s) by Value");
            this.entities = procedureService.getByValue(value);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.procedure-by-value-not-found",
                    "error.detail.procedure-by-value-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Find all Procedures with a certain ProcessStep
     *
     * @param processStep the ProcessStep to be searched for
     * @return all Procedures with that ProcessStep
     * @see ProcessStep
     */
    List<Procedure> getByProcessStep(ProcessStep processStep) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Procedure(s) by ProcessStep");
            this.entities = procedureService.getByProcessStep(processStep);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.procedure-by-step-not-found",
                    "error.detail.procedure-by-step-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Find all Procedures with a certain StateHistory
     *
     * @param stateHistory the StateHistory to be searched for
     * @return all Procedures with that StateHistory
     * @see StateHistory
     */
    List<Procedure> getByStateHistory(StateHistory stateHistory) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Procedure(s) by StateHistory");
            this.entities = procedureService.getByStateHistory(stateHistory);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.procedure-by-statehistory-not-found",
                    "error.detail.procedure-by-statehistory-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Find all Procedures in a certain State
     *
     * @param state the State to be searched for
     * @return all Procedures in that State
     * @see State
     */
    List<Procedure> getByState(State state) {
        this.entities = new ArrayList<>();

        try {
            logger.debug("Querying Procedure(s) by StateHistory");
            this.entities = procedureService.getByState(state);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.procedure-by-state-not-found",
                    "error.detail.procedure-by-state-not-found", FacesMessage.SEVERITY_ERROR);
        }

        return this.entities;
    }

    /**
     * Export the given Procedure as a file containing json in the
     * format the sfb uses.
     *
     * @param procedure to export parameters for
     * @return StreamedContent of parameters for given procedure.
     */
    public StreamedContent exportParameters(final Procedure procedure) {
        try {
            File tmpFile = this.procedureService.exportParameters(procedure);
            final String downloadName = "parameters.json";

            if (tmpFile == null) {
                super.displayMessageFromResource("error.summary.export-failed", "error.detail.export-failed", FacesMessage.SEVERITY_ERROR);
                return null;
            }

            return new DefaultStreamedContent(new FileInputStream(tmpFile), "application/json", downloadName);

        } catch (ExportException | FileNotFoundException e) {
            logger.debug(e);
            super.displayMessageFromResource("error.summary.export-failed", "error.detail.export-failed", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }
}