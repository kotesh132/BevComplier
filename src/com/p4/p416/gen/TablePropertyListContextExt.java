package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.TablePropertyListContext;

public class TablePropertyListContextExt extends AbstractBaseExt {

	public TablePropertyListContextExt(TablePropertyListContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TablePropertyListContext getContext(){
		return (TablePropertyListContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TablePropertyListContext getContext(String str){
		return (TablePropertyListContext)new PopulateExtendedContextVisitor().visit(getParser(str).tablePropertyList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TablePropertyListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TablePropertyListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
}
