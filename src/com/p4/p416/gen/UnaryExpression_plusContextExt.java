package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.UnaryExpression_plusContext;

public class UnaryExpression_plusContextExt extends AbstractBaseExt {

    public UnaryExpression_plusContextExt(UnaryExpression_plusContext ctx) {
        super(ctx);
    }

    @Override
    public  UnaryExpression_plusContext getContext(){
        return (UnaryExpression_plusContext)contexts.get(contexts.size()-1);
    }

    @Override
    public UnaryExpression_plusContext getContext(String str){
        return (UnaryExpression_plusContext)new PopulateExtendedContextVisitor().visit(getParser(str).unaryExpression_not());
    }

    @Override
    public void setContext(ParserRuleContext ctx){
        if(ctx != null){
            if(ctx instanceof UnaryExpression_plusContext){
                addToContexts(ctx);
            } else {
                throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryExpression_plusContext.class.getName());
            }
        } else {
            addToContexts(null);
        }
    }
}
