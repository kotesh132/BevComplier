package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.drmt.struct.IALUOperation;
import com.p4.p416.iface.*;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ControlBodyContext;
import com.p4.p416.gen.P416Parser.ControlDeclarationContext;
import com.p4.p416.gen.P416Parser.ControlTypeDeclarationContext;
import com.p4.tools.graph.structs.TableMap;

import lombok.Getter;
import lombok.Setter;

public class ControlDeclarationContextExt extends AbstractBaseExt implements IControl {

	@Getter @Setter protected Integer sram;
	@Getter @Setter protected Integer tcam;

	public ControlDeclarationContextExt(ControlDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  ControlDeclarationContext getContext(){
		return (ControlDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ControlDeclarationContext getContext(String str){
		return (ControlDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).controlDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ControlDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ControlDeclarationContext.class.getName());
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
		ControlDeclarationContext ctx = getContext();
		ControlTypeDeclarationContextExt controlTypeDeclarationContextExt = (ControlTypeDeclarationContextExt)getExtendedContext(ctx.controlTypeDeclaration());
		return controlTypeDeclarationContextExt.getSymbolName();
	}

	@Override
	public String getTypeName()
	{
		ControlDeclarationContext ctx = getContext();
		ControlTypeDeclarationContextExt controlTypeDeclarationContextExt = (ControlTypeDeclarationContextExt)getExtendedContext(ctx.controlTypeDeclaration());
		return controlTypeDeclarationContextExt.getTypeName();
	}

	@Override
	public Boolean isSame(Type type)
	{
		ControlDeclarationContext ctx = getContext();
		ControlTypeDeclarationContextExt controlTypeDeclarationContextExt = (ControlTypeDeclarationContextExt)getExtendedContext(ctx.controlTypeDeclaration());
		return controlTypeDeclarationContextExt.isSame(type);
	}

	@Override
	public Type getType()
	{
		return this;
	}


	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		return;
	}

	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();

		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
		{
			throw new NameCollisionException((String)this.getSymbolKey());
		}
		this.enclosingScope.add(this);
		Scope newScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(newScope));

		int bitOffset = 0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		int byteOffset=0;
		super.setByteOffset(new AtomicInteger(byteOffset)); //GL Call after setBitOffset.
		byteOffset=0;
		super.setAlignedByteOffset(new AtomicInteger(byteOffset));
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public void buildST(TableMap tm){
		super.buildST(tm);
		tm.popCurrentControlScope();
	}

	//GL - ControlBlock
	public String getName()
	{
		ControlDeclarationContext ctx = (ControlDeclarationContext) getContext();
		ControlTypeDeclarationContextExt controlTypeDeclarationContextExt = (ControlTypeDeclarationContextExt) getExtendedContext(ctx.controlTypeDeclaration());
		return controlTypeDeclarationContextExt.getName();

	}

