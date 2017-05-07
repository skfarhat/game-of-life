package core.schedulers;

import core.Utils;

import java.util.List;

public class ProbabilisticScheduler<T> extends ListScheduler<T> {

    /**
     * constructor
     *
     * @param list
     */
    public ProbabilisticScheduler(List list) {
        super(list);
    }

    @Override
    public T next() {
        int nextIndex = Utils.randomPositiveInteger(list.size());
        return list.get(nextIndex);
    }
}
