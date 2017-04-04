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
    public void consume(Consumable consumable) throws AgentIsDeadException {
        consumable.die();
    }

    @Override
    public void consumeAll(List<Consumable> consumables) throws AgentIsDeadException {
        for (Consumable consumable: consumables) {
            consumable.die();
        }
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(this.MY_INITIAL_ENERGY);
    }

    @Override
    public String toString() {
        return "Deer (" + getEnergy() + ")";
    }
}
