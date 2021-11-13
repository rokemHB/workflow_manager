package de.unibremen.swp2.kcb.persistence.statemachine;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateExec;
import de.unibremen.swp2.kcb.model.User;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.Date;
import java.util.List;


/**
 * Repository interface for StateExec
 *
 * @see StateExec
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Repository
public interface StateExecRepository extends EntityRepository<StateExec, String> {
    /**
     * Return all StateExecutions that are started at a given date
     *
     * @param startedAt of the returned StateExecs
     * @return List of StateExecs that were started At the given Date
     */
    List<StateExec> findByStartedAt(Date startedAt);

    /**
     * Return all StateExecutions that were finished at a given date
     *
     * @param finishedAt of the returned StateExecs
     * @return List of StateExecs that were finished at the given Date
     */
    List<StateExec> findByFinishedAt(Date finishedAt);

    /**
     * Return all StateExecutions executing a given State
     *
     * @param state to be executed
     * @return List of StateExecs executing the given State
     */
    List<StateExec> findByState(State state);

    /**
     * Return all StateExecutions that were triggered by a given user
     *
     * @param trigger of the execution
     * @return List of StateExecs that were triggered by the user
     */
    List<StateExec> findByTrigger(User trigger);
}
