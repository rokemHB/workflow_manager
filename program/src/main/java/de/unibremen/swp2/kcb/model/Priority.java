package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * It may be possible that a job has a higher priority
 * than another. This class saves the priority of a Job.
 *
 * @author Marc
 * @author Marius
 */
@Data
@Entity
public class Priority extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the Priority
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * the value of the priority. Importent for containing the order of priorities
     */
    @Column
    @Expose
    private int value;

    /**
     * Equals method of Priority class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Priority)) return false;
        return this.getId().equals(((Priority) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Priority
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Priority;
    }

    /**
     * HashCode method of Priority
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + this.getValue();
        return result;
    }
}
