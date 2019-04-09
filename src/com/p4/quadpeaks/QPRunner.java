package com.p4.quadpeaks;

import com.beust.jcommander.JCommander;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

public class QPRunner {
    private static final Logger L = LoggerFactory.getLogger(QPRunner.class);
    private static JCommander jc = null;
    private static QPCommandLineParser qpcp = new QPCommandLineParser(new File("./").getAbsolutePath());

    public static void main(String[] args) {
        try{
            jc = new JCommander(qpcp);
            jc.setProgramName("p4qp");
            jc.parse(args);
            qpcp.processArgs();
            qpcp.getLoglevel();
            if(qpcp.isHelp()){
                jc.usage();
                System.exit(0);
            }
        }catch(Exception e){
            jc.usage();
            System.exit(1);
        }
        String ll = qpcp.getLoglevel() == null || qpcp.getLoglevel().isEmpty() ? "info" : qpcp.getLoglevel();
        Utils.setRootLogLevel(ll);
        HashMap<File,Boolean> hashmap = new HashMap<>();
        PreprocessRunnerSession session = new PreprocessRunnerSession(qpcp.getOutputFile(), QPCommandLineParser.getInputFilesInOrder() , qpcp.getMacroDefinitions(), null, hashmap);
        session.preprocess();

        //System.exit(0);
        QPRunnerSession qpRunnerSession = new QPRunnerSession(qpcp);
        qpRunnerSession.run();

    }

    public static void takeCareOfError(String externalErrorMsg, Throwable t, boolean systemExitOnException) {
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
