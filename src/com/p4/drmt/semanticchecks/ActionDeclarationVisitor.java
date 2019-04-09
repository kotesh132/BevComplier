package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.ArgumentContext;
import com.p4.p416.gen.P416Parser.FunctionCallContext;
import com.p4.p416.gen.P416Parser.VariableDeclarationContext;
import com.p4.p416.gen.VariableDeclarationContextExt;
import com.p4.p416.iface.IActionRef;
import com.p4.p416.iface.IArgument;
import com.p4.p416.iface.IParameter;

public class ActionDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(ActionDeclarationVisitor.class);
	boolean isPresent = false;
	boolean fromActions = false;

	Map<String, ActionDeclarationContextExt> actionDeclarations;

	@Override
	public ParserRuleContext visitActionDeclaration(P416Parser.ActionDeclarationContext ctx) {
		isPresent = true;
		super.visitActionDeclaration(ctx);
		isPresent = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitSwitchStatement(P416Parser.SwitchStatementContext ctx) {
		if(isPresent) {
			L.error("Line:"+ctx.start.getLine()+"::Switch statement is not allowed.");
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitIfStatement(P416Parser.IfStatementContext ctx) {
		if(isPresent) {
			L.error("Line:"+ctx.start.getLine()+"::Conditional statement is not allowed.");
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitIfElseStatement(P416Parser.IfElseStatementContext ctx) {
		if(isPresent) {
			L.error("Line:"+ctx.start.getLine()+"::Conditional statement is not allowed.");
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx) {
		actionDeclarations = ctx.extendedContext.getActions();
		//List<IActionDeclaration>  actionDeclarations = ctx.extendedContext.getActionDeclarations();
		super.visitControlDeclaration(ctx);
		return ctx;
	}


	@Override
	public ParserRuleContext visitActionList(P416Parser.ActionListContext ctx) {
		fromActions = true;
		super.visitActionList(ctx);
		fromActions = false;
		return ctx;
	}


	@Override
	public ParserRuleContext visitActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx) {
		if(fromActions) {
			ActionDeclarationContextExt actionDeclaration = actionDeclarations.get(ctx.extendedContext.getActionName());	

			if(actionDeclaration!=null) {
				List<IParameter> actionParameters = actionDeclaration.getParameters();
				List<IParameter> directedParameters = new ArrayList<>();
				for(int i=0;i<actionParameters.size(); i++) {
					if(actionParameters.get(i).getDirection()!=null) directedParameters.add(actionParameters.get(i));
				}
				if(directedParameters.size()!=0) {
					L.error("Line:"+ctx.start.getLine()+" "+ctx.extendedContext.getActionName()+": not enough arguments.");
					L.error("Line:"+ctx.start.getLine()+": too few arguments.");
				}

			}
		}
		super.visitActionWithoutArgs(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitActionWithArgs(P416Parser.ActionWithArgsContext ctx) {
		if(fromActions) {
			ActionDeclarationContextExt actionDeclaration = actionDeclarations.get(ctx.extendedContext.getActionName());	
			List<IParameter> actionParameters = actionDeclaration.getParameters();

			if(actionDeclaration.getParameters().size()!=0) {
				List<IArgument> arguments = ctx.extendedContext.getArguments();

				for(int i=0;i<arguments.size(); i++) {
					Symbol argumentSym = ctx.extendedContext.resolve(arguments.get(i).getExpression().getNameString());
					Symbol paramSym = actionDeclaration.resolve(actionParameters.get(i).getNameString());
					if(actionParameters.get(i).getDirection() == null) {
						L.error("Line:"+ctx.start.getLine()+" illegal, cannot bind directionless data parameter.");
						if(argumentSym.getSymbolName() !=null) {
							L.error("Line:"+ctx.start.getLine()+": error: "+arguments.get(i).getExpression().getNameString()+": parameter d cannot be bound: it is set by the control plane.");
						}
					}else {
						if(argumentSym.getSymbolName() ==null){
							L.error("Line:"+ctx.start.getLine()+" illegal, "+actionParameters.get(i).getNameString()+" parameter must be bound.");
						}
					}
				}
			}
		}
		super.visitActionWithArgs(ctx);
		return ctx;
	}


	@Override
	public ParserRuleContext visitLocalConstVariable(P416Parser.LocalConstVariableContext ctx){

		if ("default_action".equals(ctx.IDENTIFIER().getText())){
			String initializer = ctx.initializer().extendedContext.getFormattedText();
			if (ctx.initializer().extendedContext.getFormattedText().endsWith("()")){
				initializer = initializer.substring(0,initializer.length()-2);
			}

			if (ctx.initializer().expression() instanceof FunctionCallContext){
				initializer = ((FunctionCallContext)ctx.initializer().expression()).expression().extendedContext.getFormattedText();
				ActionDeclarationContextExt actionDeclaration = actionDeclarations.get(initializer);
				if(actionDeclaration != null) {
					List<IParameter> actionParameters = actionDeclaration.getParameters();
					//Line 9:// error: y: argument does not match declaration in actions list: x
					if(actionDeclaration.getParameters().size()!=0) {
						if( ((FunctionCallContext)ctx.initializer().expression()).argumentList()!=null) {
							List<ArgumentContext> argumentList = ((FunctionCallContext)ctx.initializer().expression()).argumentList().argument();
							if(actionDeclaration.getParameters().size() != argumentList.size()) {
								L.error("Line:"+ctx.start.getLine()+": "+initializer+": not enough arguments.");
							}

							if(argumentList.size()==0) {
								for(int i=0;i<actionParameters.size(); i++) 
									L.error("Line:"+ctx.start.getLine()+" error: "+initializer+": "+actionParameters.get(i).getNameString()+" parameter must be bound.");
							}
							int i=0;
							for(i=0;i<argumentList.size(); i++) {
								Symbol argumentSym = ctx.extendedContext.resolve(argumentList.get(i).extendedContext.getFormattedText());
								Symbol paramSym = actionDeclaration.resolve(actionParameters.get(i).getNameString());
								if(argumentSym.getSizeInBits() == paramSym.getSizeInBits()) {
									L.error("Line:"+ctx.start.getLine()+" error: "+initializer+": "+actionParameters.get(i).getNameString()+" parameter must be bound.");
								}
								if(argumentSym.getNewValue()==null) {
									L.error("Line:"+ctx.start.getLine()+" error: "+initializer+": "+actionParameters.get(i).getNameString()+" different bound argument.");
								}
								if(argumentSym instanceof VariableDeclarationContextExt) {
									L.error("Line:"+ctx.start.getLine()+" error: "+argumentSym.getSymbolName()+": argument does not match declaration in actions list: "+initializer);
								}
							}
							if(i<actionDeclaration.getParameters().size()) {
								L.error("Line:"+ctx.start.getLine()+" error: "+initializer+": "+actionParameters.get(i).getNameString()+" parameter must be bound.");
							}
							
						}else {
							L.error("Line:"+ctx.start.getLine()+": too few arguments.");
							L.error("Line:"+ctx.start.getLine()+" "+initializer+": not enough arguments.");
							for(int i=0;i<actionParameters.size(); i++) {
								L.error("Line:"+ctx.start.getLine()+" error: "+initializer+": "+actionParameters.get(i).getNameString()+" parameter must be bound.");

							}
						}
					}
				}
			}
		}
		super.visitLocalConstVariable(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx) {
		isPresent = true;
		List<IActionRef> actionRefs = ctx.extendedContext.getActionsRefs();
		super.visitTableDeclaration(ctx);
		isPresent = false;
		return ctx;
	}



}
