package com.p4.stepper.runner;

import java.io.InputStream;
import java.util.BitSet;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.p4.stepper.EmitUtils;
import com.p4.stepper.Extractor;
import com.p4.stepper.Packet;
import com.p4.stepper.SOM;
import com.p4.stepper.data.Context;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;


public class StepperRunner {
	
	private static final Logger L = LoggerFactory.getLogger(StepperRunner.class);
	private static JCommander jc = null;
	private static CommandParser cp = new CommandParser();

	public static void main(String[] args) {
		Context context = null;
		
		try{
			jc = new JCommander(cp);
			jc.setProgramName("stepper");
			jc.parse(args);
			if(cp.isHelp()){
				jc.usage();
				System.exit(0);
			}
		}catch(Exception e){
			jc.usage();
			System.exit(1);
		}
		
		String ll = cp.getLoglevel() == null || cp.getLoglevel().isEmpty() ? "info" : cp.getLoglevel();
		Utils.setRootLogLevel(ll);
		
		try {
			InputStream is = FileUtils.getInputStream(cp.getJson());
			if(is != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				context = new Context(mapper.readValue(is, Context.UnNormalized.class));
			}
			
			String pktString = FileUtils.readFileIntoString(cp.getIpkt());
			L.info("Packet IN : " + pktString);
			Packet packet = new Packet(pktString);
			BitSet inputBitSet = (BitSet) packet.getPacketBitset().clone();
			Extractor extractor = new Extractor(packet.getPacketBitset(), context);
			extractor.extractPacket();
			
			SOM som = new SOM(FileUtils.ReadLines(cp.getCmd()));
			som.loadSOM(context);
			
			StepperSession ss = new StepperSession(context, extractor.getPacketByteVector(), extractor.getPacketBitVector(), som);
			ss.run();
			
			Packet pktOut = new Packet(ss.getPacketOut());
			BitSet outputBitSet = (BitSet) ss.getPacketOut().clone();
			L.info("Packet OUT : " + pktOut.getPacketString());
			EmitUtils.emitPacketDiff(inputBitSet, outputBitSet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
