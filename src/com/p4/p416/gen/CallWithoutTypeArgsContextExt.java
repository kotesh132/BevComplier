package com.p4.p416.gen;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.applications.Symbol;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.MethodCallStatementContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IArgument;
import com.p4.p416.iface.ICallWithoutTypeArgs;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IHeaderStackType;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.ILValue;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.ITypeRef;



import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.p416.applications.SsaForm;
import  com.p4.p416.gen.P416Parser.CallWithoutTypeArgsContext;
import com.p4.utils.Utils.Pair;

public class CallWithoutTypeArgsContextExt extends MethodCallStatementContextExt implements ICallWithoutTypeArgs {

	public CallWithoutTypeArgsContextExt(CallWithoutTypeArgsContext ctx) {
		super(ctx);
	}

	@Override
	public  MethodCallStatementContext getContext(){
		return (MethodCallStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public CallWithoutTypeArgsContext getContext(String str){
		return (CallWithoutTypeArgsContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodCallStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof CallWithoutTypeArgsContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ CallWithoutTypeArgsContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//SSA START
	@Override
	public void setVersions(SsaForm obj ){}

	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText();
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	@Override
	public void transformToCCode(LinkedHashMap<String, String> map) {
		IIRNode controlNode = getParentIRNode();
		while (controlNode != null && !(controlNode instanceof IControl)) {
			controlNode = controlNode.getParentIRNode();
		}

		StringBuilder sb = new StringBuilder();

		AbstractBaseExt parent = getParent();
		boolean doTransform = false;
		if (controlNode != null && parent instanceof StatementContextExt) {
			IControl control = (IControl) controlNode;
			String actionCall = getFormattedText();
			ILValue lValue = getLValue();
			String lValueString = lValue.getFormattedText();

			if (actionCall.startsWith("mark_to_drop()")) {
				doTransform = false;
			} else if (actionCall.endsWith("setValid();")) {
				sb.append(actionCall.replace(".setValid()", ".isValid = 1"));
				doTransform = true;
			} else if (actionCall.endsWith(".setInvalid();")) {
				sb.append(actionCall.replace(".setInvalid()", ".isValid = 0"));
				doTransform = true;
			} else if ("verify_checksum".equals(lValueString)) {
				transformCheckSumMethods(map, controlNode, sb, "verify_checksum");
				doTransform = true;
			} else if ("update_checksum".equals(lValueString)) {
				transformCheckSumMethods(map, controlNode, sb, "update_checksum");
				doTransform = true;
			} else if (lValueString.endsWith(".push_front")) {
				doTransform = transformPushPopFrontOnHeaderStack(map, sb, lValueString, "push_front");
			} else if (actionCall.contains(".pop_front")) {
				doTransform = transformPushPopFrontOnHeaderStack(map, sb, lValueString, "pop_front");
			} else if (actionCall.contains("clone3")) {
				L.debug(actionCall + " would be implemented externally");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else if (actionCall.contains("hash(")) {
				L.debug(actionCall + " : hash yet to be implemented");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else if (actionCall.contains(".execute_meter(")) {
				L.debug(actionCall + " : executemeter yet to be implemented");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else if (actionCall.contains(".count(")) {
				L.debug(actionCall + " : count() yet to be implemented");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else if (actionCall.contains("random")) {
				L.debug(actionCall + " : random yet to be implemented");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else if (actionCall.contains(".read(")) {
				L.debug(actionCall + " : read yet to be implemented");
				//TODO temporary hack to go ahead
				sb.append("mark_to_drop();");
				doTransform = true;
			} else {
				try {
					Symbol symbol = controlNode.resolve(lValueString);
					if (symbol instanceof IActionDeclaration) {
						sb.append("\t").append(control.getNameString()).append("_").append(actionCall);
						doTransform = true;
					}
				} catch (SymbolNotDefinedException ignore) {
					L.warn("Symbol " + lValueString + " not resolved");
				}
			}
		}

		if (doTransform) {
			StatementContext statementContext = ((StatementContextExt) parent).getContext(sb.toString());
			((StatementContextExt) parent).setContext(statementContext);
		}
	}

	private boolean transformPushPopFrontOnHeaderStack(LinkedHashMap<String, String> map, StringBuilder sb, String lValueString, String pushOrPop) {
		if (!"push_front".equals(pushOrPop) && !"pop_front".equals(pushOrPop)) {
			L.error("Invalid pushOrPop argument passed : " + pushOrPop + ". Expected is either push_front or pop_front");
			return false;
		}

		String headerStackVar = lValueString.replaceAll("." + pushOrPop, "");

		boolean doTransform = false;
		Symbol headerStackVarSymbol = resolve(headerStackVar);
		if (headerStackVarSymbol instanceof IStructField) {
			ITypeRef headerStackTypeRef = ((IStructField) headerStackVarSymbol).getTypeRef();
			if (headerStackTypeRef.isHeaderStackType()) {
				IHeaderStackType headerStackType = headerStackTypeRef.getHeaderStackType();
				String headerStackTypeName = headerStackTypeRef.getPrefixedType();
				List<IArgument> arguments = getArguments();
				assert arguments.size() == 1 : pushOrPop + " has illegal number of arguments " + getFormattedText();
				int size = headerStackType.getHeaderStackSize();

				String count = arguments.get(0).getFormattedText();

				String headerStackVarUnderScore = headerStackVar.replaceAll("\\.", "_");
				sb.append(pushOrPop).append("_").append(headerStackVarUnderScore).append("(");
				sb.append(headerStackVar).append(", ");
				sb.append(size).append(", ");
				sb.append(count).append(");");

				String methodName = "static void " + pushOrPop + "_" + headerStackVarUnderScore;
				if (!map.containsValue(methodName + "_method")) {
					String methodArgs = headerStackTypeName + "* headerStack, int size, int count";
					String methodBody = pushOrPop.equals("push_front") ? getPushMethodBody() : getPopMethodBody();
					String method = methodName + "(" + methodArgs + ")" + " {\n" + methodBody + "}";
					map.put(methodName + "_method", method);
				}
				doTransform = true;
			}

		}

		if (!doTransform) {
			L.error("Unable to transform handle push_front on header stack " + getFormattedText());
		}
		return doTransform;
	}

	private String getPushMethodBody() {
		return "\tfor (int i = size-1; i >= 0; i -= 1) {\n" +
				"\t\tif (i >= count) {\n" +
				"\t\t\theaderStack[i] = headerStack[i-count];\n" +
				"\t\t} else {\n" +
				"\t\t\theaderStack[i].isValid = 0;\n" +
				"\t\t}\n" +
				"\t}\n";
	}

	private String getPopMethodBody() {
		return "\tfor (int i = 0; i < this.size; i++) {\n" +
				"\t\tif (i+count < this.size) {\n" +
				"\t\t\theaderStack[i] = headerStack[i+count];\n" +
				"\t\t} else {\n" +
				"\t\t\theaderStack[i].isValid = 0;\n" +
				"\t\t}\n" +
				"\t}";
	}

	private void transformCheckSumMethods(LinkedHashMap<String, String> map, IIRNode controlNode, StringBuilder sb, String checkSumMethod) {
		List<IArgument> arguments = getArguments();
		String condition = arguments.get(0).getFormattedText();
		String checksumField = arguments.get(2).getFormattedText().replaceAll("\\.", "_");
		String buildBufferMethod = "build_" + checksumField + "_buffer()";
		String checksumVar = "BR_UINT8_T_STAR_BR&(" + arguments.get(2).getFormattedText() + ")";

		IArgument dataField = arguments.get(1);
		extractChecksumMethods(map, controlNode, buildBufferMethod, dataField, checksumField + "_buffer");

		String bufferSize = "";
		String bufferSizeKey = buildBufferMethod + "_bufferSize";
		if (map.containsKey(bufferSizeKey)) {
			bufferSize = map.get(bufferSizeKey);
			map.remove(bufferSizeKey);
		} else {
			L.error("Unable to find buffer size for checksum methods");
		}

		String hashAlgorithm = arguments.get(3).getFormattedText().split("\\.")[1];

		sb.append(checkSumMethod).
				append("(").
				append(condition).
				append(", ").
				append(buildBufferMethod).
				append(", ").
				append(checksumVar).
				append(", ").
				append(bufferSize).
				append(", ").
				append(hashAlgorithm).
				append(");");
	}

	private void extractChecksumMethods(LinkedHashMap<String, String> map, IIRNode controlNode, String buildBufferMethod, IArgument argument, String checksumField) {
		String bufferSize = "";
		List<ExprMemberAccessContextExt> exprs = new ArrayList<>();
		argument.visitNode(node -> {
			if (node instanceof ExprMemberAccessContextExt) {
				exprs.add((ExprMemberAccessContextExt) node);
				return false;
			}
			return true;
		});

		IIRNode p4ProgramNode = controlNode.getParentIRNode();
		while (p4ProgramNode != null && !(p4ProgramNode instanceof IP4Program)) {
			p4ProgramNode = p4ProgramNode.getParentIRNode();
		}

		if (p4ProgramNode != null) {
			ArrayList<String> emitStrings = new ArrayList<>();
			int size = 0;

			emitStrings.add("\tint bitOffset = 0;\n");
			for (ExprMemberAccessContextExt expr : exprs) {
				Symbol symbol = p4ProgramNode.resolve(expr.getFormattedText());
				size += symbol.getSizeInBits();

				String emitString = "\tEMIT_BITS(" + checksumField + ", (uint8_t *)&(" + expr.getFormattedText() + "), bitOffset, " + symbol.getSizeInBits() + ");";
				String bitOffsetString = "\tbitOffset = bitOffset + " + symbol.getSizeInBits() + ";\n";

				emitStrings.add(emitString);
				emitStrings.add(bitOffsetString);

			}

			emitStrings.add("\n\treturn " + checksumField + ";");
			assert size % 8 == 0;
			bufferSize = String.valueOf(size / 8);

			String initString = "\tmemset(" + checksumField + ", 0, sizeof(" + checksumField + "));";
			emitStrings.add(0, initString);
			emitStrings.add(0, "{");
			emitStrings.add(0, "static uint8_t* " + buildBufferMethod);
			emitStrings.add("}");

			map.put(buildBufferMethod + "_method", CGenUtils.joinStrings(emitStrings, "\n"));
			map.put(buildBufferMethod + "_variable", "static uint8_t " + checksumField + "[" + bufferSize + "];");
			map.put(buildBufferMethod + "_bufferSize", bufferSize);

		} else {
			L.error("P4Program Node cannot be null");
		}
	}

	@Override
	public ILValue getLValue() {
		return (ILValue) getExtendedContext(((CallWithoutTypeArgsContext) getContext()).lvalue());
	}

	@Override
	public List<IArgument> getArguments() {
		ArgumentListContextExt argumentListContextExt = (ArgumentListContextExt) getExtendedContext(((CallWithoutTypeArgsContext)getContext()).argumentList());//(ArgumentListContextExt) getExtendedContext(getContext().argumentList());
		List<IArgument> arguments = new ArrayList<>();

		if (argumentListContextExt != null) {
			for (IIRNode node : argumentListContextExt.getChildren()) {
				if (node instanceof IArgument) {
					arguments.add((IArgument) node);
				}
			}
		}

		return arguments;
	}

	@Override
	public AbstractBaseExt replaceHeaderValid(){
		String call = getFormattedText();
		AbstractBaseExt abstractBaseExt = null;
		if(getFormattedText().endsWith(".setValid();") ){
			call = call.replace(".setValid()", ".isValid = 1w0x1");
			abstractBaseExt = AbstractBaseExt.getAssignment(call);
			abstractBaseExt.setPredicateSymbol(this.predicateSymbol);
		}else if(getFormattedText().endsWith(".setInvalid();")){
			call = call.replace(".setInvalid()", ".isValid = 1w0x0");
			abstractBaseExt = AbstractBaseExt.getAssignment(call);
			abstractBaseExt.setPredicateSymbol(this.predicateSymbol);
		}else{
			abstractBaseExt = this;
		}
		return abstractBaseExt;
	}

	public String replaceHeader(){
		String call = getFormattedText();

		if(getFormattedText().endsWith(".setValid();") ){
			call = call.replace(".setValid()", ".isValid = 1w0x1");
			//abstractBaseExt = AbstractBaseExt.getAssignment(call);
			//abstractBaseExt.setPredicateSymbol(this.predicateSymbol);
		}else if(getFormattedText().endsWith(".setInvalid();")){
			call = call.replace(".setInvalid()", ".isValid = 1w0x0");
			//abstractBaseExt = AbstractBaseExt.getAssignment(call);
			//abstractBaseExt.setPredicateSymbol(this.predicateSymbol);
		}
		return call;
	}

	public void evaluateAction(BitSet packetByteVector, BitSet packetBitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		CallWithoutTypeArgsContext ctx = (CallWithoutTypeArgsContext)getContext();
		String call = getExtendedContext(ctx.lvalue()).getFormattedText();
		if(call.endsWith("setValid") ){
			packetBitVector.set(headerValidLocations.get(call.replace("setValid", "isValid")));
		}
		else if(call.endsWith("setInvalid") ){
			packetBitVector.clear(headerValidLocations.get(call.replace("setInvalid", "isValid")));
		}
	}
}
