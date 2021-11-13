package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.service.StateService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Class StateConverter
 *
 * @author Marc
 * @author Marius
 */
@FacesConverter(value = "stateConverter")
public class StateConverter implements Converter<State> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private StateService stateService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public State getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return stateService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param state the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, State state) {
        if(state == null)
            return "";

        return state.getId();
    }
}
