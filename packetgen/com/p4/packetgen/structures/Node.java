package com.p4.packetgen.structures;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.packetgen.utils.Utils;

public class Node {

	private static final Logger L = LoggerFactory.getLogger(Node.class);
	private List<Node> children;
	private List<Constraint> constraints;
	private List<String> extracts;
	private boolean accept;
	private boolean reject;


	private boolean hasLookahead;
	private int lookaheadValue;
	private String lookaheadVar;


	private int weight;

	public Node(){
		setChildren(new ArrayList<Node>());
		setConstraints(new ArrayList<Constraint>());
		setExtracts(new ArrayList<String>());
		setAccept(false);
		setReject(false);
		setHasLookahead(false);
		weight = 0;
	}

	public ST getConstraintsST(){
		ST ret = Utils.getStgGrp().getInstanceOf("conditions");
		List<ST> conditions = new ArrayList<ST>();
		for(Constraint c : constraints){
			conditions.add(c.getConditionForPacketHeader());
		}
		ret.add("conditions",conditions);
		ret.add("op","&&");
		return ret;
	}
	
	public String getRHSOfConstraint(){
		if(getConstraints().size()>1){
			L.error("Trying to get RHS of a lookahead & encountered multiple constraints.");
			return null;
		} else {
			return getConstraints().get(0).getRhs();
		}
	}

	public ST getConstraintsSTWithoutSuffix(){
		ST ret = Utils.getStgGrp().getInstanceOf("conditions");
		List<ST> conditions = new ArrayList<ST>();
		for(Constraint c : constraints){
			conditions.add(c.getConditionWithoutSuffix());
		}
		ret.add("conditions",conditions);
		ret.add("op","&&");
		return ret;
	}

	public void calculateWeights(){
		if(isLeaf()){
			setWeight(1);
		} else { 
			int thisWeight = 0;
			for(Node child : getChildren()){
				child.calculateWeights();
				thisWeight += child.getWeight();
			}
			setWeight(thisWeight);
		}
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	private Map<ST, Integer> getChildConstraints() {
		Map<ST,Integer> childConstraints = new LinkedHashMap<ST,Integer>();
		for(Node child : getChildren()){
			if(child.hasConstraint()){
				childConstraints.put(child.getConstraintsSTWithoutSuffix(),child.getWeight());
			}
		}
		return childConstraints;
	}

	private void addToWeights(List<ST> weights,int i,Integer integer,String className,String varname){
		ST weight_ = Utils.getStgGrp().getInstanceOf("weights");
		weight_.add("first", i);
		weight_.add("second", integer);
		weight_.add("class", className);
		weight_.add("varname", varname);
		weights.add(weight_);
	}

	private void addToConstraints(List<ST> constraints,int value,String className,ST constraint,String varname){
		ST constraint_ = Utils.getStgGrp().getInstanceOf("implicationConstraints");
		constraint_.add("value", value);
		constraint_.add("class", className);
		constraint_.add("expression", constraint);
		constraint_.add("varname", varname);
		constraints.add(constraint_);
	}

	public void getConstraintLines(List<ST> constLines) {
		if(!isLeaf()){
			Map<ST,Integer> childConstraints = getChildConstraints();
			if(childConstraints.size()>0){
				String className = "";
				String varname = getLookaheadVar()+"_";
				if(!hasLookahead()){
					className = getClassName();
					varname = "";
				}
				List<ST> weights = new ArrayList<ST>();
				List<ST> constraints = new ArrayList<ST>();
				int i=0;
				for(ST constraint : childConstraints.keySet()){
					addToWeights(weights,i,childConstraints.get(constraint),className,varname);
					addToConstraints(constraints,i,className,constraint,varname);
					i++;
				}
				addToWeights(weights,i,1,className,varname);
				ST conds = Utils.getStgGrp().getInstanceOf("notOfOrConditionsForConstructor");
				conds.add("stmts",childConstraints);
				addToConstraints(constraints, i, className, conds,varname);
				ST cLines = null;
				String  classOrGen= null;
				if(hasLookahead()){
					cLines = Utils.getStgGrp().getInstanceOf("linesforGenerator");
					cLines.add("varname", varname);
					classOrGen = getLookaheadVar()+"_gen"+".";
				} else {
					cLines = Utils.getStgGrp().getInstanceOf("constraintLinesForConstructor");
					cLines.add("class", className);
					classOrGen = className;
				}
				cLines.add("classOrGen", classOrGen);
				cLines.add("distLines", weights);
				cLines.add("constLines", constraints);
				constLines.add(cLines);
			}
			for(Node child : getChildren()){
				child.getConstraintLines(constLines);
			}
		}
	}

	private String getClassName(){
		String name = getExtracts().get(getExtracts().size()-1);
		return (name.equals("") || name == null) ? "" : name+".";
	}

	//	public void getAllPaths(List<Path> paths,Path p){
	//		if(isAccept() || isReject() || isLeaf()){
	//			Path copy = p.deepcopy();
	//			paths.add(copy);
	//		} else {
	//			if(getConstraints().size()>0)
	//				p.add(getConstraints());
	//			if(getExtracts().size()>0)
	//				p.add(getExtracts());
	//			for(Node n:getChildren()){
	//				n.getAllPaths(paths, p);
	//			}
	//			if(getConstraints().size()>0)
	//				p.remove(getConstraints());
	//			if(getExtracts().size()>0)
	//				p.remove(getExtracts());
	//		}
	//	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public boolean isLeaf(){
		return children != null && children.size()>0 ? false : true;
	}

	public void addChild(Node node){
		children.add(node);
	}


	public boolean hasConstraint(){
		return constraints.size() >0 ? true : false;
	}

	public List<String> getExtracts() {
		return extracts;
	}

	public void setExtracts(List<String> extracts) {
		this.extracts = extracts;
	}

	public void addExtract(String extract){
		extracts.add(extract);
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public boolean isReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}

	public void addToConstraints(Constraint c){
		this.constraints.add(c);
	}

	public boolean hasLookahead() {
		return hasLookahead;
	}

	public void setHasLookahead(boolean hasLookahead) {
		this.hasLookahead = hasLookahead;
	}

	public int getLookaheadValue() {
		return lookaheadValue;
	}

	public void setLookaheadValue(int lookaheadValue) {
		this.lookaheadValue = lookaheadValue;
	}
	
	public String getLookaheadVar() {
		return lookaheadVar;
	}

	public void setLookaheadVar(String lookaheadVar) {
		this.lookaheadVar = lookaheadVar;
	}
}

