package com.p4.p416.gen;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class ErrorDeclarationContextExt extends AbstractBaseExt {

	public ErrorDeclarationContextExt(ErrorDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  ErrorDeclarationContext getContext(){
		return (ErrorDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ErrorDeclarationContext getContext(String str){
		return (ErrorDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).errorDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ErrorDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ErrorDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//GL - ControlBlock
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		return;
	}
	//GL - ControlBlock END
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (isSemanticChecksPass()){
			this.enclosingScope = enclosingScopeRef.get();
			this.enclosingScope.add(this);
//			Scope localScope = createScope();
			//super.defineSymbol(new AtomicReference<Scope>(localScope));
			super.defineSymbol(new AtomicReference<Scope>(enclosingScope));
		}
	}
	
	@Override
	public String getSymbolName()
	{
		return getContext().ERROR().getText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getContext().ERROR().getText();
	}
	
}
