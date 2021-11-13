package de.unibremen.swp2.kcb.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains every role that can be given to a user
 *
 * @author Marc
 * @author Marius
 */
public enum Role {
    PKP("PKP", "Prozesskettenplaner"),
    LOGISTIKER("LOGISTIKER", "Logistiker"),
    TECHNOLOGE("TECHNOLOGE", "Technologe"),
    ADMIN("ADMIN", "Admin"),
    TRANSPORT("TRANSPORT", "Transport");

    private final String name;
    private final String readable;

    Role(String name, String readable) {
        this.name = name;
        this.readable = readable;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Return the readable form of this role with first Letter upper - rest lowercase.
     *
     * @return 'readable' Version of this role
     */
    public String readable() {
        return this.readable;
    }

    /**
     * FromString method of Role enum
     * @param name the name
     * @return the Role
     */
    public static Role fromString(final String name) {
        switch (name) {
            case "PKP":
                return Role.PKP;
            case "LOGISTIKER":
                return Role.LOGISTIKER;
            case "TECHNOLOGE":
                return Role.TECHNOLOGE;
            case "ADMIN":
                return Role.ADMIN;
            case "TRANSPORT":
                return Role.TRANSPORT;
            default:
                return null;
        }
    }

    /**
     * Return the given role array as a Set
     *
     * @param roles to be included in the result set
     * @return set of given roles
     */
    public static Set<Role> asSet(Role... roles) {
        return Set.of(roles);
    }

    /**
     * Return a set containing all existing roles
     *
     * @return set of all existing roles
     */
    public static Set<Role> asSet() {
        return Role.asSet(Role.values());
    }

    /**
     * Return a set containing string representations of the given roles
     *
     * @param roles to be included in the result set
     * @return set of string representations of given roles
     */
    public static Set<String> asStringSet(Role... roles) {
        HashSet<String> stringSet = new HashSet<>();
        for (Role r : roles) stringSet.add(r.name());
        return stringSet;
    }

    /**
     * Return a set containing the string representations of all roles
     *
     * @return set containing string representations of all roles
     */
    public static Set<String> asStringSet() {
        return Role.asStringSet(Role.values());
    }
}
