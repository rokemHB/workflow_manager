package de.unibremen.swp2.kcb.model.Locations;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

/**
 * Transport describes the state an Assembly
 * is in while it is carried somewhere.
 * A Transport knows its transporter.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Transport extends Location {

    /**
     * unique ID
     */
    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * the person doing the transport
     */
    @OneToOne
    @Expose
    private User transporter;

    /**
     * Equals method of Transport class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Transport)) return false;
        return this.getId().equals(((Transport) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Transport
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Transport;
    }

    /**
     * HashCode method of Transport
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $transporter = this.getTransporter();
        result = result * PRIME + ($transporter == null ? 43 : $transporter.hashCode());
        return result;
    }
}
