package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for RegEx
 *
 * @see ValidationPattern
 *
 * @author Robin
 * @author Arvid
 */
@Repository(forEntity = ValidationPattern.class)
public interface ValidationPatternRepository extends EntityRepository<ValidationPattern, String> {

    /**
     * Finds an expression by advanced type
     * @return List of validationPatterns which fit that type
     */
    List<ValidationPattern> findByAdvanced(boolean type);
}
