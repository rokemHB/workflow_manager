package de.unibremen.swp2.kcb.validator.backend;

import de.unibremen.swp2.kcb.validator.ValidatorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * Superclass of all backend validators using pattern validation, configured in
 * Validator Config.
 *
 * @param <T> Object to be validated
 * @see de.unibremen.swp2.kcb.validator.ValidatorConfig
 *
 * @author Marius
 */
public abstract class PatternValidator<T> implements Validator<T> {

    /**
     * Logger object of the PatternValidator class
     */
    private static final Logger logger = LogManager.getLogger(PatternValidator.class);

    /**
     * Injected ValidatorConfig to access configured patterns
     */
    @Inject
    protected ValidatorConfig validatorConfig;

    /**
     * Validates an instance of T using pattern validation.
     *
     * @param instance to be validated
     * @return true if instance is valid, false if instance is invalid.
     */
    @Override
    public abstract boolean validate(T instance) throws ValidationException;

    /**
     * Checks if a given value matches the given pattern.
     *
     * @param pattern to be checked
     * @param value   to be validated against the given pattern
     * @return does the value match the pattern?
     */
    protected boolean checkMatch(final Pattern pattern, final String value) {
        if (!pattern.matcher(value).matches()) {
            logger.debug("Validation failed for value: {} and pattern: {}", value, pattern.pattern());
            return false;
        }
        return true;
    }
}
