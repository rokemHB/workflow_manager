package de.unibremen.swp2.kcb.controller.single;


import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.service.ValidationPatternService;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Controller class to handle {@link ValidationPattern}.
 *
 * @author Robin
 */
@ViewScoped
@Named
public class ValidationPatternController extends SingleController<ValidationPattern> {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * ValidationPatternService to handle actions
     */
    @Inject
    private ValidationPatternService validationPatternService;

    /**
     * Gets a ValidationPattern by its name.
     *
     * @param name the name of the validationPattern
     * @return the validationPattern
     */
    public ValidationPattern getByName(String name) {
        return validationPatternService.getByName(name);
    }

    /**
     * Method does not apply for this specific class
     */
    @Override
    public void create() {
        throw new UnsupportedOperationException("This method should never be called");
    }

    /**
     * Method does not apply for this specific class
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException("This method should never be called");
    }

    /**
     * Updates an existing entity with the information of a new entity.
     */
    @Override
    public void update() {
        try {
            this.entity = validationPatternService.update(this.entity);
        } catch (UpdateException e) {
            logger.debug("Error occurred during regEx update.", e);
        } catch (IllegalArgumentException e) {
            String summary = localeController.formatString("error.summary.validation-pattern-illegal-entry");
            String detail = localeController.formatString("error.detail.validation-pattern-illegal-entry");
            displayMessage(summary, detail, FacesMessage.SEVERITY_ERROR);
        }
    }

    /**
     * Updates the entity according to the stated edit form
     * @param entity to be updated
     */
    public void updateEntity(ValidationPattern entity) {
        this.entity = entity;
        super.render("validationpattern_edit_form");
        super.render("tabViewTest");
    }

    /**
     * Resets this entitiy
     */
    public void reset() {
        this.entity = new ValidationPattern();
    }

    /**
     * Checks whether the environmental variable KCB_PATTERN is set to true. If so, admins can change
     * validation patterns at runtime.
     * @return whether KCB_PATTERN env. variable is set to true
     */
    public boolean isEnabled() {
        return validationPatternService.isEnabled();
    }
}
