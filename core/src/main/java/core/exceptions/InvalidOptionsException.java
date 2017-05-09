package core.exceptions;

/**
 * convenience exception class that can be used by other
 */
public class InvalidOptionsException extends LifeException {

    public InvalidOptionsException() {}

    public InvalidOptionsException(String message) {
        super(message);
    }
}
