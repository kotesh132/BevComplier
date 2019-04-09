package com.p4.drmt.alu;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.schd.NewScheduler;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IOperator;
import com.p4.p416.applications.Target;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.p416.gen.IntegerContextExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionalConfigGenerator{

	private static final Logger L = LoggerFactory.getLogger(ConditionalConfigGenerator.class);
	
	public static ConditionalComplexRow getConfigRow (AssignmentStatementContextExt ctx){
		ExpressionContextExt expressionContextExt = (ExpressionContextExt) ctx.getExtendedContext(ctx.getContext().expression());
		if(expressionContextExt.isBitIns()){
			ConditionalComplexRow c = ConstructConditionalComplexRow(ctx); 
			L.debug(c.summary());
			return c;
		}else{
			return ConditionalComplexRow.DEFAULT_NOP;
		}
	}

	private static ConditionalComplexRow ConstructConditionalComplexRow(AssignmentStatementContextExt ctx) {
		ExpressionContextExt expressionContextExt = (ExpressionContextExt) ctx.getExtendedContext(ctx.getContext().expression());
		String LHS = ctx.getExtendedContext(ctx.getContext().lvalue()).getFormattedText();
		ConditionalComplexRow c = new ConditionalComplexRow();
		c.getAlu_inst().pad();
		c.getBr_vld().addItem(DumbConditional.getBr_vld());
		c.getBr_data().addItem(DumbConditional.getBr_data());
		c.getBr_mask().addItem(DumbConditional.getBr_mask());
		int br_dest = ctx.getAlignedByteOffset(LHS);
		L.debug("Cond Res:");
		L.debug(Integer.toString(br_dest));
		c.getBr_dest().addItem(br_dest);
		if(expressionContextExt.isTerminal()){
			//This is terminal just resolve it
			c.getPrim_vld().addItem(1);
			c.getPrim_neg().addItem(0);
			c.getPrim_offset().addItem(DumbConditional.GetOffsetOfSymbol(ctx, expressionContextExt.TerminalValue()));
			c.getCond_adr().addItem(15);
			//REST Not needed
		}else if(expressionContextExt.isSimpleExpression()){
			if(expressionContextExt.isUnaryExpression()){
				assert(expressionContextExt.getOperator().equals("!"));
				c.getPrim_vld().addItem(1);
				c.getPrim_neg().addItem(1);
				c.getPrim_offset().addItem(DumbConditional.GetOffsetOfSymbol(ctx, expressionContextExt.getLeft().TerminalValue()));
				c.getCond_adr().addItem(7);
				c.getAlu_inst().addItem(6, 7);//Take left child of 7
			}else{
				int operator = expressionContextExt.getOperator().equals("&&")?3:2; //AND or OR
				//c.getAlu_inst().addItem(operator);
				c.getAlu_inst().addItem(operator,7);//getItems().add(7, new FW(operator, c.getAlu_inst().getWidth()));//Take left child of 7
				c.getCond_adr().addItem(7);//This is the Cut Point
				//Left child 
				c.getPrim_vld().addItem(1);
				c.getPrim_neg().addItem(0);
				c.getPrim_offset().addItem(DumbConditional.GetOffsetOfSymbol(ctx, expressionContextExt.getLeft().TerminalValue()));
				
				//Right Child
				c.getPrim_vld().addItem(1);
				c.getPrim_neg().addItem(0);
				c.getPrim_offset().addItem(DumbConditional.GetOffsetOfSymbol(ctx, expressionContextExt.getRight().TerminalValue()));
				
			}
		}else{
			//TODO Assumption all the operands are Anded now.
			//TODO TOTAL HACK. Fix this
			String[] parts = expressionContextExt.getFormattedText().split("&&");
			assert(parts.length == 3);
			c.getPrim_vld().addItem(1);
			c.getPrim_neg().addItem(0);
			int offset = DumbConditional.GetOffsetOfSymbol(ctx, parts[0]);
			c.getPrim_offset().addItem(offset);
			c.getPrim_vld().addItem(1);
			c.getPrim_neg().addItem(0);
			offset = DumbConditional.GetOffsetOfSymbol(ctx, parts[1]);
			c.getPrim_offset().addItem(offset);
			c.getPrim_vld().addItem(1);
			c.getPrim_neg().addItem(0);
			offset = DumbConditional.GetOffsetOfSymbol(ctx, parts[2]);
			c.getPrim_offset().addItem(offset);
			c.getCond_adr().addItem(3);//Cut point
			c.getAlu_inst().addItem(3, 7);//7 is anding 
			c.getAlu_inst().addItem(6, 8);//8 is left child
			c.getAlu_inst().addItem(3, 3);//3 is anding
		}
		c.padAll();
		return c;
	}

	public static void generateConditionalConfig(BitALUOperation bitALUOperation, int address, ConditionalComplexRow c){
		L.info("Row: " + address+ bitALUOperation.summary());
		c.getBr_vld().setItem(DumbConditional.getBr_vld(), address, 0);
		c.getBr_data().setItem(DumbConditional.getBr_data(), address, 0);
		c.getBr_mask().setItem(DumbConditional.getBr_mask(), address, 0);
		c.getBr_dest().setItem(bitALUOperation.getLhs().getOffset(), address, 0);
		if(bitALUOperation.getOpCode().equals(BitALUOpCode.DIRECT)){
			c.getPrim_vld().setItem(1, address, 0);
			c.getPrim_neg().setItem(0, address, 0);
			c.getPrim_offset().setItem(bitALUOperation.getOp0().getOffset(), address, 0);
			c.getCond_adr().setItem(15, address, 0);
			//REST Not needed
		}else if(bitALUOperation.getOpCode().equals(BitALUOpCode.ZER0)){
			c.getPrim_vld().setItem(1, address, 0);
			c.getPrim_neg().setItem(0, address, 0);
			c.getPrim_offset().setItem(bitALUOperation.getOp0().getOffset(), address, 0);
			c.getCond_adr().setItem(7, address, 0);
			c.getAlu_inst().setItem(0, address, 7);//Force LHS to 0
		}else if(bitALUOperation.getOpCode().equals(BitALUOpCode.ONE)){
			c.getPrim_vld().setItem(1, address, 0);
			c.getPrim_neg().setItem(0, address, 0);
			c.getPrim_offset().setItem(bitALUOperation.getOp0().getOffset(), address, 0);
			c.getCond_adr().setItem(7, address, 0);
			c.getAlu_inst().setItem(1, address, 7);//Force RHS to 1
		}else if(bitALUOperation.getAllOperands().size()==1){//UNARY
			assert(bitALUOperation.getOpCode().equals(BitALUOpCode.NOT));
			c.getPrim_vld().setItem(1, address, 0);
			c.getPrim_neg().setItem(1, address, 0);
			c.getPrim_offset().setItem(bitALUOperation.getOp0().getOffset(), address, 0);
			c.getCond_adr().setItem(7, address, 0);
			c.getAlu_inst().setItem(6, address, 7);//Take left child of 7
		}else if(bitALUOperation.getAllOperands().size()==2) {//Binary
			BitALUOpCode opcode = (BitALUOpCode) bitALUOperation.getOpCode();
			//c.getAlu_inst().addItem(operator);
			c.getAlu_inst().setItem(opcode.type, address,7);//getItems().add(7, new FW(operator, c.getAlu_inst().getWidth()));//Take left child of 7
			c.getCond_adr().setItem(7, address, 0);//This is the Cut Point
			//Left child
			c.getPrim_vld().setItem(1, address,0);
			c.getPrim_neg().setItem(0, address,0);
			c.getPrim_offset().setItem(bitALUOperation.getOp0().getOffset(), address, 0);
			//Right Child
			c.getPrim_vld().setItem(1, address, 1);
			c.getPrim_neg().setItem(0, address, 1);
			c.getPrim_offset().setItem(bitALUOperation.getOp1().getOffset(), address, 1);
		}else if(bitALUOperation.getAllOperands().size()==3){
			//OP0
			int index = 0;
			c.getPrim_vld().setItem(1, address, index);
			c.getPrim_neg().setItem(0, address, index);
			c.getPrim_offset().setItem(bitALUOperation.getAllOperands().get(0).getOffset(), address, index);
			index++;
			//OP1
			c.getPrim_vld().setItem(1, address, index);
			c.getPrim_neg().setItem(0, address, index);
			c.getPrim_offset().setItem(bitALUOperation.getAllOperands().get(1).getOffset(), address, index);
			index++;
			c.getPrim_vld().setItem(1, address, index);
			c.getPrim_neg().setItem(0, address, index);
			c.getPrim_offset().setItem(bitALUOperation.getAllOperands().get(2).getOffset(), address, index);

			c.getCond_adr().setItem(3, address, 0);//Cut point
			c.getAlu_inst().setItem(3, address, 7);//7 is anding
			c.getAlu_inst().setItem(6, address, 8);//8 is left child
			c.getAlu_inst().setItem(3, address, 3);//3 is anding
		}else{
			throw new UnsupportedOperationException();
		}
	}

	public enum BitALUOpCode implements IOperator{
		OR(2, "||"),
		AND(3, "&&"),
		NOT(8, "!"),
		DIRECT(9, ""),
		ZER0(0, "0"),
		ONE(1, "1");
		private final int type;
		private final String val;
		BitALUOpCode(int type, String val){
			this.type = type;this.val = val;
		}
		public static BitALUOpCode getOpCodeOf(int v){
			for(BitALUOpCode o: BitALUOpCode.values()){
				if(v == o.type)
					return o;
			}
			throw new RuntimeException("No Opcode found matching value "+v);
		}
		public String summary(){
			return this.val;
		}
	}

	public static BitALUOperation getBitALUOperation(AssignmentStatementContextExt ctx){
		ExpressionContextExt expressionContextExt = (ExpressionContextExt) ctx.getExtendedContext(ctx.getContext().expression());
		String LHS = ctx.getExtendedContext(ctx.getContext().lvalue()).getFormattedText();
		IField lhs = AbstractBaseExt.getFieldOf(ctx,LHS, 1, Target.MemoryType.TypePktBit);
		if(expressionContextExt.isTerminal()){
			int numOperands = 1;
			IField op0 = AbstractBaseExt.getFieldOf(ctx, expressionContextExt.TerminalValue(), 1, Target.MemoryType.TypePktBit);
			BitALUOpCode opcode;
			if(expressionContextExt instanceof IntegerContextExt){
				IntegerContextExt integerContextExt = (IntegerContextExt) expressionContextExt;
				opcode = integerContextExt.getFW().getValue()==0?BitALUOpCode.ZER0: BitALUOpCode.ONE;
			}else {
				opcode = BitALUOpCode.DIRECT;
			}
			BitALUOperation bitALUOperation = new BitALUOperation(numOperands, lhs, DRMTConstants.actionDelay, opcode, op0, null);
			bitALUOperation.getAllOperands().add(op0);
			return bitALUOperation;
		}else if(expressionContextExt.isSimpleExpression()){
			if(expressionContextExt.isUnaryExpression()){
				int numOperands = 1;
				IField op0 = AbstractBaseExt.getFieldOf(ctx, expressionContextExt.getLeft().TerminalValue(), 1, Target.MemoryType.TypePktBit);
				BitALUOpCode opcode = BitALUOpCode.NOT;
				BitALUOperation bitALUOperation = new BitALUOperation(numOperands, lhs,DRMTConstants.actionDelay, opcode, op0, null);
				bitALUOperation.getAllOperands().add(op0);
				return bitALUOperation;
			}else{
				int numOperands = 2;
				IField op0 = AbstractBaseExt.getFieldOf(ctx, expressionContextExt.getLeft().TerminalValue(), 1, Target.MemoryType.TypePktBit);
				IField op1 = AbstractBaseExt.getFieldOf(ctx, expressionContextExt.getRight().TerminalValue(), 1, Target.MemoryType.TypePktBit);
				BitALUOpCode opcode = expressionContextExt.getOperator().equals("&&")? BitALUOpCode.AND:BitALUOpCode.OR;
				BitALUOperation bitALUOperation = new BitALUOperation(numOperands, lhs,DRMTConstants.actionDelay, opcode, op0, op1);
				bitALUOperation.getAllOperands().add(op0);
				bitALUOperation.getAllOperands().add(op1);
				return bitALUOperation;
			}
		}else{
			String[] parts = expressionContextExt.getFormattedText().split("&&");
			int numOperands = parts.length;
			BitALUOperation bitALUOperation = new BitALUOperation(numOperands,lhs,DRMTConstants.actionDelay,BitALUOpCode.AND, null,null );
			for(String operand:parts){
				bitALUOperation.getAllOperands().add(AbstractBaseExt.getFieldOf(ctx, operand, 1, Target.MemoryType.TypePktBit));
			}
			return bitALUOperation;
		}
	}
}
