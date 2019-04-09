package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.TernaryContext;

public class TernaryContextExt extends ExpressionContextExt {
	
	private static final Logger L = LoggerFactory.getLogger(TernaryContextExt.class);

	public TernaryContextExt(TernaryContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TernaryContext getContext(){
		return (TernaryContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TernaryContext getContext(String str){
		return (TernaryContext)new PopulateExtendedContextVisitor().visit(getParser(str).expression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TernaryContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TernaryContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType(){
		Type type0 = getExtendedContext(getContext().expression(0)).getType();
		Type type1 = getExtendedContext(getContext().expression(1)).getType();
		Type type2 = getExtendedContext(getContext().expression(2)).getType();
		if ((type0 == null) || !("BoolType".equals(type0.getTypeName()) || "BooleanLiteralTrue".equals(type0.getTypeName()) || "BooleanLiteralFalse".equals(type0.getTypeName()))){
			L.error("Line:"+getContext().start.getLine()+": Ternary Expression should be boolean: "+getExtendedContext(getContext().expression(0)).getFormattedText());
		}

		if (!type1.isEquivalenceCompatible(type2)){
			L.error("Line:"+getContext().start.getLine()+": Ternary Expression types should match: "+getExtendedContext(getContext()).getFormattedText());
			return null;
		}
		
		//Extra check for Bit types and booleans. By default they are treated as implicitly cast-able.
		if (("BitType".equals(type1.getTypeName()) && ("BoolType".equals(type2.getTypeName()) || "BooleanLiteralTrue".equals(type2.getTypeName()) || "BooleanLiteralFalse".equals(type2.getTypeName())))
				||
				("BitType".equals(type2.getTypeName()) && ("BoolType".equals(type1.getTypeName()) || "BooleanLiteralTrue".equals(type1.getTypeName()) || "BooleanLiteralFalse".equals(type1.getTypeName())))
				){
			L.error("Line:"+getContext().start.getLine()+": Ternary Expression types should match: "+getExtendedContext(getContext()).getFormattedText());
		}
		return type1;
	}
}
