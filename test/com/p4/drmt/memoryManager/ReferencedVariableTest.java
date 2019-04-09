package com.p4.drmt.memoryManager;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Lexer;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.PopulateExtendedContextVisitor;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

@SuppressWarnings("serial")
public class ReferencedVariableTest {
	private static final Logger L = LoggerFactory.getLogger(ReferencedVariableTest.class);

	private static P4programContext p4programContext = null;

	private HashMap<String, Integer> cfgBitOffsetMap = new HashMap<String, Integer>(){{
	}};

	private HashMap<String, Integer> cfgByteOffsetMap = new HashMap<String, Integer>(){{
		put("8", 20);
		put("123456", 8);
		put("256", 0);
		put("2", 4);
		put("1", 24);
		put("3", 16);
		put("123454", 12);
		put("4", 28);

	}};

	private HashMap<String, Integer> pktByteOffsetMap = new HashMap<String, Integer>(){{
		put("hdr.tcp.dstPort", 0);
		put("hdrExtIp.ipv4.srcAddr", 12);
		put("hdrExtIp.ipv6.srcAddr", 4);
		put("meta.hdrExtIpStruct.ipv6.srcAddr", 4);
		put("hdrExtIp.ipv4.protocol", 4);
		put("hdrExtIp.ipv6.nextHdr", 20);
		put("meta.hdrExtIpStruct.ipv4.srcAddr", 24);
		put("meta.fwd_metadata.out_bd", 52);
		put("hdr.ethernet.dstAddr", 40);
		put("hdr.ipv4.srcAddr", 36);
		put("hdr.ethernet.srcAddr", 28);
		put("meta.fwd_metadata.l2ptr", 48);
		put("hdr.ipv4.dstAddr", 56);

	}};

	private HashMap<String, Integer> pktBitOffsetMap = new HashMap<String, Integer>(){{
		put("meta.hdrExtIpStruct.ipv4.testBit", 0);
		put("hdr.tcp.oneBit", 1);
		put("hdr.ethernet.isValid", 2);
		put("hdr.ipv4.isValid", 3);
		put("hdr.ipv6.isValid", 4);
		put("hdr.tcp.isValid", 5);
		put("hdr.udp.isValid", 6);
		put("hdrExtIp.isValid", 7);
		put("hdrExtIp.ipv4.isValid",8);
		put("hdrExtIp.ipv6.isValid", 9);
		put("meta.hdrExtIpStruct.isValid", 10);
		put("meta.hdrExtIpStruct.ipv4.isValid", 11);
		put("meta.hdrExtIpStruct.ipv6.isValid", 12);
		put("meta.dummyStruct.ethernet.isValid", 13);
		put("meta.dummyStruct.ipv4.isValid", 14);
	}};

