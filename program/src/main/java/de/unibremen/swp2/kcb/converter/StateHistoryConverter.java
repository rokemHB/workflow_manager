package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.service.StateHistoryService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type StateHistory converter.
 *
 * @author Marc
 */
@FacesConverter(forClass = StateHistory.class)
public class StateHistoryConverter implements Converter<StateHistory> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private StateHistoryService stateHistoryService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public StateHistory getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return stateHistoryService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param stateHistory the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, StateHistory stateHistory) {
        return stateHistory.getId();
    }
}
