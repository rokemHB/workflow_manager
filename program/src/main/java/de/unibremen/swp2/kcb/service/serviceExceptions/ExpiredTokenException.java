package de.unibremen.swp2.kcb.service.serviceExceptions;

/**
 * Class ExpiredTokenException
 *
 * @author Marius
 */
public class ExpiredTokenException extends InvalidTokenException {
    public ExpiredTokenException(final String message) {
        super(message);
    }
}
