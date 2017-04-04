/**
 * Created by Sami on 29/03/2017.
 */
package core;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @class Life class created for each simulation to step. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life {

    // ===========================================================================================
    // List of keys in the params map
    // ===========================================================================================

    /** @brief MAX_ITERATIONS key in params */
    public static final String KEY_MAX_ITERATIONS = "MAX_ITERATIONS";

    /** @brief GRID_COLS key in params */
    public static final String KEY_GRID_COLS = "GRID_COLS";

    /** @brief GRID_ROWS key in params */
    public static final String KEY_GRID_ROWS = "GRID_ROWS";

    /** @brief E_GRASS_INITIAL key in params */
    public static final String KEY_E_GRASS_INITIAL = "E_GRASS_INITIAL";

    /** @brief E_DEER_INITIAL key in params */
    public static final String KEY_E_DEER_INITIAL = "E_DEER_INITIAL";

    /** @brief E_WOLF_INITIAL key in params */
    public static final String KEY_E_WOLF_INITIAL = "E_WOLF_INITIAL";

    /** @brief E_DEER_GAIN key in params */
    public static final String KEY_E_DEER_GAIN = "E_DEER_GAIN";

    /** @brief E_WOLF_GAIN key in params */
    public static final String KEY_E_WOLF_GAIN = "E_WOLF_GAIN";

    /** @brief E_STEP_DECREASE key in params */
    public static final String KEY_E_STEP_DECREASE = "E_STEP_DECREASE";

    /** @brief R_GRASS key in params */
    public static final String KEY_R_GRASS = "R_GRASS";

    /** @brief R_DEER key in params */
    public static final String KEY_R_DEER = "R_DEER";

    /** @brief R_WOLF key in params */
    public static final String KEY_R_WOLF = "R_WOLF";

    /** @brief I_GRASS key in params */
    public static final String KEY_I_GRASS = "I_GRASS";

    /** @brief I_DEER key in params */
    public static final String KEY_I_DEER = "I_DEER";

    /** @brief I_WOLF key in params */
    public static final String KEY_I_WOLF = "I_WOLF";

    // ===========================================================================================
    // Defaults
    // ===========================================================================================

    /** @brief default maximum number of iterations, a negative value means run indefinitely */
    public static final int DEFAULT_MAX_ITERATIONS = -1;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int DEFAULT_GRID_N = 10;

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int E_DEFAULT_GAIN = 2;

    /** @brief default energy decrease for Agents when they age */
    public static final int E_DEFAULT_DECREASE = 1;

    /** @brief default initial agent energy */
    public static final int E_DEFAULT_INITIAL = 10;

    /** @brief default random schedule frequency - how often are you likely to be selected to act by the scheduler */
    public static final double R_DEFAULT = 0.33;

    /** @brief default initial number of instance of an agent type */
    public static final int I_DEFAULT = 1;

    // ===========================================================================================
    // Params
    // ===========================================================================================

    /**
     * @brief map determining which typ of LifeAgent can consume which type,
     * e.g.  Wolf.class -->  [Deer.class, Sheep.class...]
     */
    private final Map<Class, List<Class>> CONSUME_RULES = new HashMap<Class, List<Class>>();

    /** @brief the maximum number of iterations - max number of times step is called, a negative means run indefinitely */
    public final int maxIterations;

    /** @brief the index of the current iteration */
    private int iteration;

    /** @brief number of columns in the grid */
    public final int GRID_COLS;

    /** @brief number of rows in the grid */
    public final int GRID_ROWS;

    /** @brief grass' initial energy */
    public final int E_GRASS_INITIAL;

    /** @brief deer's initial energy */
    public final int E_DEER_INITIAL;

    /** @brief wolf's initial energy */
    public final int E_WOLF_INITIAL;

    /** @brief energy gained when the deer consumes grass */
    public final int E_DEER_GAIN;

    /** @brief energy gained when the wolf consumes a deer */
    public final int E_WOLF_GAIN;

    /** @brief value by which Agent's energy is decreased at each step */
    public final int E_STEP_DECREASE;

    /** @brief grass' random scheduling frequency */
    public final double R_GRASS;

    /** @brief deer's random scheduling frequency */
    public final double R_DEER;

    /** @brief wolf's random scheduling frequency */
    public final double R_WOLF;

    /** @brief initial number of grass instances */
    public final int I_GRASS;

    /** @brief initial number of deers */
    public final int I_DEER;

    /** @brief initial number of wolves */
    public final int I_WOLF;

    /** @brief the grid containing all cells on which the agents will be placed */
    private final Grid<Cell> grid;

    /** @brief list of all of the agents in Life */
    private final List<Agent> agents;

    /** @brief default constructor, calls other constructor and initialises fields to their defaults */
    public Life() throws GridCreationException, InvalidPositionException, AgentIsDeadException { this(null);}

    // AgentIsDeadException --> invalid initial energy
    // InvalidPositionException --> distribution of the agents failed.
    /** @brief constructor taking in a params map specifying the input parameters */
    public Life(Map<String, Number> params) throws IllegalArgumentException, GridCreationException, AgentIsDeadException, InvalidPositionException {

        // Consume Rules: dictate who is allowed to consume whom
        CONSUME_RULES.put(Wolf.class, new ArrayList<Class>(){{add(Deer.class );}}); // Wolf eats Deer
        CONSUME_RULES.put(Deer.class, new ArrayList<Class>(){{add(Grass.class);}}); // Deer eats Grass

        // if the params map passed is null, we assume the user has no input parameters, we create an empty map
        // and the below code will step and set all fields to their defaults
        if (params == null) params= new HashMap<>();

        maxIterations = params.containsKey(KEY_MAX_ITERATIONS)? params.get(KEY_MAX_ITERATIONS).intValue() : DEFAULT_MAX_ITERATIONS;
        exceptionIfNegative(GRID_COLS = params.containsKey(KEY_GRID_COLS)? params.get(KEY_GRID_COLS).intValue() : DEFAULT_GRID_N);
        exceptionIfNegative(GRID_ROWS = params.containsKey(KEY_GRID_ROWS)? params.get(KEY_GRID_ROWS).intValue() : DEFAULT_GRID_N);
        exceptionIfNegative(E_GRASS_INITIAL = params.containsKey(KEY_E_GRASS_INITIAL)? params.get(KEY_E_GRASS_INITIAL).intValue() : E_DEFAULT_INITIAL);
        exceptionIfNegative(E_DEER_INITIAL = params.containsKey(KEY_E_DEER_INITIAL)? params.get(KEY_E_DEER_INITIAL).intValue() : E_DEFAULT_INITIAL);
        exceptionIfNegative(E_WOLF_INITIAL = params.containsKey(KEY_E_WOLF_INITIAL)? params.get(KEY_E_WOLF_INITIAL).intValue() : E_DEFAULT_INITIAL);
        exceptionIfNegative(E_DEER_GAIN = params.containsKey(KEY_E_DEER_GAIN)? params.get(KEY_E_DEER_GAIN).intValue() : E_DEFAULT_GAIN);
        exceptionIfNegative(E_WOLF_GAIN = params.containsKey(KEY_E_WOLF_GAIN)? params.get(KEY_E_WOLF_GAIN).intValue() : E_DEFAULT_GAIN);
        exceptionIfNegative(E_STEP_DECREASE = params.containsKey(KEY_E_STEP_DECREASE)? params.get(KEY_E_STEP_DECREASE).intValue() : E_DEFAULT_DECREASE);
        exceptionIfNegative(I_GRASS = params.containsKey(KEY_I_GRASS)? params.get(KEY_I_GRASS).intValue() : I_DEFAULT);
        exceptionIfNegative(I_DEER = params.containsKey(KEY_I_DEER)? params.get(KEY_I_DEER).intValue() : I_DEFAULT);
        exceptionIfNegative(I_WOLF = params.containsKey(KEY_I_WOLF)? params.get(KEY_I_WOLF).intValue() : I_DEFAULT);

        // the doubles must be in range 0-1 - we use exceptionIfOutOfRange
        exceptionIfOutOfRange(R_GRASS = params.containsKey(KEY_R_GRASS)? params.get(KEY_R_GRASS).doubleValue() : R_DEFAULT);
        exceptionIfOutOfRange(R_DEER = params.containsKey(KEY_R_DEER)? params.get(KEY_R_DEER).doubleValue() : R_DEFAULT);
        exceptionIfOutOfRange(R_WOLF = params.containsKey(KEY_R_WOLF)? params.get(KEY_R_WOLF).doubleValue() : R_DEFAULT);


        // Create Life in 7 days
        // ---------------------

        // create grid
        grid = GridCellFactory.createGridCell(this.GRID_ROWS, this.GRID_COLS); // create a square Grid

        // create all agents and distribute
        agents = new ArrayList<Agent>(I_DEER+I_WOLF+I_GRASS);
        for (int i = 0; i < I_DEER; i++) agents.add(new Deer(E_DEER_INITIAL));
        for (int i = 0; i < I_WOLF; i++) agents.add(new Wolf(E_WOLF_INITIAL));
        for (int i = 0; i < I_GRASS; i++) agents.add(new Grass(E_GRASS_INITIAL));
        uniformlyDistribute(agents);

    }

    /** @brief uniformly distribute the agents on the grid */
    private void uniformlyDistribute(List<Agent> agents) throws InvalidPositionException {
        for (Agent a : agents) {
            Point2D p = Utils.randomPoint(this.GRID_ROWS, this.GRID_COLS);
            grid.get(p.getX(), p.getY()).addAgent(a);
        }
    }

    public Grid<Cell> getGrid() {
        return grid;
    }

    private Point2D findAdjacentPointInGrid(Point2D p) throws InvalidPositionException {
        return grid.randomAdjacentPoint(p);
    }

    /**
     * @brief remove all dead agents from the cell s
     * @param agents list which we want to filter
     * @return return the number of agents that were removed
     */
    public int recycleDeadAgents(List<LifeAgent> agents) {
        final int beforeSize = agents.size();
        agents.removeIf(a -> !a.isAlive());
        final int afterSize = agents.size();
        return beforeSize - afterSize;
    }

    /**
     *
     * @param list list to filter from
     * @param agent agent which consumes
     * @return a list of all consumable Agents by Agent @param a
     */
    private List<Consumable> filterConsumablesForAgent(List<Consumable> list, Agent agent) {
        // list of classes that the chosen type can consume
        final List<Class> consumables =  CONSUME_RULES.get(agent.getClass());
        list.stream().filter(a -> consumables.contains(a.getClass()));
        return list;
    }

    /** @brief move to next position */
    public Cell moveToAdjacentCell(Agent agent) throws InvalidPositionException {
        Point2D nextPoint = findAdjacentPointInGrid(agent.getPos());
        Cell nextCell = grid.get(nextPoint);
        grid.moveAgentToCell(agent, nextCell);
        return nextCell;
    }

    public void loop() throws AgentIsDeadException, InvalidPositionException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // we can use sleep
        // with this one not much has to be changed.

        // or executor service
        // with this one, we have to cancel the service when the frequency changes,
        //
    }

    /**
     * @brief chose an agent at random to act
     * @throws InvalidPositionException
     * @throws AgentIsDeadException
     * @return the iteration index
     */
    public int step() throws InvalidPositionException, AgentIsDeadException {

        if (iteration >= maxIterations) {

        }

        // choose an agent at random
        int randI = Utils.randomPositiveInteger(agents.size());
        LifeAgent chosen = (LifeAgent) agents.get(randI);

        // Wolves and Deers
        if ((chosen instanceof Wolf) || (chosen instanceof Deer)) {

            // move to adjacent cell
            Cell nextCell = moveToAdjacentCell(chosen);

            // put all the cell's agents in an ArrayList and pass them to the chosen
            List<Consumable> list = new ArrayList<>();
            for (Iterator<Agent> it = nextCell.getAgents(); it.hasNext();)
                list.add((Consumable) it.next());

            List<Consumable> consumableAgents = filterConsumablesForAgent(list, chosen);

            // consume all
            ((Consumes) chosen).consumeAll(consumableAgents);

            // reproduce at random
            double rAgent = (chosen instanceof Wolf)? R_WOLF : R_DEER;
            boolean willReproduce = Utils.getRand().nextDouble() < rAgent;
            if (willReproduce) {
                LifeAgent newBaby = chosen.reproduce();
                nextCell.addAgent(newBaby);
            }

            System.out.println(chosen);

            // decrease energy
            chosen.decreaseEnergyBy(E_STEP_DECREASE);

            // only in the dst cell can someone die
            recycleDeadAgents((List<LifeAgent>) nextCell.getAgents());
        }
        return iteration++;
    }

    /**
     * @return number of rows in the grid
     */
    public int getGridRows() { return GRID_ROWS; }

    /**
     * @return number of columns in the grid
     */
    public int getGridCols() { return GRID_COLS; }


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

    /** @brief get the current iteration */
    public int getIteration() {
        return iteration;
    }

    public static void main(String []args) throws GridCreationException, AgentIsDeadException, InvalidPositionException {
        Life life = new Life();

        int i = 0 ;
        while(life.agents.size() > 0){
            System.out.println(i++);
            life.step();
        }
    }
}
