package com.p4.p416.ex;

import com.beust.jcommander.JCommander;
import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parsers;
import com.p4.p416.archModel.GraphBuilderVisitor;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.iface.IParserState;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.pp.runner.PreprocessRunnerSession;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class ParserExperiment {
    private static final Logger L = LoggerFactory.getLogger(ParserExperiment.class);
    private static JCommander jc = null;
    private static CommandLineParser cp = new CommandLineParser(new File("./").getAbsolutePath());

    public static void main(String[] args){
        try{
            jc = new JCommander(cp);
            jc.setProgramName("p4parserexp");
            jc.parse(args);
            cp.processArgs();
            cp.getLoglevel();
            if(cp.isHelp()){
                jc.usage();
                System.exit(0);
            }
        }catch(Exception e){
            jc.usage();
            System.exit(1);
        }


        HashMap<File,Boolean> hashmap = new HashMap<>();
        PreprocessRunnerSession session = new PreprocessRunnerSession(cp.getOutputFile(), CommandLineParser.getInputFilesInOrder() , cp.getMacroDefinitions(), null, hashmap);
        session.preprocess();

        P416ParserUtil mp = new P416ParserUtil();
        File dir = cp.getOutputDir();
        List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
        for(File f :allFiles) {
            String text = FileUtils.readFileIntoString(f, "\n");
            P416Parser.P4programContext ctx = mp.getP416Context(text, f.getName());
            /*ctx.extendedContext.defineSymbol(null);
            GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
            graphBuilderVisitor.visit(ctx);
            List<List<IParserState>> loopPaths = new ArrayList<>();
            loopPaths = graphBuilderVisitor.getLoopPaths();

            List<IParserState> parseStates = new ArrayList<IParserState>();
            ctx.extendedContext.inlineParserStates(parseStates,loopPaths, new ArrayList<>());
            System.out.println(ctx.extendedContext.getFormattedText());*/
            AbstractBaseExt.getExtendedContext(ctx).defineSymbol(null);

            P4Headers hdrs = new P4Headers();
            AbstractBaseExt.getExtendedContext(ctx).buildTypes(hdrs);



            LinkedHashSet<Edge<String>> edges = new LinkedHashSet<>();

            Map<String, Integer> stateKeySizes = new HashMap<>();
            AbstractBaseExt.getExtendedContext(ctx).parserStats("", edges, hdrs, stateKeySizes);
            System.out.println(Utils.summary(stateKeySizes));
            Graph<String> graph = new Graph<>(edges);
            if(graph.containsSelfLoops()){
                L.info("Removing self loops");
                graph.removeSelfLoops();
            }

            List<List<String>> paths =  graph.allpaths("start", "accept");
            HashMap<Integer,Integer> stats = new HashMap<>();
            HashMap<Integer,Integer> keyStats = new HashMap<>();
            for(List<String> path:paths){
                //System.out.println(Utils.summary(path));
                int size = path.size();
                if(!stats.containsKey(size)){
                    stats.put(size, 0);
                }
                stats.put(size, stats.get(size)+1);


            }
            System.out.println("Number of nodes = "+ graph.getNodes().size());
            System.out.println("Number of edges = "+graph.getEdges().size());
            System.out.println("Number of distinct paths = "+paths.size()+"");
            for(Map.Entry<Integer, Integer> entry:stats.entrySet()){
                System.out.println(entry.getKey()+", "+entry.getValue());
            }
            System.out.println("++++++++++++++");
            for(int j=4; j<=8; j++){

                HashMap<Integer,Integer> compressedKeyStats = new HashMap<>();
                Set<String> compressedNodes = new HashSet<>();
                Set<Utils.Pair<String, String>> edg = new HashSet<>();
                int edgecount = 0;

                for(List<String>path: paths){
                    int len = 0;
                    for(String node: path){
                        if(stateKeySizes.containsKey(node)){
                            int x = Utils.ceil(stateKeySizes.get(node), 8);
                            len+=x;
                        }
                    }
                    if(!keyStats.containsKey(len)){
                        keyStats.put(len, 0);
                    }
                    keyStats.put(len, keyStats.get(len)+1);
                    PathHops p = new PathHops(path, stateKeySizes);
                    List<String> r = p.compressPath(j);
                    String lastEdge = r.get(0);
                    for(int i=1; i< r.size();i++){
                        edg.add(Utils.Pair.of(lastEdge, r.get(i)));
                        lastEdge = r.get(i);
                    }
                    compressedNodes.addAll(r);
                    PathHops.addToMap(compressedKeyStats,r.size());
                    edgecount+=r.size();
                }

                //System.out.println(edgecount);

                //System.out.println(Utils.summary("\n", compressedNodes));
                System.out.println("For max key bytes,"+j);
                System.out.println("Node count,"+compressedNodes.size());
                System.out.println("Edge Count,"+edg.size());
                System.out.println("Critical Path,"+ compressedKeyStats.keySet().stream().max(Comparator.comparing(Integer::valueOf)).get());
                for(Map.Entry<Integer, Integer> entry:compressedKeyStats.entrySet()){
                    System.out.println(entry.getKey()+", "+entry.getValue());
                }
                System.out.println("************");
            }



            for(Map.Entry<Integer, Integer> entry:keyStats.entrySet()){
                System.out.println(entry.getKey()+", "+entry.getValue());
            }

            System.out.println("++++++++++++++");

        }

    }
}
