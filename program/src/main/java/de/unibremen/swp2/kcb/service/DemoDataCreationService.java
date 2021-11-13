package de.unibremen.swp2.kcb.service;

import com.lambdaworks.crypto.SCryptUtil;
import de.unibremen.swp2.kcb.model.*;
import de.unibremen.swp2.kcb.model.Locations.Stock;
import de.unibremen.swp2.kcb.model.Locations.Transport;
import de.unibremen.swp2.kcb.model.Locations.Workstation;
import de.unibremen.swp2.kcb.model.StateMachine.State;
import de.unibremen.swp2.kcb.model.StateMachine.StateHistory;
import de.unibremen.swp2.kcb.model.StateMachine.StateMachine;
import de.unibremen.swp2.kcb.model.parameter.Parameter;
import de.unibremen.swp2.kcb.model.parameter.Value;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static de.unibremen.swp2.kcb.service.DefaultDataCreationService.DEFAULT_PASSWORD;


/**
 * Class DemoDataCreationService
 *
 * @author Marc
 * @author Robin
 * @author Marius
 * @author Arvid
 */
public class DemoDataCreationService {

    /**
     * Entity manager instance for DemoDataCreation class
     */
    private final EntityManager em;

    /**
     * Constant string for queries
     */
    private final String selectUser = "SELECT u FROM User u";

    /**
     * Create new instance of DemoDataCreationService
     *
     * @param em to access the datasource
     */
    public DemoDataCreationService(final EntityManager em) {
        this.em = em;
    }

    /**
     * Create demo data for Workflow Manager
     */
    public void createDemoData() {
        if (this.em == null) return;
        this.createDemoUsers();
        this.createDemoStateMachine();
        this.createDemoWorkstation();
        this.createDemoParameter();
        this.createDemoProcessStep();
        this.createDemoCarriersAndAssemblies();
        this.createDemoTransport();
        this.createDemoProcessChain();
        this.createDemoPriorities();
        this.createDemoJobs();
    }

    /**
     * Create demo users of Workflow Managers
     */
    public void createDemoUsers() {
        List<User> users = this.em.createQuery(selectUser).getResultList();
        String[][] names = {
                {"Petra", "Lothar ", "Thea", "Armin", "Thorsten"},
                {"Prozesskettenplanerin", "Logistiker", "Technologin", "Admin", "the Transporter"}
        };

        Role[] roles = Role.values();
        int i = 0;
        for (Role role : roles) {
            User user = new User();
            user.setEmail(role.readable().toLowerCase() + "@kcb-test.de");
            user.setUsername(role.readable().toLowerCase());
            user.setFirstName(names[0][i]);
            user.setLastName(names[1][i]);
            user.setPassword(
                    SCryptUtil.scrypt(DEFAULT_PASSWORD,
                            UserService.DEFAULT_SCRYPT_N,
                            UserService.DEFAULT_SCRYPT_R,
                            UserService.DEFAULT_SCRYPT_P));
            user.setRoles(Role.asStringSet(role));
            if (!this.contains(user, users))
                save(user);
            i++;
        }

        // Create super user with all roles
        User superUser = new User();
        superUser.setEmail("super@kcb-test.de");
        superUser.setUsername("super");
        superUser.setFirstName("Super");
        superUser.setLastName("Man");
        superUser.setPassword(
                SCryptUtil.scrypt(DEFAULT_PASSWORD,
                        UserService.DEFAULT_SCRYPT_N,
                        UserService.DEFAULT_SCRYPT_R,
                        UserService.DEFAULT_SCRYPT_P));
        // Assign all existing roles to super user
        superUser.setRoles(Role.asStringSet());
        if (!this.contains(superUser, users)) save(superUser);
    }

    /**
     * Create demo transport.
     */
    public void createDemoTransport() {
        List<User> users = this.em.createQuery(selectUser).getResultList();

        for (User user : users) {
            if (user.getRoles().contains(Role.TRANSPORT)) {
                Transport transport = new Transport();
                transport.setTransporter(user);
                transport.setPosition(user.getFirstName() + " " + user.getLastName());
                save(transport);
            }
        }
    }

