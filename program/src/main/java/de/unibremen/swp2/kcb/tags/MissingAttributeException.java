package de.unibremen.swp2.kcb.tags;

/**
 * Exception class that is thrown if a required attribute of a tag is missing
 *
 * @author Marius
 */
public class MissingAttributeException extends RuntimeException {

    /**
     * Create new MissingAttributeException
     *
     * @param message of exception
     */
    public MissingAttributeException(final String message) {
        super(message);
    }

}
