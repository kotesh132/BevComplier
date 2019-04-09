package com.p4.ds;

import com.p4.ids.IEdge;

import java.util.Objects;

public class BaseEdge implements IEdge {

    private Object source;
    private Object destination;

    public BaseEdge(Object source, Object destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public Object getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IEdge) {
            return (getSource() == ((IEdge) obj).getSource() && getDestination() == ((IEdge) obj).getDestination());
        }
        return super.equals(obj);

    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getDestination());
    }
}
