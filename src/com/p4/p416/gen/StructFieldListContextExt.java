package com.p4.p416.gen;

import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.IStructFieldList;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.StructFieldContext;
import com.p4.p416.gen.P416Parser.StructFieldListContext;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructFieldListContextExt extends AbstractBaseExt implements  IStructFieldList{

	public StructFieldListContextExt(StructFieldListContext ctx) {
		super(ctx);
	}

	@Override
	public  StructFieldListContext getContext(){
		return (StructFieldListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StructFieldListContext getContext(String str){
		return (StructFieldListContext)new PopulateExtendedContextVisitor().visit(getParser(str).structFieldList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StructFieldListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StructFieldListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}


	//GL - ControlBlock START
	/*
	@Override
    public void defineSymbol(){
		StructFieldListContext ctx = (StructFieldListContext) getContext();
		List<StructFieldContext> structFieldList = ctx.structField();
		int i = 0;
		for( StructFieldContext structFieldContext : structFieldList)
		{
			StructFieldContext latestStructFieldContext = (StructFieldContext)structFieldContext.extendedContext.getContext();
			latestStructFieldContext.extendedContext.setField(i++);
		}


    	this.fieldIndex = ctx.
    	super.defineSymbol();
    }
	 */
	//GL - ControlBlock END

	@Override
	public List<IStructField> getStructFields() {
		List<IStructField> structFields = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IStructField) {
				structFields.add((IStructField) node);
			}
		});

		return structFields;
	}

	public void inlineStructFields(){
		StructFieldListContextExt structFieldListContextContextExt = (StructFieldListContextExt) getExtendedContext(getContext());
		StringBuilder structFieldListText = new StringBuilder();
		System.out.println(structFieldListContextContextExt.getFormattedText());
		for(IStructField structField : getStructFields()){
			Symbol symbol = structFieldListContextContextExt.resolve(structField.getNameString());
			Integer listCount = 0;
			if(structField.getTypeRef().isHeaderStackType()){
				listCount = symbol.getType().getTypeSize();
				List<StructFieldContext> inlinedStructFieldContext = new ArrayList<StructFieldContext>();
				for(int i =0 ; i<listCount ; i++){
					((StructFieldContextExt)structField).inlineStructFields(inlinedStructFieldContext, i);
				}
				for(StructFieldContext newStructFieldContext : inlinedStructFieldContext){
					structFieldListText.append(getExtendedContext(newStructFieldContext).getFormattedText());
				}
				
			}else{
				structFieldListText.append(structField.getFormattedText()+"\n");
			}
		}
		StructFieldListContext copiedCtx = getContext(structFieldListText.toString());
		addToContexts(copiedCtx);
	}
}
