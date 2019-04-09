package com.p4.drmt.semanticchecks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.pp.Macro;
import com.p4.p416.pp.MacroKey;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class SemanticUtils {
	private static final Logger L = LoggerFactory.getLogger(SemanticUtils.class);

	public static FileAppender<ILoggingEvent> startLogger(String fileName) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		FileAppender<ILoggingEvent> file = new FileAppender<>();
		file.setName("FileLogger");
		file.setFile(fileName);
		file.setContext(context);
		file.setAppend(true);

		//		ThresholdFilter warningFilter = new ThresholdFilter();
		//		warningFilter.setLevel("WARN");
		//		warningFilter.setContext(context);
		//		warningFilter.start();
		//		file.addFilter(warningFilter);

		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setContext(context);
		ple.setPattern("%-5level - %msg%n");
		ple.start();
		file.setEncoder(ple);

		file.start();

		ch.qos.logback.classic.Logger chl = (ch.qos.logback.classic.Logger) Utils.getRootLogger();
		chl.addAppender(file);

		return file;
	}

	public static SemanticTestDataSet readJson(String path) {
		ObjectMapper mapper = new ObjectMapper();
		SemanticTestDataSet semanticTestData = new SemanticTestDataSet();
		try {
			semanticTestData = mapper.readValue(FileUtils.getInputStream(new File(path)), SemanticTestDataSet.class);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return semanticTestData;
	}

	public static boolean compareData(SemanticTestData semTestData) {
		return compareData( semTestData,null, null);
	}

	public static boolean compareData(SemanticTestData semTestData,P416BaseVisitor<?> visitor) {
		return compareData( semTestData,visitor, null);
	}

	public static boolean compareData(SemanticTestData semTestData, P416BaseVisitor<?> visitor, String logFilePath) {
		List<String> actualDataList = new ArrayList<String>();
		List<String> expectedDataList = new ArrayList<String>();

		String filePath = logFilePath != null? logFilePath: "p4jc_logs/temp.log";
		File logFile = new File(filePath);
		FileAppender<ILoggingEvent> fileAppender = SemanticUtils.startLogger(filePath);

		if(logFile.exists()) {
			L.info("Cleared the log file");
			FileUtils.writeToFile(logFile, false, "");
		} 

		String inputdata = semTestData.getInputData() != null? semTestData.getInputData():fileExists(semTestData.getInputFile(), true)? FileUtils.readFileIntoString(getProcessedFile(new File(semTestData.getInputFile())),"\n"): null;
//		L.info("Input p4 test data: \n"+inputdata);

		L.info("Running Junit test");

		P416ParserUtil p416ParserUtil = new P416ParserUtil();
		P4programContext p4ProgramContext = p416ParserUtil.getP416Context(inputdata, semTestData.getInputFile() !=null ? (getProcessedFile(new File(semTestData.getInputFile()))).getAbsolutePath() :"Testdata_");

		//p4ProgramContext.extendedContext.defineSymbol(null);
		
		if(visitor != null) {
			visitor.visit(p4ProgramContext);
		} else {
			((P4programContextExt)P4programContextExt.getExtendedContext(p4ProgramContext)).runSemanticChecks();
		}

		List<String> logLines = FileUtils.ReadLines(logFile);

		for (String line:logLines) {
			if (line.startsWith("ERROR - ")){
				actualDataList.add(line.substring(8));
			}
		}

		L.info("Deleting log file");
		logFile.delete();
		fileAppender.stop();

		expectedDataList = semTestData.getExpectedData() != null? semTestData.getExpectedData():fileExists(semTestData.getExpectedDataFile(), true)? readTestDataFileIntoList(semTestData.getExpectedDataFile()): null;
		if(!expectedDataList.isEmpty()) {
			expectedDataList.removeIf(String::isEmpty);
		}
		
//		System.out.println(actualDataList.toString());

		boolean result = false;
		if(expectedDataList != null && actualDataList.size() == expectedDataList.size()) {
			L.info("==============================DATA==================================");
			for(int index=0 ; index<expectedDataList.size();index++) {
				L.info("actual data = "+ actualDataList.get(index) + "	expected data = " + expectedDataList.get(index));
				//				Assert.assertTrue(actualDataList.get(index).equals(expectedDataList.get(index)));
				if(actualDataList.get(index).equals(expectedDataList.get(index)))
					result |= true;
			}
			L.info("====================================================================");
		}
		return result;
	}

	private static List<String> readTestDataFileIntoList(String filePath){
		List<String> data = new ArrayList<String>();
		try{
			File fileName = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			try{

				String str;
				while ((str = br.readLine()) != null){
					data.add(str);
				}

			} finally {
				br.close();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return data;
	}

	private static boolean fileExists(String filePath, boolean warn) {
		File file = filePath != null? new File(filePath):null;
		boolean ret = filePath != null && file.exists();
		if(!ret && warn) 
			L.warn("File does not exists. -- " + (file == null ? "null" : file.getAbsolutePath()));
		return ret;
	}

	private static File getProcessedFile(File fileName) {
		HashMap<File,Boolean> filePreprocessStatusMap = new HashMap<File, Boolean>();
		HashMap<MacroKey,Macro> tempMacroMap = new HashMap<MacroKey, Macro>();
		File outputDir = new File("p4jc_pp");

		List<File> allInputFilesInOrder = new ArrayList<File>();
		allInputFilesInOrder.add(fileName);

		PreprocessRunnerSession session = new PreprocessRunnerSession(outputDir, allInputFilesInOrder , tempMacroMap, fileName, filePreprocessStatusMap);
		session.preprocess();
		L.info("Preprocssing done");
		File preProcessedFile = new File(outputDir,fileName.getName());

		return preProcessedFile;
	}

}
