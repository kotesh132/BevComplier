package com.p4.p416.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.drmt.struct.IALUOperation;
import com.p4.p416.applications.IMemoryRequest;
import com.p4.p416.applications.MemoryRequest;
import com.p4.p416.iface.IAssignmentStatement;
import com.p4.p416.iface.IBlockStatement;
import com.p4.p416.iface.IStatementOrDeclaration;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.p416.gen.P416Parser.StatOrDeclListContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.p416.gen.P416Parser.StatementOrDeclarationContext;
import com.p4.utils.Utils.Pair;

public class BlockStatementContextExt extends AbstractBaseExt implements IBlockStatement{

	public BlockStatementContextExt(BlockStatementContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  BlockStatementContext getContext(){
		return (BlockStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BlockStatementContext getContext(String str){
		return (BlockStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).blockStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof BlockStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ BlockStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}


	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/


	@Override
	public Type getType()
	{
		return this;
	}

	@Override
	public String getTypeName()
	{
		return "BlockStatement";
	}

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		this.enclosingScope.add(this);
		this.symbolName = UUID.randomUUID().toString();
		if ( this.enclosingScope.lookup(this.getSymbolKey()) != null)
		{
			throw new NameCollisionException((String)this.getSymbolKey());
		}
		this.enclosingScope.add(this);
		Scope newScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(newScope));

		int bitOffset=0;
		super.setBitOffset(new AtomicInteger(bitOffset));
		int byteOffset=0;
		super.setByteOffset(new AtomicInteger(byteOffset));
		int alignedByteOffset=0;
		super.setAlignedByteOffset(new AtomicInteger(alignedByteOffset));

	}
	private String symbolName;
	@Override
	public String getSymbolName()
	{
		return this.symbolName;
	}
	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	//GL - ControlBlock -START

	private Map<StatementOrDeclarationContextExt, StatementType> statements;

	@Override
	public void getStatements(Map<StatementOrDeclarationContextExt, StatementType>  stmts) {

		if ( this.statements == null)
		{
			this.statements = new LinkedHashMap<StatementOrDeclarationContextExt, StatementType>();
			super.getStatements(stmts);
			this.statements.putAll(stmts);
		}
		else{
			stmts.putAll(this.statements);
		}
	}

	@Override
	public StatementType getStatementType()
	{
		return StatementType.BLOCK_STATEMENT;
	}


	//GL - ControlBlock -END

	@Override
	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		if(this.cFGBuildingBlock==null){
			BlockStatementContext ctx = getContext();
			if(ctx.statOrDeclList()!=null){
				List<StatementOrDeclarationContext> children = ctx.statOrDeclList().statementOrDeclaration();
				if(children.size()==0){
					this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(ctx));
				}else if(children.size()==1){
					this.cFGBuildingBlock = getExtendedContext(children.get(0)).buildNGetCFG(cfgmap);
				}else{
					CFGBuildingBlock ret = getExtendedContext(children.get(0)).buildNGetCFG(cfgmap);
					for(int i=1; i<children.size(); i++){
						ret = CFGBuildingBlock.linkDisjoint(ret, getExtendedContext(children.get(i)).buildNGetCFG(cfgmap));
					}
					this.cFGBuildingBlock = ret;
				}
			}else{
				this.cFGBuildingBlock = CFGBuildingBlock.unitOf(getExtendedContext(ctx));
			}
		}
		return this.cFGBuildingBlock;
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		BlockStatementContext ctx = getContext();
		if(probe){
			List<StatementOrDeclarationContextExt> toplevel = new ArrayList<>();
			if(ctx.statOrDeclList()!=null){
				StatOrDeclListContext statOrDeclListContext= getExtendedContext(ctx.statOrDeclList()).getContext();
				List<StatementOrDeclarationContext> statementOrDeclarationContexts = statOrDeclListContext.statementOrDeclaration();
				for(StatementOrDeclarationContext s: statementOrDeclarationContexts){
					List<StatementOrDeclarationContextExt> p = new ArrayList<StatementOrDeclarationContextExt>();
					getExtendedContext(s).quadrupalize(probe,p);
					toplevel.addAll(p);
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("{\n");
			for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:toplevel){
				sb.append(statementOrDeclarationContextExt.getFormattedText());
				sb.append("\n");
			}
			sb.append("}\n");
			BlockStatementContext blockStatementContext = getContext(sb.toString());
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getExtendedContext(blockStatementContext).getFormattedText()));
		}
	}
	@Override
	public void removeElse(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
		BlockStatementContext ctx = getContext();
		List<StatementOrDeclarationContextExt> toplevel = new ArrayList<>();
		if(ctx.statOrDeclList()!=null){
			List<StatementOrDeclarationContext> statementOrDeclarationContexts = ((StatOrDeclListContext)getExtendedContext(ctx.statOrDeclList()).getContext()).statementOrDeclaration();
			for(StatementOrDeclarationContext s: statementOrDeclarationContexts){
				List<StatementOrDeclarationContextExt> p = new ArrayList<StatementOrDeclarationContextExt>();
				getExtendedContext(s).removeElse(p,branchStack);
				if(p.size()>0)
					toplevel.addAll(p);
				else
					toplevel.add((StatementOrDeclarationContextExt) getExtendedContext(s));
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:toplevel){
			sb.append(statementOrDeclarationContextExt.getFormattedText());
			sb.append("\n");
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = getContext(sb.toString());
		setContext(blockStatementContext);
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getExtendedContext(blockStatementContext).getFormattedText()));
	}
	
	public void evaluateAction(BitSet packetByteVector, BitSet packetBitVector, List<ArrayList<Pair<Integer,Integer>>> headerBounds, Map<String, Long> values, Map<String, Long> randomActionParameterValues, Map<String, Integer> headerValidLocations, Map<String, Pair<Integer, Integer>> headerFieldsOffsetsAndSizes) {
		BlockStatementContext blockStatementContext = getContext();
		if(blockStatementContext.statOrDeclList() != null) {
			StatOrDeclListContext statOrDeclListContext = getExtendedContext(blockStatementContext.statOrDeclList()).getContext();
			if(statOrDeclListContext.statementOrDeclaration() != null) {
				List<StatementOrDeclarationContext> statementOrDeclarationContextList = statOrDeclListContext.statementOrDeclaration();
				for(StatementOrDeclarationContext statementOrDeclarationContext: statementOrDeclarationContextList) {
					if(statementOrDeclarationContext.statement() != null) {
						StatementContext statementContext = getExtendedContext(statementOrDeclarationContext.statement()).getContext();
						if(statementContext.assignmentStatement() != null) {
							AssignmentStatementContextExt asce = (AssignmentStatementContextExt) getExtendedContext(statementContext.assignmentStatement());
							asce.evaluateAction(packetByteVector, packetBitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
						}
						else if(statementContext.blockStatement() != null) {
							BlockStatementContextExt blkstmt = (BlockStatementContextExt) getExtendedContext(statementContext.blockStatement());
							blkstmt.evaluateAction(packetByteVector, packetBitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
						}
						else if(statementContext.methodCallStatement() != null) {
							MethodCallStatementContextExt callStmt = (MethodCallStatementContextExt) getExtendedContext(statementContext.methodCallStatement());
							if(callStmt instanceof CallWithoutTypeArgsContextExt) {
								((CallWithoutTypeArgsContextExt) callStmt).evaluateAction(packetByteVector, packetBitVector, headerBounds, values, randomActionParameterValues, headerValidLocations, headerFieldsOffsetsAndSizes);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public List<IAssignmentStatement> getAssignmentStatements() {
		List<IAssignmentStatement> assignmentStatements = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IAssignmentStatement) {
				assignmentStatements.add((IAssignmentStatement) node);
			}
		});
		return assignmentStatements;
	}

    @Override
    public List<IStatementOrDeclaration> getStatementOrDeclarations() {
		List<IStatementOrDeclaration> statementOrDeclarations = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IStatementOrDeclaration) {
				statementOrDeclarations.add((IStatementOrDeclaration) node);
			}
		});
		return statementOrDeclarations;
	}

	@Override
	public AbstractBaseExt replaceHeaderValid(){
		List<StatementOrDeclarationContextExt> topLevel = new ArrayList<>();
		StringBuilder sb =  new StringBuilder();
		sb.append("{\n");
		if(getContext().statOrDeclList()!=null){
			List<StatementOrDeclarationContext> statementOrDeclarationContexts = ((StatOrDeclListContext)getExtendedContext(getContext().statOrDeclList()).getContext()).statementOrDeclaration();
			for(StatementOrDeclarationContext s: statementOrDeclarationContexts){
				AbstractBaseExt abstractBaseExt = getExtendedContext(s).replaceHeaderValid();
				sb.append(abstractBaseExt.getFormattedText());
				sb.append("\n");
			}
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = getContext(sb.toString());
		blockStatementContext.extendedContext.setPredicateSymbol(this.predicateSymbol);
		setContext(blockStatementContext);
		return getExtendedContext(blockStatementContext);
	}

	@Override
	public void removeSwitch(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack) {
		BlockStatementContext ctx = getContext();
		StatOrDeclListContext statOrDeclListContext = ctx.statOrDeclList();
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		if(statOrDeclListContext == null) return;
		List<StatementOrDeclarationContext> cc = statOrDeclListContext.statementOrDeclaration();

		for(StatementOrDeclarationContext c: cc){
			StatementContext stmtContext = c.statement();
			StatementOrDeclarationContextExt statementOrDeclarationContext = (StatementOrDeclarationContextExt)getExtendedContext(c);
			P416Parser.SwitchStatementContext switchCtx = stmtContext.switchStatement();
			if(switchCtx == null){
				sb.append(statementOrDeclarationContext.getFormattedText());
				sb.append("\n");
			}
		}
		sb.append("}\n");
		BlockStatementContext blockStatementContext = getContext(sb.toString());
		setContext(blockStatementContext);
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getExtendedContext(blockStatementContext).getFormattedText()));
	}

	@Override
	public void replaceVd(){

		if(getContext().statOrDeclList()!=null){
			List<StatementOrDeclarationContext> statementOrDeclarationContexts = ((StatOrDeclListContext)getExtendedContext(getContext().statOrDeclList()).getContext()).statementOrDeclaration();
			boolean contains = false;
			for(StatementOrDeclarationContext s: statementOrDeclarationContexts){
				if(s.statement() !=null) {
					if (s.statement().extendedContext.getContext().methodCallStatement() != null) {
						if (s.statement().extendedContext.getContext().methodCallStatement() instanceof P416Parser.CallWithoutTypeArgsContext) {
							contains = true;
						}
					}
				}
			}
			if(!contains){
				super.replaceVd();
			}else{
				StringBuilder sb =  new StringBuilder();
				sb.append("{\n");
				for(StatementOrDeclarationContext s: statementOrDeclarationContexts){
					if(s.statement() !=null && s.statement().extendedContext.getContext().methodCallStatement() != null && s.statement().extendedContext.getContext().methodCallStatement() instanceof P416Parser.CallWithoutTypeArgsContext) {
						CallWithoutTypeArgsContextExt callWithoutTypeArgsContextExt = (CallWithoutTypeArgsContextExt) s.statement().extendedContext.getContext().methodCallStatement().extendedContext;
						sb.append(callWithoutTypeArgsContextExt.replaceHeader());
					}else {
						sb.append(s.extendedContext.getFormattedText());
					}
					sb.append("\n");
				}
				sb.append("}\n");
				BlockStatementContext blockStatementContext = getContext(sb.toString());
				setContext(blockStatementContext);
			}

		}

	}

	@Override
	public void preAllocateGlobalAddress(Set<IMemoryRequest> symbolSet) {
		Set<IMemoryRequest> newSymbolSet = new LinkedHashSet<>();

		super.preAllocateGlobalAddress(newSymbolSet);

		for (IMemoryRequest symObj : newSymbolSet) {
			symbolSet.add(symObj);
		}

		// De-Allocation is in reverse order
		ArrayList<IMemoryRequest> arrayList = new ArrayList<IMemoryRequest>(newSymbolSet);
		Collections.reverse(arrayList);

		for (IMemoryRequest requestObj : arrayList) {
			MemoryRequest newRequestObj = new MemoryRequest(IMemoryRequest.AllocType.FREE, requestObj.getMemRequestSymbol());
		//	symbolSet.add(newRequestObj);
		}
		newSymbolSet.clear();
	}
	@Override
	public void collectAllAssignmentOperations(List<IALUOperation> aluOperations){
		BlockStatementContext blockStatementContext = getContext();
		if(blockStatementContext.statOrDeclList() != null) {
			StatOrDeclListContext statOrDeclListContext = getExtendedContext(blockStatementContext.statOrDeclList()).getContext();
			if (statOrDeclListContext.statementOrDeclaration() != null) {
				List<StatementOrDeclarationContext> statementOrDeclarationContextList = statOrDeclListContext.statementOrDeclaration();
				for (StatementOrDeclarationContext statementOrDeclarationContext : statementOrDeclarationContextList) {
					if (getExtendedContext(statementOrDeclarationContext.statement()) != null) {
						StatementContext statementContext = getExtendedContext(statementOrDeclarationContext.statement()).getContext();
						if(getExtendedContext(statementContext.assignmentStatement()) != null) {
							aluOperations.addAll(((AssignmentStatementContextExt)getExtendedContext(statementContext.assignmentStatement())).allALUOperations());
						}else if(getExtendedContext(statementContext.blockStatement()) != null){
							getExtendedContext(statementContext.blockStatement()).collectAllAssignmentOperations(aluOperations);
						}else if(getExtendedContext(statementContext.conditionalStatement()) != null) {
							getExtendedContext(statementContext.conditionalStatement()).collectAllAssignmentOperations(aluOperations);
						}
					}
				}
			}
		}
	}


}
