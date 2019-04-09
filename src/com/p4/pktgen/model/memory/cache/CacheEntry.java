package com.p4.pktgen.model.memory.cache;

import java.util.BitSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CacheEntry {
	
	private BitSet key;
	private BitSet data;
	private BitSet ptr;
}
