package de.unibremen.swp2.kcb.validator;


import de.unibremen.swp2.kcb.service.ValidationPatternService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test Class to test {@link de.unibremen.swp2.kcb.model.ValidationPattern}s.
 *
 * @author Robin
 * @author Marius
 */
public class ValidatorConfigTest {

    /**
     * Injected instance of validatorConfig for mocks to be injected in.
     */
    @InjectMocks
    private ValidatorConfig validatorConfig;

    /**
     * Mocked instance of validationPatternService.
     */
    @Mock
    private ValidationPatternService validationPatternService;

    /**
     * SetUp Method to inject Mock-Objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test not found.
     */
    @Test
    public void testNotFound() {
        // if no pattern is found the default pattern should be returned
        assertEquals(".*", validatorConfig.getRegex("testNotFound"));
        Pattern pattern = validatorConfig.getPattern("testNotFound");
        assertTrue("Default pattern should match anything.", pattern.matcher("foobar").matches());
        assertTrue("Default pattern should match anything.", pattern.matcher("123foo*'./").matches());
    }

    /**
     * Test email pattern valid.
     */
    @Test
    public void testEmailPatternValid() {
        // Check if the correct pattern is loaded and different versions of adresses
        when(validationPatternService.getRegEx("testEmail")).thenReturn("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Pattern pattern = validatorConfig.getPattern("testEmail");
        assertTrue("Email should match pattern.", pattern.matcher("foo@example.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo.bar@example.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo123@example.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo123@example2.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo123.bar@example-domain.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("Foo.bar@example-domain.com").matches());
        assertTrue("Email should match pattern.", pattern.matcher("Foo@example.de").matches());
        assertTrue("Email should match pattern.", pattern.matcher("1@example.de").matches());
        assertTrue("Email should match pattern.", pattern.matcher("1@1.de").matches());
        assertTrue("Email should match pattern.", pattern.matcher("1+1@1.de").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo_bar@bonobo.de").matches());
        assertTrue("Email should match pattern.", pattern.matcher("foo@example.onetwo").matches());
    }


    /**
     * Test assembly id.
     */
    @Test
    public void testAssemblyId() {
        when(validationPatternService.getRegEx("testAssemblyId")).thenReturn("^[A-Z][0-9][0-9].[0-9]+(.[0-9]+)+$");
        Pattern pattern = validatorConfig.getPattern("testAssemblyId");
        assertTrue("AssemblyId should match pattern.", pattern.matcher("A98.9.9").matches());
        assertTrue("AssemblyId should match pattern.", pattern.matcher("A98.9.9.3.4.5.6.7").matches());
        assertFalse("AssemblyId should not match pattern.", pattern.matcher("A98.9").matches());
        assertFalse("AssemblyId should not match pattern.", pattern.matcher("A98.9lsajdla").matches());
    }

}
