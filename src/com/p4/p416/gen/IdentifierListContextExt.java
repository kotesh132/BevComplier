package com.p4.p416.gen;


import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.exceptions.NameCollisionException;

public class IdentifierListContextExt extends AbstractBaseExt {

	public IdentifierListContextExt(IdentifierListContext ctx) {
		super(ctx);
	}

	@Override
	public  IdentifierListContext getContext(){
		return (IdentifierListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public IdentifierListContext getContext(String str){
		return (IdentifierListContext)new PopulateExtendedContextVisitor().visit(getParser(str).identifierList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof IdentifierListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ IdentifierListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (this.isSemanticChecksPass()){
    	this.enclosingScope = enclosingScopeRef.get();
    	List<NameContext> list =  this.getContext().name();
    	for (NameContext name:list){
    		if ( this.enclosingScope.lookup(getExtendedContext(name).getSymbolKey()) != null){
        		throw new NameCollisionException((String)getExtendedContext(name).getSymbolKey());
        	}
    		this.enclosingScope.add(getExtendedContext(getExtendedContext(name).getContext()));
    		}
		}
	}
}
