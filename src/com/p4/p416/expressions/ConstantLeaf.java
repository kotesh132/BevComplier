package com.p4.p416.expressions;

import com.p4.drmt.parser.FW;
import lombok.Data;

@Data
public class ConstantLeaf implements IExpressionTree {
    private final FW number;
    private CastType cast;

    @Override
    public boolean isLeaf() {
        return true;
    }
}
