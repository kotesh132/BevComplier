package com.p4.p416.expressions;

import com.p4.drmt.parser.FW;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

public interface PAluOffset extends Summarizable{
    public Utils.Pair<FW, FW> getOffsetMask(ExpressionContextExt expressionContextExt);
    public int getRelativeOffset(ExpressionContextExt expressionContextExt);
    public int getCandidateSize(ExpressionContextExt expressionContextExt);
}
