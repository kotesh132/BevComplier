package com.p4.packetgen.runner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;

import com.p4.p416.gen.*;
import com.p4.p416.gen.P416Parser.*;
import com.p4.packetgen.utils.DescriptiveErrorListener;

import lombok.Data;
@Data
public class P416ParserUtil {

	private static final Logger L = LoggerFactory.getLogger(P416ParserUtil.class);

	public P4programContext getP416Context(String text,String FileName){
		L.info("trying to parse "+FileName);
		ParserRuleContext p = trySLLContent(text);
		if(p==null){
			p = tryLLContent(text);
		}
		if(p!=null){
			PopulateExtendedContextVisitor pecv = new PopulateExtendedContextVisitor();
			pecv.visit(p);
			ExtendedContextGetVisitor ecv = new ExtendedContextGetVisitor();
			AbstractBaseExt abxt = ecv.visit(p);
			P4programContextExt stxt = (P4programContextExt) abxt;
			if(stxt==null){
				L.warn("No context");
			}
			P4programContext stc = (P4programContext)stxt.getContext();
			L.warn("Done with "+FileName);
			return stc;
		}else{
			throw new IllegalStateException("Could not parse p416 file :"+FileName);
		}
	}

	public static ParserRuleContext parse(String content){
		ParserRuleContext p = trySLLContent(content);
		if(p==null){
			p = tryLLContent(content);
		}
		return p;
	}


	public static ParserRuleContext tryLLContent(String content){
		P416Lexer lexer = new P416Lexer(new ANTLRInputStream(content));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		P416Parser parser = new P416Parser(null);
		try{
			parser.getInterpreter().setPredictionMode(PredictionMode.LL);
//			parser.setErrorHandler(new BailErrorStrategy());
			DescriptiveErrorListener descError = DescriptiveErrorListener.INSTANCE;
			parser.addErrorListener(descError);
			
			parser.setBuildParseTree(true);
			parser.setTokenStream(tokens);
			ParserRuleContext tree = parser.p4program();
			return tree;
		}catch(Exception e){
			L.error("Error parsing content with LL strategy");
			return null;
		}
	}

	public static ParserRuleContext trySLLContent(String content){
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
		P416Lexer lexer = new P416Lexer(antlrInputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		P416Parser parser = new P416Parser(null);
		try{
			parser.setTrace(true);
			parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
			parser.setErrorHandler(new BailErrorStrategy());
			parser.setBuildParseTree(true);
			parser.setTokenStream(tokens);
			ParserRuleContext tree = parser.p4program();
			return tree;
		}catch(Exception e){
			L.debug("Error parsing content with SLL strategy");
			return null;
		}
	}


}
