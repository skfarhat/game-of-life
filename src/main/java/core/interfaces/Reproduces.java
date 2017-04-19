package core.interfaces;

import core.LifeAgent;
import core.exceptions.AgentIsDeadException;
import core.exceptions.LifeException;

/**
 * Created by Sami on 28/03/2017.
 */
public interface Reproduces {

    LifeAgent reproduce() throws AgentIsDeadException;
}
