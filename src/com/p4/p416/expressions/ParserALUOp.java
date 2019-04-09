package com.p4.p416.expressions;

import com.p4.drmt.parser.FW;
import com.p4.p416.gen.ExpressionContextExt;
import lombok.Data;

@Data
public class ParserALUOp {
    public static final int SHLAOPCODE = 13;
    public static final int SHLOPCODE = 7;
    private final ExpressionContextExt expressionContextExt;
    private final FW offset;
    private final FW mask;
    private final FW a; //op1
    private final FW b; //op2
    private final FW opCode;
    private final int relativeOffset;
    private final int size;
    private FW dpOffset = FW.ZERO_2BYTES;
    private String fieldName=null;
    public static final ParserALUOp defaultALUOP = new ParserALUOp(null, FW.ZERO_2BYTES, FW.ZERO_2BYTES, FW.ZERO_2BYTES, FW.ZERO_2BYTES, new FW(0,ExpressionTreeHelper.PARSEROPCODESIZE), 0,0);


}
