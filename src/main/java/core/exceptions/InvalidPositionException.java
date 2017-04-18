package core.exceptions;

public class InvalidPositionException extends LifeException {

    /**  default constructor */
    public InvalidPositionException() {super(); };

    /**  constructor with String message */
    public InvalidPositionException(String e) { super(e); };
}
