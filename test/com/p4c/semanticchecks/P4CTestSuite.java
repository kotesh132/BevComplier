package com.p4c.semanticchecks;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.semanticchecks.utils.SemanticTestData;
import com.p4.drmt.semanticchecks.utils.SemanticTestDataSet;
import com.p4.drmt.semanticchecks.utils.SemanticUtils;

@RunWith(Parameterized.class)
@SuppressWarnings("unused")
public class P4CTestSuite {

	private SemanticTestData semTestData;
	private String p4FilePath;

	private Logger L = LoggerFactory.getLogger(P4CTestSuite.class);
	static SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/p4cTestSuite.json");

	public P4CTestSuite(SemanticTestData data, String p4FilePath) {
		semTestData = data;
		this.p4FilePath = p4FilePath;
	}

	@Test
	public void semanticCheckForP4CValidation(){
//		L.info("Running Junit semanticCheckFor ActionDeclaration");
		boolean result = SemanticUtils.compareData(semTestData);
		Assert.assertTrue(result);
	}	

	@Parameters( name = "{index}: {1}" )
	public static List<Object[]> data() {
		List<Object[]> list = new ArrayList<Object[]>();

		for (SemanticTestData data: semanticTestDataSet.getDataSet()) {
//			if(data.getInputFile().contains("extract.p4"))
				list.add(new Object[] {data,getFileName(data.getInputFile())});
		}
		return list;
	}

	private static String getFileName(String path) {
		Path p = Paths.get(path);
		return p.getFileName().toString();
	}

}
