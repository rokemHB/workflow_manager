package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;

/**
 * Contains all the ProcessSteps that form a whole ProcessChain
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class ProcessChain extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the ProcessChain
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * the actual ProcessChain consist of one or more ProcessSteps
     */
    @ManyToMany
    @Expose
    private List<ProcessStep> chain;

    /**
     * Equals method of ProcessChain class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ProcessChain)) return false;
        return this.getId().equals(((ProcessChain) o).getId());
    }

    /**
     * Checks whether other Object is an instance of ProcessChain
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ProcessChain;
    }

    /**
     * HashCode method of ProcessChain
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
        final Object $chain = this.getChain();
        result = result * PRIME + ($chain == null ? 43 : $chain.hashCode());
        return result;
    }
}
