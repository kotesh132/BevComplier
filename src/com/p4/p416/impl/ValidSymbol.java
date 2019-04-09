package com.p4.p416.impl;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.HeaderTypeDeclarationContextExt;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.concurrent.atomic.AtomicReference;

public class ValidSymbol extends AbstractBaseExt {
    public ValidSymbol(ParserRuleContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
    public <T extends ParserRuleContext> T getContext() {
        return null;
    }

    @Override
    public <T extends ParserRuleContext> T getContext(String str) {
        return null;
    }

    @Override
    public <T extends ParserRuleContext> void setContext(T ctx) {

    }

    @Override
    public String getSymbolName()
    {
        return "isValid";
    }
    
    @Override
    public Type getType(){
    	return this;
    }
    
    @Override
    public String getTypeName(){
    	return "BoolType";
    }
    
    @Override
    public int getTypeSize(){
    	return this.getSizeInBits();
    }

    @Override
    public int getSizeInBits() {
        return 1;
    }

    @Override
    public int getSizeInBytes() {
        return 1;
    }

    @Override
    public int getAlignedSizeInBytes() {
        return 1;
    }

    @Override
    public int getValidBitOffset() {
        if (enclosingScope instanceof HeaderTypeDeclarationContextExt) {
            return ((HeaderTypeDeclarationContextExt) enclosingScope).getValidBitOffset();
        }else {
            return super.getValidBitOffset();
        }
    }

    @Override
    public void defineSymbol(AtomicReference<Scope> enclosingScopeRef) throws NameCollisionException {
        this.enclosingScope = enclosingScopeRef.get();
        enclosingScope.add(this);
    }
}
