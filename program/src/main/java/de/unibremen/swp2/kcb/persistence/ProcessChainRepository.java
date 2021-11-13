package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.model.ProcessStep;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for ProcessChain
 *
 * @see ProcessChain
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface ProcessChainRepository extends EntityRepository<ProcessChain, String> {

    /**
     * Find a ProcessChain with a certain name
     *
     * @param name the name to be searched for
     * @return the ProcessChain with the provided name
     */
    ProcessChain findByName(String name);

    /**
     * Find all ProcessChains by its 'chain'-attribute,
     * which consists of a List of ProcessStep
     *
     * @param chain the chain to be searched for
     * @return all ProcessChains with that chain
     * @see ProcessStep
     */
    List<ProcessChain> findByChain(List<ProcessStep> chain);
}
