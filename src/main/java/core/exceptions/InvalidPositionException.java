package core.exceptions;

public class InvalidPositionException extends LifeException {

    /** @brief default constructor */
    public InvalidPositionException() {super(); };

    /** @brief constructor with String message */
    public InvalidPositionException(String e) { super(e); };
}
