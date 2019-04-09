package com.p4.p416.gen;

import com.p4.p416.iface.IHeaderStackType;
import com.p4.p416.util.ExpressionEvalVisitor;

import  org.antlr.v4.runtime.ParserRuleContext;

import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Type;

public class HeaderStackTypeContextExt extends AbstractBaseExt implements IHeaderStackType {
	
	public static final String LAST_HEADER = "last";
	public static final String HEADER_SIZE = "size";
	public static final String NEXT_HEADER = "next";
	public static final String LAST_INDEX = "lastIndex";
	public static final String PUSH_FRONT = "push_front";
	public static final String POP_FRONT = "pop_front";

	public HeaderStackTypeContextExt(HeaderStackTypeContext ctx) {
		super(ctx);
	}

	@Override
	public  HeaderStackTypeContext getContext(){
		return (HeaderStackTypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public HeaderStackTypeContext getContext(String str){
		return (HeaderStackTypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).headerStackType());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof HeaderStackTypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ HeaderStackTypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public int getHeaderStackSize() {
		//TODO should expression be evaluated upfront?
		String expr = getExtendedContext(getContext().expression()).getFormattedText();
		return Integer.parseInt(expr);
	}
	
	@Override
	public int getSizeInBytes() {
		ExpressionEvalVisitor evalVisitor = new ExpressionEvalVisitor();
		evalVisitor.visit(getContext().expression());
		String value = ((ExpressionContextExt)getExtendedContext(getContext().expression())).value;
		if (this.isSemanticChecksPass()){
			if (value!=null){
				return getExtendedContext(getContext().typeName()).getSizeInBytes() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
			}else{
				//TODO Ideally we should not return -1 but should give meaningful error if it is not evaluatable. In this case, it is done with the help of visitors ....
				//But at the same time we have to show the error message only once in the semantic error log. Thus, executing this part only when semantic checks pass is run. In other cases, the earlier implementation (which has to be changed) will be run
				//L.error("Line:"+ctx.start.getLine()+": Size of header stack type should be a constant: "+ctx.extendedContext.getFormattedText());
				return -1;
			}
		}
		return getExtendedContext(getContext().typeName()).getSizeInBits() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
	}
	
	@Override
	public int getSizeInBits(){
		ExpressionEvalVisitor evalVisitor = new ExpressionEvalVisitor();
		evalVisitor.visit(getContext().expression());
		String value = ((ExpressionContextExt)getExtendedContext(getContext().expression())).value;
		if (this.isSemanticChecksPass()){
			if (value!=null){
				return getExtendedContext(getContext().typeName()).getSizeInBits() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
			}else{
				//TODO Ideally we should not return -1 but should give meaningful error if it is not evaluatable. In this case, it is done with the help of visitors ....
				//But at the same time we have to show the error message only once in the semantic error log. Thus, executing this part only when semantic checks pass is run. In other cases, the earlier implementation (which has to be changed) will be run
				//L.error("Line:"+ctx.start.getLine()+": Size of header stack type should be a constant: "+ctx.extendedContext.getFormattedText());
				return -1;
			}
		}
		return getExtendedContext(getContext().typeName()).getSizeInBits() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
	}
	
	@Override
	public int getAlignedSizeInBytes() {
		ExpressionEvalVisitor evalVisitor = new ExpressionEvalVisitor();
		evalVisitor.visit(getContext().expression());
		String value = ((ExpressionContextExt)getExtendedContext(getContext().expression())).value;
		if (this.isSemanticChecksPass()){
			if (value!=null){
				return getExtendedContext(getContext().typeName()).getSizeInBytes() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
			}else{
				//TODO Ideally we should not return -1 but should give meaningful error if it is not evaluatable. In this case, it is done with the help of visitors ....
				//But at the same time we have to show the error message only once in the semantic error log. Thus, executing this part only when semantic checks pass is run. In other cases, the earlier implementation (which has to be changed) will be run
				//L.error("Line:"+ctx.start.getLine()+": Size of header stack type should be a constant: "+ctx.extendedContext.getFormattedText());
				return -1;
			}
		}
		return getExtendedContext(getContext().typeName()).getSizeInBits() * Integer.parseInt(((ExpressionContextExt)getExtendedContext(getContext().expression())).value);
		
	}
	
	//TODO: Not sure why do we need getType1() API on AbstractBaseExt. Implemented this based on existing design. Guess we can do away with this
	@Override
	public Type getType1(){
		return this;
	}
	
	@Override
	public int getTypeSize(){
		//TODO: expression evaluation to go here.
		return Integer.parseInt(getExtendedContext(getContext().expression()).getFormattedText());
	}
}
