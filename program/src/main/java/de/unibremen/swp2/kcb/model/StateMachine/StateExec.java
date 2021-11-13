package de.unibremen.swp2.kcb.model.StateMachine;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.KCBEntity;
import de.unibremen.swp2.kcb.model.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * StateExec is an association class between
 * State and StateHistory. It is needed to save
 * the start- and end-time.
 *
 * @see State
 * @see StateHistory
 * @see StateMachine
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Data
@Entity
public class StateExec extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the User who triggered the State
     */
    @OneToOne
    private User trigger;

    /**
     * marks when the StateExec started
     */
    @Column
    @Expose
    private LocalDateTime startedAt;

    /**
     * marks when the StateExec ended
     */
    @Column
    @Expose
    private LocalDateTime finishedAt;

    /**
     * marks when the StateExec got created. Allows to calculate transition time.
     */
    @Column
    @Expose
    private LocalDateTime transitionAt;

    /**
     * the transition time in minutes.
     */
    @Column
    @Expose
    private int transitionTime;

    /**
     * the State that gets executed
     */
    @ManyToOne
    @Expose
    private State state;

    /**
     * Equals method of StateExec class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof StateExec)) return false;
        return this.getId().equals(((StateExec) o).getId());
    }

    /**
     * Checks whether other Object is an instance of StateExec
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof StateExec;
    }

    /**
     * HashCode method of StateExec
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $trigger = this.getTrigger();
        result = result * PRIME + ($trigger == null ? 43 : $trigger.hashCode());
        final Object $startedAt = this.getStartedAt();
        result = result * PRIME + ($startedAt == null ? 43 : $startedAt.hashCode());
        final Object $finishedAt = this.getFinishedAt();
        result = result * PRIME + ($finishedAt == null ? 43 : $finishedAt.hashCode());
        final Object $state = this.getState();
        result = result * PRIME + ($state == null ? 43 : $state.hashCode());
        return result;
    }
}
