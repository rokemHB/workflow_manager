package de.unibremen.swp2.kcb.model.StateMachine;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * A StateMachine is made of several (or just one) States.
 * Therefore it saves a Collection of those States
 * in the attribute 'stateList'.
 *
 * @see State
 * @see StateExec
 * @see StateHistory
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class StateMachine extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the StateMachine
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * the actual StateMachine consists of one or more States
     */
    @ManyToMany
    @JoinColumn(name = "statemachine_id")
    @Expose
    private List<State> stateList;

    /**
     * Equals method of StateMachine class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof StateMachine)) return false;
        return this.getId().equals(((StateMachine) o).getId());
    }

    /**
     * Checks whether other Object is an instance of StateMachine
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof StateMachine;
    }

    /**
     * HashCode method of StateMachine
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
        final Object $stateList = this.getStateList();
        result = result * PRIME + ($stateList == null ? 43 : $stateList.hashCode());
        return result;
    }
}
