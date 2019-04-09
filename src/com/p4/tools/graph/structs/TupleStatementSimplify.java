package com.p4.tools.graph.structs;

import java.util.ArrayList;
import java.util.List;

import com.p4.drmt.compiler.Temporaries;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.p416.gen.VariableDeclarationContextExt;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils.Pair;

import lombok.Data;
@Data
public class TupleStatementSimplify implements Summarizable{
	private ExpressionNode simpleExpression = null;
	private final List<Pair<VariableDeclarationContextExt, AssignmentStatementContextExt>> tempAssigns = new ArrayList<>();
	
	public String getSimpleExpressionText(){
		ExpressionContextExt expressionContextExt = (ExpressionContextExt) simpleExpression;
		return expressionContextExt.getFormattedText().trim();
	}
	
	public static VariableDeclarationContextExt getDeclaration(String tempVariableName, String type){
		return AbstractBaseExt.getDeclaration(type+" "+tempVariableName+";");
	}
	public static AssignmentStatementContextExt getAssignment(String lhs, String rhs){
		return AbstractBaseExt.getAssignment(lhs+" = "+rhs+";");
	}
	
	public TupleStatementSimplify terminalize(){
		if(simpleExpression.isTerminal()){
			return this;
		}else if(simpleExpression.isSimpleExpression()){
			String temp = Temporaries.nextTempIdByte();
			TupleStatementSimplify ret = new TupleStatementSimplify();
			ret.setSimpleExpression(AbstractBaseExt.getExpression(temp));
			ret.tempAssigns.addAll(this.tempAssigns);
			int size = simpleExpression.getResultSize();
			String type = size==1? "bit":"bit<"+size+">";
			ret.tempAssigns.add(Pair.of(getDeclaration(temp, type), getAssignment(temp, this.getSimpleExpressionText())));
			return ret;
		}else{
			throw new RuntimeException();
		}
	}
	
	@Override
	public String summary() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for(Pair<VariableDeclarationContextExt, AssignmentStatementContextExt> p:tempAssigns){
			sb.append(p.first().getFormattedText()+"\n");
			sb.append(p.second().getFormattedText()+"\n");
		}
		sb.append(getSimpleExpressionText());
		return sb.toString();
	}
}
