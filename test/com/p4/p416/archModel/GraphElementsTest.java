package com.p4.p416.archModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IGraphNode;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IParser;
import com.p4.p416.iface.IParserState;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

public class GraphElementsTest {

	private static P4programContext p4programContext = null;
	private HashMap<String, List<String>> expectedStateChildren = new HashMap<String, List<String>>();
	private static GraphBuilderVisitor graphBuilderVisitor;

	@BeforeClass
	public static void setUp() {
		File inputFile = new File("test/com/p4/p416/archModel/GraphElementTestData.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
		graphBuilderVisitor = new GraphBuilderVisitor();
		graphBuilderVisitor.visit(p4programContext);
	}

	@Test
	public void getAdjacentChildrenTest() {
		IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getP4Program();
		expectedStateChildren.put("start",Arrays.asList("parse_ethernet"));
		expectedStateChildren.put("parse_ethernet",Arrays.asList("parse_ipv4","parse_ipv6","accept"));
		expectedStateChildren.put("parse_ipv4",Arrays.asList("parse_tcp","parse_udp","accept"));
		expectedStateChildren.put("parse_ipv6",Arrays.asList("parse_tcp","parse_udp","accept"));
		expectedStateChildren.put("parse_tcp",Arrays.asList("parse_dummy","accept"));
		expectedStateChildren.put("parse_udp",Arrays.asList("accept"));
		expectedStateChildren.put("parse_dummy",Arrays.asList("accept"));


		for(IParser parserDeclaration: p4Program.getParsers()){
			HashMap<String, IParserState> parserStateMap = parserDeclaration.getParserStatesMap();
			for(String parseStateName :parserStateMap.keySet()){
				List<IGraphNode> childrenNodes = parserStateMap.get(parseStateName).getChildrenNodes();
				if(expectedStateChildren.containsKey(parseStateName)){
					Assert.assertEquals(childrenNodes.size(),expectedStateChildren.get(parseStateName).size());
					for(IGraphNode parserState : childrenNodes){
						Assert.assertTrue(expectedStateChildren.get(parseStateName).contains(((IParserState) parserState).getNameString()));
					}
				}
			}
		}
	}
	
	@Test
	public void longestPathTest() {
		
		IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getP4Program();
		List<IParser> parsers = p4Program.getParsers();
		List<List<IParserState>> longestPath = new ArrayList<List<IParserState>>();
		for(IParser parserDeclaration: parsers){
			longestPath = graphBuilderVisitor.getLongestPathInParser(parserDeclaration);
		}
		
		List<String> expectedList1 = Arrays.asList("start", "parse_ethernet", "parse_ipv6", "parse_tcp", "parse_dummy", "accept");
		List<String> expectedList2 = Arrays.asList("start", "parse_ethernet", "parse_ipv4", "parse_tcp", "parse_dummy", "accept");
		List<List<String>> expectedList = new ArrayList<List<String>>();
		expectedList.add(expectedList1);
		expectedList.add(expectedList2);
		
		Assert.assertEquals(longestPath.size(), 2);
		Assert.assertEquals(longestPath.get(0).size(),6);
		
		List<String> actualList1 = new ArrayList<String>();
		List<String> actualList2 = new ArrayList<String>();
		List<List<String>> actualList = new ArrayList<List<String>>();
		
		
		for(IParserState path : longestPath.get(0)){
			actualList1.add(path.getNameString());
		}
		for(IParserState path : longestPath.get(1)){
			actualList2.add(path.getNameString());
		}
		actualList.add(actualList1);
		actualList.add(actualList2);
		Assert.assertTrue(actualList.equals(expectedList));
	}
}
