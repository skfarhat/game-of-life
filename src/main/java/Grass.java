/**
 * Created by Sami on 30/03/2017.
 */

/**
 * @class Grass
 *
 * Grass is a LifeAgent, it reproduces
 * Grass
 */
public class Grass extends LifeAgent {
    public Grass() throws AgentIsDeadException {
        super();
    }

    public Grass(int initialE) throws AgentIsDeadException {
        super(initialE);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Grass(this.MY_INITIAL_ENERGY);
    }
}
