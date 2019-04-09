package com.p4.stepper;

import java.util.BitSet;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.stepper.data.Context;
import com.p4.utils.Utils;

public class EmitUtils {
	
	private static final Logger L = LoggerFactory.getLogger(EmitUtils.class);

	public static void emit(Context context, BitSet byteVector, BitSet bitVector) {
		L.info("Header field values : ");
		for(Entry<String, Integer[]> entry : context.getHeaderOffsetAndSize().entrySet()) {
			if(entry.getKey().endsWith("isValid")) continue;
			L.info(entry.getKey() + " - " + Utils.bitSetToHex(byteVector.get(entry.getValue()[0], entry.getValue()[0] + entry.getValue()[1]), entry.getValue()[1]));
		}
		
		L.info("Predicate field values : ");
		for(Entry<String, Integer> entry : context.getPredicateOffsets().entrySet()) {
			L.info(entry.getKey() + " - " + bitVector.get(entry.getValue()));
		}
	}
	
	public static void emitPacketDiff(BitSet currByteVector, BitSet prevByteVector) {
		L.info("packet diff:");
		emitBytesDiff(prevByteVector, currByteVector, Constants.PACKETLEN, "inPkt", "outPkt");
	}
	
	public static void emitByteVectorDiff(BitSet currByteVector, BitSet prevByteVector) {
		L.info("byte vector diff:");
		emitBytesDiff(currByteVector, prevByteVector, Constants.PACKET_BYTEVECTOR_LEN, "prev", "curr");
	}
	
	private static void emitBytesDiff(BitSet currByteVector, BitSet prevByteVector, int len, String ip, String op) {
		int byteIndex = 0;
		boolean hasAnythingChanged = true;
		for(int i=0; i<len; i+=8) {
			String curr = Utils.bitSetToHex(currByteVector.get(i, i+8), 8);
			String prev = Utils.bitSetToHex(prevByteVector.get(i, i+8), 8);
			if(!curr.equals(prev)) {
				L.info("byte index : " + byteIndex + ", "+ ip +" : " + prev + ", "+ op +" : " + curr);
				hasAnythingChanged = false;
			}
			byteIndex++;
		}
		if(hasAnythingChanged)
			L.info("None");
	}
	
	public static void emitBitVectorDiff(BitSet currBitVector, BitSet prevBitVector) {
		boolean hasAnythingChanged = true;
		L.info("bit vector diff:");
		for(int i=0; i<Constants.PACKET_BITVECTOR_LEN; i++) {
			if(currBitVector.get(i) != prevBitVector.get(i)) {
				L.info("bit index : " + i + ", prev : " + prevBitVector.get(i) + ", curr : " + currBitVector.get(i));
				hasAnythingChanged = false;
			}
		}
		if(hasAnythingChanged)
			L.info("None");
	}
}
