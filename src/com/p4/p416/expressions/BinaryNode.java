package com.p4.p416.expressions;

import lombok.Data;

@Data
public class BinaryNode implements IExpressionTree {
    private final IExpressionTree left;
    private final IExpressionTree right;
    private final String operator;
    private CastType cast;

    @Override
    public boolean isLeaf() {
        return false;
    }
}