	@BeforeClass
	public static void setUp() {
		File inputFile = new File("test/com/p4/drmt/memoryManager/ReferenceVariableTest.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

	}

	@Test
	public void referencedVariableTest(){
		ReferencedVariableVisitor referencedVariableVisitor = new ReferencedVariableVisitor();
		referencedVariableVisitor.visit(p4programContext);
		Map<IMemoryInstance, Symbol> referencedSymbols = referencedVariableVisitor.getReferencedSymbols();

		Assert.assertEquals("expected reference variable count matches ", referencedSymbols.size(), 27);
		List<String> expectedReferencedVariables = Arrays.asList("hdr.ipv4.srcAddr", "256",
				"hdr.ipv4.dstAddr", "hdr.tcp.dstPort", "hdr.ethernet.dstAddr", "hdr.ethernet.srcAddr", "123456", "2", "3", "l2ptr", "out_bd", "bd", "intf",
				"dmac", "meta.fwd_metadata.out_bd", "meta.fwd_metadata.l2ptr", "smac",
				"123454", "hdrExtIp.ipv4.srcAddr", "hdrExtIp.ipv6.srcAddr", "hdrExtIp.ipv6.nextHdr", "hdrExtIp.ipv4.protocol",
				//"meta.hdrExtIpStruct.ipv6.srcAddr", 
				"meta.hdrExtIpStruct.ipv4.srcAddr", "meta.hdrExtIpStruct.ipv4.testBit", "4", "8",
				"hdr.tcp.oneBit", "1");

		for(IMemoryInstance memoryInstance : referencedSymbols.keySet()){
			L.debug("symbol searched in expected symbols "+memoryInstance.getInstanceName()+" "+memoryInstance.getMemoryType()+" "+memoryInstance.getMemoryType()+" "+referencedSymbols.get(memoryInstance).getSymbolName());
			Assert.assertTrue("symbol doesnot exist in expected symbols "+memoryInstance.getInstanceName(), expectedReferencedVariables.contains(memoryInstance.getInstanceName()));
		}

	}

	@Test
	public void getDisjointSetsTest(){
		ReferencedVariableVisitor referencedVariableVisitor = new ReferencedVariableVisitor();
		referencedVariableVisitor.visit(p4programContext);

		Map<IMemoryInstance, Symbol> referencedSymbols = referencedVariableVisitor.getReferencedSymbols();
		FetchDisjointSets fetchDisjointSets = new FetchDisjointSets();
		List<IDisjointSet<IMemoryInstance>> disjointSets = fetchDisjointSets.getDisjointSets(referencedSymbols);

		Assert.assertTrue("number of disjoint sets doesnot match "+disjointSets.size(),disjointSets.size()==4);

	}

	@Test
	public void MemoryAllocationTest(){
		//not handling scratch type
		ReferencedVariableVisitor referencedVariableVisitor = new ReferencedVariableVisitor();
		referencedVariableVisitor.visit(p4programContext);

		Map<IMemoryInstance, Symbol> referencedSymbols = referencedVariableVisitor.getReferencedSymbols();
		FetchDisjointSets fetchDisjointSets = new FetchDisjointSets();
		List<IDisjointSet<IMemoryInstance>> disjointSets = fetchDisjointSets.getDisjointSets(referencedSymbols);

		MemoryManager.allocateMemory(disjointSets, null);
		MemoryManager.printMemoryAllocated();
		for(Entry<IMemoryInstance, Symbol> referencedSymbol : referencedSymbols.entrySet()){

			if(referencedSymbol.getKey().getMemoryType().name().equals("TypePktByte")){
				Integer offset = MemoryManager.getOffset(referencedSymbol.getKey().getInstanceName(), referencedSymbol.getKey().getType());
				L.debug("offset of "+ referencedSymbol.getKey().getInstanceName()+" offset is "+ offset);
				String instanceName = referencedSymbol.getKey().getInstanceName();
				Assert.assertTrue("TypePktByte offset didnotMatch for "+instanceName +" expected is "+ offset +" and got is "+pktByteOffsetMap.get(instanceName), pktByteOffsetMap.get(instanceName)==offset);
			}else if(referencedSymbol.getKey().getMemoryType().name().equals("TypeCfgByte")){
				Integer offset = MemoryManager.getOffset(referencedSymbol.getKey().getInstanceName(), referencedSymbol.getKey().getType());
				L.debug("offset of "+ referencedSymbol.getKey().getInstanceName()+" offset is "+ offset);
				String instanceName = referencedSymbol.getKey().getInstanceName();
				Assert.assertTrue("TypeCfgByte offset didnotMatch for "+instanceName +" expected is "+ offset +" and got is "+cfgByteOffsetMap.get(instanceName), cfgByteOffsetMap.get(instanceName)==offset);
			}else if(referencedSymbol.getKey().getMemoryType().name().equals("TypeCfgBit")){
				Integer offset = MemoryManager.getOffset(referencedSymbol.getKey().getInstanceName(), referencedSymbol.getKey().getType());
				L.debug("offset of "+ referencedSymbol.getKey().getInstanceName()+" offset is "+ offset);
				String instanceName = referencedSymbol.getKey().getInstanceName();
				Assert.assertTrue("TypeCfgBit offset didnotMatch for "+instanceName +" expected is "+ offset +" and got is "+cfgBitOffsetMap.get(instanceName), cfgBitOffsetMap.get(instanceName)==offset);
			}else if(referencedSymbol.getKey().getType().getMemoryType().name().equals("TypePktBit")){
				Integer offset = MemoryManager.getOffset(referencedSymbol.getKey().getInstanceName(), referencedSymbol.getKey().getType());
				L.debug("offset of "+ referencedSymbol.getKey().getInstanceName()+" offset is "+ offset);
				String instanceName = referencedSymbol.getKey().getInstanceName();
				Assert.assertTrue("TypePktBit offset didnotMatch for "+instanceName +" expected is "+ offset +" and got is "+pktBitOffsetMap.get(instanceName), pktBitOffsetMap.get(instanceName)==offset);
			}
		}
	}

	@Test
	public void isValidSymbolsTest(){

		ReferencedVariableVisitor referencedVariableVisitor = new ReferencedVariableVisitor();
		referencedVariableVisitor.visit(p4programContext);

		Map<IMemoryInstance, Symbol> referencedSymbols = referencedVariableVisitor.getReferencedSymbols();
		FetchDisjointSets fetchDisjointSets = new FetchDisjointSets();
		List<IDisjointSet<IMemoryInstance>> disjointSets = fetchDisjointSets.getDisjointSets(referencedSymbols);
		Map<String, IMemoryInstance> isValidInstances = fetchDisjointSets.getIsValidInstances();

		MemoryManager.allocateMemory(disjointSets, null);
		//MemoryManager.printMemoryAllocated();

		Assert.assertTrue("size of isValidInstances "+isValidInstances.size(), isValidInstances.size()==13);
		for(Entry<String, IMemoryInstance> validInstance : isValidInstances.entrySet()){

			Assert.assertTrue("unexpected isValid bit "+ validInstance,pktBitOffsetMap.containsKey(validInstance.getKey()));

			Integer offset = MemoryManager.getOffset(validInstance.getValue().getInstanceName(), validInstance.getValue().getType());
			L.debug("offset of "+ validInstance.getKey()+" offset is "+ offset);
			Assert.assertTrue("", pktBitOffsetMap.get(validInstance.getKey())==offset);

		}

	}

	@Test
	public void MemoryAllocationP4(){
		//test for transformed p4

		File inputFile = new File("test/com/p4/drmt/memoryManager/Test.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
		
		ReferencedVariableVisitor newReferencedVariableVisitor = new ReferencedVariableVisitor();
		newReferencedVariableVisitor.visit(p4programContext);
		
		Map<IMemoryInstance, Symbol> referencedSymbols = newReferencedVariableVisitor.getReferencedSymbols();
		FetchDisjointSets fetchDisjointSets = new FetchDisjointSets();
		List<IDisjointSet<IMemoryInstance>> disjointSets = fetchDisjointSets.getDisjointSets(referencedSymbols);

		MemoryManager.allocateMemory(disjointSets, null);
		MemoryManager.printMemoryAllocated();

		Assert.assertEquals("expected reference variable count matches ", referencedSymbols.size(), 2);

		String newContext = "header udp_t {	bit<16> srcPort; bit<16> dstPort; }"
				+ "struct headers { udp_t udp; } "
				+ "headers() hdr; "
				+ "control ingress(inout headers hdr){ "
				+ "apply { "
				+ "hdr.udp.srcPort = 1; "
				+ "hdr.udp.dstPort = 1;}"
				+ " }";
		P416Lexer lexer = new P416Lexer(new ANTLRInputStream(newContext));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		P416Parser p416Parser =  new P416Parser(tokens);

		P4programContext newP4ProgramContext =  (P4programContext)new PopulateExtendedContextVisitor().visit(p416Parser.p4program());
		P4programContextExt.getExtendedContext(p4programContext).setContext(newP4ProgramContext);
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);

		newReferencedVariableVisitor = new ReferencedVariableVisitor();
		newReferencedVariableVisitor.visit(p4programContext);
		
		referencedSymbols = newReferencedVariableVisitor.getReferencedSymbols();
		fetchDisjointSets = new FetchDisjointSets();
		disjointSets = fetchDisjointSets.getDisjointSets(referencedSymbols);

		MemoryManager.allocateMemory(disjointSets, null);
		MemoryManager.printMemoryAllocated();

		Assert.assertEquals("expected reference variable count matches ", referencedSymbols.size(), 3);
	}

}
