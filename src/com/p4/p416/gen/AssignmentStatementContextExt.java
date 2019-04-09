package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import com.p4.cgen.utils.CGenUtils;
import com.p4.drmt.alu.ByteAluOperation;
import com.p4.drmt.alu.ConditionalConfigGenerator;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IField;
import com.p4.p416.iface.IAssignmentStatement;
import com.p4.p416.iface.IBaseType;
import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.ILValue;
import com.p4.p416.iface.IPrefixedNameLValue;
import com.p4.p416.iface.IRangeIndex;
import com.p4.p416.iface.IRangeIndexLvalue;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.z3.*;
import com.p4.drmt.alu.ConcreteAluInstruction;
import com.p4.p416.applications.AluInstruction;
import com.p4.p416.applications.AluInstruction.OpCode;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.applications.SsaForm;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.TargetSymbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.P416Parser.AssignmentStatementContext;
import com.p4.p416.gen.P416Parser.PrefixedNameLvalueContext;
import com.p4.p416.gen.P416Parser.PrefixedNonTypeNameLvalueContext;
import com.p4.packetgen.Z3Solver;
import com.p4.tools.graph.structs.TupleStatementSimplify;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

import lombok.Getter;

public class AssignmentStatementContextExt extends AbstractBaseExt implements IAssignmentStatement {

	private static final Logger L = LoggerFactory.getLogger(AssignmentStatementContextExt.class);
	
	// SSA START
	boolean isDel=false;

	public boolean getisDel(){
		return isDel;
	}
	public void setDel(boolean x){
		isDel=x;
	}
	//SSA END

