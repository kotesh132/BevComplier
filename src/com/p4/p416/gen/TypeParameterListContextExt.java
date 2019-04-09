package com.p4.p416.gen;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;
import com.p4.p416.gen.P416Parser.TypeParameterListContext;

public class TypeParameterListContextExt extends AbstractBaseExt {

	public TypeParameterListContextExt(TypeParameterListContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TypeParameterListContext getContext(){
		return (TypeParameterListContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TypeParameterListContext getContext(String str){
		return (TypeParameterListContext)new PopulateExtendedContextVisitor().visit(getParser(str).typeParameterList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TypeParameterListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TypeParameterListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
//	@Override
//	public String getSymbolName()
//	{
//		TypeParameterListContext ctx = getContext();
//		List<NonTypeNameContext> list = ctx.nonTypeName();
//		String stringifiedTypeParams="";
//		for (NonTypeNameContext name:list){
//		NonTypeNameContextExt nonTypeNameContextExt  = (NonTypeNameContextExt)getExtendedContext(name);
//		stringifiedTypeParams = stringifiedTypeParams+nonTypeNameContextExt.getFormattedText()+",";
//		}
//		return stringifiedTypeParams;
//	}
//	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (this.isSemanticChecksPass()){
    	this.enclosingScope = enclosingScopeRef.get();
    	List<NonTypeNameContext> list =  this.getContext().nonTypeName();
    	for (NonTypeNameContext nonTypeName:list){
        if ( this.enclosingScope.lookup(getExtendedContext(nonTypeName).getSymbolKey()) != null){
        		throw new NameCollisionException((String)getExtendedContext(nonTypeName).getSymbolKey());
        	}
    	this.enclosingScope.add(getExtendedContext(getExtendedContext(nonTypeName).getContext()));
    	//super.defineSymbol(enclosingScopeRef);
    		}
		}
	}
}
