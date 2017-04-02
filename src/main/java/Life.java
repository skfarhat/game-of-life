/**
 * Created by Sami on 29/03/2017.
 */

import java.util.*;

/**
 * @class Life class created for each simulation to run. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life {

    // ===========================================================================================
    // List of keys in the params map
    // ===========================================================================================

    /** @brief GRID_N key in params */
    public static final String KEY_GRID_N = "GRID_N";

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

    /** @brief the grid's size */
    public final int GRID_N;

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
    Life() throws GridCreationException, InvalidPositionException, AgentIsDeadException { this(null);}

    /** @brief constructor taking in a params map specifying the input parameters */
    Life(Map<String, Number> params) throws IllegalArgumentException, GridCreationException, AgentIsDeadException, InvalidPositionException {

        // Consume Rules: dictate who is allowed to consume whom
        CONSUME_RULES.put(Wolf.class, new ArrayList<Class>(){{add(Deer.class );}}); // Wolf eats Deer
        CONSUME_RULES.put(Deer.class, new ArrayList<Class>(){{add(Grass.class);}}); // Deer eats Grass

        // if the params map passed is null, we assume the user has no input parameters, we create an empty map
        // and the below code will run and set all fields to their defaults
        if (params == null) params= new HashMap<String, Number>();

        exceptionIfNegative(GRID_N = params.containsKey(KEY_GRID_N)? params.get(KEY_GRID_N).intValue() : DEFAULT_GRID_N);
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
        grid = GridCellFactory.createGridCell(GRID_N, GRID_N); // create a square Grid

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
            Point2D p = Utils.randomPoint(this.GRID_N, this.GRID_N);
            grid.get(p.getX(), p.getY()).addAgent(a);
        }
    }

    private Point2D findAdjacentPointInGrid(Point2D p) throws InvalidPositionException {
        return grid.randomAdjacentPoint(p);
    }

    /**
     *
     * @param it Iterator of the Agents to filter from
     * @param agent agent which consumes
     * @return a list of all consumable Agents by Agent @param a
     */
    private List<LifeAgent> filterConsumablesForAgent(Iterator<Agent> it, Agent agent) {
        // list of classes that the chosen type can consume
        final List<Class> consumables =  CONSUME_RULES.get(agent.getClass());

        // put all agents in a list, then  filter the list for the consumables
        final List<LifeAgent> cellAgents = new ArrayList<LifeAgent>();
        while(it.hasNext()) // add all to List
            cellAgents.add((LifeAgent) it.next());

        cellAgents.stream().filter(a -> consumables.contains(a.getClass()));
        return cellAgents;
    }

    /** @brief move to next position */
    public Cell moveToAdjacentCell(Agent agent) throws InvalidPositionException {
        Point2D nextPoint = findAdjacentPointInGrid(agent.getPos());
        Cell nextCell = grid.get(nextPoint);
        grid.moveAgentToCell(agent, nextCell);
        return nextCell;
    }

    /** @brief remove all dead LifeAgents from the cell */
    private void clearDeadLifeAgents(Cell cell) {
        for (Iterator<Agent> it = cell.getAgents(); it.hasNext(); ) {
            LifeAgent a = (LifeAgent) it.next();
            if (!a.isAlive()){
                cell.removeAgent(a);
                agents.remove(a); // remove agent from agents list
            }
        }
    }

    /** @brief run this intolerable thing that is called life */
    public void run() throws InvalidPositionException, AgentIsDeadException {

        // choose an agent at random
        int randI = Utils.randomPositiveInteger(agents.size());
        LifeAgent chosen = (LifeAgent) agents.get(randI);

        // Wolves and Deers
        if ((chosen instanceof Wolf) || (chosen instanceof Deer)) {

            // move to adjacent cell
            Cell nextCell = moveToAdjacentCell(chosen);

            // put all the cell's agents in an ArrayList and pass them to the chosen
            List<LifeAgent> consumableAgents = filterConsumablesForAgent(nextCell.getAgents(), chosen);

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

            // clear out all the dead agents
//            clearDeadLifeAgents(cell);
        }
    }

    public int getGridSize() {
        return GRID_N;
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

    public static void main(String []args) throws GridCreationException, AgentIsDeadException, InvalidPositionException {
        Life life = new Life();

        int i = 0 ;
        while(life.agents.size() > 0){
            System.out.println(i++);
            life.run();
        }
    }
}
