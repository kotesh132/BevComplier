package com.p4.drmt.semanticchecks;


import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.ParserStatesContextExt;


public class NameValidationVisitor extends P416BaseVisitor<ParserRuleContext>{
		protected static final Logger L = LoggerFactory.getLogger(NameValidationVisitor.class);
		
		//Check to ensure that Parser state name should not be 'accept' or 'reject'
		@Override
		public ParserRuleContext visitParserState(P416Parser.ParserStateContext ctx){
			super.visitParserState(ctx);
			String parserState = (ParserStatesContextExt.getExtendedContext(ctx.name())).getFormattedText();
			if ("accept".equals(parserState) || "reject".equals(parserState)){
				L.error("Line:"+ctx.start.getLine()+": "+"Parser state '"+parserState+"' should not be implemented, it is built-in");
			}
			return ctx;
		}
}
