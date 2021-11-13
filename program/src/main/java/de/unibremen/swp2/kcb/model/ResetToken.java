package de.unibremen.swp2.kcb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

/**
 * ResetToken class for password reset by email
 *
 * @author Robin
 * @author Marius
 */
@Entity
public class ResetToken {

    /**
     * How long do token take to expire
     */
    public static final int EXPIRATION_PERIOD_MINUTES = 15;

    /**
     * Standard ID for reset tokens
     */
    @Id
    @Getter
    private String id = UUID.randomUUID().toString();

    /**
     * the token
     */
    @Column
    @Getter
    @Setter
    private String token;

    /**
     * Time object for expiration date
     */
    @Column
    @Getter
    @Setter
    private LocalTime expirationDate;

    /**
     * User object for reset token
     */
    @JoinColumn
    @ManyToOne
    @Getter
    @Setter
    private User user;


    /**
     * Update the ResetToken expirationDate
     */
    public void updateExpirationDate() {
        this.expirationDate = LocalTime.now().plusMinutes(EXPIRATION_PERIOD_MINUTES);
    }

    /**
     * Update the ResetToken expirationDate. Expires in given amount of minutes.
     *
     * @param minuteOffset minutes it takes for the token to expire
     */
    public void updateExpirationDate(final int minuteOffset) {
        this.expirationDate = LocalTime.now().plusMinutes(minuteOffset);
    }

    /**
     * Generate a random token
     *
     * @return String representation of the randomly generated token
     */
    public static String generateToken() {
        return String.valueOf(UUID.randomUUID());
    }
}
