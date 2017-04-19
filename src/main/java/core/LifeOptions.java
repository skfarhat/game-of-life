package core;

import core.exceptions.LifeException;
import core.exceptions.LifeImplementationException;
import core.interfaces.Consumable;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class LifeOptions {

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
     *  map determining which typ of LifeAgent can consume which type,
     * e.g.  Wolf.class --&gt; [Deer.class, Sheep.class...]
     */
    private final Map<Class<?extends LifeAgent>, List<Class<?extends LifeAgent>>> consumeRules = new HashMap<>();

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
        options.addConsumeRule(Wolf.class, Deer.class);
        options.addConsumeRule(Deer.class, Grass.class);
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
            consumeRules.put(lifeAgentClass, new ArrayList<>());
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

    /**
     * @return unmodifiable map of "Consumes Class" --&gt; "List of Consumables"
     */
    public Map<Class<?extends LifeAgent>, List<Class<?extends LifeAgent>>> getConsumeRules() {
        return Collections.unmodifiableMap(consumeRules);
    }

    /**
     *
     * @param cls consuming class
     * @param consumableClass consumableclass
     * @return
     * @throws LifeException
     */
    public boolean addConsumeRule(Class<?extends LifeAgent> cls, Class<?extends LifeAgent> consumableClass) throws LifeException {
        if (false == agentTypeIsSupported(cls)
                || false == agentTypeIsSupported(consumableClass)) {
            // the consumes class is not supported..
            Class unsupportedClass = (true == agentTypeIsSupported(cls)) ? consumableClass : cls;
            throw new LifeException(String.format("Agent %s type not supported!", unsupportedClass.getName()));
        }

        // prevent consuming oneself
        if (cls.equals(consumableClass)) {
            throw new LifeException("Cannot add rule where LifeAgent can consume itself");
        }
        // don't re-add a consumable class if it is already there
        else if (false == consumeRules.get(cls).contains(consumableClass)) {
            return consumeRules.get(cls).add(consumableClass);
        }
        else
            return false;
    }

    /**
     *
     * @param cls consuming class
     * @param list list of consumable classes
     * @return true if the addition fo the list of consume rules was successful
     * @throws LifeException if any of the classes in @param list or @param cls are unsupported in LifeOptions. If they have
     * no LifeAgentOptions entry.
     */
    public boolean addConsumeRules(Class<?extends LifeAgent> cls, List<Class<?extends LifeAgent>>  list) throws LifeException {
        return addConsumeRules(cls, (Class<? extends LifeAgent>[]) list.toArray());
    }

    /**
     *
     * @param cls consuming class
     * @param list list of consumable classes
     * @return true if the addition fo the list of consume rules was successful
     * @throws LifeException if any of the classes in @param list or @param cls are unsupported in LifeOptions. If they have
     * no LifeAgentOptions entry.
     */
    public boolean addConsumeRules(Class<?extends LifeAgent> cls, Class<?extends LifeAgent>...list) throws LifeException {
        boolean success = true;
        for (Class<?extends LifeAgent> c :  list) {
            success &= addConsumeRule(cls, c);
        }
        return success;
    }


    /**
     * @param cls consuming class
     * @param consumableClass
     * @return true if the removal of the consume rule was successful
     * @throws LifeException
     */
    public boolean removeConsumeRule(Class<?extends LifeAgent> cls, Class<?extends LifeAgent> consumableClass) throws LifeException {
        if (false == agentTypeIsSupported(cls)
                || false == agentTypeIsSupported(consumableClass)) {
            Class unsupportedClass = (true == agentTypeIsSupported(cls)) ? consumableClass : cls;
            throw new LifeException(String.format("Agent %s type not supported!", unsupportedClass.getName()));
        }

        // if the rule to remove doesn't exist
        if (true == consumeRules.get(cls).contains(consumableClass)) {
            return consumeRules.get(cls).remove(consumableClass);
        }
        return false;
    }

    /**
     *
     * @param cls consuming class
     * @param list list of consumable classes
     * @return true if the removal of the list of consume rules was successful
     * @throws LifeException
     */
    public boolean removeConsumeRules(Class<?extends LifeAgent> cls, List<Class<?extends LifeAgent>> list) throws LifeException {
        boolean success = true;
        for (Class<?extends LifeAgent> c : list) {
            success &= removeConsumeRule(cls, c);
        }
        return success;
    }

    public boolean agentTypeIsSupported(Class c) {
        return lifeAgentParams.containsKey(c);
    }

    public List<Consumable> filterConsumablesForAgent(Iterator<Consumable> it, LifeAgent agent) {
        final List<Class<? extends LifeAgent>> consumableClasses = getConsumeRules().get(agent.getClass()); // list of classes consumable by agent
        List<Consumable> consumables = new ArrayList<>();
        while(it.hasNext()){
            Consumable c = it.next();
            if (consumableClasses.contains(c.getClass()))
                consumables.add(c);
        }
        return consumables;
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }
}
