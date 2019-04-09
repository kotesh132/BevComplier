package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.TrueContext;

public class TrueContextExt extends ExpressionContextExt {

	public TrueContextExt(TrueContext ctx) {
		super(ctx);
	}

	@Override
	public  TrueContext getContext(){
		return (TrueContext)contexts.get(contexts.size()-1);
	}

	@Override
	public TrueContext getContext(String str){
		return (TrueContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TrueContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TrueContext.class.getName());
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
		return "BooleanLiteralTrue";
	}
	
	@Override
	public Type getType(){
		return this;
	}
}
