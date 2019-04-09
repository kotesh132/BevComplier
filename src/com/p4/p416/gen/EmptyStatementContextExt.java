package com.p4.p416.gen;

import java.util.List;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.EmptyStatementContext;

public class EmptyStatementContextExt extends AbstractBaseExt {

	public EmptyStatementContextExt(EmptyStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  EmptyStatementContext getContext(){
		return (EmptyStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public EmptyStatementContext getContext(String str){
		return (EmptyStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).emptyStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof EmptyStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ EmptyStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//GL - ControlBlock START

	@Override
	public StatementType getStatementType()
	{
		return StatementType.EMPTY_STATEMENT;
	}
	//GL - ControlBlock END
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
}
