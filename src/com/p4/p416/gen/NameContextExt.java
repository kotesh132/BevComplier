package com.p4.p416.gen;

import com.p4.p416.iface.IName;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.EnumDeclarationContext;
import com.p4.p416.gen.P416Parser.NameContext;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;

public class NameContextExt extends AbstractBaseExt implements IName {

	private static final Logger L = LoggerFactory.getLogger(NameContextExt.class);

	public NameContextExt(NameContext ctx) {
		super(ctx);
	}

	@Override
	public  NameContext getContext(){
		return (NameContext)contexts.get(contexts.size()-1);
	}

	@Override
	public NameContext getContext(String str){
		return (NameContext)new PopulateExtendedContextVisitor().visit(getParser(str).name());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NameContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NameContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public String getSymbolName()
	{
		NameContext ctx = getContext();
		NameContextExt nameContextExt  = (NameContextExt)getExtendedContext(ctx);
		return nameContextExt.getFormattedText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		if (this.getParent().getParent() instanceof EnumDeclarationContextExt)
			return this.getParent().getParent().getTypeName();
		else
			return super.getTypeName();
	}
}
