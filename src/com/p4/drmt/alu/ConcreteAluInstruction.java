package com.p4.drmt.alu;

import com.p4.p416.applications.AluInstruction;
import com.p4.p416.gen.AssignmentStatementContextExt;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(exclude={"assignmentStatementContextExt"})
public class ConcreteAluInstruction implements AluInstruction{

	private final AssignmentStatementContextExt assignmentStatementContextExt;
	
	private final int cond_en;
	@Override public int cond_en() {return cond_en;}
	
	private final int cond_off;
	@Override public int cond_off() {return cond_off;}
	
	private final int op0_type;
	@Override public int op0_type() {return op0_type;}
	private final int op0_len;
	@Override public int op0_len() {return op0_len;}
	private final int op0_off;
	@Override public int op0_off() {return op0_off;}
	
	private final int op1_type;
	@Override public int op1_type() {return op1_type;}
	private final int op1_len;
	@Override public int op1_len() {return op1_len;}
	private final int op1_off;
	@Override public int op1_off() {return op1_off;}
	
	private final int op_code;
	@Override public int op_code() {return op_code;}
	
	private final int res_len;
	@Override public int res_len() {return res_len;}
	private final int res_off;
	@Override public int res_off() {return res_off;}
	
	private final int pt_step;
	@Override public int pt_step() {return pt_step;}
	
	private final boolean conditional;
	
	/*
	public int op_code();
	public int res_len();
	public int res_off();
	public int pt_step();
	public boolean isConditional();
	
	 */
	
	
	@Override
	public String summary() {
		StringBuffer sb = new StringBuffer();
		sb.append(assignmentStatementContextExt.getFormattedText()+"\n");
		sb.append("{\ncond_en:"+cond_en());
		sb.append("\ncond_off:"+cond_off());
		sb.append("\ncond_off:"+cond_off());
		sb.append("\nop0_type:"+op0_type());
		sb.append("\nop0_len:"+op0_len());
		sb.append("\nop0_off:"+op0_off());
		sb.append("\nop1_type:"+op1_type());
		sb.append("\nop1_len:"+op1_len());
		sb.append("\nop1_off:"+op1_off());
		sb.append("\nop_code:"+op_code());
		sb.append("\nres_len:"+res_len());
		sb.append("\nres_off:"+res_off());
		sb.append("\npt_step:"+pt_step()+"\n}");
		return sb.toString();
	}

}