	//GL - ControlBlock START
	@Override
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		controlBlocks.put(getName(), this);
		return;
	}

	protected List<TableDeclarationContextExt> tableApplySequence;

	private Map<String, TableDeclarationContextExt> tables;
	private Map<String, ActionDeclarationContextExt> actions;

	public List<TableDeclarationContextExt> getTableApplySequence()
	{
		if ( tableApplySequence != null ) return tableApplySequence;
		List<ApplyMethodCallContextExt> tableApplyCalls = new ArrayList<ApplyMethodCallContextExt>();
		//Call only on ControlBody.
		ControlDeclarationContext controlDeclarationContext = (ControlDeclarationContext) getContext();
		ControlBodyContext controlBodyContext = getExtendedContext( controlDeclarationContext.controlBody()).getContext();
		getExtendedContext(controlBodyContext).populateTableApplySequence(tableApplyCalls);
		if ( tableApplyCalls.size() > 0)
		{
			List<TableDeclarationContextExt> tableList = new ArrayList<TableDeclarationContextExt>();
			for(ApplyMethodCallContextExt extCtx:tableApplyCalls)
			{
				String tableName = extCtx.getLvalue();
				tableList.add(tables.get(tableName));
			}
			this.tableApplySequence = tableList;
		}
		return tableApplySequence;
	}


	public Map<String, TableDeclarationContextExt> getTables()
	{
		if (this.tables == null)
		{
			this.getTables(new LinkedHashMap<String, TableDeclarationContextExt>());
		}
		return this.tables;
	}


	@Override
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		super.getTables(tables);
		this.tables = new LinkedHashMap<String, TableDeclarationContextExt>();
		this.tables.putAll(tables);
	}

	public Map<String, ActionDeclarationContextExt> getActions() {
		if ( this.actions == null)
		{
			this.actions = new LinkedHashMap<String, ActionDeclarationContextExt>();
			this.getActions(this.actions);
		}
		return this.actions;
	}

	@Override
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		super.getActions(actions);
		this.actions = new LinkedHashMap<String, ActionDeclarationContextExt>();
		this.actions.putAll(actions);
	}


	//GL - ControlBlock END
	@Getter
	private int controlId = Integer.MIN_VALUE;

	@Override
	public void setIds(AtomicInteger controlId, AtomicInteger tableId, AtomicInteger actionId,AtomicInteger instId){
		this.controlId = controlId.getAndIncrement();
		super.setIds(controlId, tableId, actionId, instId);
	}

	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			ControlDeclarationContext ctx = (ControlDeclarationContext) getContext();
			String name = getExtendedContext(((ControlTypeDeclarationContext)getExtendedContext(ctx.controlTypeDeclaration()).getContext()).name()).getContext().getText();
			cfgmap.setCurrentCntrlBlock(this);
			if(ctx.controlLocalDeclarations()!=null){
				getExtendedContext(ctx.controlLocalDeclarations()).buildNGetCFG(cfgmap);
			}
			CFGBuildingBlock ret = CFGBuildingBlock.unitOf(getExtendedContext(ctx));
			CFGBuildingBlock next = getExtendedContext(((ControlBodyContext)getExtendedContext(ctx.controlBody()).getContext()).blockStatement()).buildNGetCFG(cfgmap);
			this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(ret, next);
			cfgmap.getCfgmap().put(name, this.cFGBuildingBlock);
			cfgmap.setCurrentCntrlBlock(null);
		}
		return this.cFGBuildingBlock;
	}


	@Override
	public String cFGNodeSummary(){
		ControlDeclarationContext ctx = (ControlDeclarationContext) getContext();
		return ctx.controlTypeDeclaration().name().getText();
	}

	@Override
	public void unroll(Map<String, ControlDeclarationContextExt> controlBlocks,
			List<ControlLocalDeclarationContextExt> controlLocalDeclarations,
			List<ParserRuleContext> applyStatements ) {
		ControlDeclarationContext ctx = (ControlDeclarationContext) getContext();

		getControlLocalDeclarationsContextExt(controlLocalDeclarations);

		Map<StatementOrDeclarationContextExt,StatementType> stmts = new HashMap<StatementOrDeclarationContextExt, StatementType>();
		getExtendedContext(ctx.controlBody()).getStatements(stmts);
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Map.Entry<StatementOrDeclarationContextExt, StatementType> stmt : stmts.entrySet()){
			stmt.getKey().unroll(controlBlocks, controlLocalDeclarations, applyStatements);
			sb.append(stmt.getKey().getFormattedText()+"\n");
		}

		sb.append("}\n");
		ParserRuleContext blockStatementContext= new PopulateExtendedContextVisitor().visit(getParser(sb.toString()).blockStatement());
		applyStatements.add(blockStatementContext);

	}


	public void flatten(Map<String, ControlDeclarationContextExt> controlBlocks){
		List<ControlLocalDeclarationContextExt> controlLocalDeclarations = new ArrayList<ControlLocalDeclarationContextExt>();
		List<ParserRuleContext> applyStatements = new ArrayList<ParserRuleContext>();
		unroll(controlBlocks, controlLocalDeclarations, applyStatements);

		ControlDeclarationContext ctx = (ControlDeclarationContext) getContext();
		StringBuilder sb = new StringBuilder();
		sb.append(getExtendedContext(ctx.controlTypeDeclaration()).getFormattedText());
		if(ctx.optConstructorParameters() != null && !ctx.optConstructorParameters().getText().equals(""))
			sb.append(getExtendedContext(ctx.optConstructorParameters()).getFormattedText());
		sb.append("{ \n");
		for(ControlLocalDeclarationContextExt controlDeclarationContextExt: controlLocalDeclarations){
			sb.append(controlDeclarationContextExt.getFormattedText()+"\n");
		}
		sb.append("apply ");
		for(ParserRuleContext parserRuleContext: applyStatements){
			sb.append(getExtendedContext(parserRuleContext).getFormattedText()+"\n");
		}
		sb.append("}");
		this.setContext(getContext(sb.toString()));
	}


	@Override
	public List<IParameter> getParameters() {

		P416Parser.ControlTypeDeclarationContext controlTypeDeclarationContext = getContext().controlTypeDeclaration();
		if(controlTypeDeclarationContext.parameterList() != null) {
			ParameterListContextExt parameterListContextExt = (ParameterListContextExt) getExtendedContext(controlTypeDeclarationContext.parameterList());
			return parameterListContextExt.getParameters();
		}
		return new ArrayList<>();
	}

	@Override
	public String getNameString() {

		return getSymbolName();
	}

	@Override
	public IControlBody getControlBody() {
		return (IControlBody) getExtendedContext(getContext().controlBody());
	}

	@Override
	public List<ITable> getTableObjects() {
		List<ITable> tables = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof ITable) {
				tables.add((ITable) node);
			}
		});
		return tables;
	}

	@Override
	public List<IActionDeclaration> getActionDeclarations() {
		List<IActionDeclaration> actions = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IActionDeclaration) {
				actions.add((IActionDeclaration) node);
			}
		});
		return actions;
	}
	//Not in AbstractBaseExt
	public List<IALUOperation> getAllAssignments(){
		List<IALUOperation> ret = new ArrayList<>();
		ControlDeclarationContext controlDeclarationContext = getContext();
		getExtendedContext(controlDeclarationContext.controlBody()).collectAllAssignmentOperations(ret);
		return ret;
	}

}
