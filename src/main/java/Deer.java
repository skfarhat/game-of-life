import java.util.List;

/**
 * Created by Sami on 30/03/2017.
 */
public class Deer extends LifeAgent implements Ages, Consumes, Moveable {

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

    /** @brief method from interface Ages, LifeAgent loses energy equivalent to LifeAgent.ENERGY_DECREMENT_VAL */
    public void consume(LifeAgent prey) throws AgentIsDeadException {
        try {
            prey.die();
        } catch (AgentIsDeadException e) {
            throw new AgentIsDeadException("Cannot consume an already dead prey!");
        }
    }

    /** @brief method from interface Consumes, Deer will consume grass agents */
    public void consumeAll(List<LifeAgent> agents) throws AgentIsDeadException {
        for (LifeAgent a : agents)
            consume(a);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(this.MY_INITIAL_ENERGY);
    }

    /** @brief change the position of Wolf */
    public void moveTo(Point2D pos) {
        pos.set(pos);
    }
}
