package com.p4.p416.gen;

import com.p4.p416.iface.IMatchKindDeclaration;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import  com.p4.p416.gen.P416Parser.*;

public class MatchKindDeclarationContextExt extends AbstractBaseExt implements IMatchKindDeclaration {

	public MatchKindDeclarationContextExt(MatchKindDeclarationContext ctx) {
		super(ctx);
	}

	@Override
	public  MatchKindDeclarationContext getContext(){
		return (MatchKindDeclarationContext)contexts.get(contexts.size()-1);
	}

	@Override
	public MatchKindDeclarationContext getContext(String str){
		return (MatchKindDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).matchKindDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof MatchKindDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ MatchKindDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	//GL - ControlBlock -START
	@Override
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		return;
	}
	//GL - ControlBlock -END

	@Override
	public List<String> getMatchKinds() {
		List<String> matchKinds = new ArrayList<>();
		visitNode(node -> {
			if (node instanceof NameContextExt) {
				matchKinds.add(node.getFormattedText());
			}
		});

		return matchKinds;
	}
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		if (isSemanticChecksPass()){
			this.enclosingScope = enclosingScopeRef.get();
			this.enclosingScope.add(this);
			Scope localScope = createScope();
			//super.defineSymbol(new AtomicReference<Scope>(localScope));
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
	}
	
	@Override
	public String getSymbolName()
	{
		return getContext().MATCH_KIND().getText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return getContext().MATCH_KIND().getText();
	}
}
