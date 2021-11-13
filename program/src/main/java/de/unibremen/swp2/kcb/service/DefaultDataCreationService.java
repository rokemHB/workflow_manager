package de.unibremen.swp2.kcb.service;

import com.lambdaworks.crypto.SCryptUtil;
import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.util.EntityManagerProducer;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates default data that will be persisted to the DB on restart.
 *
 * @author Robin
 * @author Marius
 * @author Arvid
 * @author Sören
 */
@WebListener
public class DefaultDataCreationService implements ServletContextListener {

    /**
     * Logger object of the BasicDataUtil class
     */
    private static final Logger logger = LogManager.getLogger(DefaultDataCreationService.class);

    /**
     * Password used for the default admin account - this needs to be changed later by the Administrator
     */
    public static final String DEFAULT_PASSWORD = "kcb";

    /**
     * EntityManager to handle persistence
     */
    private EntityManager em;

    /**
     * EntityManagerProducer to get EntityManager from
     */
    @Inject
    private EntityManagerProducer emFactory;

    private final String findValidationPattern = "findValidationsPatternsByName";

    /**
     * Method that is called on context initialization/server startup
     *
     * @param contextEvent invoked context event
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        this.em = emFactory.getEntityManager();
        this.createDefaultUser();
        this.createDefaultCarrierTypes();
        this.createDefaultStock();
        this.createDefaultTransportState();
        this.createDefaultUsernameValPattern();
        this.createDefaultCarrierTypeValPattern();
        this.createDefaultCarrierIdValPattern();
        this.createDefaultJobNameValPattern();
        this.createDefaultStateNameValPattern();
        this.createDefaultStateMachineNameValPattern();
        this.createDefaultWorkstationNameValPattern();
        this.createDefaultProcessStepNameValPattern();
        this.createDefaultProcessChainNameValPattern();
        this.createDefaultNameValPattern();
        this.createDefaultEmailValPattern();
        this.createDefaultPasswordValPattern();
        this.createDefaultAssemblyAlloyValPattern();
        this.createDefaultAssemblyCommentValPattern();
        this.createDefaultAssemblyIdValPattern();
        this.createDefaultPositionValPattern();
        this.createDefaultParameterFieldValPattern();
        this.createDefaultPriorityNameFieldValPattern();
        this.createDefaultValueValPattern();
        this.createDefaultIdValPattern();
        this.createDefaultIntegerValPattern();
        this.createDefaultOldActiveJobValue();
        this.createDefaultValueValPattern();
        final String kcbDebug = System.getenv("KCB_DEBUG");
        if (kcbDebug != null && kcbDebug.equalsIgnoreCase("true")) {
            logger.warn("KCB_DEBUG environment variable found true. Will create demo data.");
            try {
                new DemoDataCreationService(this.em).createDemoData();
            } catch (RuntimeException e) {
                logger.warn("Demo data creation failed: " + e);
            }
        }
    }

    /**
     * Method that is called on context destruction/server shutdown
     *
     * @param contextEvent invoked context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        emFactory.closeEntityManager(em);
    }

    /**
     * Create default admin user if it's not present in the database
     */
    private void createDefaultUser() {
        List<User> users = (List<User>) this.em.createNamedQuery("findUsersByUsername")
                .setParameter("username", "admin").getResultList();

        User user = null;

        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }

