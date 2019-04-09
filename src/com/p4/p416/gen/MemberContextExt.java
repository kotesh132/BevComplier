package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class MemberContextExt extends AbstractBaseExt {

	public MemberContextExt(MemberContext ctx) {
		super(ctx);
	}

	@Override
	public  MemberContext getContext(){
		return (MemberContext)contexts.get(contexts.size()-1);
	}

	@Override
	public MemberContext getContext(String str){
		return (MemberContext)new PopulateExtendedContextVisitor().visit(getParser(str).member());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MemberContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MemberContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	public Type getType(){
		return getExtendedContext(getContext().name()).getType();
	}
}
