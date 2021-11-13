package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.Locations.Location;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Depicts a Carrier to hold an Assembly.
 * The Carrier knows the Location where itself is present currently.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Carrier extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * Contains the ID that is physically written on the Carrier
     */
    @Column(unique = true)
    @Expose
    private String carrierID;

    /**
     * the type of the carrier
     */
    @ManyToOne
    @Expose
    private CarrierType carrierType;

    /**
     * stores where the carrier is located at
     */
    @ManyToOne
    @Expose
    private Location location;

    /**
     * Equals method for Carrier class
     * @param o the Object to compare
     * @return whether equals is true
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Carrier)) return false;
        return this.getId().equals(((Carrier) o).getId());
    }

    /**
     * Checks whether other Object is an instance of carrier
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Carrier;
    }

    /**
     * hashCode method for carrier class
     * @return
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $carrierID = this.getCarrierID();
        result = result * PRIME + ($carrierID == null ? 43 : $carrierID.hashCode());
        final Object $carrierType = this.getCarrierType();
        result = result * PRIME + ($carrierType == null ? 43 : $carrierType.hashCode());
        final Object $location = this.getLocation();
        result = result * PRIME + ($location == null ? 43 : $location.hashCode());
        return result;
    }

    /**
     * custom to string method of carrier class
     * @return the string
     */
    @Override
    public String toString() {
        return "Carrier(id=" + this.getId() + ", carrierID=" + this.getCarrierID() + ", carrierType=" + this.getCarrierType() + ", location=" + this.getLocation() + ")";
    }
}
