package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import com.p4.p416.iface.IParameter;
import com.p4.p416.iface.IParameterList;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.ST;

import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parameter;
import com.p4.p416.gen.P416Parser.ParameterContext;
import com.p4.p416.gen.P416Parser.ParameterListContext;

public class ParameterListContextExt extends AbstractBaseExt implements IParameterList {

	public ParameterListContextExt(ParameterListContext ctx) {
		super(ctx);
	}

	@Override
	public  ParameterListContext getContext(){
		return (ParameterListContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParameterListContext getContext(String str){
		return (ParameterListContext)new PopulateExtendedContextVisitor().visit(getParser(str).parameterList());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParameterListContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParameterListContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
    protected void getSt(List<ST> sts){
    	/*
        ParameterListContext ctx = (ParameterListContext) getContext();
        if(ctx.nonEmptyParameterList() != null){
            ctx.nonEmptyParameterList().extendedContext.getSt(sts);
        }*/
    }

	public List<P4Parameter> getParameters(P4Headers headers) {
		List<P4Parameter> ret = new ArrayList<>();
		ParameterListContext ctx = getContext();
		for(ParameterContext pctx: ctx.parameter()){
			ret.add(((ParameterContextExt) getExtendedContext(pctx)).addParameter(headers));
		}
		return ret;
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
