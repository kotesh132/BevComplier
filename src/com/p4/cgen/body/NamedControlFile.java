package com.p4.cgen.body;

import com.p4.cgen.header.BaseCFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IBlockStatement;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IKeyElement;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IParameter;
import com.p4.p416.iface.ITable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NamedControlFile extends BaseCFile {

    private String namedControlFileName = "";
    private static STGroupFile namedControlStg = new STGroupFile("templates/p416/c/namedControl.stg");

    @Override
    public String getFileName() {
        return namedControlFileName;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<IControl> controls = p4Program.getControls();
        IControl deparser = p4Program.getDeparser().get(0);
        controls.remove(deparser);
        //Currently trying to process fixed control blocks. It is not necessary though, until we clean up unrolling
//        List<String> unrolledControlBlocks = Arrays.asList("ingress", "egress", "computeChecksum", "verifyChecksum");
        for (IControl control : controls) {
            String controlName = control.getNameString();
//            if (unrolledControlBlocks.contains(controlName)) {
                processControlBlock(control);
//            }
        }

    }

    private void processControlBlock(IControl control) {
        String controlName = control.getNameString();
        namedControlFileName = controlName + "_control.c";
        LinkedHashMap<String, String> extractedMap = new LinkedHashMap<>();
        String controlBody = buildBlockStatementBody(control.getControlBody().getBlockStatement(), extractedMap);

        Map<String, String> actionToParamsMap = new LinkedHashMap<>();
        Map<String, String> actionToRedirectActionBodyMap = new LinkedHashMap<>();
        Map<String, String> actionToActionBodyMap = new LinkedHashMap<>();

        populateActionsData(control, actionToParamsMap, actionToRedirectActionBodyMap, actionToActionBodyMap, extractedMap);

        List<ITable> tableObjects = control.getTableObjects();
        for (ITable tableObject : tableObjects) {
            List<IKeyElement> keyElements = tableObject.getKeyElements();
            for (IKeyElement keyElement : keyElements) {
                //To transform isValid() methods
                //This Also transfroms range expression int values to hex values
                transformToCCodeDriver(keyElement, extractedMap);
            }
        }

        List<String> extractedMethods = extractedMap.keySet()
                .stream()
                .filter(key -> key.contains("_method"))
                .map(extractedMap::get)
                .collect(Collectors.toList());

        List<String> extractedVariables = extractedMap.keySet()
                .stream()
                .filter(key -> key.contains("_variable"))
                .map(extractedMap::get)
                .collect(Collectors.toList());

        List<String> staticVarList = new ArrayList<>();
        staticVarList.add(controlMethodToStaticVarMap.get(controlName));
        staticVarList.addAll(extractedVariables);
        String staticVar = CGenUtils.joinStrings(staticVarList, "\n");



        writeNamedControl(controlName,
                controlMethodToArgumentPtrParamsMap.get(controlName),
                controlBody,
                controlMethodToCopyInArgInitMap.get(controlName),
                controlMethodToCopyOutArgInitMap.get(controlName),
                staticVar,
                tableObjects,
                actionToParamsMap,
                actionToRedirectActionBodyMap,
                actionToActionBodyMap,
                extractedMethods);
    }

    private void populateActionsData(IControl control, Map<String, String> actionToParamsMap,
                                     Map<String, String> actionToRedirectActionBodyMap,
                                     Map<String, String> actionToActionBodyMap,
                                     LinkedHashMap<String, String> extractedMap) {
        String controlName = control.getNameString();
        List<IActionDeclaration> actionDeclarations = control.getActionDeclarations();
        for (IActionDeclaration actionDeclaration : actionDeclarations) {
            List<String> params = new ArrayList<>();
            List<String> callableParams = new ArrayList<>();
            StringBuilder redirectActionBody = new StringBuilder();
            int bitOffset = 0;
            for (IParameter parameter : actionDeclaration.getParameters()) {
                String sizeString = parameter.getTypeRef().getSize();
                String parameterNameString = parameter.getNameString();
                if (sizeString != null) {
                    int size = Integer.parseInt(sizeString);
                    params.add(getCType(size) + " " + parameterNameString + getCVarArraySize(size));
                    redirectActionBody.append(getRedirectActionParamConversionCnt(parameterNameString, bitOffset, size));
                    bitOffset += size;
                } else {
                    String typeString = parameter.getTypeRef().getPrefixedType();
                    params.add(typeString + " " + parameterNameString);
                }
                callableParams.add(parameterNameString);
            }

            String actionControlName = controlName + "_" + actionDeclaration.getNameString();

            actionToParamsMap.put(actionControlName, CGenUtils.joinStrings(params, ", "));

            redirectActionBody.append("\t").append(actionControlName).append("(").append(CGenUtils.joinStrings(callableParams, ", ")).append(");");
            actionToRedirectActionBodyMap.put(actionControlName, redirectActionBody.toString());

            IBlockStatement blockStatement = actionDeclaration.getBlockStatement();
            actionToActionBodyMap.put(actionControlName, buildBlockStatementBody(blockStatement, extractedMap));

        }
    }

    private void writeNamedControl(String controlMethodName,
                                   String controlMethodArgumentPtrParams,
                                   String controlMethodBody,
                                   String controlMethodCopyInArgInit,
                                   String controlMethodCopyOutArgInit,
                                   String controlStaticVar,
                                   List<ITable> controlTableObjects,
                                   Map<String, String> actionToParamsMap,
                                   Map<String, String> actionToRedirectActionBody,
                                   Map<String, String> actionToActionBody,
                                   List<String> extractedMethods) {

        STGroupFile stgGroupFile = getStgGroupFile();
        ST top = stgGroupFile.getInstanceOf(TOP);

        //generic
        ST namedControl_include = stgGroupFile.getInstanceOf("namedControl_include");
        ST namedControl_staticVar = stgGroupFile.getInstanceOf("namedControl_staticVar");
        namedControl_staticVar.add("staticVar", controlStaticVar);

        ST namedControl_externs = stgGroupFile.getInstanceOf("namedControl_externs");

        //Actions related
        ST namedControl_actions_staticP4Actions = stgGroupFile.getInstanceOf("namedControl_actions_staticP4Actions");

        List<ST> namedControl_actions_actions = new ArrayList<>();
        for (String actionControlName : actionToParamsMap.keySet()) {
            ST actions_action = stgGroupFile.getInstanceOf("namedControl_actions_action");
            actions_action.add("actionControlName", actionControlName);
            actions_action.add("actionParams", actionToParamsMap.get(actionControlName));
            actions_action.add("actionBody", actionToActionBody.get(actionControlName));
            actions_action.add("redirectActionBody", actionToRedirectActionBody.get(actionControlName));
            namedControl_actions_actions.add(actions_action);
        }


        //Table method apply related
        ST namedControl_tables_lookup_methods = stgGroupFile.getInstanceOf("namedControl_tables_lookup_methods");
        namedControl_tables_lookup_methods.add("tableObjects", controlTableObjects);

        ST namedControl_tables_match_methods = stgGroupFile.getInstanceOf("namedControl_tables_match_methods");
        namedControl_tables_match_methods.add("tableObjects", controlTableObjects);

        ST namedControl_tables_apply_methods = stgGroupFile.getInstanceOf("namedControl_tables_apply_methods");
        namedControl_tables_apply_methods.add("tableObjects", controlTableObjects);


        //control method related


        ST namedControl_extractedMethods = stgGroupFile.getInstanceOf("namedControl_extractedMethods");
        namedControl_extractedMethods.add("extractedMethods", extractedMethods);

        ST namedControl_apply = stgGroupFile.getInstanceOf("namedControl_apply");
        namedControl_apply.add("controlMethodName", controlMethodName);
        namedControl_apply.add("controlMethodArguments", controlMethodArgumentPtrParams);
        namedControl_apply.add("controlMethodBody", controlMethodBody);
        namedControl_apply.add("controlMethodCopyInArgInit", controlMethodCopyInArgInit);
        namedControl_apply.add("controlMethodCopyOutArgInit", controlMethodCopyOutArgInit);

        List<ST> stmts = new ArrayList<>();
        top.add(STMTS, stmts);

        stmts.add(namedControl_include);
        stmts.add(namedControl_staticVar);
        stmts.add(namedControl_externs);

        stmts.add(namedControl_actions_staticP4Actions);

        stmts.add(namedControl_extractedMethods);
        stmts.addAll(namedControl_actions_actions);

        stmts.add(namedControl_tables_lookup_methods);
        stmts.add(namedControl_tables_match_methods);
        stmts.add(namedControl_tables_apply_methods);

        stmts.add(namedControl_apply);
        writeTop(getFileName(), top);

    }


    @Override
    public STGroupFile getStgGroupFile() {
        return namedControlStg;
    }
}
