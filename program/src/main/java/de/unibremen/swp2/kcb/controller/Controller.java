package de.unibremen.swp2.kcb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for all Controllers.
 *
 * @see de.unibremen.swp2.kcb.controller
 *
 * @author Robin
 * @author Marius
 * @author Sören
 */
public abstract class Controller implements Serializable {

    /**
     * Logger object of the OverviewController class
     */
    private static final Logger logger = LogManager.getLogger(Controller.class);

    /**
     * LocaleController to get localized messages
     */
    @Inject
    protected LocaleController localeController;

    /**
     * Display a faces message with the given message and the given Servity
     *
     * @param summary of the message
     * @param detail  of the message
     */
    public void displayMessage(final String summary, final String detail, final FacesMessage.Severity severity) {
        if (summary != null && detail != null) {
            FacesContext.getCurrentInstance().addMessage(null, new
                    FacesMessage(severity, summary, detail));
        } else {
            logger.debug("Displaying message: {}", summary);
        }
    }

    /**
     * Display a faces message with the given message and the given Servity
     *
     * @param summary  of the message
     * @param detail   of the message
     * @param clientId where should the message be displayed? e.g. "formid:inputid"
     */
    protected void displayMessage(final String summary, final String detail, final String clientId, final FacesMessage.Severity severity) {
        if (summary != null && detail != null) {
            FacesContext.getCurrentInstance().addMessage(clientId, new
                    FacesMessage(severity, summary, detail));
        } else {
            logger.debug("Displaying message: {}", summary);
        }
    }

    /**
     * Display a message from the resource bundle with the given key
     *
     * @param summaryKey of the message to be displayed
     * @param detailKey  of the message to be displayed
     */
    protected void displayMessageFromResource(final String summaryKey, final String detailKey, final FacesMessage.Severity severity) {
        final String summary = localeController.formatString(summaryKey);
        final String detail = localeController.formatString(detailKey);
        if (summary != null && !summary.equals("") && detail != null && !detail.equals(""))
            displayMessage(summary, detail, severity);
        else
            logger.debug("Display of message {} skipped. No message found.", summary);
    }

    /**
     * Display a message from the resource bundle with the given key
     *
     * @param summaryKey of the message to be displayed
     * @param detailKey  of the message to be displayed
     * @param clientId   where should the message be displayed? e.g. "formid:inputid"
     */
    protected void displayMessageFromResource(final String summaryKey, final String detailKey, final String clientId, final FacesMessage.Severity severity) {
        final String summary = localeController.formatString(summaryKey);
        final String detail = localeController.formatString(detailKey);
        if (summary != null && !summary.equals("") && detail != null && !detail.equals(""))
            displayMessage(summary, detail, clientId, severity);
        else
            logger.debug("Display of message {} skipped. No message found.", summary);
    }

    /**
     * Get all Messages for the current Instance
     */
    public List<FacesMessage> getMessageQueue() {
        if (FacesContext.getCurrentInstance() == null) return new ArrayList<>();
        return FacesContext.getCurrentInstance().getMessageList();
    }

    /**
     * Remove all Messages for the current Instance
     */
    public void removeMessageQueue() {
        FacesContext.getCurrentInstance().getMessageList().clear();
    }

    /**
     * Force re-rendering of component with the given id.
     *
     * @param id of component to be re-rendered.
     */
    protected void render(final String id) {
        try {
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(id);
        } catch (NullPointerException e) {
            logger.warn("NullPointerException occurred during render of {}", id);
        }
    }

    /**
     * Redirect the user to a given path
     *
     * @param path to redirect user to
     */
    public void redirect(final String path) {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(path + "?faces-redirect=true");
        } catch (IOException e) {
            logger.debug("Redirect failed: {}", e.getMessage());
        }
    }

    /**
     * Redirect the user to a given url with params
     *
     * @param path to redirect user to
     */
    public void redirectToPath(final String path) {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(path);
        } catch (IOException e) {
            logger.debug(e);
        }
    }

    /**
     * Redirect the user to a given path
     *
     * @param path to redirect user to
     */
    public void redirect(final String path, final String id) {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(path + "?faces-redirect=true" + "&id=" + id);
        } catch (IOException e) {
            logger.debug("Redirect failed: {}", e.getMessage());
        }
    }

}
