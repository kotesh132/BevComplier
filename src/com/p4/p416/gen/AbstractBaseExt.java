package com.p4.p416.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import com.p4.drmt.alu.CField;
import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IField;
import com.p4.p416.applications.*;
import com.p4.p416.iface.*;
import com.p4.p416.impl.IRTerminalNode;
import com.p4.proto.pi.*;
import com.p4.tools.graph.Edge;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.drmt.GlobalAddress;
import com.p4.drmt.parser.P4Assign;
import com.p4.drmt.parser.P4Extract;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parsers;
import com.p4.drmt.Transitions;
import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.cfg.TableMeta;
import com.p4.drmt.parser.FW;
import com.p4.drmt.semanticchecks.SemanticChecksPass;
import com.p4.p416.applications.IMemoryManager;
import com.p4.p416.archModel.ArchitecturalModel;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.ArgumentContext;
import com.p4.p416.gen.P416Parser.AssignmentStatementContext;
import com.p4.p416.gen.P416Parser.BlockStatementContext;
import com.p4.p416.gen.P416Parser.InstantiationContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.p416.gen.P416Parser.StatementOrDeclarationContext;
import com.p4.p416.gen.P416Parser.VariableDeclarationContext;
import com.p4.packetgen.structures.Field;
import com.p4.packetgen.structures.Node;
import com.p4.packetgen.utils.HeadersInfo;
import com.p4.packetgen.utils.StatesInfo;
import com.p4.tools.graph.structs.TableMap;
import com.p4.utils.Utils.Pair;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@EqualsAndHashCode(of={"CFGHashObject"})
@Data
public abstract class AbstractBaseExt  implements IExtendedContext {
	protected static final Logger L = LoggerFactory.getLogger(AbstractBaseExt.class);
	//variables
	@Getter
	protected static ExtendedContextGetVisitor extendedContextGetVisitor = new ExtendedContextGetVisitor();
	protected ContextGetVisitor contextGetVisitor;
	protected IMemoryManager memoryManager;
	protected Object CFGHashObject;
	protected SemanticChecksPass semanticChecksPass;
	protected ArchitecturalModel architecturalModel;
	public static int uid = 0;
	protected boolean isConstant;
	protected Integer newValue;

	// all struct and header fields except isvalid.
	protected static Map<AbstractBaseExt,String> allHdrFieldsMap = new HashMap<>();

	// all symbols that were resolved in expression(rvalue) and lvalue context .
	protected static Map<AbstractBaseExt,String> mapUsedStructFields = new HashMap<>();

	//SSA START
	public static SsaForm ssaObject = new SsaForm();
	protected int ssaVersion=0;
	protected boolean doneDelete=false;
	//SSA END

	public static Map<Pair<String,String> ,Pair<Integer,Integer>> fieldsMap  = new HashMap<>();

	@Setter(AccessLevel.NONE)
	protected List<ParserRuleContext> contexts;

	//constructor
//	public AbstractBaseExt(){
//		CFGHashObject = new Object();
//		extendedContextGetVisitor = new ExtendedContextGetVisitor();
//		contextGetVisitor = new ContextGetVisitor();
//		contexts = new ArrayList<ParserRuleContext>();
//		memoryManager = com.p4.drmt.MemoryManager.getInstance();
//		byteOffset = new Integer(Integer.MIN_VALUE);
//		semanticChecksPass = SemanticChecksPass.getInstance();
//		architecturalModel = ArchitecturalModel.getInstance();
//		alignedByteOffset = new Integer(Integer.MIN_VALUE);
//	}

	public AbstractBaseExt(ParserRuleContext context){
		CFGHashObject = new Object();
		//extendedContextGetVisitor = new ExtendedContextGetVisitor();
		contextGetVisitor = new ContextGetVisitor();
		contexts = new ArrayList<ParserRuleContext>();
		memoryManager = com.p4.drmt.MemoryManager.getInstance();
		byteOffset = new Integer(Integer.MIN_VALUE);
		semanticChecksPass = SemanticChecksPass.getInstance();
		architecturalModel = ArchitecturalModel.getInstance();
		alignedByteOffset = new Integer(Integer.MIN_VALUE);
		contexts.add(context);
	}

	//This method is not exposed outside.
	protected P416Parser getParser(String str){
		P416Lexer lexer = new P416Lexer(new ANTLRInputStream(str));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new P416Parser(tokens);
	}

	protected void addToContexts(ParserRuleContext context)
	{
		AbstractBaseExt extCtx = getExtendedContext(context);
		if(extCtx != null){
			extCtx.contexts = contexts;
		}
		//GL It should be an ordered set rather than an array, so that we don't do repeated additions of same context.
		contexts.add(context);
	}


	public static AbstractBaseExt getExtendedContext(ParseTree context)
	{
		if(context != null){
			return extendedContextGetVisitor.visit(context);
		} else{
			L.warn("Returning Null for extendedContext");
			return null;
		}
	}
	public ParserRuleContext getLatestContext(ParseTree context)
	{
		if(context != null){
			return contextGetVisitor.visit(context);
		} else{
			L.warn("Returning Null for extendedContext");
			return null;
		}
	}


	public void copyState(AbstractBaseExt that)
	{
		this.contexts = that.contexts;
		this.extendedContextGetVisitor  = that.extendedContextGetVisitor;
		this.contextGetVisitor = that.contextGetVisitor;
		this.tableApplyNode = that.tableApplyNode;
		this.nextCFGNodes = that.nextCFGNodes;
		this.CFGHashObject = that.CFGHashObject;
		this.byteOffset = that.byteOffset;
		this.alignedByteOffset = that.alignedByteOffset;

	}


	public AbstractBaseExt getParent() {
		if(contexts.get(0).getParent() ==null){
			return null;
		}else{
			return getExtendedContext(contexts.get(0).getParent());
		}
	}

	//SSA START
	public int getVersion()
	{
		return this.ssaVersion;
	}

	public int getAndIncVersion()
	{
		int oldVersion=this.ssaVersion;
		++this.ssaVersion;
		return oldVersion;
	}

	public void incVersion()
	{
		++this.ssaVersion;
		return;
	}

