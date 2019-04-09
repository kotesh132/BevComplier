package com.p4.drmt.semanticchecks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

public class SymbolCheckTest {
	private static final Logger L = LoggerFactory.getLogger(SymbolCheckTest.class);

	@BeforeClass
	public static void setUp() throws IOException{
		L.info("Running Symbol semantic check tests - start");
		L.info("Creating preprocessed files directory");
		File f = new File("test/com/p4/drmt/semanticchecks/data/preprocessedfiles");
		f.createNewFile();
	}
	
	@Before
	public void clearLogFile() throws IOException{
		File logFile = new File("p4jc_logs/symbolchecks.log");
		FileUtils.writeToFile(logFile, false, "");
		L.info("Cleared the log file");
	}
	
	@Test
	public void semanticCheckForSymbolInAssignment(){
		L.info("Running Junit semanticCheckForSymbolInAssignment");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/Assignment.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);
        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:109: 'ptr1' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForSymbolInConditionalStatement(){
		L.info("Running Junit semanticCheckForSymbolInConditionalStatement");
        P416ParserUtil mp = new P416ParserUtil();
       
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ConditionalStatement.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);
        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:3: 'ptr2' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForSymbolInMemberReference(){
		L.info("Running Junit semanticCheckForSymbolInMemberReference");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/MemberReference.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:102: 'hdr.tcp.dstPort2' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForReferenceOfParserParamInBody(){
		L.info("Running Junit semanticCheckForReferenceOfParserParamInBody");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ParserBlockParamReference.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:108: 'packet2' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForReferenceOfParserStateNameInTransition(){
		L.info("Running Junit semanticCheckForReferenceOfParserStateNameInTransition");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ParserStateReferenceInTransition.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:126: '8w0x6: parse_tcp;' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForReferenceOfControlBlockParamInBody(){
		L.info("Running Junit semanticCheckForReferenceOfControlBlockParamInBody");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ControlBlockParamReference.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:100: 'packet3' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForMainTypeRef(){
		L.info("Running Junit semanticCheckForMainTypeRef");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/MainInstantiation.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:120: 'V1Switch' can not be resolved".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	
	@AfterClass
	public static void tearDown(){
		L.info("Deleting preprocessed files directory");
		File f = new File("test/com/p4/drmt/semanticchecks/data/preprocessedfiles");
		f.delete();
		L.info("Running Symbol semantic check tests - end");
	}

}
