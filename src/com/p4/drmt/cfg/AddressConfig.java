package com.p4.drmt.cfg;

import java.util.LinkedHashSet;
import java.util.Set;

import com.p4.utils.Utils.Pair;

public class AddressConfig {

	private static Set<Pair<String,String>> map = new LinkedHashSet<>();
	static {
		map.add(Pair.of("hdr", "headers"));
		map.add(Pair.of("meta", "metadata"));
		map.add(Pair.of("standard_metadata", "standard_metadata_t"));
	}
	
	public static boolean containsGlobalPredefined(String symbolName, String type){
		return map.contains(Pair.of(symbolName, type));
	}
}
