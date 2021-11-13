package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * This class depicts the type of a specific carrier.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class CarrierType extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the CarrierType
     *
     * @see Carrier
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * Equals method of CarrierType class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof CarrierType)) return false;
        return this.getId().equals(((CarrierType) o).getId());
    }

    /**
     * Checks whether other Object is an instance of CarrierType
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof CarrierType;
    }

    /**
     * HashCode method of CarrierType
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }
}
