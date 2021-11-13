package de.unibremen.swp2.kcb.model.parameter;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * This class depicts a Parameter that belongs to
 * a ProcessStep.
 *
 * @author Marc
 * @author Robin
 * @author Marius
 */
@Data
@Entity
public class Parameter extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the Parameter
     */
    @Column (unique = true)
    @Expose
    private String field;

    /**
     * Equals method of Parameter class
     * @param o object to be tested
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Parameter)) return false;
        return this.getId().equals(((Parameter) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Parameter
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Parameter;
    }

    /**
     * HashCode method of Parameter
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $field = this.getField();
        result = result * PRIME + ($field == null ? 43 : $field.hashCode());
        return result;
    }
}
