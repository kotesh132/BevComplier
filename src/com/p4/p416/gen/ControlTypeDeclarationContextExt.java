package com.p4.p416.gen;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import  org.antlr.v4.runtime.ParserRuleContext;
import  com.p4.p416.gen.P416Parser.*;
import com.p4.tools.graph.structs.TableMap;
import  com.p4.p416.*;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.utils.Utils.*;

public class ControlTypeDeclarationContextExt extends AbstractBaseExt {

	public ControlTypeDeclarationContextExt(ControlTypeDeclarationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ControlTypeDeclarationContext getContext(){
		return (ControlTypeDeclarationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ControlTypeDeclarationContext getContext(String str){
		return (ControlTypeDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).controlTypeDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ControlTypeDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ControlTypeDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	/*========================================================================
	 *   Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public String getSymbolName()
	{
		ControlTypeDeclarationContext ctx = getContext();
		NameContextExt nameContextExt = (NameContextExt)getExtendedContext(ctx.name());
		return nameContextExt.getFormattedText();
	}

	@Override
	public String getTypeName()
	{
		ControlTypeDeclarationContext ctx = getContext();
		return ctx.CONTROL().getText();
	}
	
	@Override
	public Boolean isSame(Type thatType)
	{
		return thatType.getTypeName().equals(getTypeName())
				&& thatType.getSymbolName().equals(getSymbolName());
	}

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		/* 
		 * As control block params are  blobal variables, which we symbolized them
		 * from their extended context, we skip redoing that by not processing the parametrs
		 */

		if (isSemanticChecksPass()){
			if (this.getParent() instanceof ControlDeclarationContextExt){
				super.defineSymbol(enclosingScopeRef);
			}
			else{
				this.enclosingScope = enclosingScopeRef.get();
				this.enclosingScope.add(this);
				Scope localScope = createScope();
				super.defineSymbol(new AtomicReference<Scope>(localScope));
			}
		}
		return;
	}

	/*========================================================================
	 *   Symbol, Scope & Type Interface END
	 * ======================================================================*/


	@Override
	public void buildST(TableMap tm){
		ControlTypeDeclarationContext ctx = (ControlTypeDeclarationContext) getContext();
		tm.addControlScope(getExtendedContext(ctx.name()).getFormattedText());
		super.buildST(tm);
	}

	public String getName()
	{
		ControlTypeDeclarationContext controlTypeDeclarationContext = (ControlTypeDeclarationContext) getContext();
		NameContext nameContext = getExtendedContext((NameContext) controlTypeDeclarationContext.name()).getContext();
		return nameContext.getText();
	}
	
	@Override 
	public boolean isEquivalenceCompatible(Type that){
		if (this.equals(that)){
			return true;
		}
		
		Symbol thatSymbol = this.resolve(that.getTypeName());
		if (thatSymbol!=null && (this.getTypeName().equals(thatSymbol.getTypeName()) && this.getTypeSize() == that.getTypeSize())){
			return true;
		}
		
		return false;
	}
}
