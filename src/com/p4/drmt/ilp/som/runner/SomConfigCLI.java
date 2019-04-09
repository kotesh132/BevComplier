package com.p4.drmt.ilp.som.runner;


import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.p4.packetgen.runner.InvalidOptionException;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Data
public class SomConfigCLI {

    private static final Logger L = LoggerFactory.getLogger(SomConfigCLI.class);
    @Parameter(names = {"-i", "-input"}, description = "Input folder name", converter = FileNameConverter.class, required = true)
    File inputFolder;

    @Parameter(names = {"-o", "-output"}, description = "Output file name", converter = FileNameConverter.class, required = true)
    File outputFile;

    @Parameter(names = {"-ll", "-loglevel"}, description = "Log level")
    String loglevel;

    @Parameter(names = "-help", help = true, description = "Produces this Output")
    private boolean help;

    @Getter
    private Path sourceFile;
    private Path basePath;

    public SomConfigCLI(String path) throws InvalidOptionException {
        sourceFile = FileSystems.getDefault().getPath(path).normalize();
        if (!sourceFile.toFile().exists()) {
            throw new InvalidOptionException(basePath.toString());
        }
        if (sourceFile.toFile().isDirectory()) {
            basePath = sourceFile;
            sourceFile = null;
        } else {
            basePath = sourceFile.getParent();
        }
    }

    public void processArgs(String... filenames) throws FileNotFoundException {
        if (!inputFolder.isDirectory()) {
            throw new InvalidOptionException("input file not found or not a directory");
        } else {
            String[] list = inputFolder.list();
            list = list == null ? new String[0] : list;
            List<String> childFiles = Arrays.asList(list);
            for (String fileName : filenames) {
                if (!childFiles.contains(fileName)) {
                    throw new RuntimeException("File " + fileName + " not found in the directory +" + inputFolder.getPath());
                }

            }
        }
    }

    public static class FileNameConverter implements IStringConverter<File> {
        @Override
        public File convert(String value) {
            return new File(value);
        }
    }

    public File getOutputDir() {
        if (outputFile.isDirectory()) {
            return outputFile;
        } else if (outputFile.getParentFile() == null)
            return new File(".");
        return outputFile.getParentFile();
    }

}
