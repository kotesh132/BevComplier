package com.p4.tools.graph.runner;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.utils.Utils;

public class GraphRunner {
	private static final Logger L = LoggerFactory.getLogger(GraphRunner.class);
	private static JCommander jc = null;
	private static CommandLineParser cp = new CommandLineParser(new File("./").getAbsolutePath());
	
	public static void main(String[] args) {		
		try{
			jc = new JCommander(cp);
			jc.setProgramName("proteus");
			jc.parse(args);
			cp.processArgs();
			if(cp.isHelp()){
				jc.usage();
				System.exit(0);
			}
		}catch(Exception e){
			jc.usage();
			System.exit(1);
		}
		HashMap<File,Boolean> hashmap = new HashMap<>();
		PreprocessRunnerSession session = new PreprocessRunnerSession(cp.getOutputFile(), CommandLineParser.getInputFilesInOrder() , cp.getMacroDefinitions(), null, hashmap);
		session.preprocess();
		
		L.info("Preprocssing done");
		GraphRunnerSession gr = new GraphRunnerSession(cp);
		gr.run5(); //combination of run3 and run4
		L.info("Graph Extraction completed");
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
