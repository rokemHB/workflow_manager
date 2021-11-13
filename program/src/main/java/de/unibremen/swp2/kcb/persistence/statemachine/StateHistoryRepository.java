package de.unibremen.swp2.kcb.persistence.statemachine;

import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Repository interface for StateHistory
 *
 * @see StateHistory
 *
 * @author Arvid
 */
@Repository(forEntity = StateHistory.class)
public interface StateHistoryRepository extends EntityRepository<StateHistory, String> {

}