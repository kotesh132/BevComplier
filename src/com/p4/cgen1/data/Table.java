package com.p4.cgen1.data;


import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.ITable;

public class Table {

	@JsonIgnore
	private ITable table;
	private List<KeyElement> keyElements;
	
	@JsonCreator
	public Table(ITable table, List<KeyElement> keyElements) {
		this.table = table;
		this.keyElements = keyElements;
	}
	
	public String getName() {
		return table.getNameString();
	}
	
	public List<KeyElement> getKeyElements() {
		return this.keyElements;
	}
	
	public Integer getTableId() {
		return table.getTableId();
	}

}
