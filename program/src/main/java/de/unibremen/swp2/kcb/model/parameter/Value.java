package de.unibremen.swp2.kcb.model.parameter;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * A Value always belongs to a Parameter.
 * It stores the explicit value of the step.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Value extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the actual value
     */
    @Column
    @Expose
    private String value;

    /**
     * a parameter is specified by the Value
     */
    @ManyToOne
    @Expose
    private Parameter parameter;

    /**
     * Equals method of Value class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Value)) return false;
        return this.getId().equals(((Value) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Value
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Value;
    }

    /**
     * HashCode method of Value
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        final Object $parameter = this.getParameter();
        result = result * PRIME + ($parameter == null ? 43 : $parameter.hashCode());
        return result;
    }
}
