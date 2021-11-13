package de.unibremen.swp2.kcb.model.Locations;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Stock is an explicit Location.
 * Assemblies are stored here.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Stock extends Location {

    /**
     * unique ID
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * Equals method ot the Stock class
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Stock)) return false;
        return this.getId().equals(((Stock) o).getId());
    }

    /**
     * Returns true if two objects are of the same type
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Stock;
    }

    /**
     * Hashcode method of Stock class
     * @return hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}
