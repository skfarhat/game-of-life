package core;

import core.actions.Action;
import core.actions.Consume;
import core.actions.EnergyChange;
import core.actions.Move;
import core.actions.Reproduce;
import core.exceptions.*;
import core.interfaces.Consumable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**  default constructor, calls other constructor and initialises fields to their defaults */
    public Life() throws LifeException, IllegalArgumentException { this(null); }

    public Life(LifeOptions options) throws IllegalArgumentException, TooManySurfacesException {

        if (null == options)
            options = new LifeOptions();

        this.options = options;

        // Create Life in 7 days
        // ---------------------

        // create grid
        grid = new LifeGrid(getGridRows(), getGridCols());

        // create agents and distribute
        agents = new ArrayList<>();
        createAndDistributeAgents();
    }

    /**
     *
     * @param a
     * @return
     * @throws SurfaceAlreadyPresent
     */
    public boolean addAgent(LifeAgent a) throws SurfaceAlreadyPresent {
        if (false == grid.isPointInBounds(a.getPos()))
            return false;
        LifeCell lc = grid.get(a.getPos());
        return (lc.addAgent(a) && agents.add(a));
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
            LifeCell cell = grid.get(a.getPos());
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
    public List<Action> step() throws AgentAlreadyDeadException {

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

        LifeAgentOptions agentOpts = options.getOptionsForAgent(chosen.getClass());

        if (chosen instanceof Creature) {

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

            double rAgent = agentOpts.getReproductionRate();
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

            int ageBy = agentOpts.getAgeBy();
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

            double rSurface = agentOpts.getReproductionRate();
            boolean willReproduce = Utils.getRand().nextDouble() < rSurface;

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
            int ageGrass = agentOpts.getAgeBy();
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

    /** @return total number number of created agents */
    private int createAndDistributeAgents() {

        // This is an important check necessary to prevent infinite loops in the code bit adding the agents below
        //
        // the check on SurfaceCount vs nb of cells has to be made here
        // because we cannot check this in LifeOptions constructor - since gridCols and gridRows are set-able
        final int surfaceCount = surfaceCount(options);
        final int cellCount = options.getGridRows() * options.getGridCols();
        if (surfaceCount > cellCount)
            throw new TooManySurfacesException(String.format("%d surface instances requested with only %d cells present", surfaceCount, cellCount));


        int nCreated = 0; // number of agents created

        for (Class<?extends LifeAgent> cls : options.getSupportedAgents()) {
            LifeAgentOptions lifeAgentOptions = options.getOptionsForAgent(cls);
            int I0 = lifeAgentOptions.getInitialCount(); // number of instances
            int E0 = lifeAgentOptions.getInitialEnergy(); // initial energy

            // for each type of agent
            try {
                // create however many agents of this type are needed
                for (int i = 0; i < I0; i++) {
                    // find a random point in the grid to place this agent instance
                    Point2D p = Utils.randomPoint(options.getGridCols(), options.getGridRows());
                    LifeAgent lifeAgent = cls.getConstructor(Point2D.class, Integer.class).newInstance(p, E0);

                    // CAREFUL: if no check is done with surfacelessCells < I0 before getting to here, this will iterate forever..
                    // keeps trying to find a position for the Grass
                    do {
                        p = Utils.randomPoint(options.getGridCols(), options.getGridRows());
                        lifeAgent.setPos(p);
                        try {
                            addAgent(lifeAgent);
                            break;
                        }
                        catch (SurfaceAlreadyPresent e) {
                            /*
                            we expect an exception to be thrown when we are trying to find a random
                            position for a Surface but the random points chosen are already occupied
                            */
                        }
                    } while (true);

                    nCreated++;
                }
            }
            catch (ReflectiveOperationException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                throw new LifeImplementationException("Implementation error: could not create instance of Agent " + cls.getName() +  "\n"
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
            LOGGER.log(Level.WARNING, "processConsume called without any consumables");
            return;
        }

        final int choiceOfImplementation = 1;
        final int GAIN_CAP = 10;

        Consumable consumable = action.getConsumables().next();
        LifeAgent consumingAgent = action.getAgent();

        // defines the energy gained by the consuming agent
        int energyGain;
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
                energyGain = options.getOptionsForAgent(consumingAgent.getClass()).getEnergyGained();
        }

        // energy lost field is only used for Surfaces -- ignored for Creatures in this implementation
        if (consumable instanceof Surface) {
            int eGrassLost = options.getOptionsForAgent(((LifeAgent)consumable).getClass()).getEnergyLost();
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
