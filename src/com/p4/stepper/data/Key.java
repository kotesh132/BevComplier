package com.p4.stepper.data;

import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.stepper.types.MatchType;
import com.p4.utils.Utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter

public class Key {
	
	private String name;
	private MatchType type;
	private Integer pvOffset;
	private Integer size;

	@Setter
	private BitSet value;
	
	@Getter(AccessLevel.NONE)
	private Logger L = LoggerFactory.getLogger(Key.class);
	
	public Key(String name, MatchType type, Integer pvOffset, Integer size) {
		this.name = name;
		this.type = type;
		this.pvOffset = pvOffset;
		this.size = size;
	}
	
	public Key(String name, MatchType type, Integer pvOffset, Integer size, BitSet value) {
		this.name = name;
		this.type = type;
		this.pvOffset = pvOffset;
		this.size = size;
		this.value = value;
	}
	
	public Key(Key.UnNormalized un) {
		name = un.name;
		type = un.type;
		pvOffset = un.pvOffset;
		size = un.size;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String name;
		public MatchType type;
		public Integer pvOffset;
		public Integer size;
	}
	
	public void summary() {
		L.info("Key: name=" + name + ", type=" + type + ", value=" + Utils.bitSetToHex(value));
	}
}
