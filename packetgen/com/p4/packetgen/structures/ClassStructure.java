package com.p4.packetgen.structures;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.ST;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ExtendedContextGetVisitor;
import com.p4.packetgen.utils.Utils;

public class ClassStructure {

	private String name;
	private ParserRuleContext ctx;
	private Map<String,String> fieldsMap;
	private ST constraint;
	
	public ClassStructure(){
		setFieldsMap(new HashMap<String,String>());
	}
	
	public ST getST(ExtendedContextGetVisitor extendedContextVisitor){
		ST stclass = null;
		AbstractBaseExt extendedContext =  extendedContextVisitor.visit(ctx);
		if(extendedContext.hasPrefixedType()){
			stclass = Utils.getStgGrp().getInstanceOf("RegularClass");
		} else {
			stclass = Utils.getStgGrp().getInstanceOf("ClassWithBv");
			getFieldsMap().put("dummy","rand_var < int >");
			if(constraint != null)
				stclass.add("constraint", constraint);
		}
		stclass.add("classname",name);
		stclass.add("fieldsMap",getFieldsMap());
		return stclass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addField(String left,String right){
		fieldsMap.put(left, right);
	}
	
	public ParserRuleContext getCtx() {
		return ctx;
	}
	public void setCtx(ParserRuleContext ctx) {
		this.ctx = ctx;
	}
	public Map<String,String> getFieldsMap() {
		return fieldsMap;
	}
	public void setFieldsMap(Map<String,String> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}

	public ST getConstraint() {
		return constraint;
	}

	public void setConstraint(ST constraint) {
		this.constraint = constraint;
	}
	
}
