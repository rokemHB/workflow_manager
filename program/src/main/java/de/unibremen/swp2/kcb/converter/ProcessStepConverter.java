package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.ProcessStep;
import de.unibremen.swp2.kcb.service.ProcessStepService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type processStep converter.
 *
 * @author Marc
 * @author Marius
 * @author Arvid
 */
@FacesConverter(value = "processStepConverter")
public class ProcessStepConverter implements Converter<ProcessStep> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private ProcessStepService processStepService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public ProcessStep getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s.equals(""))
            return null;

        return processStepService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param processStep the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, ProcessStep processStep) {
        if(processStep == null)
            return "";

        return processStep.getId();
    }
}
