package com.p4.cgen1.runner;

import com.p4.ANTLR.generator.Generator;
import com.p4.cgen1.data.Control;
import com.p4.cgen1.data.Header;
import com.p4.cgen1.data.Parameter;
import com.p4.cgen1.data.StructField;
import com.p4.cgen1.data.StructType;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IHeader;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IStruct;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

import freemarker.template.*;
import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CGen1RunnerSession {

	private static final Logger L = LoggerFactory.getLogger(CGen1RunnerSession.class);

	private final CGen1CLIParser clp;
	private List<File> allFiles;
	private File phvOffsetsJsonFile ;
	private Map<String, Integer> phvOffsets = new HashMap<String, Integer>();
	private static Properties props = new Properties();

	private void index() {
		File dir = clp.getOutputDir();
		allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
	}

	public void run() {
		index();
		phvOffsetsJsonFile = clp.getPhvOffsetsFile();
		try {
			InputStream inputStream = Generator.class.getClassLoader().getResourceAsStream("com/p4/cgen1/runner/CGen1.properties");
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ((clp.getOutputDir().exists() || clp.getOutputDir().mkdirs())) {
			for (File file : allFiles) {
				String text = FileUtils.readFileIntoString(file, "\n");
				P416ParserUtil parserUtil = new P416ParserUtil();
				P4programContext p4programContext = parserUtil.getP416Context(text, file.getName());

				P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
				P4programContextExt.getExtendedContext(p4programContext).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
				L.debug("reading json file "+phvOffsetsJsonFile.getAbsolutePath());
				phvOffsets = CGen1Utils.readPhvFormatFromJson(phvOffsetsJsonFile);

				generateCppFiles(p4programContext);

				CGen1Utils.copyResourceFiles(clp.getOutputDir());
			}
		}
	}

	private void generateCppFiles(P4programContext p4programContext){
		IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getP4Program();
		List<IControl> iControls = p4Program.getControls();
		List<IHeader> iHeaders = p4Program.getHeaders();
		List<IStruct>  iStructs  = p4Program.getStructs();

		Configuration cfg = setupFreemarker();
		List<Control> controls = CGen1Utils.getControls(iControls);

		emitControlHeaderFile(cfg, controls);
		emitControlSourceFile(cfg, controls);
		emitEnumsHeaderFile(cfg, controls);
		emitEnumsSourceFile(cfg);
		emitFieldsSourceFile(cfg,controls);

		List<Header> headers = CGen1Utils.getHeaders(iHeaders);
		List<StructType> structTypes = CGen1Utils.getStructTypes(iStructs);
		emitHeadersHeaderFile(cfg, headers, structTypes);
		emitHeadersSourceFile(cfg, headers);

		emitPhvParserHeaderFile(cfg, headers, structTypes, controls);
		emitPhvDeParserHeaderFile(cfg, headers, structTypes, controls);
	}

	
	private void emitHeadersSourceFile(Configuration cfg, List<Header> headers){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("headerSourceTemplate"));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("headers", headers);
		writeFile(template,"headers.cpp",root);
	}

	private void emitHeadersHeaderFile(Configuration cfg, List<Header> headers, List<StructType>  structTypes){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("headerHeaderTemplate"));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("headers", headers);
		root.put("structTypes", structTypes);
		writeFile(template,"headers.hpp",root);
	}

	private void emitFieldsSourceFile(Configuration cfg, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("fieldSourceTemplate"));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("controls", controls);
		writeFile(template,"fields.cpp",root);
	}

	private void emitEnumsSourceFile(Configuration cfg){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("enumsSourceTemplate"));
		Map<String, Object> root = new HashMap<String, Object>();
		writeFile(template,"enums.cpp",root);
	}

	private void emitEnumsHeaderFile(Configuration cfg, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("enumsHeaderTemplate"));//"EnumsHeader.ftl");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("controls", controls);
		writeFile(template,"enums.hpp",root);
	}

	private void emitControlSourceFile(Configuration cfg, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("controlSourceTemplate"));//"ControlSource.ftl");
		Map<String, Object> root = new HashMap<String, Object>();
		for(Control control : controls){
			root.put("control", control);
			writeFile(template,control.getName()+".cpp",root);
		}
	}

	private void emitControlHeaderFile(Configuration cfg, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("controlHeaderTemplate"));//"ControlHeader.ftl");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("controls", controls);
		writeFile(template,"control.hpp",root);
	}
	
	private void emitPhvDeParserHeaderFile(Configuration cfg, List<Header> headers, List<StructType> structTypes, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("phvDeParserTemplate"));//"PhvDeParser.ftl");
		Map<String, Object> root = new HashMap<String, Object>();
		List<Parameter> parameters = CGen1Utils.getInOutParametersInControls(controls);

		HashMap<String, Object> structTypeMap = new HashMap<String, Object>();
		HashMap<String, Header> headersMap = new HashMap<String, Header>();
		for(Parameter parameter: parameters){
			flattenedName = "";
			populateStructuresForPhvParser(structTypes, headers, parameter.getType(), parameter.getName(), structTypeMap, headersMap);
		}
		root.put("structTypeMap", structTypeMap);
		root.put("headersMap", headersMap);
		root.put("parameters", parameters);
		writeFile(template,"phvdeparser.hpp",root);
	}

	private String flattenedName = "";
	private void emitPhvParserHeaderFile(Configuration cfg, List<Header> headers, List<StructType> structTypes, List<Control> controls){
		Template template = CGen1Utils.getTemplate(cfg, props.getProperty("phvParserTemplate"));//"PhvParser.ftl");

		Map<String, Object> root = new HashMap<String, Object>();
		List<Parameter> parameters = CGen1Utils.getInOutParametersInControls(controls);

		HashMap<String, Object> structTypeMap = new HashMap<String, Object>();
		HashMap<String, Header> headersMap = new HashMap<String, Header>();
		for(Parameter parameter: parameters){
			flattenedName = "";
			populateStructuresForPhvParser(structTypes, headers, parameter.getType(), parameter.getName(), structTypeMap, headersMap);
		}

		root.put("structTypeMap", structTypeMap);
		root.put("headersMap", headersMap);
		root.put("parameters", parameters);
		writeFile(template,"phvparser.hpp",root);
	}

	private void populateStructuresForPhvParser(List<StructType> structTypes, List<Header> headers, String paramType, String paramName, HashMap<String, Object> structTypeMap, HashMap<String, Header> headersMap){
		Object obj = CGen1Utils.getDerivedTypeDeclarationParameter(structTypes, headers, paramType);
		flattenedName = flattenedName.equals("")? paramName : flattenedName+"."+paramName;
		if(obj instanceof StructType){
			StructType structType = (StructType)obj;
			List<StructField> structFields = structType.getStructFields();
			for(StructField structField: structFields){
				if(structField.getIsBaseType()){
					populateOffsets(structField, flattenedName+"."+structField.getName());
				}else if (structField.getIsTypeNameType()){
					populateStructuresForPhvParser(structTypes, headers, structField.getPrefixedType(), structField.getName(), structTypeMap, headersMap);
					flattenedName = flattenedName.substring(0,flattenedName.lastIndexOf('.')).trim();
				}
			}
			structTypeMap.put(paramName, structType);
		} if(obj instanceof Header){
			Header header = (Header)obj;
			List<StructField> structFields = header.getStructFields();
			for(StructField structField: structFields){
				if(structField.getIsBaseType()){
					populateOffsets(structField, flattenedName+"."+structField.getName());
				}else if (structField.getIsTypeNameType()){
					populateStructuresForPhvParser(structTypes, headers, structField.getName(), structField.getName(), structTypeMap, headersMap);
					flattenedName = flattenedName.substring(0,flattenedName.lastIndexOf('.') ).trim();
				}
			}
			Integer isValid = getValidBitFromPhvOffsets(flattenedName);
			header.setIsValid(isValid);
			headersMap.put(paramName, header);
		}
	}

	private void writeFile(Template template,String filename,Object root){
		File file = new File(clp.getOutputDir().toString() + "/" + filename);
		try {
			Writer writer;
			writer = new FileWriter (file);
			template.process(root, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	public static Configuration setupFreemarker() {
		Configuration cfg = new Configuration(new Version("2.3.23"));
		try {
			cfg.setDirectoryForTemplateLoading(new File(props.getProperty("templatesDir")));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to setup freemarker template directory.", e);
		}
		return cfg;
	}
	
	private Integer getValidBitFromPhvOffsets(String flattenedName){
		String keyName = flattenedName+".isValid";
		if(phvOffsets.get(keyName) != null){
			L.debug("flattenedName is "+keyName+" valid bit is "+ phvOffsets.get(keyName));
			Integer isValid = phvOffsets.get(keyName);
			return isValid;
		}else{
			L.error("valid bit for key "+keyName+" not found in "+phvOffsetsJsonFile.getAbsolutePath());
		}
		return null;
	}

	private void populateOffsets(StructField structField, String flattenedName){
		if(phvOffsets.get(flattenedName) != null){
			L.debug("flattenedName "+ flattenedName+" offset is "+phvOffsets.get(flattenedName));
			Integer startSize = phvOffsets.get(flattenedName)*8;
			structField.setStartSize(startSize);
			Integer endSize = startSize+(structField.getSize())-1;
			structField.setEndSize(endSize);
		}else{
			L.error("value for key "+flattenedName+" not found in "+phvOffsetsJsonFile.getAbsolutePath());
		}
	}
}
