package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.iface.IFunctionPrototype;
import com.p4.p416.iface.IParameter;
import com.p4.p416.applications.Type;

public class FunctionPrototypeContextExt extends AbstractBaseExt implements IFunctionPrototype{

	public FunctionPrototypeContextExt(FunctionPrototypeContext ctx) {
		super(ctx);
	}

	@Override
	public  FunctionPrototypeContext getContext(){
		return (FunctionPrototypeContext)contexts.get(contexts.size()-1);
	}

	@Override
	public FunctionPrototypeContext getContext(String str){
		return (FunctionPrototypeContext)new PopulateExtendedContextVisitor().visit(getParser(str).functionPrototype());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof FunctionPrototypeContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ FunctionPrototypeContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getExtendedContext(getContext().typeOrVoid()).getTypeName();
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().typeOrVoid()).getTypeSize();
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getFormattedText();
	}
	
	@Override
	public void getFunctionPrototypes(Map<String, FunctionPrototypeContextExt> functionPrototypes) {
		functionPrototypes.put(getNameString(), this);
	}

	@Override
	public List<IParameter> getParameters() {
		List<IParameter> parameters = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IParameter) {
				parameters.add((IParameter) node);
			}
		});
		return parameters;
	}

}
