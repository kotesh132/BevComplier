package com.p4.cgen.header;

import com.p4.cgen.body.FieldsFile;
import com.p4.cgen.body.MainFile;
import com.p4.cgen.body.NamedControlFile;
import com.p4.cgen.body.PHVDeparserFile;
import com.p4.cgen.body.PHVParserFile;
import com.p4.cgen.interfaces.ICFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IBlockStatement;
import com.p4.p416.iface.ICTransformable;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IIRNode;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IParameter;
import org.apache.commons.io.FileUtils;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class BaseCFile implements ICFile {

    protected final static String STMTS = "stmts";
    protected final static String TOP = "top";
    private final static String RESOURCE_DIR = "templates/p416/c/resources";

    static protected Map<String, String> controlMethodToArgumentsMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToStaticVarMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToCallParamsMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToArgumentPtrParamsMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToCopyInArgInitMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToCopyOutArgInitMap = new LinkedHashMap<>();
    static protected Map<String, String> controlMethodToLocalVarMap = new LinkedHashMap<>();

    private static File outputDir;

    protected static File phvOffsetsJsonFile;

    protected void writeTop(String fileName, ST top) {
        File file = new File(outputDir + "/" + fileName);


        if (file.exists()) {
            com.p4.utils.FileUtils.Delete(file, true);
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        com.p4.utils.FileUtils.writeToFile(file, false, top.render());

    }

    public static void setOutputDir(File dir) {
        outputDir = dir;
    }

    public static void setPhvOffsetsJsonFile(File jsonFile) {
        phvOffsetsJsonFile = jsonFile;
    }

    public static void generateFiles(IP4Program p4Program) {

        populateControlMethodParams(p4Program);

        //main
        new MainFile().generateFile(p4Program);

        //generate headers
        new HeadersHFile().generateFile(p4Program);
        new EnumsHFile().generateFile(p4Program);
        new FieldsHFile().generateFile(p4Program);
        new ControlHFile().generateFile(p4Program);
        new TablesHFile().generateFile(p4Program);
        new ActionsHFile().generateFile(p4Program);
        new PHVParserHFile().generateFile(p4Program);
        new PHVDeparserHFile().generateFile(p4Program);

        //generate bodies
        new FieldsFile().generateFile(p4Program);
//        new ControlFile().generateFile(p4Program);
//        new TablesFile().generateFile(p4Program);
//        new ActionsFile().generateFile(p4Program);
        new PHVParserFile().generateFile(p4Program);
        new PHVDeparserFile().generateFile(p4Program);
        new NamedControlFile().generateFile(p4Program);


        clearControlMethodParams();
    }

    private static void clearControlMethodParams() {
        controlMethodToArgumentsMap.clear();
        controlMethodToCallParamsMap.clear();
        controlMethodToArgumentPtrParamsMap.clear();
        controlMethodToCopyInArgInitMap.clear();
    }

    public static void copyResourceFiles() {
        URL resourceDir = BaseCFile.class.getClassLoader().getResource(RESOURCE_DIR);

        if (resourceDir != null) {
            List<URL> resources = getResources(RESOURCE_DIR);
            for (URL resource : resources) {
                String dstFileName = new File(resource.getFile()).getName();
                try {
                    FileUtils.copyURLToFile(resource, new File(outputDir.getAbsoluteFile() + File.separator + dstFileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("Resource Director " + RESOURCE_DIR + " not found");
        }

    }

    private static List<URL> getResources(final String path) {

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //Choose the files which we want to push to generated files
        List<String> fileNames = Arrays.asList(
                "checksums.c",
                "checksums.h",
                "commands.txt",
                "iPhv.data",
                "iPhvBit.data",
                "Makefile",
                "pd.c",
                "pd.h",
                "primitives.c",
                "primitives.h",
                "utility.c",
                "utility.h",
                "vPhv.data",
                "vPhvBit.data");

        return fileNames.stream()
                .map(l -> path + "/" + l)
                .map(loader::getResource)
                .collect(toList());
    }

    private static void populateControlMethodParams(IP4Program p4Program) {

        clearControlMethodParams();
        List<IControl> controls = p4Program.getControls();

        IControl deparser = p4Program.getDeparser().get(0);
        controls.remove(deparser);
        for (IControl control : controls) {
            List<IParameter> parameters = control.getParameters();

            if (parameters != null) {
                List<String> params = new ArrayList<>();
                List<String> params1 = new ArrayList<>();
                List<String> params2 = new ArrayList<>();
                List<String> params3 = new ArrayList<>();
                List<String> params4 = new ArrayList<>();
                List<String> params5 = new ArrayList<>();
                for (IParameter parameter : parameters) {
                    String nameString = parameter.getNameString();
                    params.add(parameter.getTypeRef().getPrefixedType() + "* " + nameString);
                    params1.add(nameString + "_ptr");
                    params2.add(parameter.getTypeRef().getPrefixedType() + "* " + nameString + "_ptr");
                    params3.add("\t" + nameString + " = " + "*" + nameString + "_ptr;");
                    params4.add("\t" + "*" + nameString + "_ptr" + " = " + nameString + ";");
                    params5.add(("static " + parameter.getTypeRef().getPrefixedType() + " " + nameString + ";"));
                }
                controlMethodToArgumentsMap.put(control.getNameString(), CGenUtils.joinStrings(params, ", "));
                controlMethodToCallParamsMap.put(control.getNameString(), CGenUtils.joinStrings(params1, ", "));
                controlMethodToArgumentPtrParamsMap.put(control.getNameString(), CGenUtils.joinStrings(params2, ", "));
                controlMethodToCopyInArgInitMap.put(control.getNameString(), CGenUtils.joinStrings(params3, "\n"));
                controlMethodToCopyOutArgInitMap.put(control.getNameString(), CGenUtils.joinStrings(params4, "\n"));
                controlMethodToStaticVarMap.put(control.getNameString(), CGenUtils.joinStrings(params5, "\n"));
            }
        }
    }

    protected String getCType(int bitSize) {
        int byteSize = (bitSize + 7) / 8;
        switch (byteSize) {
            case 1:
                return "uint8_t";
            case 2:
                return "uint16_t";
            case 3:
            case 4:
                return "uint32_t";
            default:
                return "uint8_t";
        }
    }

    protected String getCVarArraySize(int bitSize) {
        int byteSize = (bitSize + 7) / 8;
        switch (byteSize) {
            case 1:
            case 2:
            case 3:
            case 4:
                return "";
            default:
                return "[" + byteSize + "]";
        }
    }

    protected String buildBlockStatementBody(IBlockStatement blockStatement, LinkedHashMap<String, String> map) {

        transformToCCodeDriver(blockStatement, map);

        String body = blockStatement.getFormattedText();

        body = body.replaceFirst("\\{", "");

        int ind = body.lastIndexOf("}");
        if (ind >= 0)
            body = new StringBuilder(body).replace(ind, ind + 1, "").toString();

        body = body.replaceAll("BREAK_SWITCH\\(\\)", "break");
        body = body.replaceAll("BR_UINT8_T_STAR_BR", "(uint8_t *)");
        body = body.replaceAll("UINT8_T_STAR", "uint8_t*");
        body = body.replaceAll("CASE_", "case ");
        body = body.replaceAll("isValid\\(\\)", "isValid");
        body = body.replaceAll("AMPERSAND", "&");

        return body;
    }

    protected String getRedirectActionParamConversionCnt(String paramName, int bitOffset, int bitSize) {
        int byteSize = (bitSize + 7) / 8;
        String type = getCType(bitSize);

        if (byteSize > 4) {
            String nonDefaultBody = "\tuint8_t " + paramName + "[" + byteSize + "];\n" +
                    "\tEXTRACT_BITS(data, " + paramName + ", " + bitOffset + ", " + bitSize + ");\n";
            String printMsg = "\tprint_hex(\"" + paramName + ": \", " + paramName + ", " + byteSize + ");\n\n";
            return nonDefaultBody + printMsg;
        } else {
            String defaultBody = "\t" + type + " " + paramName + ";\n" +
                    "\tEXTRACT_BITS(data, (uint8_t *)&" + paramName + "," + bitOffset + ", " + bitSize + ");\n";
            String printMsg = "\tprint_hex(\"" + paramName + ": \", " + "(uint8_t *)&" + paramName + ", " + byteSize + ");\n\n";
            return defaultBody + printMsg;
        }

    }

    protected List<String> getOrderedControlActionNames(IP4Program p4Program) {

        List<IControl> controls = p4Program.getControls();
        SortedMap<IActionDeclaration, String> actionToControl = new TreeMap<>(Comparator.comparing(IActionDeclaration::getActionId));

        for (IControl control : controls) {
            List<IActionDeclaration> actionDecList = control.getActionDeclarations();
            for (IActionDeclaration actionDeclaration : actionDecList) {
                actionToControl.put(actionDeclaration, control.getNameString());
            }
        }

        List<String> orderedControlActionNames = actionToControl.keySet()
                .stream()
                .map(action -> actionToControl.get(action) + "_" + action.getNameString())
                .collect(Collectors.toList());

        return orderedControlActionNames;
    }

    protected void transformToCCodeDriver(IIRNode node, LinkedHashMap<String, String> map) {
        node.postVisitNode(enode -> {
            if (enode instanceof ICTransformable) {
                ((ICTransformable) enode).transformToCCode(map);
            }
        });
    }
}
