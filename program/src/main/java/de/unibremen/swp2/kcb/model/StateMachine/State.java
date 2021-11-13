package de.unibremen.swp2.kcb.model.StateMachine;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * A State is one piece of a StateMachine.
 * A State may be blocking a workstation, but it does not
 * have to, thus it has an attribute 'blocking'.
 *
 * @see StateExec
 * @see StateHistory
 * @see StateMachine
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class State extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the State
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * if this is true, the State blocks a Workstation
     */
    @Column
    @Expose
    private boolean blocking;

    /**
     * Equals method of State class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof State)) return false;
        return this.getId().equals(((State) o).getId());
    }

    /**
     * Checks whether other Object is an instance of State
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof State;
    }

    /**
     * HashCode method of State
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
        result = result * PRIME + (this.isBlocking() ? 79 : 97);
        return result;
    }
}
