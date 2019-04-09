package com.p4.p416.archModel;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.utils.Utils;

public class ArchitecturalModelRunner {
	private static final Logger L = LoggerFactory.getLogger(ArchitecturalModelRunner.class);
	private static JCommander jc = null;
	private static CommandLineParser cp = new CommandLineParser(new File("./").getAbsolutePath());
	
	public static void main(String[] args) {		
		try{
			jc = new JCommander(cp);
			jc.setProgramName("p4jc");
			jc.parse(args);
			cp.processArgs();
			cp.getLoglevel();
			if(cp.isHelp()){
				jc.usage();
				System.exit(0);
			}
		}catch(Exception e){
			jc.usage();
			System.exit(1);
		}
		String ll = cp.getLoglevel() == null || cp.getLoglevel().isEmpty() ? "info" : cp.getLoglevel();
		Utils.setRootLogLevel(ll);
		HashMap<File,Boolean> hashmap = new HashMap<>();
		PreprocessRunnerSession session = new PreprocessRunnerSession(cp.getOutputFile(), CommandLineParser.getInputFilesInOrder() , cp.getMacroDefinitions(), null, hashmap);
		session.preprocess();
		
		L.info("Preprocssing done");
		ArchitecturalModelRunnerSession architecturalModelRunnerSession = new ArchitecturalModelRunnerSession(cp);
		architecturalModelRunnerSession.run();
		L.info("Architectural Model completed");
	}
	
	public static void takeCareOfError(String externalErrorMsg, Throwable t, boolean systemExitOnException) {
		L.error(externalErrorMsg + ": " + Utils.getRootCauseUserDisplayString(t));
		takeCareOfError(new RuntimeException(externalErrorMsg, t), systemExitOnException);
	}

	private static void takeCareOfError(Throwable t, boolean systemExitOnException) {
		if (systemExitOnException)
			System.exit(1);
		else
			throw new RuntimeException(t);
	}
}
