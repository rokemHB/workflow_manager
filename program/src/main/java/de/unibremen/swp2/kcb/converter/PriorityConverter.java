package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Priority;
import de.unibremen.swp2.kcb.service.PriorityService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Class PriorityConverter
 *
 * @author Marc
 */
@FacesConverter(forClass = Priority.class)
public class PriorityConverter implements Converter<Priority> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private PriorityService priorityService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Priority getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return priorityService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param priority the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Priority priority) {
        return priority.getId();
    }
}
