package de.unibremen.swp2.kcb.service.serviceExceptions;

/**
 * Class PasswordsNotMatchingException
 *
 * @author Marius
 */
public class PasswordsNotMatchingException extends PasswordChangeException {
    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
