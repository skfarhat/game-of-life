package core.interfaces;

import core.LifeAgent;
import core.exceptions.AgentAlreadyDeadException;

public interface Reproduces {

    LifeAgent reproduce() throws AgentAlreadyDeadException;
}
