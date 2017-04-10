/**
 * Created by Sami on 30/03/2017.
 */

package core;
import core.exceptions.AgentIsDeadException;

import java.util.List;

/**
 * @interface Consumes
 */
public interface Consumes {

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    boolean consume(Consumable consumable) throws AgentIsDeadException;

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    int consumeAll(List<Consumable> consumables) throws AgentIsDeadException;
}
