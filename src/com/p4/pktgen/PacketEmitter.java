package com.p4.pktgen;

import java.io.File;
import java.util.List;

import com.p4.utils.FileUtils;
import com.p4.utils.Utils.Pair;

public class PacketEmitter {

	private File outputDir;
	private File inputPacket;
	private File nwinputPacket;
	private File inputPacketData;
	private File outputPacket;
	private File nwoutputPacket;
	private File outputPacketData;
	
	private Integer packetId;
	
	public PacketEmitter(File outDir) {
		outputDir = outDir;
		inputPacket = new File(outputDir.getAbsolutePath() + "/ipkt.txt");
		outputPacket = new File(outputDir.getAbsolutePath() + "/opkt.txt");
		nwinputPacket = new File(outputDir.getAbsolutePath() + "/nwipkt.txt");
		nwoutputPacket = new File(outputDir.getAbsolutePath() + "/nwopkt.txt");
		inputPacketData = new File(outputDir.getAbsolutePath() + "/ipktData.txt");
		outputPacketData = new File(outputDir.getAbsolutePath() + "/opktData.txt");
		packetId = 0;
		
		FileUtils.createNewFile(inputPacket, true);
		FileUtils.createNewFile(outputPacket, true);
		FileUtils.createNewFile(inputPacketData, true);
		FileUtils.createNewFile(outputPacketData, true);
	}
	
	public void emitPacket(Pair<Pair<String,String>, Pair<String,String>> packetPair1, Pair<Pair<String,String>, Pair<String,String>> packetPair2) {
		try {
			FileUtils.writeToFile(inputPacket, true, packetPair1.first().first()+"\n");
			FileUtils.writeToFile(outputPacket, true, packetPair1.second().first()+"\n");
			FileUtils.writeToFile(nwinputPacket, true, packetPair2.first().first()+"\n");
			FileUtils.writeToFile(nwoutputPacket, true, packetPair2.second().first()+"\n");
			FileUtils.writeToFile(inputPacketData, true, "============ PACKET-" + packetId + " ============\n" + packetPair1.first().second()+"\n\n");
			FileUtils.writeToFile(outputPacketData, true, "============ PACKET-" + packetId + " ============\n" + packetPair1.second().second()+"\n\n");
			packetId++;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
