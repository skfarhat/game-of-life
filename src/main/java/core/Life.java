package core;

import core.actions.*;
import core.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @class Life class created for each simulation to step. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life implements LifeGetter {

    // ===========================================================================================
    // List of keys in the options map
    // ===========================================================================================

    /* keys used in the params Map passed in constructor */
    public static final String KEY_LIFE_PARAMS = "LIFE_PARAMS";
    public static final String KEY_LIFEAGENT_PARAMS = "LIFE_AGENT_PARAMS";

    /* keys used in the Life params Map */
    public static final String KEY_LIFE_MAX_ITERATIONS = "MAX_ITERATIONS";
    public static final String KEY_LIFE_GRID_ROWS = "GRID_ROWS";
    public static final String KEY_LIFE_GRID_COLS = "GRID_COLS";

    // ===========================================================================================
    // Defaults
    // ===========================================================================================

    /** @brief default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRID_N = 5;

    /** @brief default initial energy for LifeAgents*/
    public static final int DEFAULT_E0 = 5;

    /** @brief default initial number of instances of that LifeAgent */
    public static final int DEFAULT_I0 = 5;

    /** @brief default reproduction rate for LifeAgents */
    private static final Double DEFAULT_R = 0.1;

    /** @brief default energy lost by a LifeAgent when it is consumed - only applicable in some implementations of consume*/
    private static final Integer DEFAULT_E_LOSS = 1;

    /** @brief default energy gained when a LifeAgent consumes another */
    public static final int DEFAULT_E_GAIN = 2;

    /** @brief default energy decrease for Agents when they age */
    public static final int DEFAULT_AGE = 1;

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

    /**
     * e.g.
     *
     * key 1:
     * "LIFE" --> map{
     *     "MAX_ITERATIONS": 20,
     *     "GRID_COLS": 5,
     *     "GRID_ROWS": 10
     * }
     *
     * key 2:
     * "LIFE_AGENT" --> map {
     *     Wolf.class   : LifeAgentParams(Wolf.class, 10, 1, 0.1, 1, ... )
     *     Deer.class   : LifeAgentParams(Deer.class, 5, 1, 0.1, 1, ... )
     *     Grass.class  : LifeAgentParams(Grass.class, 5, 1, 1.0, 1, ... )
     * }
     *
     */
    Map<String, Map> options;

    /**
     * @brief map determining which typ of LifeAgent can consume which type,
     * e.g.  Wolf.class -->  [Deer.class, Sheep.class...]
     */
    private final Map<Class, List<Class>> CONSUME_RULES = new HashMap<Class, List<Class>>();

    /** @brief array of classes supported by this implementation of life */
    private final Class<?extends LifeAgent>[] SUPPORTED_AGENTS = new Class[]{Grass.class, Wolf.class, Deer.class};

//    public final Map<Class<?extends LifeAgent>, LifeAgentParams> configOptions = new HashMap<>();

    /** @brief the number of times step has been called and returned a non-empty list of Actions */
    private int stepCount;


    /** @brief the grid containing all cells on which the agents will be placed */
    private final Grid<LifeCell> grid;

    /** @brief list of all of the agents in Life */
    private final List<Agent> agents;

    // ===========================================================================================
    // METHODS
    // ===========================================================================================

    /** @brief default constructor, calls other constructor and initialises fields to their defaults */
    public Life() throws LifeException, IllegalArgumentException { this(null);}

    // AgentIsDeadException --> invalid initial energy
    // InvalidPositionException --> distribution of the agents failed.
    /** @brief constructor taking in a params map specifying the input parameters */
    public Life(Map<String, Map> opts) throws LifeException, IllegalArgumentException {

        if (opts == null)
            opts = new HashMap<>();
        this.options = opts;

        // -------------------------------------------------------------------------------------------------------------
        // Constructor overview:
        // [1] handle consume rules
        // [2] handle LifeAgent parameters
        // [3] handle Life parameters
        // [4] create grid
        // [5] create agents and distribute them on the grid
        // -------------------------------------------------------------------------------------------------------------

        // [1] Consume Rules: dictate who is allowed to consume whom
        CONSUME_RULES.put(Wolf.class, new ArrayList<Class>(){{add(Deer.class );}}); // Wolf eats Deer
        CONSUME_RULES.put(Deer.class, new ArrayList<Class>(){{add(Grass.class);}}); // Deer eats Grass

        // [2] LifeAgent params
        try {
            Map<Class<?extends LifeAgent>, LifeAgentParams> lifeAgentParams = opts.get(KEY_LIFEAGENT_PARAMS);

            for (Class<? extends LifeAgent> agentType : SUPPORTED_AGENTS) {

                // LifeAgentParams are taken from whichever of the below occurs first:
                // 1 - user defined LifeAgentParams are used
                // 2 - subclass defined default LifeAgentParams are used
                // 3 - Life defined LifeAgentParams are used

                if (lifeAgentParams == null)
                    lifeAgentParams = new HashMap<>();

                LifeAgentParams typeParams = null;
                Method methodGetDefaultParams = null;

                // (2) get the method to be used by reflection
                try { methodGetDefaultParams = agentType.getMethod(LifeAgent.METHOD_NAME_GET_DEFAULT_PARAMS);}
                catch (NoSuchMethodException e) { }

                // (1) user defined
                if (lifeAgentParams.containsKey(agentType)) {
                    typeParams = lifeAgentParams.get(agentType);
                }
                // (2) LifeAgent subclass defined
                else if (null != methodGetDefaultParams) {
                    typeParams = (LifeAgentParams) methodGetDefaultParams.invoke(null);
                }
                // (3) Life class defined
                else {
                    typeParams = new LifeAgentParams(agentType, DEFAULT_AGE, DEFAULT_E0, DEFAULT_R, DEFAULT_I0, DEFAULT_E_GAIN, DEFAULT_E_LOSS);
                }
                lifeAgentParams.put(agentType, typeParams);
            }
            options.put(KEY_LIFE_PARAMS, lifeAgentParams);
        } catch (IllegalAccessException e) {
            // if we don't have right access to the method invoked (e.g. the method is private)
            e.printStackTrace();
            throw new LifeImplementationException(e.getMessage());
        } catch (InvocationTargetException e) {
            // if the underlying method throws an exception
            e.printStackTrace();
            throw new LifeImplementationException(e.getMessage());
        }

        // [3] handle Life params
        Map<String, Number> lifeParams = (Map<String, Number>) opts.get(KEY_LIFE_PARAMS);
        if (lifeParams == null)
            lifeParams = new HashMap<>();

        // check the sanity of the params in lifeParams
        if (false == lifeParams.containsKey(KEY_LIFE_MAX_ITERATIONS))
            lifeParams.put(KEY_LIFE_MAX_ITERATIONS, DEFAULT_MAX_ITERATIONS);

        if (lifeParams.containsKey(KEY_LIFE_GRID_COLS))
            exceptionIfNegative(lifeParams.get(KEY_LIFE_GRID_COLS).intValue());
        else
            lifeParams.put(KEY_LIFE_GRID_COLS, DEFAULT_GRID_N);

        if (lifeParams.containsKey(KEY_LIFE_GRID_ROWS))
            exceptionIfNegative(lifeParams.get(KEY_LIFE_GRID_ROWS).intValue());
        else
            lifeParams.put(KEY_LIFE_GRID_COLS, DEFAULT_GRID_N);


        // Create Life in 7 days
        // ---------------------

        // [4] create grid
        int gridRows = lifeParams.get(KEY_LIFE_GRID_ROWS).intValue();
        int gridCols = lifeParams.get(KEY_LIFE_GRID_COLS).intValue();
        grid = GridLifeCellFactory.createGridCell(gridRows, gridCols); // create a square Grid

        // [5] create agents and distribute
        agents = new ArrayList<>();
        createAndDistributeAgents(gridRows, gridCols);

    }

    /**
     * @param a
     * @return
     */
    public boolean addAgent(Agent a) {
        if (false == grid.pointInBounds(a.getPos())) return false;
        try {return grid.get(a.getPos()).addAgent(a) && agents.add(a);}
        catch (InvalidPositionException e) { return false; } // this shouldn't happen because we already checked }
    }

    /**
     *
     * @param agents
     * @return
     */
    public boolean addAgents(List<Agent> agents) {
        boolean success = true;
        for (Agent a : agents) {
            success &= addAgent(a);
        }
        return success;
    }

    /**
     * @brief remove the passed Agent @param a from the local agents list and from the cell's agents list
     * @return true if the removal was successful from both lists, false otherwise
     */
    public boolean removeAgent(Agent a)  {
        if (false == grid.pointInBounds(a.getPos())) return false;
        try { return grid.get(a.getPos()).removeAgent(a) && agents.remove(a);}
        catch (InvalidPositionException e) { return false; } // this shouldn't happen because we already checked
    }

    /**
     * @brief removes all agents from the provided list. Failure to remove an agent in the list will make the method return false.
     * @param agents list of agents to remove
     * @return false if any one of the agents in the list could not be removed
     */
    public boolean removeAgents(List<Agent> agents) {
        boolean success = true;
        for (Agent a : agents) {
            success &= removeAgent(a);
        }
        return success;
    }

    /**
     * @brief choose an agent at random to act
     * @throws InvalidPositionException
     * @throws AgentIsDeadException
     * @return the stepCount index or -1 if there was nothing to do
     */
    public List<Action> step() throws InvalidPositionException, AgentIsDeadException {

        List<Action> actions = new ArrayList<>();

        // guard - nothing to do
        if (agents.size() < 1) {
//            System.out.println("nothing to do ");
            return actions;
        }

        // choose an agent at random
        int randI = Utils.randomPositiveInteger(agents.size());
        LifeAgent chosen = (LifeAgent) agents.get(randI);
        if (!chosen.isAlive())
            System.out.println("We chose a non-alive agent!"); // TODO(sami): throw an exception

        // Wolves and Deers
        if ((chosen instanceof Wolf) || (chosen instanceof Deer)) {
            // -------
            // Move
            // -------
            Point2D srcPoint = new Point2D(chosen.getPos()); // make a new copy of the src point
            Point2D nextPoint = findAdjacentPointInGrid(chosen.getPos());
            Cell nextCell = grid.get(nextPoint);
            Action move = new Move(chosen, srcPoint, nextPoint);
            actions.add(move);
            processMoveAction((Move) move);

            // -------
            // Consume
            // -------

            List<Consumable> consumableAgents = filterConsumablesForAgent(nextCell.getAgents(), chosen);

            // choose one at random to consume
            if (consumableAgents.size() > 0) {
                int index = Utils.randomPositiveInteger(consumableAgents.size());
                Consumable agentToConsume = consumableAgents.get(index);
                Action consume = new Consume(chosen, agentToConsume);
                actions.add(consume);
                processConsume((Consume) consume);
            }

            // ---------
            // Reproduce
            // ---------

            double rWolf = lifeAgentOpts().get(Wolf.class).getReproductionRate();
            double rDeer = lifeAgentOpts().get(Deer.class).getReproductionRate();
            double rAgent = (chosen instanceof Wolf)? rWolf : rDeer;
            boolean willReproduce = Utils.getRand().nextDouble() < rAgent;
            if (willReproduce) {
                LifeAgent baby = chosen.reproduce();
                Action reproduce = new Reproduce(chosen, baby);
                actions.add(reproduce);
                processReproduce((Reproduce) reproduce);
            }

            // ------------
            // EnergyChange
            // ------------

            int ageWolf = lifeAgentOpts().get(Wolf.class).getAgeBy();
            int ageDeer = lifeAgentOpts().get(Deer.class).getAgeBy();
            int ageBy = (chosen instanceof Wolf)? ageWolf: ageDeer;
            Action age = new EnergyChange(chosen, -ageBy);
            actions.add(age);
            processAgeAction((EnergyChange) age);

            // find the cell's dead agents
            // double cast to change List<LifeAgent> to List<Agent>
            List<Agent> deadAgents = (List<Agent>) (List) ((LifeCell)nextCell).findDeadAgents();

            // removes dead agents from the local agents list and from the cells' respective agents lists
            if (false == removeAgents(deadAgents)) {
                System.out.println("Failed to remove some agents.. ");
            }
            // TODO(sami); consider sending events for all new dead agents,
        }

        else if (chosen instanceof Grass) {
            Point2D nextPoint = findAdjacentPointInGrid(chosen.getPos());

            // ---------
            // Reproduce
            // ---------

            if (!((LifeCell)grid.get(nextPoint)).isContainsGrass()) {
                LifeAgent babyGrass = chosen.reproduce();
                babyGrass.setPos(nextPoint);
                Action reproduce = new Reproduce(chosen, babyGrass);
                actions.add(reproduce);
                processReproduce((Reproduce) reproduce);
            }

            // -----------------
            // Age (gain energy)
            // -----------------
            int ageGrass = lifeAgentOpts().get(Grass.class).getAgeBy();
            Action energyGain = new EnergyChange(chosen, ageGrass);
            actions.add(energyGain);
            processAgeAction((EnergyChange) energyGain);
        }

        stepCount++;
        return actions;
    }

    /**
     * @return total number number of created agents
     * @throws AgentIsDeadException if the initial energy given to any of the agents is negative
     */
    private int createAndDistributeAgents(int rows, int columns) throws LifeException {

        int nCreated = 0;
        for (Class<? extends LifeAgent> agentType : SUPPORTED_AGENTS) {
            // get the initial energy
            // get the initial number

            LifeAgentParams lifeAgentParams = (LifeAgentParams) lifeAgentOpts().get(agentType);
            int I0 = lifeAgentParams.getInitialCount(); // number of instances
            int E0 = lifeAgentParams.getInitialEnergy(); // initial energy

            // for each type of agent
            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I0; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(rows, columns); // TODO(sami): should this be the other way around ?
                    Agent agent = agentType.getConstructor(Point2D.class, Integer.class).newInstance(p, E0);
                    addAgent(agent);
                    nCreated++;
                }
            } catch (ReflectiveOperationException exc) {
                exc.printStackTrace();
                throw new LifeException("Implementation error: could not create instance of Agent " + agentType.getName() +  "\n"
                        + exc.getMessage());
            }
        }
        return nCreated;
    }

    private void processActions(List<Action> actions) throws InvalidPositionException, AgentIsDeadException {
        for (Action action: actions) {
            if (action instanceof EnergyChange) {
                processAgeAction((EnergyChange) action);
            }
            else if (action instanceof Consume) {
                processConsume((Consume) action);
            }
            else if (action instanceof Reproduce) {
                processReproduce((Reproduce) action);
            }
            else if (action instanceof  Move) {
                processMoveAction((Move) action);
            }
        }
    }

    private void processAgeAction(EnergyChange age) throws AgentIsDeadException {
        age.getAgent().changeEnergyBy(age.getEnergyDelta());
    }

    private void processMoveAction(Move action) throws InvalidPositionException {
        Cell nextCell = grid.get(action.getTo());
        grid.moveAgentToCell(action.getAgent(), nextCell);
    }

    private void processReproduce(Reproduce action) throws InvalidPositionException {
        Iterator<LifeAgent> babies = action.getBabies();
        while(babies.hasNext()) {
            LifeAgent baby = babies.next();
            addAgent(baby);
        }
    }

    private void processConsume(Consume action) throws AgentIsDeadException {
        if (false == action.getConsumables().hasNext()) {
            // nothing to consume
            // TODO(sami): put some DEBUG info here
            return;
        }

        final int choiceOfImplementation = 3;
        final int GAIN_CAP = 10;

        Consumable consumable = action.getConsumables().next();
        LifeAgent consumingAgent = action.getAgent();

        // defines the energy gained by the consuming agent
        int energyGain = 0;
        int consumableEnergy = consumable.getEnergy();
        int energyLoss = consumableEnergy; // defaults to consumable's energy but isn't the case for everything (e.g. Grass)

        switch(choiceOfImplementation) {
            case 2:
                // Implementation 2:
                // the consuming agent gains the energy of the consumable
                energyGain = energyLoss;
                break;
            case 3:
                // Implementation 3:
                // the consuming agents gains energy of its consumable with a cap on the energy gained
                energyGain = (consumableEnergy < GAIN_CAP)? consumableEnergy : GAIN_CAP;
                break;
            case 1:
            default:
                // Implementation 1 and the default :
                // the consuming agent gains a fixed energy defined by E_{X}_GAIN
                int eWolfGain = lifeAgentOpts().get(Wolf.class).getEnergyGained();
                int eDeerGain = lifeAgentOpts().get(Deer.class).getEnergyGained();
                int eGrassGain = lifeAgentOpts().get(Grass.class).getEnergyGained();
                energyGain = (consumable instanceof Wolf)? eWolfGain : (consumable instanceof Deer)? eDeerGain: eGrassGain;
        }


        if (consumable instanceof Grass) {
            int eGrassLost = lifeAgentOpts().get(Grass.class).getEnergyLost();
            energyLoss = Math.min(eGrassLost, consumableEnergy);
        }

        // consume the consumable and increase energy by 'energyGain'
        // the try-catch is redundant we already tested for it
        try { consumingAgent.consumeBy(consumable, energyLoss); }
        catch (ConsumableOutOfEnergy exc) { exc.printStackTrace(); }

        consumingAgent.changeEnergyBy(energyGain);
    }

    private Point2D findAdjacentPointInGrid(Point2D p) throws InvalidPositionException {
        return grid.randomAdjacentPoint(p);
    }

    /**
     * @param val that must be non-negative
     * @throws IllegalArgumentException if val is negative
     */
    private void exceptionIfNegative(int val) throws IllegalArgumentException {
        if (val < 0) throw new IllegalArgumentException();
    }

    /**
     * @param val that must be in 0-1 range
     * @throws IllegalArgumentException if val is out of the range
     */
    private void exceptionIfOutOfRange(double val) throws IllegalArgumentException {
        if (val < 0 || val > 1)
            throw new IllegalArgumentException("Double values must be between 0 and 1: " + val + " given.");
    }

    private List<Consumable> filterConsumablesForAgent(Iterator<Consumable> it, Agent agent) {
        final List<Class> consumableClasses = CONSUME_RULES.get(agent.getClass()); // list of classes consumable by agent
        List<Consumable> consumables = new ArrayList<>();
        while(it.hasNext()){
            Consumable c = it.next();
            if (consumableClasses.contains(c.getClass()))
                consumables.add(c);
        }
        return consumables;
    }

    /**
     * @return map containing life options
     */
    private Map<String, Number> lifeOpts() { return (Map<String, Number>) options.get(KEY_LIFE_PARAMS); }

    /**
     * @return map containing life options
     */
    private Map<Class, LifeAgentParams> lifeAgentOpts() { return options.get(KEY_LIFEAGENT_PARAMS); }

    /**
     * @return
     */
    @Override
    public List<Agent> getAgents() {
        return agents;
    }

    /** @return the current stepCount */
    public int getStepCount() {
        return stepCount;
    }

    /** @return the maximum number of iterations that this simulation should run - this is not enforced in this class
     * but the member variable is set when parsing the options */
    @Override
    public int getMaxIterations() {
        return lifeOpts().get(KEY_LIFE_MAX_ITERATIONS).intValue();
    }

    @Override
    public Grid<LifeCell> getGrid() {
        return grid;
    }

    /**
     * @return number of rows in the grid
     */
    @Override
    public int getGridRows() { return lifeOpts().get(KEY_LIFE_GRID_ROWS).intValue(); }

    /**
     * @return number of columns in the grid
     */
    @Override
    public int getGridCols() { return lifeOpts().get(KEY_LIFE_GRID_COLS).intValue();  }

    public static void main(String []args) throws LifeException {
        Life life = new Life();

        while(life.agents.size() > 0){
            life.step();
        }
    }
}
