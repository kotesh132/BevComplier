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

public class TypeChecksTest {
	private static final Logger L = LoggerFactory.getLogger(TypeChecksTest.class);

	@BeforeClass
	public static void setUp() throws IOException{
		L.info("Running Type semantic check tests - start");
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
	public void typeCheckForCasts(){
		L.info("Running Junit typeCheckForCasts");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidCast.p4");
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
        		if ("Line:28: '(hdr.tcp.res)' can not be cast to 'BoolType'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForCastOfCasts(){
		L.info("Running Junit typeCheckForCastOfCasts");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/CastOfCasts.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);
        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		//making sure that there is no error
        		assert(false);
        	}
        }
        assert(true);  
	}
	
	@Test
	public void typeCheckForAssignment(){
		L.info("Running Junit typeCheckForAssignment");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidAssignment.p4");
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
        		if ("Line:28: 'hdr.tcp.res' is incompatible with 'hdr.tcp.window'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForAddition(){
		L.info("Running Junit typeCheckForAddition");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidSum.p4");
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
        		if ("Line:28: 'hdr.tcp.window' is incompatible with 'hdr.tcp.res'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForSubtraction(){
		L.info("Running Junit typeCheckForSubtraction");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidSubtraction.p4");
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
        		if ("Line:28: 'hdr.tcp.window' is incompatible with 'hdr.tcp.res'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForMultiplication(){
		L.info("Running Junit typeCheckForMultiplication");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidMultiplication.p4");
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
        		if ("Line:28: 'hdr.tcp.window' is incompatible with 'hdr.tcp.res'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForDivision(){
		L.info("Running Junit typeCheckForMultiplication");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/InvalidDivision.p4");
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
        		if ("Line:28: 'hdr.tcp.window' is incompatible with 'hdr.tcp.res'".equals(line)){
        			assert(true);
        			return;
        		}
        	}
        }
        assert(false);  
	}
	
	@Test
	public void typeCheckForFunctionCall_isValid_SpecialCase(){
		L.info("Running Junit typeCheckForFunctionCall_isValid_SpecialCase");
        P416ParserUtil mp = new P416ParserUtil();
        
        File p4File = new File("test/com/p4/drmt/semanticchecks/data/FunctionCall_isValid_SpecialCase.p4");
        String text = FileUtils.readFileIntoString(p4File,"\n");
        P4programContext ctx = mp.getP416Context(text, p4File.getName());
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        
        //Index 0 corresponds to SymbolChecksVisitor
        P4programContextExt.getExtendedContext(ctx).getSemanticChecksPass().getSemanticCheckVisitors().get(0).visit(ctx);
        
        File logFile = new File("p4jc_logs/symbolchecks.log");
        List<String> logLines = FileUtils.ReadLines(logFile);
        for (String line:logLines){
        	if (line.startsWith("ERROR - ")){
        		assert(false);//There shouldn't be any line starting with error in the log file as the p4 is valid one
        	}
        }
        assert(true);  
	}
	
	@AfterClass
	public static void tearDown(){
		L.info("Deleting preprocessed files directory");
		File f = new File("test/com/p4/drmt/semanticchecks/data/preprocessedfiles");
		f.delete();
		L.info("Running Symbol semantic check tests - end");
	}

}