package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.model.Locations.Location;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Carrier
 *
 * @see Carrier
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface CarrierRepository extends EntityRepository<Carrier, String> {

    /**
     * Find all Carriers with a certain CarrierType
     *
     * @param carrierType the CarrierType to be searched for
     * @return all Carriers with that CarrierType
     * @see CarrierType
     */
    List<Carrier> findByCarrierType(CarrierType carrierType);

    /**
     * Find all Carriers at a certain Location
     *
     * @param location the Location to be searched at
     * @return all Carriers at that Location
     * @see Location
     */
    List<Carrier> findByLocation(Location location);

    /**
     * Find carrier by carrier id.
     *
     * @param carrierID the carrier id
     * @return the carrier
     */
    Carrier findByCarrierID(String carrierID);

    /**
     * Find all Carriers with a certain CarrierType at certain Location
     *
     * @param carrierType the CarrierType to be searched for
     * @param location    the Location to be searched at
     * @return all Carriers with that CarrierType at that Location
     */
    List<Carrier> findByCarrierTypeAndLocation(CarrierType carrierType, Location location);

    /**
     * Find unused carriers list.
     *
     * @return the list
     */
    @Query("SELECT c FROM Carrier c WHERE c NOT IN (SELECT c FROM Assembly a JOIN a.carriers c)")
    List<Carrier> findUnusedCarriers();
}
