package de.unibremen.swp2.kcb.persistence.parameter;

import de.unibremen.swp2.kcb.model.parameter.CardinalValue;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for CardinalValue
 *
 * @see CardinalValue
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface CardinalValueRepository extends EntityRepository<CardinalValue, String> {
    /**
     * Return all CardinalValues specifying a parameter
     *
     * @param parameter of the returned values
     * @return All CardinalValues specifying the parameter
     */
    List<CardinalValue> findByParameter(Parameter parameter);

    /**
     * Return all CardinalValues with a given value
     *
     * @param value of the returned values
     * @return All CardinalValues with the given value
     */
    List<CardinalValue> findByValue(String value);

    /**
     * Return all CardinalValues with a given unit
     *
     * @param unit of the returned values
     * @return All CardinalValues with the given unit
     */
    List<CardinalValue> findByUnit(String unit);
}
