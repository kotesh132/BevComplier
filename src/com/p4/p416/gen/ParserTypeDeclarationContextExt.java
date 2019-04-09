package com.p4.p416.gen;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.ST;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ParserTypeDeclarationContext;

public class ParserTypeDeclarationContextExt extends AbstractBaseExt {

	public ParserTypeDeclarationContextExt(ParserTypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  ParserTypeDeclarationContext getContext(){
		return (ParserTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserTypeDeclarationContext getContext(String str){
		return (ParserTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserTypeDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}


    protected void getSt(List<ST> sts){
    	/*
        ParserTypeDeclarationContext ctx = (ParserTypeDeclarationContext) getContext();
        if(ctx.parameterList() != null && !ctx.parameterList().getText().equals("")){
            ctx.parameterList().nonEmptyParameterList().extendedContext.getSt(sts);
        }*/
    }
	
		@Override
		public String getSymbolName()
		{
			ParserTypeDeclarationContextExt parserTypeDeclarationContextExt = (ParserTypeDeclarationContextExt) getExtendedContext(getContext());
			NameContextExt nameContextExt = (NameContextExt)parserTypeDeclarationContextExt.getExtendedContext(getContext().name());
			return nameContextExt.getFormattedText();
		}
		
		@Override
		public String getTypeName()
		{
			return getContext().PARSER().getText();
		}
		
		@Override
		public Boolean isSame(Type thatType)
		{
			return thatType.getTypeName().equals(this.getTypeName()) && thatType.getSymbolName().equals(this.getTypeName());
		}
		
	//GL - ControlBlock END
		
		@Override
		public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
		{
			if (isSemanticChecksPass()){
				if (this.getParent() instanceof ParserDeclarationContextExt){
					super.defineSymbol(enclosingScopeRef);
				}
				else{
					this.enclosingScope = enclosingScopeRef.get();
					this.enclosingScope.add(this);
					Scope localScope = createScope();
					super.defineSymbol(new AtomicReference<Scope>(localScope));
				}
			}
			return;
		}
	
}
