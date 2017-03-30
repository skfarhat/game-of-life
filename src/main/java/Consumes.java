/**
 * Created by Sami on 30/03/2017.
 */

import java.util.List;

/**
 * @interface Consumes
 *
 *
 */
public interface Consumes {

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    public void consume(LifeAgent agent) throws AgentIsDeadException;

    /** @brief LifeAgent can only consume other LifeAgents (they cannot consume an Agent with no energy) */
    public void consumeAll(List<LifeAgent> agents) throws AgentIsDeadException;
}
