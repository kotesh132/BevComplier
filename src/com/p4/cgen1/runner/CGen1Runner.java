package com.p4.cgen1.runner;

import com.beust.jcommander.JCommander;
import com.p4.packetgen.runner.P416Runner;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class CGen1Runner {
    private static final Logger L = LoggerFactory.getLogger(P416Runner.class);
    private static JCommander commander = null;
    private static CGen1CLIParser commandLineParser = new CGen1CLIParser(new File("./").getAbsolutePath());

    public static void main(String[] args) {
        L.info("Launching C generator");
        runGenerateC(args, true);
    }

    private static void printHelp(JCommander jac) {
        jac.usage();
    }

    private static void runGenerateC(String[] args, boolean systemExitOnException) {
        try {
            commander = new JCommander(commandLineParser);
            commander.setProgramName("P4toC");
            commander.parse(args);
            commandLineParser.processArgs();
            if (commandLineParser.isHelp()) {
                printHelp(commander);
                System.exit(0);
            }
        } catch (Throwable t) {
            printHelp(commander);
            takeCareOfError("Error parsing Arguments", t, systemExitOnException);
        }
        try {
            File packetVectorOffsetsFile = commandLineParser.getPacketVectorOffsetsFile();
            if (packetVectorOffsetsFile == null) {
                throw new FileNotFoundException("packet vector json file is not found");
            }

            HashMap<File, Boolean> filePreprocessStatusMap = new HashMap<File, Boolean>();
            PreprocessRunnerSession session = new PreprocessRunnerSession(commandLineParser.getOutputFile(), commandLineParser.getInputFilesInOrder(),
                    commandLineParser.getMacroDefinitions(), null, filePreprocessStatusMap);
            session.preprocess();
            L.info("pre-processing done");
            CGen1RunnerSession cGenRunnerSession = new CGen1RunnerSession(commandLineParser);
            cGenRunnerSession.run();
            L.info("done");
        } catch (Throwable t) {
            t.printStackTrace();
            takeCareOfError("Error Running P4toC Session", t, systemExitOnException);
        }
    }

    private static void takeCareOfError(String externalErrorMsg, Throwable t, boolean systemExitOnException) {
        L.error(externalErrorMsg + ": " + Utils.getRootCauseUserDisplayString(t));
        takeCareOfError(new RuntimeException(externalErrorMsg, t), systemExitOnException);
    }

    private static void takeCareOfError(Throwable t, boolean systemExitOnException) {
        if (systemExitOnException)
            System.exit(1);
        else
            throw new RuntimeException(t);
    }
}
