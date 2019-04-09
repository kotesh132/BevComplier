package com.p4.p416.expressions;

import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.FW;
import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

@Data
public class LookaheadLeaf implements IExpressionTree, Summarizable,PAluOffset{
    private static final int Byte2 = ExpressionTreeHelper.PARSEROPSIZE;
    private final String typeName;
    private final LookaheadType latype;
    private final int size;
    private String memberAccess; //Optional
    private CastType cast;

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String summary() {
        if(latype.equals(LookaheadType.BASE)){
            return "bit<"+size+">";
        }else{
            String ret = typeName;
            if(memberAccess !=null){
                ret = ret+"."+memberAccess;
            }
            return ret;
        }
    }

    public static enum LookaheadType{
        BASE,
        NAMED
    }

    public static LookaheadLeaf getLookAheadNamed(String name, int size){
        return new LookaheadLeaf(name, LookaheadType.NAMED, size);
    }
    public static LookaheadLeaf getLookAheadBase(int size){
        return new LookaheadLeaf("_base_", LookaheadType.BASE, size);
    }
    @Override
    public Utils.Pair<FW, FW> getOffsetMask(ExpressionContextExt expressionContextExt){
        if(latype.equals(LookaheadType.NAMED)) {
            if (memberAccess != null || !memberAccess.isEmpty()) {
                Symbol s = expressionContextExt.resolve(typeName+"."+memberAccess);
                int bitOffst = s.getBitOffset();
                int sizeOfMember = s.getSizeInBits();
                assert(sizeOfMember<=Byte2);
                int offSet = bitOffst+sizeOfMember-1;
                int startPos = sizeOfMember-1;
                int endPos = 0;
                FW mask = ByteUtils.createMsbMask(Byte2, startPos, endPos);
                FW off = new FW(offSet,Byte2);
                return Utils.Pair.of(off,mask);
            }else{
                FW mask = ByteUtils.createMsbMask(Byte2, Byte2-1,size-1);
                FW offset = new FW(size, Byte2);
                return Utils.Pair.of(offset,mask);
            }
        }else{
            FW mask = ByteUtils.createMsbMask(Byte2, Byte2-1,size-1);
            FW offset = new FW(size, Byte2);
            return Utils.Pair.of(offset,mask);
        }
    }
    @Override
    public int getRelativeOffset(ExpressionContextExt expressionContextExt){
        if(latype.equals(LookaheadType.NAMED)) {
            if (memberAccess != null || !memberAccess.isEmpty()) {
                Symbol s = expressionContextExt.resolve(typeName + "." + memberAccess);
                return s.getBitOffset();
            }
        }
        return 0;
    }
    @Override
    public int getCandidateSize(ExpressionContextExt expressionContextExt){
        if(latype.equals(LookaheadType.NAMED)) {
            if (memberAccess != null || !memberAccess.isEmpty()) {
                Symbol s = expressionContextExt.resolve(typeName + "." + memberAccess);
                return s.getSizeInBits();
            }
        }
        return size;
    }
}
