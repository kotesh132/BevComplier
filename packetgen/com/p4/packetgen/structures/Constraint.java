package com.p4.packetgen.structures;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.stringtemplate.v4.ST;

import com.p4.packetgen.utils.Utils;

public class Constraint {

	private String lhs;
	private String op;
	private String rhs;
	
	public Constraint(String lhs,String op,String rhs){
		this.setLhs(lhs);
		this.setRhs(rhs);
		this.setOp(op);
	}
	
	
	public ST getConditionForPacketHeader(){
		ST ret = Utils.getStgGrp().getInstanceOf("condition");
		ret.add("lhs",getLhs()+".getValue");
		ret.add("rhs",getRhs());
		ret.add("op",getOp());
		return ret;
	}
	
	public ST getConditionWithoutSuffix(){
		ST ret = Utils.getStgGrp().getInstanceOf("condition");
		ret.add("lhs",getLhs());
		ret.add("rhs",getRhs());
		ret.add("op",getOp());
		return ret;
	}

	public String getLhs() {
		return lhs;
	}

	public void setLhs(String lhs) {
		this.lhs = lhs;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getRhs() {
		return rhs;
	}

	public void setRhs(String rhs) {
		this.rhs = rhs;
	}

	@JsonIgnore
	public Constraint deepcopy() {
		return new Constraint(getLhs(), getOp(), getRhs());
	}
	
}