    /**
     * Create demo state machine.
     */
    public void createDemoStateMachine() {
        String[] blockingStates = {"Erhitzen", "Einfrieren", "Schütteln", "Baden"};
        String[] nonBlockingStates = {"Abkühlen", "Auftauen", "Legen", "Trocknen"};

        List<State> blocking = new ArrayList<>();
        List<State> nonBlocking = new ArrayList<>();

        State transport  = (State) this.em.createQuery("SELECT s FROM State s WHERE s.name = 'Transport'").getSingleResult();

        for (String name : blockingStates) {
            State state = new State();
            state.setBlocking(true);
            state.setName(name);
            blocking.add(state);
            save(state);
        }

        for (String name : nonBlockingStates) {
            State state = new State();
            state.setBlocking(false);
            state.setName(name);
            nonBlocking.add(state);
            save(state);
        }

        blocking.add(transport);
        nonBlocking.add(transport);

        StateMachine stateMachine1 = new StateMachine();
        stateMachine1.setName("State Machine 1");
        stateMachine1.setStateList(blocking);
        save(stateMachine1);

        StateMachine stateMachine2 = new StateMachine();
        stateMachine2.setName("State Machine 2");
        stateMachine2.setStateList(nonBlocking);
        save(stateMachine2);

        List<State> weaved = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            weaved.add(blocking.get(i));
            weaved.add(nonBlocking.get(i));
        }
        weaved.add(transport);

