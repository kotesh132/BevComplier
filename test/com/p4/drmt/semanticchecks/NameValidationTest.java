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

public class NameValidationTest {
	private static final Logger L = LoggerFactory.getLogger(SymbolCheckTest.class);

	@BeforeClass
	public static void setUp(){
		L.info("Running Name Validation tests - start");
	}
	
	@Before
	public void clearLogFile() throws IOException{
		File logFile = new File("p4jc_logs/symbolchecks.log");
		FileUtils.writeToFile(logFile, false, "");
		L.info("Cleared the log file");
	}
	
	@Test
	public void semanticCheckForNameValidation(){
		L.info("Running Junit semanticCheckForNameValidation");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/ParserStateNameValidation.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 1 corresponds to NameValidationVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(1).visit(ctx);

        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		line = line.substring(8);
        		if ("Line:2: Parser state 'accept' should not be implemented, it is built-in".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}	
	
	@AfterClass
	public static void tearDown(){
		L.info("Running Name Validation tests - end");
	}

}
