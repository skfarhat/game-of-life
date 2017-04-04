/**
 * Created by Sami on 30/03/2017.
 */

package core;
import java.util.List;

/**
 * @interface Consumes
 */
public interface Consumes {

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    void consume(Consumable consumable) throws AgentIsDeadException;

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    void consumeAll(List<Consumable> consumables) throws AgentIsDeadException;
}
