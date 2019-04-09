package com.p4.p416.iface;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;


public class ParameterTest {
	
	@Test
	public void getDirectionTest() {
		File inputFile = new File("test/com/p4/p416/iface/parameterTest.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
		
		Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
		
		P4programContextExt.getExtendedContext(p4programContext).getActions(actions);
		
		String expDirection = actions.get("a").getParameters().get(0).getDirection();
		
		Assert.assertEquals(expDirection, "inout");
		
	}

}
