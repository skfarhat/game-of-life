/**
 * Created by Sami on 01/04/2017.
 */

/**
 * @class Exception class thrown when the creation of Grid object fails
 */
public class GridCreationException extends Exception {
    /** @brief default constructor */
    public GridCreationException() { super(); }

    /**
     * @brief exception constructor with msg
     * @param msg exception message
     * */
    public GridCreationException(String msg) { super(msg); }
}
