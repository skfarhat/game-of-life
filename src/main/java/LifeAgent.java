/**
 * Created by Sami on 28/03/2017.
 */
public class LifeAgent extends Agent {

    /** @brief value by which we decrement the energy when decreaseEnergy is called */
    private static final int ENERGY_DECREMENT_VAL = 1;

    /** @brief default value for the initial energy of a LifeAgent */
    private static final int DEFAULT_INITIAL_ENERGY = 10;

    /** @brief the energy of the LifeAgent */
    private Integer energy = 0;

    /** @brief is set to true when the Agent dies and prevents further changes to the energy after it death,
     * zombies are not allowed :)
     */
    private boolean died = false;

    /** @brief default constructor */
    public LifeAgent() throws AgentIsDeadException {
        this(DEFAULT_INITIAL_ENERGY);
    }

    /** @brief constructor with initial energy */
    public LifeAgent(int initialEnergy) throws AgentIsDeadException {
        try {
            setEnergy(initialEnergy);
        } catch (AgentIsDeadException e) {
            throw new AgentIsDeadException("Creating a LifeAgent with invalid intitalEnergy");
        }
    }

    /** @return the energy of the LifeAgent */
    final public Integer getEnergy() { return energy; }

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

    /** @brief kill the poor LifeAgent, there's no coming back after this */
    final public void die() throws AgentIsDeadException {
        if (!isAlive()) {
            throw new AgentIsDeadException("Can't kill a LifeAgent twice. You're mean.");
        }
        died = true;
    }

    /**
     * @brief is a convenience method to decrease the energy by ENERGY_DECREMENT_VAL,
     * and is not to be confused with the age() method in Age interface which might choose to call decreaseEnergy() or not
     * */
    public void decreaseEnergy() throws AgentIsDeadException { setEnergy(getEnergy()-ENERGY_DECREMENT_VAL); }

    /** @return true if the LifeAgent instance is still alive, false otherwise */
    final public boolean isAlive() { return !died; }

}
