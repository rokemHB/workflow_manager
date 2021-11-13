package de.unibremen.swp2.kcb.converter;

import de.unibremen.swp2.kcb.model.Procedure;
import de.unibremen.swp2.kcb.service.ProcedureService;
import lombok.SneakyThrows;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * The type procedure converter.
 *
 * @author Arvid
 */
@FacesConverter(forClass = Procedure.class)
public class ProcedureConverter implements Converter<Procedure> {

    /**
     * Injected instance of service class to handle the converter.
     */
    @Inject
    private ProcedureService procedureService;

    /**
     * Converts a given string to the accoring object.
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param s the string for which the object should be given
     * @return the object for the given string
     */
    @SneakyThrows
    @Override
    public Procedure getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return procedureService.getById(s);
    }

    /**
     * Converts
     * @param facesContext the facesContext
     * @param uiComponent the UIComponent
     * @param procedure the object which shell be converted to string
     * @return the string of the object
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Procedure procedure) {
        return procedure.getId();
    }
}
