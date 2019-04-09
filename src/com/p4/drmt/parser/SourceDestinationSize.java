package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import lombok.Data;

@Data
public class SourceDestinationSize implements Summarizable{
	public final int sourceBit;
	public final int destBit;
	public final int size;
	public final String fieldName;
	public final String fullName;
	public final boolean emit;
	public final boolean varbit;
	@Override
	public String summary() {
		return fieldName+"[s="+sourceBit+",d="+destBit+",sz="+size+"]";
	}
}
