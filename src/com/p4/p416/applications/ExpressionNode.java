package com.p4.p416.applications;

import java.util.List;

public interface ExpressionNode {
	public boolean isTerminal();
	public boolean isNumber();
	public boolean isSymbol();
	public String TerminalValue();
	public boolean isLogicalExpression();
	public int getResultSize();
	public boolean isSimpleExpression();
	public boolean isBinaryExpression();
	public boolean isUnaryExpression();
	public ExpressionNode getLeft();
	public ExpressionNode getRight();
	public String getOperator();
	public List<ExpressionNode> getOperands();
}
