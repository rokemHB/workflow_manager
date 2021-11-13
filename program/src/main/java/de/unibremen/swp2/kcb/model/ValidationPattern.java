package de.unibremen.swp2.kcb.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;

/**
 * Instance of regulard expression for validation purposes
 *
 * @author Marc
 * @author Robin
 * @author Marius
 */
@Data
@Entity
@NamedQueries({
  @NamedQuery(name = "findValidationsPatternsByName", query = "SELECT v FROM ValidationPattern v WHERE v.name = :name")
})
public class ValidationPattern extends KCBEntity {

    /**
     * name of pattern
     */
    @Id
    @Expose
    private String name;

    /**
     * pattern of corresponding regular expression
     * If this is not null, all other values should be set to null or 0.
     * If the regex gets created by the other attributes, this has to be null!
     */
    @Column
    @Expose
    private String pattern;

    /**
     * indicating whether small characters should be included.
     */
    @Column
    @Expose
    private boolean smallCharacter;

    /**
     * indicating whether capital characters should be included.
     */
    @Column
    @Expose
    private boolean capitalCharacter;

    /**
     * indicating whether all numbers should be included.
     */
    @Column
    @Expose
    private boolean digits;

    /**
     * indicating whether special characters should be included.
     */
    @Column
    @Expose
    private boolean specialCharacters;

    /**
     * indicating whether dot should be included.
     */
    @Column
    @Expose
    private boolean dot;

    /**
     * indicating whether dash should be included.
     */
    @Column
    @Expose
    private boolean dash;

    /**
     * indicating whether underscore should be included.
     */
    @Column
    @Expose
    private boolean underscore;

    /**
     * indicating whether slash should be included.
     */
    @Column
    @Expose
    private boolean slash;

    /**
     * indicating whether backslash should be included.
     */
    @Column
    @Expose
    private boolean backslash;

    /**
     * indicating the minimum length of the expression.
     */
    @Column
    @Expose
    private int minLength;

    /**
     * indicating the maximum length of the expression.
     */
    @Column
    @Expose
    private int maxLength;

    /**
     * indicating whether space-character is allowed.
     */
    @Column
    @Expose
    private boolean space;

    /**
     * indicating whether the pattern is classified as standard
     * or advanced regular expression.
     * Advanced are email, password etc.
     */
    @Column
    @Expose
    private boolean advanced;

    /**
     * Equals method of validationPattern class
     * @param o Object
     * @return Equals true or false
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ValidationPattern)) return false;
        return this.getName().equals(((ValidationPattern) o).getName());
    }

    /**
     * canEqual methode for ValidationPattern
     * @param other the other object
     * @return true if object can be equal
     */
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ValidationPattern;
    }

    /**
     * hashCode method for validationPattern class
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $pattern = this.getPattern();
        result = result * PRIME + ($pattern == null ? 43 : $pattern.hashCode());
        result = result * PRIME + (this.isSmallCharacter() ? 79 : 97);
        result = result * PRIME + (this.isCapitalCharacter() ? 79 : 97);
        result = result * PRIME + (this.isDigits() ? 79 : 97);
        result = result * PRIME + (this.isSpecialCharacters() ? 79 : 97);
        result = result * PRIME + (this.isDot() ? 79 : 97);
        result = result * PRIME + (this.isDash() ? 79 : 97);
        result = result * PRIME + (this.isUnderscore() ? 79 : 97);
        result = result * PRIME + (this.isSlash() ? 79 : 97);
        result = result * PRIME + (this.isBackslash() ? 79 : 97);
        result = result * PRIME + this.getMinLength();
        result = result * PRIME + this.getMaxLength();
        result = result * PRIME + (this.isSpace() ? 79 : 97);
        result = result * PRIME + (this.isAdvanced() ? 79 : 97);
        return result;
    }

}
