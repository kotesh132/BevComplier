package com.p4.drmt.alu;

import com.p4.drmt.parser.FW;
import com.p4.p416.gen.AbstractBaseExt;

public class DumbConditional {

	public static FW getBr_vld(){
		return FW.ONE;
	}
	public static int getBr_data(){
		return 1;
	}
	public static int getBr_mask(){
		return 254;
	}

	public static int GetOffsetOfSymbol(AbstractBaseExt ctx, String symbol){
		/*if(symbol.endsWith("isValid")){
			System.out.println("************WARNING*****************");
			System.out.println("This is wrong offset");
			return 50;
		}else */
			return ctx.getAlignedByteOffset(symbol);
	}
}
