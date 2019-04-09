package com.p4.packetgen.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public interface ICommandLineParser {

	List<Path> getIncludedFiles();
	
	public File getOutputDir();

	void processArgs() throws FileNotFoundException;

}
