package com.p4.drmt.parser;

import com.p4.p416.expressions.ParserALUOp;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class P4Extract implements Summarizable{
	private final String headerActualName;	
	private final String header;
	//private final AF af;
	private final List<SourceDestinationSize> fields = new ArrayList<>();
	//private final int startingAddress;
	//private final int size;
	private final int validLoc;
	private final String validField;
	private final ParserALUOp parserALUOp;
	private final boolean isALUneeded;
	public int getTotalSize(){
		int ret = 0;
		for(SourceDestinationSize s:fields){
			ret+=s.size;
		}
		return ret;
	}
	public int getMinSize(){
		int ret = 0;
		for(SourceDestinationSize s:fields){
			if(!s.isVarbit())ret+=s.size;
		}
		return ret;
	}
	
	public int sizeInBytes(){
		return Utils.ceil(getTotalSize(), 8);
	}
	@Override
	public String summary() {
		return header;
	}
	public static List<SourceDestinationSize> getFields(List<P4Extract> ects){
		List<SourceDestinationSize> ret = new ArrayList<>();
		for(P4Extract p:ects){
			ret.addAll(p.fields);
		}
		return ret;
	}
	
	/*
	public int sizeInBytes(){
		return Utils.ceil(af.getSize(), 8);
	}
	
	public int startingAddressInBytes(){
		return Utils.ceil(af.getStartingAddress(), 8);
	}
	
	public static List<AF> getAFs(List<P4Extract> input){
		List<AF> ret = new ArrayList<>();
		for(P4Extract p:input){
			ret.add(p.af);
		}
		return ret;
	}
	*/
	public static P4Extract filterALUneeded(List<P4Extract> extracts){
		P4Extract extract = null;
		int count = 0;
		for(P4Extract extract1:extracts){
			if(extract1.isALUneeded){
				extract = extract1;
				count++;
			}
		}
		if(count>1){
			throw new UnsupportedOperationException("Can't handle more than one extracts with ALU operation");
		}
		return extract;
	}
}
