package de.unibremen.swp2.kcb.model.Locations;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Roughly describes a Location.
 * This class contains the actual String that marks the 'position'.
 *
 * @author Marc
 * @author Marius
 */
@Data
@Entity
public abstract class Location extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    private String id = UUID.randomUUID().toString();


    /**
     * the concrete position of the Location
     */
    @Column
    @Expose
    private String position;

    /**
     * Equals method of Location class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Location)) return false;
        return this.getId().equals(((Location) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Location
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Location;
    }

    /**
     * HashCode method of Location
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $position = this.getPosition();
        result = result * PRIME + ($position == null ? 43 : $position.hashCode());
        return result;
    }
}
