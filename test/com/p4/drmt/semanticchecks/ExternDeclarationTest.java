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
public class ExternDeclarationTest {

	private static  Logger L = LoggerFactory.getLogger(ExternDeclarationTest.class);
	final static SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/ExternDeclaration.json");

	private SemanticTestData semTestData;

	public ExternDeclarationTest(SemanticTestData data) {
		semTestData = data;
	}

	@Test
	public void semanticCheckForExternDeclarationValidation(){
		L.info("Running Junit semanticCheckForExternDeclaration");
		Assert.assertTrue(SemanticUtils.compareData(semTestData,new ExternDeclarationVisitor()));
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

