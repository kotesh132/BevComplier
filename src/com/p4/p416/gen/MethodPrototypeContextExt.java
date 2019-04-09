package com.p4.p416.gen;

import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class MethodPrototypeContextExt extends AbstractBaseExt {

	public MethodPrototypeContextExt(MethodPrototypeContext ctx) {
		super(ctx);
	}

	@Override
	public  MethodPrototypeContext getContext(){
		return (MethodPrototypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public MethodPrototypeContext getContext(String str){
		return (MethodPrototypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodPrototype());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MethodPrototypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MethodPrototypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
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
		MethodPrototypeContext ctx = getContext();
		if (ctx.functionPrototype()!=null){
			NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.functionPrototype().name());
			return nameContextExt.getFormattedText();
		}
		return this.getContext().IDENTIFIER().getText();
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
