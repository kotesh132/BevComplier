package com.p4.p416.gen;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class ExternFunctionDeclarationContextExt extends ExternDeclarationContextExt {

	public ExternFunctionDeclarationContextExt(ExternFunctionDeclarationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ExternFunctionDeclarationContext getContext(){
		return (ExternFunctionDeclarationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExternFunctionDeclarationContext getContext(String str){
		return (ExternFunctionDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).externDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExternFunctionDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExternFunctionDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		return;
	}


	@Override
	public void defineSymbol(AtomicReference<Scope> enclosingScopeRef) throws NameCollisionException
	{
		//do nothing
		//added this to not process extern methods. previous to this, it is throwing error.
		if (isSemanticChecksPass()){
			Scope enclosingScope = enclosingScopeRef.get();
			enclosingScope.add(this);
			
			Scope localScope = createScope();
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
	}

	@Override
	public String getSymbolName()
	{
		if (isSemanticChecksPass()){
			return this.getContext().functionPrototype().name().getText();
		}
		else{
			throw new UnsupportedOperationException("ExternFunctionDeclaration.defineSymbol()");
		}
	}
	
	@Override
	public String getNameString() {
		return null ;
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(getContext().functionPrototype().typeOrVoid()).getType().getTypeName();
	}
	
}
