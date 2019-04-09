package com.p4.cgen1;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.cgen1.data.Action;
import com.p4.cgen1.data.ActionParameter;
import com.p4.cgen1.data.Control;
import com.p4.cgen1.data.Header;
import com.p4.cgen1.data.KeyElement;
import com.p4.cgen1.data.Parameter;
import com.p4.cgen1.data.StructType;
import com.p4.cgen1.data.Table;
import com.p4.cgen1.runner.CGen1Utils;
import com.p4.p416.gen.ControlBodyContextExt;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IHeader;
import com.p4.p416.iface.IStruct;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

public class GetDataTest {
	private static final Logger L = LoggerFactory.getLogger(GetDataTest.class);

	private static P4programContext p4programContext = null;
	private static Map<String, Integer> phvOffsets = null;

	@BeforeClass
	public static void setUp() {
		String file = P4programContextExt.class.getClassLoader().getResource("com/p4/cgen1/testResources/gendata.p4").getFile();
		File inputFile = new File(file);
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
		P4programContextExt.getExtendedContext(p4programContext).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
	
		String phvFile = P4programContextExt.class.getClassLoader().getResource("com/p4/cgen1/testResources/phvOffsets.json").getFile();
		File phvJsonFile = new File(phvFile);
		phvOffsets = CGen1Utils.readPhvFormatFromJson(phvJsonFile);
	}


	@Test
	public void getControls(){
		List<IControl> iControls = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getControls();
		List<Control> controls = CGen1Utils.getControls(iControls);
		for(Control control: controls){
			List<Parameter> parameters = control.getParameters();
			List<Action> actions = control.getActions();
			List<Table> tables = control.getTables();
			Assert.assertEquals("number of parameters match ",parameters.size(), 1);
			Assert.assertEquals("number of tables match ",tables.size(), 1);
			Assert.assertEquals("number of actions match ",actions.size(), 1);
		}
	}
	
	@Test
	public void getStructureForParameterTest() {
		List<IHeader> iHeaders = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getHeaders();
		List<Header> headers = CGen1Utils.getHeaders(iHeaders);
		List<IStruct> iStructs = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getStructs();
		List<StructType> structTypes = CGen1Utils.getStructTypes(iStructs);
		
		Object obj = CGen1Utils.getDerivedTypeDeclarationParameter(structTypes, headers, "headers");
		Assert.assertTrue("expected header instance",obj instanceof StructType);
		if(obj instanceof StructType){
			StructType expectedStructure = (StructType) obj;
			Assert.assertTrue(" header name match ",expectedStructure.getName().equals("headers"));
		}
	}
	
