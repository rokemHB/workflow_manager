package de.unibremen.swp2.kcb.persistence.locations;

import de.unibremen.swp2.kcb.model.Locations.Stock;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for Stock
 *
 * @see Stock
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Repository
public interface StockRepository extends EntityRepository<Stock, String> {
    /**
     * Find Stock by position
     *
     * @param position of the stock
     * @return All Stocks at given position
     */
    List<Stock> findByLocation(String position);
}
