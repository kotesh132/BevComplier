package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416BaseVisitor;

public class SemanticChecksPass {	
	public static Set<String> reservedKeywords = new HashSet<String>();
	static{
		reservedKeywords.addAll(Arrays.asList(new String[]{"action","apply","bit","bool","const","control","default","else”, “enum","error","extern","exit”,“false","header","header_union","if", "in","inout","int","match_kind","package","parser","out","return","select","state","struct","switch","table","transition","true","tuple","typedef","varbit","verify","void"}));
	}
	private List<P416BaseVisitor<ParserRuleContext>> semanticCheckVisitors = new ArrayList<P416BaseVisitor<ParserRuleContext>>();
	private boolean isSemanticChecksPass;
	private SemanticChecksPass(){
		populateSemanticCheckVisitors();
	}
	private static SemanticChecksPass semanticChecksPassInstance = new SemanticChecksPass();
	private void populateSemanticCheckVisitors() {
		//new visitors go at the bottom
		this
			.addToList(new ExpressionEvaluatorVisitor()) //This should be the first Visitor as the calculated values may be needed across different visitors later
			.addToList(new NameValidationVisitor())
			.addToList(new ConstantDeclarationVisitor())
			.addToList(new ActionDeclarationVisitor())
			.addToList(new InstantiationVisitor())
			.addToList(new ControlDeclarationVisitor())
			.addToList(new StructDeclarationVisitor())
			.addToList(new ExternDeclarationVisitor())
			.addToList(new HeaderDeclarationVisitor())
			.addToList(new ParserDeclarationVisitor())
			.addToList(new TableDeclarationVisitor())
			.addToList(new SwitchStatementVisitor())
			.addToList(new PackageTypeDeclarationVisitor())
			.addToList(new HeaderUnionDeclarationVisitor())
/////		.addToList(new ActionBindVisitor())
			.addToList(new SymbolChecksVisitor())
			.addToList(new TableEntriesVisitor())
			.addToList(new AnnotationsVisitor())
		;
	}
	public List<P416BaseVisitor<ParserRuleContext>> getSemanticCheckVisitors(){
		return semanticCheckVisitors;
	}
	private SemanticChecksPass addToList(P416BaseVisitor<ParserRuleContext> visitor){
		semanticCheckVisitors.add(visitor);
		return this;
	}
	public static SemanticChecksPass getInstance() {
		return semanticChecksPassInstance;
	}
	public void setIsSemanticChecksPass(boolean b) {
		isSemanticChecksPass = true;	
	}
	public boolean isSemanticChecksPass() {
		return isSemanticChecksPass;
	}
	//clears the previous state of visitors and populates new instances visitors of so that the instances are different across a series of semantic check runs (like in test suite)
	public void init() {
		this.semanticCheckVisitors.clear();
		this.populateSemanticCheckVisitors();
		this.setIsSemanticChecksPass(true);
	}
	public void complete() {
		this.setIsSemanticChecksPass(false);
	}
}
