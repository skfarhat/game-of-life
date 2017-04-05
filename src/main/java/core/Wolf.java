package core;
import java.util.List;

/**
 * @classs Wolf
 */
public class Wolf extends LifeAgent implements Ages, Consumes {

    /** @@brief default constructor */
    public Wolf() throws AgentIsDeadException {
        super();
    }
    public Wolf(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        Wolf babyWolf = new Wolf();
        return babyWolf;
    }

    /** @brief method from interface Ages, LifeAgent loses energy equivalent to LifeAgent.ENERGY_DECREMENT_VAL */
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

    @Override
    public String toString() {
        return String.format("Wolf[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
