package com.p4.stepper.runner;

import java.io.File;

import lombok.Data;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

@Data
public class CommandParser {
	@Parameter(names = {"-j","-json"}, description="Json file for stepper", converter=FileNameConverter.class,required=true)
	File json;
 
	@Parameter(names = {"-i","-ipkt"}, description="Input packet", converter=FileNameConverter.class,required=true)
	private File ipkt;
 
	@Parameter(names = {"-c","-cmd"}, description="Table configuration commands", converter=FileNameConverter.class,required=true)
	private File cmd;
	
	@Parameter(names = {"-ll","-loglevel"}, description="Log level")
	String loglevel;
    
	@Parameter(names = "-help",help=true,description="Produces this output")
	private boolean help; 
	
    public static class FileNameConverter implements IStringConverter<File>{
		@Override
		public File convert(String value) {
			return new File(value);
		}
	}
}
