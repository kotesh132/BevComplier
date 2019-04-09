package com.p4.cgen1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.cgen1.runner.CGen1CLIParser;
import com.p4.cgen1.runner.CGen1RunnerSession;

public class CppFileCompareTest {
	private static final Logger L = LoggerFactory.getLogger(CppFileCompareTest.class);
	static String outDir = "test/com/p4/cgen1/testResources/";

	@BeforeClass
	public static void setUp() throws FileNotFoundException{
		List<File> inputFiles = new ArrayList<>();
		inputFiles.add(new File("test/com/p4/cgen1/testResources/gendata.p4"));

		File out = new File(outDir);
		if (!out.exists()) {
			out.mkdirs();
		}

		CGen1CLIParser commandLineParser = new CGen1CLIParser(new File("./").getAbsolutePath());
		commandLineParser.setInputFilesInOrder(inputFiles);
		commandLineParser.setOutputFile(new File(outDir));
		commandLineParser.setPhvOffsetsFile(new File("test/com/p4/cgen1/testResources/phvOffsets.json"));
		commandLineParser.processArgs();
		CGen1RunnerSession cppGenRunnerSession = new CGen1RunnerSession(commandLineParser);
		cppGenRunnerSession.run();
	}

	@Test
	public void controlSourceFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/ingress.cpp",outDir+"/../expected/ingress.cpp"));
	}

	@Test
	public void controlHeaderFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/control.hpp",outDir+"/../expected/control.hpp"));
	}
	
	@Test
	public void enumHeaderFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/enums.hpp",outDir+"/../expected/enums.hpp"));
	}
	
	@Test
	public void filedsSourceFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/fields.cpp",outDir+"/../expected/fields.cpp"));
	}
	
	@Test
	public void headersSourceFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/headers.cpp",outDir+"/../expected/headers.cpp"));
	}
	
	@Test
	public void headersHeaderFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/headers.hpp",outDir+"/../expected/headers.hpp"));
	}
	
	@Test
	public void phvParserHeaderFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/phvparser.hpp",outDir+"/../expected/phvparser.hpp"));
	}
	
	@Test
	public void phvDeParserHeaderFile() throws FileNotFoundException{
		Assert.assertTrue(compareTwoFiles(outDir+"/phvdeparser.hpp",outDir+"/../expected/phvdeparser.hpp"));
	}
	
	public boolean compareTwoFiles(String file1Path, String file2Path) {
		Path p1 = Paths.get(file1Path);
		Path p2 = Paths.get(file2Path);

		try {
			List<String> listF1 = Files.readAllLines(p1);
			List<String> listF2 = Files.readAllLines(p2);
			return listF1.containsAll(listF2);

		} catch (IOException ie) {
			return false;
		}

	}

}
