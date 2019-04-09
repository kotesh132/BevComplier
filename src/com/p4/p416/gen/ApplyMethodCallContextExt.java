package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.LinkedHashMap;

import com.p4.cgen.utils.CGenUtils;
import com.p4.drmt.alu.CMatchNode;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IMatchNode;
import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.MethodCallStatementContext;
import com.p4.p416.iface.IApplyMethodCall;
import com.p4.p416.iface.IArgument;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.ILValue;
import com.p4.p416.iface.ITable;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416Parser.ApplyMethodCallContext;

public class ApplyMethodCallContextExt extends MethodCallStatementContextExt implements IApplyMethodCall {

	protected static final Logger L = LoggerFactory.getLogger(ApplyMethodCallContextExt.class);
	
	public ApplyMethodCallContextExt(ApplyMethodCallContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MethodCallStatementContext getContext(){
		return (MethodCallStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MethodCallStatementContext getContext(String str){
		return (MethodCallStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodCallStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ApplyMethodCallContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ MethodCallStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	public String getLvalue()
	{
		if (getContext() instanceof ApplyMethodCallContext) {
			return getExtendedContext(((ApplyMethodCallContext)getContext()).lvalue()).getFormattedText();
		}
		return null;
	}

	@Override
	public void populateTableApplySequence(List<ApplyMethodCallContextExt> tableApplyCalls) {
		tableApplyCalls.add(this);
	}

	@Override
	public void assignPredicate(Stack<ExpressionContextExt> stack){
		StringBuilder sb = new StringBuilder();
		sb.append(getFormattedText());
		if(!stack.isEmpty()){
			this.predicateSymbol = stack.peek().getFormattedText();
			sb.append("//PREDICATE = "+stack.peek().getFormattedText());
		}else{
			this.predicateSymbol = null;
		}
		L.debug(sb.toString());
	}

	@Override
	public void printInstruction(){
		if (getContext() instanceof ApplyMethodCallContext) {
            L.info("MATCH => " + getExtendedContext(((ApplyMethodCallContext)getContext()).lvalue()).getFormattedText());
		}
	}
	// SSA START
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
	public void unroll(Map<String, ControlDeclarationContextExt> controlBlocks,
			List<ControlLocalDeclarationContextExt> controlLocalDeclarations,
			List<ParserRuleContext> applyStatements) {

		if (getContext() instanceof ApplyMethodCallContext) {
			ApplyMethodCallContext ctx = (ApplyMethodCallContext) getContext();
			String name = getExtendedContext(ctx.lvalue()).getFormattedText();
			List<ParserRuleContext> localApplyStatements = new ArrayList<ParserRuleContext>();
			if (controlBlocks.containsKey(name)) {
				AbstractBaseExt controlDeclarationContextExt = controlBlocks.get(name);
				controlDeclarationContextExt.unroll(controlBlocks, controlLocalDeclarations, localApplyStatements);
				ParserRuleContext blockStatementContext = localApplyStatements.get(0);
				AbstractBaseExt blockStatementContextExt = getExtendedContextGetVisitor().visit(blockStatementContext);
				ParserRuleContext statementContext = new PopulateExtendedContextVisitor().visit(getParser(blockStatementContextExt.getFormattedText()).statement());
				getParent().setContext(statementContext);
			}
		}
	}

	@Override
	public ILValue getLValue() {
		if (getContext() instanceof ApplyMethodCallContext) {
			return (ILValue) getExtendedContext(((ApplyMethodCallContext)getContext()).lvalue());
		}
		return null;
	}

	@Override
	public List<IArgument> getArguments() {
		List<IArgument> arguments = new ArrayList<>();
		if (getContext() instanceof ApplyMethodCallContext) {
			ArgumentListContextExt argumentListContextExt = (ArgumentListContextExt) getExtendedContext(((ApplyMethodCallContext) getContext()).argumentList());

			if (argumentListContextExt != null) {
				for (IIRNode node : argumentListContextExt.getChildren()) {
					if (node instanceof IArgument) {
						arguments.add((IArgument) node);
					}
				}
			}
		}

		return arguments;
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {

		IIRNode controlNode = getParentIRNode();
		while (controlNode != null && !(controlNode instanceof IControl)) {
			controlNode = controlNode.getParentIRNode();
		}

		if (controlNode != null && getContext() instanceof ApplyMethodCallContext) {
			IControl control = (IControl) controlNode;
			StringBuilder sb = new StringBuilder();
			String applyMethod = getFormattedText();
			ILValue lValue = getLValue();
			Symbol symbol = controlNode.resolve(lValue.getFormattedText());
			if (symbol instanceof ITable) {
				sb.append("\t").append(control.getNameString()).append("_").append(applyMethod.replaceFirst(".apply", "_apply"));
			} else {
				List<String> args = new ArrayList<>();
				sb.append("\t").append(getLValue().getFormattedText()).append("_apply(");
				List<IArgument> arguments = getArguments();
				for (IArgument argument : arguments) {
					if (argument.getExpression().isFunctionCall()) {
						L.error(argument.getFormattedText() + " is not yet handled");
					}
					args.add("AMPERSAND"+argument.getFormattedText());
				}
				sb.append(CGenUtils.joinStrings(args, ", "));
				sb.append(");");
			}

			P416Parser parser = getParser(sb.toString());
			MethodCallStatementContext methodCallStatementContext = (MethodCallStatementContext) new PopulateExtendedContextVisitor().visit(parser.methodCallStatement());
			this.setContext(methodCallStatementContext);

		}
	}

	public IMatchNode getMatchNode(){
		if (getContext() instanceof ApplyMethodCallContext) {
			IField predicateField =null;
			if(getPredicateSymbol()!=null){
				String predicate = AbstractBaseExt.getExpression(getPredicateSymbol()).TerminalValue();
				predicateField = AbstractBaseExt.getPredicateField(this, predicate);
			}
			String tableName = getExtendedContext(((ApplyMethodCallContext) getContext()).lvalue()).getFormattedText();
			TableDeclarationContextExt tableDeclarationContextExt = (TableDeclarationContextExt) resolve(tableName);
			return new CMatchNode(tableDeclarationContextExt.getTableDef(), predicateField, DRMTConstants.matchDelay);
		}
		return null;
	}
}
