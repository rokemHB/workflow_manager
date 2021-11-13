package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.Job;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Procedure
 *
 * @see Procedure
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface ProcedureRepository extends EntityRepository<Procedure, String> {

    /**
     * Find all Procedures with a certain Value
     *
     * @param value the Value to be searched for
     * @return all Procedures with that Value
     * @see Value
     */
    List<Procedure> findByValue(Value value);

    /**
     * Find all Procedures with a certain ProcessStep
     *
     * @param processStep the ProcessStep to be searched for
     * @return all Procedures with that ProcessStep
     * @see ProcessStep
     */
    List<Procedure> findByProcessStep(ProcessStep processStep);

    /**
     * Find by State.
     *
     * @param state the State to be searched for
     * @return all Procedures in that State
     */
    List<Procedure> findByState(State state);

    /**
     * Find all Procedures with a certain StateHistory
     *
     * @param stateHistory the StateHistory to be searched for
     * @return all Procedures with that StateHistory
     * @see StateHistory
     */
    List<Procedure> findByStateHistory(StateHistory stateHistory);


    /**
     * Find by Workstation.
     *
     * @param workstation the Workstation to be searched for
     * @return all Procedures at that Workstation
     */
    @Query("SELECT p FROM Procedure p JOIN p.processStep ps JOIN ps.workstation ws WHERE :workstation = ws")
    List<Procedure> findByWorkstation(@QueryParam("workstation") Workstation workstation);

    /**
     * Find active by workstation list.
     *
     * @param workstation the workstation
     * @return the list
     */
    @Query("SELECT p FROM Job j JOIN j.procedures p JOIN p.processStep ps JOIN ps.workstation ws JOIN p.stateHistory sh JOIN sh.stateExecs se WHERE :workstation = ws AND sh.stateExecs IS NOT EMPTY AND se.finishedAt IS NULL AND j.jobState = 1")
    List<Procedure> findActiveByWorkstation(@QueryParam("workstation") Workstation workstation);

    /**
     * Find active procedure list.
     *
     * @param job the job
     * @return the list
     */
    @Query("SELECT DISTINCT p FROM Job j JOIN j.procedures p JOIN p.stateHistory sh JOIN sh.stateExecs se WHERE j = :job AND sh.stateExecs IS NOT EMPTY AND se.finishedAt IS NULL")
    List<Procedure> findActiveProcedure(@QueryParam("job") Job job);

    /**
     * Find all Procedures with one of the given ids each.
     *
     * @param ids to get steps for
     * @return List of all Procedures with the given ids
     */
    List<Procedure> findByIds(List<String> ids);
}
