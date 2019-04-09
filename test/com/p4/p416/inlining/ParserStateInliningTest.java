package com.p4.p416.inlining;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.p4.p416.archModel.GraphBuilderVisitor;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IParser;
import com.p4.p416.iface.IParserState;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

public class ParserStateInliningTest {

	@Test
	public void InlineTransformation() {
		File inputFile = new File("test/com/p4/p416/inlining/ParserStateInliningData.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

		GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
		graphBuilderVisitor.visit(p4programContext);
		List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

		P4programContextExt.getExtendedContext(p4programContext).inlineParserStates(null,loopPaths, null);
		for(IParser parserDeclaration:  ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getParsers()){
			HashMap<String, IParserState> parserList = parserDeclaration.getParserStatesMap();
			System.out.println(P4programContextExt.getExtendedContext(p4programContext).getFormattedText());
			
			Assert.assertEquals(parserList.keySet().size(), 7);
			String expectedCtx =  FileUtils.ReadFromFile(new File("test/com/p4/p416/inlining/ParserStateInliningExpected.p4"));
			System.out.println(P4programContextExt.getExtendedContext(p4programContext).getFormattedText());
			Assert.assertEquals(expectedCtx,P4programContextExt.getExtendedContext(p4programContext).getFormattedText());
		}
	}

	@Test
	public void  InlineTransformationAdjacency() {
		File inputFile = new File("test/com/p4/p416/inlining/ParserStateInliningData.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

		GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
		graphBuilderVisitor.visit(p4programContext);

		HashMap<IParserState, List<IParserState>> adjacencyList = graphBuilderVisitor.getAdjacencyList();

		Assert.assertEquals(adjacencyList.size(),5);
		HashMap<String, List<String>> expectedStateChildren = new HashMap<String, List<String>>();
		expectedStateChildren.put("start",Arrays.asList("parse"));
		expectedStateChildren.put("guess_mpls_payload",Arrays.asList("parse_dummy"));
		expectedStateChildren.put("parse",Arrays.asList("parse_mpls"));
		expectedStateChildren.put("accept",Arrays.asList("accept"));
		expectedStateChildren.put("parse_dummy",Arrays.asList("accept"));
		expectedStateChildren.put("parse_mpls",Arrays.asList("parse_mpls","guess_mpls_payload"));

		for(Entry<IParserState, List<IParserState>> entry : adjacencyList.entrySet()){
			String parserStateName = entry.getKey().getNameString();
			List<String> parserStateCHildrenName = new ArrayList<String>();
			for(IParserState parserState : entry.getValue()){
				parserStateCHildrenName.add(parserState.getNameString());
			}

			if(expectedStateChildren.containsKey(parserStateName)){
				for(String actualName : parserStateCHildrenName){
					Assert.assertTrue(expectedStateChildren.get(parserStateName).contains(actualName));
				}
			}
		}

	}

	@Test
	public void  InlineTransformationLoopPaths() {
		File inputFile = new File("test/com/p4/p416/inlining/ParserStateInliningData.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

		GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
		graphBuilderVisitor.visit(p4programContext);

		List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

		Assert.assertTrue(loopPaths.size()==1);
		for(List<IParserState>  rootParsePaths1 : loopPaths){
			for(IParserState loopelement : rootParsePaths1){
				Assert.assertTrue(loopelement.getNameString().equals("parse_mpls"));
			}
		}
	}

	//@Test(expected = RuntimeException.class)
	@Test
	public void  InlineInvalidLoopPathsTransformation() {
		try{
			File inputFile = new File("test/com/p4/p416/inlining/ParserStateInliningLoopsData.p4");
			String text = FileUtils.readFileIntoString(inputFile, "\n");
			P416ParserUtil parserUtil = new P416ParserUtil();
			P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
			P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

			GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
			graphBuilderVisitor.visit(p4programContext);
			List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

			List<String> parseStatesInUnionStack = new ArrayList<String>();

			P4programContextExt.getExtendedContext(p4programContext).inlineParserStates(null, loopPaths, parseStatesInUnionStack);

		}catch (RuntimeException e) {
			Assert.assertEquals(e.getMessage(), ("invalid loop with start parseState which is not breakable for parse"));
		}
	}

	@Test
	public void  InlineForIpv6Transformation() {

		File inputFile = new File("test/com/p4/p416/inlining/base2.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

		GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
		graphBuilderVisitor.visit(p4programContext);
		List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

		List<String> parseStatesInUnionStack = new ArrayList<String>();
		parseStatesInUnionStack.add("parse_ipv6ext_hopbyhop");
		parseStatesInUnionStack.add("parse_ipv6ext_routing");

		P4programContextExt.getExtendedContext(p4programContext).inlineParserStates(null, loopPaths, parseStatesInUnionStack);
		for(IParser parserDeclaration: ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getParsers()){
			HashMap<String, IParserState> parserList = parserDeclaration.getParserStatesMap();
			Assert.assertEquals(parserList.keySet().size(), 15);
			String expectedCtx =  FileUtils.ReadFromFile(new File("test/com/p4/p416/inlining/base2_expected.p4"));
			System.out.println(P4programContextExt.getExtendedContext(p4programContext).getFormattedText());
			Assert.assertEquals(expectedCtx, P4programContextExt.getExtendedContext(p4programContext).getFormattedText());
		}
	}

	@Test
	public void  InlineTransformationWithInsufficientLoopBreak() {
		try{
			File inputFile = new File("test/com/p4/p416/inlining/base2.p4");
			String text = FileUtils.readFileIntoString(inputFile, "\n");
			P416ParserUtil parserUtil = new P416ParserUtil();
			P4programContext p4programContext = parserUtil.getP416Context(text, inputFile.getName());
			P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

			GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
			graphBuilderVisitor.visit(p4programContext);
			List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

			List<String> parseStatesInUnionStack = new ArrayList<String>();
			parseStatesInUnionStack.add("parse_ipv6ext_hopbyhop"); // parseStatesInUnionStack should have 2 states, but only one is given

			((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).inlineParserStates(null, loopPaths, parseStatesInUnionStack);

		}catch (RuntimeException e) {
			Assert.assertEquals(e.getMessage(), ("invalid loop with start parseState which is not breakable for parse_ipv6ext_routing"));
		}
	}

}
