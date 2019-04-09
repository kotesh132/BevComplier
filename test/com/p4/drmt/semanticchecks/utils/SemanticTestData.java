package com.p4.drmt.semanticchecks.utils;

import java.util.List;

import lombok.Data;

@Data
public class SemanticTestData {
	private String inputData;
	private String inputFile;
	private List<String> expectedData;
	private String expectedDataFile;
}
