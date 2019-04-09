package com.p4.p416.gen;

import com.p4.drmt.alu.CField;
import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.IKeyElement;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IRangeIndex;

import lombok.Data;
import lombok.EqualsAndHashCode;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.p4.p416.gen.P416Parser.*;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.p416.applications.Symbol;

@EqualsAndHashCode(callSuper=false)
@Data
public class KeyElementContextExt extends AbstractBaseExt implements IKeyElement {

	private static final Logger L = LoggerFactory.getLogger(KeyElementContextExt.class);

	protected Integer sram;
	protected Integer tcam;

	public KeyElementContextExt(KeyElementContext ctx) {
		super(ctx);
	}

	@Override
	public  KeyElementContext getContext(){
		return (KeyElementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public KeyElementContext getContext(String str){
		return (KeyElementContext)new PopulateExtendedContextVisitor().visit(getParser(str).keyElement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof KeyElementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ KeyElementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void setKeys(KeyMeta k){
		ExpressionContextExt expressionContextExt = (ExpressionContextExt)getExtendedContext(getContext().expression());
		String keyName = expressionContextExt.getFormattedText();
		keyName = keyName.replaceAll("\\(", "");
		keyName = keyName.replaceAll("\\)", "");
		CField cField = (CField) AbstractBaseExt.getFieldOf(expressionContextExt, keyName);
		L.info("Key Element,"+keyName+":"+cField.summary());
		k.addKeyMap(k.currentKeyId,cField);
		k.addTableMap(k.currentKeyId, k.tableName);
	}

	@Override
	public String getKeyName() {
		return ((IExpression)getExtendedContext(getContext().expression())).getNameString();
	}

	@Override
	public String getKeyMatchKind() {
		return getExtendedContext(getContext().name()).getFormattedText();
	}

	@Override
	public Symbol getKeySymbol() {
		IIRNode node = this;
		while (node.getParentIRNode() != null) {
			node = node.getParentIRNode();
		}

		Symbol symbol = null;

		try {
			if (node instanceof IP4Program) {
				symbol = node.resolve(getKeyName());
			} else {
				throw new Exception("Root node other than P4Program found");
			}
		} catch (Exception e) {
			L.error(e.getMessage());
			e.printStackTrace();
		}

		return symbol;
	}

	@Override
	public boolean isBitSliced() {
		AbstractBaseExt expression = getExtendedContext(getContext().expression());
		return expression instanceof IRangeIndex;
	}

	@Override
	public String getBitSliceFrom() {
		AbstractBaseExt expression = getExtendedContext(getContext().expression());
		if (expression instanceof IRangeIndex) {
			return ((IRangeIndex) expression).getFrom().getNameString();
		}
		return null;
	}

	@Override
	public String getBitSliceTo() {
		AbstractBaseExt expression = getExtendedContext(getContext().expression());
		if (expression instanceof IRangeIndex) {
			return ((IRangeIndex) expression).getTo().getNameString();
		}
		return null;
	}

	public Symbol getResolvedKey(){                                                           
		ExpressionContextExt expressionContextExt = (ExpressionContextExt)getExtendedContext(getContext().expression());
		String keyName = expressionContextExt.getFormattedText();                         
		Symbol expressionSymbol = resolve(keyName);                                       
		return expressionSymbol;                                                          
	} 

}
