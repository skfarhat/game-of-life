package core.schedulers;

/**
 *
 * @param <T>
 */
public interface Scheduler<T> {

    /** @return next scheduled element */
    T next();
}
