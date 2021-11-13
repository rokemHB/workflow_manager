package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Locations.Location;
import de.unibremen.swp2.kcb.service.LocationService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type Location converter.
 *
 * @author Marc
 */
@FacesConverter(forClass = Location.class)
public class LocationConverter implements Converter<Location> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private LocationService locationService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Location getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return locationService.getById(s);
    }
    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param location the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Location location) {
        if(location == null)
            return "";

        return location.getId();
    }
}
