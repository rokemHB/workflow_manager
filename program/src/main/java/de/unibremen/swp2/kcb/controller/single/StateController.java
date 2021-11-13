package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.service.StateService;
import de.unibremen.swp2.kcb.service.serviceExceptions.CreationException;
import de.unibremen.swp2.kcb.service.serviceExceptions.DeletionException;
import de.unibremen.swp2.kcb.service.serviceExceptions.EntityAlreadyExistingException;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller class to handle a single {@link de.unibremen.swp2.kcb.model.StateMachine.State}.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Arvid
 * @author Sören
 */
@ViewScoped
@Named("stateController")
public class StateController extends SingleController<State> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(StateController.class);

    /**
     * State Service to handle actions
     */
    @Inject
    private StateService stateService;

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        try {
            stateService.create(this.entity);
        } catch (CreationException e) {
            logger.debug("Error occurred during State creation.", e);
            final String statename = this.getSaveStatename(this.entity);
            final String summary = localeController.formatString("error.summary.state-creation");
            final String detail = localeController.formatString("error.detail.state-creation");
            final String updatedDetail = detail != null ? detail.replace("<statename>", statename) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        } catch (EntityAlreadyExistingException e) {
            logger.debug("ProcessChainController with that name already exists.", e);
            final String stateName = this.getSaveStatename(entity);
            final String summary = localeController.formatString("error.summary.entity-creation");
            final String detail = localeController.formatString("error.detail.entity-creation");
            final String updatedDetail = detail != null ? detail.replace("<name>", stateName) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        try {
            stateService.delete(this.entity);
        } catch (DeletionException e) {
            logger.debug("Error occurred during State deletion.", e);
            final String statename = this.getSaveStatename(this.entity);
            final String summary = localeController.formatString("error.summary.state-delete");
            final String detail = localeController.formatString("error.detail.state-delete");
            final String updatedDetail = detail != null ? detail.replace("<statename>", statename) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            stateService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during State update.", e);
            final String statename = this.getSaveStatename(entity);
            final String summary = localeController.formatString("error.summary.state-update");
            final String detail = localeController.formatString("error.detail.state-update");
            final String updatedDetail = detail != null ? detail.replace("<statename>", statename) : null;
            displayMessage(summary, updatedDetail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Returns if the entity of this controller is complete.
     *
     * @return if complete
     */
    public boolean isComplete(State state, Procedure procedure) {
        return stateService.isComplete(state, procedure);
    }

    /**
     * Returns if the entity of this controller is pending.
     *
     * @return if pending
     */
    public boolean isPending(State state, Procedure procedure) {
        return stateService.isPending(state, procedure);
    }

    /**
     * Resets the entity with an empty State
     */
    public void reset() {
        this.entity = new State();
    }

    /**
     * Updates the entity with a given state
     *
     * @param entity the state to be updated
     */
    public void updateEntity(State entity) {
        this.entity = entity;
        super.render("state_edit_form");
    }

    /**
     * Get the name of the given entity and check for null references while doing so.
     *
     * @param entity to get statename  of
     * @return statename if entity has one, 'null' otherwise
     */
    private String getSaveStatename(final State entity) {
        if (entity == null) return "null";
        if (entity.getName() == null) return "null";
        return entity.getName();
    }

    /**
     * Returns if the state is currently not in use and can be deleted.
     *
     * @param state the carrier
     * @return the boolean
     */
    public boolean canDelete(State state) {
        return stateService.canDelete(state);
    }
}