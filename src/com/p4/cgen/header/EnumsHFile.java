package com.p4.cgen.header;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.iface.IMatchKindDeclaration;
import com.p4.p416.iface.IP4Program;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;

public class EnumsHFile extends BaseCFile {

    private static final String enumsH = "enums.h";
    private static STGroupFile enumsHStg = new STGroupFile("templates/p416/c/headers/enumsH.stg");


    @Override
    public String getFileName() {
        return enumsH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<String> matchKinds = null;
        IMatchKindDeclaration matchKindDeclaration = p4Program.getMatchKindDeclaration();
        if (matchKindDeclaration != null) {
            matchKinds = matchKindDeclaration.getMatchKinds();
        }

        List<String> operators = new ArrayList<>();
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.EQ)+"="+P416Parser.EQ);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.NE)+"="+P416Parser.NE);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.GT)+"="+P416Parser.GT);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.LT)+"="+P416Parser.LT);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.PLUS)+"="+P416Parser.PLUS);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.MINUS)+"="+P416Parser.MINUS);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.XOR)+"="+P416Parser.XOR);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.OR)+"="+P416Parser.OR);
        operators.add(P416Parser.VOCABULARY.getSymbolicName(P416Parser.AND)+"="+P416Parser.AND);
        String operatorsEnum = CGenUtils.joinStrings(operators, ",\n");

        List<String> orderedControlActionNames = getOrderedControlActionNames(p4Program);
        LinkedHashSet<String> orderedControlActionEnums = orderedControlActionNames.stream()
                .map(controlActionName -> controlActionName + "_enum")
                .collect(Collectors.toCollection(LinkedHashSet::new));
        String actionsEnum = orderedControlActionEnums.stream().collect(Collectors.joining(",\n"));
        writeEnumsH(matchKinds, operatorsEnum, actionsEnum);

    }

    @Override
    public STGroupFile getStgGroupFile() {
        return enumsHStg;
    }

    private void writeEnumsH(List<String> matchKinds, String operatorsEnum, String actionsEnum) {

        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST enumsH_ifndef = stgGroup.getInstanceOf("enumsH_ifndef");

        ST enumsH_matchKinds = stgGroup.getInstanceOf("enumsH_matchKinds");
        if (!matchKinds.isEmpty()) {
            enumsH_matchKinds.add("matchKinds", matchKinds);
        }

        ST enumsH_operators = stgGroup.getInstanceOf("enumsH_operators");
        enumsH_operators.add("operatorsEnum", operatorsEnum);

        ST enumsH_actions = stgGroup.getInstanceOf("enumsH_actions");
        enumsH_actions.add("actionsEnum", actionsEnum);

        ST enumsH_hashAlgorithms = stgGroup.getInstanceOf("enumsH_hashAlgorithms");

        ST enumsH_endif = stgGroup.getInstanceOf("enumsH_endif");

        List<ST> stmts = new ArrayList<>();

        top.add(STMTS, stmts);
        stmts.add(enumsH_ifndef);
        stmts.add(enumsH_matchKinds);
        stmts.add(enumsH_operators);
        stmts.add(enumsH_actions);
        stmts.add(enumsH_hashAlgorithms);
        stmts.add(enumsH_endif);

        writeTop(getFileName(), top);
    }
}
