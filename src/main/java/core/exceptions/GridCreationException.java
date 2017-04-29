/**
 * Created by Sami on 01/04/2017.
 */

package core.exceptions;

/**
 *  Exception class thrown when the creation of Grid object fails
 */
public class GridCreationException extends LifeRuntimeException {

    /**  default constructor */
    public GridCreationException() { super(); }

    /**
     *  exception constructor with msg
     * @param msg exception message
     * */
    public GridCreationException(String msg) { super(msg); }
}
