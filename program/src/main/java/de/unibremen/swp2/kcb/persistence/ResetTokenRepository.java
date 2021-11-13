package de.unibremen.swp2.kcb.persistence;

import de.unibremen.swp2.kcb.model.ResetToken;
import de.unibremen.swp2.kcb.model.User;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * Repository interface for ResetToken
 *
 * @see ResetToken
 *
 * @author Marius
 */
@Repository
public interface ResetTokenRepository extends EntityRepository<ResetToken, String> {
    /**
     * Find reset token for given user
     *
     * @param user to get reset token for
     * @return reset tokens for given user
     */
    List<ResetToken> findByUser(User user);

    /**
     * Find reset token by given token
     *
     * @param token to be looked for
     * @return reset tokens with given token
     */
    List<ResetToken> findByToken(String token);
}
