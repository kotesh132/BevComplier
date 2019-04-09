package com.p4.cgen.body;

import com.p4.cgen.header.BaseCFile;
import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.applications.Symbol;
import com.p4.p416.iface.*;
import com.p4.utils.Utils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class MainFile extends BaseCFile {
    private static final String mainFile = "main.c";
    private static STGroupFile mainFileStg = new STGroupFile("templates/p416/c/main.stg");


    @Override
    public String getFileName() {
        return mainFile;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        IInstantiation mainInstantiation = p4Program.getMainInstantiation();
        List<IArgument> arguments = mainInstantiation.getArguments();

        List<String> functionCalls = new ArrayList<>();
        for (IArgument argument : arguments) {
            if (argument.getExpression().isFunctionCall()) {
                String functionCall = ((IFunctionCall) argument.getExpression()).getExpression().getFormattedText();
                functionCalls.add(functionCall);
            }
        }

        List<Symbol> symbols = new ArrayList<>();
        for (String functionCall : functionCalls) {
            try {
                Symbol symbol = p4Program.resolve(functionCall);
                if (symbol instanceof IParser || symbol instanceof IControl) {
                    symbols.add(symbol);
                }
            }catch(Exception ignore) {
                //ignore for now
            }
        }

        Map<String, String> varMap = new LinkedHashMap<>();
        Utils.Pair<String, String> parserMethodToArgumentsPair = null;
        Utils.Pair<String, String> deParserMethodToArgumentsPair = null;
        Map<String, String> controlMethodToArgumentsMap = new LinkedHashMap<>();

        IControl deparser = p4Program.getDeparser().get(0);

        for (Symbol symbol : symbols) {
            List<IParameter> parameters = null;
            if (symbol instanceof IParser) {
                parameters = ((IParser) symbol).getParameters();
            }
            if (symbol instanceof IControl) {
                parameters = ((IControl) symbol).getParameters();
            }

            if (parameters != null) {
                List<String> params = new ArrayList<>();
                for (IParameter parameter : parameters) {
                    String prefixedType = parameter.getTypeRef().getPrefixedType();
                    varMap.put(parameter.getNameString(), prefixedType);
                    if (p4Program.resolve(prefixedType) instanceof IExternDeclaration) {
                        if ("packet_in".equals(prefixedType) || "packet_out".equals(prefixedType)) {
                            params.add("packet");
                            params.add("packetBit");
                        }else {
                            params.add(parameter.getNameString());
                        }
                    } else {
                        params.add("&" + parameter.getNameString());
                    }
                }

                if (symbol instanceof IControl) {
                    if (symbol == deparser) {
                        deParserMethodToArgumentsPair = new Utils.Pair<>(symbol.getSymbolName(), CGenUtils.joinStrings(params, ", "));
                    } else {
                        controlMethodToArgumentsMap.put(symbol.getSymbolName(), CGenUtils.joinStrings(params, ", "));
                    }
                }
                if (symbol instanceof IParser) {
                    parserMethodToArgumentsPair = new Utils.Pair<>(symbol.getSymbolName(), CGenUtils.joinStrings(params, ", "));
                }
            }

        }


        writeMain(controlMethodToArgumentsMap, parserMethodToArgumentsPair, deParserMethodToArgumentsPair, p4Program.getInstantiations());
    }

    private void writeMain(Map<String, String> controlMethodToArgumentsMap, Utils.Pair<String, String> parserMethodToArgumentsPair,
                           Utils.Pair<String, String> deParserMethodToArgumentsPair, List<IInstantiation> instantiations) {

        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST main_include = stgGroup.getInstanceOf("main_include");
//        ST main_global_var = stgGroup.getInstanceOf("main_global_var");
//        main_global_var.add("instantiations", instantiations);

        ST main_helper_methods = stgGroup.getInstanceOf("main_helper_methods");

        ST main_fromPhv = stgGroup.getInstanceOf("main_fromPhv");
        main_fromPhv.add("controlMethodsApplyMap", controlMethodToArgumentsMap);
        main_fromPhv.add("phvParserParams", parserMethodToArgumentsPair.second());
        main_fromPhv.add("phvDeparserParams", parserMethodToArgumentsPair.second());
        main_fromPhv.add("instantiations", instantiations);


        ST main_main = stgGroup.getInstanceOf("main_main");

        List<ST> stmts = new ArrayList<>();
        stmts.add(main_include);
//        stmts.add(main_global_var);

        stmts.add(main_helper_methods);
        stmts.add(main_fromPhv);
        stmts.add(main_main);

        top.add(STMTS, stmts);
        writeTop(getFileName(), top);


    }

    @Override
    public STGroupFile getStgGroupFile() {
        return mainFileStg;
    }
}
