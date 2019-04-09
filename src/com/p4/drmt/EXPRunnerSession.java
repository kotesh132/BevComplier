package com.p4.drmt;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

import lombok.Data;
@Data
public class EXPRunnerSession {

	private static final Logger L = LoggerFactory.getLogger(EXPRunnerSession.class);

	public final CommandLineParser cp;

	public void run() {
		L.info("EXPRunner session : starting");
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(P4programContextExt.getExtendedContext(ctx));
			P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
			
		}
	}


}
