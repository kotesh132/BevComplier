package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.p4.drmt.struct.MANode;
import com.p4.p416.iface.IApplyMethodCall;
import com.p4.p416.iface.IBlockStatement;
import com.p4.p416.iface.IControlBody;
import com.p4.p416.iface.IStatementOrDeclaration;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.p416.gen.P416Parser.ControlBodyContext;
import com.p4.tools.graph.structs.TableMap;

public class ControlBodyContextExt extends AbstractBaseExt implements IControlBody {

	public ControlBodyContextExt(ControlBodyContext ctx) {
		super(ctx);
	}

	@Override
	public  ControlBodyContext getContext(){
		return (ControlBodyContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ControlBodyContext getContext(String str){
		return (ControlBodyContext)new PopulateExtendedContextVisitor().visit(getParser(str).controlBody());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ControlBodyContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ControlBodyContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void buildST(TableMap tm){
		tm.markEnterApplyControl();
		super.buildST(tm);
		tm.markExitApplyControl();
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		ControlBodyContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		blockStatementContextExt.quadrupalize(true, statementOrDeclarationContextExts2);
		StringBuilder sb = new StringBuilder();
		assert(statementOrDeclarationContextExts2.size()==0 || statementOrDeclarationContextExts2.size()==1);
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:statementOrDeclarationContextExts2){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		BlockStatementContext blockStatementContext = blockStatementContextExt.getContext(sb.toString());
		blockStatementContextExt.setContext(blockStatementContext);
	}
	@Override
	public void removeElse(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
		ControlBodyContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		blockStatementContextExt.removeElse(statementOrDeclarationContextExts2, branchStack);
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:statementOrDeclarationContextExts2){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = blockStatementContextExt.getContext(sb.toString());
		blockStatementContextExt.setContext(blockStatementContext);
	}

	@Override
	public void removeSwitch(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack) {
		ControlBodyContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		blockStatementContextExt.removeSwitch(statementOrDeclarationContextExts2, branchStack);
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:statementOrDeclarationContextExts2){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = blockStatementContextExt.getContext(sb.toString());
		blockStatementContextExt.setContext(blockStatementContext);
	}

	@Override
	public List<IApplyMethodCall> getApplyMethodCalls() {
		List<IApplyMethodCall> applyMethodCalls = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IApplyMethodCall) {
				applyMethodCalls.add((IApplyMethodCall) node);
			}
		});
		return applyMethodCalls;
	}

	@Override
	public List<IStatementOrDeclaration> getStatementOrDeclarations() {
		List<IStatementOrDeclaration> statementOrDeclarations = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IStatementOrDeclaration) {
				statementOrDeclarations.add((IStatementOrDeclaration) node);
				return false;
			}
			return true;
		});
		return statementOrDeclarations;
	}

	@Override
	public IBlockStatement getBlockStatement() {
		return (IBlockStatement) getExtendedContext(getContext().blockStatement());
	}

	public List<MANode> getAllMANodes(){
		List<MANode> ret = new ArrayList<>();
		visitNode(node -> {
			if(node instanceof AssignmentStatementContextExt){
				ret.addAll( ((AssignmentStatementContextExt)node).allALUOperations() );
			}else if(node instanceof ApplyMethodCallContextExt){
				ret.add(((ApplyMethodCallContextExt)node).getMatchNode());
			}
		});
		return ret;
	}

	public IControlBody getCppTransformed() {
		ControlBodyContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		blockStatementContextExt.getCppTransformedControlBody();
		return (ControlBodyContextExt) getExtendedContext(getContext(blockStatementContextExt.getFormattedText()));
	}
}
