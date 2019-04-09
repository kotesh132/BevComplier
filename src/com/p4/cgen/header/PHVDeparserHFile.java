package com.p4.cgen.header;

import com.p4.p416.iface.IInstantiation;
import com.p4.p416.iface.IP4Program;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

public class PHVDeparserHFile extends BaseCFile {

    private static final String phvdeparserH = "phvdeparser.h";
    private static STGroupFile phvdeparserHStg = new STGroupFile("templates/p416/c/headers/phvdeparserH.stg");

    @Override
    public String getFileName() {
        return phvdeparserH;
    }

    protected String getPHVDeparserMethodParams(List<IInstantiation> instantiations) {
        StringBuilder sb = new StringBuilder();
        sb.append(", packet_out* packet, packet_out* packetBit");
        for (IInstantiation instantiation : instantiations) {
            sb.append(", ").append(instantiation.getTypeRef().getPrefixedType()).append("* ").append(instantiation.getNameString());
        }
        return sb.substring(", ".length());
    }


    @Override
    public void generateFile(IP4Program p4Program) {
        List<IInstantiation> instantiations = p4Program.getInstantiations();
        String params = getPHVDeparserMethodParams(instantiations);
        writePHVDeparserH(params);
    }

    private void writePHVDeparserH(String params) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST phvdeparserH_ifNDef = stgGroup.getInstanceOf("phvdeparserH_ifndef");
        ST phvdeparserH_include = stgGroup.getInstanceOf("phvdeparserH_include");

        ST phvdeparserH_method_decl = stgGroup.getInstanceOf("phvdeparserH_method_decl");
        phvdeparserH_method_decl.add("params", params);

        ST phvdeparserH_endIf = stgGroup.getInstanceOf("phvdeparserH_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(phvdeparserH_ifNDef);
        stmts.add(phvdeparserH_include);
        stmts.add(phvdeparserH_method_decl);
        stmts.add(phvdeparserH_endIf);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return phvdeparserHStg;
    }
}
