package com.p4.cgen.header;

import com.p4.p416.iface.IInstantiation;
import com.p4.p416.iface.IP4Program;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

public class PHVParserHFile extends BaseCFile {
    private static final String phvparserH = "phvparser.h";
    private static STGroupFile phvparserHStg = new STGroupFile("templates/p416/c/headers/phvparserH.stg");


    @Override
    public String getFileName() {
        return phvparserH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<IInstantiation> instantiations = p4Program.getInstantiations();
        String params = getPHVParserMethodParams(instantiations);
        writePHVParserH(params);
    }

    protected String getPHVParserMethodParams(List<IInstantiation> instantiations) {
        StringBuilder sb = new StringBuilder();
        sb.append(", packet_in* packet, packet_in* packetBit");
        for (IInstantiation instantiation : instantiations) {
            sb.append(", ").append(instantiation.getTypeRef().getPrefixedType()).append("* ").append(instantiation.getNameString());
        }
        return sb.substring(", ".length());
    }

    private void writePHVParserH(String params) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST phvparserH_ifNDef = stgGroup.getInstanceOf("phvparserH_ifndef");
        ST phvparserH_include = stgGroup.getInstanceOf("phvparserH_include");

        ST phvparserH_method_decl = stgGroup.getInstanceOf("phvparserH_method_decl");
        phvparserH_method_decl.add("params", params);

        ST phvparserH_endIf = stgGroup.getInstanceOf("phvparserH_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(phvparserH_ifNDef);
        stmts.add(phvparserH_include);
        stmts.add(phvparserH_method_decl);
        stmts.add(phvparserH_endIf);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return phvparserHStg;
    }
}
