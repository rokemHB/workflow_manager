package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.Priority;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Priority
 *
 * @see Priority
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface PriorityRepository extends EntityRepository<Priority, String> {

    /**
     * Finds a Priorities with a certain name
     *
     * @param name the name to be searched for
     * @return Priority with that name
     */
    Priority findByName(String name);

    /**
     * Find all Priorities with a certain Value
     *
     * @param value priority value
     * @return all Priorities with that Value
     */
    List<Priority> findByValue(int value);

    /**
     * Find all Priorities ordered by value
     *
     * @return all Priorities ordered by value
     */
    List<Priority> findAllOrderByValueAsc();
}
