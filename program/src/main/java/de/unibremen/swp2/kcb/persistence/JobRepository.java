package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Job
 *
 * @see Job
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Repository
public interface JobRepository extends EntityRepository<Job, String> {

    /**
     * Find all Jobs with a certain name
     *
     * @param name the name to be searched for
     * @return all Jobs with that name
     */
    List<Job> findByName(String name);

    /**
     * Find all Jobs with a certain ProcessChain
     *
     * @param processChain the ProcessChain to be searched for
     * @return all Jobs with that ProcessChain
     * @see ProcessChain
     */
    List<Job> findByProcessChain(ProcessChain processChain);

    /**
     * Find all Jobs with a certain Priority
     *
     * @param priority the Priority to be searched for
     * @return all Jobs with that Priority
     * @see Priority
     */
    List<Job> findByPriority(Priority priority);

    /**
     * Find all Jobs with a certain Procedures
     *
     * @param procedures the Procedures to be searched for
     * @return all Jobs with that Procedures
     * @see Procedure
     */
    List<Job> findByProcedures(List<Procedure> procedures);

    /**
     * Finds a Job with a certain Procedure
     *
     * @param procedure the Procedure to be searched for
     * @return the Job with that Procedure
     */
    @Query("SELECT j FROM Job j JOIN j.procedures p WHERE :procedure IN p")
    List<Job> findByProcedure(@QueryParam("procedure") Procedure procedure);

    /**
     * Find all Jobs with a certain Procedures
     *
     * @param assembly the Assembly to be searched for
     * @return all Jobs with that Assembly
     * @see Assembly
     */
    @Query("SELECT j FROM Job j JOIN j.assemblies a WHERE :assembly IN a")
    List<Job> findByAssembly(@QueryParam("assembly") Assembly assembly);

    /**
     * Find active jobs list.
     *
     * @return the list
     */
    @Query("SELECT DISTINCT j FROM Job j JOIN j.assemblies a JOIN a.carriers c JOIN c.location l WHERE j.jobState = 1")
    List<Job> findActiveJobs();

    /**
     * Find currently running by workstation list.
     *
     * @param workstation the workstation
     * @return the list
     */
    @Query("SELECT j FROM Job j JOIN j.procedures p JOIN p.processStep ps JOIN ps.workstation ws JOIN p.stateHistory sh JOIN sh.stateExecs se WHERE :workstation = ws AND sh.stateExecs IS NOT EMPTY AND se.finishedAt IS NULL AND j.jobState = 1")
    List<Job> findCurrentlyRunningByWorkstation(@QueryParam("workstation") Workstation workstation);

    /**
     * Find all order by priority value desc list.
     *
     * @return the list
     */
    @Query("SELECT j FROM Job j JOIN j.priority p ORDER BY p.value DESC")
    List<Job> findAllOrderByPriorityValueDesc();

    /**
     * Find all Jobs with the given JobState.
     *
     * @param jobState the JobState
     * @return all Jobs with that JobState
     */
    List<Job> findByJobState(JobState jobState);
}
