package com.p4.p416.gen;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.drmt.alu.CAction;
import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IField;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IBlockStatement;
import com.p4.p416.iface.IParameter;
import com.p4.proto.pi.Pi;
import com.p4.proto.pi.Pi.Action;
import com.p4.proto.pi.Pi.Action.Param;
import com.p4.proto.pi.Pi.Action.Param.Builder;
import com.p4.proto.pi.Pi.HwActionInfo;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.schd.NewScheduler;
import com.p4.p416.applications.AluInstruction;
import com.p4.p416.applications.AluMapEntry;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ActionDeclarationContext;
import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.utils.Utils.Pair;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ActionDeclarationContextExt extends AbstractBaseExt implements IActionDeclaration {

	protected static final Logger L = LoggerFactory.getLogger(ActionDeclarationContextExt.class);
	

	public ActionDeclarationContextExt(ActionDeclarationContext ctx) {
		super(ctx);

	}

	@Override
	@SuppressWarnings("unchecked")
	public  ActionDeclarationContext getContext(){
		return (ActionDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActionDeclarationContext getContext(String str){
		return (ActionDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).actionDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ActionDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ActionDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}


	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public String getSymbolName()
	{
		ActionDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}

	@Override
	public String getTypeName()
	{
		return getContext().ACTION().getText().trim();
	}

	@Override
	public Type getType()
	{
		return this;
	}

	@Override
	public Boolean isSame(Type type)
	{
		return (this.getTypeName().equals(type.getTypeName()));
	}

	@Override
	public void setByteOffset(AtomicInteger offset)
	{
		// This is necessary to avoid the control level setByteOffset() setting the parameter's offset in the action Block
		return;
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger offset)
	{
		// This is necessary to avoid the control level setByteOffset() setting the parameter's offset in the action Block
		return;
	}
	@Override
	public void setBitOffset(AtomicInteger offset)
	{
		// This is necessary to avoid the control level setByteOffset() setting the parameter's offset in the action Block
		return;
	}

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		this.enclosingScope.add(this);
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));

		int bitOffset = 0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		int byteOffset = 0;
		super.setByteOffset(new AtomicInteger(byteOffset)); //GL Call after setBitOffset.
		int alignedByteOffset=0;
		super.setAlignedByteOffset(new AtomicInteger(alignedByteOffset));
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/




	//GL - ControlBlock START
	@Override
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	@Override
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}

	@Override
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		//get the lastest context.
		ActionDeclarationContext ctx = getContext();
		//get the lastest context of NameContext.
		NameContextExt nameContext =  (NameContextExt) getExtendedContext(ctx.name());
		actions.put(nameContext.getFormattedText(), this);
		return;
	}



	protected int actionLocation;
	@Getter
	protected int actionId;
	public void setIds(AtomicInteger controlId, AtomicInteger tableId, AtomicInteger actionId, AtomicInteger instId){
		this.actionId = actionId.getAndIncrement();
		L.debug(getFormattedText()+":"+this.actionId);
		super.setIds(controlId, tableId, actionId, instId);
	}
	protected List<AluInstruction> instructionsList;

	public List<AluInstruction> getInstructionsList()
	{
		//We should have analyzed the instructions list and grouped them 
		//this APi should return the group of instruction sets, that can be executed in parallel.
		//For now all the instructions can be executed in parallel.
		return this.instructionsList;
	}

	@Override
	public void encode()
	{
		this.instructionsList = new ArrayList<AluInstruction>();
		super.populateALUInstructions(this.instructionsList);
	}


	@AllArgsConstructor
	public class AluMapEntryImpl implements AluMapEntry{
		@Getter
		public final List<AluInstruction> aluInstructions;
		@Getter
		public final int instructionIndex;
		@Getter
		public final int actionIndex;
		@Getter
		public final Map<AluInstruction,Integer> cpus;
		@Getter
		public final int tableId;
		@Getter
		public final int instEn;
	}

	public AluMapEntry getAluMapEntry(int instructionIndex, int actionIndex, int tableId, int instEn)
	{
		//{cycle ID} -> {No of ALUs}
		//AluMapEntryImpl aluMapEntryImpl = new AluMapEntryImpl();
		Map<AluInstruction,Integer> cpus = new LinkedHashMap<>();

		for(int i=0; i< this.instructionsList.size() ; i++)
		{
			cpus.put(instructionsList.get(i), i);
		}
		//aluMapEntryImpl.aluInstructions = getInstructionsList();
		//aluMapEntryImpl.cpus = cpus;
		AluMapEntryImpl aluMapEntryImpl = new AluMapEntryImpl(getInstructionsList(), instructionIndex, actionIndex, cpus, tableId, instEn);
		return aluMapEntryImpl;
	}


	//GL - ControlBlock END
	@Override
	public void populateTableActionsAndData(Map<String,List<Integer>> map) {
		ActionDeclarationContext ctx = (ActionDeclarationContext) getContext();
		List<Integer> paramWidths = new LinkedList<Integer>();
		super.populateParams(paramWidths);
		map.put(getExtendedContext(ctx.name()).getContext().getText(),paramWidths);
	}

	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			ActionDeclarationContext ctx = getContext();
			CFGBuildingBlock ret = CFGBuildingBlock.unitOf(getExtendedContext(ctx));
			CFGBuildingBlock next = getExtendedContext(ctx.blockStatement()).buildNGetCFG(cfgmap);
			this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(ret, next);
		}
		return this.cFGBuildingBlock;
	}

	@Override
	public String cFGNodeSummary(){
		ActionDeclarationContext ctx = (ActionDeclarationContext) getContext();
		return getExtendedContext(ctx.name()).getContext().getText();
	}

	public void evaluateAction(BitSet packetByteVector, BitSet packetBitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		ActionDeclarationContext ctx = getContext();
		if(ctx.blockStatement() != null) {
			BlockStatementContextExt blockStatementContext = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
			blockStatementContext.evaluateAction(packetByteVector, packetBitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
		}
	}

	@Override
	public void quadrupalize(boolean probe, List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		ActionDeclarationContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		blockStatementContextExt.quadrupalize(true, statementOrDeclarationContextExts2);
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:statementOrDeclarationContextExts2){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = blockStatementContextExt.getContext(sb.toString());
		blockStatementContextExt.setContext(blockStatementContext);
	}

	@Override
	public void removeElse(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
		ActionDeclarationContext ctx = getContext();
		BlockStatementContextExt blockStatementContextExt = (BlockStatementContextExt) getExtendedContext(ctx.blockStatement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		blockStatementContextExt.removeElse(statementOrDeclarationContextExts2, branchStack);
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:statementOrDeclarationContextExts2){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = blockStatementContextExt.getContext(sb.toString());
		blockStatementContextExt.setContext(blockStatementContext);
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getFormattedText();
	}

	@Override
	public List<IParameter> getParameters() {
		List<IParameter> parameters = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IParameter) {
				parameters.add((IParameter) node);
			}
		});
		return parameters;
	}

	@Override
	public IBlockStatement getBlockStatement() {
		return (IBlockStatement) getExtendedContext(getContext().blockStatement());
	}

	public List<IALUOperation> getAllAssignments(){
		List<IALUOperation> ret = new ArrayList<>();
		ActionDeclarationContext actionDeclarationContext = getContext();
		getExtendedContext(actionDeclarationContext.blockStatement()).collectAllAssignmentOperations(ret);
		return ret;
	}

	public List<IField> getParams1(){
		List<IField> ret = new ArrayList<>();
		ActionDeclarationContext ctx = getContext();
		if(ctx.parameterList() !=null){
			for(P416Parser.ParameterContext param: ((ParameterListContextExt)getExtendedContext(ctx.parameterList())).getContext().parameter()){
				ret.add(AbstractBaseExt.getFieldOf(this, getExtendedContext(param.name()).getFormattedText()));
			}
		}
		return ret;
	}

	public IAction getCAction(){
		CAction action = new CAction(getExtendedContext(getContext().name()).getFormattedText());
		action.getParameters().addAll(getParams1());
		List<IALUOperation> aluOperations = new ArrayList<>();
		getExtendedContext(getContext().blockStatement()).collectAllAssignmentOperations(aluOperations);
		action.getInstructions().addAll(aluOperations);
		return action;
	}

	@Override
	public void emitP4Info(Pi.P4Info.Builder p4Info) {
		L.info("Populating p4 actions info ... ");
		Action.Builder actionBuilder = Action.newBuilder();
		actionBuilder.setId(getActionId());
		actionBuilder.setName(getNameString());
		List<IParameter> params = getParameters();

		//HWinfo
		HwActionInfo.Builder hwActionBuilder = HwActionInfo.newBuilder();

		for(IParameter iParam : params) {
			Builder paramBuilder = Param.newBuilder();
			paramBuilder.setName(iParam.getNameString());
			paramBuilder.setBitwidth(iParam.getType().getSizeInBits());
			actionBuilder.addParams(paramBuilder);

			//hw
			hwActionBuilder.addOffset(MemoryManager.getOffset(iParam.getNameString(), (Type)iParam));

			Map<Integer,Map<String,Integer>> tableToActionInstPtrMap = NewScheduler.getTableToActionInstPtrMap();
			boolean ptrPlaced = false;
			for(Map<String,Integer> actionToInstPtrMap : tableToActionInstPtrMap.values()) {
				L.info(getNameString());
				if(actionToInstPtrMap.get(getNameString()) != null && !ptrPlaced){
					Integer addr = actionToInstPtrMap.get(getNameString());
					hwActionBuilder.setInstructionAddress(addr);
					ptrPlaced = true;
				}
			}
		}



		actionBuilder.setHwActionInfo(hwActionBuilder);
		p4Info.addActions(actionBuilder.build());
	}
}
