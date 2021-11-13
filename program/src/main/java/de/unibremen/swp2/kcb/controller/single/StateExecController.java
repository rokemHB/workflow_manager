package de.unibremen.swp2.kcb.controller.single;

import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Controller class to handle single {@link StateExec}.
 *
 * @author Arvid
 * @author Robin
 */
@ViewScoped
@Named
public class StateExecController extends SingleController<StateExec> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(StateExecController.class);

    /**
     * Creates a new entity.
     */
    @Override
    public void create() {
        throw new UnsupportedOperationException("this should never be called");
    }

    /**
     * Deletes an existing entity.
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException("this should never be called");
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        throw new UnsupportedOperationException("this should never be called");
    }

    /**
     * Reset.
     */
    public void reset() {
        this.entity = new StateExec();
    }

    /**
     * Update entity.
     *
     * @param entity the entity
     */
    public void updateEntity(StateExec entity) {
        logger.debug("Entity updated");
        this.entity = entity;
    }

    /**
     * Get the id of the given entity and check for null references while doing so.
     *
     * @param entity to get id of
     * @return id if entity has one, 'null' otherwise
     */
    private String getSaveId(final StateExec entity) {
        if (entity == null) return "null";
        if (entity.getId() == null) return "null";
        return entity.getId();
    }
}