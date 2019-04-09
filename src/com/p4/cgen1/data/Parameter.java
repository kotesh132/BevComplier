package com.p4.cgen1.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IParameter;

public class Parameter {

	@JsonIgnore private IParameter parameter;

	public Parameter(IParameter parameter) {
		this.parameter = parameter;
	}
	
	public String getName() {
		return parameter.getNameString();
	}

	public String getType() {
		return parameter.getType().getSymbolName();
	}

	public Integer getSize() {
		if(parameter.getTypeRef().getSize() != null){
			return Integer.parseInt(parameter.getTypeRef().getSize());
		}else{
			return null;
		}
	}

}
