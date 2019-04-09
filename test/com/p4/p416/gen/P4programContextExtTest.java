package com.p4.p416.gen;

import com.p4.cgen.utils.CGenUtils;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IHeader;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class P4programContextExtTest {

    private static P4programContextExt p4Program = null;

    @BeforeClass
    public static void setUp() {
        String file = P4programContextExt.class.getClassLoader().getResource("tdata/sample.p4").getFile();

        File fileName = new File(file);
        String text = FileUtils.readFileIntoString(fileName, "\n");

        P416ParserUtil parserUtil = new P416ParserUtil();
        P4programContext ctx = parserUtil.getP416Context(text, fileName.getName());

        p4Program = (P4programContextExt) P4programContextExt.getExtendedContext(ctx);
    }

    @Test
    public void getHeaders() throws Exception {


        if (p4Program == null) {
            Assert.fail("P4 Program is not loaded");
        }

        List<IHeader> headers = p4Program.getHeaders();

        List<String> headerNames = headers.stream()
                .map(IHeader::getNameString)
                .collect(Collectors.toList());

        String actual = CGenUtils.joinStrings(headerNames, ",");
        String expected = "ethernet_t,ipv4_t,ipv6_t,tcp_t,udp_t";

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void getControls() throws Exception {


        if (p4Program == null) {
            Assert.fail("P4 Program is not loaded");
        }

        List<IControl> controls = p4Program.getControls();

        List<String> controlNames = controls.stream()
                .map(IControl::getNameString)
                .collect(Collectors.toList());

        String actual = CGenUtils.joinStrings(controlNames, ",");
        String expected = "egress,ingress,DeparserImpl,verifyChecksum,computeChecksum";

        Assert.assertEquals(expected, actual);

    }



    @AfterClass
    public static void tearDown() {
        //any thing to free?
        p4Program = null;
    }

}