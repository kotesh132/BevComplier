package com.p4.quadpeaks;

import com.p4.drmt.DRMTRunnerSession;
import com.p4.drmt.hints.Hints;
import com.p4.drmt.memoryManager.IMemoryInstance;
import com.p4.drmt.memoryManager.MemoryInstance;
import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.parser.*;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.pktgen.P4Blocks;
import com.p4.pktgen.PacketGenerator;
import com.p4.pktgen.config.Config;
import com.p4.pktgen.model.SOMModel;
import com.p4.quadpeaks.extractor.JSONDesez;
import com.p4.quadpeaks.extractor.MemoryMap;
import com.p4.quadpeaks.parser.ParserConfigUtil;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import lombok.Data;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Data
public class QPRunnerSession {
    private static final Logger L = LoggerFactory.getLogger(DRMTRunnerSession.class);

    public final QPCommandLineParser qpcp;

    public void run(){
        Hints hints = processHints();
        P416ParserUtil mp = new P416ParserUtil();
        File dir = qpcp.getOutputDir();
        List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
        MemoryManager.outputFile = new File(qpcp.getOutputFile()+File.separator+"memory.txt");

        for(File f :allFiles) {
            if (qpcp.isRunSemanticChecks()) {
                this.runSemanticChecks(f);
            }

            String text = FileUtils.readFileIntoString(f,"\n");
            P416Parser.P4programContext ctx = mp.getP416Context(text, f.getName());
            QPTransforms.runStandard(ctx,qpcp, f);
            Set<IMemoryInstance> extraInstances = new HashSet<>();
            for(String extraField :hints.getCompilerHints().getParser_extra_fields()){
                Symbol instance = AbstractBaseExt.getExtendedContext(ctx).resolve(extraField);
                extraInstances.add(new MemoryInstance(extraField, (Type)instance));
            }
            MemoryManager.allocateMemory(P4programContextExt.getExtendedContext(ctx), null, extraInstances);
            MemoryManager.printMemoryAllocated();

            L.info("Building Headers for Parser Config");
            P4Headers hdrs = new P4Headers();
            P4programContextExt.getExtendedContext(ctx).buildTypes(hdrs);
            P4Parsers parsers = new P4Parsers();
            P4programContextExt.getExtendedContext(ctx).buildParsers(hdrs, parsers);


            Config config = null;
            try {
                InputStream is = FileUtils.getInputStream(qpcp.getPcfgFile());
                if(is != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    config = new Config(mapper.readValue(is, Config.UnNormalized.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            P4Blocks.createInstance(new LinkedHashMap<>(), null);
            SOMModel.createInstance(config.getSomConfig(),config.getCacheConfig());

            MemoryMap memoryMap = null;
            if(qpcp.extractorFile!=null){
                memoryMap = JSONDesez.extractorMap(qpcp.extractorFile);
                L.info(memoryMap.summary());

            }
            Set<String> outer = new LinkedHashSet<>();
            outer.addAll(hints.getCompilerHints().getOuter_layer_states());
            for(P4Parser parser:parsers.getAllParsers()){
                L.debug("*************");
                L.debug(parser.getName());
                L.debug("*************");
                Graph<P4State> graph = parser.getParseGraph();

                ParserConfigUtil.generateParserRows(parser.getName(),hdrs,graph, qpcp.getOutputDir(), qpcp.isDump(), memoryMap, outer);
                L.info("Done generating parser rows");
                List<String> fields = hdrs.flattenAllTypes();
                Map<String, Integer> fieldSizes = new HashMap<String, Integer>();

                if(qpcp.isPgen()) {
                    L.info("Running Packet generator");
                    for (String field : fields) {
                        MemoryInstance mi = new MemoryInstance(field, (Type) P4programContextExt.getExtendedContext(ctx).resolve(field));
                        fieldSizes.put(field, mi.getType().getSizeInBits());
                    }
                    PacketGenerator pgen = new PacketGenerator();
                    pgen.setConfigFile(qpcp.getPcfgFile());
                    pgen.setParseGraph(graph);
                    pgen.setOutputDir(qpcp.getOutputDir());
                    pgen.setHeaders(hdrs);
                    pgen.setPktFieldSizes(fieldSizes);
                    //pgen.setAllHeaderValidFields(validBitOffsets);
                    pgen.run();
                }

                //PacketUtils.generatePktNTblCfg(ctx.extendedContext, cp.getOutputDir(), cp.isUsebmv(), graph, g.allpaths(start, end), controlBlocks, hdrs, null, km, ScheduleRunner.getTableToActionInstPtrMap(), null,ScheduleRunner.getTableToCycleMap());
            }

        }
    }

    private Hints processHints(){
        Hints hints = null;
        try {
            if(qpcp.getCompilerHints() !=null){
                InputStream is = FileUtils.getInputStream(qpcp.getCompilerHints());
                if(is != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    hints = new Hints(mapper.readValue(is, Hints.UnNormalized.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hints;
    }

    public void runSemanticChecks(File f){
        P416ParserUtil mp = new P416ParserUtil();
        String text = FileUtils.readFileIntoString(f,"\n");
        P416Parser.P4programContext ctx = mp.getP416Context(text, f.getName());
        L.info("Running semantic checks on "+f.getName());
        P4programContextExt.getExtendedContext(ctx).runSemanticChecks();
        L.info("Completed semantic checks on "+f.getName());
    }

}
