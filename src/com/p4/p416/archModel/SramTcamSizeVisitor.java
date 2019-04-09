package com.p4.p416.archModel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.KeyContextExt;
import com.p4.p416.gen.KeyElementContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.TableDeclarationContextExt;
import com.p4.p416.iface.IActionRef;
import com.p4.p416.iface.IParameter;

@EqualsAndHashCode(callSuper=false)
@Data
public class SramTcamSizeVisitor extends P416BaseVisitor<ParserRuleContext>{

	protected static final Logger L = LoggerFactory.getLogger(SramTcamSizeVisitor.class);
	
	protected Integer sramSize;
	protected Integer tcamSize;
	
	
	public SramTcamSizeVisitor(){
		sramSize = 0;
		tcamSize = 0;
	}
	
	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx){
		P4programContextExt p4programContextExt = (P4programContextExt) P4programContextExt.getExtendedContext(ctx);
		sramSize = 0;
		tcamSize = 0;
		super.visitP4program(ctx);
		p4programContextExt.setSram(sramSize);
		p4programContextExt.setTcam(tcamSize);
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx){
		ControlDeclarationContextExt controlDeclarationContextExt = (ControlDeclarationContextExt) ControlDeclarationContextExt.getExtendedContext(ctx);
		int totalSramSize = sramSize;
		int totalTcamSize = tcamSize;
		sramSize = 0;
		tcamSize = 0;
		super.visitControlDeclaration(ctx);
		controlDeclarationContextExt.setSram(sramSize);
		controlDeclarationContextExt.setTcam(tcamSize);
		sramSize = controlDeclarationContextExt.getSram() +totalSramSize;
		tcamSize = controlDeclarationContextExt.getTcam() +totalTcamSize;
		
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx){
		TableDeclarationContextExt tableDeclarationContextExt = (TableDeclarationContextExt) TableDeclarationContextExt.getExtendedContext(ctx);
		String tSize = tableDeclarationContextExt.getTableSize();
		Integer tableSize = (tSize!= null) ? Integer.valueOf(tSize) : 0;

		List<Integer> dataSizeList = new ArrayList<Integer>();

		List<IActionRef> actionsRefs = tableDeclarationContextExt.getActionsRefs();
		for(IActionRef actionRef:actionsRefs){
			ActionDeclarationContextExt actionDeclarationContextExt = (ActionDeclarationContextExt) tableDeclarationContextExt.resolve(actionRef.getActionDeclaration().getNameString());
			List<IParameter> parameters = actionDeclarationContextExt.getParameters();
			Integer dataSize = 0;
			for(IParameter parameter : parameters){
				dataSize = dataSize + Integer.valueOf(parameter.getTypeRef().getSize());
			}
			dataSizeList.add(dataSize);
		}

		Integer maxDataSize = Integer.MIN_VALUE;
		for (Integer element : dataSizeList) {
			if (element > maxDataSize) {
				maxDataSize = element;
			}
		}
		int totalSramSize = sramSize;
		int totalTcamSize = tcamSize;
		sramSize = 0;
		tcamSize = 0;
		super.visitTableDeclaration(ctx);
		tableDeclarationContextExt.setSram((sramSize+maxDataSize)*tableSize);
		tableDeclarationContextExt.setTcam(tcamSize*tableSize);
		sramSize = tableDeclarationContextExt.getSram() + totalSramSize;
		tcamSize = tableDeclarationContextExt.getTcam() + totalTcamSize;
		
		
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitKeyElement(P416Parser.KeyElementContext ctx){
		KeyElementContextExt keyElementContextExt = (KeyElementContextExt) KeyElementContextExt.getExtendedContext(ctx);
		String key = keyElementContextExt.getKeyName();
		String matchKind = keyElementContextExt.getKeyMatchKind();

		Symbol symbol = keyElementContextExt.resolve(key);
		Integer keySize = symbol.getSizeInBits();

		if(!matchKind.equals("exact")){
			keyElementContextExt.setTcam(keySize);
			tcamSize = tcamSize+keySize;
		}else{
			keyElementContextExt.setSram(keySize);
			sramSize = sramSize+keySize;
		}
		return ctx;
	}
	
	
	
	@Override
	public ParserRuleContext visitKey(P416Parser.KeyContext ctx){
		KeyContextExt keyContextExt = (KeyContextExt) KeyContextExt.getExtendedContext(ctx);
		int totalSramSize = sramSize;
		int totalTcamSize = tcamSize;
		sramSize = 0;
		tcamSize = 0;
		super.visitKey(ctx);
		keyContextExt.setSram(sramSize);
		keyContextExt.setTcam(tcamSize);
		sramSize = sramSize+totalSramSize;
		tcamSize = tcamSize+totalTcamSize;
		return ctx;
	}

}
