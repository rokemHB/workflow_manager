package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This class contains the attributes for the 'User' Entity
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Data
@Entity
@NamedQuery(name = "findUsersByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
@NamedQuery(name = "findUsersByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
public class User extends KCBEntity {

    /**
     * unique ID
     */
    @Id
    @Expose
    private String id = UUID.randomUUID().toString();

    /**
     * the unique username
     */
    @Expose
    @Column(unique = true)
    private String username;

    /**
     * the password needed to login
     */
    @NotNull
    @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein!")
    @Column(columnDefinition = "VARCHAR(100)")
    @Expose(serialize = false, deserialize = true)
    private String password;

    /**
     * the user's first name
     */
    @NotNull
    @Column
    @Expose
    private String firstName;

    /**
     * the user's last name
     */
    @NotNull
    @Column
    @Expose
    private String lastName;

    /**
     * the user's email address
     */
    @NotNull
    @Email
    @Column(unique = true)
    @Expose
    private String email;

    /**
     * the roles of the user
     */
    @Column
    @ElementCollection
    @Expose
    private Set<String> roles;

    /**
     * the selected locale of the given user
     */
    @Column
    private String preferredLocale;

    /**
     * Stores for every user whether the side menu is pinned open or not
     */
    @Column
    private boolean pinned;


    /**
     * Stores if the users is using dark mode
     */
    @Column
    private boolean darkMode;

    /**
     * Default Constructor
     */
    public User() {
        this.roles = new HashSet<>();

        //Initialize NotNull-annotated fields so they are never Null
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
    }

    /**
     * Return all roles of the given user
     *
     * @return Set of all roles
     */
    public Set<Role> getRoles() {
        if (this.roles == null)
            return null;
        HashSet<Role> r = new HashSet<>();
        for (String role : roles) {
            r.add(Role.fromString(role));
        }
        return r;
    }

    /**
     * Set user roles
     */
    public void setRoles(final Set<String> roles) {
        this.roles = roles;
    }

    /**
     * Set user roles
     */
    public void setRolesFromRoles(final Set<Role> roles) {
        this.roles = new HashSet<>();
        for (Role role : roles) {
            this.roles.add(role.toString());
        }
    }

    /**
     * Equals method of User class
     * @param o object to be testet
     * @return whether other object is equal
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof User)) return false;
        return this.getId().equals(((User) o).getId());
    }

    /**
     * Checks whether other Object is an instance of User
     * @param other object
     * @return whether in can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    /**
     * HashCode method of User
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $roles = this.getRoles();
        result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
        final Object $preferredLocale = this.getPreferredLocale();
        result = result * PRIME + ($preferredLocale == null ? 43 : $preferredLocale.hashCode());
        result = result * PRIME + (this.isPinned() ? 79 : 97);
        return result;
    }
}
