package core;

import core.exceptions.LifeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsumeRuleTest {

    @Test (expected = LifeException.class)
    public void exceptionWhenConsumerConsumeeAreTheSame() throws LifeException {
        new ConsumeRule(Wolf.class, Wolf.class);
    }

    @Test
    public void testEquals() throws LifeException {
        ConsumeRule cr1 = new ConsumeRule(Wolf.class, Deer.class);
        ConsumeRule cr2 = new ConsumeRule(Wolf.class, Deer.class);
        ConsumeRule cr3 = new ConsumeRule(Wolf.class, Grass.class);
        ConsumeRule cr4 = new ConsumeRule(Grass.class, Deer.class);

        // Same consumer/consumable
        assertEquals(cr1, cr2);

        // Equals to itself
        assertEquals(cr1, cr1);
        assertEquals(cr2, cr2);

        // Different consumer/consumable
        assertNotEquals(cr1, cr3);
        assertNotEquals(cr2, cr3);
        assertNotEquals(cr1, cr4);

        // different object type
        Object o = new Object();
        assertNotEquals(cr1, o);

        //
        assertFalse(cr1.equals(null));

    }

    @Test
    public void testGetConsumable() throws Exception {
        ConsumeRule cr1 = new ConsumeRule(Wolf.class, Deer.class);
        assertEquals(Deer.class, cr1.getConsumable());
    }

    @Test
    public void testGetConsumer() throws Exception {
        ConsumeRule cr1 = new ConsumeRule(Wolf.class, Deer.class);
        assertEquals(Wolf.class, cr1.getConsumer());
    }

    @Test
    public void testHashCode() throws Exception {
        ConsumeRule cr1 = new ConsumeRule(Wolf.class, Deer.class);
        ConsumeRule cr2 = new ConsumeRule(Wolf.class, Deer.class);
        assertEquals(cr1.hashCode(), cr2.hashCode());
    }

}