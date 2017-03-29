/**
 * Created by Sami on 28/03/2017.
 */

/**
 *
 */
public class Wolf extends LifeAgent implements Ages {

    /** @brief energy gained by the wolf when it consumes a deer */
    private static final int E_WOLF = 1;

    /** @@brief default constructor */
    public Wolf() throws AgentIsDeadException {
        super();
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        Wolf babyWolf = new Wolf();
        return babyWolf;
    }

    public void age() throws AgentIsDeadException {
        decreaseEnergy();
    }
}
