package com.p4.p416.gen;

import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.ISwitchLabel;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SwitchLabelContext;

import java.util.LinkedHashMap;

public class SwitchLabelContextExt extends AbstractBaseExt implements ISwitchLabel {

	public SwitchLabelContextExt(SwitchLabelContext ctx) {
		super(ctx);
	}

	@Override
	public  SwitchLabelContext getContext(){
		return (SwitchLabelContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SwitchLabelContext getContext(String str){
		return (SwitchLabelContext)new PopulateExtendedContextVisitor().visit(getParser(str).switchLabel());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SwitchLabelContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SwitchLabelContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		IIRNode controlNode = getParentIRNode();
		while (controlNode != null && !(controlNode instanceof IControl)) {
			controlNode = controlNode.getParentIRNode();
		}

		if (controlNode != null) {
			if (getContext().DEFAULT() == null) {
				String switchLabel = getExtendedContext(getContext().name()).getFormattedText();
				//TODO should we resolve switchLable to action always before transforming?
				switchLabel = "CASE_" + ((IControl) controlNode).getNameString() + "_" + switchLabel + "_enum";
				SwitchLabelContext context = getContext(switchLabel);
				setContext(context);
			}
		}
	}
}
