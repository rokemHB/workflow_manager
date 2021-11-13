package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.CarrierType;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Repository interface for CarrierType
 *
 * @see CarrierType
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface CarrierTypeRepository extends EntityRepository<CarrierType, String> {

    /**
     * Find all CarrierTypes with a certain name
     *
     * @param name the name to be searched for
     * @return all CarrierTypes with that name
     */
    CarrierType findByName(String name);
}
