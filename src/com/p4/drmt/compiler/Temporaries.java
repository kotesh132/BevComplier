package com.p4.drmt.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.p4.utils.Utils.Pair;

public class Temporaries {

	private static List<AtomicInteger> channels = new ArrayList<>();
	private static Pair<String,Integer> bit = Pair.of("tb", 0);
	private static Pair<String,Integer> byt = Pair.of("t", 1);
	static {
		channels.add(bit.second(), new AtomicInteger(0));
		channels.add(byt.second(), new AtomicInteger(0));
	}
	
	public static synchronized String nextTempIdByte(){
		return byt.first()+channels.get(byt.second()).incrementAndGet();
	}
	
	public static synchronized String nextTempIdBit(){
		return bit.first()+channels.get(bit.second()).incrementAndGet();
	}
}
