package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.AnnotationsContext;

public class AnnotationsContextExt extends AbstractBaseExt {

	public AnnotationsContextExt(AnnotationsContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  AnnotationsContext getContext(){
		return (AnnotationsContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public AnnotationsContext getContext(String str){
		return (AnnotationsContext)new PopulateExtendedContextVisitor().visit(getParser(str).annotations());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof AnnotationsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ AnnotationsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
