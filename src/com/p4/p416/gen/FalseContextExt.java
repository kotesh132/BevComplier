package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.FalseContext;

public class FalseContextExt extends ExpressionContextExt {

	public FalseContextExt(FalseContext ctx) {
		super(ctx);
	}

	@Override
	public  FalseContext getContext(){
		return (FalseContext)contexts.get(contexts.size()-1);
	}

	@Override
	public FalseContext getContext(String str){
		return (FalseContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof FalseContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ FalseContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override public int getResultSize(){return 1;}
	@Override
	public boolean isTerminal() {
		return true;
	}
	
	@Override
	public String getTypeName(){
		return "BooleanLiteralFalse";
	}
	
	@Override
	public Type getType(){
		return this;
	}
}
