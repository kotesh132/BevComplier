package com.p4.cgen1.runner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.p4.p416.pp.Macro;
import com.p4.p416.pp.MacroKey;
import com.p4.packetgen.runner.CommandLineParser.FileNameConverter;
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
import java.util.Map.Entry;

@Data
public class CGen1CLIParser {

    private static final Logger L = LoggerFactory.getLogger(CGen1CLIParser.class);
    @Parameter()
    private List<String> parameters;

    @Parameter(names = {"-o","-output"}, description="Output file name", converter=FileNameConverter.class,required=true)
    File outputFile;

    @Parameter(names = {"-ll","-loglevel"}, description="Log level")
    String loglevel;

    @Parameter(names = {"-p", "-phvOffsets"}, description = "Packet vector offsets json file name", converter = FileNameConverter.class, required = true)
    File phvOffsetsFile;

    @Parameter(names = "-help",help=true,description="Produces this Output")
    private boolean help;

    @Getter
    public List<File> inputFilesInOrder = new ArrayList<File>();


    public File getOutputDir(){
        if(outputFile.isDirectory()){
            return outputFile;
        } else
        if(outputFile.getParentFile()==null)
            return new File(".");
        return outputFile;
    }

    public File getPacketVectorOffsetsFile(){
        if(phvOffsetsFile.isFile() && phvOffsetsFile.exists()){
            return phvOffsetsFile;
        }
        return null;
    }

    @Getter
    private Path sourceFile;
    private Path basePath;
    private static Map<String,Path> includedFiles = new HashMap<String,Path>();
    private static Map<String,String> macroMap = new HashMap<String,String>();

    private boolean CompletedprocessingAllFOptionFiles;

    public CGen1CLIParser(String path) throws InvalidOptionException {
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
        CGen1CLIParser clp = new CGen1CLIParser(new File("./").getAbsolutePath());
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


    public List<Path> getIncludedFiles() {
        ArrayList<Path> includedFilesToReturn = new ArrayList<Path>();
        for(Entry<String,Path> entry:includedFiles.entrySet())
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


    public void processArgs() throws FileNotFoundException {
        processMacroDefinitions();
        processIncludeFiles();
    }

}