	// This method transforms the code into SSA form but in turn depends
	// on the removeElse method to transform the code into a chain of if's
	// some of them nested.
	// setVers introduces the versions for the variables.
	// markDel marks the assignment statement context as "can be deleted".
	public void  transform2SSA() {
		//remove else using the removeElse transform then apply
		//ssa transformations
		//Get all the symbols before removeElse
		defineSymbol(null);
		Stack<ExpressionContextExt> newStack = new Stack<>();
		removeElse(null,newStack);
		newStack.clear();
		// Get the new symbols after removeElse i.e. temporaries introduced
		// during removeElse as well
		defineSymbol(null);

		allHdrFieldsMap.clear();
		mapUsedStructFields.clear();

		P4Headers hdrs = new P4Headers();
		buildTypes(hdrs);
		List<String> fields = hdrs.flattenAllTypes();
		for (String str : fields) {
			// skip the hidden field
			if(str.endsWith(".isValid")==false) {
				allHdrFieldsMap.put((AbstractBaseExt)resolve(str),str);
			}
		}


		setVersions(ssaObject);
		//reinitialize parent before passing to markDelete
		ssaObject.setParentCtx(null);
		markDelete(ssaObject);
		assignPredicate(newStack);
		defineSymbol(null);

	}


	//internally used by setVersions
	public void populateMaxMap(Map<AbstractBaseExt,Integer> maxMap) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateMaxMap(maxMap);
				}
			}
		}
	}

	public void setVersions(SsaForm obj){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).setVersions(obj);
				}
			}
		}
	}

	public void markDelete(SsaForm obj){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null){
						getExtendedContext(childCtx).markDelete(obj);
					}
				}
			}
		}
	}

	//getMyFormattedText() app
	public String getSSAFormattedText(){
		StringBuilder sb = new StringBuilder();
		Params params = new Params(getLatestContext(getContext()), sb);
		params.setBeginingOfAlignemtText(getLatestContext(getContext()).start.getStartIndex());
		getSSAFormattedText(params);
		//logger.debug("output =\n" + sb.toString());
		return sb.toString();
	}

	public void getSSAFormattedText(Params params){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(childCtx instanceof TerminalNode){
					printTerminalNode((TerminalNode)childCtx,params);
				}
				else if((childCtx!=null) && (childCtx.getText().length() >0)){
					params.setContext((ParserRuleContext)childCtx);
					if(getExtendedContext(childCtx)!=null) {
						Params thisCtxParams = getExtendedContext(childCtx).getUpdatedParams(params);
						getExtendedContext(childCtx).getSSAFormattedText(thisCtxParams);
					} else {
						params.setBeginingOfAlignemtText(params.getContext().stop.getStopIndex()+1);
					}
				} 
			}
			getUpdatedParams(params);
		} 
	}
	//SSA END

	@Data
	protected class Params{
		public Params( ParserRuleContext ctx, StringBuilder sb)
		{
			this.context = ctx;
			beginingOfAlignemtText = 0;
			input = ctx.start.getInputStream();
			this.stringBuilder = sb;
		}
		private ParserRuleContext context;
		private CharStream input;
		private StringBuilder stringBuilder;
		//private int endOfAlignmentText;
		private int beginingOfAlignemtText;

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();

			sb.append("Context = " + context.getClass().getSimpleName() +"\n"+ context.getText()); sb.append("\n");
			sb.append("Text = "+ stringBuilder.toString()); sb.append("\n");
			sb.append("start ="+beginingOfAlignemtText); sb.append("\n");
			//sb.append("end = "+endOfAlignmentText);sb.append("\n");
			return sb.toString();
		}

	}
	//getFormattedText() app
	@Override
	public String getFormattedText(){
		StringBuilder sb = new StringBuilder();
		if(getContext() == null) return sb.toString();
		Params params = new Params(getLatestContext(getContext()), sb);
		params.setBeginingOfAlignemtText(getLatestContext(getContext()).start.getStartIndex());
		getFormattedText(params);
		//logger.debug("output =\n" + sb.toString());
		return sb.toString();
	}

	protected void getFormattedText(Params params){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(childCtx instanceof TerminalNode){
					printTerminalNode((TerminalNode)childCtx,params);
				}
				else if((childCtx!=null) && (childCtx.getText().length() >0)){
					params.setContext((ParserRuleContext)childCtx);
					if(getExtendedContext(childCtx)!=null){
						Params thisCtxParams = getExtendedContext(childCtx).getUpdatedParams(params);
						getExtendedContext(childCtx).getFormattedText(thisCtxParams);
					} else {
						params.setBeginingOfAlignemtText(params.getContext().stop.getStopIndex()+1);
					}
				}
			}
			getUpdatedParams(params);
		}
	}

	/*
	 * check if they are pointing to the same char stream, else it resets the 
	 * params with the new char stream params.
	 */

	private Params getUpdatedParams(Params params) {

		if ( getContext() == null)
		{
			//The item is removed during the transformation, hence skip its contents.
			String alignmentText = params.getInput().getText(new Interval(params.beginingOfAlignemtText, params.getContext().start.getStartIndex()-1));
			params.getStringBuilder().append(alignmentText);
			params.setBeginingOfAlignemtText(params.getContext().stop.getStopIndex() + 1); 
			return null;
		}
		if (getContext().start.getInputStream() != params.getContext().start.getInputStream())
		{
			/*
			 * advance the  begining of  alignment text, as we are going to consider 'mostRecentContext' in its place.
			 */
			if ( params.beginingOfAlignemtText  <  params.getContext().start.getStartIndex())
			{
				String alignmentText = params.getInput().getText(new Interval(params.beginingOfAlignemtText, params.getContext().start.getStartIndex()-1));
				params.getStringBuilder().append(alignmentText);
			}
			params.setBeginingOfAlignemtText(params.getContext().stop.getStopIndex() + 1); 
			return new Params(getContext(),params.getStringBuilder());
		}
		else
		{
			if (getContext().parent == null)
			{
				String alignmentText = params.getInput().getText(new Interval(params.beginingOfAlignemtText, params.getContext().start.getInputStream().size()));
				params.getStringBuilder().append(alignmentText);
			}
			params.setContext(getContext());
			return params;
		}
	}


	private void printTerminalNode(TerminalNode node,Params params){
		CharStream input = params.getContext().start.getInputStream();
		if(node.getText().equals("<EOF>")){
			String end = input.getText(new Interval(params.getBeginingOfAlignemtText(),input.size()));
			params.getStringBuilder().append(end);
		} else {
			if(params.getBeginingOfAlignemtText() < node.getSymbol().getStartIndex()){
				Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),node.getSymbol().getStartIndex()-1);
				String alignmentText = input.getText(alignmentTextInterval);
				params.getStringBuilder().append(alignmentText);
			}
			params.getStringBuilder().append(node.getText());
			params.setBeginingOfAlignemtText(node.getSymbol().getStopIndex()+1);
		}
	}


	public void collectInfo(StatesInfo si,HeadersInfo hi){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).collectInfo(si,hi);
				}
			}
		}
	}

	public void getNode(Node node,StatesInfo si){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getNode(node,si);
				}
			}
		}
	}

	protected void getCases(List<Pair<List<String>, String>> cases) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getCases(cases);
				}
			}
		}
	}

	public void getVariablesForHeaderClass(List<ST> sts){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getVariablesForHeaderClass(sts);
				}
			}
		}
	}

	protected String getName(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					String out = getExtendedContext(childCtx).getName();
					if(out != null){
						return out;
					}
				}
			}
		}
		return null;
	}

	public void getFields(List<Field> fields){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getFields(fields);
				}
			}
		}
	}

	public void preProcess(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).preProcess();
				}
			}
		}
	}

	protected String getSize(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					String out = getExtendedContext(childCtx).getSize();
					if(out != null){
						return out;
					}
				}
			}
		}
		return null;
	}

	protected String getPrefixedType(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					String out = getExtendedContext(childCtx).getPrefixedType();
					if(out != null){
						return out;
					}
				}
			}
		}
		return null;
	}

	public Boolean hasPrefixedType(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					Boolean out = getExtendedContext(childCtx).hasPrefixedType();
					if(out){
						return out;
					}
				}
			}
		}
		return false;
	}

	protected String getLookaheadValue(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					String out = getExtendedContext(childCtx).getLookaheadValue();
					if(out != null){
						return out;
					}
				}
			}
		}
		return null;
	}

	public void getAddedVariablesForHeaderClass(List<ST> sts){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getAddedVariablesForHeaderClass(sts);
				}
			}
		}
	}


	//packetGen end

	//SAMEEK
	public void buildST(TableMap tm){
		ParserRuleContext ctx = getContext();
		//System.out.println(ctx.getText() );
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).buildST(tm);
				}
			}
		}
	}

	//SAMEEK
	public void populateParser(Transitions tm){
		ParserRuleContext ctx = getContext();
		//System.out.println(ctx.getText() );
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateParser(tm);;
				}
			}
		}
	}

	//SAMEEK
	public void buildTypes(P4Headers headers){
		ParserRuleContext ctx = getContext();
		//System.out.println(ctx.getText() );
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).buildTypes(headers);
				}
			}
		}
	}

	//SAMEEK
	public void buildParsers(P4Headers headers, P4Parsers parsers){
		ParserRuleContext ctx = getContext();
		//System.out.println(ctx.getText() );
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).buildParsers(headers, parsers);
				}
			}
		}
	}

	//POOJA
	public void unroll(Map<String, ControlDeclarationContextExt> controlBlocks,
			List<ControlLocalDeclarationContextExt> controlLocalDeclarations,
			List<ParserRuleContext> applyStatements) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).unroll(controlBlocks, controlLocalDeclarations, applyStatements);
				}
			}
		}
	}

	public void getControlLocalDeclarationsContextExt(List<ControlLocalDeclarationContextExt> controlLocalDeclarations)
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getControlLocalDeclarationsContextExt(controlLocalDeclarations);
				}
			}
		}
	}

	public void getInstantiations(Map<String, InstantiationContext> instantiations){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getInstantiations(instantiations);
				}
			}
		}
	}

	public void getArgumentListExpressionNonTypeName(List<String> argumentListExpressionNonTypeNames)
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getArgumentListExpressionNonTypeName(argumentListExpressionNonTypeNames);
				}
			}
		}
	}


	//GL - ControlBlock START

	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getControlBlocks(controlBlocks);
				}
			}
		}
	}
	
	public void getPackages(Map<String, PackageTypeDeclarationContextExt> packages) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getPackages(packages);
				}
			}
		}
	}

	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getTables(tables);
				}
			}
		}
	}

	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getActions(actions);
				}
			}
		}
	}
	
	public void getStructs(Map<String, StructTypeDeclarationContextExt> structTypeDeclarations) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getStructs(structTypeDeclarations);
				}
			}
		}
	}
	
	public void getHeaders(Map<String, HeaderTypeDeclarationContextExt> headerTypeDeclarations) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getHeaders(headerTypeDeclarations);
				}
			}
		}
	}
	
	public void getFunctionPrototypes(Map<String, FunctionPrototypeContextExt> functionPrototypes) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getFunctionPrototypes(functionPrototypes);
				}
			}
		}
	}
	
	public void getExternObjects(Map<String, ExternObjectDeclarationContextExt> externObjects) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getExternObjects(externObjects);
				}
			}
		}
	}

	public static enum StatementType{ 
		TYPE_ERROR,
		VARIABLE_DECLARATION_STATEMENT,
		CONSTANT_DECLARATION_STATEMENT,
		INSTANTIATION_STATEMENT,
		BLOCK_STATEMENT,
		ASSIGNMENT_STATEMENT,
		METHODCALL_STATEMENT,
		CONDITIONAL_STATEMENT,
		RETURN_STATEMENT,
		EXIT_STATEMENT,
		SWITCH_STATEMENT,
		EMPTY_STATEMENT,
	}

	public void getStatements(Map<StatementOrDeclarationContextExt,StatementType> stmts) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getStatements(stmts);
				}
			}
		}
	}


	public StatementType getStatementType()
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					StatementType statementType = getExtendedContext(childCtx).getStatementType();
					if (statementType != StatementType.TYPE_ERROR)
						return statementType;
				}
			}
		}
		return StatementType.TYPE_ERROR;
	}


	public void encode()
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).encode();
				}
			}
		}
	}

	public void populateALUInstructions(List<AluInstruction> instructions)
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateALUInstructions(instructions);
				}
			}
		}
	}
	public void populateTableApplySequence(List<ApplyMethodCallContextExt> tableApplyCalls) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateTableApplySequence(tableApplyCalls);
				}
			}
		}

	}



	public int getStartingAddress(){
		return Integer.MIN_VALUE;
	}

	//GL - ControlBlock END

	public void preAllocateGlobalAddress(Set<IMemoryRequest> symbolSet){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null) {
						getExtendedContext(childCtx).preAllocateGlobalAddress(symbolSet);
					}
				}
			}
		}
	}


	//SAMEEK
	public void allocateGlobalAddress(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null) {
						getExtendedContext(childCtx).allocateGlobalAddress();
					}
				}
			}
		}
	}

	//SAMEEK
	public void setIds(AtomicInteger controlId, AtomicInteger tableId, AtomicInteger actionId, AtomicInteger instId){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null) {
						getExtendedContext(childCtx).setIds(controlId,tableId, actionId, instId);
					}
				}
			}
		}
	}

	//SAMEEK
	public int getControlId(){
		throw new IllegalStateException();
	}
	public int getTableId(){
		throw new IllegalStateException();
	}

	public void setKeys(KeyMeta k){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).setKeys(k);
				}
			}
		}
	}

	public void setSelectOffsets(Map<String, Integer[]> map) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).setSelectOffsets(map);
				}
			}
		}
	}

	public void populateTableActionsAndData(Map<String,List<Integer>> map) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateTableActionsAndData(map);
				}
			}
		}
	}

	public void populateParams(List<Integer> list) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateParams(list);
				}
			}
		}
	}

	public void populateTableActionsMap(Map<String,TableMeta> map) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateTableActionsMap(map);
				}
			}
		}
	}

	public void populateActionsList(TableMeta meta) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).populateActionsList(meta);
				}
			}
		}
	}
	//SAMEEK
	//CFG Related Code
	public void invokeProgramFlow(List<String> flowNodes){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null){
						getExtendedContext(childCtx).invokeProgramFlow(flowNodes);
					}
				}
			}
		}
	}

	@Getter
	protected Set<CFGNode> nextCFGNodes = new LinkedHashSet<>();
	@Getter
	protected CFGNode jumpNode = null;
	public boolean isJumpNode(){
		return jumpNode!=null;
	}
	@Getter
	protected CFGNode tableApplyNode = null;
	public boolean isTableApplyNode(){
		return tableApplyNode!=null;
	}

	@Getter
	protected CFGBuildingBlock cFGBuildingBlock=null;

	public CFGBuildingBlock buildNGetCFG(CFGMap cfgmap){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null) {
						getExtendedContext(childCtx).buildNGetCFG(cfgmap);
					}
				}
			}
		}
		return null;
	}
	@Override
	public String cFGNodeSummary(){
		return this.getFormattedText();
	}

	//END CFG

	//Type checking -start

	//Default Implementation. This may vary based on specific Operation of different types and is usually overridden in specific "Type" subclass wherever needed
	@Override
	public int getTypeSize(){
		return this.getSizeInBits();
	}


	//Default Implementation returns true if both the "Type"s are equal. e.g., a+a. But it is usually overridden in specific subclasses of "Type" to enforce custom rules on type compatibility
	@Override 
	public boolean isTypeCompatible(Type type1, Type type2){
		if (type1.equals(type2)){
			return true;
		}
		return false;
	}
	
	//Default Implementation returns true if both the "this" type and "type" type are compatible for 
	//equivalence. e.g., a = a. But it is usually overridden in specific subclasses of "Type" to enforce custom rules on equivalence type compatibility
	//If this method is not overridden in the specific type, then this will just check the type name and type sizes, thus the type check will be at risk of not doing
	//checks in special cases like say, for example, handling implicit typecasts.
	
	@Override 
	public boolean isEquivalenceCompatible(Type that){
		if (this.getTypeName().equals(that.getTypeName()) && this.getTypeSize() == that.getTypeSize()){
			return true;
		}
		return false;
	}

	//Type checking -end

	/*=============================================================================
		Scope Interface.
		public String getScopeName();
	    public <T extends Scope> T getScope();
	    public <T extends Scope> T getEnclosingScope();
	    public void add(Symbol symbol);
	    public <T extends Scope> T createScope();
	    public Symbol lookup(Object key);
=============================================================================*/

	@Override
	public String getScopeName()
	{
		throw new UnsupportedOperationException("getSymbolInsertionOrderNumber");
	}

	@Override
	public Symbol getScope()
	{
		if ( this.symbolTable != null)
		{
			return (Symbol)this;
		}
		return null;
	}

	protected Scope enclosingScope;
	@Override
	public Scope getEnclosingScope()
	{
		if(this.enclosingScope != null) return this.enclosingScope;
		AbstractBaseExt parentExtCtx = getParent();
		if (parentExtCtx != null){

			if(parentExtCtx.getScope() != null) {
				return parentExtCtx.getScope();
			}
			else {
				return parentExtCtx.getEnclosingScope();
			}
		}
		else
			return null;
	}


	@Override
	public void add(Symbol symbol){
		if(!this.symbolTable.containsKey(symbol.getSymbolKey()) )
		{
			//L.info("Putting into symbol table: "+symbol.getSymbolKey()+" :"+this.getClass().toString());
			this.symbolTable.put(symbol.getSymbolKey(),symbol);
		}
		//assert(value == null);
	}

	protected  Map<Object, Symbol>symbolTable = null;
	@Override
	public Scope createScope()
	{
		this.symbolTable = new LinkedHashMap<Object,Symbol>();
		return (Scope)this;
	}

	@Override
	public Symbol lookup(Object key){
		return this.symbolTable.get(key);
	}

	/*============================================================================
    Symbol Interface
    String getSymbolName();
    <T extends Object> T getKey(Symbol symbol);
    public <T extends Scope> void defineSymbol(AtomicReference<T> enclosingScope);
    public Symbol resolve(String name);
	int getSymbolInsertionOrderNumber(); // index showing insertion order from 0

    public int getByteOffset();
    public int getBitOffset();
=============================================================================*/

	@Override
	public String getSymbolName()
	{
		throw new UnsupportedOperationException("getSymbolName: "+getFormattedText());
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public Object getSymbolKey()
	{
		return getSymbolName();
	}

	@Override
	public void defineSymbol(AtomicReference<Scope> enclosingScope) throws NameCollisionException 
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx) != null)
						getExtendedContext(childCtx).defineSymbol(enclosingScope);
				}
			}
		}
	}


	private Pair<String,String> getSymbolKey(String symbolName, String scopeName)
	{	
		assert( symbolName != null && scopeName != null);
		return new Pair<String,String>(symbolName,scopeName);
	}


	private  Symbol resolve1(String SymbolName){

		//To resolve symbols like hdr.mpls[2].label to hdr.mpls.label.
		String modSymbolName = SymbolName.replaceAll("\\[.*?\\]", "");

		Scope scope = getScope();
		if (scope == null)
			scope = getEnclosingScope();
		while (true)
		{
			if(scope == null)
			{
				throw new SymbolNotDefinedException(modSymbolName);
			}
			if (scope.lookup(modSymbolName) != null)
			{
				return scope.lookup(modSymbolName);
			}
			scope = scope.getEnclosingScope();
		}
	}

	@Override
	public Symbol resolve(String symbolName)
	{
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = resolve1(symbolNameParts[0]);
		for(int i=1; i < symbolNameParts.length;i++)
		{
			symbol = symbol.getType();
			symbol = symbol.resolve(symbolNameParts[i]);
		}
		return symbol;
	}

	public  int getSymbolInsertionOrderNumber()
	{
		throw new UnsupportedOperationException("getSymbolInsertionOrderNumber");
	}; 

	protected Integer bitOffset=0;
	@Override
	public int getBitOffset() {
		ParserRuleContext ctx = getContext();
		int bitOffset = -1;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					bitOffset = getExtendedContext(childCtx).getBitOffset();
					if (bitOffset != -1) return bitOffset;
				}
			}
		}
		return bitOffset;
	}

	protected Integer byteOffset=0;
	@Override
	public int getByteOffset(){
		ParserRuleContext ctx = getContext();
		int byteOffset = -1;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					byteOffset = getExtendedContext(childCtx).getByteOffset();
					if (byteOffset != -1) return byteOffset;
				}
			}
		}
		return byteOffset;
	}

	protected Integer alignedByteOffset;
	@Override
	public int getAlignedByteOffset(){
		ParserRuleContext ctx = getContext();
		int alignedByteOffset = -1;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					alignedByteOffset = getExtendedContext(childCtx).getAlignedByteOffset();
					if (alignedByteOffset != -1) return alignedByteOffset;
				}
			}
		}
		return alignedByteOffset;
	}

	protected Integer validBitOffset;
	@Override
	public int getValidBitOffset()
	{
		return Integer.MIN_VALUE;
	}


	/*=============================================================================
    Type Interface
    public String getTypeName();
    public Type getType();
	public boolean isSameType(Type type);
	public int getSizeInBits();
	public int getSizeInBytes();
==============================================================================*/
	@Override
	public String getTypeName(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if (getExtendedContext(childCtx).getTypeName() != null){
						return getExtendedContext(childCtx).getTypeName();
					}
				}
			}
		}
		return null;
	}
	@Override
	public Symbol getSymbol() {
		return (Symbol)this;
	}


	@Override
	public Type getType() {
		//I am a symbol, so resolve the symbol and get the type.
		Scope enclosingScope = getEnclosingScope();
		Symbol declarationSymbol = enclosingScope.resolve(getSymbolName());
		Type type = (Type)declarationSymbol.resolve(declarationSymbol.getTypeName());
		return type;
	}

	@Override
	public Boolean isSame(Type type){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if (getExtendedContext(childCtx).getTypeName() != null){
						Boolean result =  getExtendedContext(childCtx).isSame(type);
						if( result != null) return result;
					}
				}
			}
		}
		return null;
	}


	@Override
	public int getSizeInBits() {
		ParserRuleContext ctx = getContext();
		int size = 0;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					size += getExtendedContext(childCtx).getSizeInBits();
				}
			}
		}
		return size;
	}

	@Override
	public int getSizeInBytes() {
		ParserRuleContext ctx = getContext();
		int size = 0;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					size += getExtendedContext(childCtx).getSizeInBytes();
				}
			}
		}
		return size;
	}

	@Override
	public int getAlignedSizeInBytes() {
		ParserRuleContext ctx = getContext();
		int alignedSize = 0;
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					alignedSize += getExtendedContext(childCtx).getAlignedSizeInBytes();
				}
			}
		}
		return alignedSize;
	}


	/*============================================================================
    DerivedType Interface
    public boolean hasMember(String memberName);
	public Symbol getMember(String memberName);
=============================================================================*/

	@Override
	public boolean hasMember(String memberName)
	{
		Scope scope  = getScope();
		assert(scope != null);
		return (scope.lookup(memberName) != null);

	}
	@Override
	public Symbol getMember(String memberName){
		Scope scope  = getScope();
		assert(scope != null);
		return scope.lookup(memberName);
	}
	public int getMemberBitOffset(String memberName)
	{
		Scope scope  = getScope();
		assert(scope != null);
		Symbol member = scope.lookup(memberName);
		return member.getBitOffset();
	}
	public int getMemberByteOffset(String memberName)
	{
		Scope scope  = getScope();
		assert(scope != null);
		Symbol member = scope.lookup(memberName);
		return member.getByteOffset();
	}


	/*============================================================================
    Symbol Utils START
    	private  printScope();
    	private setBitOffset()
    	private SetByteOffset()
=============================================================================*/


	protected Type getType1()
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					Type t = getExtendedContext(childCtx).getType1();
					if( t != null ) return t;
				}
			}
		}
		return null;
	}

	protected  void setBitOffset(AtomicInteger bitOffset) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).setBitOffset(bitOffset);
				}
			}
		}
	}

	protected void setByteOffset(AtomicInteger bitOffset) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					//System.out.println("structfieldtype="+childCtx.getClass().getName());
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).setByteOffset(bitOffset);
				}
			}
		}
	}

	protected void setAlignedByteOffset(AtomicInteger alignedByteOffset) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					//System.out.println("structfieldtype="+childCtx.getClass().getName());
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).setAlignedByteOffset(alignedByteOffset);
				}
			}
		}
	}

	public void printScope(){
		System.out.println(this.getClass().getName());
		try{
			String symbolName  = getSymbolName();
			if ( symbolName != null)
			{
				Scope enlcosingScope  = getEnclosingScope();
				Symbol symbol =  resolve(symbolName);
				System.out.println( "\nCONTEXT = " + getFormattedText());
				System.out.println( "SymbolName = " + symbolName);
				Type type = getType();
				System.out.println( "TypeName = " + type.getTypeName());
				System.out.println( "SizeInBits = " + type.getSizeInBits() );
				System.out.println( "SizeInBytes = " + type.getSizeInBytes() );
			}
		}
		catch(UnsupportedOperationException ex)
		{
			if(!ex.getMessage().equals("getSymbolName"))
			{
				System.out.println(ex.getMessage());
			}
		}
		catch(SymbolNotDefinedException ex)
		{
			if(!ex.getMessage().equals("getSymbolName"))
			{
				System.out.println(ex.getMessage());
			}
		}
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).printScope();
				}
			}
		}
		System.out.flush();
	}

	public int getSizeInBits(String symbolName)
	{
		Symbol symbol = resolve(symbolName);
		Type type = symbol.getType();
		return type.getSizeInBits();
	}

	public int getSizeInBytes(String symbolName)
	{
		Symbol symbol = resolve(symbolName);
		Type type = symbol.getType();
		return type.getSizeInBytes();
	}

	public int getAlignedSizeInBytes(String symbolName)
	{
		Symbol symbol = resolve(symbolName);
		Type type = symbol.getType();
		return type.getAlignedSizeInBytes();
	}

	/*
	 * It should always get the type.
	 */
	public int getBitOffset(String symbolName)
	{
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = this;
		int bitOffset = 0;
		for(int i=0;i<symbolNameParts.length;i++)
		{
			if ( symbolNameParts[i].equals("isValid"))
			{
				return symbol.getValidBitOffset();
			}
			symbol = symbol.resolve(symbolNameParts[i]);
			bitOffset += symbol.getBitOffset();
			symbol = symbol.getType();
		}
		return bitOffset;
	}

	public int getByteOffset(String symbolName)
	{
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = this;
		int bitOffset = 0;
		for(int i=0;i<symbolNameParts.length;i++)
		{
			if ( symbolNameParts[i].equals("isValid"))
			{
				return symbol.getValidBitOffset();
			}
			symbol = symbol.resolve(symbolNameParts[i]);
			bitOffset += symbol.getByteOffset();
			symbol = symbol.getType();
		}
		return bitOffset;
	}



	public int getAlignedByteOffset(String symbolName)
	{
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = this;
		int alignedByteOffset = 0;
		for(int i=0;i<symbolNameParts.length;i++)
		{
			if ( symbolNameParts[i].equals("isValid"))
			{
				return symbol.getValidBitOffset();
			}
			symbol = symbol.resolve(symbolNameParts[i]);
			alignedByteOffset += symbol.getAlignedByteOffset();
			symbol = symbol.getType();
		}
		return alignedByteOffset;
	}
	/*============================================================================
    Symbol Utils END
    	private  printScope();
    	private setBitOffset()
    	private SetByteOffset()
=============================================================================*/

	/*============================================================================
	 * 	Target START
	 *===========================================================================*/
	@Override
	public MemoryType getMemoryType()
	{
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					MemoryType memoryType = getExtendedContext(childCtx).getMemoryType();
					if ( memoryType != null) return memoryType;
				}
			}
		}
		return null;
	}
	@Override
	public IMemoryManager getMemoryManager()
	{
		return com.p4.drmt.MemoryManager.getInstance();
	}

	public void resetMemoryBuffer() {
		this.memoryManager.resetMemoryBuffer();
	}
	/*============================================================================
	 * 	Target END
	 *===========================================================================*/		
	/*
	 * A method to construct StatementOrDeclaration Context from a String;
	 */

	public static StatementOrDeclarationContextExt getStatementOrDeclaration(String input){
		StatementOrDeclarationContextExt statementOrDeclarationContextExt = new StatementOrDeclarationContextExt(null);
		StatementOrDeclarationContext ctx = statementOrDeclarationContextExt.getContext(input);
		statementOrDeclarationContextExt.setContext(ctx);
		return statementOrDeclarationContextExt;
	}

	public static VariableDeclarationContextExt getDeclaration(String input){
		VariableDeclarationContextExt variableDeclarationContextExt = new VariableDeclarationContextExt(null);
		VariableDeclarationContext ctx = variableDeclarationContextExt.getContext(input);
		variableDeclarationContextExt.setContext(ctx);
		return variableDeclarationContextExt;
	}

	public static AssignmentStatementContextExt getAssignment(String input){
		AssignmentStatementContextExt assignmentStatementContextExt = new AssignmentStatementContextExt(null);
		AssignmentStatementContext ctx = assignmentStatementContextExt.getContext(input);
		assignmentStatementContextExt.setContext(ctx);
		return assignmentStatementContextExt;
	}

	public static ExpressionContextExt getExpression(String input){
		//TODO A hack to circumvent the grammar implementation
		ArgumentContextExt argumentContextExt = new ArgumentContextExt(null);
		ArgumentContext actx = argumentContextExt.getContext(input);
		argumentContextExt.setContext(actx);
		return (ExpressionContextExt) getExtendedContext(argumentContextExt.getContext().expression());
	}

	public static StatementContextExt getStatement(String input){
		StatementContextExt stmtContextExt = new StatementContextExt(null);
		StatementContext stmtctx = stmtContextExt.getContext(input);
		stmtContextExt.setContext(stmtctx);
		return stmtContextExt;
	}

	public static BlockStatementContextExt getBlockStatement(String input){
		BlockStatementContextExt stmtContextExt = new BlockStatementContextExt(null);
		BlockStatementContext stmtctx = stmtContextExt.getContext(input);
		stmtContextExt.setContext(stmtctx);
		return stmtContextExt;
	}

	/*
	 * Quadruplizing expressions
	 * Returns a list of variable declarations and simple expressions 
	 * Flag determines if returned simple expression must be a terminal or not
	 * */

	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){

		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).quadrupalize(probe,statementOrDeclarationContextExts);
				}
			}
		}
	}

	protected String predicateSymbol = null;
	public void removeElse(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).removeElse(statementOrDeclarationContextExts, branchStack);
				}
			}
		}
	}

	public void getExtracts(List<P4Extract> extracts, P4Headers hdr) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getExtracts(extracts, hdr);
				}
			}
		}
	}

	public void assignPredicate(Stack<ExpressionContextExt> stack){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).assignPredicate(stack);
				}
			}
		}
	}



	public void getAssigns(List<P4Assign> assigns) {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).getAssigns(assigns);
				}
			}
		}
	}

	public void printInstruction(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).printInstruction();
				}
			}
		}
	}

	protected static Scope globalScope;

	@Getter
	protected static Map<String,AbstractBaseExt> constants = new LinkedHashMap<>();

	public FW getFW()
	{
		return null;
	}

	public void flattenIfStatements() {
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					getExtendedContext(childCtx).flattenIfStatements();
				}
			}
		}
	}

	public void updatePacketVector(BitSet packetVector, BitSet value, int offset, int size) {
		for(int index=0; index<size; index++) {
			if(value.get(index))
				packetVector.set(offset + index);
			else
				packetVector.clear(offset + index);
		}
	}

	public void listGlobalAddresses(GlobalAddress ga, List<String> fields){
		for(String field:fields){
			int offset = getAlignedByteOffset(field);
			int size = field.endsWith("isValid")?1:getSizeInBits(field);
			L.debug(field+", "+offset);
			//TODO BAD Hack
			int mult = field.endsWith("isValid")?1:8;
			ga.getFields().put(field, mult*offset);
			ga.getSizes().put(field, size);
		}
	}

	@Override
	public List<IIRNode> getChildrenIncludingTerminalNodes() {
		List<IIRNode> children = new ArrayList<>();
		ParserRuleContext context = getContext();
		if (context != null && context.children != null && context.children.size() > 0) {
			for (ParseTree childCtx : context.children) {
				AbstractBaseExt extendedContext = getExtendedContext(childCtx);
				if (extendedContext != null) {
					children.add(extendedContext);
				} else if (childCtx instanceof TerminalNode) {
					children.add(new IRTerminalNode(childCtx.getText()));
				}
			}
		}
		return children;
	}

	@Override
	public List<IIRNode> getChildren() {

		List<IIRNode> children = new ArrayList<>();
		ParserRuleContext context = getContext();
		if (context != null && context.children != null && context.children.size() > 0) {
			for (ParseTree childCtx : context.children) {
				AbstractBaseExt extendedContext = getExtendedContext(childCtx);
				if (extendedContext != null) {
					children.add(extendedContext);
				}
			}
		}
		return children;
	}

	@Override
	public IIRNode getParentIRNode() {
		ParserRuleContext parent = getContext().getParent();
		return parent != null ? getExtendedContext(parent) : null;
	}

	public void visitNode(IIRNodeVisitor visitor){
		visitor.visitNode(this);
		for (IIRNode child : getChildren()) {
			child.visitNode(visitor);
		}
	}

	@Override
	public void postVisitNode(IIRNodeVisitor visitor) {
		for (IIRNode child : getChildren()) {
			child.postVisitNode(visitor);
		}
		visitor.visitNode(this);
	}

	@Override
	public void postVisitNode(IIRNodeVisitor visitor, Function<IIRNode, Boolean> canVisitChildren) {
		if (canVisitChildren.apply(this)) {
			for (IIRNode child : getChildren()) {
				child.postVisitNode(visitor, canVisitChildren);
			}
		}
		visitor.visitNode(this);
	}

	@Override
	public boolean visitNode(Function<IIRNode, Boolean> function) {
		Boolean applyFurther = function.apply(this);
		if (applyFurther) {
			for (IIRNode child : getChildren()) {
				child.visitNode(function);
			}
		}
		return applyFurther;
	}

	public boolean visitFirstNode(Function<IIRNode, Boolean> function) {
		Boolean applyFurther = function.apply(this);
		if (applyFurther) {
			for (IIRNode child : getChildren()) {
				if (!child.visitFirstNode(function)) {
					break;
				}
			}
		}
		return applyFurther;
	}

	@Override
	public void enterExitNode(Consumer<IIRNode> enterConsumer, Consumer<IIRNode> exitConsumer) {
		enterConsumer.accept(this);
		for (IIRNode child : getChildren()) {
			child.enterExitNode(enterConsumer, exitConsumer);
		}
		exitConsumer.accept(this);
	}

	@Override
	public boolean isTerminalNode() {
		return (getContext() instanceof TerminalNode);
	}

	public AbstractBaseExt replaceHeaderValid(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0) {
			for (ParseTree childCtx : ctx.children) {
				if (!(childCtx instanceof TerminalNode)) {
					getExtendedContext(childCtx).replaceHeaderValid();
				}
			}
		}
		return this;
	}

	public void replaceVd(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0) {
			for (ParseTree childCtx : ctx.children) {
				if (!(childCtx instanceof TerminalNode)) {
					getExtendedContext(childCtx).replaceVd();
				}
			}
		}
	}
	/*
	 *	curOff is aligned according to sizeInBytes.
	 *	If sizeinBytes are greater than 3, curOff is aligned to 4 byte boundary.
	 *  If sizeinBytes are equal to 3, curOff is aligned to 2 byte boundary.
	 *  Otherwise curOffset remains as it is.
	 *  The return value is the increment needed to align the address value
	 *  curOff to byte boundary. The caller adds the increment value and assigns
	 *  the address to byteOffset. This is required so that it works also for
	 *  AtomicInteger semantics like addAndGet and getAndAdd.
	 *
	 */
	public int alignMem(int curOff, int sizeInBytes) {
		int inc = 0;
		if (sizeInBytes >= 3) {
			int rem = curOff % 4;
			if (rem != 0)
				inc = 4 - rem;
		}
		if (sizeInBytes == 2) {
			int rem = curOff % 2;
			if (rem != 0) inc = 2 - rem;
		}
		return inc;
	}

	public static IField getFieldOf(AbstractBaseExt ctx, String fieldName, int size, Target.MemoryType type){
		//int offset = ctx.getAlignedByteOffset(fieldName);
		Symbol symbol = ctx.resolve(fieldName);
		return new CField(size, type, fieldName, (Type)symbol, 0);
	}

	public static IField getFieldOf(AbstractBaseExt ctx, String fieldName, int size){
		//int offset = ctx.getAlignedByteOffset(fieldName);
		Symbol symbol = ctx.resolve(fieldName);
		Target.MemoryType type = ((AbstractBaseExt)symbol).getMemoryType();
		return new CField(size, type, fieldName, (Type)symbol, 0);
	}

	public static IField getFieldOf(AbstractBaseExt ctx, String fieldName){
		//int offset = ctx.getAlignedByteOffset(fieldName);
		Symbol symbol = ctx.resolve(fieldName);
		int size = symbol.getSizeInBits();
		Target.MemoryType type = ((AbstractBaseExt)symbol).getMemoryType();
		return new CField(size, type, fieldName, (Type)symbol, 0);
	}

	public static IField getPredicateField(AbstractBaseExt ctx, String fieldName){
		//int offset = ctx.getAlignedByteOffset(fieldName);
		Symbol symbol = ctx.resolve(fieldName);
		int size = symbol.getSizeInBits();
		return new CField(size, MemoryType.TypePktBit, fieldName , (Type)symbol, 0);
	}

	public void collectAllAssignmentOperations(List<IALUOperation> aluOperations){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).collectAllAssignmentOperations(aluOperations);
				}
			}
		}
	}



	protected boolean isSemanticChecksPass() {
		return semanticChecksPass.isSemanticChecksPass();
	}
	
	public void runSemanticChecks(){
		try{
			this.getSemanticChecksPass().init();
			this.defineSymbol(null);
			for (P416BaseVisitor<ParserRuleContext> visitor: semanticChecksPass.getSemanticCheckVisitors()){
				visitor.visit(this.getContext());
			}
			this.getSemanticChecksPass().complete();
		}
		catch(Throwable t){
			L.error("Unhandled issue while running the semantic checks: "+t.getMessage());
			t.printStackTrace();
		}
	}

	public void runArchitecturalModel(){
		for (P416BaseVisitor<ParserRuleContext> visitor: architecturalModel.getArchitecturalModelVisitors()){
			visitor.visit(this.getContext());
		}
	}

	public void getTransitionCases(Map<String, String> transitionCases){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).getTransitionCases(transitionCases);
				}
			}
		}
	}

	public void inlineParserStates(List<IParserState> inlinedParserStates, List<List<IParserState>>  loopPaths, List<String> parseStatesInUnionStack){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).inlineParserStates(inlinedParserStates, loopPaths, parseStatesInUnionStack);
				}
			}
		}
	}

	public Boolean hasNextInStateExpression(){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null){
						Boolean hasNext = getExtendedContext(childCtx).hasNextInStateExpression();
						if(hasNext) return hasNext;
					}
				}
			}
		}
		return false;
	}

	// Gets the relative offset in bits of member inside container.
	// Container is known by it is typename like ethernet_t or ipv4_t.
	public int getMemberRelativeOffset(String container,String member){
		// below 2 lines if instance is passed in place of container like "hdr.ethernet"
		//Symbol sym=resolve(container);
		//container = sym.getTypeName();
		Pair<String,String> npair = new Pair<>(container,member);
		Pair<Integer,Integer> pii = fieldsMap.get(npair);
		if(pii!=null)
			return pii.first();
		return -1;
	}

	// Gets the size of member in bits given the container and the member.
	// Container is known by it is typename like ethernet_t or ipv4_t.
	public int getMemberSizeInBits(String container,String member){
		//  below 2 lines if instance is passed in place of container like "hdr.ethernet"
		//Symbol sym=resolve(container);
		//container = sym.getTypeName();
		Pair<String,String> npair = new Pair<>(container,member);
		Pair<Integer,Integer> pii = fieldsMap.get(npair);
		if(pii!=null)
			return pii.second();
		return -1;
	}


	public void inlineParserStateForNewInstances(Integer instanceNumber, String parseStateName, Integer count, List<String> parseStatesInUnionStack){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).inlineParserStateForNewInstances(instanceNumber, parseStateName, count, parseStatesInUnionStack);
				}
			}
		}
	}

	public void inlineParentNameInTransition(List<IParserState> fullyInlinedParseStates, List<String> parseStatesWithLoop){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).inlineParentNameInTransition(fullyInlinedParseStates, parseStatesWithLoop);
				}
			}
		}
	}

	//Remove Switch constructs from Initial P4 program.
    public void removeSwitch(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
        ParserRuleContext ctx = getContext();
        if(ctx != null && ctx.children != null && ctx.children.size()>0){
            for(ParseTree childCtx : ctx.children){
                if(!(childCtx instanceof TerminalNode)){
                    getExtendedContext(childCtx).removeSwitch(statementOrDeclarationContextExts, branchStack);
                }
            }
        }
    }

    public String replaceNextOrLast(Integer instanceNumber){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null){
						String replacedNextOrLast = getExtendedContext(childCtx).replaceNextOrLast(instanceNumber);
						if( replacedNextOrLast!=null){
							return replacedNextOrLast;
						}
					}
				}
			}
		}
		return null;
	}

	public int getTypeByteOffset(String symbolName)
	{
		/*
		 * for non 'dot member' type varibles  getTypeByteOffset(String symbol) = getByteOffset(String symbol)
		 * for 'dot member' types getTypeByteOffset(String symbol) returns the offset of the member in that structure
		 * getTypeByteOffset("x.y.member") = offset of the member in the 'y' structure.
		 */
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = this;
		int byteOffset = 0;
		for(int i=0;i<symbolNameParts.length;i++)
		{
			if ( symbolNameParts[i].equals("isValid"))
			{
				return symbol.getValidBitOffset();
			}
			symbol = symbol.resolve(symbolNameParts[i]);
			byteOffset = symbol.getByteOffset();
			symbol = symbol.getType();
		}
		return byteOffset;

	}


	public int getTypeBitOffset(String symbolName)
	{
		String[] symbolNameParts = symbolName.split("\\.");
		Symbol symbol = this;
		int bitOffset = 0;
		for(int i=0;i<symbolNameParts.length;i++)
		{
			if ( symbolNameParts[i].equals("isValid"))
			{
				return symbol.getValidBitOffset();
			}
			symbol = symbol.resolve(symbolNameParts[i]);
			bitOffset = symbol.getBitOffset();
			symbol = symbol.getType();
		}
		return bitOffset;
	}

	//Experimental APIs. Don't use in normal flow
	//SAMEEK
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).parserStats(startState, edges, hdrs, stateKeySizes);
				}
			}
		}
	}

	//END of Experimental APIs
    
    public void getCppTransformedControlBody(){
        ParserRuleContext ctx = getContext();
        if(ctx != null && ctx.children != null && ctx.children.size()>0){
            for(ParseTree childCtx : ctx.children){
                if(!(childCtx instanceof TerminalNode)){
                    if(getExtendedContext(childCtx)!=null)
                        getExtendedContext(childCtx).getCppTransformedControlBody();
                }
            }
        }
    }

    //kotesh
	public void emitP4Info(Pi.P4Info.Builder p4Info){
		ParserRuleContext ctx = getContext();
		if(ctx != null && ctx.children != null && ctx.children.size()>0){
			for(ParseTree childCtx : ctx.children){
				if(!(childCtx instanceof TerminalNode)){
					if(getExtendedContext(childCtx)!=null)
						getExtendedContext(childCtx).emitP4Info(p4Info);
				}
			}
		}
	}
	
	@Override
	public void setNewValue(Integer num){
		this.newValue = num;
	}
	
	@Override
	public Integer getNewValue(){
		return this.newValue;
	}
	
	@Override
	public boolean getIsConstant(){
		return this.isConstant;
	}
	
	@Override
	public void setIsConstant(boolean isConstant){
		this.isConstant = isConstant;
	}
}
