package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.AnnotationContext;
import com.p4.p416.gen.P416Parser.SimpleAnnotationContext;

public class SimpleAnnotationContextExt extends AnnotationContextExt {

	public SimpleAnnotationContextExt(SimpleAnnotationContext ctx) {
		super(ctx);
	}

	@Override
	public  AnnotationContext getContext(){
		return (AnnotationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public AnnotationContext getContext(String str){
		return (AnnotationContext)new PopulateExtendedContextVisitor().visit(getParser(str).annotation());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SimpleAnnotationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SimpleAnnotationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public String getInlineAnnotationString(Integer instanceNumber) {
		return getFormattedText();
	}
}
