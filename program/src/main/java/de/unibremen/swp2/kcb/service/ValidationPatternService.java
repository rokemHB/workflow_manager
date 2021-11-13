package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.persistence.ValidationPatternRepository;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Service class for handling validationPatterns.
 * Regular Expression Strings are created by fetching data from the repository class.
 *
 * @author Robin
 * @author Arvid
 */
@Data
@Transactional
public class ValidationPatternService implements Service<ValidationPattern>{

    /**
     * Injected instance of validationPatternRepository
     */
    @Inject
    private ValidationPatternRepository validationPatternRepository;

    /**
     * Logger object of the RegExService class
     */
    private static final Logger logger = LogManager.getLogger(AssemblyService.class);

    /**
     * Create method not needed since Validation Patterns are hardcoded in the beginning
     * and can only be updated but never created.
     *
     * @param entity to be created and persisted.
     * @return nothing
     */
    @Override
    public ValidationPattern create(final ValidationPattern entity) {
        throw new UnsupportedOperationException("This method should never be called");
    }

    /**
     * Gets a ValidationPattern by its name.
     *
     * @param name the name of the validationPattern
     * @return the validationPattern
     */
    public ValidationPattern getByName(String name) {
        return validationPatternRepository.findBy(name);
    }

    /**
     * Searches a validationPattern with a given name from the database. Creates the regular expression
     * accordingly
     *
     * @param name the name of the validationPattern
     * @return the regular expression
     */
    public String getRegEx(String name) {
        ValidationPattern regEx = validationPatternRepository.findBy(name);

        if ((regEx.getPattern() == null || regEx.getPattern().equals("")) && !regEx.isAdvanced()) {
            String result = "^[";
            result += regEx.isSmallCharacter() ? "a-z" : "";
            result += regEx.isCapitalCharacter() ? "A-Z" : "";
            result += regEx.isDigits() ? "0-9" : "";
            result += regEx.isSpecialCharacters() ? "äüöÄÜÖß" : "";
            result += regEx.isDot() ? "." : "";
            result += regEx.isUnderscore() ? "_" : "";
            result += regEx.isSpace() ? " " : "";
            result += regEx.isSlash() ? "/" : "";
            result += regEx.isBackslash() ? "\\\\" : "";
            result += regEx.isDash() ? "-" : ""; // must be last part in [] brackets

            result += "]{" + regEx.getMinLength() + "," + regEx.getMaxLength() + "}$";
            return result;
        } else if (regEx.getPattern() == null && regEx.isAdvanced()) {
            return ".*"; // if pattern == null in advanced regex match all Strings
        } else {
            return regEx.getPattern(); // if a regEx pattern is set it will always be returned
        }
    }

    /**
     * Updates the provided entity.
     *
     * @param entity to be created and persisted.
     * @return RegEx that has been updated and stored.
     * @throws UpdateException thrown if validating given entity fails.
     */
    @Override
    public ValidationPattern update(ValidationPattern entity) throws UpdateException {
        //Checking, whether validationPattern is stored in database
        final ValidationPattern storedValidationPattern = validationPatternRepository.findBy(entity.getName());
        if (storedValidationPattern == null) {
            final String message = "Entity \"" + entity + "\" not found in datasource.";
            logger.debug(message);
            throw new UpdateException(message);
        }

        //Validating validationPattern
        if (entity.getMaxLength() < entity.getMinLength()) {
            logger.trace("Maximum length of validation Pattern \"{}\" is smaller than minimum length", entity);
            throw new IllegalArgumentException("Max length can't be smaller than min length");
        }

        //Saving validationPattern
        try {
            logger.trace("Attempting to save new regEx \"{}\" ...", entity);
            validationPatternRepository.save(entity);
        } catch (PersistenceException e) {
            logger.debug("Error occurred while persisting regEx \"{}\". Can't update regEx.", entity);
            throw new UpdateException("Can't update regEx: " + e.getMessage());
        }

        logger.trace("Saving of regEx \"{}\" completed without exceptions.", entity);
        logger.trace("Returning regEx \"{}\"", entity);
        return entity;
    }


    /**
     * Delete method not needed since Validation Patterns are hardcoded in the beginning
     * and can only be updated but never created or deleted.
     *
     * @param entity to be deleted
     */
    @Override
    public void delete(ValidationPattern entity) {
        throw new UnsupportedOperationException("This method should never be called");
    }

    /**
     * Returns all validationPatterns from the database
     *
     * @return List with all validationPatterns
     */
    public List<ValidationPattern> getAll() {
        return validationPatternRepository.findAll();
    }

    /**
     * Returns all validationPatterns from the database according to advanced attribute.
     *
     * @return List with validationPatterns with respective advanced attribute
     */
    public List<ValidationPattern> getByAdvanced(boolean advanced) {
        return validationPatternRepository.findByAdvanced(advanced);
    }

    /**
     * Checks whether the environmental variable KCB_PATTERN is set to true. If so, admins can change
     * validation patterns at runtime.
     * @return whether KCB_PATTERN env. variable is set to true
     */
    public boolean isEnabled() {
        final String kcbPattern = System.getenv("KCB_PATTERN");
        if (kcbPattern != null && kcbPattern.equalsIgnoreCase("true")) {
            logger.warn("KCB_PATTERN environment variable found true. Will enable editing validation patterns.");
            return true;
        } else {
            logger.warn("KCB_PATTERN environment variable not found or false. Editing validation patterns will be disabled.");
            return false;
        }
    }

}
