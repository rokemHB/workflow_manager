package de.unibremen.swp2.kcb.persistence.parameter;

import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Values
 *
 * @see Value
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface ValueRepository extends EntityRepository<Value, String> {
    /**
     * Return all Values specifying a parameter
     *
     * @param parameter of the returned values
     * @return All Values specifying the parameter
     */
    List<Value> findByParameter(Parameter parameter);

    /**
     * Return all Values with a given value
     *
     * @param value of the returned values
     * @return All Values with the given value
     */
    List<Value> findByValue(String value);
}
