package de.unibremen.swp2.kcb.controller;

import de.unibremen.swp2.kcb.model.ResetToken;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.InvalidTokenException;
import de.unibremen.swp2.kcb.service.serviceExceptions.PasswordChangeException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * Controller class to set the locale language.
 *
 * @author Marius
 * @author Arvid
 */
@Named
@RequestScoped
public class PasswordResetConfirmController extends Controller {

    /**
     * Logger object of the LocaleController class
     */
    private static final Logger logger = LogManager.getLogger(PasswordResetConfirmController.class);

    /**
     * Injected instance of UserService
     */
    @Inject
    private UserService userService;

    /**
     * Password attribute of PasswordResetConfirmController
     */
    @Getter
    @Setter
    private String password;

    /**
     * Passwordrepeated attribute of PasswordResetConfirmController
     */
    @Getter
    @Setter
    private String passwordRepeated;

    /**
     * Check if Form should be rendered.
     *
     * @return should reset password form be rendered?
     */
    public boolean show() {
        String token = this.getResetToken();
        try {
            ResetToken storedToken = userService.validateToken(token);
            return storedToken != null;
        } catch (InvalidTokenException e) {
            logger.debug("Token validation failed. " + e.getMessage());
            // Token is invalid. Show 401 Error.
            try {
                this.redirect("invalid.xhtml");
            } catch (RuntimeException exc) {
                logger.debug("Error occurred during password reset failure redirect: " + exc);
            }
        }
        return false;
    }

    /**
     * Get reset token from request
     *
     * @return reset token provided in request
     */
    private String getResetToken() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap();
            return params.get("token");
        } catch (RuntimeException e) {
            logger.debug("Invalid token in request. Empty token will be returned.");
        }
        return "";
    }

    /**
     * Confirm the password change
     */
    public void submit() {
        try {
            userService.confirmPasswordChange(this.password, this.passwordRepeated, getResetToken());
            // User service should log user in on success.
            this.redirect("../dashboard.xhtml");
        } catch (PasswordChangeException | InvalidTokenException e) {
            logger.debug("Password change failed. Passwords didn't match.");
        } catch (AuthenticationException e) {
            logger.debug("Auto login attempt failed after password reset. Will redirect to login.");
            this.redirect("../login.xhtml");
        }
    }
}
