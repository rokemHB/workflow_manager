package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.User;
import de.unibremen.swp2.kcb.service.UserService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type user converter.
 *
 * @author Arvid
 */
@FacesConverter(forClass = User.class)
public class UserConverter implements Converter<User> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private UserService userService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public User getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return userService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param user the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, User user) {
        return user.getId();
    }
}
