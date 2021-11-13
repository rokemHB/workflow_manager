package de.unibremen.swp2.kcb.tags;

import de.unibremen.swp2.kcb.model.StateMachine.State;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@FacesComponent(createTag = true, namespace = "http://kcb.mschaeff.de",
        tagName = "isNotTransportState", value = "de.unibremen.swp2.kcb.tags.IsNotTransportStateCondition")
public class IsNotTransportStateCondition extends UIComponentBase {

    /**
     * Family of the UIComponent
     */
    public static final String FAMILY = "de.unibremen.swp2.kcb.tags.IsNotTransportStateCondition";

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getFamily() {
        return FAMILY;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean isRendered() {
        State state = (State) getAttributes().get("state");
        return !state.getName().equals("Transport");
    }
}
