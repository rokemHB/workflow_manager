package de.unibremen.swp2.kcb.persistence.parameter;

import de.unibremen.swp2.kcb.model.parameter.Parameter;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;


/**
 * Repository interface for Parameter
 *
 * @see Parameter
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface ParameterRepository extends EntityRepository<Parameter, String> {
    /**
     * Return all Parameters with a given field
     *
     * @param field of the returned parameters
     * @return List of all Parameters with the given field
     */
    List<Parameter> findByField(String field);
}
