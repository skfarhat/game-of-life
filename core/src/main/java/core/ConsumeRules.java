package core;

import java.util.*;

/**
 * This class implements the Set interface backed by a Map which inserts ConsumeRules according to the Consumer in arrays.
 * The class implementation is optimised for fast consumeRulesForConsumer() calls.
 *
 * The internal cachedList is not modified on modification operations such as add(), remove(), addAll() and removeAll(), clear()
 * and only refreshed when read-related operations on this class are called such as size(), isEmpty(), iterator(), toArray()
 */
public class ConsumeRules implements Set<ConsumeRule> {

    private boolean cacheIsValid = true;

    private Map<Class<?extends LifeAgent>, Set<ConsumeRule>> map = new HashMap<>();

    /**
     * a list of all the consume rules. The list can become invalid in modification operations such as add, remove, addAll, removeAll
     * in which case refreshCachedList should be called.
     */
    private List<ConsumeRule> cachedList = new ArrayList<>();

    @Override
    public int size() {
        return getCachedList().size();
    }

    @Override
    public boolean isEmpty() {
        return 0 == size();
    }

    @Override
    public boolean contains(Object o) {
       Set<ConsumeRule> set = getSet(o);
       if (set == null)
           return false;
       else
           return set.contains(o);
    }

    @Override
    public Iterator<ConsumeRule> iterator() {
        return getCachedList().iterator();
    }

    @Override
    public Object[] toArray() {
        return getCachedList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * adds the consume rule to the set
     *
     * @param cr
     * @return true if the ConsumeRule is not already in the set and was correctly added
     */
    @Override
    public boolean add(ConsumeRule cr) {
        boolean b = addRule(cr);
        cacheIsValid = false;
        return b;
    }

    @Override
    public boolean remove(Object o) {

        Set<ConsumeRule> set = getSet(o);

        if (set == null)
            return false;
        boolean b = set.remove(o);
        cacheIsValid = false;
        return b;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (false == contains(o))
                return false;
        }
        return true;
    }

    /**
     * adds all the consume rules only if none of them are already in the set
     * @param c collection of consume rules to add
     * @return false when at least one of the consume rules is already present.
     */
    @Override
    public boolean addAll(Collection<? extends ConsumeRule> c) {

        // first check that none exist
        for (ConsumeRule cr : c) {
            if (true == contains(cr))
                return false;
        }

        for (ConsumeRule cr : c) {
            addRule(cr);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("ConsumeRules:retainAll not supported");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // first check that none exist
        for (Object cr : c) {
            if (false == contains(cr))
                return false;
        }

        for (Object cr : c) {
            remove(cr);
        }
        return true;
    }

    @Override
    public void clear() {

        // clear the lists first, then remove the lists from the map
        // in reality, we should be able to call map.clear() and with garbage collection all should be fine.
        // But to guard against future faulty implementations where the list references are passed,
        // then we would have an inconsistency, where the referenced list still contains the entries which in reality should
        // have been cleared. The below 'clearing' should at the very list alleviate the mentioned possible issue.
        for (Map.Entry<Class<? extends LifeAgent>, Set<ConsumeRule>> e : map.entrySet()) {
            map.get(e.getKey()).clear();
        }

        map.clear();
        cacheIsValid = false;
    }

    public List<Class<?extends LifeAgent>> getConsumableClassesForAgent(Class<? extends LifeAgent> agentClass) {
        ArrayList<Class<?extends LifeAgent>> ret = new ArrayList<>();
        Set<ConsumeRule> set = map.get(agentClass);

        if (null != set) {
            Iterator<ConsumeRule> it = set.iterator();
            while(it.hasNext()) {
                ret.add(it.next().getConsumable());
            }
        }
        return ret;
    }

    /**
     * @param o
     * @return the set associated with the Object passed. If the Object is not a ConsumeRule, null is returned. If the ConsumeRule is
     * not in the map, null is returned.
     */
    private Set<ConsumeRule> getSet(Object o) {
        if (null == o || false == o instanceof ConsumeRule)
            return null;
        ConsumeRule cr = (ConsumeRule) o;

        // if key not in map return false
        if (false == map.containsKey(cr.getConsumer()))
            return null;

        Set<ConsumeRule> set = map.get(cr.getConsumer());
        return set;
    }

    /**
     * adds the consume rule to the map. If this is the first ConsumeRule for a given consumer then the array list in map
     * is created.
     * @param cr the consume rule to add
     */
    private boolean addRule(ConsumeRule cr) {
        if (null == cr)
            return false;

        Set<ConsumeRule> set = getSet(cr);
        if (set == null) {
            set = new HashSet<>();
            map.put(cr.getConsumer(), set);
        }

        boolean b = set.add(cr);
        cacheIsValid = false;
        return b;
    }

    /**
     * clears the cachedList then reinserts all entries - method should be called after each
     */
    private void refreshCachedList() {
        cachedList.clear();
        for (Map.Entry<Class<? extends LifeAgent>, Set<ConsumeRule>> e : map.entrySet()) {
            cachedList.addAll(e.getValue());
        }
        cacheIsValid = true;
    }

    /**
     * @return the cachedList ensuring the it is always fresh and up to date. Internal methods should call this method
     * for getting references to the cachedList instead of directly refrencing the list with 'this'
     */
    private List<ConsumeRule> getCachedList() {
        if (false == cacheIsValid) {
            refreshCachedList();
        }
        return cachedList;
    }
}
