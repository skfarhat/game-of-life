package core;

import core.exceptions.LifeException;
import core.exceptions.LifeImplementationException;
import core.exceptions.LifeRuntimeException;
import core.exceptions.UnsupportedAgentException;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * class encapsulating all configurable parameters to a Life simulation
 */
public class LifeOptions {

    /**
     * when an agent consumes another there are many way to define what energy the consumer gains. This enum allows users
     * to configure through the API which energy-gain behaviour is adopted.
     */
    public enum ConsumeImplementation {
        /** the consumer agent gains a fixed energy defined in LifeAgentOptions */
        FIXED_ENERGY,

        /** the consumer agent gains the consumable's energy */
        CONSUMABLE_ENERGY,

        /** the consumer agent gains the consumable's energy with a configurable cap (upper limit value) */
        CAPPED_CONSUMABLE_ENERGY
    }

    /** logger */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    // =================================================================================================================
    // DEFAULTS
    // =================================================================================================================

    /**  default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /**  default energy gained by wolf and deer when they consume other core.agents */
    public static final int DEFAULT_GRID_N = 5;

    /** default consume implementation */
    public static final ConsumeImplementation DEFAULT_CONSUME_IMPLEMENTATION = ConsumeImplementation.FIXED_ENERGY;

    /** default cap (upper limit) used when ConsumeImplementation = CAPPED_CONSUMABLE_ENERGY */
    public static final int DEFAULT_CONSUME_ENERGY_CAP = 10;

    // =================================================================================================================
    // FIELDS
    // =================================================================================================================

    /**
     * map of ConsumeRules that will govern who consumes whom.
     * the Boolean map values are redundant effectively not used. Map was chosen over array for faster get() operations
     */
    private final ConsumeRules consumeRules = new ConsumeRules();

    /** map mapping each LifeAgent class type to its LifeAgentOptions */
    private Map<Class<?extends LifeAgent>, LifeAgentOptions> lifeAgentParams = new HashMap<>();

    /** maximum number of iterations to run the system for (0 and -1 mean indefinitely) */
    private int maximumIterations = DEFAULT_MAX_ITERATIONS;

    /** number of rows in the grid used by Life */
    private int gridRows = DEFAULT_GRID_N;

    /** number of columns in the grid used by Life */
    private int gridCols = DEFAULT_GRID_N;

    /** the consume implementation adopted by Life - defaults to fixed energy */
    private ConsumeImplementation consumeImplementation = DEFAULT_CONSUME_IMPLEMENTATION;

    /** the cap (upper limit) used when consumeImplementation is set to CAPPED_CONSUMABLE_ENERGY */
    private int consumableEnergyCap = DEFAULT_CONSUME_ENERGY_CAP;

    // =================================================================================================================
    // METHODS
    // =================================================================================================================

    /** default constructor */
    public LifeOptions() {
        this((Class<? extends LifeAgent>[]) null);
    }

    /**
     *  constructor called by all others
     * @param opts
     * @throws LifeImplementationException
     */
    public LifeOptions(List<LifeAgentOptions> opts) {
        init(opts);
    }

    /**
     * constructor with LifeAgentOptions passed
     * @param opts
     */
    public LifeOptions(LifeAgentOptions... opts) {
        if (opts != null)
            init(Arrays.stream(opts).collect(Collectors.toList()));
    }

    /**
     * constructor with LifeAgent class types passed
     * @param cls
     */
    public LifeOptions(Class<?extends LifeAgent>... cls)  {
        if (null != cls) {
            List<LifeAgentOptions> opts = new ArrayList<>();
            for (Class<?extends LifeAgent> c : cls) {
                opts.add(new LifeAgentOptions(c));
            }
            init(opts);
        }
    }

    /**
     * called by constructors for initialisation (more flexible than calling another constructor with 'this')
     * because init can be called anywhere in the constructor (beginning, end) as opposed to calling this(..) which must
     * be the first thing.
     * @param opts
     */
    private final void init(List<LifeAgentOptions> opts) {
        if (null == opts)
            return;

        for (LifeAgentOptions opt : opts) {
            Class<?extends LifeAgent> lifeAgentClass = opt.getAgentType();
            // if the class is already in the lifeAgentParams map then the user must have mistakenly (or purposefully)
            // passed the same class type s
            if (lifeAgentParams.containsKey(lifeAgentClass))
                throw new LifeRuntimeException(String.format("Cannot pass the same LifeAgent class (%s) multiple times to LifeOptions.", lifeAgentClass.getName()));
            lifeAgentParams.put(lifeAgentClass, opt);
        }
    }

