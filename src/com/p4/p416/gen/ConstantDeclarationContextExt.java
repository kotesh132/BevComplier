package com.p4.p416.gen;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ConstantDeclarationContext;
import com.p4.p416.gen.P416Parser.VariableDeclarationContext;

public class ConstantDeclarationContextExt extends AbstractBaseExt {

	public ConstantDeclarationContextExt(ConstantDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  ConstantDeclarationContext getContext(){
		return (ConstantDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ConstantDeclarationContext getContext(String str){
		return (ConstantDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).constantDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ConstantDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ConstantDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
	@Override
	public boolean isTypeCompatible(Type t1, Type t2){
		return t1.isEquivalenceCompatible(t2);
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (this.isSemanticChecksPass()){
		this.enclosingScope = enclosingScopeRef.get();
		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
		{
        	L.error("Line:"+getContext().start.getLine()+": "+" Duplicate declaration of: "+getExtendedContext(getContext().name()).getFormattedText());
		}
		this.enclosingScope.add(this);
		this.isConstant = true;
		super.defineSymbol(enclosingScopeRef);
		}
	}
	
	@Override
	public String getSymbolName()
	{
		ConstantDeclarationContext ctx = getContext();
		NameContextExt nameContextExt  = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}
	
	@Override
	public Type getType(){
		Type type =  getType1();
		type.setIsConstant(true);
		return type;
	}
	
	@Override
	public String getTypeName()
	{
		ConstantDeclarationContext ctx = getContext();
		TypeRefContextExt typeRefContextExt  = (TypeRefContextExt)getExtendedContext(ctx.typeRef());
		return typeRefContextExt.getFormattedText();
	}
	
}
