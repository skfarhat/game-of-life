package core;

import core.exceptions.LifeException;
import core.exceptions.LifeImplementationException;
import core.exceptions.UnsupportedAgentException;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 */
public class LifeOptions {

    /**
     * @brief logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    // =================================================================================================================
    // DEFAULTS
    // =================================================================================================================

    /**  default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /**  default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRID_N = 5;

    // =================================================================================================================
    // FIELDS
    // =================================================================================================================

    /**
     * Map of ConsumeRules that will govern who consumes whom.
     * the Boolean map values are redundant effectively not used. Map was chosen over array for faster get() operations
     */
//    private final Map<ConsumeRule, Boolean> consumeRules = new HashMap<>();
//    private final Map<Class<?extends LifeAgent>, List<ConsumeRule>> consumeRules = new HashMap<>();
    private final ConsumeRules consumeRules = new ConsumeRules();

    private Map<Class<?extends LifeAgent>, LifeAgentOptions> lifeAgentParams = new HashMap<>();

    private int maximumIterations = DEFAULT_MAX_ITERATIONS;

    private int gridRows = DEFAULT_GRID_N;

    private int gridCols = DEFAULT_GRID_N;

    // =================================================================================================================
    // METHODS
    // =================================================================================================================

    /**
     * factory method returning a defaultLifeOptions instance with Wolf, Deer and Grass
     * @return
     */
    public static LifeOptions createDefaultLifeOptions() throws LifeException {
        LifeOptions options = new LifeOptions(Wolf.class, Deer.class, Grass.class);
        options.addConsumeRule(new ConsumeRule(Wolf.class, Deer.class));
        options.addConsumeRule(new ConsumeRule(Deer.class, Grass.class));
        return options;
    }

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

    public LifeOptions(LifeAgentOptions... opts) {
        if (opts != null)
            init(Arrays.stream(opts).collect(Collectors.toList()));
    }

    public LifeOptions(Class<?extends LifeAgent>... cls)  {
        if (null == cls)
            return;
        List<LifeAgentOptions> opts = new ArrayList<>();
        for (Class<?extends LifeAgent> c : cls) {
            opts.add(new LifeAgentOptions(c));
        }
        init(opts);
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
            lifeAgentParams.put(lifeAgentClass, opt);
        }
    }

    /** @return read-only list of supported agents */
    public List<Class<? extends LifeAgent>> getSupportedAgents() {
        List<Class<? extends LifeAgent>> list = lifeAgentParams.keySet().stream().collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    public int getMaximumIterations() {
        return maximumIterations;
    }

    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridCols() {
        return gridCols;
    }

    public LifeAgentOptions getOptionsForAgent(Class<?extends LifeAgent> type) {
        return lifeAgentParams.get(type);
    }

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
     *
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
     *
     * @param list list of consumeRules to remove
     * @return true if the removal of the list of consume rules was successful
     * @throws LifeException
     */
    public boolean removeConsumeRules(ConsumeRule... list) {
        return consumeRules.removeAll(Arrays.asList(list));
//        boolean success = true;
//        for (ConsumeRule cr : list) {
//            success &= removeConsumeRule(cr);
//        }
//        return success;
    }

    public boolean agentTypeIsSupported(Class c) {
        return lifeAgentParams.containsKey(c);
    }

    public List<Class<?extends LifeAgent>> getConsumableClassesForAgent(LifeAgent agent) {
        return consumeRules.getConsumableClassesForAgent(agent.getClass());
    }

    public boolean containsConsumeRule(ConsumeRule cr) {
        exceptionIfInvalidConsumeRule(cr);

        return consumeRules.contains(cr);
//        return consumeRules.get(cr.getConsumer()).contains(cr);
    }

    /** returns true if the agents in the consume rule are supported */
    private boolean validConsumeRule(ConsumeRule cr) {
        return agentTypeIsSupported(cr.getConsumable())
                && agentTypeIsSupported(cr.getConsumer()) ;
    }

    /** returns true if the agents in the consume rule are supported */
    private void exceptionIfInvalidConsumeRule(ConsumeRule cr) {
        if (false == validConsumeRule(cr)) {
            Class unsupportedClass = (true == agentTypeIsSupported(cr.getConsumable())) ? cr.getConsumer(): cr.getConsumable();
            throw new UnsupportedAgentException(unsupportedClass);
        }
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }
}
