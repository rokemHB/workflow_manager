package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.parameter.Value;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Actually executes a ProcessStep. This class depicts
 * an instance of a ProcessStep.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Data
@Entity
public class Procedure extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the values applied in the procedure
     */
    @OneToMany(cascade = CascadeType.ALL)
    @Expose
    private List<Value> values;

    /**
     * the ProcessStep that shall be executed
     */
    @ManyToOne
    @Expose
    private ProcessStep processStep;

    /**
     * the StateHistory to write into the protocol
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private StateHistory stateHistory;

    /**
     * Equals method of Procedure class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Procedure)) return false;
        return this.getId().equals(((Procedure) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Procedure
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Procedure;
    }

    /**
     * HashCode method of Procedure
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $values = this.getValues();
        result = result * PRIME + ($values == null ? 43 : $values.hashCode());
        final Object $processStep = this.getProcessStep();
        result = result * PRIME + ($processStep == null ? 43 : $processStep.hashCode());
        final Object $stateHistory = this.getStateHistory();
        result = result * PRIME + ($stateHistory == null ? 43 : $stateHistory.hashCode());
        return result;
    }

    /**
     * Gets values from the model class.
     *
     * @return the values
     */
    public List<Value> getValues() {
        return this.values;
    }
}
