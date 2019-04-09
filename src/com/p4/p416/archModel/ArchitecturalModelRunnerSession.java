package com.p4.p416.archModel;

import java.io.File;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.utils.FileUtils;
import com.p4.packetgen.runner.P416ParserUtil;

import lombok.Data;
@Data
public class ArchitecturalModelRunnerSession {
	private static final Logger L = LoggerFactory.getLogger(ArchitecturalModelRunnerSession.class);

	public final CommandLineParser cp;

	//ArchitecturalModel
	public void run(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			AbstractBaseExt.getExtendedContext(ctx).defineSymbol(null);
			AbstractBaseExt.getExtendedContext(ctx).runArchitecturalModel();

			L.info("sram size is "+ ((P4programContextExt) AbstractBaseExt.getExtendedContext(ctx)).getSram());
			L.info("tcam size is "+((P4programContextExt) AbstractBaseExt.getExtendedContext(ctx)).getTcam());
			L.info("Completed arch model on "+f.getName());
		}
	}
	
}
