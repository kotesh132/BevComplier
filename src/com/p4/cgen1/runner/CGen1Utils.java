package com.p4.cgen1.runner;

import static java.lang.System.exit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.cgen1.data.Action;
import com.p4.cgen1.data.ActionParameter;
import com.p4.cgen1.data.Control;
import com.p4.cgen1.data.Header;
import com.p4.cgen1.data.KeyElement;
import com.p4.cgen1.data.Parameter;
import com.p4.cgen1.data.StructField;
import com.p4.cgen1.data.StructType;
import com.p4.cgen1.data.Table;
import com.p4.p416.gen.ControlBodyContextExt;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IHeader;
import com.p4.p416.iface.IKeyElement;
import com.p4.p416.iface.IParameter;
import com.p4.p416.iface.IStruct;
import com.p4.p416.iface.IStructField;
import com.p4.p416.iface.ITable;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CGen1Utils {
	private static final Logger L = LoggerFactory.getLogger(CGen1Utils.class);
	private static Integer actionNumber  = 0;

	public static List<Table> getTables(IControl icontrol){
		List<Table> tables = new ArrayList<Table>();
		for(ITable  iTable : icontrol.getTableObjects()){
			List<KeyElement> keyElements = getKeyElements(iTable);
			tables.add(new Table(iTable,keyElements));
		}
		return tables;
	}

	public static List<KeyElement> getKeyElements(ITable itable){
		List<KeyElement> keyElements = new ArrayList<KeyElement>();
		for(IKeyElement iKeyElement : itable.getKeyElements()){
			keyElements.add(new KeyElement(iKeyElement));
		}
		return keyElements;
	}

	public static StructType getStructType(IStruct iStruct){
		List<IStructField> iStructFields = iStruct.getStructFields();
		List<StructField> structFields = new ArrayList<StructField>();
		for(IStructField iStructField : iStructFields){
			StructField structField = new StructField(iStructField);
			structFields.add(structField);
		}
		StructType structType = new StructType(iStruct, structFields);
		return structType;
	}

	public static Header getHeader(IHeader iHeader){
		List<IStructField> iStructFields = iHeader.getStructFields();
		List<StructField> structFields = new ArrayList<StructField>();
		for(IStructField iStructField : iStructFields){
			StructField structField = new StructField(iStructField);
			structFields.add(structField);
		}
		Header header = new Header(iHeader, structFields);
		return header;
	}

	public static List<Action> getActions(IControl icontrol){
		List<Action> actions = new ArrayList<Action>();
		for(IActionDeclaration iActionDeclaration : icontrol.getActionDeclarations()){
			List<ActionParameter> actionParameters = getActionParameters(iActionDeclaration);
			actions.add(new Action(iActionDeclaration, actionParameters));
		}
		return actions;
	}

	public static List<ActionParameter> getActionParameters(IActionDeclaration iActionDeclaration) {
		List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		for(IParameter parameter : iActionDeclaration.getParameters()){
			parameters.add(new ActionParameter(parameter));
		}
		return parameters;
	}

	public static List<Parameter> getParameters(IActionDeclaration iActionDeclaration) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(IParameter parameter : iActionDeclaration.getParameters()){
			parameters.add(new Parameter(parameter));
		}
		return parameters;
	}

	public static List<Parameter> getParameters(IControl icontrol) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(IParameter parameter : icontrol.getParameters()){
			parameters.add(new Parameter(parameter));
		}
		return parameters;
	}

	public static List<Control> getControls(List<IControl> iControls) {
		List<Control> controls  = new ArrayList<Control>();
		for(IControl icontrol: iControls){
			String controlBody = ((ControlBodyContextExt) icontrol.getControlBody()).getCppTransformed().getFormattedText();
			List<Parameter> parameters = CGen1Utils.getParameters(icontrol);
			List<Action> actions = CGen1Utils.getActions(icontrol);
			List<Table> tables = CGen1Utils.getTables(icontrol);
			Control control = new Control(icontrol, parameters, actions, tables, controlBody, actionNumber);
			actionNumber = actionNumber + actions.size();
			controls.add(control);
		}
		return controls;
	}

	public static List<Header> getHeaders(List<IHeader> iHeaders) {
		List<Header> headers = new ArrayList<Header>();
		for(IHeader iHeader: iHeaders){
			Header header = CGen1Utils.getHeader(iHeader);
			headers.add(header);
		}
		return headers;
	}

	public static List<StructType> getStructTypes(List<IStruct> iStructs) {
		List<StructType> structTypes = new ArrayList<StructType>();
		for(IStruct iStruct: iStructs){
			StructType structType = CGen1Utils.getStructType(iStruct);
			structTypes.add(structType);
		}
		return structTypes;
	}

	public static Template getTemplate(Configuration cfg, String ftl) {
		Template template = null;
		try {
			template = cfg.getTemplate(ftl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return template;
	}

	private final static String RESOURCE_DIR = "templates/cgen1/resources";

	public static void copyResourceFiles(File outputDir) {
		for(String filename : getResources()){
			try {
				FileUtils.copyFile(new File(RESOURCE_DIR+ File.separator+ filename), new File(outputDir.getAbsoluteFile() + File.separator + filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static List<String> getResources() {
		//Choose the files which we want to push to generated files
		List<String> fileNames = Arrays.asList(
				"pd.cpp",
				"pd.hpp",
				"primitives.cpp",
				"primitives.hpp",
				"utility.cpp",
				"utility.hpp",
				"phvdeparser.cpp",
				"phvparser.cpp",
				"header_stack.cpp",
				"header_stack.hpp",
				"main.cpp"
				);

		return fileNames;
	}

	public static Object getDerivedTypeDeclarationParameter(List<StructType> structTypes, List<Header> headers,
			String parameterName) {
		for(StructType structType : structTypes){
			if(structType.getName().equals(parameterName)){
				return structType;
			}
		}

		for(Header header : headers){
			if(header.getName().equals(parameterName)){
				return header;
			}
		}
		return null;
	}
	
	public static Boolean parameterContainsInlist(List<Parameter> parameters, Parameter parameter){
		for(Parameter param : parameters){
			if(param.getType().equals(parameter.getType())){
				return true;
			}
		}
		return false;
	}
	
	public static List<Parameter> getInOutParametersInControls(List<Control> controls){
		List<Parameter> parameters = new ArrayList<Parameter>();
		for(Control control : controls){
			for(Parameter parameter : control.getParameters()){
				if(!parameterContainsInlist(parameters, parameter)){
					parameters.add(parameter);
				}
			}
		}
		return parameters;
	}

	public static Map<String, Integer> readPhvFormatFromJson(File phvOffsetsJsonFile){
		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<String, Integer> map  = new LinkedHashMap<>();
		try {
			if (!phvOffsetsJsonFile.exists()) {
				L.error("Packet vector layout json file not available");
				throw new RuntimeException("Packet vector layout json file not available");
			}
			map = mapper.readValue(phvOffsetsJsonFile, LinkedHashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e){
			e.printStackTrace();
			exit(1);
		}
		return map;
	}

}
