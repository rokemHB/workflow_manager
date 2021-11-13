package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.GlobalConfig;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for GlobalConfig
 *
 * @see GlobalConfig
 *
 * @author Marius
 * @author Arvid
 */
@Repository
public interface GlobalConfigRepository extends EntityRepository<GlobalConfig, String> {

    /**
     * Return all Configurations with a given key
     *
     * @param key of the returned configurations
     * @return List of all configurations with the given key
     */
    List<GlobalConfig> findByKey(String key);
}
