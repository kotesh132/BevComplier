package com.p4.p416.gen;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.AnnotationContext;
import com.p4.p416.gen.P416Parser.ComplexAnnotationContext;
import com.p4.p416.gen.P416Parser.ExpressionListContext;

public class ComplexAnnotationContextExt extends AnnotationContextExt {

	public ComplexAnnotationContextExt(ComplexAnnotationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  AnnotationContext getContext(){
		return (AnnotationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnnotationContext getContext(String str){
		return (AnnotationContext)new PopulateExtendedContextVisitor().visit(getParser(str).annotation());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ComplexAnnotationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ComplexAnnotationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	//| AT name LPARAN expressionList? RPARAN	#complexAnnotation;
	@Override
	public String getInlineAnnotationString(Integer instanceNumber) {
		ComplexAnnotationContext ctx = (ComplexAnnotationContext) getContext();
		StringBuilder sb = new StringBuilder();
		sb.append("    @");
		sb.append(getExtendedContext(ctx.name()).getFormattedText()+" ");
		sb.append("(");
		for(int i=0; i< ((ExpressionListContext)getExtendedContext(ctx.expressionList()).getContext()).expression().size(); i++){
			if(getExtendedContext(((ExpressionListContext)getExtendedContext(ctx.expressionList()).getContext()).expression(i)) instanceof StringContextExt){
				String originalName = getExtendedContext(((ExpressionListContext)getExtendedContext(ctx.expressionList()).getContext()).expression(i)).getFormattedText();
				String changedAnnotationName = originalName.substring(1).substring(0,originalName.length()-2);
				sb.append("\""+changedAnnotationName+"_"+instanceNumber+"\"");
			}
		}
		sb.append(")");
		return sb.toString();
	}

}