    /** @return read-only list of supported core.agents */
    public List<Class<? extends LifeAgent>> getSupportedAgents() {
        List<Class<? extends LifeAgent>> list = lifeAgentParams.keySet().stream().collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    /** get the maximum number of iterations the system will run. values 0 and -1 mean run indefinitely */
    public int getMaximumIterations() {
        return maximumIterations;
    }

    /** set the maximum number of iterations the system will run. values 0 and -1 mean run indefinitely */
    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    /** get the number of rows in the grid used in Life */
    public int getGridRows() {
        return gridRows;
    }

    /** get the number of columns in the grid used in Life */
    public int getGridCols() {
        return gridCols;
    }

    /** set the number of rows in the grid used in Life */
    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    /** set the number of columns in the grid used in Life */
    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    /**
     * get the LifeAgentOptions class for the passed agent
     * @param type the agent type
     * @return LifeAgentOptions instance associated with the given agent type
     */
    public LifeAgentOptions getOptionsForAgent(Class<?extends LifeAgent> type) {
        return lifeAgentParams.get(type);
    }

    /** @return consumeRules member */
    public ConsumeRules getConsumeRules() {
        return consumeRules;
    }

    /**
     *
     * @param cr
     * @return
     * @throws LifeException
     */
    public boolean addConsumeRule(ConsumeRule cr) throws LifeException {
        exceptionIfInvalidConsumeRule(cr);
        return consumeRules.add(cr);
    }

    /**
     * @param list of consume rules to add
     * @return true if the addition of the list of consume rules was successful
     * @throws LifeException if any of the classes in @param list or @param cls are unsupported in LifeOptions. If they have
     * no LifeAgentOptions entry.
     */
    public boolean addConsumeRules(ConsumeRule... list) throws LifeException {
        boolean success = true;
        for (ConsumeRule cr : list) {
            success &= addConsumeRule(cr);
        }
        return success;
    }

    /**
     * @param cr consumeRule to remove
     * @return true if the removal of the consume rule was successful
     * @throws LifeException
     */
    public boolean removeConsumeRule(ConsumeRule cr) {
        exceptionIfInvalidConsumeRule(cr);
        return consumeRules.remove(cr);
    }

    /**
     * set the consume implementation adopted
     * @param consumeImplementation enum value to set the consume implementation to
     */
    public void setConsumeImplementation(ConsumeImplementation consumeImplementation) {
        this.consumeImplementation = consumeImplementation;
    }

    /** set the consumableEnergyCap */
    public void setConsumableEnergyCap(int consumableEnergyCap) {
        this.consumableEnergyCap = consumableEnergyCap;
    }

    /** get the consumableEnergyCap */
    public int getConsumableEnergyCap() {
        return consumableEnergyCap;
    }

    /**
     * @return the adopted consume implementation
     */
    public ConsumeImplementation getConsumeImplementation() {
        return consumeImplementation;
    }

    /**
     * @param list list of consumeRules to remove
     * @return true if the removal of the list of consume rules was successful
     * @throws LifeException
     */
    public boolean removeConsumeRules(ConsumeRule... list) {
        return consumeRules.removeAll(Arrays.asList(list));
    }

    /**
     * @return true if the given LifeAgent class type is supported. This is only true if that class type was passed
     * to this object in the constructor or in an appropriate LifeAgentOptions object
     */
    public boolean agentTypeIsSupported(Class<?extends LifeAgent> c) {
        return lifeAgentParams.containsKey(c);
    }

    public List<Class<?extends LifeAgent>> getConsumableClassesForAgent(LifeAgent agent) {
        return consumeRules.getConsumableClassesForAgent(agent.getClass());
    }

    public boolean containsConsumeRule(ConsumeRule cr) {
        exceptionIfInvalidConsumeRule(cr);
        return consumeRules.contains(cr);
    }

    /** @return true if the core.agents in the consume rule are supported */
    private boolean validConsumeRule(ConsumeRule cr) {
        return agentTypeIsSupported(cr.getConsumable())
                && agentTypeIsSupported(cr.getConsumer()) ;
    }

    /** @return true if the core.agents in the consume rule are supported */
    private void exceptionIfInvalidConsumeRule(ConsumeRule cr) {
        if (false == validConsumeRule(cr)) {
            Class unsupportedClass = (true == agentTypeIsSupported(cr.getConsumable())) ? cr.getConsumer(): cr.getConsumable();
            throw new UnsupportedAgentException(unsupportedClass);
        }
    }
}
