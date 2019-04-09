package com.p4.p416.gen;

import com.p4.p416.iface.ICallWithTypeArgs;



import java.util.LinkedHashMap;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.SsaForm;
import com.p4.p416.gen.P416Parser.CallWithTypeArgsContext;
import com.p4.p416.gen.P416Parser.MethodCallStatementContext;
import com.p4.p416.gen.P416Parser.StatementContext;

public class CallWithTypeArgsContextExt extends MethodCallStatementContextExt implements ICallWithTypeArgs {

	public CallWithTypeArgsContextExt(CallWithTypeArgsContext ctx) {
		super(ctx);
	}

	@Override
	public  MethodCallStatementContext getContext(){
		return (MethodCallStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public CallWithTypeArgsContext getContext(String str){
		return (CallWithTypeArgsContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodCallStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof CallWithTypeArgsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ CallWithTypeArgsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//SSA START
	@Override
	public void setVersions(SsaForm obj)	{}
	//SSA END

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		String actionCall = getFormattedText();
		StringBuilder sb = new StringBuilder();
		boolean doTransform = false;
		AbstractBaseExt parent = getParent();
		if (actionCall.contains("digest")) {
			L.debug(actionCall + " : digest <T> yet to be implemented");
			//TODO temporary hack to go ahead
			sb.append("mark_to_drop();");
			doTransform = true;
		}

		if (doTransform) {
			StatementContext statementContext = ((StatementContextExt) parent).getContext(sb.toString());
			((StatementContextExt) parent).setContext(statementContext);
		}

	}
}
