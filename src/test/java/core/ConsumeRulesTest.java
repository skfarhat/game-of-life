package core;

import core.exceptions.LifeException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Sami on 23/04/2017.
 */
public class ConsumeRulesTest {
    @Test
    public void size() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        assertEquals(0, crs.size());
    }

    @Test
    public void isEmpty() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        assertTrue(crs.isEmpty());
    }

    @Test
    public void add() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        ConsumeRule cr = new ConsumeRule(Wolf.class, Deer.class);
        assertTrue(crs.add(cr));
        assertEquals(1, crs.size());
        assertFalse(crs.isEmpty());
        assertFalse(crs.add(cr)); // should not add duplicate
        assertEquals(1, crs.size());
        assertFalse(crs.isEmpty());
    }

    @Test
    public void remove() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        ConsumeRule cr = new ConsumeRule(Wolf.class, Deer.class);
        assertFalse(crs.remove(cr)); // can't remove something we haven't added
        assertTrue(crs.add(cr)); // add it
        assertTrue(crs.remove(cr)); // remove it
        assertEquals(0, crs.size());
        assertTrue(crs.isEmpty());
    }

    @Test
    public void containsSimple() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        ConsumeRule cr = new ConsumeRule(Wolf.class, Deer.class);
        crs.add(cr);
        assertTrue(crs.contains(cr));
    }

    @Test
    public void containsAfterRemoval() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        ConsumeRule cr = new ConsumeRule(Wolf.class, Deer.class);
        crs.add(cr);
        crs.remove(cr);
        assertFalse(crs.contains(cr));
    }

    @Test
    public void iteratorInitiallyEmpty() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        assertFalse(crs.iterator().hasNext());
    }

    @Test
    public void testAddNull() {
        ConsumeRules crs = new ConsumeRules();
        assertFalse(crs.add(null));
    }

    @Test
    public void testRemoveNull() {
        ConsumeRules crs = new ConsumeRules();
        assertFalse(crs.remove(null));
    }

    @Test
    public void iterator() throws Exception {

        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
                new ConsumeRule(Grass.class, Deer.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        final ArrayList<ConsumeRule> addedRules  = new ArrayList<>();
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            addedRules.add(rules[i]);
            crs.add(rules[i]);
        }

        // used to count the number of entries in the iterator
        int count = 0;
        Iterator<ConsumeRule> it= crs.iterator();
        while(it.hasNext()) {
            ConsumeRule cr = it.next();
            assertTrue(addedRules.contains(cr));
            addedRules.remove(cr);
            count++;
        }
        assertEquals(end-start+1, count);

    }

    @Test
    public void toArray() throws Exception {

    }

    @Test
    public void containsAll() throws Exception {
        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
                new ConsumeRule(Grass.class, Deer.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        final ArrayList<ConsumeRule> addedRules  = new ArrayList<>();
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            addedRules.add(rules[i]);
            crs.add(rules[i]);
        }

        assertTrue(crs.containsAll(addedRules));
    }

    @Test
    public void containsAll2() throws Exception {
        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        final ArrayList<ConsumeRule> addedRules  = new ArrayList<>();
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            addedRules.add(rules[i]);
            crs.add(rules[i]);
        }

        // add one more rule to addedRules but not to ConsumeRules
        addedRules.add(new ConsumeRule(Grass.class, Deer.class));
        assertFalse(crs.containsAll(addedRules));
    }

    @Test
    public void addAll() throws Exception {
        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
                new ConsumeRule(Grass.class, Deer.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        final ArrayList<ConsumeRule> addedRules  = new ArrayList<>();
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            addedRules.add(rules[i]);
        }

        assertTrue(crs.addAll(addedRules));
        assertEquals(addedRules.size(), crs.size());
        assertFalse(crs.isEmpty());
    }

    /**
     * test add all
     * @throws Exception
     */
    @Test
    public void addAll2() throws Exception {
        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
                new ConsumeRule(Grass.class, Deer.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        final ArrayList<ConsumeRule> addedRules  = new ArrayList<>();
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            addedRules.add(rules[i]);
        }

        assertTrue(crs.addAll(addedRules));
        assertEquals(addedRules.size(), crs.size());
        assertFalse(crs.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void retainAll() throws Exception {
        ConsumeRules crs = new ConsumeRules();
        crs.retainAll(null);
    }

    @Test
    public void removeAll() throws Exception {

    }

    @Test
    public void clear() throws Exception {
        // all possible rules
        ConsumeRule []rules = {
                new ConsumeRule(Wolf.class, Deer.class),
                new ConsumeRule(Wolf.class, Grass.class),
                new ConsumeRule(Deer.class, Wolf.class),
                new ConsumeRule(Deer.class, Grass.class),
                new ConsumeRule(Grass.class, Wolf.class),
                new ConsumeRule(Grass.class, Deer.class),
        };
        final int start = Utils.randomPositiveInteger(rules.length);
        final int end = Utils.randomIntegerInRange(start, rules.length-1);
        // list of rules to add to ConsumeRules, used to verify the iterator contains all the entries added
        ConsumeRules crs = new ConsumeRules();
        for (int i = start; i <= end; i++) {
            crs.add(rules[i]);
        }

        assertEquals(end-start+1, crs.size());
        crs.clear();
        assertTrue(crs.isEmpty());
        assertFalse(crs.iterator().hasNext());
    }

    @Test
    public void testConsumableClassesForAgent() throws LifeException {
        ConsumeRules crs = new ConsumeRules();
        crs.add(new ConsumeRule(Wolf.class, Deer.class));
        crs.add(new ConsumeRule(Wolf.class, Grass.class));
        List<Class<? extends LifeAgent>> list = crs.getConsumableClassesForAgent(Wolf.class);
        assertEquals(2, list.size());
        assertTrue(list.contains(Deer.class));
        assertTrue(list.contains(Grass.class));
    }

}