	public AssignmentStatementContextExt(AssignmentStatementContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  AssignmentStatementContext getContext(){
		return (AssignmentStatementContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AssignmentStatementContext getContext(String str){
		return (AssignmentStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).assignmentStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof AssignmentStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ AssignmentStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Getter
	protected int instId;
	public void setIds(AtomicInteger controlId, AtomicInteger tableId, AtomicInteger actionId, AtomicInteger instId){
		this.instId = instId.getAndIncrement();
		L.debug(getFormattedText()+":"+this.instId);
	}


	@Override
	public void populateALUInstructions(List<AluInstruction> instructions)
	{
		instructions.addAll(getInstructions());

		/*
		 * hdr.ethernet.srcAddr=smac; 
		 * -> //[108:106] = Scr[2:0] 
		 * 	->  HDR+ Offset(ethernet) + Offset(srcAddr) = SCRATCH + Offset(smac)
		 *  OP0_type = get_type(HDR)
		 *  OP0_len = BitLength(hdr.ethernet.srcAddr)
		 *  OP0_Offset = HDR+ Offset(ethernet) + Offset(srcAddr)
		 */
	}

	public void evaluateAction(BitSet packetByteVector, BitSet packetBitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		AssignmentStatementContext ctx = getContext();
		if(getExtendedContext(ctx.lvalue()).getContext() instanceof PrefixedNonTypeNameLvalueContext || getExtendedContext(ctx.lvalue()).getContext() instanceof PrefixedNameLvalueContext) {
			ExpressionContextExt expressionContext = (ExpressionContextExt) getExtendedContext(ctx.expression());
			Long expressionValue = expressionContext.eval(packetByteVector, packetBitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
			String lhsVar = getExtendedContext(ctx.lvalue()).getFormattedText();
			Symbol lhsVarSymbol = resolve(lhsVar);

			if(lhsVarSymbol != null) {
				int offset = headerFieldsOffsetsAndSizes.get(lhsVar).first();
				int size = headerFieldsOffsetsAndSizes.get(lhsVar).second();
				BitSet bs = Utils.longToBitset(expressionValue, size);
				updatePacketVector(packetByteVector, bs, offset, size);
			}
			else {
				L.info("unable to resolve symbol");
				if(randomActionParameterValues.containsKey(lhsVar))
					randomActionParameterValues.put(lhsVar, expressionValue);
				else
					values.put(lhsVar, expressionValue);
			}
		}
		else {
			throw new RuntimeException("BAILOUT!!! Can handle only lvalue type prefixedNonTypeNameLvalue or prefixedNameLvalue");
		}

	}



	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		AssignmentStatementContext ctx = getContext();
		TupleStatementSimplify quadrupleMinimize = ((ExpressionContextExt)getExtendedContext(ctx.expression())).simplify();
		String simpleAssignmentString = getExtendedContext(ctx.lvalue()).getFormattedText() + ctx.ASSIGN().getText() + quadrupleMinimize.getSimpleExpressionText()+";";
		for(Pair<VariableDeclarationContextExt, AssignmentStatementContextExt> p:quadrupleMinimize.getTempAssigns()){
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(p.first().getFormattedText()));
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(p.second().getFormattedText()));
		}
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(simpleAssignmentString));
	}

	@Override
	public void assignPredicate(Stack<ExpressionContextExt> stack){
		StringBuilder sb = new StringBuilder();
		sb.append(getFormattedText());
		if(!stack.isEmpty()){
			this.predicateSymbol = stack.peek().getFormattedText();
			sb.append("//PREDICATE = "+stack.peek().getFormattedText());
		}else{
			this.predicateSymbol = null;
		}
		L.info(sb.toString());
	}

	@Override
	public void printInstruction(){
		L.debug("*************************");
		ExpressionContextExt expressionContextExt = (ExpressionContextExt) getExtendedContext(getContext().expression());
		if(expressionContextExt.isBitIns()){
			L.info("Bit ins");
		}
		L.info("Instruction\n" + getFormattedText());
		String LHS = getExtendedContext(getContext().lvalue()).getFormattedText();
		TargetSymbol ts = (TargetSymbol) resolve(LHS);
		int offset = Integer.MIN_VALUE;
		if(ts.getMemoryType().equals(MemoryType.TypePktBit)){
			offset = getAlignedByteOffset(LHS);
		}else{
			offset = getAlignedByteOffset(LHS);
		}
		L.info("LHS = "+LHS +"\tOffset = "+offset);
		String ps = getPredicateSymbol();
		if(ps!=null){
			//TargetSymbol pts = (TargetSymbol) resolve(ps);
			ps = ps.replaceAll("\\(", "");
			ps = ps.replaceAll("\\)", "");
			L.info("Predicate = "+ ps +"\tPredicate Offset = " + getAlignedByteOffset(ps));
		}
		L.debug("*************************");
	}

	public String InsSummary(){
        if (getContext() != null) {
            ExpressionContextExt expressionContextExt = (ExpressionContextExt) getExtendedContext(getContext().expression());
            String LHS = getExtendedContext(getContext().lvalue()).getFormattedText();
            StringBuilder sb = new StringBuilder();
            TargetSymbol ts = (TargetSymbol) resolve(LHS);
            //int lhsOffset = getByteOffset(LHS);
            if (expressionContextExt.isBitIns()) {
                assert (ts.getMemoryType().equals(MemoryType.TypePktBit));
            }
            return sb.toString();
        }
        return new String();
    }


	public int cond_en() {
		if(isConditional()) return 0;
		if (getPredicateSymbol() != null)
			return 1;
		else
			return 0;
	}


	public int cond_off() {
		if(isConditional()) return 0;
		String predicateSymbol = getPredicateSymbol();
		if (predicateSymbol != null)
			return getAlignedByteOffset(predicateSymbol);
		return 0;
	}



	private int opType(ExpressionNode expression)
	{
		if ( expression.isNumber()){
			return MemoryType.TypeCfgByte.type/2;
		}
		else if ( expression.isTerminal()){
			Symbol symbol = resolve(expression.TerminalValue());
			return ((AbstractBaseExt)symbol).getMemoryType().type/2;
		}
		else if ( expression.isBinaryExpression()){
			ExpressionNode  leftExpression = expression.getLeft();
			return opType(leftExpression);
		}
		throw new RuntimeException("Can't calculate op_type for :"+getFormattedText());
	}


	public int op0_type() {
		if(isConditional()) return 0;
		ExpressionNode  RHS = (ExpressionNode)getExtendedContext(getContext().expression());
		return opType(RHS);
	}


	private int opLen(ExpressionNode expression)
	{
		if ( expression.isNumber()){
			return ((AbstractBaseExt)expression).getSizeInBytes();
		}
		else if ( expression.isTerminal()){
			String symbolName = ((AbstractBaseExt)expression).getSymbolName();
			int size = getSizeInBytes(symbolName);
			return size;
		}
		else if ( expression.isBinaryExpression()){
			ExpressionNode  leftExpression = expression.getLeft();
			return opLen(leftExpression);
		}
		throw new RuntimeException("Can't calculate oplen for :"+getFormattedText());
	}


	public int op0_len1() {
		if(isConditional()) return 0;
        if (getContext() != null) {
            ExpressionNode RHS = (ExpressionNode) getExtendedContext(getContext().expression());
            return opLen(RHS);
        }
        return 0;
    }

	private int opOff(ExpressionNode expression)
	{
		if ( expression.isNumber()){
			return getAlignedByteOffset(expression.TerminalValue());
		}
		else if ( expression.isTerminal()){
			String symbolName = ((AbstractBaseExt)expression).getSymbolName();
			Integer x = getAlignedByteOffset(symbolName);
			return x;
		}
		else if ( expression.isBinaryExpression()){
			ExpressionNode  leftExpression = expression.getLeft();
			return opOff(leftExpression);
		}
		throw new RuntimeException("Can't calculate offset for :"+getFormattedText());
	}

	public int op0_off() {
		if(isConditional()) return 0;
		ExpressionNode  RHS = (ExpressionNode)getExtendedContext(getContext().expression());
		return opOff(RHS);
	}


	public int op1_type() {
		if(isConditional()) return 0;
		ExpressionNode  RHS = (ExpressionNode)getExtendedContext(getContext().expression());
		if ( RHS.isBinaryExpression()){
			ExpressionNode  rightExpression = RHS.getRight();
			return opType(rightExpression);
		}
		if(isSimpleAssignment())
			return 0;
		throw new RuntimeException("Can't calculate op1_off for :"+getFormattedText());
	}


    public int op1_len1() {
        if (isConditional()) return 0;
        if (getContext() != null) {
            ExpressionNode RHS = (ExpressionNode) getExtendedContext(getContext().expression());
            if (RHS.isBinaryExpression()) {
                ExpressionNode rightExpression = RHS.getRight();
                return opLen(rightExpression);
            }
            if (isSimpleAssignment())
                return 0;
        } else return 0;

        throw new RuntimeException("Can't calculate op1_len for :" + getFormattedText());
    }


	public int op1_off() {
		if(isConditional()) return 0;
		ExpressionNode  RHS = (ExpressionNode)getExtendedContext(getContext().expression());
		if ( RHS.isBinaryExpression()){
			ExpressionNode  rightExpression = RHS.getRight();
			return opOff(rightExpression);
		}
		if(isSimpleAssignment())
			return 0;

		throw new RuntimeException("Can't calculate op1_off for :"+getFormattedText());
	}

	private boolean isSimpleAssignment(){
		return op_code()==OpCode.COPY.type;
	}

	public int op_code() {
		if(isConditional()) return 0;
		ExpressionNode  RHS = (ExpressionNode)getExtendedContext(getContext().expression());
		if ( RHS.isNumber()){
			return OpCode.COPY.type;
		}
		else if ( RHS.isTerminal()){
			return OpCode.COPY.type;
		}
		else if ( RHS.isBinaryExpression()){
			String operator = RHS.getOperator();
			switch(operator){
			case "+" :
				return OpCode.ADD.type;
			case "-" :
				return OpCode.SUB.type;
			case "^" :
				return OpCode.XOR.type;
			case "&" :
				return OpCode.ADD.type;
			case "|" :
				return OpCode.OR.type;
			case "<<" :
				return OpCode.SHL.type;
			case "==" :
				return OpCode.EQ.type;
			case ">" :
				return OpCode.GT.type;
			case "<" :
				return OpCode.LT.type;
			case "!=":
				return OpCode.NE.type;
			default:
				throw new UnsupportedOperationException();
				//return OpCode.NOOP.type;
			}
		}
		throw new RuntimeException("Can't calculate opcode for :"+getFormattedText());
	}

    public int res_len1() {
        if (isConditional()) return 0;
        if (getContext() == null) return 0;
        LvalueContextExt lhs = (LvalueContextExt) getExtendedContext(getContext().lvalue());
        Symbol lhsSymbol = resolve(lhs.getSymbolName());
        Type lhsSymbolType = lhsSymbol.getType();
        if (lhsSymbolType.getSizeInBits() == 1) return 0;
        int size = lhsSymbolType.getSizeInBytes();
        return size;
    }

	public int res_off() {
		if(isConditional()) return 0;
		LvalueContextExt lhs =  (LvalueContextExt)getExtendedContext(getContext().lvalue());
		return getAlignedByteOffset(lhs.getSymbolName());
	}

	public int pt_step() {
		return 0;
	}


	public String summary(){
		StringBuffer sb = new StringBuffer();
		sb.append(getFormattedText()+"\n");
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

    public boolean isConditional() {
        //bit x = a&&b
        if (getContext() != null) {
            ExpressionContextExt RHS = (ExpressionContextExt) getExtendedContext(getContext().expression());
            return RHS.isBitIns();
        } else {
            return false;
        }
    }

	private int wordize(int input){
		if(input>=3)
			return 4;
		else
			return input;
	}

	public List<AluInstruction> getInstructions() {
		List<AluInstruction> ret = Utils.newArrList();
		if(!isConditional()){
			if(op1_len1()>4 || op0_len1()>4 || res_len1()>4){
				if(isSimpleAssignment() && res_len1() == op0_len1()){
					Integer[] parts = Utils.calcParts(op0_len1(), 4);
					int runningOffset = 0;
					for(int p:parts){
						ConcreteAluInstruction ci = new ConcreteAluInstruction(this, cond_en(), cond_off(), 
								op0_type(), wordize(p), op0_off()+runningOffset,
								op1_type(), op1_len(), op1_off(), 
								op_code(), wordize(p), res_off()+runningOffset, pt_step(), isConditional());
						ret.add(ci);
						runningOffset+=p;
					}
					return ret;
				}else{
					L.warn("Arithmetic Operation on > 4 bytes operands: Operation will only be applied on last 4 bytes");
				}
			}

			ConcreteAluInstruction ci = new ConcreteAluInstruction(this, cond_en(), cond_off(), op0_type(), op0_len(), op0_off(), 
					op1_type(), op1_len(), op1_off(), 
					op_code(), res_len(), res_off(), pt_step(), isConditional());

			ret.add(ci);
		}else{
			ConcreteAluInstruction ci = new ConcreteAluInstruction(this, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
			ret.add(ci);
		}
		return ret;
	}

	public int res_len() {
		return wordize(res_len1());
	}

	public int op1_len() {
		return wordize(op1_len1());
	}

	public int op0_len() {
		return wordize(op0_len1());
	}


	//SSA START
	@Override
	public void setVersions(SsaForm obj ){
		AbstractBaseExt oldAssignCtx = obj.getAssignCtx();
		obj.setAssignCtx(getExtendedContext(getContext()));
		super.setVersions(obj);
		obj.setAssignCtx(oldAssignCtx);
	}

	@Override
	public void getSSAFormattedText(Params params){
		if(getContext()==null) return;
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText="";
		alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr="";
		if(this.getisDel() == true){
			newStr="/*DEL*/";
		}
		super.getSSAFormattedText(params);
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	public Expr gatherSymbolsAndConstraints(Z3Solver solver, BitSet bs, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		AssignmentStatementContext ctx = getContext();
		if(getExtendedContext(ctx.lvalue()).getContext() instanceof PrefixedNonTypeNameLvalueContext || getExtendedContext(ctx.lvalue()).getContext() instanceof PrefixedNameLvalueContext) {
			ExpressionContextExt expressionContext = (ExpressionContextExt) getExtendedContext(ctx.expression());
			String lhsVar = getExtendedContext(ctx.lvalue()).getFormattedText();
			solver.addSymbol(lhsVar);
			return solver.getCtx().mkEq(solver.getLatestContext(lhsVar), expressionContext.getExpr(expressionContext.getExpressionFromString(expressionContext.getFormattedText()), solver, bs, headerValidLocations, headerFieldsOffsetsAndSizes));
		}
		else {
			throw new RuntimeException("BAILOUT!!! Can handle only lvalue type prefixedNonTypeNameLvalue or prefixedNameLvalue");
		}
	}

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		ILValue lValue = (ILValue) getExtendedContext(getContext().lvalue());
		IExpression rightHandExpression = (IExpression) getExtendedContext(getContext().expression());
		String transformedString = "";
		boolean needsTransformation = true;
		boolean doTransform = false;
		if (lValue instanceof IPrefixedNameLValue && !(rightHandExpression instanceof IRangeIndex)) {
			int sizeInBits = lValue.getSizeInBits();
			int byteSize = (sizeInBits + 7) / 8;
			if (byteSize > 4) {
				String lValueString = lValue.getFormattedText();
				String rValue = rightHandExpression.getFormattedText();
				doTransform = true;
				if (rightHandExpression instanceof IntegerContextExt && (rValue.startsWith("0x") || rValue.startsWith("0X"))) {
					uid++;
					String tmpVar = lValueString.replaceAll("\\.", "_") + "_buffer_" + uid;
					String transformedRValue = "hexCharToHex(\"" + rValue + "\", " + sizeInBits + ")";

					String tmpVarInit = "\t\t\tUINT8_T_STAR " + tmpVar + " = " + transformedRValue + ";";
					String transformedAssignmentStatement = "\t\t\tmemcpy(" + lValueString + ", " + tmpVar + ", " + byteSize + ");";
					String freeTmpVar = "\t\t\tfree(" + tmpVar + ");";

					transformedString = CGenUtils.joinStrings("\n", "{", tmpVarInit, transformedAssignmentStatement, freeTmpVar, "\t\t}");

				} else {
					Symbol symbol = resolve(lValueString);
					if (symbol.getType() instanceof IBaseType) {
						transformedString = "memcpy(" + lValueString + ", " + rValue + ", " + byteSize + ");";
					} else {
						needsTransformation = false;
					}
				}
			} else {
				needsTransformation = false;
			}
		} else {
			doTransform = true;
			transformedString = transformBitSliceStatements(lValue, rightHandExpression);
			if (transformedString == null || "".equalsIgnoreCase(transformedString)) {
				doTransform = false;
			}
		}

		if (needsTransformation) {
			if (doTransform) {
				ParserRuleContext transformedContext = getParent().getContext(transformedString);
				getParent().setContext(transformedContext);
			} else {
				L.error("Unhandled assignment expression :" + getFormattedText());
			}
		}
	}

	private String transformBitSliceStatements(ILValue lValue, IExpression rightHandExpression){
		String transformedString = "";
		if (lValue instanceof IRangeIndexLvalue || rightHandExpression instanceof IRangeIndex) {
			if (lValue instanceof IRangeIndexLvalue && rightHandExpression instanceof IRangeIndex) {
				//something like hdr.ethernet.dstAddr[12:8] = hdr.ipv4.dstAddr[22:18];
				ILValue prefixLValue = ((IRangeIndexLvalue) lValue).getLValue();
				if (prefixLValue instanceof IPrefixedNameLValue) {
					int dstSizeInBits = prefixLValue.getSizeInBits();
					int dstByteSize = (dstSizeInBits + 7) / 8;

					IExpression rhsExpression = ((IRangeIndex) rightHandExpression).getExpression();
					int srcSizeInBits = getSizeInBits(rhsExpression.getNameString());
					int srcByteSize = (srcSizeInBits + 7) / 8;

					String dst = dstByteSize <= 4 ? "BR_UINT8_T_STAR_BR&(" + prefixLValue.getFormattedText() + ")" : prefixLValue.getFormattedText();
					String src = srcByteSize <= 4 ? "BR_UINT8_T_STAR_BR&(" + rhsExpression.getNameString() + ")" : rhsExpression.getNameString();
					String dstFrom = ((IRangeIndexLvalue) lValue).getFrom().getNameString();
					String srcFrom = ((IRangeIndex) rightHandExpression).getFrom().getNameString();
					String srcTo = ((IRangeIndex) rightHandExpression).getTo().getNameString();

					transformedString = "EMIT_BITS_SLICED(" + dst + ", " + src + ", " + dstFrom + ", " + srcFrom + ", " + srcTo + ");";
				}

			} else if (lValue instanceof IRangeIndexLvalue && rightHandExpression instanceof IntegerContextExt) {
				//something like hdr.ethernet.dstAddr[16:8] = 1w0xAC;
				ILValue prefixLValue = ((IRangeIndexLvalue) lValue).getLValue();
				String lValueString = prefixLValue.getFormattedText();
				String rValue = rightHandExpression.getFormattedText();
				if (prefixLValue instanceof IPrefixedNameLValue) {
					int dstSizeInBits = prefixLValue.getSizeInBits();
					int dstByteSize = (dstSizeInBits + 7) / 8;
					if ((rValue.startsWith("0x") || rValue.startsWith("0X"))) {
						uid++;
						String tmpVar = lValueString.replaceAll("\\.", "_") + "_buffer_" + uid;

						String dst = dstByteSize <= 4 ? "BR_UINT8_T_STAR_BR&(" + prefixLValue.getFormattedText() + ")" : prefixLValue.getFormattedText();
						String src = tmpVar;
						String dstFrom = ((IRangeIndexLvalue) lValue).getFrom().getNameString();
						String dstTo = ((IRangeIndexLvalue) lValue).getTo().getNameString();
						String srcFrom = "0";
						String srcTo = dstTo + "-" + dstFrom;

						String transformedRValue = "hexCharToHex(\"" + rValue + "\", " + srcTo + ")";
						String tmpVarInit = "\t\t\tUINT8_T_STAR " + tmpVar + " = " + transformedRValue + ";";
						String transformedAssignmentStatement = "\t\t\tEMIT_BITS_SLICED(" + dst + ", " + src + ", " + dstFrom + ", " + srcFrom + ", " + srcTo + ");";
						String freeTmpVar = "\t\t\tfree(" + tmpVar + ");";

						transformedString = CGenUtils.joinStrings("\n", "{", tmpVarInit, transformedAssignmentStatement, freeTmpVar, "\t\t}");
					}
				}
			} else if (lValue instanceof IPrefixedNameLValue && rightHandExpression instanceof IRangeIndex) {
				//a.b.c = x.y.z[47:0];
				int dstSizeInBits = lValue.getSizeInBits();
				int dstByteSize = (dstSizeInBits + 7) / 8;

				IExpression rhsExpression = ((IRangeIndex) rightHandExpression).getExpression();
				int srcSizeInBits = getSizeInBits(rhsExpression.getNameString());
				int srcByteSize = (srcSizeInBits + 7) / 8;

				String dst = dstByteSize <= 4 ? "BR_UINT8_T_STAR_BR&(" + lValue.getFormattedText() + ")" : lValue.getFormattedText();
				String src = srcByteSize <= 4 ? "BR_UINT8_T_STAR_BR&(" + rhsExpression.getNameString() + ")" : rhsExpression.getNameString();
				String dstFrom = "0";
				String srcFrom = ((IRangeIndex) rightHandExpression).getFrom().getNameString();
				String srcTo = ((IRangeIndex) rightHandExpression).getTo().getNameString();

				transformedString = "EMIT_BITS_SLICED(" + dst + ", " + src + ", " + dstFrom + ", " + srcFrom + ", " + srcTo + ");";
			}
		}
		return transformedString;
	}

	public List<IALUOperation> allALUOperations(){
		L.info(getFormattedText());
		List<IALUOperation> ret = new ArrayList<>();
		if(!isConditional()){//Byte ALU
			ExpressionContextExt expressionContextExt = (ExpressionContextExt) getExtendedContext(getContext().expression());
			if(!expressionContextExt.isSimpleExpression()){
				throw new RuntimeException("Can't handle complex Byte Operations");
			}
			IField lhs = AbstractBaseExt.getFieldOf(this, getExtendedContext(getContext().lvalue()).getFormattedText());

			IField predicate = null;
			if(cond_en()==1){
				String pred = getPredicateSymbol();
				pred = pred.replaceAll("\\(", "");
				pred = pred.replaceAll("\\)", "");
				predicate = AbstractBaseExt.getFieldOf(this, pred);
			}
			OpCode opCode = OpCode.getOpCodeof(op_code());
			if(op1_len1()>4 || op0_len1()>4 || res_len1()>4){//Multi Word Operation
				if(isSimpleAssignment() && res_len1() == op0_len1()){//Assignment
					IField op0 = AbstractBaseExt.getFieldOf(this,((ExpressionContextExt)getExtendedContext(getContext().expression())).TerminalValue());
					Integer[] parts = Utils.calcParts(op0_len1(), 4);
					int runningOffset = 0;
					for(int p:parts){
						IField l = lhs.addOffset(runningOffset, p*8);
						IField r = op0.addOffset(runningOffset, p*8);
						ret.add(new ByteAluOperation(l, opCode, r, null,predicate, DRMTConstants.actionDelay));
						runningOffset+=p;
					}
				}else{// Not an assignment but a complex operation so do it for Lower word only
					L.warn("Complex ALU Operation being used for >32 bit quantity. Only lower 4 byte will be used");
					IField op0 = AbstractBaseExt.getFieldOf(this, ((ExpressionContextExt)getExtendedContext(getContext().expression())).getLeft().TerminalValue(), op0_len()*8);
					IField op1 = AbstractBaseExt.getFieldOf(this, ((ExpressionContextExt)getExtendedContext(getContext().expression())).getRight().TerminalValue(), op1_len()*8);
					ret.add(new ByteAluOperation(lhs, opCode, op0, op1, predicate,DRMTConstants.actionDelay));
				}
			}else{//Normal word operation
				if(isSimpleAssignment()){
					IField op0 = AbstractBaseExt.getFieldOf(this, ((ExpressionContextExt)getExtendedContext(getContext().expression())).TerminalValue(), op0_len()*8);
					ret.add(new ByteAluOperation(lhs, opCode, op0, null, predicate,DRMTConstants.actionDelay));
				}else{
					IField op0 = AbstractBaseExt.getFieldOf(this, ((ExpressionContextExt)getExtendedContext(getContext().expression())).getLeft().TerminalValue(), op0_len()*8);
					IField op1 = AbstractBaseExt.getFieldOf(this, ((ExpressionContextExt)getExtendedContext(getContext().expression())).getRight().TerminalValue(), op1_len()*8);
					ret.add(new ByteAluOperation(lhs, opCode, op0, op1, predicate,DRMTConstants.actionDelay));
				}
			}
		}else{//Bit ALU
			ret.add(ConditionalConfigGenerator.getBitALUOperation(this));
		}
		return ret;
	}
	
	@Override
	public boolean isTypeCompatible(Type type1,Type type2){
		if (type1==null || type2==null){
			return false;
		}
		
		if (type1.isEquivalenceCompatible(type2)){
			return true;
		}
		
		if (type1!=null && type2!=null){
			String typeName1 = type1.getTypeName();
			String typeName2 = type2.getTypeName();
			int typeSize1 = type1.getTypeSize();
			int typeSize2 = type2.getTypeSize();
			if (typeName1.equals(typeName2) && (typeSize1 == typeSize2)){
				return true;
			}
		}
		return false;
	}
}
