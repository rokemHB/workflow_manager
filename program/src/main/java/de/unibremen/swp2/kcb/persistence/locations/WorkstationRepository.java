package de.unibremen.swp2.kcb.persistence.locations;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Workstation
 *
 * @see Assembly
 *
 * @author Marc
 * @author Marius
 */
@Repository
public interface WorkstationRepository extends EntityRepository<Workstation, String> {

    /**
     * Find Workstations by position
     *
     * @param position of the workstation
     * @return All Workstations at given position
     */
    List<Workstation> findByPosition(String position);

    /**
     * Find Workstations by name
     *
     * @param name of the workstation
     * @return All workstations with given name
     */
    List<Workstation> findByName(String name);

    /**
     * Find active/inactive Workstations
     *
     * @param active state of the workstation
     * @return All Workstations with the given state
     */
    List<Workstation> findByActive(boolean active);
}
