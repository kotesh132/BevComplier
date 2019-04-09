package com.p4.p416.gen;

import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.ILValue;
import com.p4.p416.iface.IRangeIndexLvalue;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.RangeIndexLvalueContext;

public class RangeIndexLvalueContextExt extends LvalueContextExt implements IRangeIndexLvalue {

	public RangeIndexLvalueContextExt(RangeIndexLvalueContext ctx) {
		super(ctx);
	}

	@Override
	public  RangeIndexLvalueContext getContext(){
		return (RangeIndexLvalueContext)contexts.get(contexts.size()-1);
	}

	@Override
	public RangeIndexLvalueContext getContext(String str){
		return (RangeIndexLvalueContext)new PopulateExtendedContextVisitor().visit(getParser(str).lvalue());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof RangeIndexLvalueContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ RangeIndexLvalueContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public IExpression getFrom() {
		return (IExpression) getExtendedContext(getContext().expression(1));
	}

	@Override
	public IExpression getTo() {
		return (IExpression) getExtendedContext(getContext().expression(0));
	}

	@Override
	public ILValue getLValue() {
		return (ILValue) getExtendedContext(getContext().lvalue());
	}

}
