/**
 * Created by Sami on 30/03/2017.
 */

package core;

import core.exceptions.AgentIsDeadException;

/**
 * @class Grass is a LifeAgent, it reproduces
 */
public class Grass extends LifeAgent {

    public Grass() throws AgentIsDeadException {
        super();
    }

    public Grass(Integer initialE) throws AgentIsDeadException {
        super(initialE);
    }

    public Grass(Point2D p, Integer initialE) throws AgentIsDeadException {
        super(p, initialE);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Grass(this.MY_INITIAL_ENERGY);
    }

    @Override
    public String toString() {
        return String.format("Grass[%s](e=%d)", getId().substring(0, 5), getEnergy());
    }
}
