package core.actions;

import core.Creature;
import core.LifeAgent;
import core.Point2D;

public class Move extends Action<Creature> {

    private Point2D from;
    private Point2D to;

    /**
     * @param from the source point
     * @param to the destination point
     */
    public Move(Creature agent, Point2D from, Point2D to){
        super(agent);

        this.from = from;
        this.to = to;
    }

    public Point2D getFrom() {
        return from;
    }

    public Point2D getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("[Move(%s): %s --> %s]", getAgent(), from, to);
    }
}
