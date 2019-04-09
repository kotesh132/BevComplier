package com.p4.p416.gen;

import com.p4.p416.gen.P416Parser.ApplyMethodCallContext;
import com.p4.p416.gen.P416Parser.MethodCallStatementContext;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public abstract class MethodCallStatementContextExt extends AbstractBaseExt {

	public MethodCallStatementContextExt(ParserRuleContext context) {
		super(context);
	}

	/*

	@Override
	public void getNode(Node node,StatesInfo si){
		AssignmentOrMethodCallStatementContext ctx = (AssignmentOrMethodCallStatementContext) getContext();
		if(ctx.lvalue().getText().contains("extract")){	
			if(ctx.argumentList().getText().equals("")){
				L.warn("no arguments passed in extract");
			} else {
				String[] args = ctx.argumentList().getText().split(",");
				if(args.length > 1){
					L.warn("more than 1 argument supplied in extract");
				} else {
					String rhs = ctx.argumentList().getText();
					node.addExtract(rhs);
				}
			}
		}
		super.getNode(node, si);
	}

	@Override
	public void buildST(TableMap tm){
		AssignmentOrMethodCallStatementContext ctx = (AssignmentOrMethodCallStatementContext) getContext();
		if(tm.currentControlScope().applyTraverseStatus){
			//TODO Something better
			if(ctx.lvalue().extendedContext.getFormattedText().endsWith(".apply")){
				String tableName = Utils.replaceSuffix(ctx.lvalue().extendedContext.getFormattedText(), ".apply", "");
				DummyNode currentNode = (DummyNode) tm.currentControlScope().cg.currentNode;
				DummyNode leaf = new DummyNode(tableName+"_c");
				TableNode tn = new TableNode(tableName, leaf);
				tn.parent = currentNode;
				leaf.parent = tn;
				currentNode.addSingleTableNode(tn);
				tm.currentControlScope().cg.currentNode = leaf;
			}
		}
		super.buildST(tm);
	}
	 */
	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MethodCallStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MethodCallStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
	@Override
	public void getCppTransformedControlBody(){
		StringBuilder sb = new StringBuilder();
		MethodCallStatementContext methodCallStatementContext = (MethodCallStatementContext)getContext();
		if(methodCallStatementContext instanceof ApplyMethodCallContext){

			ApplyMethodCallContext applyMethodCallContext = (ApplyMethodCallContext)getContext();
			sb.append(getExtendedContext(applyMethodCallContext.lvalue()).getFormattedText());
			sb.append("_");
			sb.append(applyMethodCallContext.APPLY().getText());
			sb.append(applyMethodCallContext.LPARAN().getText());
			if(applyMethodCallContext.argumentList() != null){
				sb.append(getExtendedContext(applyMethodCallContext.argumentList()).getFormattedText());
			}
			sb.append(applyMethodCallContext.RPARAN().getText());
			sb.append(applyMethodCallContext.SEMI().getText());

		}
		String abc = getExtendedContext(methodCallStatementContext).getFormattedText().replace(".apply", "_apply");
		MethodCallStatementContext methodCallStatementContext_new = getContext(abc);
		addToContexts(methodCallStatementContext_new);
	}

}
