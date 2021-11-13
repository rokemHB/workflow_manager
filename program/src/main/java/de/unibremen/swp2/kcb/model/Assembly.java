package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

/**
 * This class represents one or more samples
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
@Data
@Entity
public class Assembly extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * The range of ID's the sample(s) reserve
     */
    @Column
    @Expose
    private String assemblyID;

    /**
     * The amount of samples an Assembly represents
     */
    @Min(1)
    @Expose
    private int sampleCount;

    /**
     * Stores the alloy of an assembly
     */
    @Column
    @Expose
    private String alloy;

    /**
     * Modifications applied to the assembly
     */
    @ManyToMany
    @Expose
    private List<Procedure> modifications;

    /**
     * Position on the carrier where the assembly is located
     */
    @Column
    @Expose
    private String positionAtCarrier;

    /**
     * Stores a user-written comment for an assembly
     */
    @Column( columnDefinition = "TEXT")
    @Expose
    private String comment;

    /**
     * The carrier(s) the Assembl(ie/y)s is/are located on
     */
    @ManyToMany
    @Expose
    private List<Carrier> carriers;

    /**
     * ToString() of Assembly class.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "ID: " + assemblyID;
    }

    /**
     * Equals method of Assembly model class
     * @param o object to check
     * @return whether its equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Assembly)) return false;
        return this.getId().equals(((Assembly) o).getId());
    }

    /**
     * Checks whether an object is instance of an Assembly
     * @param other object
     * @return whether it is an instance
     */
    protected boolean canEqual(final Object other) {
        return other instanceof Assembly;
    }

    /**
     * Hashcode method of assembly class
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $assemblyID = this.getAssemblyID();
        result = result * PRIME + ($assemblyID == null ? 43 : $assemblyID.hashCode());
        result = result * PRIME + this.getSampleCount();
        final Object $alloy = this.getAlloy();
        result = result * PRIME + ($alloy == null ? 43 : $alloy.hashCode());
        final Object $modifications = this.getModifications();
        result = result * PRIME + ($modifications == null ? 43 : $modifications.hashCode());
        final Object $positionAtCarrier = this.getPositionAtCarrier();
        result = result * PRIME + ($positionAtCarrier == null ? 43 : $positionAtCarrier.hashCode());
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        final Object $carriers = this.getCarriers();
        result = result * PRIME + ($carriers == null ? 43 : $carriers.hashCode());
        return result;
    }
}
