package com.p4.quadpeaks;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.p4.p416.pp.Macro;
import com.p4.p416.pp.MacroKey;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.packetgen.runner.ICommandLineParser;
import com.p4.packetgen.runner.InvalidOptionException;
import com.p4.utils.FileUtils;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class QPCommandLineParser implements ICommandLineParser {
    private static final Logger L = LoggerFactory.getLogger(QPCommandLineParser.class);
    @Parameter()
    private List<String> parameters;

    @Parameter(names = {"-o","-output"}, description="Output Directory name", converter=QPCommandLineParser.FileNameConverter.class,required=true)
    File outputFile;

    @Parameter(names = {"-ll","-loglevel"}, description="Log level")
    String loglevel;

    @Parameter(names = "-chnts", description="compiler hints json file", converter=QPCommandLineParser.FileNameConverter.class,required=false)
    File compilerHints;

    @Parameter(names = "-pcfg", description="Packet generation config file", converter=QPCommandLineParser.FileNameConverter.class,required=false)
    File pcfgFile;

    @Parameter(names = "-dumpstats",help=true,description="Dump Parser Stats")
    private boolean dump;

    @Parameter(names = "-pgen",help=true,description="generate packets for testing")
    private boolean pgen;

    @Parameter(names = "-ex", description="Extractor JSON File path", converter=QPCommandLineParser.FileNameConverter.class,required=false)
    File extractorFile;


    @Parameter(names = "-runsemanticchecks", help = true, description = "Run Semantic Checks on the input p4 file")
    private boolean runSemanticChecks = false;

    public File quadupalizedFile(File input){
        return new File(getOutputFile().getAbsoluteFile().getAbsolutePath()+File.separator+input.getName()+".quadrupalized");
    }

    public File transformedFile(File input){
        return new File(getOutputFile().getAbsoluteFile().getAbsolutePath()+File.separator+input.getName()+".transformed");
    }


    @Override
    public File getOutputDir(){
        if(outputFile.isDirectory()){
            return outputFile;
        } else
        if(outputFile.getParentFile()==null)
            return new File(".");
        return outputFile.getParentFile();
    }

    @Getter
    public static List<File> inputFilesInOrder = new ArrayList<File>();

    @Parameter(names = "-help",help=true,description="Produces this Output")
    private boolean help;

    public static class FileNameConverter implements IStringConverter<File> {
        @Override
        public File convert(String value) {
            return new File(value);
        }

    }

    @Getter
    private Path sourceFile;
    private Path basePath;
    private static Map<String,Path> includedFiles = new HashMap<String,Path>();
    private static Map<String,String> macroMap = new HashMap<String,String>();

    private boolean CompletedprocessingAllFOptionFiles;

    public QPCommandLineParser(String path) throws InvalidOptionException {
        sourceFile = FileSystems.getDefault().getPath(path).normalize();
        if ( !sourceFile.toFile().exists()){
            throw new InvalidOptionException(basePath.toString());
        }
        if ( sourceFile.toFile().isDirectory() ){
            basePath = sourceFile;
            sourceFile = null;
        }
        else{
            basePath = sourceFile.getParent();
        }
        parameters = new ArrayList<String>();
    }


    private Path getNormalizedPath(String path){
        return basePath.resolve(path).normalize();
    }

    private boolean isP4File(Path normalizedPath){
        if (normalizedPath.toFile().exists() && FileUtils.getExtension(normalizedPath.toFile().toString()).equals("p4")){
            return normalizedPath.toFile().isFile();
        } else {
            throw new InvalidOptionException("File not found\t"+normalizedPath.toString());
        }
    }

    private void processIncludeFiles() throws FileNotFoundException
    {
        for(String param: parameters)
        {
            Path normalizedPath = getNormalizedPath(param);
            inputFilesInOrder.add(normalizedPath.toFile());
            if (isP4File(normalizedPath))
            {
                includedFiles.put(normalizedPath.toString(), normalizedPath);
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("parameters=\n");
        for(String param:parameters)
        {
            sb.append("\t"+param+"\n");
        }
        sb.append("libPathList=\n");
        return sb.toString();
    }


    public static void main(String[] args) {
        QPCommandLineParser clp = new QPCommandLineParser(new File("./").getAbsolutePath());
        try{
            L.info(System.setProperty(JCommander.DEBUG_PROPERTY,"debug"));
            JCommander jCommander = new JCommander(clp);
            jCommander.parse(args);
            L.info("Command="+jCommander.getParsedCommand());
        }
        catch(ParameterException ex)
        {
            L.error(ex.getMessage());
        }
        L.info(clp.toString());
        try {
            clp.processIncludeFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        L.info(clp.toString());
    }


    @Override
    public List<Path> getIncludedFiles() {
        ArrayList<Path> includedFilesToReturn = new ArrayList<Path>();
        for(Map.Entry<String,Path> entry:includedFiles.entrySet())
        {
            includedFilesToReturn.add(entry.getValue());
        }
        return includedFilesToReturn;
    }

    private void processMacroDefinitions(){
        List<String> params = new ArrayList<String>();
        for(String param : parameters)
        {
            if(param.startsWith("+"))
            {
                String[] keyValue = null;
                if ( param.startsWith("+define+")){
                    keyValue  = param.substring("+define+".length(), param.length()).split("=");
                }
                else
                {
                    keyValue  = param.substring(1, param.length()).split("=");
                }

                macroMap.put(keyValue[0],keyValue[1]);
                params.add(param);
            }
        }
        for( String param : params)
        {
            parameters.remove(param);
        }
    }

    public HashMap<MacroKey,Macro> getMacroDefinitions()
    {
        HashMap<MacroKey,Macro> tempMacroMap = new HashMap<MacroKey, Macro>();
        for(String macroid : macroMap.keySet()){
            Macro macro = new Macro();
            MacroKey macrokey = new MacroKey();
            macrokey.setMacroName(macroid);
            macrokey.setNoOfParameters(null);
            macro.setMacroName(macroid);
            macro.setListOfParameters(null);
            macro.setMacroKey(macrokey);
            macro.setMacroDef(macroMap.get(macroid));
            tempMacroMap.put(macrokey, macro);
        }
        return tempMacroMap;
    }


    @Override
    public void processArgs() throws FileNotFoundException {
        processMacroDefinitions();
        processIncludeFiles();
    }

}
