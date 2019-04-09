package com.p4.p416.gen;

import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.DerivedTypeDefContext;
import com.p4.p416.gen.P416Parser.SimpleTypeDefContext;
import com.p4.p416.gen.P416Parser.TypedefDeclarationContext;

public class TypedefDeclarationContextExt extends AbstractBaseExt {

	public TypedefDeclarationContextExt(TypedefDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypedefDeclarationContext getContext(){
		return (TypedefDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypedefDeclarationContext getContext(String str){
		return (TypedefDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).typedefDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypedefDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypedefDeclarationContext.class.getName());
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
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
		return;
	}
	
	@Override
	public String getSymbolName()
	{
		String typeDefName;
		if (this.getContext() instanceof SimpleTypeDefContext){
			typeDefName = ((SimpleTypeDefContext)(this.getContext())).name().getText();
		}else{
			typeDefName = ((DerivedTypeDefContext)(this.getContext())).name().getText();
		}
		return typeDefName;
	}
	
}
