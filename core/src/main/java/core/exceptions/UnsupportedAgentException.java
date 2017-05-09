package core.exceptions;

import core.Agent;

public class UnsupportedAgentException extends LifeRuntimeException {

    public UnsupportedAgentException() {}

    public UnsupportedAgentException(Class<?extends Agent> cls) {
        this(String.format("Agent %s type not supported!", cls.getName()));
    }

    public UnsupportedAgentException(String message) {
        super(message);
    }
}
