package core;

import core.actions.*;
import core.exceptions.*;
import core.interfaces.Consumable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Life class created for each simulation to step. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life implements LifeGetter {

    /**  logger */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

    /**  the number of times step has been called and returned a non-empty list of Actions */
    private int stepCount;

    /** the grid containing all cells on which the agents will be placed */
    private final LifeGrid grid;

    /** list of all of the agents in Life */
    private final List<Agent> agents;

    /**  */
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

    /**  default constructor, calls other constructor and initialises fields to their defaults */
    public Life() throws LifeException, IllegalArgumentException { this(null); }

    public Life(LifeOptions options) throws IllegalArgumentException, TooManySurfacesException {

        if (null == options)
            options = new LifeOptions();

        this.options = options;

        // TODO(sami); why doesn't options check this
        final int surfaceCount = surfaceCount(options);
        final int cellCount = options.getGridRows() * options.getGridCols();
        if (surfaceCount > cellCount)
            throw new TooManySurfacesException(String.format("%d surface instances requested with only %d cells present", surfaceCount, cellCount));

        // Create Life in 7 days
        // ---------------------

        // [4] create grid
        grid = new LifeGrid(getGridRows(), getGridCols());

        // [5] create agents and distribute
        agents = new ArrayList<>();
        createAndDistributeAgents();
    }

    public boolean addAgent(LifeAgent a) throws SurfaceAlreadyPresent {
        if (a instanceof Creature)
            return addCreature((Creature) a);
        else if (a instanceof Surface)
            return addSurface((Surface) a);
        else {
            LOGGER.log(Level.SEVERE, "Attempting to add LifeAgent of unknown type {0}", a.getClass().getName());
            return false;
        }
    }

    public boolean addCreature(Creature c) {
        if (false == grid.isPointInBounds(c.getPos()))
            return false;
        LifeCell lc = grid.get(c.getPos());
        return (lc.addAgent(c) && agents.add(c));
    }

    public boolean addSurface(Surface s) throws SurfaceAlreadyPresent {
        if (false == grid.isPointInBounds(s.getPos()))
            return false;
        LifeCell lc = grid.get(s.getPos());
        return lc.addAgent(s) && agents.add(s);
    }

    /**
     * adds all LifeAgents in the list by checking the type of LifeAgent and calling the right
     * method addSurface() or addCreature()
     * @param agents
     * @return
     */
    public boolean addAgents(List<LifeAgent> agents) throws SurfaceAlreadyPresent {
        boolean success = true;
        for (LifeAgent a : agents) {
            success &= addAgent(a);
        }
        return success;
    }

    /**
     * remove the passed Agent @param a from the local agents list and from the cell's agents list
     * @return true if the removal was successful from both lists, false otherwise
     */
    public boolean removeAgent(LifeAgent a)  {
        if (false == grid.isPointInBounds(a.getPos()))
            return false;
        try {
            LifeCell cell = (LifeCell) grid.get(a.getPos());
            return cell.removeAgent(a) && agents.remove(a);
        }
        catch (InvalidPositionException e) { return false; } // this shouldn't happen because we already checked
    }

    /**
     * removes all agents from the provided list. Failure to remove an agent in the list will make the method return false.
     * @param agents list of agents to remove
     * @return false if any one of the agents in the list could not be removed
     */
    public boolean removeAgents(List<LifeAgent> agents) {
        boolean success = true;
        for (LifeAgent a : agents) {
            success &= removeAgent(a);
        }
        return success;
    }

    /**
     * choose an agent at random to act
     * @throws InvalidPositionException
     * @throws AgentAlreadyDeadException
     * @return the stepCount index or -1 if there was nothing to do
     */
    public List<Action> step() throws SurfaceAlreadyPresent, AgentAlreadyDeadException {

        List<Action> actions = new ArrayList<>();

        // guard - nothing to do
        if (agents.size() < 1) {
            LOGGER.log(Level.FINE, "step() nothing to do");
            return actions;
        }

        // choose an agent at random
        int randI = Utils.randomPositiveInteger(agents.size());
        // TODO(sami): remove the casting
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

            List<Consumable> consumableAgents = new ArrayList<>();
            List<Class<?extends LifeAgent>> classes = options.getConsumableClassesForAgent(chosen);
            Iterator<LifeAgent> it = nextCell.getAgents();
            while(it.hasNext()) {
                LifeAgent la = it.next();
                if (classes.contains(la.getClass()))
                    consumableAgents.add(la);
            }

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
            List<LifeAgent> deadAgents = ((LifeCell)nextCell).findDeadAgents();

            // removes dead agents from the local agents list and from the cells' respective agents lists
            if (false == removeAgents(deadAgents)) {
                LOGGER.log(Level.SEVERE, "Failed to remove some agents!");
            }
            // TODO(sami); consider sending events for all new dead agents,
        }

        else if (chosen instanceof Surface) {
            // NOTE: explicitly handling Grass ONLY at the moment

            Point2D nextPoint = findAdjacentPointInGrid(chosen.getPos());
            LifeCell currCell = grid.get(chosen.getPos());
            LifeCell nextCell = grid.get(nextPoint);

            // -------
            // Consume
            // -------

            List<Class<?extends LifeAgent>> classes = options.getConsumableClassesForAgent(chosen);
            Iterator<LifeAgent> it = nextCell.getAgents();
            while(it.hasNext()) {
                LifeAgent la = it.next();
                if (classes.contains(la.getClass())){
                    Consume consume = new Consume(chosen, la);
                    actions.add(consume);
                    processConsume(consume);
                }
            }

            // ---------
            // Reproduce
            // ---------

            double rGrass = options.getOptionsForAgent(Grass.class).getReproductionRate();
            boolean willReproduce = Utils.getRand().nextDouble() < rGrass;

            if (willReproduce && !(grid.get(nextPoint)).containsSurface()) {
                LifeAgent babyGrass = chosen.reproduce();
                babyGrass.setPos(nextPoint);
                Action reproduce = new Reproduce(chosen, babyGrass);
                actions.add(reproduce);
                processReproduce((Reproduce) reproduce);
            }

            // ---
            // Age
            // ---
            int ageGrass = options.getOptionsForAgent(Grass.class).getAgeBy();
            Action energyGain = new EnergyChange(chosen, -ageGrass);
            actions.add(energyGain);
            processAgeAction((EnergyChange) energyGain);

            List<LifeAgent> deadAgents = currCell.findDeadAgents();
            deadAgents.addAll(nextCell.findDeadAgents());
            if (false == removeAgents(deadAgents)) {
                LOGGER.log(Level.SEVERE, "Failed to remove some agents!");
            }
        }

        stepCount++;
        return actions;
    }

    /**
     * @return total number number of created agents
     * @throws AgentAlreadyDeadException if the initial energy given to any of the agents is negative
     */
    private int createAndDistributeAgents() {

        // TODO(sami); no need to separate the two creationns anymomre ?
        int nCreated = 0; // number of agents created

        // surfaces list and creatures list
        List<Class<?extends LifeAgent>> surfaces = options.getSupportedAgents().stream()
                .filter(a -> Surface.class.isAssignableFrom(a)).collect(Collectors.toList());
        List<Class<?extends LifeAgent>> creatures = options.getSupportedAgents().stream()
                .filter(a -> Creature.class.isAssignableFrom(a)).collect(Collectors.toList());

        // Surfaces:
        // ------------
        for (Class<?extends LifeAgent> surfaceClass : surfaces) {
            LifeAgentOptions lifeAgentOptions = options.getOptionsForAgent(surfaceClass);
            int I0 = lifeAgentOptions.getInitialCount(); // number of instances
            int E0 = lifeAgentOptions.getInitialEnergy(); // initial energy

            // for each type of agent
            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I0; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(options.getGridCols(), options.getGridRows());
                    Surface surface = (Surface) surfaceClass.getConstructor(Point2D.class, Integer.class).newInstance(p, E0);

                    // keeps trying to find a position for the Grass
                    // CAREFUL: if no check is done with surfacelessCells < I0 before getting to here, this will iterate forever..
                    do {
                        p = Utils.randomPoint(options.getGridCols(), options.getGridRows());
                        surface.setPos(p);
                        try { addSurface(surface); break; }
                        catch(SurfaceAlreadyPresent e) {}
                    } while(true);

                    nCreated++;
                }
            } catch (ReflectiveOperationException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                throw new LifeImplementationException("Implementation error: could not create instance of Agent " + surfaceClass.getName() +  "\n"
                        + e.getMessage());
            }
        }

        // Non-Surfaces:
        // ------------
        for (Class<? extends LifeAgent> agentType : creatures) {

            LifeAgentOptions lifeAgentOptions = options.getOptionsForAgent(agentType);
            int I0 = lifeAgentOptions.getInitialCount(); // number of instances
            int E0 = lifeAgentOptions.getInitialEnergy(); // initial energy

            // for each type of agent
            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I0; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(options.getGridCols(), options.getGridRows());
                    Creature creature = (Creature) agentType.getConstructor(Point2D.class, Integer.class).newInstance(p, E0);
                    addCreature(creature);
                    nCreated++;
                }
            } catch (ReflectiveOperationException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                throw new LifeImplementationException("Implementation error: could not create instance of Agent " + agentType.getName() +  "\n"
                        + e.getMessage());
            }
        }
        return nCreated;
    }

    private void processAgeAction(EnergyChange action) throws AgentAlreadyDeadException {
        LOGGER.log(Level.INFO, action.toString());
        action.getAgent().changeEnergyBy(action.getEnergyDelta());
    }

    private void processMoveAction(Move action) throws SurfaceAlreadyPresent {
        LOGGER.log(Level.INFO, action.toString());
        Cell nextCell = grid.get(action.getTo());
        grid.moveAgentToCell(action.getAgent(), nextCell);
    }

    private void processReproduce(Reproduce action) throws SurfaceAlreadyPresent {
        LOGGER.log(Level.INFO, action.toString());
        Iterator<LifeAgent> babies = action.getBabies();
        while(babies.hasNext()) {
            LifeAgent baby = babies.next();
            addAgent(baby);
        }
    }

    private void processConsume(Consume action) throws AgentAlreadyDeadException {
        LOGGER.log(Level.INFO, action.toString());
        if (false == action.getConsumables().hasNext()) {
            // nothing to consume
            // TODO(sami): put some DEBUG info here
            return;
        }

        final int choiceOfImplementation = 1;
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

    /**
     * @param p
     * @return
     * @throws InvalidPositionException
     */
    private Point2D findAdjacentPointInGrid(Point2D p) throws InvalidPositionException {
        return grid.randomAdjacentPoint(p);
    }

    /** @return the number of surface instances in LifeOptions */
    private int surfaceCount(LifeOptions opts) {
        int count = 0;
        for (Class<? extends LifeAgent> opt : opts.getSupportedAgents()) {
            if (Surface.class.isAssignableFrom(opt))
                count+=opts.getOptionsForAgent(opt).getInitialCount();
        }
        return count;
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

    /**
     * @return the maximum number of iterations that this simulation should run - this is not enforced in this class
     * but the member variable is set when parsing the options
     */
    @Override
    public int getMaxIterations() {
        return options.getMaximumIterations();
    }

    @Override
    public LifeGrid getGrid() {
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
