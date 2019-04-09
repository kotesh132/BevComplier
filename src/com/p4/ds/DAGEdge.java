package com.p4.ds;

import com.p4.ids.IDAGEdge;

import java.util.Objects;

public class DAGEdge extends BaseEdge implements IDAGEdge {

    private int delay;

    public DAGEdge(Object source, Object destination, int delay) {
        super(source, destination);
        this.delay = delay;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DAGEdge) {
            return (super.equals(obj) && this.delay == ((DAGEdge) obj).getDelay());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getDestination(), delay);
    }
}
