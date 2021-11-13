package de.unibremen.swp2.kcb.persistence.locations;

import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.User;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Transport
 *
 * @see Transport
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface TransportRepository extends EntityRepository<Transport, String> {
    /**
     * Find Transport for given {@link User}
     *
     * @param transporter the User performing the transport
     * @return Transport for given user
     */
    List<Transport> findByTransporter(User transporter);
}
