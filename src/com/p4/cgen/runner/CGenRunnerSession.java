package com.p4.cgen.runner;

import com.p4.cgen.header.BaseCFile;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IP4Program;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
class CGenRunnerSession {
    private static final Logger L = LoggerFactory.getLogger(CGenRunnerSession.class);

    private final CGenCLIParser clp;
    private List<File> allFiles;

    final private static String STMTS = "stmts";
    final private static String TOP = "top";

    private void index() {
        File dir = clp.getOutputDir();
        allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
    }

    void run() {
        index();
        if ((clp.getOutputDir().exists() || clp.getOutputDir().mkdirs())) {
            for (File file : allFiles) {
                String text = FileUtils.readFileIntoString(file, "\n");

                //get P416 Context could have been static method
                P416ParserUtil parserUtil = new P416ParserUtil();
                P4programContext ctx = parserUtil.getP416Context(text, file.getName());
                AbstractBaseExt.uid = 0;

                P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
                P4programContextExt.getExtendedContext(ctx).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));

                preprrocessing(ctx);

                BaseCFile.setOutputDir(clp.getOutputDir());
                BaseCFile.setPhvOffsetsJsonFile(clp.getPacketVectorOffsetsFile());
                IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(ctx)).getP4Program();

                List<String> cnodes = Utils.newArrList();
                P4programContextExt.getExtendedContext(ctx).invokeProgramFlow(cnodes);

                BaseCFile.generateFiles(p4Program);

                BaseCFile.copyResourceFiles();

                AbstractBaseExt.uid = 0;

            }
        }
    }

    private void preprrocessing(P416Parser.P4programContext ctx) {
    	P4programContextExt.getExtendedContext(ctx).preProcess();
    }


}


