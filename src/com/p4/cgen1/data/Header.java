package com.p4.cgen1.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IHeader;

public class Header {

	@JsonIgnore
	private IHeader iHeader;
	private List<StructField> structFields;
	private Integer isValid;
	
	public Header(IHeader iHeader, List<StructField> structFields) {
		this.iHeader = iHeader;
		this.structFields = structFields;
	}

	public String getName() {
		return iHeader.getNameString();
	}
	
	public List<StructField> getStructFields(){
		return this.structFields;
	}
	
	public Integer getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}
}
