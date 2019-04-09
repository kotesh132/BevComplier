package com.p4.stepper;

import java.util.BitSet;

import lombok.Getter;

import com.p4.utils.Utils;

@Getter
public class Packet {

	private String packetString;
	private BitSet packetBitset;
	
	public Packet(String pktString) {
		packetString = pktString;
		packetBitset = stringToBitset(pktString);
	}
	
	public Packet(BitSet pktBs) {
		packetBitset = pktBs;
		packetString = bitsetToString(pktBs);
	}
	
	private String bitsetToString(BitSet pktBs) {
		return Utils.bitSetToHex(pktBs, Constants.PACKETLEN);
	}
	
	private BitSet stringToBitset(String pktString) {
		BitSet bs = new BitSet(Constants.PACKETLEN);
		
		if(pktString != null && !pktString.isEmpty()) {
			int index = 0;
			String[] pktHex = pktString.split("");
			for(int i=pktHex.length-1; i>0; i--) {
				BitSet hbs = Utils.stringToBitSet(pktHex[i], Constants.HEX_WIDTH, 16, false);
				for(int j=0; j<Constants.HEX_WIDTH; j++) {
					if(hbs.get(j))
						bs.set(index);
					index++;
				}
			}
		}
		
		return bs;
	}
}
