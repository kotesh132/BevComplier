package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.DirectApplicationContext;

public class DirectApplicationContextExt extends AbstractBaseExt {

	public DirectApplicationContextExt(DirectApplicationContext ctx) {
		super(ctx);
	}

	@Override
	public  DirectApplicationContext getContext(){
		return (DirectApplicationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DirectApplicationContext getContext(String str){
		return (DirectApplicationContext)new PopulateExtendedContextVisitor().visit(getParser(str).directApplication());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DirectApplicationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DirectApplicationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
}
