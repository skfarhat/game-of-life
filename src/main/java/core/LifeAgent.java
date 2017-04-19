/**
 * Created by Sami on 28/03/2017.
 */
package core;

import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.ConsumableOutOfEnergy;
import core.interfaces.Consumable;
import core.interfaces.Consumes;
import core.interfaces.Reproduces;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  LifeAgent
 *
 * Every LifeAgent is an Agent
 * Every LifeAgent has energy
 * Every LifeAgent has a position
 *
 * Not every LifeAgent ages (think of Grass)
 */
public abstract class LifeAgent extends Agent implements Reproduces, Consumable, Consumes {

    /**
     *  logger
     */
    private static final Logger LOGGER = Logger.getLogger(Life.class.getName());

    /**  this string must match the method name of getDefaultParams */
    public static final String METHOD_NAME_GET_DEFAULT_PARAMS = "getDefaultParams";

    /**
     * it is not essential that subclasses to worry about implementing getDefaultParams
     * so a default implementation returning null
     * @return default parameters for the LifeAgent class to be used in Life. These may well be overridden by Life.
     */
    public static LifeAgentOptions getDefaultParams() {
        return null;
    }

    /**  the energy with which this instance was created. This same value is used later when reproducing. */
    protected final Integer MY_INITIAL_ENERGY;

    /**  the energy of the LifeAgent */
    private Integer energy = 0;

    /**  is set to true when the Agent dies and prevents further changes to the energy after it death,
     * zombies are not allowed :)
     */
    private boolean died = false;

    /**  default constructor */
    public LifeAgent() throws AgentAlreadyDeadException {
        this(null, LifeAgentOptions.DEFAULT_E0);
    }

    public LifeAgent(Point2D p) throws AgentAlreadyDeadException {
        this(p, LifeAgentOptions.DEFAULT_E0);
    }

    /**  constructor with initial energy - records the instance's initial energy */
    public LifeAgent(int initialEnergy) throws AgentAlreadyDeadException {
        super();
        if (initialEnergy <= 0)
            throw new AgentAlreadyDeadException("Creating a LifeAgent with invalid intitalEnergy");
        setEnergy(MY_INITIAL_ENERGY = initialEnergy);
    }

    public LifeAgent(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p);
        if (energy <= 0)
            throw new AgentAlreadyDeadException("Creating a LifeAgent with invalid intitalEnergy");
        setEnergy(MY_INITIAL_ENERGY = energy);
    }

    /** @return the energy of the LifeAgent */
    @Override
    public final Integer getEnergy() { return energy; }

    /**
     * @param e the amount to decrease the energy by
     * @throws AgentAlreadyDeadException
     */
    @Override
    public final void decreaseEnergy(int e) throws AgentAlreadyDeadException { changeEnergyBy(-e); }

    /**
     *  sets the new energy on the LifeAgent
     * @param energy
     * @throws AgentAlreadyDeadException
     */
    public final void setEnergy(Integer energy) throws AgentAlreadyDeadException {
        if (!isAlive()) {
            throw new AgentAlreadyDeadException("Can't setEnergy on a dead LifeAgent");
        }

        this.energy = energy;
        if (this.energy <= 0)
            die();
    }

    /**  kill the poor LifeAgent, there's no coming back after this. The energy is set to 0. */
    @Override
    public final void die() throws AgentAlreadyDeadException {
        if (!isAlive()) {
            throw new AgentAlreadyDeadException("Can't kill a LifeAgent twice. You're mean.");
        }
        died = true;
        energy = 0;

        LOGGER.log(Level.FINE, toString() + "died. ");
    }

    /**
     *  is a convenience method to change the energy by @param val which can be positive (for energy gain) and negative
     * (for energy loss)
     * */
    public final void changeEnergyBy(int val) throws AgentAlreadyDeadException { setEnergy(getEnergy() + val); }

    /** @return true if the LifeAgent instance is still alive, false otherwise */
    public final boolean isAlive() { return !died; }


    /**
     *  decrease the consumable's energy by @param e
     * @param consumable the consumable to be consumed
     * @param e
     */
    @Override
    public final void consumeBy(Consumable consumable, int e) throws ConsumableOutOfEnergy, AgentAlreadyDeadException {
        if (consumable.getEnergy() < e) {
            throw new ConsumableOutOfEnergy();
        }
        consumable.decreaseEnergy(e);
    }

    // TODO(sami): replace with a new exception
    @Override
    public final boolean consume(Consumable consumable) throws AgentAlreadyDeadException {
        if (consumable == this)
            throw new IllegalArgumentException("Cannot consume myself!");
        try {
            consumable.die();
            return true;
        }
        catch(AgentAlreadyDeadException exc) {
            return false;
        }
    }

    @Override
    public final int consumeAll(List<Consumable> consumables) throws AgentAlreadyDeadException {
        int count = 0;
        for (Consumable consumable : consumables) {
            // if consume succeeds increment count
            if (consume(consumable))
                count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]%s(e=%d)", getClass().getSimpleName(), getId().substring(0, 5), getPos(), getEnergy());
    }
}
