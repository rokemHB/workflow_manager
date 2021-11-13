package de.unibremen.swp2.kcb.converter;


import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.service.ParameterService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type parameter converter.
 *
 * @author Marius
 */
@FacesConverter(value = "parameterConverter")
public class ParameterConverter implements Converter<Parameter> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private ParameterService parameterService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Parameter getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s.equals(""))
            return null;

        return parameterService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param parameter the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Parameter parameter) {
        if (parameter == null)
            return "";

        return parameter.getId();
    }
}
