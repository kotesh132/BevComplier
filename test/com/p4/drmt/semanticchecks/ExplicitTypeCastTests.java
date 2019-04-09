package com.p4.drmt.semanticchecks;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

public class ExplicitTypeCastTests {
	private static final Logger L = LoggerFactory.getLogger(ExplicitTypeCastTests.class);

	@BeforeClass
	public static void setUp() throws IOException{
		L.info("Running Explicit Type cast semantic check tests - start");
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
	public void semanticCheckForExplicitTypeCast_BitNToIntN(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BitNToIntN");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BitNtoIntN.p4");
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
        		if ("Line:34: '(hdr.tcp.checksum)' can not be cast to 'IntSizeType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForExplicitTypeCast_BitToBool(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BitToBool");
        P416ParserUtil mp = new P416ParserUtil();
       
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BitToBool.p4");
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
        		if ("Line:34: 'hdr.tcp.isValid' is incompatible with '(hdr.tcp.oneBit)'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForExplicitTypeCast_BitWidthTruncation(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BitWidthTruncation");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BitWidthTruncation.p4");
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
        		if ("Line:34: '(hdr.tcp.ackNoInt)' can not be cast to 'BitSizeType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForExplicitTypeCast_BitWidthWidening(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BitWidthWidening");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BitWidthWidening.p4");
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
        		if ("Line:34: '(hdr.tcp.ackNoInt)' can not be cast to 'BitSizeType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForExplicitTypeCast_BoolToBit1(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BoolToBit1");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BoolToBit1.p4");
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
        		if ("Line:35: 'hdr.tcp.window' is incompatible with '(bit<1>)(hdr.tcp.isValid)'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForExplicitTypeCast_BoolToPlainBit(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_BoolToPlainBit");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_BoolToPlainBit.p4");
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
        		if ("Line:34: 'hdr.tcp.window' is incompatible with '(bit)(hdr.tcp.isValid)'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@Test
	public void semanticCheckForExplicitTypeCast_intNToBitN(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_intNToBitN");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_intNtobitN.p4");
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
        		if ("Line:35: '(hdr.tcp.checksumInt)' can not be cast to 'BitSizeType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForExplicitTypeCast_intWidthTruncation(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_intWidthTruncation");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_IntWidthTruncation.p4");
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
        		if ("Line:34: '(hdr.tcp.window)' can not be cast to 'IntSizeType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForExplicitTypeCast_intWidthWidening(){
		L.info("Running Junit semanticCheckForExplicitTypeCast_intWidthWidening");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ExplicitTypeCast_IntWidthWidening.p4");
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
        		if ("Line:34: '(hdr.tcp.flags)' can not be cast to 'IntSizeType'".equals(line)){
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
		L.info("Running Explicit Type cast semantic check tests - end");
	}

}
