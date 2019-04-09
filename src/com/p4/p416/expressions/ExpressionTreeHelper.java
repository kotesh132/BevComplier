package com.p4.p416.expressions;

import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.FW;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.*;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressionTreeHelper {
    public static final int PARSEROPCODESIZE = 4;
    public static final int PARSEROPSIZE = 16;

    private static final Logger L = LoggerFactory.getLogger(ExpressionTreeHelper.class);
    public static IExpressionTree compactExpression(ExpressionContextExt expressionContextExt){
        if(expressionContextExt instanceof CastContextExt){
            CastContextExt castContextExt = (CastContextExt) expressionContextExt;
            String castType = castContextExt.getTypeName();
            IExpressionTree iExpressionTree = compactExpression( (ExpressionContextExt) AbstractBaseExt.getExtendedContext(((P416Parser.CastContext)castContextExt.getContext()).expression()));
            iExpressionTree.setCast(new CastType(castType));
            return iExpressionTree;
        }else if(expressionContextExt instanceof OfContextExt){
            OfContextExt ofContextExt = (OfContextExt) expressionContextExt;
            IExpressionTree iExpressionTree = compactExpression((ExpressionContextExt) AbstractBaseExt.getExtendedContext(((P416Parser.OfContext)ofContextExt.getContext()).expression()));
            return iExpressionTree;
        }
        else if(expressionContextExt.isBinaryExpression()){
            IExpressionTree left = compactExpression((ExpressionContextExt) expressionContextExt.getLeft());
            IExpressionTree right = compactExpression((ExpressionContextExt) expressionContextExt.getRight());
            String op = expressionContextExt.getOperator();
            return new BinaryNode(left, right, op);
        }else if(expressionContextExt.isUnaryExpression()){
            IExpressionTree left = compactExpression((ExpressionContextExt) expressionContextExt.getLeft());
            String op = expressionContextExt.getOperator();
            return new BinaryNode(left, null, op);
        }else if(expressionContextExt instanceof IntegerContextExt){
            FW fw = ByteUtils.parseP4Number(expressionContextExt.getFormattedText());
            return new ConstantLeaf(fw);
        }else if(expressionContextExt instanceof ExprMemberAccessContextExt){
            ExprMemberAccessContextExt exprMemberAccessContextExt = (ExprMemberAccessContextExt) expressionContextExt;
            String member = AbstractBaseExt.getExtendedContext(((P416Parser.ExprMemberAccessContext)expressionContextExt.getContext()).member()).getFormattedText();
            IExpressionTree iExpressionTree = compactExpression((ExpressionContextExt) AbstractBaseExt.getExtendedContext(((P416Parser.ExprMemberAccessContext)expressionContextExt.getContext()).expression()));
            if(iExpressionTree instanceof LookaheadLeaf){
                //String member = ((P416Parser.ExprMemberAccessContext)expressionContextExt.getContext()).member().extendedContext.getFormattedText();
                LookaheadLeaf lookaheadLeaf = (LookaheadLeaf) iExpressionTree;
                lookaheadLeaf.setMemberAccess(member);
                return lookaheadLeaf;
            }else if(iExpressionTree instanceof VariableLeaf){
                VariableLeaf v = new VariableLeaf(((VariableLeaf) iExpressionTree).getVarName()+"."+member);
                v.setCast(iExpressionTree.getCast());
                return v;
            }else{
                throw new IllegalStateException();
            }
        }else if(expressionContextExt instanceof TempletizedFunctionCallContextExt){
            TempletizedFunctionCallContextExt templetizedFunctionCallContextExt= (TempletizedFunctionCallContextExt)expressionContextExt;
            if(templetizedFunctionCallContextExt.getFormattedText().contains(".lookahead<")){
                 P416Parser.TypeRefContext typeRefContext= ((P416Parser.TempletizedFunctionCallContext)expressionContextExt.getContext()).typeArgumentList().typeArg(0).typeRef();
                 if(typeRefContext.baseType()!=null){
                     if(typeRefContext.baseType() instanceof P416Parser.BitSizeTypeContext){
                         P416Parser.BitSizeTypeContext bitSizeTypeContext = (P416Parser.BitSizeTypeContext) typeRefContext.baseType();
                         String number  = BitSizeTypeContextExt.getExtendedContext(bitSizeTypeContext.number()).getFormattedText();
                         int size = Integer.parseInt(number);
                         return LookaheadLeaf.getLookAheadBase(size);
                     }else{
                         throw new UnsupportedOperationException("Can't support any other base type");
                     }
                 }else if(typeRefContext.typeName()!=null){
                     String typeName = AbstractBaseExt.getExtendedContext(typeRefContext.typeName()).getFormattedText();

                     Type type = expressionContextExt.resolve(typeName).getType();
                     L.info(type.getClass().getName());
                     int size = type.getTypeSize();
                     return LookaheadLeaf.getLookAheadNamed(typeName,size);
                 }else{
                     throw new UnsupportedOperationException("Can't support any other type");
                 }
            }else{
                throw new IllegalStateException("XXX");
            }
        }else if(expressionContextExt instanceof NonTypeContextExt){
            return new VariableLeaf(expressionContextExt.getFormattedText());
        }
        L.info(expressionContextExt.getClass().getName());
        L.info(expressionContextExt.getFormattedText());
        throw new IllegalStateException();
    }

    public static ParserALUOp getExtractExpression(ExpressionContextExt expressionContextExt, IExpressionTree expressionTree){
        if(expressionTree instanceof BinaryNode){
            BinaryNode bn = (BinaryNode) expressionTree;
            if(bn.getOperator().equals("-") || bn.getOperator().equals("+")){ // a*x+b format
                int a=0,b=0;
                PAluOffset x = null;
                IExpressionTree left = bn.getLeft();
                IExpressionTree right = bn.getRight();
                L.info("Found expression of format a*x + b");
                if(left instanceof BinaryNode){
                    BinaryNode inner = (BinaryNode) left;
                    if(inner.getOperator().equals("*")) {
                        IExpressionTree innerLeft = inner.getLeft();
                        if(innerLeft instanceof ConstantLeaf){
                            a = ((ConstantLeaf)innerLeft).getNumber().getValue();
                        }
                        IExpressionTree innerRight = inner.getRight();
                        if(innerRight instanceof LookaheadLeaf){
                            x = (LookaheadLeaf) innerRight;
                        }
                    }
                }else{
                    throw new IllegalStateException("Expression not simplifiable");
                }
                if(right instanceof ConstantLeaf){
                    b = ((ConstantLeaf)right).getNumber().getValue();
                }else {
                    throw new IllegalStateException("Expression not simplifiable");
                }
                Utils.Pair<FW, FW> offmask = x.getOffsetMask(expressionContextExt);

                L.info("a = "+a+", b = "+b+ ", x = "+x.summary());
                assert(a!=0 && (a & (a-1))==0);
                a = Utils.lg(a);
                FW opCode = new FW(ParserALUOp.SHLAOPCODE, PARSEROPCODESIZE);

                ParserALUOp parserALUOp = null;
                if(bn.getOperator().equals("-"))
                    b = ByteUtils.nBit2sComplement(16, b);
                return new ParserALUOp(expressionContextExt, offmask.first(), offmask.second(),
                        new FW(a, PARSEROPSIZE), new FW(b, PARSEROPSIZE), opCode,
                        x.getRelativeOffset(expressionContextExt), x.getCandidateSize(expressionContextExt));
            }else if(bn.getOperator().equals("*") ){ //a*x format
                int a = 0;
                PAluOffset x = null;
                IExpressionTree left = bn.getLeft();
                IExpressionTree right = bn.getRight();
                if(left instanceof ConstantLeaf){
                    a = ((ConstantLeaf)left).getNumber().getValue();
                }
                Utils.Pair<FW, FW> offmask=null;
                if(right instanceof LookaheadLeaf){
                    x = (LookaheadLeaf) right;
                    offmask = x.getOffsetMask(expressionContextExt);
                }else if(right instanceof VariableLeaf){
                    x = (VariableLeaf) right;
                    offmask = x.getOffsetMask(expressionContextExt);
                }else{
                    throw new UnsupportedOperationException();
                }

                assert(a!=0 && (a & (a-1))==0);//POW2 check
                a = Utils.lg(a);
                FW opCode = new FW(ParserALUOp.SHLAOPCODE, PARSEROPCODESIZE);
                ParserALUOp ret =  new ParserALUOp(expressionContextExt, offmask.first(), offmask.second(),
                        new FW(a, PARSEROPSIZE), FW.ZERO_2BYTES, opCode,
                        x.getRelativeOffset(expressionContextExt), x.getCandidateSize(expressionContextExt));
                if(right instanceof VariableLeaf){
                    ret.setFieldName(((VariableLeaf) right).getVarName());
                    int num = ((VariableLeaf) right).getAbsoluteOffset(expressionContextExt);
                    ret.setDpOffset(new FW(num,PARSEROPSIZE));
                }
                return ret;
            }
            else{
                throw new IllegalStateException("Not implemented");
            }
        }
        throw new IllegalStateException("Expression not simplifiable");

    }

}
