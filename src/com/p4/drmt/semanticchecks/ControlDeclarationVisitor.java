package com.p4.drmt.semanticchecks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.IntegerContext;
import com.p4.p416.gen.P416Parser.LvalueContext;
import com.p4.p416.gen.P416Parser.PrefixedNonTypeNameLvalueContext;
import com.p4.p416.iface.IParameter;

public class ControlDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(ControlDeclarationVisitor.class);
	boolean fromControlDecl = false;
	String controlTypeDeclName = null;

	Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();

	@Override 
	public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx) {
		(ControlDeclarationContextExt.getExtendedContext(ctx)).getActions(actions);
		fromControlDecl = true;
		controlTypeDeclName = ctx.controlTypeDeclaration().name().getText();
		super.visitControlDeclaration(ctx);
		fromControlDecl = false;
		return ctx;
	}



	@Override
	public ParserRuleContext visitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx) {
		super.visitApplyMethodCall(ctx);
		if(fromControlDecl) {
			if(ctx.lvalue() != null) {
				LvalueContext lvalueContext = ctx.lvalue();
				if(lvalueContext instanceof PrefixedNonTypeNameLvalueContext) {
					String name = lvalueContext.getText();
					if(name != null && name.equals(controlTypeDeclName))
						L.error("Line:"+ctx.start.getLine()+": "+"Cannot refer to control inside itself");
				}
			}
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitHeaderStackType(P416Parser.HeaderStackTypeContext ctx) {
		super.visitHeaderStackType(ctx);
		if(ctx.expression() != null) {
			if(! (ctx.expression() instanceof IntegerContext)) {
				//TODO We need to double check whether the header stack declaration must be an integer or can it be an expression which is a compile time constant i.e., evaluatable 
				L.error("Line:"+ctx.start.getLine()+": "+"Size of header stack type should be a constant.");
			}
		}
		return ctx;
	}


	//	 | lvalue LPARAN argumentList? RPARAN SEMI								#callWithoutTypeArgs
	//	| lvalue LT typeArgumentList GT LPARAN argumentList? RPARAN SEMI		#callWithTypeArgs;

	@Override
	public ParserRuleContext visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx) {
		ActionDeclarationContextExt iaction = actions.get(ctx.lvalue().getText());
		if(iaction != null) {
			List<IParameter> paramList = actions.get(ctx.lvalue().getText()).getParameters();

			if(ctx.argumentList() != null) {
				if(paramList.size() != ctx.argumentList().argument().size() ) {
					L.error("Line:"+ctx.start.getLine()+": "+"Parameter port must be bound.");
				}
			}
			else {
				if(paramList.size() > 0) {
					L.error("Line:"+ctx.start.getLine()+": "+"Parameter port must be bound.");
				}
			}
		}
		return ctx;
	}

}
