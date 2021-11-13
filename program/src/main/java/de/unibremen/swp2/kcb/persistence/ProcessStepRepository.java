package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for ProcessStep
 *
 * @see ProcessStep
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Repository
public interface ProcessStepRepository extends EntityRepository<ProcessStep, String> {

    /**
     * Find all ProcessSteps with a certain name
     *
     * @param name the name to be searched
     * @return all ProcessSteps with that name
     */
    List<ProcessStep> findByName(String name);

    /**
     * Find all ProcessSteps with a certain estimated Duration
     *
     * @param estDuration the estimated Duration to be searched
     * @return all ProcessSteps with that estimated Duration
     */
    List<ProcessStep> findByEstDuration(int estDuration);

    /**
     * Find all ProcessSteps with a certain StateMachine
     *
     * @param stateMachine the StateMachine to be searched
     * @return all ProcessSteps with that StateMachine
     * @see StateMachine
     */
    List<ProcessStep> findByStateMachine(StateMachine stateMachine);

    /**
     * Find all ProcessSteps with a certain output-CarrierType
     *
     * @param output the CarrierType to be searched
     * @return all ProcessSteps with that CarrierType
     * @see CarrierType
     */
    List<ProcessStep> findByOutputCarrierType(CarrierType output);

    /**
     * Find all ProcessSteps with a certain CarrierType
     *
     * @param preparation the CarrierType to be searched
     * @return all ProcessSteps with that CarrierType
     * @see CarrierType
     */
    List<ProcessStep> findByPreparationCarrierType(CarrierType preparation);

    /**
     * Find all ProcessSteps with a certain Workstation
     *
     * @param workstation the Workstation to be searched
     * @return all ProcessSteps with that Workstation
     * @see Workstation
     */
    List<ProcessStep> findByWorkstation(Workstation workstation);

    /**
     * Find all ProcessSteps with a certain Parameter
     *
     * @param parameter the Parameter to be searched
     * @return all ProcessSteps with that Parameter
     * @see Parameter
     */
    @Query("SELECT ps FROM ProcessStep ps JOIN ps.parameters p WHERE :parameter = p")
    List<ProcessStep> findByParameter(@QueryParam("parameter") Parameter parameter);
}
