package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.OptAnnotationsContext;

public class OptAnnotationsContextExt extends AbstractBaseExt {

	public OptAnnotationsContextExt(OptAnnotationsContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  OptAnnotationsContext getContext(){
		return (OptAnnotationsContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OptAnnotationsContext getContext(String str){
		return (OptAnnotationsContext)new PopulateExtendedContextVisitor().visit(getParser(str).optAnnotations());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof OptAnnotationsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ OptAnnotationsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public void quadrupalize(boolean probe, List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		return;
	}
}
