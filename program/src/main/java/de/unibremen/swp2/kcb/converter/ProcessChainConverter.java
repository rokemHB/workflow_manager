package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.ProcessChain;
import de.unibremen.swp2.kcb.service.ProcessChainService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type processChain converter.
 *
 * @author Marc
 * @author Arvid
 */
@FacesConverter(forClass = ProcessChain.class)
public class ProcessChainConverter implements Converter<ProcessChain> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private ProcessChainService processChainService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public ProcessChain getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return processChainService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param processChain the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, ProcessChain processChain) {
        if(processChain == null)
            return "";

        return processChain.getId();
    }
}
