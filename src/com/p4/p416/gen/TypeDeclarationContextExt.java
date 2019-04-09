package com.p4.p416.gen;

import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.TypeDeclarationContext;

public class TypeDeclarationContextExt extends AbstractBaseExt {

	public TypeDeclarationContextExt(TypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeDeclarationContext getContext(){
		return (TypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeDeclarationContext getContext(String str){
		return (TypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeDeclarationContext.class.getName());
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
	
//	@Override
//	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
//	{
//		this.enclosingScope = enclosingScopeRef.get();
//
//		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
//		{
//			throw new NameCollisionException((String)this.getSymbolKey());
//		}
//		this.enclosingScope.add(this);
//		Scope newScope = createScope();
//		super.defineSymbol(new AtomicReference<Scope>(newScope));
//
//	}

	
//	@Override
//	public String getSymbolName()
//	{
//		TypeDeclarationContext ctx = getContext();
//		if (ctx.controlTypeDeclaration()!=null){
//			return ctx.controlTypeDeclaration().extendedContext.getSymbolName();
//		}
//		else if (ctx.parserTypeDeclaration()!=null){
//			return ctx.parserTypeDeclaration().extendedContext.getSymbolName();
//		}
//		else if (ctx.packageTypeDeclaration()!=null){
//			return ctx.packageTypeDeclaration().extendedContext.getSymbolName();
//		}
//		else if (ctx.derivedTypeDeclaration()!=null){
//			DerivedTypeDeclarationContext ctxIn = ctx.derivedTypeDeclaration();
//			if (ctxIn.enumDeclaration()!=null){
//				return ctxIn.enumDeclaration().name().getText();	
//			}
//			if (ctxIn.headerTypeDeclaration()!=null){
//				return ctxIn.headerTypeDeclaration().name().getText();	
//			}
//			if (ctxIn.headerUnionDeclaration()!=null){
//				return ctxIn.headerUnionDeclaration().name().getText();	
//			}
//			if (ctxIn.structTypeDeclaration()!=null){
//				return ctxIn.structTypeDeclaration().name().getText();	
//			}
//			
//		}
//		L.error("Something wrong in Type Declaration with context and value: "+this.getContext().getText()+":"+this.getClass()+":"+this.getParent().getClass());
//		return "";
//	}
}
