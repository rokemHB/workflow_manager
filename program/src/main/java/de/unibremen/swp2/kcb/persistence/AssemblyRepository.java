package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.model.Locations.Location;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Assembly
 *
 * @see Assembly
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface AssemblyRepository extends EntityRepository<Assembly, String> {

    /**
     * Find all Assemblies with a certain sample count
     *
     * @param sampleCount the sample count to be searched
     * @return all Assemblies with that sample count
     */
    List<Assembly> findBySampleCount(int sampleCount);

    /**
     * Find all Assemblies with a certain alloy
     *
     * @param alloy the alloy to be searched
     * @return all Assemblies with that alloy
     */
    List<Assembly> findByAlloy(String alloy);

    /**
     * Find all assemblies starting with a given assemblyID.
     *
     * @param assemblyID the assemblyId it should start with
     * @return all assemblies with the matching id
     */
    @Query("SELECT a from Assembly a where a.assemblyID LIKE :assemblyID")
    List<Assembly> findAssemblyByAssemblyID(@QueryParam("assemblyID") String assemblyID);

    /**
     * Find assembly by location list.
     *
     * @param locationID the location id
     * @return the list
     */
    @Query("SELECT a from Assembly a JOIN a.carriers c WHERE c.location = :locationID")
    List<Assembly> findAssemblyByLocation(@QueryParam("locationID") Location locationID);

    /**
     * Find assembly by carrier list.
     *
     * @param carrier the carrier
     * @return the list
     */
    @Query("SELECT a FROM Assembly a JOIN a.carriers c WHERE :carrier = c")
    List<Assembly> findAssemblyByCarrier(@QueryParam("carrier") Carrier carrier);
}
