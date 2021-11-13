package de.unibremen.swp2.kcb.persistence.statemachine;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for StateMachine
 *
 * @see StateMachine
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository(forEntity = StateMachine.class)
public interface StateMachineRepository extends EntityRepository<StateMachine, String> {

    /**
     * Return StateMachine with the given name
     *
     * @param name of the returned StateMachine
     * @return List of all StateMachines with the given name. Should contain only one StateMachine.
     */
    List<StateMachine> findByName(String name);

    @Query("SELECT sm FROM StateMachine sm JOIN sm.stateList s WHERE :state IN s")
    List<StateMachine> findByState(@QueryParam("state") State state);
}
