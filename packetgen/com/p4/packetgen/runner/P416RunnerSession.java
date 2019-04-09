package com.p4.packetgen.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.beust.jcommander.JCommander;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.packetgen.structures.Node;
import com.p4.packetgen.utils.HeadersInfo;
import com.p4.packetgen.utils.StatesInfo;
import com.p4.packetgen.utils.Utils;
import com.p4.utils.FileUtils;

import lombok.Data;

@Data
public class P416RunnerSession {
	private static final Logger L = LoggerFactory.getLogger(P416RunnerSession.class);

	private final CommandLineParser clp;
	private P416ParserUtil pP;
	private List<File> allFiles;

	private void index(){
		File dir = clp.getOutputDir();
		allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
	}

	public void run(){
		index();
		if((clp.getOutputDir().exists() || clp.getOutputDir().mkdirs())){
			pP = new P416ParserUtil();
			for(File file:allFiles){
				String text = FileUtils.readFileIntoString(file,"\n");
				P4programContext ctx = pP.getP416Context(text, file.getName());
				preprrocessing(ctx);
				StatesInfo si = new StatesInfo();
				HeadersInfo hi = new HeadersInfo();
				AbstractBaseExt.getExtendedContext(ctx).collectInfo(si,hi);
				//ctx.extendedContext.collectInfo(si,hi);
				si.setNode();
				List<ST> classes = hi.getSTs();
				ST headerClass = getPacketHeaderClass(ctx,si);
				writeTop(classes,headerClass,file);
				//writePathsWithWeights(si,grp);
			}
		}
	}

	private void preprrocessing(P4programContext ctx) {
		AbstractBaseExt.getExtendedContext(ctx).preProcess();
	}

	private void writeTop(List<ST> classes, ST headerClass, File file) {
		String name = file.getName().split("\\.")[0];
		List<ST> stmts = new ArrayList<ST>();
		stmts.addAll(classes);
		stmts.add(headerClass);
		ST out = Utils.getStgGrp().getInstanceOf("Top");
		out.add("stmts",stmts);
		File dir = clp.getOutputDir();
		String outFileName =name+"_pktgen.cpp";
		File outFile = new File(dir+"/"+outFileName);
		File PacketFile = new File(dir+"/Packet.cpp");
		if(outFile.exists()){
			FileUtils.Delete(outFile, true);
		}
		if(PacketFile.exists()){
			FileUtils.Delete(PacketFile, true);
		}
		try {
			PacketFile.createNewFile();
			outFile.createNewFile();
		} catch (IOException e) {
			L.error("cannot create file in out directory");
		}
		ST packetString = Utils.getStgGrp().getInstanceOf("PacketClass");
		packetString.add("filename",outFileName);
		FileUtils.writeToFile(PacketFile, false, packetString.render());
		FileUtils.writeToFile(outFile, false, out.render());
		for(File f:FileUtils.allFilesInDir("./packetgen/resources/files")){
			FileUtils.copyFileToDir(f.getAbsolutePath(),dir.getAbsolutePath());
		}
	}

	private ST getPacketHeaderClass(P4programContext ctx,StatesInfo si){
		Node n = si.getNode();
		n.calculateWeights();
		
		List<ST> constraintLines = new ArrayList<ST>();
		n.getConstraintLines(constraintLines);
		ST addConstraintsApiForPacketHeader = Utils.getStgGrp().getInstanceOf("AddConstraintsApiForPacketHeader");
		addConstraintsApiForPacketHeader.add("constraintLines", constraintLines);
		
		List<ST> variables = new ArrayList<ST>();
		AbstractBaseExt.getExtendedContext(ctx).getVariablesForHeaderClass(variables);
		List<ST> addedVariables = new ArrayList<ST>();
		AbstractBaseExt.getExtendedContext(ctx).getAddedVariablesForHeaderClass(addedVariables);
		
		ST constructor = null;
		if(addedVariables.size()>0){
			constructor = Utils.getStgGrp().getInstanceOf("constructorForPacketHeader");
			constructor.add("vars", addedVariables);
		}
		
		ST getLengthApi =Utils.getStgGrp().getInstanceOf("getLengthApi");
		getLengthApi.add("stmts",variables);

		ST getserializeApi = Utils.getStgGrp().getInstanceOf("getserializeApi");
		getserializeApi.add("stmts",variables);

		ST getdeserializeApi = Utils.getStgGrp().getInstanceOf("getdeserializeApi");
		getdeserializeApi.add("stmts",variables);

		ST getCMPApi = Utils.getStgGrp().getInstanceOf("getCMPApi");
		getCMPApi.add("stmts",variables);
		
		ST randomize = si.getRandomizeApi();
		ST toStrign = si.getToStringApi();

		List<ST> stmts = new ArrayList<ST>();
		stmts.addAll(variables);
		stmts.addAll(addedVariables);
		if(constructor != null)
				stmts.add(constructor);
		stmts.add(addConstraintsApiForPacketHeader);
		stmts.add(getLengthApi);
		stmts.add(getserializeApi);
		stmts.add(getdeserializeApi);
		stmts.add(getCMPApi);
		stmts.add(randomize);
		stmts.add(toStrign);
		
		ST packet_header = Utils.getStgGrp().getInstanceOf("PacketHeader");
		packet_header.add("stmts",stmts);
		return packet_header;
	}

	public static void main(String[] args)
	{
		CommandLineParser clp = new CommandLineParser(new File("./").getAbsolutePath());
		P416RunnerSession proteusRunnerSession = new P416RunnerSession(clp);
		JCommander jCommander = new JCommander(clp);
		jCommander.parse(args);
		proteusRunnerSession.run();
	}
}


