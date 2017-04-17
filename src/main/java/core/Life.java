package core;

import core.actions.*;
import core.exceptions.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class Life class created for each simulation to step. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life implements LifeGetter {

    /** @brief logger */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

    /** @brief the number of times step has been called and returned a non-empty list of Actions */
    private int stepCount;

    /** @brief the grid containing all cells on which the agents will be placed */
    private final Grid<LifeCell> grid;

    /** @brief list of all of the agents in Life */
    private final List<Agent> agents;

    /** @brief */
    private final LifeOptions options;

    // ===========================================================================================
    // METHODS
    // ===========================================================================================

    /**
     * factory method to create a default life
     * @return
     * @throws LifeException
     */
    public static Life createDefaultLife() throws LifeException {
        List<LifeAgentOptions> agentOptions = new ArrayList<>();
        agentOptions.add(new LifeAgentOptions(Wolf.class));
        agentOptions.add(new LifeAgentOptions(Deer.class));
        agentOptions.add(new LifeAgentOptions(Grass.class));
        LifeOptions options = new LifeOptions(agentOptions);

        Life life = new Life(options);
        return life;
    }

    /** @brief default constructor, calls other constructor and initialises fields to their defaults */
    public Life() throws LifeException, IllegalArgumentException { this(null); }

    public Life(LifeOptions options) throws LifeException, IllegalArgumentException {

        if (null == options)
            options = new LifeOptions();

        this.options = options;

        // Create Life in 7 days
        // ---------------------

        // [4] create grid
        grid = GridLifeCellFactory.createGridCell(getGridRows(), getGridCols()); // create a square Grid

        // [5] create agents and distribute
        agents = new ArrayList<>();
        createAndDistributeAgents();
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
            LOGGER.log(Level.FINE, "step() nothing to do");
            return actions;
        }

        // choose an agent at random
        int randI = Utils.randomPositiveInteger(agents.size());
        LifeAgent chosen = (LifeAgent) agents.get(randI);
        if (!chosen.isAlive())
            LOGGER.log(Level.SEVERE, "We chose a dead agent!");

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

            List<Consumable> consumableAgents = options.filterConsumablesForAgent(nextCell.getAgents(), chosen);

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

            double rWolf = options.getOptionsForAgent(Wolf.class).getReproductionRate();
            double rDeer = options.getOptionsForAgent(Deer.class).getReproductionRate();
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

            int ageWolf = options.getOptionsForAgent(Wolf.class).getAgeBy();
            int ageDeer = options.getOptionsForAgent(Deer.class).getAgeBy();
            int ageBy = (chosen instanceof Wolf)? ageWolf: ageDeer;
            Action age = new EnergyChange(chosen, -ageBy);
            actions.add(age);
            processAgeAction((EnergyChange) age);

            // find the cell's dead agents
            // double cast to change List<LifeAgent> to List<Agent>
            List<Agent> deadAgents = (List<Agent>) (List) ((LifeCell)nextCell).findDeadAgents();

            // removes dead agents from the local agents list and from the cells' respective agents lists
            if (false == removeAgents(deadAgents)) {
                LOGGER.log(Level.SEVERE, "Failed to remove some agents!");
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
            int ageGrass = options.getOptionsForAgent(Grass.class).getAgeBy();
            Action energyGain = new EnergyChange(chosen, -ageGrass);
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

        int nCreated = 0;
        for (Class<? extends LifeAgent> agentType : options.getSupportedAgents()) {
            // get the initial energy
            // get the initial number

            LifeAgentOptions lifeAgentOptions = options.getOptionsForAgent(agentType); //(LifeAgentOptions) lifeAgentOpts().get(agentType);
            int I0 = lifeAgentOptions.getInitialCount(); // number of instances
            int E0 = lifeAgentOptions.getInitialEnergy(); // initial energy

            // for each type of agent
            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I0; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(options.getGridRows(), options.getGridCols()); // TODO(sami): should this be the other way around ?
                    Agent agent = agentType.getConstructor(Point2D.class, Integer.class).newInstance(p, E0);
                    addAgent(agent);
                    nCreated++;
                }
            } catch (ReflectiveOperationException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                throw new LifeException("Implementation error: could not create instance of Agent " + agentType.getName() +  "\n"
                        + e.getMessage());
            }
        }
        return nCreated;
    }

    private void processAgeAction(EnergyChange action) throws AgentIsDeadException {
        LOGGER.log(Level.INFO, action.toString());
        action.getAgent().changeEnergyBy(action.getEnergyDelta());
    }

    private void processMoveAction(Move action) throws InvalidPositionException {
        LOGGER.log(Level.INFO, action.toString());
        Cell nextCell = grid.get(action.getTo());
        grid.moveAgentToCell(action.getAgent(), nextCell);
    }

    private void processReproduce(Reproduce action) throws InvalidPositionException {
        LOGGER.log(Level.INFO, action.toString());
        Iterator<LifeAgent> babies = action.getBabies();
        while(babies.hasNext()) {
            LifeAgent baby = babies.next();
            addAgent(baby);
        }
    }

    private void processConsume(Consume action) throws AgentIsDeadException {
        LOGGER.log(Level.INFO, action.toString());
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
                int eWolfGain = options.getOptionsForAgent(Wolf.class).getEnergyGained();
                int eDeerGain = options.getOptionsForAgent(Deer.class).getEnergyGained();
                int eGrassGain = options.getOptionsForAgent(Grass.class).getEnergyGained();
                energyGain = (consumable instanceof Wolf)? eWolfGain : (consumable instanceof Deer)? eDeerGain: eGrassGain;
        }


        if (consumable instanceof Grass) {
            int eGrassLost = options.getOptionsForAgent(Grass.class).getEnergyLost();
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
    }     * but the member variable is set when parsing the options */
    @Override
    public int getMaxIterations() {
        return options.getMaximumIterations();
    }

    @Override
    public Grid<LifeCell> getGrid() {
        return grid;
    }

    /**
     * @return number of rows in the grid
     */
    @Override
    public int getGridRows() { return options.getGridRows();  }

    /**
     * @return number of columns in the grid
     */
    @Override
    public int getGridCols() { return options.getGridCols(); }

    public static void main(String []args) throws LifeException {
        Life life = new Life();

        while(life.agents.size() > 0){
            life.step();
        }
    }
}
