package com.p4.p416.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.drmt.alu.CTable;
import com.p4.p416.iface.*;
import com.p4.pktgen.model.SOMModel;
import com.p4.proto.pi.Pi;
import com.p4.proto.pi.Pi.Table;
import com.p4.proto.pi.Pi.HwTableInfo;
import com.p4.proto.pi.Pi.HwTableInfo.TableType;
import com.p4.proto.pi.Pi.IntArray;
import com.p4.proto.pi.Pi.IntArray.Builder;
import com.p4.proto.pi.Pi.MatchKey;
import com.p4.proto.pi.Pi.MatchKey.MatchType;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.cfg.TableMeta;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ActionsContext;
import com.p4.p416.gen.P416Parser.ConstEntriesContext;
import com.p4.p416.gen.P416Parser.KeyContext;
import com.p4.p416.gen.P416Parser.LocalConstVariableContext;
import com.p4.p416.gen.P416Parser.LocalVariableContext;
import com.p4.p416.gen.P416Parser.NameContext;
import com.p4.p416.gen.P416Parser.TableDeclarationContext;
import com.p4.p416.gen.P416Parser.TablePropertyContext;
import com.p4.p416.gen.P416Parser.TablePropertyListContext;
import com.p4.tools.graph.structs.TableMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=false)
@Data
public class TableDeclarationContextExt extends AbstractBaseExt implements ITable{

	private static final Logger L = LoggerFactory.getLogger(TableDeclarationContextExt.class);
	
	protected Integer sram;
	protected Integer tcam;


	public TableDeclarationContextExt(TableDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TableDeclarationContext getContext(){
		return (TableDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TableDeclarationContext getContext(String str){
		return (TableDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).tableDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TableDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TableDeclarationContext.class.getName());
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
		TableDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}


	@Override
	public String getTypeName()
	{
		TableDeclarationContext ctx = getContext();
		return ctx.TABLE().getText();
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName())
				&& thatType.getSymbolName().equals(getSymbolName());
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

	protected int sizeInBits;
	protected int sizeInBytes;
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		this.enclosingScope.add(this);
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/


	@Override
	public void buildST(TableMap tm){
		TableDeclarationContext ctx = (TableDeclarationContext) getContext();
		tm.add(getExtendedContext(ctx.name()).getFormattedText(), tm.currentControlScope().cg.name);
		//System.out.println(ctx.name().getText());
		super.buildST(tm);
	}

	//GL - ControlBlock START
	//TODO These should be lists.
	@Getter
	private TablePropertyContextExt actionTablePropertyContext;
	@Getter
	private TablePropertyContextExt keyTablePropertyContext;
	@Getter
	private TablePropertyContextExt localVariableTablePropertyContext;
	@Getter
	private TablePropertyContextExt sizeTablePropertyContext;
	@Getter
	private TablePropertyContextExt defaultActionTablePropertyContext;

	public void extractTableProperties(){
		/* Tables has different table properties , action, key, Size , defaultAction .. */
		TableDeclarationContext ctx = (TableDeclarationContext)getContext();
		TablePropertyListContext tablePropertyListContext = getExtendedContext((TablePropertyListContext) ctx.tablePropertyList()).getContext();

		if ( tablePropertyListContext != null &&  tablePropertyListContext.getText() != "")
		{
			for( TablePropertyContext tablePropertyContext : tablePropertyListContext.tableProperty()){
				if(tablePropertyContext instanceof ActionsContext){
					actionTablePropertyContext = (TablePropertyContextExt) getExtendedContext((getExtendedContext(tablePropertyContext).getContext()));
				}else if(tablePropertyContext instanceof KeyContext){
					keyTablePropertyContext =(TablePropertyContextExt) getExtendedContext(getExtendedContext(tablePropertyContext).getContext());
				}else if(tablePropertyContext instanceof LocalVariableContext){
					localVariableTablePropertyContext =(TablePropertyContextExt) getExtendedContext(getExtendedContext(tablePropertyContext).getContext());
				}else if(tablePropertyContext instanceof ConstEntriesContext){
					throw new UnsupportedOperationException();
				}else if(tablePropertyContext instanceof LocalConstVariableContext){
					sizeTablePropertyContext =(TablePropertyContextExt) getExtendedContext(getExtendedContext(tablePropertyContext).getContext());
				}
			}
		}
	}

	@Override
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		//Get the lastest extended context.
		TableDeclarationContext ctx = (TableDeclarationContext) getContext();

		//Get the Lastest extended context of NameContext.
		NameContext nameContext = (NameContext) getExtendedContext(ctx.name()).getContext();
		tables.put(nameContext.getText(),this);
		return;
	}

	@Override
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		//Stop the  traversal, as it does not make sense.
		return;
	}


