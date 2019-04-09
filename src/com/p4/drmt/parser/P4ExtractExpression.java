package com.p4.drmt.parser;

import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ExpressionContextExt;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class P4ExtractExpression {
    private final List<AssignmentStatementContextExt> assignments = new ArrayList<>();
    private final ExpressionContextExt extractExpression;

    public void simplify(){

    }

}
