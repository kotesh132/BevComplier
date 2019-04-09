package com.p4.drmt.semanticchecks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.LoggerFactory;

import com.p4.drmt.semanticchecks.utils.SemanticTestData;
import com.p4.drmt.semanticchecks.utils.SemanticTestDataSet;
import com.p4.drmt.semanticchecks.utils.SemanticUtils;

import org.slf4j.Logger;

@RunWith(Parameterized.class)
public class ActionDeclarationTest {

	private static  Logger L = LoggerFactory.getLogger(ActionDeclarationTest.class);
	final static SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/ActionDeclarationchecksExp.json");

	private SemanticTestData semTestData;

	public ActionDeclarationTest(SemanticTestData data) {
		semTestData = data;
	}

	@Test
	public void semanticCheckForActionDeclarationValidation(){
		L.info("Running Junit semanticCheckForActionDeclaration");
		Assert.assertTrue(SemanticUtils.compareData(semTestData,new ActionDeclarationVisitor()));
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
