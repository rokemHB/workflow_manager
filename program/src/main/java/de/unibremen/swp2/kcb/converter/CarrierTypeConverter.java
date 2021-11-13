package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.CarrierType;
import de.unibremen.swp2.kcb.service.CarrierTypeService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type Carrier type converter.
 *
 * @author Marc
 */
@FacesConverter(forClass = CarrierType.class)
public class CarrierTypeConverter implements Converter<CarrierType> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private CarrierTypeService carrierTypeService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public CarrierType getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return carrierTypeService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param carrierType the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, CarrierType carrierType) {
        if(carrierType == null)
            return "";

        return carrierType.getId();
    }
}
