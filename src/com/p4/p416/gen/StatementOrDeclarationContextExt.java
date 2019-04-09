package com.p4.p416.gen;

import java.util.Map;

import com.p4.p416.iface.IStatementOrDeclaration;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.p416.gen.P416Parser.StatementOrDeclarationContext;

public class StatementOrDeclarationContextExt extends AbstractBaseExt implements IStatementOrDeclaration {

	public StatementOrDeclarationContextExt(StatementOrDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  StatementOrDeclarationContext getContext(){
		return (StatementOrDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StatementOrDeclarationContext getContext(String str){
		return (StatementOrDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).statementOrDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StatementOrDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StatementOrDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	//GL - ControlBlock -START
	@Override
	public void getStatements(Map<StatementOrDeclarationContextExt,StatementType>  stmts){
		StatementType statementType = super.getStatementType();
		stmts.put(this, statementType);
		return;
	}
	
	//GL - ControlBlock -END
	
	@Override
	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			StatementOrDeclarationContext child = (StatementOrDeclarationContext) getContext();
			if(child.variableDeclaration()!=null){
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(child.variableDeclaration()));
			}else if(child.constantDeclaration()!=null){
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(child.constantDeclaration()));
			}else if(child.statement()!=null){
				this.cFGBuildingBlock = getExtendedContext(child.statement()).buildNGetCFG(cfgmap);
			}else if(child.instantiation()!=null){
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(child.instantiation()));
			}else{
				throw new IllegalStateException("Wrong Rule");
			}
		}
		return this.cFGBuildingBlock;
		
	}
}
