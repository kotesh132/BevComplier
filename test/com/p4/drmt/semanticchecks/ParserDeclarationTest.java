package com.p4.drmt.semanticchecks;

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
public class ParserDeclarationTest {

	private static  Logger L = LoggerFactory.getLogger(ParserDeclarationTest.class);
	final static SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/ParserDeclaration.json");

	private SemanticTestData semTestData;

	public ParserDeclarationTest(SemanticTestData data) {
		semTestData = data;
	}

	@Test
	public void semanticCheckForParserDeclarationValidation(){
		L.info("Running Junit semanticCheckForParserDeclaration");
		Assert.assertTrue(SemanticUtils.compareData(semTestData,new ParserDeclarationVisitor()));
		L.info("*** Running tests - end ***");
	}	

	@Parameters
	public static List<Object[]> data() {
		List<Object[]> list = new ArrayList<Object[]>();
		for (SemanticTestData semTestData: semanticTestDataSet.getDataSet()) {
			list.add(new Object[] {semTestData});
		}
		return list;
	}
}

