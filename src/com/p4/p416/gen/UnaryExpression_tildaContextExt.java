package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.UnaryExpression_tildaContext;

public class UnaryExpression_tildaContextExt extends AbstractBaseExt {

    public UnaryExpression_tildaContextExt(UnaryExpression_tildaContext ctx) {
        super(ctx);
    }

    @Override
    public  UnaryExpression_tildaContext getContext(){
        return (UnaryExpression_tildaContext)contexts.get(contexts.size()-1);
    }

    @Override
    public UnaryExpression_tildaContext getContext(String str){
        return (UnaryExpression_tildaContext)new PopulateExtendedContextVisitor().visit(getParser(str).unaryExpression_not());
    }

    @Override
    public void setContext(ParserRuleContext ctx){
        if(ctx != null){
            if(ctx instanceof UnaryExpression_tildaContext){
                addToContexts(ctx);
            } else {
                throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ UnaryExpression_tildaContext.class.getName());
            }
        } else {
            addToContexts(null);
        }
    }
}
