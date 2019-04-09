package com.p4.drmt.semanticchecks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.HeaderTypeDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.VarBitSizeTypeContextExt;
import com.p4.p416.gen.P416Parser.ExtractMethodCallContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.VarBitSizeTypeContextExt;
import com.p4.p416.gen.P416Parser.ExtractMethodCallContext;
import com.p4.p416.gen.P416Parser.FunctionCallContext;
import com.p4.p416.gen.P416Parser.HeaderTypeDeclarationContext;
import com.p4.p416.gen.P416Parser.StructFieldContext;

public class ParserDeclarationVisitor  extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(ParserDeclarationVisitor.class);
	boolean insideParserDeclaration = false;
	Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
	ActionDeclarationContextExt actionCtx = null;
	boolean hasStart = false; 
	Map<String, HeaderTypeDeclarationContextExt> headerTypeDeclarations;

	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		P4programContextExt.getExtendedContext(ctx).getActions(actions);
		super.visitP4program(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitParserDeclaration(P416Parser.ParserDeclarationContext ctx) {
		insideParserDeclaration = true;
		super.visitParserDeclaration(ctx);
		insideParserDeclaration = false;
		if(!hasStart) {
			L.error("Line:"+ctx.start.getLine()+": "+"No start state.");
		}
		return ctx;
	}


	@Override
	public ParserRuleContext visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx) {
		if (insideParserDeclaration){
			String methodName = ctx.lvalue().getText();
			actionCtx = actions.get(methodName);
			if(actionCtx != null) {
				L.error("Line:"+ctx.start.getLine()+": "+"Action calls are not allowed within parsers.");
			}
		}
		return ctx;
	}



	@Override
	public ParserRuleContext visitParserState(P416Parser.ParserStateContext ctx) {
		super.visitParserState(ctx);
		if(insideParserDeclaration) {
			if(ctx.name().getText().equals("start")) {
				hasStart = true;
			}
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx){
		super.visitExtractMethodCall(ctx);
		if (ctx instanceof ExtractMethodCallContext){
			try {
				Symbol extractArg = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx.argumentList().argument(0)).getFormattedText());
				if(!extractArg.getType().getTypeName().equals("header")) {
					L.error("Line:"+ctx.start.getLine()+": "+"error: "+AbstractBaseExt.getExtendedContext(ctx.argumentList().argument(0)).getFormattedText()+": not a header");
				}else {
					if(ctx.argumentList().argument().size()>1)
					if (extractArg.getType().getTypeSize() == VarBitSizeTypeContextExt.SIZE_INDETERMINATE){
						L.error("Line:"+ctx.start.getLine()+": error: "+AbstractBaseExt.getExtendedContext(ctx.argumentList().argument(0)).getFormattedText()+": not a variable-sized header");
					}
				}
			}catch (SymbolNotDefinedException e) {
			}
		}
		return ctx;
	}

}
