package de.unibremen.swp2.kcb.service.serviceExceptions;

/**
 * Class ServiceException
 *
 * @author Marius
 */
public abstract class ServiceException extends Exception {
    public ServiceException(final String message) {
        super(message);
    }
}
