package com.p4.p416.gen;

import com.p4.p416.gen.P416Parser.InstantiationContext;
import com.p4.p416.iface.IDeclaration;
import com.p4.p416.iface.IInstantiation;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.DeclarationContext;

public class DeclarationContextExt extends AbstractBaseExt implements IDeclaration {

	public DeclarationContextExt(DeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  DeclarationContext getContext(){
		return (DeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public DeclarationContext getContext(String str){
		return (DeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).declaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof DeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ DeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public IInstantiation getInstantiation() {
		InstantiationContext instantiation = getContext().instantiation();
		return instantiation != null ? (IInstantiation) getExtendedContext(instantiation) : null;
	}
}
