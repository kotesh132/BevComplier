package com.p4.cgen1.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IStructField;

public class StructField {

	@JsonIgnore
	private IStructField iStructField;
	private Integer startSize;
	private Integer endSize;
	
	public StructField(IStructField iStructField) {
		this.iStructField = iStructField;
	}

	public String getName() {
		return iStructField.getNameString();
	}
	
	public Integer getSize() {
		return Integer.parseInt(iStructField.getTypeRef().getSize());
	}
	
	public void setStartSize(Integer startSize) {
		this.startSize = startSize;
	}
	
	public void setEndSize(Integer endSize) {
		this.endSize = endSize;
	}
	
	public Integer getStartSize() {
		return this.startSize;
	}
	
	public Integer getEndSize() {
		return this.endSize;
	}
	
	
	public Boolean getIsBaseType(){
		if(iStructField.getTypeRef().isBaseType())
			return true;
		else
			return false;
	}
	
	public Boolean getIsTypeNameType(){
		return iStructField.getTypeRef().isTypeName();
	}
	
	public String getPrefixedType(){
		return iStructField.getTypeRef().getPrefixedType();
	}
}