	@Override
	public void encode()
	{
		//Assume name of the action block is a symbol
		// and this symbol is initialized with the adress at which the action block is loaded.
		//Here we generate the instructions( sudo lang ) for the statements in the action block.
		super.encode();

	}


	public int getCycleCount()
	{
		// How many Cycles are required to execute this table's action block.
		return 1;
	}
	public int getALUCount(int cycle)
	{
		assert (cycle == 1);
		//Sum of all alu's required by all the actions of this table;
		return 0;
	}

	//GL - ControlBlock END
	@Getter
	private int tableId = Integer.MIN_VALUE;
	@Getter private String tableName = "";
	public void setIds(AtomicInteger controlId, AtomicInteger tableId, AtomicInteger actionId, AtomicInteger instId){
		this.tableId = tableId.getAndIncrement();
		TableDeclarationContext tableDeclarationContext = (TableDeclarationContext) getContext();
		NameContext nameContext = (NameContext) getExtendedContext(tableDeclarationContext.name()).getContext();
		tableName = nameContext.getText();
		L.debug(getExtendedContext(tableDeclarationContext).getFormattedText()+":"+this.tableId);
		super.setIds(controlId, tableId, actionId, instId);
	}

	public void setKeys(KeyMeta k){
		if(this.tableId == Integer.MIN_VALUE){
			throw new RuntimeException();
		}
		k.currentKeyId = tableId;//Set correct Table Id
		k.tableName = tableName;
		//TableDeclarationContext =
		super.setKeys(k);
		k.currentKeyId = Integer.MIN_VALUE;
	}

	@Override
	public void populateTableActionsMap(Map<String,TableMeta> map) {
		TableDeclarationContext ctx = (TableDeclarationContext) getContext();
		NameContext nameContext = (NameContext) getExtendedContext(ctx.name()).getContext();
		//TablePropertyListContext tplctx = (TablePropertyListContext)ctx.tablePropertyList().extendedContext.getContext();
		TableMeta meta = new TableMeta();
		String tableName = nameContext.getText();
		super.populateActionsList(meta);
		map.put(tableName, meta);
	}

