package com.p4.cgen.header;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IP4Program;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

public class FieldsHFile extends BaseCFile {

    private static final String fieldsH = "fields.h";
    private static STGroupFile fieldsHStg = new STGroupFile("templates/p416/c/headers/fieldsH.stg");


    @Override
    public String getFileName() {
        return fieldsH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {
        writeFieldsH();

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return fieldsHStg;
    }

    private void writeFieldsH() {

        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST fields_ifndef = stgGroup.getInstanceOf("fields_ifndef");
        ST fields_include = stgGroup.getInstanceOf("fields_include");
        ST fields_endif = stgGroup.getInstanceOf("fields_endif");
        ST fields_helperMethodsDecl = stgGroup.getInstanceOf("fields_helperMethodsDecl");


        List<ST> stmts = new ArrayList<>();
        stmts.add(fields_ifndef);
        stmts.add(fields_include);
        stmts.add(fields_helperMethodsDecl);
        stmts.add(fields_endif);

        top.add(STMTS, stmts);
        writeTop("fields.h", top);


    }

}
