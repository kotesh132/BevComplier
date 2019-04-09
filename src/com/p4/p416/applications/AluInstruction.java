package com.p4.p416.applications;

import com.p4.drmt.struct.IOperator;

public interface AluInstruction {
	public int cond_en();
	public int cond_off();
	public int op0_type();
	public int op0_len();
	public int op0_off();
	public int op1_type();
	public int op1_len();
	public int op1_off();
	public int op_code();
	public int res_len();
	public int res_off();
	public int pt_step();
	public boolean isConditional();
	public String summary();
	
	public enum OpCode implements IOperator{
		NOOP(0, ""),
		COPY(1, ""),
		ADD(2, "+"),
		SUB(3, "-"),
		XOR(4, "^"),
		AND(5, "&"),
		OR(6, "|"),
		SHL(7, "<<"),
		EQ(8, "=="),
		GT(9, ">"),
		LT(10, "<"),
		NE(11, "!=");
		public int type;
		public String val;
		private OpCode(int type, String v){
			this.type = type;
			this.val = v;
		}
		public int getOPCode()
		{
			return type;
		}
		public static OpCode getOpCodeof(int v){
			for(OpCode o: OpCode.values()){
				if(v == o.type)
					return o;
			}
			throw new RuntimeException("No Opcode found matching value "+v);
		}

		public String summary(){
			return val;
		}
	}

}
