package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ExitStatementContext;

public class ExitStatementContextExt extends AbstractBaseExt {

	public ExitStatementContextExt(ExitStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  ExitStatementContext getContext(){
		return (ExitStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ExitStatementContext getContext(String str){
		return (ExitStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).exitStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExitStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExitStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	//GL - ControlBlock START

	@Override
	public StatementType getStatementType()
	{
		return StatementType.EXIT_STATEMENT;
	}
	//GL - ControlBlock END
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
}
