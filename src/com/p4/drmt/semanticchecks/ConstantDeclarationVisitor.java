package com.p4.drmt.semanticchecks;


import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.ConstantDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.BaseTypeContext;
import com.p4.p416.gen.P416Parser.BitSizeTypeContext;
import com.p4.p416.gen.P416Parser.BitTypeContext;
import com.p4.p416.gen.P416Parser.BoolTypeContext;
import com.p4.p416.gen.P416Parser.ErrorTypeContext;
import com.p4.p416.gen.P416Parser.IntSizeTypeContext;
import com.p4.p416.gen.P416Parser.VarBitSizeTypeContext;


public class ConstantDeclarationVisitor extends P416BaseVisitor<ParserRuleContext>{
	private static final Logger L = LoggerFactory.getLogger(ConstantDeclarationVisitor.class);

	@Override 
	public ParserRuleContext visitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx){
		super.visitConstantDeclaration(ctx);
		if(ctx.typeRef() != null && ctx.typeRef().baseType() !=null) {
			BaseTypeContext baseTypeCtx = ctx.typeRef().baseType();
			String baseType = ConstantDeclarationContextExt.getExtendedContext(baseTypeCtx).getFormattedText();

			/// Width of a bit<> type is at least 0
			if(baseType.startsWith("bit")){
				if(!(ctx.typeRef().baseType() instanceof BitSizeTypeContext)) {
					L.error("Line:"+ctx.start.getLine()+":: Width of a bit<> type is at least 0.");
				}
			}
			
			/// Width of an int<> type is at least 1
			if(baseTypeCtx instanceof IntSizeTypeContext){
				IntSizeTypeContext intSizeTypeContext = (IntSizeTypeContext) baseTypeCtx;
				int size = ConstantDeclarationContextExt.getExtendedContext(intSizeTypeContext).getSizeInBits();
				
				if(size < 2 && size >0) {
					L.error("Line:"+ctx.start.getLine()+": "+ "Width of an int<> type is at least 1.");
				}
			}

			/// Check that the type of a constant is either bit<>, int<> or int
			if(baseTypeCtx instanceof BoolTypeContext || baseTypeCtx instanceof ErrorTypeContext || 
					baseTypeCtx instanceof BitTypeContext || baseTypeCtx instanceof VarBitSizeTypeContext){	
				{
					L.error("Line:"+ctx.start.getLine()+": "+ "Type of a constant should be either bit<>, int<> or int.");
				}

			}

		}
		if(ctx.name() != null) {
			if(ConstantDeclarationContextExt.getExtendedContext(ctx.name()).getFormattedText().equals("_")) {
				L.error("Line:"+ctx.start.getLine()+": "+ "Constant names cannot be underscore.");
			}
		}
		return ctx;
	}
	
}
