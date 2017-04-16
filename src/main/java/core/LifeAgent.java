/**
 * Created by Sami on 28/03/2017.
 */
package core;

import core.exceptions.AgentIsDeadException;
import core.exceptions.ConsumableOutOfEnergy;

import java.util.List;

/**
 * @class LifeAgent
 *
 * Every LifeAgent is an Agent
 * Every LifeAgent has energy
 * Every LifeAgent has a position
 *
 * Not every LifeAgent ages (think of Grass)
 */
public abstract class LifeAgent extends Agent implements Reproduces, Consumable, Consumes {

    /** @brief default value for the initial energy of a LifeAgent */
    private static final int DEFAULT_INITIAL_ENERGY = 10;

    /** @brief this string must match the method name of getDefaultParams */
    public static final String METHOD_NAME_GET_DEFAULT_PARAMS = "getDefaultParams";

    /**
     * it is not essential that subclasses to worry about implementing getDefaultParams
     * so a default implementation returning null
     * @return default parameters for the LifeAgent class to be used in Life. These may well be overridden by Life.
     */
    public static LifeAgentOptions getDefaultParams() {
        return null;
    }

    /** @brief the energy with which this instance was created. This same value is used later when reproducing. */
    protected final Integer MY_INITIAL_ENERGY;

    /** @brief the energy of the LifeAgent */
    private Integer energy = 0;

    /** @brief is set to true when the Agent dies and prevents further changes to the energy after it death,
     * zombies are not allowed :)
     */
    private boolean died = false;

    /** @brief default constructor */
    public LifeAgent() throws AgentIsDeadException {
        this(null, DEFAULT_INITIAL_ENERGY);
    }

    public LifeAgent(Point2D p) throws AgentIsDeadException {
        this(p, DEFAULT_INITIAL_ENERGY);
    }

    /** @brief constructor with initial energy - records the instance's initial energy */
    public LifeAgent(int initialEnergy) throws AgentIsDeadException {
        super();
        if (initialEnergy <= 0)
            throw new AgentIsDeadException("Creating a LifeAgent with invalid intitalEnergy");
        setEnergy(MY_INITIAL_ENERGY = initialEnergy);
    }

    public LifeAgent(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p);
        if (energy <= 0)
            throw new AgentIsDeadException("Creating a LifeAgent with invalid intitalEnergy");
        setEnergy(MY_INITIAL_ENERGY = energy);
    }


    /** @return the energy of the LifeAgent */
    @Override
    final public Integer getEnergy() { return energy; }

    /**
     * @param e the amount to decrease the energy by
     * @throws AgentIsDeadException
     */
    @Override
    final public void decreaseEnergy(int e) throws AgentIsDeadException { changeEnergyBy(-e); }

    /**
     * @brief sets the new energy on the LifeAgent
     * @param energy
     * @throws AgentIsDeadException
     */
    final public void setEnergy(Integer energy) throws AgentIsDeadException {
        if (!isAlive()) {
            throw new AgentIsDeadException("Can't setEnergy on a dead LifeAgent");
        }

        this.energy = energy;
        if (this.energy <= 0)
            die();
    }

    /** @brief kill the poor LifeAgent, there's no coming back after this. The energy is set to 0. */
    @Override
    final public void die() throws AgentIsDeadException {
        if (!isAlive()) {
            throw new AgentIsDeadException("Can't kill a LifeAgent twice. You're mean.");
        }
        died = true;
        energy = 0;
        System.out.println(toString() + "died. ");
    }

    /**
     * @brief is a convenience method to change the energy by @param val which can be positive (for energy gain) and negative
     * (for energy loss)
     * */
    public void changeEnergyBy(int val) throws AgentIsDeadException { setEnergy(getEnergy() + val); }

    /** @return true if the LifeAgent instance is still alive, false otherwise */
    public boolean isAlive() { return !died; }


    /**
     * @brief decrease the consumable's energy by @param e
     * @param consumable the consumable to be consumed
     * @param e
     * @return
     */
    @Override
    public final void consumeBy(Consumable consumable, int e) throws ConsumableOutOfEnergy, AgentIsDeadException {
        if (consumable.getEnergy() < e) {
            throw new ConsumableOutOfEnergy();
        }
        consumable.decreaseEnergy(e);
    }

    // TODO(sami): replace with a new exception
    @Override
    public final boolean consume(Consumable consumable) throws AgentIsDeadException {
        if (consumable == this)
            throw new IllegalArgumentException("Cannot consume myself!");
        try {
            consumable.die();
            return true;
        }
        catch(AgentIsDeadException exc) {
            return false;
        }
    }

    @Override
    public final int consumeAll(List<Consumable> consumables) throws AgentIsDeadException {
        int count = 0;
        for (Consumable consumable : consumables) {
            // if consume succeeds increment count
            if (consume(consumable))
                count++;
        }
        return count;
    }
}
