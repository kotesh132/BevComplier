package com.p4.p416.expressions;

import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.FW;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.utils.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class VariableLeaf implements IExpressionTree, PAluOffset{
    private static final Logger L = LoggerFactory.getLogger(VariableLeaf.class);
    private static final int Byte2 = ExpressionTreeHelper.PARSEROPSIZE;
    private final String varName;
    private CastType cast;

    @Override
    public boolean isLeaf() {
        return true;
    }
    @Override
    public Utils.Pair<FW, FW> getOffsetMask(ExpressionContextExt expressionContextExt){
        String fieldName = varName;
        L.info(varName);
        int index = fieldName.lastIndexOf(".");
        String structName = fieldName.substring(0, index);
        Symbol s = expressionContextExt.resolve(structName);
        int structSize = s.getSizeInBits();
        Symbol s1 = expressionContextExt.resolve(varName);
        int offset = s1.getBitOffset();
        int fSize = s1.getSizeInBits();
        assert(fSize<=Byte2);
        int posOffset = structSize - (offset+fSize-1);
        L.info("Index backwards: "+ posOffset);
        FW aoffset = new FW(ByteUtils.nBit2sComplement(Byte2, posOffset), Byte2);
        int startPos = fSize-1;
        int endPos = 0;
        FW mask = ByteUtils.createMsbMask(Byte2, startPos, endPos);
        return Utils.Pair.of(aoffset,mask);
    }

    @Override
    public int getRelativeOffset(ExpressionContextExt expressionContextExt) {
        String fieldName = varName;
        L.info(varName);
        int index = fieldName.lastIndexOf(".");
        String structName = fieldName.substring(0, index);
        Symbol s = expressionContextExt.resolve(structName);
        int structSize = s.getSizeInBits();
        Symbol s1 = expressionContextExt.resolve(varName);
        int offset = s1.getBitOffset();
        int fSize = s1.getSizeInBits();
        assert(fSize<=Byte2);
        int posOffset = structSize - offset;
        return ByteUtils.nBit2sComplement(Byte2, posOffset);
    }

    @Override
    public int getCandidateSize(ExpressionContextExt expressionContextExt) {
        Symbol s1 = expressionContextExt.resolve(varName);
        return s1.getSizeInBits();
    }

    int getAbsoluteOffset(AbstractBaseExt ctx){
        Symbol s = ctx.resolve(varName);
        int offset = MemoryManager.getOffset(varName, (Type) s);
        return offset*8 +256;
    }

    @Override
    public String summary() {
        return varName;
    }
}
