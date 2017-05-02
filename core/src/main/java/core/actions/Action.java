package core.actions;

import core.Agent;

public class Action<T extends Agent> {

    private T agent;

    public Action(T t) {
        this.agent = t;
    }

    public T getAgent() {
        return agent;
    }
}
