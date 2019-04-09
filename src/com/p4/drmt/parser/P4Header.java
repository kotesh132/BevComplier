package com.p4.drmt.parser;

import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class P4Header implements P4Type{
	private final String name;
	private final List<BitStringType> fields = new ArrayList<>();
	
	@Override
	public void pushType(P4Type type) {
		fields.add((BitStringType) type);
	}
	
	public int totalSize(){
		int ret = 0;
		for(BitStringType b: fields){
			ret+=b.getLength();
		}
		return ret;
	}
	
	public int totalSizeinBytes(){
		return Utils.ceil(totalSize(), 8);
	}
	public boolean containsField(String field){
		for(BitStringType b: fields){
			if(field.equals(b.getIdentifier())){
				return true;
			}
		}
		return false;
	}
	
	public int sizeOf(String field){
		if(containsField(field)){
			for(BitStringType b: fields){
				if(field.equals(b.getIdentifier())){
					return b.getLength();
				}
			}
		}
		throw new IllegalStateException("No matching field["+field+"] in Header:"+name);
	}
	
	public int offSetOf(String field){
		if(containsField(field)){
			int offset = 0;
			for(BitStringType b: fields){
				if(field.equals(b.getIdentifier())){
					return offset;
				}else{
					offset+=b.getLength();
				}
			}
		}
		throw new IllegalStateException("No matching field["+field+"] in Header:"+name);
	}
	
	public BitStringType getField(String field){
		if(containsField(field)){
			for(BitStringType b: fields){
				if(field.equals(b.getIdentifier())){
					return b;
				}
			}
		}
		throw new IllegalStateException("No matching field["+field+"] in Header:"+name);
	}

	@Override
	public boolean isBaseType() {
		return false;
	}
}
