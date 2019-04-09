package com.p4.p416.gen;

import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.AnnotationContext;

public abstract class AnnotationContextExt extends AbstractBaseExt {
	
	public AnnotationContextExt(ParserRuleContext context) {
		super(context);
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof AnnotationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ AnnotationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	public abstract String getInlineAnnotationString(Integer instanceNumber); 

}
