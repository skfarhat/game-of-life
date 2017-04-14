package core;

import core.actions.*;
import core.exceptions.AgentIsDeadException;
import core.exceptions.ConsumableOutOfEnergy;
import core.exceptions.InvalidPositionException;
import core.exceptions.LifeException;

import java.lang.reflect.InvocationTargetException;
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
    // List of keys in the params map
    // ===========================================================================================

    public static final String KEY_MAX_ITERATIONS = "MAX_ITERATIONS";
    public static final String KEY_GRID_COLS = "GRID_COLS";
    public static final String KEY_GRID_ROWS = "GRID_ROWS";
    public static final String KEY_E_GRASS_INITIAL = "E_GRASS_INITIAL";
    public static final String KEY_E_DEER_INITIAL = "E_DEER_INITIAL";
    public static final String KEY_E_WOLF_INITIAL = "E_WOLF_INITIAL";
    public static final String KEY_E_DEER_GAIN = "E_DEER_GAIN";
    public static final String KEY_E_WOLF_GAIN = "E_WOLF_GAIN";
    public static final String KEY_E_GRASS_LOSS = "E_GRASS_DECREASE";
    public static final String KEY_E_GRASS_GAIN = "KEY_E_GRASS_GAIN";
    public static final String KEY_E_STEP_DECREASE = "E_STEP_DECREASE";
    /* public static final String KEY_AGE_GRASS = "AGE_GRASS"; */ // Grass doesn't age in this implementation
    public static final String KEY_AGE_DEER = "AGE_DEER";
    public static final String KEY_AGE_WOLF = "AGE_WOLF";
    public static final String KEY_R_GRASS = "R_GRASS";
    public static final String KEY_R_DEER = "R_DEER";
    public static final String KEY_R_WOLF = "R_WOLF";
    public static final String KEY_I_GRASS = "I_GRASS";
    public static final String KEY_I_DEER = "I_DEER";
    public static final String KEY_I_WOLF = "I_WOLF";

    // ===========================================================================================
    // Defaults
    // ===========================================================================================

    /** @brief default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRID_N = 5;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRASS_GAIN = 3;

    /** @brief default energy gained by grass during reproduction*/
    public static final int DEFAULT_E_GAIN = 2;

    /** @brief default energy decrease for Agents when they age */
    public static final int DEFAULT_AGE = 1;

    /** @brief default initial deer energy */
    public static final int DEFAULT_E_DEER = 5;

    /** @brief default initial wolf energy */
    public static final int DEFAULT_E_WOLF = 5;

    /** @brief default initial number of wolves */
    public static final int DEFAULT_I_WOLF = 5;

    /** @brief default initial number of deers */
    public static final int DEFAULT_I_DEER = 5;

    /** @brief default initial number of deers */
    public static final int DEFAULT_I_GRASS = 5;

    /** @brief default probability of reproducing for a wolf */
    public static final double DEFAULT_R_WOLF = 0.1;

    /** @brief default probability of reproducing for a deer*/
    public static final double DEFAULT_R_DEER = 0.15;

    /** @brief default probability of reproducing for grass */
    public static final double DEFAULT_R_GRASS = 1.0;

    /** @brief default energy lost by Grass when it is consumed */
    public static final int DEFAULT_E_GRASS_LOSS = 5;

    // ===========================================================================================
    // Params
    // ===========================================================================================

    /**
     * @brief map determining which typ of LifeAgent can consume which type,
     * e.g.  Wolf.class -->  [Deer.class, Sheep.class...]
     */
    private final Map<Class, List<Class>> CONSUME_RULES = new HashMap<Class, List<Class>>();

    public final int GRID_COLS;
    public final int GRID_ROWS;
    public final int E_GRASS_INITIAL;
    public final int E_DEER_INITIAL;
    public final int E_WOLF_INITIAL;
    public final int E_GRASS_GAIN;
    public final int E_DEER_GAIN;
    public final int E_WOLF_GAIN;
    public final double R_GRASS;
    public final double R_DEER;
    public final double R_WOLF;
    public final int I_GRASS;
    public final int I_DEER;
    public final int I_WOLF;
    public final int AGE_WOLF;
    public final int AGE_DEER;
    public final int E_GRASS_LOSS;

    /**
     * @brief the maximum number of iterations (stepCounts) - max number of times step can be called.
     * A negative means run indefinitely
     * */
    public final int maxIterations;

    /** @brief the number of times step has been called and returned a non-empty list of Actions */
    private int stepCount;

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

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
    public Life(Map<String, Number> params) throws LifeException, IllegalArgumentException {

        // Consume Rules: dictate who is allowed to consume whom
        CONSUME_RULES.put(Wolf.class, new ArrayList<Class>(){{add(Deer.class );}}); // Wolf eats Deer
        CONSUME_RULES.put(Deer.class, new ArrayList<Class>(){{add(Grass.class);}}); // Deer eats Grass

        // if the params map passed is null, we assume the user has no input parameters, we create an empty map
        // and the below code will step and set all fields to their defaults
        if (params == null) params= new HashMap<>();

        maxIterations = params.containsKey(KEY_MAX_ITERATIONS)? params.get(KEY_MAX_ITERATIONS).intValue() : DEFAULT_MAX_ITERATIONS;
        exceptionIfNegative(GRID_COLS = params.containsKey(KEY_GRID_COLS)? params.get(KEY_GRID_COLS).intValue() : DEFAULT_GRID_N);
        exceptionIfNegative(GRID_ROWS = params.containsKey(KEY_GRID_ROWS)? params.get(KEY_GRID_ROWS).intValue() : DEFAULT_GRID_N);

        // initial energy
        exceptionIfNegative(E_GRASS_INITIAL = params.containsKey(KEY_E_GRASS_INITIAL)? params.get(KEY_E_GRASS_INITIAL).intValue() : DEFAULT_E_DEER);
        exceptionIfNegative(E_DEER_INITIAL = params.containsKey(KEY_E_DEER_INITIAL)? params.get(KEY_E_DEER_INITIAL).intValue() : DEFAULT_E_DEER);
        exceptionIfNegative(E_WOLF_INITIAL = params.containsKey(KEY_E_WOLF_INITIAL)? params.get(KEY_E_WOLF_INITIAL).intValue() : DEFAULT_E_WOLF);

        // energy gain
        exceptionIfNegative(E_GRASS_GAIN = params.containsKey(KEY_E_GRASS_GAIN)? params.get(KEY_E_GRASS_GAIN).intValue() : DEFAULT_GRASS_GAIN);
        exceptionIfNegative(E_DEER_GAIN = params.containsKey(KEY_E_DEER_GAIN)? params.get(KEY_E_DEER_GAIN).intValue() : DEFAULT_E_GAIN);
        exceptionIfNegative(E_WOLF_GAIN = params.containsKey(KEY_E_WOLF_GAIN)? params.get(KEY_E_WOLF_GAIN).intValue() : DEFAULT_E_GAIN);

        // energy loss
        exceptionIfNegative(E_GRASS_LOSS = params.containsKey(KEY_E_GRASS_LOSS)? params.get(KEY_E_GRASS_LOSS).intValue() : DEFAULT_E_GRASS_LOSS);
        /*  Wolf dies when consumed. */
        /*  Deer dies when consumed. */

        // initial count
        exceptionIfNegative(I_GRASS = params.containsKey(KEY_I_GRASS)? params.get(KEY_I_GRASS).intValue() : DEFAULT_I_GRASS);
        exceptionIfNegative(I_DEER = params.containsKey(KEY_I_DEER)? params.get(KEY_I_DEER).intValue() : DEFAULT_I_DEER);
        exceptionIfNegative(I_WOLF = params.containsKey(KEY_I_WOLF)? params.get(KEY_I_WOLF).intValue() : DEFAULT_I_WOLF);

        // age
        exceptionIfNegative(AGE_DEER = params.containsKey(KEY_AGE_DEER)? params.get(KEY_AGE_DEER).intValue() : DEFAULT_AGE);
        exceptionIfNegative(AGE_WOLF = params.containsKey(KEY_AGE_WOLF)? params.get(KEY_AGE_WOLF).intValue() : DEFAULT_AGE);

        // reproduction (the doubles must be in range [0-1])
        exceptionIfOutOfRange(R_DEER = params.containsKey(KEY_R_DEER)? params.get(KEY_R_DEER).doubleValue() : DEFAULT_R_DEER);
        exceptionIfOutOfRange(R_WOLF = params.containsKey(KEY_R_WOLF)? params.get(KEY_R_WOLF).doubleValue() : DEFAULT_R_WOLF);
        R_GRASS = DEFAULT_R_GRASS; // at the time of writing this should not be variable and has thus been hardcoded

        // Create Life in 7 days
        // ---------------------

        // create grid
        grid = GridLifeCellFactory.createGridCell(this.GRID_ROWS, this.GRID_COLS); // create a square Grid

        final int nAgents = I_DEER + I_WOLF + I_GRASS;
        agents = new ArrayList<>(nAgents);
        int nCreated = createAndDistributeAgents();
        if (nCreated != nAgents)  {
            throw new LifeException(String.format("Created number of agents  (%d) does not match the number we expect (%d).\n",
                    nCreated, nAgents));
        }
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

            double rAgent = (chosen instanceof Wolf)? R_WOLF : R_DEER;
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

            int ageBy = (chosen instanceof Wolf)? AGE_WOLF: AGE_DEER;
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

            // ---------------
            // Increase Energy
            // ---------------
            Action energyGain = new EnergyChange(chosen, E_GRASS_GAIN);
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
    private int createAndDistributeAgents() throws LifeException {

        // TODO(sami): improvement would be to generalise all of this so that we can easily extend with more (new) agents
        // the number of different types of agents in the system
        final Class<?extends Agent> TYPES[] = new Class[]{Deer.class, Wolf.class, Grass.class};
        final int I_INITIAL[] = {I_DEER, I_WOLF, I_GRASS};
        final int E_INITIAL[] = {E_DEER_INITIAL, E_WOLF_INITIAL, E_GRASS_INITIAL};

        // check that there isn't anything fishy
        if (TYPES.length != I_INITIAL.length || TYPES.length != E_INITIAL.length) {
            throw new LifeException("Implementation error: array sizes are not matching with the number of agent types");
        }

        // Note: this method is so general and flexible, I have tears of happiness in my eyes

        int nCreated = 0;
        // for each type of agent
        for (int type = 0 ; type < TYPES.length; type++) {

            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I_INITIAL[type]; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(this.GRID_ROWS, this.GRID_COLS); // TODO(sami): should this be the other way around ?
                    Agent agent = TYPES[type].getConstructor(Point2D.class, Integer.class).newInstance(p, E_INITIAL[type]);
                    addAgent(agent);
                    nCreated++;
                }
            } catch (ReflectiveOperationException exc) {
                exc.printStackTrace();;
                throw new LifeException("Implementation error: could not create instance of Agent " + TYPES[type]);
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
                energyGain = (consumable instanceof Wolf)? E_WOLF_GAIN : (consumable instanceof Deer)? E_DEER_GAIN : E_GRASS_GAIN;
        }


        if (consumable instanceof Grass)
            energyLoss = Math.min(E_GRASS_LOSS, consumableEnergy);

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
        return maxIterations;
    }

    @Override
    public Grid<LifeCell> getGrid() {
        return grid;
    }

    /**
     * @return number of rows in the grid
     */
    @Override
    public int getGridRows() { return grid.getRows(); }

    /**
     * @return number of columns in the grid
     */
    @Override
    public int getGridCols() { return grid.getCols(); }

    public static void main(String []args) throws LifeException {
        Life life = new Life();

        while(life.agents.size() > 0){
            life.step();
        }
    }
}
