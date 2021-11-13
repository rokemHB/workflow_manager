package de.unibremen.swp2.kcb.validator;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.regex.Pattern;

import static org.mockito.Mockito.when;


/**
 * Abstract Test Class for all {@link de.unibremen.swp2.kcb.validator.backend.Validator}.
 * Will create a mocked {@link ValidatorConfig} that can be used by it's subclasses.
 *
 * @author Marius
 * @author Sören
 */
public abstract class ValidatorTest {
    /**
     * Mocked ValidatorConfig.
     */
    @Mock
    protected ValidatorConfig validatorConfig;

    /**
     * Should be run before each test
     */
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(validatorConfig.getPattern(Mockito.anyString())).thenReturn(Pattern.compile(".*"));
    }
}
