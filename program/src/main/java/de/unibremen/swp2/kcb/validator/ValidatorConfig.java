package de.unibremen.swp2.kcb.validator;

import de.unibremen.swp2.kcb.service.ValidationPatternService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Class that provides an interface for accessing configured validation patterns.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@SessionScoped
public class ValidatorConfig implements Serializable {

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger(ValidatorConfig.class);

    /**
     * Injected instance of ValidationPatternService to get regex patterns
     */
    @Inject
    private ValidationPatternService validationPatternService;

    /**
     * Returns compiled pattern from configuration file 'validator.ini'. If no matching pattern is found,
     * the pattern '.*' will be returned. WARNING: This pattern will match anything!
     * <p>
     * If activated is set to false in 'validator.ini' the Pattern '.*' will be returned.
     *
     * @param name of the pattern configured in the configuration file.
     * @return compiled regex pattern for entered name
     */
    public Pattern getPattern(String name) {
        String regex = this.getRegex(name);
        return Pattern.compile(regex);
    }

    /**
     * Returns a regex, configured from validationPatterns stored in database. If no matching pattern is found,
     * the String '.*' will be returned. WARNING: This will match anything!
     *
     * If activated is set to false in 'validator.ini' the String '.*' will be returned.
     *
     * @param name of the pattern configured in the database.
     * @return regex pattern for entered name
     */
    public String getRegex(String name) {
        String regex = validationPatternService.getRegEx(name);

        if (regex == null) {
            // Regex was not found. Match anything
            logger.warn("Pattern '{}' not found in database. Using default pattern. Will match anything!", name);
            regex = ".*";
        }
        return regex;
    }

}
