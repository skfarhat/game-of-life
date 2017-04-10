package core.exceptions;

public class AgentIsDeadException extends LifeException {

    public AgentIsDeadException() { super(); }

    public AgentIsDeadException(String message) { super(message); }

}
