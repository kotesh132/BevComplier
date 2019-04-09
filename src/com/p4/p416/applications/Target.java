package com.p4.p416.applications;

import com.p4.utils.Summarizable;

public interface Target {
//DRMT target specific interfaces/
	public enum MemoryType implements Summarizable{
		TypePktBit(0, "bP", true),
		TypePktByte(1, "BP", false),
		TypeSrcBit(2, "bS", true),
		TypeSrcByte(3, "BS", false),
		TypeCfgBit(4, "bC", true),
		TypeCfgByte(5, "BC", false);
		public final int type;
		public final String val;
		public final boolean bitType;
		private MemoryType(int type, String val, boolean bitType){
			this.type = type; this.val = val;this.bitType = bitType;
		}
		public int getMemType()
		{
			return type;
		}

		public boolean isBitType(){ return bitType;}
		public String summary(){return val;}

	}
	
	
	public IMemoryManager getMemoryManager();
	
}
