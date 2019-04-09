package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.UnaryExpression_minusContext;

public class UnaryExpression_minusContextExt extends AbstractBaseExt {

    public UnaryExpression_minusContextExt(UnaryExpression_minusContext ctx) {
        super(ctx);
    }

    @Override
    public  UnaryExpression_minusContext getContext(){
        return (UnaryExpression_minusContext)contexts.get(contexts.size()-1);
    }

    @Override
    public UnaryExpression_minusContext getContext(String str){
        return (UnaryExpression_minusContext)new PopulateExtendedContextVisitor().visit(getParser(str).unaryExpression_not());
    }

    @Override
    public void setContext(ParserRuleContext ctx){
        if(ctx != null){
            if(ctx instanceof UnaryExpression_minusContext){
                addToContexts(ctx);
            } else {
                throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryExpression_minusContext.class.getName());
            }
        } else {
            addToContexts(null);
        }
    }
}
