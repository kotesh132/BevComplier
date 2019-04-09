package com.p4.drmt;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;
@Data
public class GlobalAddress {
	private final Map<String,Integer> fields = new LinkedHashMap<>();
	private final Map<String,Integer> sizes = new LinkedHashMap<>();
	
	public static void writeJSON(File outputDir, String fileName, Map<?, ?> ga) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.defaultPrettyPrintingWriter().writeValue(new File(outputDir.getAbsolutePath() + File.separator + fileName), ga);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
