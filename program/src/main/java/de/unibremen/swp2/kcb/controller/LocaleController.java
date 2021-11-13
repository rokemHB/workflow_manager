package de.unibremen.swp2.kcb.controller;
import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import de.unibremen.swp2.kcb.service.serviceExceptions.UpdateException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class to set the locale language.
 *
 * Nach: https://stackoverflow.com/questions/4830588/localization-in-jsf-how-to-remember-selected-locale-per-session-instead-of-per
 * Abruf: 24.10.2019
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@Named
@SessionScoped
public class LocaleController extends Controller {

    /**
     * Logger object of the LocaleController class
     */
    private static final Logger logger = LogManager.getLogger(LocaleController.class);

    /**
     * Currently selected locale
     */
    @Getter
    private Locale currentLocale;

    /**
     * User service to access executing user
     */
    @Inject
    private UserService userService;

    /**
     * Collection of all messages to be translated
     */
    private ResourceBundle messageBundle;

    /**
     * Initiate LocaleController
     */
    @PostConstruct
    public void init() {
        currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            // Check if logged in user set different locale
            User currentUser = userService.getExecutingUser();
            String userPreferredLocale = currentUser.getPreferredLocale();
            if (userPreferredLocale != null && !userPreferredLocale.equals(currentLocale.getLanguage())) {
                // User has defined a different preference
                currentLocale = Locale.forLanguageTag(userPreferredLocale);
            }
        }
        this.messageBundle = loadResourceBundle("msg");
        this.updateLanguageCookies(currentLocale.getLanguage());
    }

    /**
     * Return the current locale of the session
     *
     * @return currentLocale of the session
     */
    public String getLanguage() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        // Check if locale has changed - if so: Update current locale in FacesContext
        if (viewRoot != null && !viewRoot.getLocale().equals(currentLocale))
            FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
        return currentLocale != null ? currentLocale.getLanguage() : "";
    }

    /**
     * Change the currently selected locale.
     *
     * @param language of the new locale
     */
    public void setLanguage(String language) {
        if (language == null || language.equals("")) {
            logger.debug("Attempt to set current locale with locale = {}", language);
            return;
        }
        currentLocale = Locale.forLanguageTag(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
        User currentUser = this.userService.getExecutingUser();
        try {
            currentUser.setPreferredLocale(language);
            userService.updateWithoutPermission(currentUser);
        } catch (UpdateException e) {
            super.displayMessageFromResource("error.summary.locale-change-failed",
                    "error.detail.locale-change-failed", FacesMessage.SEVERITY_ERROR);
        }
        this.updateLanguageCookies(language);
        this.messageBundle = loadResourceBundle("msg");
        super.redirect("./settings.xhtml");
    }

    /**
     * Format the message with the given name to the localized version.
     *
     * @param key of the message to format
     */
    public String formatString(String key) {
        // Try to load messageBundle if not defined
        if (this.messageBundle == null)
            this.messageBundle = loadResourceBundle("message");
        if (this.messageBundle != null && messageBundle.containsKey(key)) {
            final String message = messageBundle.getString(key);
            logger.debug("Found message: {} for key: {}", message, key);
            return message;
        }
        logger.warn("Message for key {} could not be loaded.", key);
        return "";
    }

    /**
     * Loads the resource bundle with the given name.
     *
     * @param name of the resource bundle
     * @return ResourceBundle with given name or null if an error occurred.
     */
    private ResourceBundle loadResourceBundle(final String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getApplication() != null) {
            return context.getApplication().getResourceBundle(context, name);
        }
        logger.warn("Resource bundle {} could not be loaded.", name);
        return null;
    }

    /**
     * Update the language cookie for HttpServletRequest
     *
     * @param language to be set in the language cookie
     */
    private void updateLanguageCookies(final String language) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) return;
        ExternalContext externalContext = facesContext.getExternalContext();
        if (externalContext == null) return;
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("language")) {
                cookie.setValue(language);
                HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
                response.addCookie(cookie);
                return;
            }
        }
        Cookie cookie = new Cookie("language", language);
        cookie.setMaxAge(-1);
        cookie.setPath(request.getContextPath());
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        response.addCookie(cookie);
    }

}
