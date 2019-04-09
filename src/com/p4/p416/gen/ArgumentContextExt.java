package com.p4.p416.gen;

import java.util.List;

import com.p4.p416.iface.IArgument;
import com.p4.p416.iface.IExpression;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.p416.gen.AbstractBaseExt.Params;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.AbstractBaseExt.Params;
import com.p4.p416.gen.P416Parser.ArgumentContext;
import com.p4.p416.gen.P416Parser.FunctionCallContext;

public class ArgumentContextExt extends AbstractBaseExt implements IArgument {

	public ArgumentContextExt(ArgumentContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  ArgumentContext getContext(){
		return (ArgumentContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArgumentContext getContext(String str){
		return (ArgumentContext)new PopulateExtendedContextVisitor().visit(getParser(str).argument());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ArgumentContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ArgumentContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void invokeProgramFlow(List<String> flowNodes){
		ArgumentContext argumentContext = getContext();
		if(getExtendedContext(argumentContext.expression()).getContext() instanceof FunctionCallContext){
			FunctionCallContext functionCallContext = (FunctionCallContext) getExtendedContext(argumentContext.expression()).getContext();
			flowNodes.add(getExtendedContext(functionCallContext.expression()).getFormattedText());
		}else{
		}
	}
	//SSA START
	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText();
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	@Override
	public IExpression getExpression() {
		return (IExpression) getExtendedContext(getContext().expression());
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return  getExtendedContext(getContext().expression()).getType().getTypeName();
	}
	
	@Override
	public boolean isEquivalenceCompatible(Type that){
		return getExtendedContext(getContext().expression()).getType().isEquivalenceCompatible(that);
	}
	
	@Override
	public int getTypeSize(){
		return getExtendedContext(getContext().expression()).getType().getTypeSize();
	}

}
