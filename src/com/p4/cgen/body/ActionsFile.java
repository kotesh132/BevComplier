package com.p4.cgen.body;

import com.p4.cgen.header.ActionsHFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class ActionsFile extends ActionsHFile {

    private static final String actions = "actions.c";
    private static STGroupFile actionsStg = new STGroupFile("templates/p416/c/actions.stg");

    @Override
    public String getFileName() {
        return actions;
    }

    @Override
    public void generateFile(IP4Program p4Program) {
        Map<String, String> actionToParamsMap = new LinkedHashMap<>();
        Map<String, String> actionToRedirectActionBody = new LinkedHashMap<>();
        Map<String, String> actionToActionBody = new LinkedHashMap<>();
        Map<String, String> actionToControlName = new LinkedHashMap<>();
        Map<String, String> actionToOrgActionMap = new LinkedHashMap<>();

        for (IControl control : p4Program.getControls()) {
            List<IActionDeclaration> actionDeclarations = control.getActionDeclarations();
            for (IActionDeclaration actionDeclaration : actionDeclarations) {
                List<String> params = new ArrayList<>();
                List<String> callableParams = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                int bitOffset = 0;
                for (IParameter parameter : actionDeclaration.getParameters()) {
                    String sizeString = parameter.getTypeRef().getSize();
                    if (sizeString != null) {
                        int size = Integer.parseInt(sizeString);
                        params.add(getCType(size) + " " + parameter.getNameString() + getCVarArraySize(size));
                        sb.append(getRedirectActionParamConversionCnt(parameter.getNameString(), bitOffset, size));
                        bitOffset += size;
                    } else {
                        String typeString = parameter.getTypeRef().getPrefixedType();
                        params.add(typeString + " " + parameter.getNameString());
                    }
                    callableParams.add(parameter.getNameString());
                }

                String actionControlName = control.getNameString() + "_" + actionDeclaration.getNameString();

                actionToParamsMap.put(actionControlName, CGenUtils.joinStrings(params, ", "));

                sb.append("\t").append(actionControlName).append("(").append(CGenUtils.joinStrings(callableParams, ", ")).append(");");
                actionToRedirectActionBody.put(actionControlName, sb.toString());

                IBlockStatement blockStatement = actionDeclaration.getBlockStatement();
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                actionToActionBody.put(actionControlName, buildBlockStatementBody(blockStatement, map));

                actionToControlName.put(actionControlName, control.getNameString());
                actionToOrgActionMap.put(actionControlName, actionDeclaration.getNameString());

            }
        }

        writeActions(actionToParamsMap, actionToRedirectActionBody, actionToActionBody, actionToControlName);
    }

    private void writeActions(Map<String, String> actionToParamsMap, Map<String, String> actionToRedirectActionBody, Map<String, String> actionToActionBody, Map<String, String> actionToControlName) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST actions_include = stgGroup.getInstanceOf("actions_include");
        ST actions_typedef = stgGroup.getInstanceOf("actions_typedef");
        ST actions_staticP4Actions = stgGroup.getInstanceOf("actions_staticP4Actions");
        List<ST> actions_actions = new ArrayList<>();

        for (String actionControlName : actionToParamsMap.keySet()) {
            ST actions_action = stgGroup.getInstanceOf("actions_action");
            actions_action.add("actionControlName", actionControlName);
            actions_action.add("actionParams", actionToParamsMap.get(actionControlName));
            actions_action.add("actionBody", actionToActionBody.get(actionControlName));
            actions_action.add("redirectActionBody", actionToRedirectActionBody.get(actionControlName));
            actions_actions.add(actions_action);
        }

        List<ST> stmts = new ArrayList<>();
        stmts.add(actions_include);
        stmts.add(actions_typedef);
        stmts.add(actions_staticP4Actions);
        stmts.addAll(actions_actions);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return actionsStg;
    }
}
