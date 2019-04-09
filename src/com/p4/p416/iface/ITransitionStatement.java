package com.p4.p416.iface;

import java.util.List;

public interface ITransitionStatement extends IIRNode {

	Boolean hasNextInStateExpression();
	
	String getNextVariable();

	List<IExpression> getSelectExpressionList();

}
