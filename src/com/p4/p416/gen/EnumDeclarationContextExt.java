package com.p4.p416.gen;


import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class EnumDeclarationContextExt extends AbstractBaseExt {

	public EnumDeclarationContextExt(EnumDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  EnumDeclarationContext getContext(){
		return (EnumDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public EnumDeclarationContext getContext(String str){
		return (EnumDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).enumDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof EnumDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ EnumDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (isSemanticChecksPass()){
			this.enclosingScope = enclosingScopeRef.get();
			this.enclosingScope.add(this);
			Scope localScope = createScope();
			//super.defineSymbol(new AtomicReference<Scope>(localScope));
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
	}
	
	@Override
	public String getSymbolName()
	{
		return getContext().name().getText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getContext().ENUM().getText();
	}
}
