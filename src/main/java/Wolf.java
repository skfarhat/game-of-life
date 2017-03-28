/**
 * Created by Sami on 28/03/2017.
 */

/**
 *
 */
public class Wolf extends LifeAgent implements Ages, Reproduces {

    /** @brief energy gained by the wolf when it consumes a deer */
    private static final int E_WOLF = 1;

    /** @@brief default constructor */
    public Wolf() throws AgentIsDeadException {
        super();
    }

    public LifeAgent reproduce() {
        Wolf babyWolf = null;
        try {
            babyWolf = new Wolf();
        } catch (AgentIsDeadException e) {
            e.printStackTrace();
        }
        return babyWolf;
    }

    public void age() throws AgentIsDeadException {
        decreaseEnergy();
    }
}
