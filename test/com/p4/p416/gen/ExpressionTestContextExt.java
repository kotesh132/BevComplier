package com.p4.p416.gen;

import com.p4.cppgen.utils.CppGenUtils;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpressionTestContextExt extends BaseTestContextExt {

    @Before
    public void setUp() {
        extendedContextGetVisitor = new ExtendedContextGetVisitor();
    }

    @Test
    public void testNumberTransformation() {
        List<String> inputExps = Arrays.asList("x > 5",
                "x > 0x5",
                "x[1] > 5",
                "x[1] > (bit<32>)5",
                "(hdr.ethernet.srcAddr == 123456 && hdr.ethernet.dstAddr > (bit<32>) 2)",
                "hdr.ethernet.dstAddr[22:0] <= hdr.ipv4.dstAddr[22:0]");
        List<String> expecteExps = Arrays.asList("x > \"5\"",
                "x > \"0x5\"",
                "x[1] > \"5\"",
                "x[1] > (bit<32>)\"5\"",
                "(hdr.ethernet.srcAddr == \"123456\" && hdr.ethernet.dstAddr > (bit<32>) \"2\")",
                "hdr.ethernet.dstAddr[22:0] <= hdr.ipv4.dstAddr[22:0]");

        for (int i = 0; i < inputExps.size(); i++) {
            String inputExp = inputExps.get(i);
            String expectedExp = expecteExps.get(i);

            P416Parser parser = getParser(inputExp);
            ExpressionContext expressionContext = (ExpressionContext) new PopulateExtendedContextVisitor().visit(parser.expression());

            CppGenUtils.transformIntegersToString(extendedContextGetVisitor.visit(expressionContext), new LinkedHashMap<>());

            AbstractBaseExt expressionContextExt = extendedContextGetVisitor.visit(expressionContext);
            String actualOutput = expressionContextExt.getFormattedText();
            Assert.assertEquals(expectedExp, actualOutput);

        }

    }

    @After
    public void tearDown() {
        extendedContextGetVisitor = null;
    }

}