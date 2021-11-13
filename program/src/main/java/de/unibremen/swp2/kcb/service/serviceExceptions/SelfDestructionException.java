package de.unibremen.swp2.kcb.service.serviceExceptions;

/**
 * Exception class to be thrown when trying to delete the user that is invoking the deletion.
 *
 * @author Marius
 */
public class SelfDestructionException extends DeletionException {
    public SelfDestructionException(final String message) {
        super(message);
    }
}
