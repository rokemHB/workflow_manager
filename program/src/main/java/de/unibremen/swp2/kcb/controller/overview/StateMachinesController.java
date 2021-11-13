package de.unibremen.swp2.kcb.controller.overview;

import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.StateMachineService;
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
 * Controller class to handle a collection of {@link StateMachine}s.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named
public class StateMachinesController extends OverviewController<StateMachine> {

    /**
     * Logger object of the StateMachineController class
     */
    private static final Logger logger = LogManager.getLogger(StateMachinesController.class);

    /**
     * Injected instance of {@link StateMachineService} to handle business logic
     */
    @Inject
    private StateMachineService stateMachineService;

    /**
     * Initially refreshes data content after injections are done
     */
    @PostConstruct
    public void init() {
        this.refresh();
    }

    /**
     * Refresh the collection of all {@link StateMachine}s.
     */
    @Override
    public void refresh() {
        List<StateMachine> stateMachines = stateMachineService.getAll();
        if (stateMachines == null || stateMachines.isEmpty()) {
            logger.debug("StateMachine couldn't be loaded. StateMachine"
                    + "Controller is empty.");
            super.displayMessageFromResource("error.summary.empty-stateMachines",
                    "error.detail.empty-stateMachines", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.entities = new ArrayList<>();
        this.entities = stateMachines;
    }

    /**
     * Get StateMachine by ID
     *
     * @param id the ID
     * @return StateMachine with that ID
     */
    @Override
    public StateMachine getById(String id) {
        try {
            return stateMachineService.getById(id);
        } catch (InvalidIdException e) {
            logger.debug("Provided StateMachine ID was found invalid.");
            final String summary = localeController.formatString("error.summary.stateMachine-idValidation");
            final String detail = localeController.formatString("error.detail.stateMachine-idValidation");
            final String updatedDetail = detail != null ? detail.replace("<idString>", id) : null;
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    /**
     * Return StateMachine with the given name
     *
     * @param name of the returned StateMachine
     * @return List of all StateMachines with the given name. Should contain only one StateMachine.
     */
    public List<StateMachine> getByName(String name) {
        List<StateMachine> stateMachines = new ArrayList<>();
        try {
            stateMachines = stateMachineService.getByName(name);
        } catch (FindByException e) {
            final String summary = localeController.formatString("error.summary.stateMachine-nameValidation");
            final String detail = localeController.formatString("error.detail.stateMachine-nameValidation");
            final String updatedDetail = detail != null ? detail.replace("<name>", name) : null;
            logger.debug("Given name is invalid.");
            super.displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
        this.entities = stateMachines;
        return this.entities;
    }
}