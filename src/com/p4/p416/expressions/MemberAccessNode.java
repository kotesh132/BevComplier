package com.p4.p416.expressions;

import lombok.Data;

@Data
public class MemberAccessNode implements IExpressionTree{
    private final IExpressionTree expression;
    private final String member;
    private CastType cast;

    @Override
    public boolean isLeaf() {
        return false;
    }
}
