package com.p4.packetgen.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.p416.gen.P416Parser.ParserStateContext;
import com.p4.p416.gen.ParserStateContextExt;
import com.p4.packetgen.structures.Node;

public class StatesInfo {

	private static final Logger L = LoggerFactory.getLogger(StatesInfo.class);
	private Map<String,ParserStateContext> states;
	private Node node;
	private Integer bits;
	private String value;

	public StatesInfo(){
		setBits(null);
		setValue(null);
		node = null;
		states = new HashMap<String,ParserStateContext>();
	}

	public void add(String name,ParserStateContext state){
		states.put(name,state);
	}

	public ParserStateContext getState(String name){
		return states.get(name);
	}

	public ParserStateContext getCustomState(String name){
		return states.get(name);
	}

	public ST getRandomizeApi(){
		ST ret = Utils.getStgGrp().getInstanceOf("randomizeApi");
		List<ST> stmts = getLines(node,"randomizeExtract");
		ret.add("stmts",stmts);
		return ret;
	}

	public ST getToStringApi(){
		ST ret = Utils.getStgGrp().getInstanceOf("ToStringApi");
		List<ST> stmts = getLines(node,"extractObj");
		ret.add("stmts",stmts);
		return ret;
	}


	public Node getNode() {
		return node;
	}

	public void setNode() {
		ParserStateContext parserStateContext = states.get("start");
		Node top = new Node();
		ParserStateContextExt.getExtendedContext(parserStateContext).getNode(top, this);
		node = top;
	}

	public List<ST> getLines(Node node,String st_type){
		List<ST> stmts = new ArrayList<ST>();
		if(node.getExtracts() != null){
			for(String s : node.getExtracts()){
				if(getValue() != null && getBits()!= null){
					ST prefix = Utils.getStgGrp().getInstanceOf("hardSetBits");
					prefix.add("type", s);
					prefix.add("bits", getBits());
					prefix.add("value", getValue());
					stmts.add(prefix);
					setBits(null);
					setValue(null);
				}
				ST extract = Utils.getStgGrp().getInstanceOf(st_type);
				extract.add("name", s);
				stmts.add(extract);
			}
		}
		if(node.isLeaf()){
			return stmts;
		} else {
			if(node.getChildren().size()==1){
				stmts.addAll(getLines(node.getChildren().get(0),st_type));
			} else {
				ST if_ = Utils.getStgGrp().getInstanceOf("ifObj");
				Node firstChild = node.getChildren().get(0);
				ST cond = firstChild.getConstraintsST();
				if(node.hasLookahead() && st_type.equals("randomizeExtract")){
					setBits(node.getLookaheadValue());
					setValue(firstChild.getRHSOfConstraint());
				}
				if_.add("condition",cond);
				if_.add("stmts",getLines(firstChild,st_type));
				stmts.add(if_);
				for(int i=1;i<node.getChildren().size()-1;i++){
					ST elseif_ = Utils.getStgGrp().getInstanceOf("elseifObj");
					Node child = node.getChildren().get(i);
					cond = child.getConstraintsST();
					if(node.hasLookahead() && st_type.equals("randomizeExtract")){
						setBits(node.getLookaheadValue());
						setValue(child.getRHSOfConstraint());
					}
					elseif_.add("condition",cond);
					elseif_.add("stmts",getLines(child,st_type));
					stmts.add(elseif_);
				}
				Node lastChild = node.getChildren().get(node.getChildren().size()-1);
				if(lastChild.hasConstraint()){
					ST elseif_ = Utils.getStgGrp().getInstanceOf("elseifObj");
					cond = lastChild.getConstraintsST();
					if(node.hasLookahead() && st_type.equals("randomizeExtract")){
						setBits(node.getLookaheadValue());
						setValue(lastChild.getRHSOfConstraint());
					}
					elseif_.add("condition",cond);
					List<ST> stmts_ = getLines(lastChild,st_type);
					if(stmts_.size() >0){
						elseif_.add("stmts",stmts_);
						stmts.add(elseif_);
					}
				} else {
					ST else_ = Utils.getStgGrp().getInstanceOf("elseObj");
					List<ST> stmts_ = getLines(lastChild,st_type);
					if(stmts_.size() >0){
						else_.add("stmts",stmts_);
						stmts.add(else_);
					}
				}
			}
		}
		return stmts;
	}

	public boolean contains(String name) {
		return states.keySet().contains(name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String string) {
		this.value = string;
	}

	public Integer getBits() {
		return bits;
	}

	public void setBits(Integer i) {
		this.bits = i;
	}

}

