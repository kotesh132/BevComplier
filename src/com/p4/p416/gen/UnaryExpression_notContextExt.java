package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.UnaryExpression_notContext;

public class UnaryExpression_notContextExt extends AbstractBaseExt {

    public UnaryExpression_notContextExt(UnaryExpression_notContext ctx) {
        super(ctx);
    }

    @Override
    public  UnaryExpression_notContext getContext(){
        return (UnaryExpression_notContext)contexts.get(contexts.size()-1);
    }

    @Override
    public UnaryExpression_notContext getContext(String str){
        return (UnaryExpression_notContext)new PopulateExtendedContextVisitor().visit(getParser(str).unaryExpression_not());
    }

    @Override
    public void setContext(ParserRuleContext ctx){
        if(ctx != null){
            if(ctx instanceof UnaryExpression_notContext){
                addToContexts(ctx);
            } else {
                throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryExpression_notContext.class.getName());
            }
        } else {
            addToContexts(null);
        }
    }
}
