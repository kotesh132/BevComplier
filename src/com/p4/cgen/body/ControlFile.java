package com.p4.cgen.body;

import com.p4.cgen.header.ControlHFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class ControlFile extends ControlHFile {

    private static final String control = "control.c";
    private static STGroupFile controlStg = new STGroupFile("templates/p416/c/control.stg");


    @Override
    public String getFileName() {
        return control;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        Map<String, String> controlMethodToBodyMap = new LinkedHashMap<>();


        List<IControl> controls = p4Program.getControls();
        for (IControl control : controls) {
            IControlBody controlBody = control.getControlBody();

            IBlockStatement blockStatement = controlBody.getBlockStatement();

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            transformToCCodeDriver(blockStatement, map);

            String body = blockStatement.getFormattedText();
            body = body.replaceFirst("\\{", "");

            int ind = body.lastIndexOf("}");
            if (ind >= 0)
                body = new StringBuilder(body).replace(ind, ind + 1, "").toString();

            controlMethodToBodyMap.put(control.getNameString(), body);
        }

        writeControl(controlMethodToArgumentPtrParamsMap, controlMethodToBodyMap, controlMethodToCopyInArgInitMap, controlMethodToCopyOutArgInitMap);
    }

    private void writeControl(Map<String, String> controlMethodToArgumentsMap, Map<String, String> controlMethodToBodyMap, Map<String, String> controlMethodToCopyInArgInitMap, Map<String, String> controlMethodToCopyOutArgInitMap) {


        STGroupFile stgGroupFile = getStgGroupFile();
        ST top = stgGroupFile.getInstanceOf(TOP);

        ST control_include = stgGroupFile.getInstanceOf("control_include");
        ST control_apply = stgGroupFile.getInstanceOf("control_apply");
        control_apply.add("controlMethodToArgumentsMap", controlMethodToArgumentsMap);
        control_apply.add("controlMethodToBodyMap", controlMethodToBodyMap);
        control_apply.add("controlMethodToCopyInArgInitMap", controlMethodToCopyInArgInitMap);
        control_apply.add("controlMethodToCopyOutArgInitMap", controlMethodToCopyOutArgInitMap);

        List<ST> stmts = new ArrayList<>();

        top.add(STMTS, stmts);
        stmts.add(control_include);
        stmts.add(control_apply);
        writeTop(getFileName(), top);
    }

    @Override
    public STGroupFile getStgGroupFile() {
        return controlStg;
    }
}
