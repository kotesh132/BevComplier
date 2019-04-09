package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SwitchStatementContext;

public class SwitchStatementContextExt extends AbstractBaseExt {

	public SwitchStatementContextExt(SwitchStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  SwitchStatementContext getContext(){
		return (SwitchStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SwitchStatementContext getContext(String str){
		return (SwitchStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).switchStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SwitchStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SwitchStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//GL - ControlBlock START

	@Override
	public StatementType getStatementType()
	{
		return StatementType.SWITCH_STATEMENT;
	}
	//GL - ControlBlock END

	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}

}
