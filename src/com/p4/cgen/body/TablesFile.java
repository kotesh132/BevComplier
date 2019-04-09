package com.p4.cgen.body;

import com.p4.cgen.header.TablesHFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.iface.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;

public class TablesFile extends TablesHFile {

    private static final String tables = "tables.c";
    private static STGroupFile tablesStg = new STGroupFile("templates/p416/c/tables.stg");


    @Override
    public String getFileName() {
        return tables;
    }

    @Override
    public void generateFile(IP4Program p4Program) {
        List<IControl> controls = p4Program.getControls();
        List<ITable> tables = new ArrayList<>();
        List<IActionDeclaration> actionDeclarations = new ArrayList<>();
        SortedMap<IActionDeclaration, String> actionToControl = new TreeMap<>(Comparator.comparing(IActionDeclaration::getActionId));
        Map<String, List<String>> controlToActionsMap = new LinkedHashMap<>();

        for (IControl control : controls) {
            tables.addAll(control.getTableObjects());
            List<String> actions = new ArrayList<>();
            List<IActionDeclaration> actionDecList = control.getActionDeclarations();
            for (IActionDeclaration actionDeclaration : actionDecList) {
                actions.add(actionDeclaration.getNameString());
                actionToControl.put(actionDeclaration, control.getNameString());
            }
            if (!actions.isEmpty()) {
                controlToActionsMap.put(control.getNameString(), actions);
            }
            actionDeclarations.addAll(actionDecList);
        }

        List<String> actionsOrderedById = actionToControl.keySet()
                .stream()
                .map(action -> actionToControl.get(action) + "_" + action.getNameString())
                .collect(Collectors.toList());


        writeTables(tables, controlToActionsMap, actionsOrderedById);
    }

    private void writeTables(List<ITable> tables, Map<String, List<String>> controlToActionsMap, List<String> actionsOrderedById) {

        STGroupFile stgGroupFile = getStgGroupFile();
        ST top = stgGroupFile.getInstanceOf(TOP);

        ST tables_include = stgGroupFile.getInstanceOf("tables_include");

        ST tables_actions_array = stgGroupFile.getInstanceOf("tables_actions_array");
        tables_actions_array.add("controlToActionsMap", controlToActionsMap);

        ST tables_all_actions = stgGroupFile.getInstanceOf("tables_all_actions");
        tables_all_actions.add("actionsOrderedById", actionsOrderedById);
        
        ST tables_lookup_methods = stgGroupFile.getInstanceOf("tables_lookup_methods");
        tables_lookup_methods.add("tableObjects", tables);

        ST tables_match_methods = stgGroupFile.getInstanceOf("tables_match_methods");
        tables_match_methods.add("tableObjects", tables);

        ST tables_apply_methods = stgGroupFile.getInstanceOf("tables_apply_methods");
        tables_apply_methods.add("tableObjects", tables);


        List<ST> stmts = new ArrayList<>();
        stmts.add(tables_include);
        stmts.add(tables_actions_array);
        stmts.add(tables_all_actions);
        stmts.add(tables_lookup_methods);
        stmts.add(tables_match_methods);
        stmts.add(tables_apply_methods);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);
    }

    @Override
    public STGroupFile getStgGroupFile() {
        return tablesStg;
    }
}
