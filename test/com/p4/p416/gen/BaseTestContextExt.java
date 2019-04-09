package com.p4.p416.gen;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

abstract class BaseTestContextExt {

    ExtendedContextGetVisitor extendedContextGetVisitor;

    P416Parser getParser(String str) {
        P416Lexer lexer = new P416Lexer(new ANTLRInputStream(str));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new P416Parser(tokens);
    }

}
