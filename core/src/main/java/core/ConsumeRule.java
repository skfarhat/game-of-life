package core;

import core.exceptions.LifeException;

public class ConsumeRule {

    private Class<?extends LifeAgent> consumer;

    private Class<?extends LifeAgent> consumable;

    public ConsumeRule(Class<? extends LifeAgent> consumer, Class<? extends LifeAgent> consumable) throws LifeException {
        this.consumer = consumer;
        this.consumable = consumable;
        if (consumable.equals(consumer)) {
            throw new LifeException("Cannot create rule where LifeAgent consumes itself");
        }
    }

    /** copy constructor */
    public ConsumeRule(ConsumeRule cr) {
        set(cr);
    }

    public void set(ConsumeRule cr) {
        this.consumer = cr.getConsumer();
        this.consumable = cr.getConsumable();
    }

    public Class<? extends LifeAgent> getConsumable() {
        return consumable;
    }

    public Class<? extends LifeAgent> getConsumer() {
        return consumer;
    }

    /**
     * hashcode is dependent on the consumer and consumable only
     * @return
     */
    @Override
    public int hashCode() {
        return consumable.hashCode() * 31 + consumer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof ConsumeRule)) return false;
        ConsumeRule cr = (ConsumeRule) obj;
        return cr.getConsumable().equals(getConsumable()) && cr.getConsumer().equals(getConsumer());
    }

    @Override
    public String toString() {
        return getConsumer().getSimpleName() + " -> " + getConsumable().getSimpleName();
    }
}
