package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.service.StateService;
import de.unibremen.swp2.kcb.service.serviceExceptions.FindByException;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidIdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a collection of {@link State}s.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("statesController")
public class StatesController extends OverviewController<State> {

    /**
     * Logger object of the StatesController class
     */
    private static final Logger logger = LogManager.getLogger(StatesController.class);

    /**
     * Injected instance of {@link StateService} to handle business logic
     */
    @Inject
    private StateService stateService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link State}s.
     */
    @Override
    public void refresh() {
        List<State> states = stateService.getAll();
        this.entities = new ArrayList<>();

        if (states == null || states.isEmpty()) {
            logger.debug("States couldn't be loaded. StatesController is empty.");
            super.displayMessageFromResource("error.summary.empty-states",
                    "error.detail.empty-states", FacesMessage.SEVERITY_ERROR);
        } else {
            this.entities = states;
        }
    }

    /**
     * Get State by ID
     *
     * @param id the ID
     * @return State with that ID
     */
    @Override
    public State getById(String id) {
        State result = null;

        try {
            result = stateService.getById(id);
        } catch (InvalidIdException e) {
            super.displayMessageFromResource("error.summary.state-by-id-not-found",
                    "error.detail.state-by-id-not-found", FacesMessage.SEVERITY_ERROR);
        }
        return result;
    }

    /**
     * Return State with the given name
     *
     * @param name of the state
     * @return List of state with the given name. Should only contain one State
     */
    List<State> getByName(String name) {
        List<State> states = new ArrayList<>();

        try {
            states = stateService.getByName(name);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.states-nameValidation",
                    "error.summary.states-nameValidation", FacesMessage.SEVERITY_ERROR);
            logger.debug("Given name is invalid.");
        }
        this.entities = states;
        return this.entities;
    }

    /**
     * Return States with given blocking state
     * A blocking state blocks the workstation while being executed
     *
     * @param blocking state of the State
     * @return List of states with the given blocking state
     */
    List<State> getByBlocking(boolean blocking) {
        List<State> states = new ArrayList<>();

        try {
            states = stateService.getByBlocking(blocking);
        } catch (FindByException e) {
            super.displayMessageFromResource("error.summary.states-by-blocking",
                    "error.detail.states-by-blocking", FacesMessage.SEVERITY_ERROR);
            logger.debug("Given blocking state is invalid.");
        }
        this.entities = states;
        return this.entities;
    }

    /**
     * Return the collection of all existing {@link State}s.
     *
     * @return All existing {@link State}s.
     */
    public List<State> getAll() {
        this.entities = new ArrayList<>();
        logger.debug("Querying all stored States.");

        this.entities = stateService.getAll();
        return this.entities;
    }
}