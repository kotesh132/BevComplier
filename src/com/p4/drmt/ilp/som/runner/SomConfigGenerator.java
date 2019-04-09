package com.p4.drmt.ilp.som.runner;


import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import com.p4.ds.ListMap;
import com.p4.drmt.ilp.som.ITablePlacementResult;
import com.p4.drmt.ilp.som.SomFullCrossbarTablePlacementSolver;
import com.p4.drmt.ilp.som.SomSpec;
import com.p4.drmt.ilp.som.SomSpec.ControllerType;
import com.p4.drmt.ilp.som.TableDetail;
import com.p4.drmt.ilp.som.TableDetail.TableSpec;
import com.p4.pktgen.SOMUtils;
import com.p4.pktgen.config.Config;
import com.p4.pktgen.config.som.SOMConfig;
import com.p4.pktgen.enums.KeyType;
import com.p4.pktgen.model.SOMModel;
import com.p4.utils.FileUtils;

import ilog.concert.IloException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SomConfigGenerator {

    private static final Logger L = LoggerFactory.getLogger(SomConfigGenerator.class);
    private static JCommander commander = null;
    private static SomConfigCLI commandLineParser = new SomConfigCLI(new File("./").getAbsolutePath());


    //    private static String somSpecsJson = "somSpecs.json";
//    private static String somSpecConstantsJson = "somSpecsConstants.json";
    private static String tableSpecsJson = "tableSpecs.json";
    private static String configJson = "somConfig.json";


    public static void main(String[] args) {
        L.info("Launching SOM Config generator");
        runGenerateSOMConfigs(args, true);
    }

    private static void printHelp(JCommander jac) {
        jac.usage();
    }

    private static void runGenerateSOMConfigs(String[] args, boolean systemExitOnException) {
        try {
            commander = new JCommander(commandLineParser);
            commander.setProgramName("SomConfigGenerator");
            commander.parse(args);
            commandLineParser.processArgs(configJson, tableSpecsJson);


            ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            String loglevel = commandLineParser.getLoglevel();
            root.setLevel(Level.toLevel(loglevel));

            if (commandLineParser.isHelp()) {
                printHelp(commander);
                System.exit(0);
            }

        } catch (Throwable t) {
            printHelp(commander);
            takeCareOfError("Error parsing Arguments", t, systemExitOnException);
        }
        try {

            SomConfigGenerator somConfigGenerator = new SomConfigGenerator();
            somConfigGenerator.generateConfigs();

            L.info("done");
        } catch (Throwable t) {
            t.printStackTrace();
            takeCareOfError("Error Running P4toC Session", t, systemExitOnException);
        }
    }

    private static void takeCareOfError(String externalErrorMsg, Throwable t, boolean systemExitOnException) {
        L.error(externalErrorMsg + ": " + t.getLocalizedMessage());
        takeCareOfError(new RuntimeException(externalErrorMsg, t), systemExitOnException);
    }

    private static void takeCareOfError(Throwable t, boolean systemExitOnException) {
        if (systemExitOnException)
            System.exit(1);
        else
            throw new RuntimeException(t);
    }

    private void generateConfigs() {

        //load somSpec
        Config config = loadConfigFromJson();
        List<SOMConfig> somConfigs = config.getSomConfig();
        SOMModel.createInstance(somConfigs,config.getCacheConfig());


        //load somSpec
        int index = 0;
        List<SomSpec> somSpecs = new ArrayList<>();
        for (SOMConfig somConfig : somConfigs) {
            SomSpec somSpec = new SomSpec();
            somSpec.setSomId(index++);
            somSpec.setNumKSeg(somConfig.getNumKseg());
            somSpec.setNumDSeg(somConfig.getNumDseg());
            somSpec.setNumTcamColumn(somConfig.getTcamConfig().getNumCol());
            somSpec.setNumTcamRow(somConfig.getTcamConfig().getNumRow());
            somSpec.setNumTcam(somConfig.getTcamConfig().getNumRow() * somConfig.getTcamConfig().getNumCol());

            if (somConfig.getNumCamControllers() > 0) {
                somSpec.addControllerTypeNum(ControllerType.NUM_CAM_CONTROLLERS, somConfig.getNumCamControllers());
            }

            if (somConfig.getNumHashControllers() > 0) {
                somSpec.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, somConfig.getNumHashControllers());
            }

            if (somConfig.getNumReadControllers() > 0) {
                somSpec.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, somConfig.getNumReadControllers());
            }

            if (somConfig.getNumWriteControllers() > 0) {
                somSpec.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, somConfig.getNumWriteControllers());
            }

            somSpec.setNumSram(somConfig.getSramConfig().getNumSram());

            somSpecs.add(somSpec);
        }


        //Load tablespecs
        TableSpec[] tableSpecs = loadTableSpecFromJson();

        if (tableSpecs == null || tableSpecs.length == 0) {
            throw new RuntimeException("TableSpecs could not be loaded");
        }
        ListMap<Integer, TableDetail> disJointTableDetails = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (TableSpec tableSpec : tableSpecs) {
            TableDetail tableDetail = new TableDetail();

            int numKSeg = (int) Math.ceil(1.0 * tableSpec.getKeyWidth() / somConfigs.get(0).getKsegWidth());
            int numDSeg = (int) Math.ceil(1.0 * tableSpec.getDataWidth() / somConfigs.get(0).getDsegWidth());
            // somConfigs.get(0).getSramConfig().getBits() = dsegWidth + pointer (10 bits) + valid bit
            int numSramsWordWise = (int) Math.ceil(1.0 * tableSpec.getNumEntries() / somConfigs.get(0).getSramConfig().getWords());
            int numSramsBitWise = (int) Math.ceil(1.0 * tableSpec.getDataWidth() / somConfigs.get(0).getDsegWidth());
            int depthInTcamUnits = (int) Math.ceil(1.0 * tableSpec.getNumEntries() / somConfigs.get(0).getTcamConfig().getWords());
            Map<String, Integer> controllerTypesMap = tableSpec.getControllerTypesMap();


            tableDetail.setTableId(tableSpec.getTableId());
            tableDetail.setNumKSeg(numKSeg);
            tableDetail.setNumDseg(numDSeg);
            tableDetail.setNumSrams(numSramsWordWise * numSramsBitWise);
            tableDetail.setDepthInTcamUnits(depthInTcamUnits);
            tableDetail.setKeyWidth(tableSpec.getKeyWidth());
            tableDetail.setDataWidth(tableSpec.getDataWidth());
            tableDetail.setKeytype(KeyType.valueOf(tableSpec.getKeyType()));
            tableDetail.setNumEntries(tableSpec.getNumEntries());
            for (String controllerType : controllerTypesMap.keySet()) {
                if (ControllerType.typeOf(controllerType) == null) {
                    throw new RuntimeException("Trying to add invalid controllerType " + controllerType);
                }
                tableDetail.addControllerTypeNum(ControllerType.typeOf(controllerType), controllerTypesMap.get(controllerType));
            }
            disJointTableDetails.add(tableSpec.getDisjointBucket(), tableDetail);

        }

        List<List<TableDetail>> disjointTableDetailsList = new ArrayList<>();
        for (Entry<Integer, List<TableDetail>> entry : disJointTableDetails.entrySet()) {
            disjointTableDetailsList.add(entry.getValue());
        }

        SomFullCrossbarTablePlacementSolver solver = new SomFullCrossbarTablePlacementSolver(disjointTableDetailsList, somSpecs);

        try {
            ITablePlacementResult solution = solver.solve();
            if (solution.containsSolution()) {
                solution.printSolution();
                SOMUtils.placeTablesInSOMs(disjointTableDetailsList);
                SOMUtils.populateTables(disjointTableDetailsList);
                SOMUtils.emitSOMConfigAndData(commandLineParser.getOutputDir());
            } else {
                L.error("Unable to generate SOM configuration for give Schedule/Som Configs/Table Specs");
            }


        } catch (IloException e) {
            e.printStackTrace();
        }

    }

    private Config loadConfigFromJson() {

        Config config = null;
        try {
            InputStream is = FileUtils.getInputStream(getFileFromInputDirectory(commandLineParser.inputFolder, SomConfigGenerator.configJson));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            config = new Config(mapper.readValue(is, Config.UnNormalized.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    private TableSpec[] loadTableSpecFromJson() {
        ObjectMapper mapper = new ObjectMapper();

        File tableSpecsJsonFile = getFileFromInputDirectory(commandLineParser.inputFolder, SomConfigGenerator.tableSpecsJson);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(tableSpecsJsonFile, TableSpec[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    private File getFileFromInputDirectory(File inputFolder, String fileName) {

        File somSpecConstantsJsonFile = new File(inputFolder + File.separator + fileName);
        if (somSpecConstantsJsonFile.exists()) {
            return somSpecConstantsJsonFile;
        }
        return null;
    }

}
