package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.DeclarationContext;
import com.p4.p416.iface.*;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.InstantiationContext;
import com.p4.p416.gen.P416Parser.P4programContext;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class P4programContextExt extends AbstractBaseExt implements IP4Program {

	protected Integer sram;
	protected Integer tcam;

	public P4programContextExt(P4programContext ctx) {
		super(ctx);
	}

	@Override
	public  P4programContext getContext(){
		assert(contexts.size() > 0);
		return (P4programContext)contexts.get(contexts.size()-1);
	}

	@Override
	public P4programContext getContext(String str){
		return (P4programContext)new PopulateExtendedContextVisitor().visit(getParser(str).p4program());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof P4programContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ P4programContext.class.getName());
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
		return null;
	}


	@Override
	public String getTypeName()
	{
		return null;
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		return false;
	}

	@Override
	public Type getType()
	{
		return null;
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
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = null;
		Scope localScope = createScope();
		globalScope = localScope;
		super.defineSymbol(new AtomicReference<Scope>(localScope));
	}

	public void flatten(Map<String, ControlDeclarationContextExt> controlBlocks){
		P4programContext ctx = (P4programContext) getContext();
		Map<String, InstantiationContext> instantiations = new HashMap<String, InstantiationContext>();
		getExtendedContext(ctx).getInstantiations(instantiations);
		String instanceName = "main";
		if(instantiations.containsKey(instanceName)){
			List<String> argumentsName = new ArrayList<String>();
			getExtendedContext(instantiations.get(instanceName)).getArgumentListExpressionNonTypeName(argumentsName);
			for(String name : argumentsName){
				if(controlBlocks.containsKey(name)){
					controlBlocks.get(name).flatten(controlBlocks);
				}
			}
		}
	}

	public IP4Program getP4Program(){
		return this;
	}

	@Override
	public List<IHeader> getHeaders() {
		List<IHeader> hdrLst = new ArrayList<>();

		visitNode(node -> {
			if (node instanceof IHeader) {
				hdrLst.add((IHeader) node);
			}
		});

		return hdrLst;
	}

	@Override
	public List<IHeaderUnion> getHeaderUnions() {
		List<IHeaderUnion> headerUnionsLst = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IHeaderUnion) {
				headerUnionsLst.add((IHeaderUnion) node);
			}
		});

		return headerUnionsLst;
	}

	@Override
	public List<IStruct> getStructs() {
		List<IStruct> structsLst = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IStruct) {
				structsLst.add((IStruct) node);
			}
		});

		return structsLst;
	}

	@Override
	public List<IControl> getControls() {
		List<IControl> controls = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IControl) {
				controls.add((IControl) node);
			}
		});

		return controls;
	}

	@Override
	public List<IControl> getControlsInMain() {
		List<IControl> controls = new ArrayList<>();

		IInstantiation mainInstantiation = getMainInstantiation();
		List<IArgument> arguments = mainInstantiation.getArguments();
		for (IArgument argument : arguments) {
			if (argument.getExpression().isFunctionCall()) {
				IFunctionCall functionCall = (IFunctionCall) argument.getExpression();
				String nameString = functionCall.getExpression().getNameString();
				Symbol symbol = resolve(nameString);
				if (symbol instanceof IControl) {
					controls.add((IControl) symbol);
				}
			}
		}

		return controls;
	}

	@Override
	public List<IParser> getParsers() {
		List<IParser> parsersLst = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IParser) {
				parsersLst.add((IParser) node);
			}
		});

		return parsersLst;
	}

	@Override
	public List<IPackage> getPackages() {
		List<IPackage> packageLst = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof IPackage) {
				packageLst.add((IPackage) node);
			}
		});

		return packageLst;
	}

	@Override
	public IInstantiation getMainInstantiation() {

		List<IInstantiation> instantiations = new ArrayList<>();

		visitNode(node -> {
			if (node instanceof IInstantiation && ((IInstantiation) node).isMain()) {
				instantiations.add((IInstantiation) node);
			}
		});

		return instantiations.isEmpty() ? null : instantiations.get(0);
	}

	@Override
	public List<IInstantiation> getAllInstantiations() {
		List<IInstantiation> instantiations = new ArrayList<>();

		visitNode(node -> {
			if (node instanceof IInstantiation && !((IInstantiation) node).isMain()) {
				instantiations.add((IInstantiation) node);
			}
		});

		return instantiations;
	}

	@Override
	public List<IInstantiation> getInstantiations() {

		List<DeclarationContext> declarations = getContext().declaration();
		List<IInstantiation> instantiations = new ArrayList<>();

		for (DeclarationContext declarationContext : declarations) {
			IDeclaration declaration = (IDeclaration) getExtendedContext(declarationContext);
			IInstantiation instantiation = declaration.getInstantiation();
			if (instantiation != null && !instantiation.isMain()) {
				instantiations.add(instantiation);
			}
		}

		return instantiations;
	}

	@Override
	public IMatchKindDeclaration getMatchKindDeclaration() {
		List<IMatchKindDeclaration> matchKinds = new ArrayList<>();

		visitFirstNode(node -> {
			if (node instanceof IMatchKindDeclaration) {
				matchKinds.add((IMatchKindDeclaration) node);
				return false;
			}
			return true;
		});

		return matchKinds.isEmpty() ? null : matchKinds.get(0);

	}

	@Override
	public List<IControl> getDeparser() {
		List<IControl> controls = getControls();
		List<IControl> deParsers = new ArrayList<>();
		for (IControl control : controls) {
			List<IParameter> parameters = control.getParameters();
			for (IParameter parameter : parameters) {
				if (parameter.getType() instanceof IExternDeclaration && parameter.getFormattedText().contains("packet_out")) {
					deParsers.add(control);
				}
			}
		}
		return deParsers;
	}

	public List<ControlDeclarationContextExt> getMatchingControlBlock(String name){
		List<ControlDeclarationContextExt> ret = new ArrayList<>();
		visitNode(node -> {
			if(node instanceof  ControlDeclarationContextExt){
				ControlDeclarationContextExt controlDeclarationContextExt = (ControlDeclarationContextExt)node;
				if(controlDeclarationContextExt.getName().equals(name)){
					ret.add(controlDeclarationContextExt);
				}
			}
		});
		return ret;
	}

	// Get used struct and Header fields

	public List<String> getUsedStructFields(){
		// This can be skipped if transform2SSA is called before this method. Otherwise it internally calls
		// transform2SSA .
		getExtendedContext(getContext()).transform2SSA();
		/*
		for(Map.Entry<AbstractBaseExt,String> eachEntry: allHdrFieldsMap.entrySet()){
			String hdrFieldName=eachEntry.getValue();
			for(String s:extractFields){
				// add a "." to the extract field at the end
				String tmp=s+".";
				if(hdrFieldName.startsWith(tmp)){
					mapUsedStructFields.put((AbstractBaseExt)resolve(hdrFieldName) ,hdrFieldName);
					break;
				}
			}
		}
		 */
		List<String> allUsedFields = new ArrayList<>();
		// Convert the UsedMap to list
		for(Map.Entry<AbstractBaseExt,String> eachEntry: mapUsedStructFields.entrySet()){
			allUsedFields.add(eachEntry.getValue());
		}
		return allUsedFields;
	}

	@Override
	public void inlineParserStates(List<IParserState> inlinedParserStates, List<List<IParserState>> loopPaths, List<String> parseStatesInUnionStack){
		P4programContext ctx = (P4programContext) getContext();
		List<IStruct> structs = ((P4programContextExt)getExtendedContext(ctx)).getStructs();
		for(IStruct struct : structs){
			List<IStructFieldList> structFieldLists =  struct.getStructFieldList();
			for(IStructFieldList structFieldList : structFieldLists){
                ((StructFieldListContextExt)structFieldList).inlineStructFields();
			}
		}
		super.inlineParserStates(inlinedParserStates, loopPaths, parseStatesInUnionStack);
	}

}
