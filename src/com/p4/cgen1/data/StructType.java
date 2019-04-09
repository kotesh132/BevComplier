package com.p4.cgen1.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IStruct;

public class StructType {

	@JsonIgnore
	private IStruct iStruct;
	private List<StructField> structFields;
	
	public StructType(IStruct iStruct, List<StructField> structFields) {
		this.iStruct = iStruct;
		this.structFields = structFields;
	}

	public String getName() {
		return iStruct.getNameString();
	}
	
	public List<StructField> getStructFields(){
		return this.structFields;
	}
}