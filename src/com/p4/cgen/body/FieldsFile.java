package com.p4.cgen.body;

import com.p4.cgen.header.BaseCFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.IActionDeclaration;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.ITable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;

public class FieldsFile extends BaseCFile {

    private static final String fields = "fields.c";
    private static STGroupFile fieldsStg = new STGroupFile("templates/p416/c/fields.stg");


    @Override
    public String getFileName() {
        return fields;
    }

    @Override
    public void generateFile(IP4Program p4Program) {
        List<IControl> controls = p4Program.getControls();
        SortedMap<IActionDeclaration, String> actionToControl = new TreeMap<>(Comparator.comparing(IActionDeclaration::getActionId));
        SortedMap<ITable, String> tableToControl = new TreeMap<>(Comparator.comparing(ITable::getTableId));

        for (IControl control : controls) {
            for (ITable table : control.getTableObjects()) {
                tableToControl.put(table, control.getNameString());
            }
            List<IActionDeclaration> actionDecList = control.getActionDeclarations();
            for (IActionDeclaration actionDeclaration : actionDecList) {
                actionToControl.put(actionDeclaration, control.getNameString());
            }
        }

        List<String> orderedTableNames = tableToControl.keySet()
                .stream()
                .map(ITable::getNameString)
                .collect(Collectors.toList());

        List<String> orderedControlNames = new ArrayList<>(tableToControl.values());


        List<String> orderedControlActionNames = actionToControl.keySet()
                .stream()
                .map(action -> actionToControl.get(action) + "_" + action.getNameString())
                .collect(Collectors.toList());
        writeFields(orderedTableNames, orderedControlNames, orderedControlActionNames);
    }

    private void writeFields(List<String> tableNames, List<String> controlNames, List<String> controlActionNames) {

        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST fields_include = stgGroup.getInstanceOf("fields_include");

        ST fields_table_names = stgGroup.getInstanceOf("fields_table_names");
        fields_table_names.add("tableNames", tableNames);

        ST fields_tableid_control_map = stgGroup.getInstanceOf("fields_tableid_control_map");
        fields_tableid_control_map.add("controlNames", controlNames);

        ST fields_controlActions = stgGroup.getInstanceOf("fields_controlActions");
        fields_controlActions.add("controlActionNames", controlActionNames);

        ST fields_helperMethods = stgGroup.getInstanceOf("fields_helperMethods");

        List<ST> stmts = new ArrayList<>();
        stmts.add(fields_include);
        stmts.add(fields_table_names);
        stmts.add(fields_tableid_control_map);
        stmts.add(fields_controlActions);
        stmts.add(fields_helperMethods);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);


    }

    @Override
    public STGroupFile getStgGroupFile() {
        return fieldsStg;
    }


}
