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
public class InstantiationTest {
	private static Logger L = LoggerFactory.getLogger(InstantiationTest.class);
	static final SemanticTestDataSet semanticTestDataSet = SemanticUtils.readJson("test/com/p4/drmt/semanticchecks/data/InstantiationChecks.json");

	private SemanticTestData semTestData;

	public InstantiationTest(SemanticTestData data) {
		semTestData = data;
	}
	
	@Test
	public void semanticCheckForInstantiationsValidation(){
		L.info("Running Junit semanticCheck for Instantiations");
		Assert.assertTrue(SemanticUtils.compareData(semTestData, new InstantiationVisitor()));
	}	

	@Parameters
	public static List<Object[]> data() {
		List<Object[]> list = new ArrayList<Object[]>();


		for (SemanticTestData seed: semanticTestDataSet.getDataSet()) {
			list.add(new Object[] {seed});
		}
		return list;
	}

}
