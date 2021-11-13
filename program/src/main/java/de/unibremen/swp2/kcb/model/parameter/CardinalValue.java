package de.unibremen.swp2.kcb.model.parameter;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * If a Value needs an explicit measuring unit
 * this class may store one.
 *
 * @author Marc
 * @author Marius
 */
@Data
@Entity
public class CardinalValue extends Value {

    /**
     * the explicit unit
     */
    @Column
    @Expose
    private String unit;

    /**
     * Equals method of CardinalValue class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof CardinalValue)) return false;
        return this.getId().equals(((CardinalValue) o).getId());
    }

    /**
     * Checks whether other Object is an instance of CardinalValue
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof CardinalValue;
    }

    /**
     * HashCode method of CardinalValue
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $unit = this.getUnit();
        result = result * PRIME + ($unit == null ? 43 : $unit.hashCode());
        return result;
    }

}
