package com.p4.stepper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Field {
	private String name;
	private Integer srcOffset;
	private Integer dstOffset;
	private Integer size;
	
	public Field(Field.UnNormalized un) {
		name = un.name;
		srcOffset = un.srcOffset;
		dstOffset = un.dstOffset;
		size = un.size;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String name;
		public Integer srcOffset;
		public Integer dstOffset;
		public Integer size;
	}
}
