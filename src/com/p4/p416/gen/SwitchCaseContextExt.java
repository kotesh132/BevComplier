package com.p4.p416.gen;

import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.p416.gen.P416Parser.StatOrDeclListContext;
import com.p4.p416.iface.ISwitchCaseContext;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.SwitchCaseContext;



import java.util.LinkedHashMap;

public class SwitchCaseContextExt extends AbstractBaseExt implements ISwitchCaseContext {

	public SwitchCaseContextExt(SwitchCaseContext ctx) {
		super(ctx);
	}

	@Override
	public  SwitchCaseContext getContext(){
		return (SwitchCaseContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SwitchCaseContext getContext(String str){
		return (SwitchCaseContext)new PopulateExtendedContextVisitor().visit(getParser(str).switchCase());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SwitchCaseContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SwitchCaseContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		BlockStatementContext blockStatementContext = getContext().blockStatement();
		if (blockStatementContext != null) {
			BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(blockStatementContext);
			StatOrDeclListContext statOrDeclListContext = blockStatementContextExt.getContext().statOrDeclList();
			AbstractBaseExt statOrDecListExt = statOrDeclListContext != null ? getExtendedContext(statOrDeclListContext) : null;
			if (statOrDecListExt != null) {
				String statOrDecList = statOrDecListExt.getFormattedText();
				StringBuilder sb = new StringBuilder();
				sb.append(blockStatementContext.LCURL());
				sb.append("\t");
				sb.append(statOrDecList);
				sb.append("\n");
				sb.append("\t\t\t\tBREAK_SWITCH();\n");
				sb.append("\t\t\t").append(blockStatementContext.RCURL());

				BlockStatementContext newBlockStatement = blockStatementContextExt.getContext(sb.toString());
				blockStatementContextExt.setContext(newBlockStatement);
			}
		}
	}
}
