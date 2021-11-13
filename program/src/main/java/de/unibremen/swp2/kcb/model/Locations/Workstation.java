package de.unibremen.swp2.kcb.model.Locations;

import com.google.gson.annotations.Expose;
import de.unibremen.swp2.kcb.model.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;

/**
 * An explicit Location.
 * Workstations execute a ProcessStep.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Data
@Entity
public class Workstation extends Location {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the name of the Workstation
     */
    @Column(unique = true)
    @Expose
    private String name;

    /**
     * true if the Workstation is broken
     */
    @Column
    @Expose
    private Boolean broken;

    /**
     * indicates whether Workstation is
     */
    @Column
    @Expose
    private Boolean active;

    /**
     * the person using the Workstation
     */
    @ManyToMany
    @Expose
    private List<User> users;

    /**
     * Equals method of Workstation class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Workstation)) return false;
        return this.getId().equals(((Workstation) o).getId());
    }

    /**
     * Checks whether other Object is an instance of Workstation
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Workstation;
    }

    /**
     * HashCode method of Workstation
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
        final Object $broken = this.getBroken();
        result = result * PRIME + ($broken == null ? 43 : $broken.hashCode());
        final Object $active = this.getActive();
        result = result * PRIME + ($active == null ? 43 : $active.hashCode());
        final Object $users = this.getUsers();
        result = result * PRIME + ($users == null ? 43 : $users.hashCode());
        return result;
    }
}
