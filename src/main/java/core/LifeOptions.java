package core;

import java.util.*;
import java.util.stream.Collectors;

///**
// * e.g.
// *
// * key 1:
// * "LIFE_PARAMS" --> map{
// *     "MAX_ITERATIONS": 20,
// *     "GRID_COLS": 5,
// *     "GRID_ROWS": 10
// * }
// *
// * key 2:
// * "LIFE_AGENT_PARAMS" --> map {
// *     Wolf.class   : LifeAgentParams(Wolf.class, 10, 1, 0.1, 1, ... )
// *     Deer.class   : LifeAgentParams(Deer.class, 5, 1, 0.1, 1, ... )
// *     Grass.class  : null
// * }
// * In the LIFE_AGENT_PARAMS map, if the key for a LifeAgent class is set to null, then the defaults for that LifeAgent
// * subclass will be loaded (from the subclass getDefaultParams() method, or from the Life class defaults if the former
// * is not implemented. If a class key is omitted from the params list it is considered to be unsupported.
// *
// */
public class LifeOptions {

    /* Level 1 keys */
    /* keys used in the params Map passed in constructor */
    public static final String KEY_LIFE_PARAMS = "LIFE_PARAMS";
    public static final String KEY_LIFEAGENT_PARAMS = "LIFE_AGENT_PARAMS";

    private Map<String, Map> root = new HashMap<>();
    private Map<Class, LifeAgentParams> lifeAgentParams = new HashMap<>();

    // =================================================================================================================
    // DEFAULTS
    // =================================================================================================================

    /** @brief default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRID_N = 5;

    // =================================================================================================================
    // FIELDS
    // =================================================================================================================

    private int maximumIterations = DEFAULT_MAX_ITERATIONS;

    private int gridRows = DEFAULT_GRID_N;

    private int gridCols = DEFAULT_GRID_N;

    public LifeOptions() { this(null); }

    public LifeOptions(List<LifeAgentParams> agentParams) {
        root.put(KEY_LIFEAGENT_PARAMS, lifeAgentParams);
        if (agentParams != null) {
            for (LifeAgentParams param : agentParams) {
                lifeAgentParams.put(param.getAgentType(), param);
            }
        }
    }

    /**
     * @return read-only list of supported agents
     */
    public List<Class<? extends LifeAgent>> getSupportedAgents() {
        List<Class<? extends LifeAgent>> list = (List<Class<? extends LifeAgent>>) root.get(KEY_LIFEAGENT_PARAMS)
                .keySet()
                .stream()
                .collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    public Map<Class<?extends LifeAgent>, LifeAgentParams> getLifeAgentParams() {
        return root.get(KEY_LIFEAGENT_PARAMS);
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

    public LifeAgentParams getOptionsForAgent(Class<?extends LifeAgent> type) {
        return lifeAgentParams.get(type);
    }
}
