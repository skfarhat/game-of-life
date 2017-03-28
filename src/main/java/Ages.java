/**
 * Created by Sami on 28/03/2017.
 */
public interface Ages {

    /**
     * @brief age method designed to decrease the energy of an Alive
     * @throws AgentIsDeadException when the agent is already dead and we're still trying to age the poor thing
     */
    public void age() throws AgentIsDeadException;
}
