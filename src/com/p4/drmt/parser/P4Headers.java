package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class P4Headers implements Summarizable{
	protected static final Logger L = LoggerFactory.getLogger(P4Headers.class);
	private final List<P4Type> allTypes = new ArrayList<>();
	
	private final Stack<P4Type> lastTypeDecl = new Stack<>();

	private final Map<String,String> st = new HashMap<>();
	
	private final List<P4HeaderInst> instances = new ArrayList<>();
	
	@Override
	public String summary() {
		return Utils.summary(allTypes);
	}
	
	public List<String> flattenAllTypes(){
		List<String> ret = new ArrayList<>();
		for(P4HeaderInst instance: instances){
			L.debug(instance.getName()+" of type "+instance.getType());
			flatten(instance.getName(), instance.getType(), ret);
		}
		return ret;
	}
	
	private P4Type getTypeByName(String typeName){
		P4Type t1 = null; 
		for(P4Type t:allTypes){
			if(!t.isBaseType() && t.getName().equals(typeName)){
				t1 = t;
			}
		}
		return t1;
	}
	
	private void flatten(String prefix, String type, List<String> ret){
		P4Type t1 = getTypeByName(type);
		if(t1 !=null ){
			if(t1 instanceof P4Struct){
				for(P4Type field: ((P4Struct) t1).getTypes()){
					if(field.isBaseType()){
						ret.add(prefix+"."+field.getName());
					}else if(field instanceof P4HeaderInst){
						flatten(prefix+"."+((P4HeaderInst) field).getName(), ((P4HeaderInst) field).getType(), ret);
					}else{
						throw new RuntimeException("Unknown Declaration Construct");
					}
				}
			}else if(t1 instanceof P4Header){
				for(BitStringType b: ((P4Header) t1).getFields()){
					ret.add(prefix+"."+b.getName());
				}
				ret.add(prefix+".isValid");
			}else{
				throw new RuntimeException("Unknown Declaration Construct");
			}
		}else{
			throw new RuntimeException("Couldn't resolve "+type);
		}
	}
	
	public P4Header resolveHeader(String name){
		String[] parts = name.split("\\.");
		if(parts.length==2){
			String st = this.st.get(parts[0]);
			P4Struct s = getP4Struct(st);
			P4HeaderInst hi = s.getHeaderByName(parts[1], this);
			return this.getHeaderByName(hi.getType());
		}
		throw new IllegalArgumentException("Can't resolve header type for "+name);
	}
	
	private P4Struct getP4Struct(String name){
		for(P4Type p:allTypes){
			if(p instanceof P4Struct){
				if(((P4Struct) p).getName().equals(name)){
					return (P4Struct) p;
				}
			}
		}
		throw new IllegalArgumentException("No definition found for "+name);
	}
	
	private P4Header getHeaderByName(String name){
		for(P4Type p:allTypes){
			if(p instanceof P4Header){
				if(((P4Header) p).getName().equals(name)){
					return (P4Header) p;
				}
			}
		}
		throw new IllegalArgumentException("No definition found for "+name);
	}
	
	public Pair<P4Header, BitStringType> resolveHeaderField(String name){
		String[] parts = name.split("\\.");
		if(parts.length==3){
			String st = this.st.get(parts[0]);
			P4Struct s = getP4Struct(st);
			P4HeaderInst hi = s.getHeaderByName(parts[1], this);
			P4Header h = this.getHeaderByName(hi.getType());
			BitStringType b = h.getField(parts[2]);
			return Pair.of(h, b);
		}
		throw new IllegalArgumentException("No definition found for "+name);
	}
}
