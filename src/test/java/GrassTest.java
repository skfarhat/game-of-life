import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

/**
 * Created by Sami on 30/03/2017.
 */
public class GrassTest {

    @Test
    public void testDefaultGrass() throws AgentIsDeadException {
        new Grass();
    }

    @Test
    public void testGrassWithInitialEnergy() throws AgentIsDeadException {
        final int INITIAL_ENERGY = new Random().nextInt(Integer.MAX_VALUE);
        new Grass(INITIAL_ENERGY);
    }

    @Test
    public void testGrassReproductionGivesNewGrass() throws AgentIsDeadException {
        Grass grass = new Grass();
        LifeAgent agent = grass.reproduce();
        assertTrue(agent instanceof Grass);
    }

}