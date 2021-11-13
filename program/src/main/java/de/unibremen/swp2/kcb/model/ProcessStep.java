package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * This class depicts one step in a ProcessChain
 * It saves the estimated Duration of itself,
 * a StateMachine, the carrier type used and on
 * which workstation it is processed.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class ProcessStep extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * name of this ProcessStep
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * N
     * the estimated Duration of this ProcessStep
     */
    @Column
    @Expose
    private int estDuration;

    /**
     * the dynamic StateMachine
     */
    @ManyToOne
    @Expose
    private StateMachine stateMachine;

    /**
     * the preparation used for this ProcessStep
     */
    @ManyToOne
    @Expose
    private CarrierType preparation;

    /**
     * the output CarrierType used for this ProcessStep
     */
    @ManyToOne
    @Expose
    private CarrierType output;

    /**
     * the Workstation this ProcessStep is executed
     */
    @ManyToOne
    @Expose
    private Workstation workstation;

    /**
     * parameters that modify this ProcessStep
     */
    @JoinColumn
    @ManyToMany
    @Expose
    private List<Parameter> parameters;

    /**
     * If the processStep modifies assemblies.
     */
    @Column
    @Expose
    private boolean modifies;

    /**
     * If the processStep creates assemblies.
     */
    @Column
    @Expose
    private boolean creates;

    /**
     * If the processStep deletes assemblies.
     */
    @Column
    @Expose
    private boolean deletes;


    /**
     * Checks whether other Object is an instance of ProcessStep
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ProcessStep;
    }

    /**
     * Equals method of ProcessStep class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ProcessStep)) return false;
        return this.getId().equals(((ProcessStep) o).getId());
    }

    /**
     * HashCode method of ProcessStep
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
        result = result * PRIME + this.getEstDuration();
        final Object $stateMachine = this.getStateMachine();
        result = result * PRIME + ($stateMachine == null ? 43 : $stateMachine.hashCode());
        final Object $preparation = this.getPreparation();
        result = result * PRIME + ($preparation == null ? 43 : $preparation.hashCode());
        final Object $output = this.getOutput();
        result = result * PRIME + ($output == null ? 43 : $output.hashCode());
        final Object $workstation = this.getWorkstation();
        result = result * PRIME + ($workstation == null ? 43 : $workstation.hashCode());
        final Object $parameters = this.getParameters();
        result = result * PRIME + ($parameters == null ? 43 : $parameters.hashCode());
        result = result * PRIME + (this.isModifies() ? 79 : 97);
        result = result * PRIME + (this.isCreates() ? 79 : 97);
        result = result * PRIME + (this.isDeletes() ? 79 : 97);
        return result;
    }
}
