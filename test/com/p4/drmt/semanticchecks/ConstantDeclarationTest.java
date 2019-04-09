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
public class ConstantDeclarationTest {
	
	private static Logger L = LoggerFactory.getLogger(ConstantDeclarationTest.class);
	final static SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/ConstantDeclarationchecksExp.json");
	
	private SemanticTestData semTestData;
	
	public ConstantDeclarationTest(SemanticTestData data) {
		semTestData = data;
	}
	
	@Test
	public void semanticCheckForConstantDeclarationValidation(){
		L.info("*** Running Junit semanticCheckForConstantDeclaration ***");
		Assert.assertTrue(SemanticUtils.compareData(semTestData, new ConstantDeclarationVisitor()));
		L.info("*** Running tests - end ***");
	}	

	@Parameters
	public static List<Object[]> data() {
		List<Object[]> list = new ArrayList<Object[]>();


		for (SemanticTestData semanticTestData: semanticTestDataSet.getDataSet()) {
			list.add(new Object[] {semanticTestData});
		}
		return list;
	}
}

