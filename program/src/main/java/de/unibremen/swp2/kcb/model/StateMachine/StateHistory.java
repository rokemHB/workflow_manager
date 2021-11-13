package de.unibremen.swp2.kcb.model.StateMachine;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * StateHistory class for the history of state executions of a procedure.
 *
 * @see State
 * @see StateExec
 * @see StateMachine
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class StateHistory extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the actual StateHistory consists of one or many States
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private List<StateExec> stateExecs;

    /**
     * Equals method of StateHistory class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof StateHistory)) return false;
        return this.getId().equals(((StateHistory) o).getId());
    }

    /**
     * Checks whether other Object is an instance of StateHistory
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof StateHistory;
    }

    /**
     * HashCode method of StateHistory
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $stateExecs = this.getStateExecs();
        result = result * PRIME + ($stateExecs == null ? 43 : $stateExecs.hashCode());
        return result;
    }
}