        StateMachine stateMachine3 = new StateMachine();
        stateMachine3.setName("State Machine 3");
        stateMachine3.setStateList(weaved);
        save(stateMachine3);
    }

    /**
     * Create demo workstation.
     */
    public void createDemoWorkstation() {
        List<User> users = this.em.createQuery("SELECT u FROM User u WHERE u.firstName = 'Thea'").getResultList();

        for (int i = 1; i <= 3; i++) {

            Workstation workstation = new Workstation();
            workstation.setBroken(false);
            workstation.setActive(true);
            workstation.setName("Workstation " + i);
            workstation.setPosition(workstation.getName());
            workstation.setUsers(users);
            save(workstation);
        }
        Workstation brokeWorkstation = new Workstation();
        brokeWorkstation.setBroken(true);
        brokeWorkstation.setActive(true);
        brokeWorkstation.setName("Workstation 4");
        brokeWorkstation.setPosition(brokeWorkstation.getName());
        brokeWorkstation.setUsers(null);
        save(brokeWorkstation);

        Workstation inactiveWorkstation = new Workstation();
        inactiveWorkstation.setBroken(false);
        inactiveWorkstation.setActive(false);
        inactiveWorkstation.setName("Workstation 5");
        inactiveWorkstation.setPosition(inactiveWorkstation.getName());
        inactiveWorkstation.setUsers(null);
        save(inactiveWorkstation);
    }

    /**
     * Create demo parameter.
     */
    public void createDemoParameter() {
        String[] parameterFields = {"Temperatur", "Druck", "Zeit", "Luftfeuchtigkeit", "Windstärke"};

        for (String parameterField : parameterFields) {
            Parameter parameter = new Parameter();
            parameter.setField(parameterField);
            save(parameter);
        }
    }

    /**
     * Create demo process step.
     */
    public void createDemoProcessStep() {
        List<Workstation> workstations = this.em.createQuery("SELECT w FROM Workstation w").getResultList();
        List<Parameter> parameters = this.em.createQuery("SELECT p FROM Parameter p").getResultList();
        List<StateMachine> stateMachines = this.em.createQuery("SELECT sm FROM StateMachine sm").getResultList();
        List<CarrierType> carrierTypes = this.em.createQuery("SELECT ct FROM CarrierType ct").getResultList();

        int counter = 0;
        for (Workstation workstation : workstations) {
            ProcessStep processStep1 = new ProcessStep();
            processStep1.setName("Prozessschritt " + counter);
            processStep1.setParameters(parameters.subList(0, parameters.size() / 2));
            processStep1.setEstDuration(420);
            processStep1.setStateMachine(stateMachines.get(counter % stateMachines.size()));
            processStep1.setPreparation(carrierTypes.get(counter % carrierTypes.size()));
            processStep1.setOutput(carrierTypes.get((counter + 1) % carrierTypes.size()));
            processStep1.setWorkstation(workstation);
            processStep1.setModifies(true);
            processStep1.setCreates(false);
            processStep1.setDeletes(false);
            save(processStep1);
            counter++;

            ProcessStep processStep2 = new ProcessStep();
            processStep2.setName("Prozessschritt " + counter);
            processStep2.setParameters(parameters.subList(parameters.size() / 2, parameters.size()));
            processStep2.setEstDuration(240);
            processStep2.setStateMachine(stateMachines.get(counter % stateMachines.size()));
            processStep2.setPreparation(carrierTypes.get(counter % carrierTypes.size()));
            processStep2.setOutput(carrierTypes.get((counter + 1) % carrierTypes.size()));
            processStep2.setWorkstation(workstation);
            processStep2.setModifies(false);
            processStep2.setCreates(false);
            processStep2.setDeletes(false);
            save(processStep2);
            counter++;

            ProcessStep processStep3 = new ProcessStep();
            processStep3.setName("Prozessschritt " + counter + " K");
            processStep3.setParameters(parameters);
            processStep3.setEstDuration(500);
            processStep3.setStateMachine(stateMachines.get(counter % stateMachines.size()));
            processStep3.setPreparation(carrierTypes.get(counter % carrierTypes.size()));
            processStep3.setOutput(carrierTypes.get((counter + 1) % carrierTypes.size()));
            processStep3.setWorkstation(workstation);
            processStep3.setModifies(false);
            processStep3.setCreates(false);
            processStep3.setDeletes(false);
            save(processStep3);
            counter++;
        }

        if (stateMachines.size() == 0 || carrierTypes.size() == 0)
            return;

        ProcessStep processStep4 = new ProcessStep();
        processStep4.setName("Urformender Prozessschritt C");
        processStep4.setParameters(parameters);
        processStep4.setEstDuration(1000);
        processStep4.setStateMachine(stateMachines.get(counter % stateMachines.size()));
        processStep4.setPreparation(carrierTypes.get(counter % carrierTypes.size()));
        processStep4.setOutput(carrierTypes.get((counter + 1) % carrierTypes.size()));
        processStep4.setWorkstation(workstations.get(0));
        processStep4.setModifies(false);
        processStep4.setCreates(true);
        processStep4.setDeletes(false);
        save(processStep4);

        ProcessStep processStep5 = new ProcessStep();
        processStep5.setName("Löschender Prozessschritt D");
        processStep5.setParameters(parameters);
        processStep5.setEstDuration(1000);
        processStep5.setStateMachine(stateMachines.get(counter % stateMachines.size()));
        processStep5.setPreparation(carrierTypes.get(counter % carrierTypes.size()));
        processStep5.setOutput(carrierTypes.get((counter + 1) % carrierTypes.size()));
        processStep5.setWorkstation(workstations.get(2));
        processStep5.setModifies(false);
        processStep5.setCreates(false);
        processStep5.setDeletes(true);
        save(processStep5);
    }

    /**
     * Create demo process chain.
     */
    public void createDemoProcessChain() {
        List<ProcessStep> workingProcessSteps = this.em.createQuery("SELECT ps FROM ProcessStep ps WHERE ps.workstation.broken = false AND " +
                "ps.name NOT LIKE '%K' AND ps.name NOT LIKE '%C' AND ps.name NOT LIKE '%D'").getResultList();

        for (int i = 0; i < 5; i++) {
            ProcessChain processChain = new ProcessChain();
            processChain.setName("Prozesskette " + i);
            processChain.setChain(workingProcessSteps);
            save(processChain);
        }

        List<ProcessStep> constantProcessSteps = this.em.createQuery("SELECT ps FROM ProcessStep ps WHERE ps.name LIKE '%K'").getResultList();
        ProcessChain processChainK = new ProcessChain();
        processChainK.setName("Prozesskette K");
        processChainK.setChain(constantProcessSteps);
        save(processChainK);

        List<ProcessStep> creatingProcessSteps = this.em.createQuery("SELECT ps FROM ProcessStep ps WHERE ps.name LIKE '%C'").getResultList();
        creatingProcessSteps.addAll(constantProcessSteps);
        ProcessChain processChainC = new ProcessChain();
        processChainC.setName("Prozesskette C");
        processChainC.setChain(creatingProcessSteps);
        save(processChainC);

        List<ProcessStep> destroyingProccessStep = this.em.createQuery("SELECT ps FROM ProcessStep ps WHERE ps.name LIKE '%D'").getResultList();
        constantProcessSteps.addAll(destroyingProccessStep);
        ProcessChain processChainD = new ProcessChain();
        processChainD.setName("Prozesskette D");
        processChainD.setChain(constantProcessSteps);
        save(processChainD);
    }

    /**
     * Create demo priorities.
     */
    public void createDemoPriorities() {
        String[] prioritiyNames = {"Sehr niedrig", "Niedrig", "Mittel", "Hoch", "Sehr hoch"};
        int[] priorityValues = {5, 10, 25, 50, 100};

        for (int i = 0; i < prioritiyNames.length; i++) {
            Priority priority = new Priority();
            priority.setName(prioritiyNames[i]);
            priority.setValue(priorityValues[i]);
            save(priority);
        }
    }

    /**
     * Create demo users of Workflow Managers
     */
    public void createDemoCarriersAndAssemblies() {
        List<CarrierType> carrierTypes = (List<CarrierType>) this.em.createQuery("select ct from CarrierType ct").getResultList();
        List<Stock> stock = (List<Stock>) this.em.createQuery("select s from Stock s").getResultList();
        String[] assemblyAlloys = {"Stahl", "Holz", "Kupfer", "Zink"};

        for (int i = 0; i < assemblyAlloys.length * 3; i++) {

            if (carrierTypes.size() == 0)
                continue;

            //Carrier
            Carrier carrier = new Carrier();
            carrier.setCarrierID("Carrier 0" + i);
            carrier.setCarrierType(carrierTypes.get(i % carrierTypes.size()));
            carrier.setLocation(stock.get(0));
            save(carrier);

            Assembly assembly = new Assembly();
            assembly.setAssemblyID("A01." + i + "." + (i % 3) * 100);
            assembly.setSampleCount(10);
            List<Carrier> carriers = new ArrayList<>();
            carriers.add(carrier);
            assembly.setCarriers(carriers);
            assembly.setAlloy(assemblyAlloys[i % assemblyAlloys.length]);
            assembly.setComment(null);
            save(assembly);
        }
    }

    /**
     * Create demo jobs.
     */
    public void createDemoJobs() {
        List<ProcessChain> processChains = this.em.createQuery("SELECT pc FROM ProcessChain pc").getResultList();
        List<Priority> priorities = this.em.createQuery("SELECT p FROM Priority p").getResultList();
        List<Assembly> assemblies = this.em.createQuery("SELECT a FROM Assembly a").getResultList();

        int counter = 1;
        int innerCounter = 0;
        for (ProcessChain processChain : processChains) {
            List<Procedure> emptyProcedures = new ArrayList<>();
            for (ProcessStep processStep : processChain.getChain()) {
                Procedure procedure = new Procedure();
                StateHistory stateHistory = new StateHistory();
                stateHistory.setStateExecs(new ArrayList<>());
                procedure.setProcessStep(processStep);
                procedure.setValues(new ArrayList<>());
                procedure.setStateHistory(stateHistory);
                List<Value> values = new ArrayList<>();
                for (Parameter parameter : processStep.getParameters()) {
                    Value value = new Value();
                    value.setParameter(parameter);
                    values.add(value);
                }
                procedure.setValues(values);
                emptyProcedures.add(procedure);
            }

            Job job = new Job();
            job.setName("Job " + counter);
            job.setAssemblies(null);
            job.setJobState(JobState.PENDING);
            job.setProcessChain(processChain);
            job.setProcedures(emptyProcedures);
            job.setPriority(priorities.get(counter % priorities.size()));

            //Starting first 3 jobs
            if (counter < 4) {
                List<Assembly> assemblyList = new ArrayList<>();
                assemblyList.add(assemblies.get(innerCounter));

                job.setAssemblies(assemblyList);
                job.setJobState(JobState.PROCESSING);

                innerCounter ++;
            }

            for (Procedure p : emptyProcedures) {
                for (Value v : p.getValues()) {
                    save(v);
                }
            }

            for (Procedure p : emptyProcedures) {
                save(p.getStateHistory());
            }

            for (Procedure p : emptyProcedures) {
                save(p);
            }

            //Starting jobs


            save(job);

            counter++;
        }
    }

    /**
     * Checks whether or not a list of users contains a user
     *
     * @param user  to be checked
     * @param users of all users to be checked
     * @return does the list contain the user?
     */
    public boolean contains(User user, List<User> users) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())
                    || u.getEmail().equals(user.getEmail())
                    || u.getId().equals(user.getId()))
                return true;
        }
        return false;
    }

    /**
     * Stores the given object in the PU using Transactions.
     *
     * @param o to be persisted
     */
    public void save(Object o) {
        em.getTransaction().begin();
        if (!em.contains(o)) {
            em.merge(o);
            em.flush();
        }
        em.getTransaction().commit();
    }
}
