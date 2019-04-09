package com.p4.cgen.header;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.IParameter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class ControlHFile extends BaseCFile {

    private static final String controlH = "control.h";
    private static STGroupFile controlHStg = new STGroupFile("templates/p416/c/headers/controlH.stg");

    @Override
    public String getFileName() {
        return controlH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        Map<String, String> controlMethodToArgumentsMap = getControlMethodToArgumentsMap(p4Program);

        writeControlH(controlMethodToArgumentsMap);
    }

    protected Map<String, String> getControlMethodToArgumentsMap(IP4Program p4Program) {
        List<IControl> controls = p4Program.getControls();

        Map<String, String> controlMethodToArgumentsMap = new LinkedHashMap<>();
        IControl deparser = p4Program.getDeparser().get(0);
        controls.remove(deparser);
        for (IControl control : controls) {
            List<IParameter> parameters = control.getParameters();

            if (parameters != null) {
                List<String> params = new ArrayList<>();
                for (IParameter parameter : parameters) {
                    params.add(parameter.getTypeRef().getPrefixedType() + "* " + parameter.getNameString());
                }
                controlMethodToArgumentsMap.put(control.getNameString(), CGenUtils.joinStrings(params, ", "));
            }
        }
        return controlMethodToArgumentsMap;
    }

    private void writeControlH(Map<String, String> controls) {

        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST controlH_ifNDef = stgGroup.getInstanceOf("controlH_ifndef");
        ST controlH_include = stgGroup.getInstanceOf("controlH_include");
        ST controlH_vars = stgGroup.getInstanceOf("controlH_vars");
        controlH_vars.add("controlMethodsApplyMap", controls);

        ST controlH_method_decl = stgGroup.getInstanceOf("controlH_method_decl");
        controlH_method_decl.add("controlMethodsApplyMap", controls);

        ST controlH_endIf = stgGroup.getInstanceOf("controlH_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(controlH_ifNDef);
        stmts.add(controlH_include);
        stmts.add(controlH_vars);
        stmts.add(controlH_method_decl);
        stmts.add(controlH_endIf);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return controlHStg;
    }
}
