package com.p4.drmt.parser;

public class Test {

	public static void main(String[] args) {
		
		FW f = ByteUtils.parseP4Number("16w0x86dd");
		for(FW m: f.byteOrder()){
			System.out.println(m.summary());
		}
		//System.out.println(ByteUtils.maskByte(f).summary());
	}


	


}
