package de.unibremen.swp2.kcb.service;

import de.unibremen.swp2.kcb.model.ValidationPattern;
import de.unibremen.swp2.kcb.persistence.ValidationPatternRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * The type Reg ex service test.
 *
 * @author Robin
 */
public class ValidationPatternServiceTest {

    /**
     * Instance of validationPatternService for testing.
     */
    @InjectMocks
    ValidationPatternService validationPatternService;

    /**
     * The Validation pattern repository.
     */
    @Mock
    ValidationPatternRepository validationPatternRepository;

    /**
     * SetUp Method to inject Mock-Objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test get reg ex pattern null.
     */
    @Test
    public void testGetRegExPatternNull() {
        ValidationPattern validationPattern = new ValidationPattern();
        validationPattern.setSmallCharacter(true);
        validationPattern.setCapitalCharacter(true);
        validationPattern.setDigits(true);
        validationPattern.setSpecialCharacters(true);
        validationPattern.setDot(true);
        validationPattern.setUnderscore(true);
        validationPattern.setSpace(true);
        validationPattern.setDash(true);
        validationPattern.setMinLength(1);
        validationPattern.setMaxLength(20);
        validationPattern.setPattern(null);

        String result = "^[a-zA-Z0-9äüöÄÜÖß._ -]{1,20}$";

        when(validationPatternRepository.findBy("StateMachineName")).thenReturn(validationPattern);
        assertEquals(validationPatternService.getRegEx("StateMachineName"), result);
    }

    /**
     * Test get reg ex pattern not null.
     */
    @Test
    public void testGetRegExPatternNotNull() {
        ValidationPattern validationPattern = new ValidationPattern();
        validationPattern.setSmallCharacter(true);
        validationPattern.setCapitalCharacter(true);
        validationPattern.setDigits(true);
        validationPattern.setSpecialCharacters(true);
        validationPattern.setDot(true);
        validationPattern.setUnderscore(true);
        validationPattern.setSpace(true);
        validationPattern.setDash(true);
        validationPattern.setMinLength(1);
        validationPattern.setMaxLength(20);
        validationPattern.setPattern("test");

        String result = "test";

        when(validationPatternRepository.findBy("StateMachineName")).thenReturn(validationPattern);
        assertEquals(validationPatternService.getRegEx("StateMachineName"), result);
    }

}