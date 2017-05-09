package core.interfaces;

import core.exceptions.AgentAlreadyDeadException;

/**
 * something that can be consumed and die
 */
public interface Consumable {

    /**
     *  decrease the Consumable's energy by @param e
     *
     * @param e the amount to decrease the energy by
     */
    void decreaseEnergy(int e) throws AgentAlreadyDeadException;

    /**  decrease the Consumable's energy entirely */
    void die() throws AgentAlreadyDeadException;

    Integer getEnergy();

}
