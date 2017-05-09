import core.IdPool;
import org.junit.Test;
import static org.junit.Assert.*;

public class IdPoolTest {

    @Test
    public void testIdPoolReturnsInstance() {
        IdPool pool = IdPool.getInstance();
        assertNotNull(pool);
    }

    @Test
    public void testIdPoolCreatesId() {
        IdPool pool = IdPool.getInstance();
        String id = pool.newId();
        assertNotNull(id);
        assertTrue(pool.idExists(id));
    }

}
