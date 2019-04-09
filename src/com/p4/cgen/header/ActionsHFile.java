package com.p4.cgen.header;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IParameter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class ActionsHFile extends BaseCFile {

    private static final String actionsH = "actions.h";
    private static STGroupFile actionsHStg = new STGroupFile("templates/p416/c/headers/actionsH.stg");


    @Override
    public String getFileName() {
        return actionsH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {
        Map<String, String> actionToParamsMap = new LinkedHashMap<>();
        Map<String, String> actionToControlMap = new LinkedHashMap<>();
        Map<String, String> actionToOrgActionMap = new LinkedHashMap<>();

        for (IControl control : p4Program.getControls()) {
            List<IActionDeclaration> actionDeclarations = control.getActionDeclarations();
            for (IActionDeclaration actionDeclaration : actionDeclarations) {
                List<String> params = new ArrayList<>();
                for (IParameter parameter : actionDeclaration.getParameters()) {
                    String sizeString = parameter.getTypeRef().getSize();
                    if (sizeString != null) {
                        int size = Integer.parseInt(sizeString);
                        params.add(getCType(size) + " " + parameter.getNameString() + getCVarArraySize(size));
                    } else {
                        String typeString = parameter.getTypeRef().getPrefixedType();
                        params.add(typeString + " " + parameter.getNameString());
                    }
                }

                String actionControlName = control.getNameString() + "_" + actionDeclaration.getNameString();
                actionToParamsMap.put(actionControlName, CGenUtils.joinStrings(params, ", "));
                actionToControlMap.put(actionControlName, control.getNameString());
                actionToOrgActionMap.put(actionControlName, actionDeclaration.getNameString());
            }
        }
        writeActionsH(actionToParamsMap, actionToControlMap, actionToOrgActionMap);
    }

    private void writeActionsH(Map<String, String> actionToParamsMap, Map<String, String> actionToControlMap, Map<String, String> actionToOrgActionMap) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST actions_ifndef = stgGroup.getInstanceOf("actions_ifndef");
        ST actions_include = stgGroup.getInstanceOf("actions_include");
        ST actions_typedefs = stgGroup.getInstanceOf("actions_typedefs");
        Set<String> controls = new HashSet<>(actionToControlMap.values());
        actions_typedefs.add("controls", controls);

        ST actions_actions = stgGroup.getInstanceOf("actions_actions");
        actions_actions.add("actionToParamsMap", actionToParamsMap);
        actions_actions.add("actionToControlMap", actionToControlMap);

        ST actions_endif = stgGroup.getInstanceOf("actions_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(actions_ifndef);
        stmts.add(actions_include);
        stmts.add(actions_typedefs);
        stmts.add(actions_actions);
        stmts.add(actions_endif);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return actionsHStg;
    }
}
