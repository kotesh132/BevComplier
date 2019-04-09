package com.p4.p416.expressions;

public interface IExpressionTree {
    public boolean isLeaf();
    public CastType getCast();
    public void setCast(CastType cast);
}