        if (!exists(user)) {
            logger.info("Default admin user not found. Creating...");
            user = new User();
            user.setEmail("admin@example.com");
            user.setUsername("admin");
            user.setFirstName("Armin");
            user.setLastName("Admin");
            user.setPassword(
                    SCryptUtil.scrypt(DEFAULT_PASSWORD,
                            UserService.DEFAULT_SCRYPT_N,
                            UserService.DEFAULT_SCRYPT_R,
                            UserService.DEFAULT_SCRYPT_P));
            user.setRoles(Role.asStringSet(Role.ADMIN));
            save(user);
        }
    }

    /**
     * Create default CarrierTypes.
     */
    private void createDefaultCarrierTypes() {
        List<CarrierType> carrierTypeList = this.em.createQuery("SELECT ct FROM CarrierType ct").getResultList();
        String[] carrierTypes = {"Glas", "Steckbrett", "Schale"};

        for (String type : carrierTypes) {
            CarrierType carrierType = new CarrierType();
            carrierType.setName(type);
            if (!this.contains(carrierType, carrierTypeList))
                save(carrierType);
        }
    }

    /**
     * Create default transport states
     */
    private void createDefaultTransportState() {
        List<State> stateList = this.em.createQuery("SELECT s FROM State s").getResultList();

        State state = new State();
        state.setName("Transport");
        state.setBlocking(false);

        if (!this.contains(state, stateList))
            save(state);
    }

    /**
     * Create default Stock.
     */
    private void createDefaultStock() {
        List<State> stockList = this.em.createQuery("SELECT s FROM Stock s").getResultList();

        if (!stockList.isEmpty())
            return;

        Stock stock = new Stock();
        stock.setPosition("Lager");
        save(stock);
    }

    /**
     * Create default ValidationPattern for CarrierType if it's not present in the database
     */
    public void createDefaultCarrierTypeValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "CarrierType").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern carrierType was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("CarrierType");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(30);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Id if it's not present in the database
     */
    public void createDefaultIdValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Id").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern Id was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Id");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(false);
            validationPattern.setCapitalCharacter(false);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(false);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(30);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for CarrierId if it's not present in the database
     */
    public void createDefaultCarrierIdValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "CarrierId").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern carrierId was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("CarrierId");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(15);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for jobName if it's not present in the database
     */
    public void createDefaultJobNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "JobName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern jobName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("JobName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for stateName if it's not present in the database
     */
    public void createDefaultStateNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "StateName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern stateName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("StateName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for stateName if it's not present in the database
     */
    public void createDefaultStateMachineNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "StateMachineName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern StateMachineName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("StateMachineName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for workstationName if it's not present in the database
     */
    public void createDefaultWorkstationNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "WorkstationName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for workstationName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("WorkstationName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Username if it's not present in the database
     */
    public void createDefaultUsernameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Username").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for validationPattern username was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Username");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(3);
            validationPattern.setMaxLength(20);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }


    /**
     * Create default ValidationPattern for ProcessStepName if it's not present in the database
     */
    public void createDefaultProcessStepNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "ProcessStepName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for processStepName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("ProcessStepName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for processChainName if it's not present in the database
     */
    public void createDefaultProcessChainNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "ProcessChainName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for processChainName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("ProcessChainName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Name if it's not present in the database
     */
    public void createDefaultNameValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Name").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Name was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Name");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Name if it's not present in the database
     */
    public void createDefaultEmailValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Email").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Email was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Email");
            validationPattern.setAdvanced(true);
            validationPattern.setSmallCharacter(false);
            validationPattern.setCapitalCharacter(false);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(false);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(0);
            validationPattern.setMaxLength(0);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Password if it's not present in the database
     */
    public void createDefaultPasswordValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Password").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Password was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Password");
            validationPattern.setAdvanced(true);
            validationPattern.setSmallCharacter(false);
            validationPattern.setCapitalCharacter(false);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(false);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(0);
            validationPattern.setMaxLength(0);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
            //(?=\S+$) -> no whitespace allowed; the others backtrack for atleast one occurrence

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for AssemblyAlloy if it's not present in the database
     */
    public void createDefaultAssemblyAlloyValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "AssemblyAlloy").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for AssemblyAlloy was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("AssemblyAlloy");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for AssemblyAlloy if it's not present in the database
     */
    public void createDefaultAssemblyCommentValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "AssemblyComment").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for AssemblyComment was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("AssemblyComment");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(255);
            validationPattern.setSlash(true);
            validationPattern.setBackslash(true);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for AssemblyId if it's not present in the database
     */
    public void createDefaultAssemblyIdValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "AssemblyId").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for AssemblyId was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("AssemblyId");
            validationPattern.setAdvanced(true);
            validationPattern.setSmallCharacter(false);
            validationPattern.setCapitalCharacter(false);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(false);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(0);
            validationPattern.setMaxLength(0);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern("^[A-Z][0-9][0-9].[0-9]+(.[0-9]+)+$");

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Position if it's not present in the database
     */
    public void createDefaultPositionValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Position").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Position was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Position");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for ParameterField if it's not present in the database
     */
    public void createDefaultParameterFieldValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "ParameterField").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for ParameterField was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("ParameterField");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(true);
            validationPattern.setBackslash(true);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for ParameterField if it's not present in the database
     */
    public void createDefaultPriorityNameFieldValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "PriorityName").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for PriorityName was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("PriorityName");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(20);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Value if it's not present in the database
     */
    public void createDefaultValueValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Value").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Value was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Value");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(true);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(true);
            validationPattern.setBackslash(true);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Integer if it's not present in the database
     */
    public void createDefaultIntegerValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Integer").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Integer was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Integer");
            validationPattern.setAdvanced(true); // advanced since it should not be changed easily
            validationPattern.setSmallCharacter(false);
            validationPattern.setCapitalCharacter(false);
            validationPattern.setDigits(false);
            validationPattern.setSpecialCharacters(false);
            validationPattern.setDot(false);
            validationPattern.setUnderscore(false);
            validationPattern.setDash(false);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(0);
            validationPattern.setMaxLength(0);
            validationPattern.setSlash(false);
            validationPattern.setBackslash(false);
            validationPattern.setPattern("^[0-9]+$");

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for Unit if it's not present in the database
     */
    public void createDefaultUnitValPattern() {
        List<ValidationPattern> patterns = (List<ValidationPattern>) this.em.createNamedQuery(findValidationPattern)
                .setParameter("name", "Unit").getResultList();

        ValidationPattern validationPattern = null;

        if (patterns != null && !patterns.isEmpty()) {
            validationPattern = patterns.get(0);
        }

        if (!exists(validationPattern)) {
            logger.info("No entry for Unit was found. Creating...");
            validationPattern = new ValidationPattern();

            validationPattern.setName("Unit");
            validationPattern.setAdvanced(false);
            validationPattern.setSmallCharacter(true);
            validationPattern.setCapitalCharacter(true);
            validationPattern.setDigits(true);
            validationPattern.setSpecialCharacters(true);
            validationPattern.setDot(true);
            validationPattern.setUnderscore(true);
            validationPattern.setDash(true);
            validationPattern.setSpace(false);
            validationPattern.setMinLength(1);
            validationPattern.setMaxLength(40);
            validationPattern.setSlash(true);
            validationPattern.setBackslash(true);
            validationPattern.setPattern(null);

            save(validationPattern);
        }
    }

    /**
     * Create default ValidationPattern for oldActiveJob if it's not present in the database.
     * This value can be set by an Admin and indicates when a active job counts as old.
     */
    public void createDefaultOldActiveJobValue() {
        List<GlobalConfig> patterns = (List<GlobalConfig>) this.em.createNamedQuery("findGlobalConfigsByName")
          .setParameter("key", "oldActiveJob").getResultList();

        GlobalConfig globalConfig = null;

        if (patterns != null && !patterns.isEmpty()) {
            globalConfig = patterns.get(0);
        }

        if (!exists(globalConfig)) {
            logger.info("No entry for oldActiveJob was found. Creating...");
            globalConfig = new GlobalConfig();

            globalConfig.setKey("oldActiveJob");
            globalConfig.setValue("4786"); // minutes --> SET TO 2880, 1 ONLY FOR TEST PURPOSES

            save(globalConfig);
        }
    }

    /**
     * Check if given entity exists
     *
     * @param entity to check
     * @return does the given entity exist?
     */
    private boolean exists(Object entity) {
        return entity != null;
    }

    /**
     * Stores the given object in the PU using Transactions.
     *
     * @param o to be persisted
     */
    public void save(Object o) {
        em.getTransaction().begin();
        if (!em.contains(o)) {
            em.persist(o);
            em.flush();
        }
        em.getTransaction().commit();
    }

    /**
     * Checks whether or not a list of carrierTypes contains a carrierType
     *
     * @param carrierType  to be checked
     * @param carrierTypes of all users to be checked
     * @return does the list contain the carrierType?
     */
    public boolean contains(CarrierType carrierType, List<CarrierType> carrierTypes) {
        for (CarrierType ct : carrierTypes) {
            if (ct.getName().equals(carrierType.getName())
                    || ct.getId().equals(carrierType.getId()))
                return true;
        }
        return false;
    }

    /**
     * Checks whether or not a list of states contains a state
     *
     * @param state  to be checked
     * @param states of all users to be checked
     * @return does the list contain the state?
     */
    public boolean contains(State state, List<State> states) {
        for (State s : states) {
            if (s.getName().equals(state.getName())
                    || s.isBlocking() == state.isBlocking()
                    || s.getId().equals(state.getId()))
                return true;
        }
        return false;
    }
}
