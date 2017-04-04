/**
 * Created by Sami on 03/04/2017.
 */
package core;

/**
 * something that can be consumed and die
 */
public interface Consumable {

    public void die() throws AgentIsDeadException;
}