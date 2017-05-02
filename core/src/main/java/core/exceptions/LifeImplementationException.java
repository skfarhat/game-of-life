package core.exceptions;

/**
 * Exception thrown when there has been an issue with an implementatin in Life or LifeAgent subclasses
 * generally caused to core.exceptions raised by reflection/
 */
public class LifeImplementationException extends RuntimeException {
    public LifeImplementationException() { super(); }
    public LifeImplementationException(String msg) { super(msg); }
}
