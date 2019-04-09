package com.p4.cgen1.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.KeyElementContextExt;
import com.p4.p416.iface.IKeyElement;

public class KeyElement {
	
	@JsonIgnore
	private IKeyElement keyElement;
	
	public KeyElement(IKeyElement keyElement) {
		this.keyElement = keyElement;
	}
	
	public String getName() {
		return keyElement.getKeyName();
	}	
	
	public Integer getSize() {
		Symbol expressionSymbol = ((KeyElementContextExt)keyElement).getResolvedKey();
		return  expressionSymbol.getSizeInBits();
	}
	
}
