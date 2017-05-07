package core.schedulers;

public interface Scheduler<T> {

    /** @return next scheduled element */
    T next();
}
