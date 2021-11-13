package de.unibremen.swp2.kcb.persistence.statemachine;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for State
 *
 * @see State
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository(forEntity = State.class)
public interface StateRepository extends EntityRepository<State, String> {

    /**
     * Return State with the given name
     *
     * @param name of the state
     * @return List of state with the given name. Should only contain one State
     */
    List<State> findByName(String name);

    /**
     * Return States with given blocking state
     * A blocking state blocks the workstation while being executed
     *
     * @param blocking state of the State
     * @return List of states with the given blocking state
     */
    List<State> findByBlocking(boolean blocking);
}