	@Override
	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			TableDeclarationContext tablectx = (TableDeclarationContext)getContext();
			CFGBuildingBlock b = CFGBuildingBlock.unitOf(getExtendedContext(tablectx));
			((TableDeclarationContextExt) getExtendedContext(tablectx)).extractTableProperties();
			TablePropertyContextExt actionTablePropertyContextExt = ((TableDeclarationContextExt) getExtendedContext(tablectx)).getActionTablePropertyContext();
			List<ActionRefContextExt> actionRefContextExts = ((ActionsContextExt)actionTablePropertyContextExt).getActionList();
			Set<CFGBuildingBlock> actioncfgs = new LinkedHashSet<>();
			for(ActionRefContextExt acrefs:actionRefContextExts){
				ActionDeclarationContextExt acde = cfgmap.getCurrentCntrlBlock().getActions().get(acrefs.getActionName());
				if(acde==null){
					throw new RuntimeException("Can't find Action declaration with Name "+acrefs.getActionName());
				}
				actioncfgs.add(acde.buildNGetCFG(cfgmap));
			}
			this.cFGBuildingBlock = CFGBuildingBlock.linkDisjoint(b, actioncfgs);
		}
		return this.cFGBuildingBlock;
	}

	public String cFGNodeSummary(){
		TableDeclarationContext tablectx = (TableDeclarationContext)getContext();
		return tablectx.name().getText();
	}

	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getName();
	}

	@Override
	public String getDefaultAction() {
		List<String> initStrings = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof LocalConstVariableContextExt) {
				return (node.getFormattedText().startsWith("default_action =") || node.getFormattedText().startsWith("default_action="));

			} else if (node instanceof InitializerContextExt) {
				initStrings.add(node.getFormattedText());
				return false;
			} else {
				return true;
			}
		});
		return !initStrings.isEmpty() ? initStrings.get(0).substring(0, initStrings.get(0).indexOf('(')) : null;
	}

	@Override
	public List<IKeyElement> getKeyElements() {
		List<IKeyElement> keyElements = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IKeyElement) {
				keyElements.add((IKeyElement) node);
			}
		});
		return keyElements;
	}

	@Override
	public String getTableSize() {
		List<String> initStrings = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof LocalConstVariableContextExt) {
				return (node.getFormattedText().contains("size =") || node.getFormattedText().contains("size="));

			} else if (node instanceof InitializerContextExt) {
				initStrings.add(node.getFormattedText());
				return false;
			} else {
				return true;
			}
		});
		return !initStrings.isEmpty() ? initStrings.get(0) : null;
	}

	@Override
	public List<IActionRef> getActionsRefs() {
		List<IActionRef> actionRefs = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IActionRef) {
				actionRefs.add((IActionRef) node);
			}
		});

		return actionRefs;
	}

	@Override
	public IControl getControl() {
		IIRNode parent = getParentIRNode();
		while (parent != null && !(parent instanceof IControl)) {
			parent = parent.getParentIRNode();
		}

		return parent != null ? (IControl) parent : null;
	}

	public com.p4.drmt.struct.ITable getTableDef(){
		CTable ret = new CTable(getExtendedContext(getContext().name()).getFormattedText(),getTableId());
		TableDeclarationContext tableDeclarationContext = getContext();
		List<KeyElementContextExt> keyElementContexts = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof KeyElementContextExt) {
				keyElementContexts.add((KeyElementContextExt) node);
			}
		});
		for(KeyElementContextExt keyElementContextExt: keyElementContexts){
			String keyField = getExtendedContext(keyElementContextExt.getContext().expression()).getFormattedText();
			keyField = keyField.replaceAll("\\(", "");
			keyField = keyField.replaceAll("\\)", "");
			ret.getKeyFields().add(AbstractBaseExt.getFieldOf(this, keyField));
		}
		List<IActionRef> actionsRefs = getActionsRefs();
		for(IActionRef actionRef:actionsRefs){
			ActionDeclarationContextExt actionDeclarationContextExt = (ActionDeclarationContextExt) resolve(actionRef.getActionDeclaration().getNameString());
			ret.getActions().add(actionDeclarationContextExt.getCAction());
		}
		return ret;
	}

	@Override
	public void emitP4Info(Pi.P4Info.Builder p4Info) {
		L.info("Populating p4 tables info ... ");
		Table.Builder tableBuilder = Table.newBuilder();
		List<IKeyElement> iKeyElements = getKeyElements();

		for(IKeyElement iKeyElement: iKeyElements) {
			MatchKey.Builder matchKeyBuilder = MatchKey.newBuilder();

			matchKeyBuilder.setName(iKeyElement.getKeyName());
			MatchType type =  MatchType.valueOf(iKeyElement.getKeyMatchKind().toUpperCase());
			matchKeyBuilder.setMatchType(type);
			if(iKeyElement.isBitSliced()) {
				int width = Math.abs(Integer.parseInt(iKeyElement.getBitSliceFrom()) - Integer.parseInt(iKeyElement.getBitSliceTo()));
				matchKeyBuilder.setBitwidth(width);
			} else {
				matchKeyBuilder.setBitwidth(iKeyElement.getKeySymbol().getSizeInBits());
			}

			tableBuilder.addMatchKeys(matchKeyBuilder.build());
		}

		tableBuilder.setId(getTableId()); // table id
		tableBuilder.setName(getNameString()); // table name

		if(getTableSize() !=null)
			tableBuilder.setSize(Integer.parseInt(getTableSize()));

		List<IActionRef> actionRefs = getActionsRefs();
		if(actionRefs != null) {
			for(IActionRef actionRef : actionRefs) {
				IActionDeclaration actiondecl = actionRef.getActionDeclaration();
				if(actiondecl != null)
					tableBuilder.addActionRefs(actiondecl.getActionId());

				if(actionRef.isDefaultAction()) {
					tableBuilder.setConstDefaultActionId(actiondecl.getActionId());
				}
			}
		}
		tableBuilder.setIsConstTable(false); //TODO check for table const

		// HW info for Table
		HwTableInfo.Builder hwActionBuilder = HwTableInfo.newBuilder();

		SOMModel somModel = SOMModel.getInstance();
		Integer somId = somModel.getSomIdOfTable(getTableId());
		com.p4.pktgen.enums.LayoutType layout = somModel.getLayoutType(somId, getTableId());
		List<List<Integer>> keysLayout = somModel.getKeyMemoryLayout(somId, getTableId());
		List<List<Integer>> dataLayout = somModel.getDataMemoryLayout(somId, getTableId());

		TableType tableType =  TableType.valueOf(layout.toString().toUpperCase());
		
		hwActionBuilder.setTableType(tableType);
		
		hwActionBuilder.setSomId(somId);
		
		for(List<Integer> col: keysLayout) {
			Builder array = IntArray.newBuilder();
			for(Integer row : col) {
				array.addField(row);
			}
			hwActionBuilder.addKeyLoc(array);
		}
		
		for(List<Integer> col: dataLayout) {
			Builder array = IntArray.newBuilder();
			for(Integer row : col) {
				array.addField(row);
			}
			hwActionBuilder.addDataLoc(array);
		}
		
		tableBuilder.setHwTableInfo(hwActionBuilder.build());
		p4Info.addTables(tableBuilder.build());

	}

}
