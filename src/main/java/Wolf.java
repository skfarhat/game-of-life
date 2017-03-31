/**
 * Created by Sami on 28/03/2017.
 */

import java.util.List;

/**
 *
 */
public class Wolf extends LifeAgent implements Ages, Consumes, Moveable {

    /** @brief energy gained by the wolf when it consumes a deer */
    private static final int E_WOLF = 1;

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

    public void consume(LifeAgent agent) {
        // TODO: check if agent is consumable
        // consume if so
    }

    public void consumeAll(List<LifeAgent> agents) throws AgentIsDeadException {
        for (LifeAgent a : agents)
            consume(a);
    }

    /** @brief change the position of Wolf */
    public void moveTo(Point2D pos) {
        this.getPos().set(pos);
    }
}
