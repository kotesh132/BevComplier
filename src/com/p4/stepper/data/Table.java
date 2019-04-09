package com.p4.stepper.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Table {
	private String name;
	private Integer id;
	private String scope;
	private List<Key> keys;
	
	public Table(Table.UnNormalized un) {
		name = un.name;
		id = un.id;
		scope = un.scope;
		if(un.keys != null) {
			keys = new ArrayList<Key>();
			for(Key.UnNormalized kUn: un.keys) {
				keys.add(new Key(kUn));
			}
		}
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String name;
		public Integer id;
		public String scope;
		public List<Key.UnNormalized> keys;
	}
}
