package com.p4.cgen.header;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.ITable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class TablesHFile extends BaseCFile {

    private static final String tablesH = "tables.h";
    private static STGroupFile tablesHStg = new STGroupFile("templates/p416/c/headers/tablesH.stg");


    @Override
    public String getFileName() {
        return tablesH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<IControl> controls = p4Program.getControls();
        List<ITable> tables = new ArrayList<>();
        for (IControl control : controls) {
            tables.addAll(control.getTableObjects());
        }

        writeTablesH(tables);
    }

    private void writeTablesH(List<ITable> tableObjects) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST tables_ifndef = stgGroup.getInstanceOf("tables_ifndef");
        ST tables_include = stgGroup.getInstanceOf("tables_include");
        ST tables_vars = stgGroup.getInstanceOf("tables_vars");
        ST tables_typedefs = stgGroup.getInstanceOf("tables_typedefs");
        tables_typedefs.add("tableObjects", tableObjects);

        ST tables_endif = stgGroup.getInstanceOf("tables_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(tables_ifndef);
        stmts.add(tables_include);
        stmts.add(tables_vars);
        stmts.add(tables_typedefs);
        stmts.add(tables_endif);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return tablesHStg;
    }
}
