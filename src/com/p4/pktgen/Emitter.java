package com.p4.pktgen;

import java.io.File;
import java.util.List;

import com.p4.pktgen.model.SOMModel;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils.Pair;

public class Emitter {

	private File outputDir;
	private SOMModel somModel;
	private List<Pair<Pair<String,String>, Pair<String,String>>> packets;
	private List<Pair<List<Pair<String,String>>,List<Pair<String,String>>>> pktFieldValues;
	private List<List<Pair<Integer,Integer>>> packetHeaderInfo;
	private int pktId = 0;
	
	public Emitter(File outputDir, SOMModel somModel, List<Pair<Pair<String,String>, Pair<String,String>>> packets, List<Pair<List<Pair<String,String>>,List<Pair<String,String>>>> pktFieldValues, List<List<Pair<Integer,Integer>>> packetHeaderInfo) {
		this.outputDir = outputDir;
		this.somModel = somModel;
		this.packets = packets;
		this.pktFieldValues = pktFieldValues;
		this.packetHeaderInfo = packetHeaderInfo;
	}
	
	public Emitter(File outputDir, SOMModel somModel) {
		this.outputDir = outputDir;
		this.somModel = somModel;
	}
	
	public void emitModelConfig() {
		emitModelSv();
		emitRTLConfig();
		//emitInputOutputPkts();
	}
	
	public void emitSomUnitConfig() {
		emitRTLConfig();
	}
	
	private void emitModelSv() {
		File file = new File(outputDir.getAbsolutePath() + "/somCfg.sv");
		try {
			FileUtils.createNewFile(file, true);
			FileUtils.writeToFile(file, true, "initial\nbegin\n");
			FileUtils.writeToFile(file, true, somModel.emitModelConfig());
			FileUtils.writeToFile(file, true, "end");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void emitRTLConfig() {
		File dir = new File(outputDir.getAbsolutePath() + "/som");
		try {
			dir.mkdirs();
			File regfile = new File(dir.getAbsolutePath() + "/somRegCfg.sv");
			File dbgfile = new File(dir.getAbsolutePath() + "/dbginfo.txt");
			FileUtils.createNewFile(regfile, true);
			FileUtils.createNewFile(dbgfile, true);
			somModel.emitRTLConfig(regfile, dbgfile);
			somModel.emitKeyAndData(dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void emitInputOutputPkts(Pair<Pair<String,String>, Pair<String,String>> pair, Pair<List<Pair<String,String>>,List<Pair<String,String>>> valsList, List<Pair<Integer,Integer>> phinfo) {
		File ifile = new File(outputDir.getAbsolutePath() + "/ipkt.txt");
		File ofile = new File(outputDir.getAbsolutePath() + "/opkt.txt");
		File ifileData = new File(outputDir.getAbsolutePath() + "/ipktData.txt");
		File ofileData = new File(outputDir.getAbsolutePath() + "/opktData.txt");
		File bevfile = new File(outputDir.getAbsolutePath() + "/ibevPkt.txt");
		File pktInfo = new File(outputDir.getAbsolutePath() + "/headerOffsets.txt");
		try {
			FileUtils.CreateNewFile(ifile);
			FileUtils.CreateNewFile(ofile);
			FileUtils.CreateNewFile(ifileData);
			FileUtils.CreateNewFile(ofileData);
			FileUtils.CreateNewFile(bevfile);
			FileUtils.CreateNewFile(pktInfo);
			FileUtils.writeToFile(ifile, true, pair.first().first()+"\n");
			FileUtils.writeToFile(ofile, true, pair.second().first()+"\n");
			FileUtils.writeToFile(ifileData, true, "============ PACKET-" + pktId + " ============\n" + pair.first().second()+"\n\n");
			FileUtils.writeToFile(ofileData, true, "============ PACKET-" + pktId + " ============\n" + pair.second().second()+"\n\n");

			FileUtils.writeToFile(bevfile, true, pktId + ",0x" + valsList.first().get(valsList.first().size()-1).second());
			for(int i=0; i<valsList.first().size()-1; i++) {
				FileUtils.writeToFile(bevfile, true, ",0x" + valsList.first().get(i).second());
			}
			FileUtils.writeToFile(bevfile, true, "\n");
			FileUtils.writeToFile(bevfile, true, pktId + ",0x" + valsList.second().get(valsList.second().size()-1).second());
			for(int i=0; i<valsList.second().size()-1; i++) {
				FileUtils.writeToFile(bevfile, true, ",0x" + valsList.second().get(i).second());
			}
			FileUtils.writeToFile(bevfile, true, "\n");
			
			for(Pair<Integer,Integer> phpair : phinfo) {
				FileUtils.writeToFile(pktInfo, true, pktId + "," + phpair.first() + "," + phpair.second() + "\n");
			}
			pktId++;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
