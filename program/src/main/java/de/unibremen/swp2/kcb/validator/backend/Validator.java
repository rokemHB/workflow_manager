package de.unibremen.swp2.kcb.validator.backend;

/**
 * Interface of all Classes used for backend validation.
 *
 * @param <T> Object to be validated
 *
 * @author Marius
 */
public interface Validator<T> {
    /**
     * Validates an instance of T.
     *
     * @param instance to be validated
     * @return true if instance is valid, false if instance is invalid.
     * @throws ValidationException if an error occurred during validation
     */
    boolean validate(T instance) throws ValidationException;
}
