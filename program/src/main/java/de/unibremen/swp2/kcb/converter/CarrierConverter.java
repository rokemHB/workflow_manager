package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Carrier;
import de.unibremen.swp2.kcb.service.CarrierService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type Carrier converter.
 *
 * @author Marc
 */
@FacesConverter(forClass = Carrier.class)
public class CarrierConverter implements Converter<Carrier> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private CarrierService carrierService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Carrier getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return carrierService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param carrier the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Carrier carrier) {
        if(carrier == null)
            return "";

        return carrier.getId();
    }
}
