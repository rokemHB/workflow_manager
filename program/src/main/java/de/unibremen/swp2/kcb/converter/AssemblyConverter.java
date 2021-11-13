package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Assembly;
import de.unibremen.swp2.kcb.service.AssemblyService;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import lombok.SneakyThrows;

/**
 * The type assembly converter.
 *
 * @author Arvid
 */
@FacesConverter(forClass = Assembly.class)
public class AssemblyConverter implements Converter<Assembly> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private AssemblyService assemblyService;

    /**
     * Converts a given string to the accoring object.
    * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Assembly getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s.equals(""))
            return null;

        return assemblyService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param assembly the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Assembly assembly) {
        if (assembly == null)
            return "";

        return assembly.getId();
    }
}