	@Test
	public void getInOutParametersInControlsTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		List<Control> controls = CGen1Utils.getControls(iControls);
		List<Parameter> parameters = CGen1Utils.getInOutParametersInControls(controls);
		Assert.assertEquals("no of parameters ", 1, parameters.size());
		for(Parameter parameter : parameters){
			Assert.assertEquals("parameter name match ", parameter.getName(), "hdr");
		}
	}
	
	@Test
	public void getValidBitTest() {
		int isValid = phvOffsets.get("hdr.udp.isValid");
		Assert.assertEquals("Isvalid match from phvOffset json", isValid, 4);
	}

	@Test
	public void getHeadersTest() {
		List<IHeader> iHeaders = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getHeaders();
		List<Header> headers = CGen1Utils.getHeaders(iHeaders);
		Assert.assertEquals("no of headers are equal", headers.size(),5);
		List<String> headersExpected = Arrays.asList("ethernet_t","udp_t","ipv4_t","ipv6_t","tcp_t");
		for(Header header : headers){
			Assert.assertTrue("expected headers exist",headersExpected.contains(header.getName()));
		}
	}
	
	@Test
	public void getStructTypeTest() {
		List<IStruct> iStructs = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getStructs();
		List<StructType> structTypes = CGen1Utils.getStructTypes(iStructs);
		Assert.assertEquals("no of structTypes are equal", structTypes.size(),5);
		List<String> structTypesExpected = Arrays.asList("standard_metadata_t","fwd_metadata_t","l3_metadata_t","metadata","headers");
		for(StructType structType : structTypes){
			Assert.assertTrue("expected structTypes exist",structTypesExpected.contains(structType.getName()));
		}
	}

	@Test
	public void getHeadersStructFieldTest() {
		List<IHeader> iHeaders = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getHeaders();
		List<Header> headers = CGen1Utils.getHeaders(iHeaders);
		Assert.assertEquals("no of headers are equal", headers.size(),5);
		HashMap<String, Integer> headersExpected = new HashMap<String, Integer>();
		headersExpected.put("ethernet_t", 3);
		headersExpected.put("udp_t", 4);
		headersExpected.put("ipv4_t", 12);
		headersExpected.put("ipv6_t", 8);
		headersExpected.put("tcp_t", 10);

		for(Header header : headers){
			Assert.assertTrue("expected headers exist",headersExpected.containsKey(header.getName()));
			if(header.getName().equals("ethernet_t")){
				Assert.assertEquals("header structField size match", Integer.toString(header.getStructFields().size()), headersExpected.get(header.getName()).toString());
			}
		}
	}

	@Test
	public void getActionParametersTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		HashMap<String, Integer> actionParametersExpected = new HashMap<String, Integer>();
		actionParametersExpected.put("bd", 24);
		actionParametersExpected.put("dmac", 48);
		actionParametersExpected.put("intf", 2);
		for(IControl icontrol: iControls){
			List<Action> actions = CGen1Utils.getActions(icontrol);
			Assert.assertEquals("no of actions are equal",actions.size(), 1);
			for(Action action : actions){
				List<ActionParameter> actionParameters = action.getActionParameters();
				for(ActionParameter actionParameter : actionParameters){
					Assert.assertTrue("expected actionParameter exist",actionParametersExpected.containsKey(actionParameter.getName()));
					if(actionParametersExpected.containsKey(actionParameter.getName())){
						Assert.assertEquals("actionParameter actionParameters size match", actionParameter.getSize(), actionParametersExpected.get(actionParameter.getName()));
					}
				}
			}
		}
	}

	@Test
	public void keyElementSizeTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		HashMap<String, Integer> keyElementsExpected = new HashMap<String, Integer>();
		keyElementsExpected.put("hdr.ipv4.dstAddr", 32);
		keyElementsExpected.put("hdr.tcp.dstPort", 16);

		for(IControl icontrol: iControls){
			List<Table> tables = CGen1Utils.getTables(icontrol);
			for(Table table : tables){
				List<KeyElement> keyElements = table.getKeyElements();
				Assert.assertEquals("no of keyelements are equal", keyElements.size(), 2);
				for(KeyElement keyElement : keyElements){
					Assert.assertTrue("expected keyElement exist",keyElementsExpected.containsKey(keyElement.getName()));
					if(keyElementsExpected.containsKey(keyElement.getName())){
						Assert.assertEquals("keyElement keyElements size match", keyElement.getSize(), keyElementsExpected.get(keyElement.getName()));
					}
				}
			}
		}
	}


	@Test
	public void getTablesTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl icontrol: iControls){
			List<Table> tables = CGen1Utils.getTables(icontrol);
			Assert.assertEquals("no of tables are equal", tables.size(), 1);
			for(Table table : tables){
				Assert.assertEquals("table name match", table.getName(), "ipv4_da_lpm");
				Assert.assertEquals("table keyElements size match", table.getKeyElements().size(), 2);
				Assert.assertEquals("table tableId match", table.getTableId().intValue(), 1);
			}
		}
	}

	@Test
	public void getParametersTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl icontrol: iControls){
			List<Parameter> parameters = CGen1Utils.getParameters(icontrol);
			Assert.assertTrue(parameters.size()==1);
			for(Parameter parameter : parameters){
				Assert.assertEquals("parameter name match ",parameter.getName(), "hdr");
				Assert.assertEquals("parameter type match ",parameter.getType(), "headers");
				Assert.assertEquals("parameter size match ",parameter.getSize(), null);
			}
		}
	}

	@Test
	public void getActionsTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl icontrol: iControls){
			List<Action> actions = CGen1Utils.getActions(icontrol);
			Assert.assertEquals("no of actions are equal",actions.size(), 1);
			for(Action action : actions){
				Assert.assertEquals("action name match", action.getName(), "set_bd_dmac_intf");
				String blockStatement = String.join("\n", Arrays.asList("{","        meta.fwd_metadata.out_bd = bd;","        hdr.ethernet.dstAddr = dmac;","        standard_metadata.egress_port = intf;","    }"));
				Assert.assertEquals("action blockStatements match", action.getBlockStatements(), blockStatement);
			}
		}
	}

	@Test
	public void getControlBodyTest() {
		List<IControl> iControls = ((P4programContextExt)P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl icontrol: iControls){
			String controlBody = ((ControlBodyContextExt)icontrol.getControlBody()).getCppTransformed().getFormattedText();
			String apply = String.join("\n", Arrays.asList("{",
					"        if( hdr.ethernet.srcAddr==123456 && hdr.ethernet.dstAddr > 2){ ",
					"        	if (hdr.ethernet.srcAddr !=hdr.ethernet.dstAddr) ",
					"            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr;        ",
					"			else if ((hdr.ipv4.isValid()) && !(hdr.ipv6.isValid())) ",
					"            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 2;        ",
					"        	else",
					"            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 3;        ",
					"		}",
					"        ipv4_da_lpm_apply();",
					"    }"));
			Assert.assertEquals("apply body match", apply, controlBody);
		}
	}

}
