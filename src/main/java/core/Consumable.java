/**
 * Created by Sami on 03/04/2017.
 */
package core;

import core.exceptions.AgentIsDeadException;

/**
 * something that can be consumed and die
 */
public interface Consumable {

    void die() throws AgentIsDeadException;

    Integer getEnergy();

}
