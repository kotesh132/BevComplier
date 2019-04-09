package com.p4.p416.gen;

import com.p4.cppgen.utils.CppGenUtils;
import com.p4.p416.gen.P416Parser.StatementContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class StatementTestContextExt extends BaseTestContextExt {
    @Before
    public void setUp() {
        extendedContextGetVisitor = new ExtendedContextGetVisitor();
    }

    @Test
    public void testNumberTransformationInStatements() {
        List<String> inputExps = Arrays.asList("x = 5;",
                "x = 0x5;",
                "x[1] = \"5\";",
                "hdr.ethernet.dstAddr = hdr.ethernet.dstAddr | 48w0x1005e000000;",
                "hdr.ethernet.dstAddr[22:0] = hdr.ipv4.dstAddr[22:0];",
                "if (hdr.ethernet.srcAddr == 123456 && hdr.ethernet.dstAddr > (bit < 32 >) 2){\n" +
                        "            if (hdr.ethernet.srcAddr != hdr.ethernet.dstAddr)\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr;        //P1\n" +
                        "            else if ((hdr.ipv4.isValid()) && !(hdr.ipv6.isValid()))\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 2;        //P2\n" +
                        "            else\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 3;        //P3\n" +
                        "        }");
        List<String> expecteExps = Arrays.asList("x = \"5\";",
                "x = \"0x5\";",
                "x[1] = \"5\";",
                "hdr.ethernet.dstAddr = hdr.ethernet.dstAddr | \"48w0x1005e000000\";",
                "hdr.ethernet.dstAddr[22:0] = hdr.ipv4.dstAddr[22:0];",
                "if (hdr.ethernet.srcAddr == \"123456\" && hdr.ethernet.dstAddr > (bit < 32 >) \"2\"){\n" +
                        "            if (hdr.ethernet.srcAddr != hdr.ethernet.dstAddr)\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr;        //P1\n" +
                        "            else if ((hdr.ipv4.isValid()) && !(hdr.ipv6.isValid()))\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + \"2\";        //P2\n" +
                        "            else\n" +
                        "                hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + \"3\";        //P3\n" +
                        "        }");
        for (int i = 0; i < inputExps.size(); i++) {
            String inputExp = inputExps.get(i);
            String expectedExp = expecteExps.get(i);

            P416Parser parser = getParser(inputExp);
            StatementContext statementContext = (StatementContext) new PopulateExtendedContextVisitor().visit(parser.statement());

            CppGenUtils.transformIntegersToString(extendedContextGetVisitor.visit(statementContext), new LinkedHashMap<>());

            StatementContextExt statementContextExt = (StatementContextExt)extendedContextGetVisitor.visit(statementContext);
            String actualOutput = statementContextExt.getFormattedText();
            Assert.assertEquals(expectedExp, actualOutput);

        }

    }

    @After
    public void tearDown() {
        extendedContextGetVisitor = null;
    }
}