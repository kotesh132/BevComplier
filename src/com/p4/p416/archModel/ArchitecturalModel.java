package com.p4.p416.archModel;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416BaseVisitor;

public class ArchitecturalModel {
	
	private static ArchitecturalModel architecturalModelInstance = new ArchitecturalModel();
	private List<P416BaseVisitor<ParserRuleContext>> architecturalModelVisitors = new ArrayList<P416BaseVisitor<ParserRuleContext>>();
	
	private ArchitecturalModel(){
		populateAchitecturalModelVisitors();
	}
	
	private void populateAchitecturalModelVisitors() {
		this.addToList(new SramTcamSizeVisitor());
		this.addToList(new GraphBuilderVisitor());
	}
	
	public List<P416BaseVisitor<ParserRuleContext>> getArchitecturalModelVisitors(){
		return architecturalModelVisitors;
	}
	private ArchitecturalModel addToList(P416BaseVisitor<ParserRuleContext> visitor){
		architecturalModelVisitors.add(visitor);
		return this;
	}
	public static ArchitecturalModel getInstance() {
		return architecturalModelInstance;
	}
}
