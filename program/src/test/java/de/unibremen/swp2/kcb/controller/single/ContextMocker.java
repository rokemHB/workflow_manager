package de.unibremen.swp2.kcb.controller.single;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.faces.context.FacesContext;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Von "http://illegalargumentexception.blogspot.com/2011/12/jsf-mocking-facescontext-for-unit-tests.html#mockFacesCurrentInstance".
 *
 * @author Arvid
 */
public abstract class ContextMocker extends FacesContext {

    /**
     * Release attribute
     */
    private static final Release RELEASE = new Release();

    /**
     * contrucor
     */
    private ContextMocker() {
    }

    /**
     * Mock faces context faces context.
     *
     * @return the faces context
     */
    public static FacesContext mockFacesContext() {
        FacesContext context = mock(FacesContext.class);
        setCurrentInstance(context);
        doAnswer(RELEASE)
                .when(context)
                .release();
        return context;
    }

    private static class Release implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }
}
