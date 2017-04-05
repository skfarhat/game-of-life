/**
 * Created by Sami on 30/03/2017.
 */

package core;

import java.util.List;

public class Deer extends LifeAgent implements Ages, Consumes {

    /** @brief default constructor, calls LifeAgent's constructor */
    public Deer() throws AgentIsDeadException {
        super();
    }

    /**
     * @brief constructor for Deer with @param initial energy
     * @param initialE initialEnergy of the deer
     */
    public Deer(int initialE) throws AgentIsDeadException {
        super(initialE);
    }

    /** @brief */
    public void ageBy(int val) throws AgentIsDeadException {
        decreaseEnergyBy(val);
    }

    @Override
    public boolean consume(Consumable consumable) throws AgentIsDeadException {
        try {
            consumable.die();
            return true;
        }
        catch(AgentIsDeadException exc) {
            return false;
        }
    }

    @Override
    public int consumeAll(List<Consumable> consumables) throws AgentIsDeadException {
        int count = 0;
        for (Consumable consumable : consumables) {
            // if consume succeeds increment count
            if (consume(consumable))
                count++;
        }
        return count;
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(this.MY_INITIAL_ENERGY);
    }

    @Override
    public String toString() {
        return String.format("Deer[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
