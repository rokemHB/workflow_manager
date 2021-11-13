package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.service.WorkstationService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type Workstation converter.
 *
 * @author Marc
 */
@FacesConverter(value = "workstationConverter") // Knallt sonst bei "Location"
public class WorkstationConverter implements Converter<Workstation> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private WorkstationService workstationService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Workstation getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return workstationService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param workstation the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Workstation workstation) {
        if(workstation == null)
            return "";

        return workstation.getId();
    }
}
