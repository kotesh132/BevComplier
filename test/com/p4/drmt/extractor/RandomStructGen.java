package com.p4.drmt.extractor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import com.p4.utils.Summarizable;

import lombok.Data;
@Data
public class RandomStructGen implements Summarizable{
	private static final Logger L = LoggerFactory.getLogger(RandomStructGen.class);
	private final HashMap<String, Integer> map = new LinkedHashMap<>();
	private final int numFields = 10;
	private final String name;
	
	private final static String fieldPrefix = "field";
	private static final int maxFSize = 32;
	
	public RandomStructGen(String name){
		this.name = name;
		Random r = new Random();
		int totalSize = 0;
		int i = 0;
		for(;i<numFields-1;i++){
			int size = 1+r.nextInt(maxFSize);
			map.put(fieldPrefix+i, size);
			totalSize+=size;
		}
		int lastSize = maxFSize-(totalSize%maxFSize);
		if(lastSize>0)
			map.put(fieldPrefix+i, lastSize);
	}
	
	public int totalSize(){
		int size = 0;
		for(Entry<String, Integer> e: map.entrySet()){
			size+=e.getValue();
		}
		return size;
	}
	
	@Override
	public String summary(){
		StringBuilder sb = new StringBuilder();
		sb.append("struct "+name+"{\n");
		for(Entry<String, Integer> e: map.entrySet()){
			sb.append("\tbit<"+e.getValue()+"> "+e.getKey()+";\n");
		}
		sb.append("};\n");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		RandomStructGen r = new RandomStructGen("r");
		System.out.println(r.summary());
		System.out.println(r.totalSize());
		
		
	}
}
