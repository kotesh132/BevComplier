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

public class ImplicitTypeCastTests {
	private static final Logger L = LoggerFactory.getLogger(ExplicitTypeCastTests.class);

	@BeforeClass
	public static void setUp() throws IOException{
		L.info("Running Implicit Type cast semantic check tests - start");
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
	public void semanticCheckForImplicitTypeCast_IntegerToBitN(){
		L.info("Running Junit semanticCheckForImplicitTypeCast_IntegerToBitN");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ImplicitTypeCast_IntegerToBit16.p4");
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
        		if ("Line:38: '16w100' is incompatible with '(hdr.tcp.dataOffset)'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void semanticCheckForImplicitTypeCast_IntegerToIntN(){
		L.info("Running Junit semanticCheckForImplicitTypeCast_IntegerToIntN");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ImplicitTypeCast_IntegerToInt16.p4");
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
        		if ("Line:38: '8s10' is incompatible with '(hdr.tcp.checksumInt)'".equals(line)){
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
		L.info("Running Implicit Type cast semantic check tests - end");
	}

}
