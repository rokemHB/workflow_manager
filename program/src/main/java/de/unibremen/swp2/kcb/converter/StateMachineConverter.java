package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.service.StateMachineService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Class StateMachineConverter
 *
 * @author Marc
 */
@FacesConverter(forClass = StateMachine.class)
public class StateMachineConverter implements Converter<StateMachine> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private StateMachineService stateMachineService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public StateMachine getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return stateMachineService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param stateMachine the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, StateMachine stateMachine) {
        if(stateMachine == null)
            return "";

        return stateMachine.getId();
    }
}
