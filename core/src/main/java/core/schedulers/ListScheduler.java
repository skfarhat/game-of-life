package core.schedulers;

import java.util.List;

/**
 * abstract class of a list scheduler
 * @param <T>
 */
public abstract class ListScheduler<T> implements Scheduler<T> {

    /** list of elements from which elements are scheduled */
    protected List<T> list;

    /** constructor */
    public ListScheduler(List<T> list) {
        this.list = list;
    }

}
