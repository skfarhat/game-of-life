package core.exceptions;

/**
 * @class Exception thrown when there has been an issue with an implementatin in Life or LifeAgent subclasses
 * generally caused to exceptions raised by reflection/
 */
public class LifeImplementationException extends LifeException {
    public LifeImplementationException() { super(); }
    public LifeImplementationException(String msg) { super(msg); }
}
