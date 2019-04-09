package com.p4.p416.gen;

import com.p4.p416.iface.IPackage;
import com.p4.p416.iface.IParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;

public class PackageTypeDeclarationContextExt extends AbstractBaseExt implements IPackage {

	public PackageTypeDeclarationContextExt(PackageTypeDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  PackageTypeDeclarationContext getContext(){
		return (PackageTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public PackageTypeDeclarationContext getContext(String str){
		return (PackageTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).packageTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof PackageTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ PackageTypeDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public String getNameString() {
		PackageTypeDeclarationContext ctx = getContext();
		return getExtendedContext(ctx.name()).getFormattedText();
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
	public void getPackages(Map<String, PackageTypeDeclarationContextExt> packages) {
		packages.put(getNameString(),this);
		return;
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (isSemanticChecksPass()){
		Scope enclosingScope = enclosingScopeRef.get();
		enclosingScope.add(this);
		
		Scope localScope = createScope();
		super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
	}
	
	@Override
	public String getSymbolName()
	{
		PackageTypeDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}
	
	@Override
	public String getTypeName(){
		return this.getContext().PACKAGE().getText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
}
