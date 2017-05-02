package core.exceptions;


public class AgentAlreadyDeadException extends LifeException {

    public AgentAlreadyDeadException() { super(); }

    public AgentAlreadyDeadException(String message) { super(message); }

}
