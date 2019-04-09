package com.p4.pktgen;

import java.io.File;
import java.util.BitSet;
import java.util.Stack;

import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

public class EmitUtils {

	private static final int MAX_WORDS = 16;
	
	public static void emitToFile(File output, Integer somId, String baseAddr, Integer numWords, Integer wordSize, Integer maxWords, BitSet config) {
		FileUtils.AppendToFile(output, "add_config_entry(" + somId + ", " + baseAddr + ", " + numWords + ", ");
		File anotherConfigFile = null;
		if(output.getAbsolutePath().endsWith(".sv")) {
			anotherConfigFile = new File(output.getAbsolutePath().replaceAll("\\.sv", ".txt"));
		}
		if(anotherConfigFile != null)
			FileUtils.AppendToFile(anotherConfigFile, somId + "\n" + baseAddr + "\n" + numWords + "\n");
		int offset = 0;
		Stack<BitSet> stack = new Stack<BitSet>();
		for(int i=0; i<numWords; i++) {
			stack.push(config.get(offset, offset + wordSize));
			offset += wordSize;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<MAX_WORDS-numWords; i++) {
			StringBuilder zeros = new StringBuilder();
			for(int j=0; j<wordSize/4; j++)
				zeros.append(0);
			sb.append(zeros);
		}
		while(!stack.empty()) {
			String hexString = Utils.bitSetToHex(stack.pop());
			StringBuilder zeros = new StringBuilder();
			for(int j=hexString.length(); j<wordSize/4; j++)
				zeros.append(0);
			sb.append(zeros).append(hexString);
		}
		FileUtils.AppendToFile(output, (MAX_WORDS * 32) + "'h" + sb.toString() + ");\n");
		if(anotherConfigFile != null)
			FileUtils.AppendToFile(anotherConfigFile, (MAX_WORDS * 32) + "'h" + sb.toString() + "\n");
	}
}
