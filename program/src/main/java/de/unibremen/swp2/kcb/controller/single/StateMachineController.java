package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.StateMachineService;
import de.unibremen.swp2.kcb.service.StateService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.StateMachine.StateMachine}
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 */
@ViewScoped
@Named("stateMachineController")
public class StateMachineController extends SingleController<StateMachine> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(StateMachineController.class);

    /**
     * StateMachine Service to handle actions
     */
    @Inject
    private StateMachineService stateMachineService;

    /**
     * Injected instance of stateService
     */
    @Inject
    private StateService stateService;

    /**
     * DualListModel object of stateMachineController
     */
    @Getter
    @Setter
    private DualListModel<State> selectedStates;

    /**
     * setup method of satateMachineController
     */
    @PostConstruct
    public void setup() {
        List<State> allStates = stateService.getAllWithoutTransport();
        List<State> selectedStateList = new ArrayList<>();

        this.entity = new StateMachine();
        selectedStates = new DualListModel<>(allStates, selectedStateList);
    }

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        if (this.entity != null && selectedStates != null && selectedStates.getTarget() != null)
            this.entity.setStateList(selectedStates.getTarget());
        try {
            stateMachineService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during StateMachine creation.", e);
            final String statemachineName = this.getSaveStateMachineName(entity);
            final String summary = localeController.formatString("error.summary.statemachine-creation");
            final String detail = localeController.formatString("error.detail.statemachine-creation");
            final String updatedDetail = detail != null ? detail.replace("<statemachineName>", statemachineName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("StateMachine with that name already exists.", e);
            final String stateMachineName = this.getSaveStateMachineName(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", stateMachineName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            stateMachineService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during StateMachine deletion.", e);
            final String statemachineName = this.getSaveStateMachineName(entity);
            final String summary = localeController.formatString("error.summary.statemachine-deletion");
            final String detail = localeController.formatString("error.detail.statemachine-deletion");
            final String updatedDetail = detail != null ? detail.replace("<statemachineName>", statemachineName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        if (this.entity != null && selectedStates != null && selectedStates.getTarget() != null)
            this.entity.setStateList(selectedStates.getTarget());
        try {
            stateMachineService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during StateMachine update.", e);
            final String statemachineName = this.getSaveStateMachineName(entity);
            final String summary = localeController.formatString("error.summary.statemachine-update");
            final String detail = localeController.formatString("error.detail.statemachine-update");
            final String updatedDetail = detail != null ? detail.replace("<statemachineName>", statemachineName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    public void reset() {
        this.setup();
    }

    public void updateEntity(StateMachine entity) {
        List<State> allStates = stateService.getAll();
        this.entity = entity;
        this.selectedStates.setTarget(entity.getStateList());
        this.selectedStates.setSource(this.intersect(allStates, entity.getStateList()));
        super.render("statemachine_edit_form");
    }

    /**
     * Return a list containing all values of all that are not contained in the selected lsit.
     *
     * @param all      items to look for
     * @param selected items to remove from all items
     * @return items from all that are not contained in selected
     */
    private List<State> intersect(final List<State> all, final List<State> selected) {
        ArrayList<State> result = new ArrayList<>();
        for (final State state : all) {
            if (!selected.contains(state))
                result.add(state);
        }
        return result;
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get name of
     * @return name if entity has one, 'null' otherwise
     */
    private String getSaveStateMachineName(final StateMachine entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Returns if a given stateMachine can be deleted.
     *
     * @param stateMachine the state machine
     * @return the boolean
     */
    public boolean canDelete(StateMachine stateMachine) {
        return stateMachineService.canDelete(stateMachine);
    }
}