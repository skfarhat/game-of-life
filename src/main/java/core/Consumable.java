/**
 * Created by Sami on 03/04/2017.
 */
package core;

import core.exceptions.AgentIsDeadException;

/**
 * something that can be consumed and die
 */
public interface Consumable {

    /**
     *  decrease the Consumable's energy by @param e
     *
     * @param e the amount to decrease the energy by
     */
    void decreaseEnergy(int e) throws AgentIsDeadException;

    /**  decrease the Consumable's energy entirely */
    void die() throws AgentIsDeadException;

    Integer getEnergy();

}
