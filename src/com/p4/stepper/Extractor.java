package com.p4.stepper;

import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

import com.p4.stepper.data.Context;
import com.p4.stepper.data.Field;
import com.p4.stepper.data.ParserState;
import com.p4.stepper.data.Transition;
import com.p4.utils.Utils;

public class Extractor {

	private BitSet packet;
	private Context context;
	@Getter private BitSet packetByteVector;
	@Getter private BitSet packetBitVector;
	
	private final Logger L = LoggerFactory.getLogger(Extractor.class);
	
	public Extractor(BitSet p, Context c) {
		packet = p;
		context = c;
		packetByteVector = new BitSet(Constants.PACKET_BYTEVECTOR_LEN);
		packetBitVector = new BitSet(Constants.PACKET_BITVECTOR_LEN);
	}

	private ParserState getNextState(String currentState, int headerIdx) {
		ParserState currState = context.getParserMap().get(currentState);
		if(currState.getTransitions() != null && currState.getTransitions().size() > 0) {
			for(Transition transition: currState.getTransitions()) {
				if(transition.getSelect() == null) {
					if(transition.getNextState() == null) {
						throw new RuntimeException("unable to figure out next state");
					}
					if(context.getParserMap().containsKey(transition.getNextState()))
						return context.getParserMap().get(transition.getNextState());
					else
						return null;
				}
				else {
					int numSelects = transition.getSelect().size();
					boolean thisIsNextHeader = true;
					for(int i=0; i<numSelects; i++) {
						String caseStr = transition.getSelect().get(i);
						String valueStr = transition.getValues().get(i);
						if(valueStr == Constants.DEFAULT && transition.getNextState() == Constants.ACCEPT)
							return null;
						Field f = currState.getFieldsMap().get(caseStr);
						BitSet bs1 = packet.get(headerIdx + f.getSrcOffset(), headerIdx + f.getSrcOffset() + f.getSize());
						BitSet bs2 = Utils.stringToBitSet(valueStr, f.getSize(), 16, true);
						if(!bs1.equals(bs2)) {
							thisIsNextHeader = false;
							break;
						}
					}
					if(thisIsNextHeader)
						return context.getParserMap().get(transition.getNextState());
				}
			}
		}
		//something wrong, stop here
		throw new RuntimeException("Unexpected error: Transition is null");
	}
 	
	public void extractPacket() {
		String current = Constants.START;
		int headerIdx = 0;
		int prevHeaderIdx = 0;
		while(true) {
			ParserState next = getNextState(current, prevHeaderIdx);
			if(next == null) {
				L.info("Finished extracting input packet to byte vector");
				break;
			}
			L.info("Extracting state - " + next.getName());
			for(Field f: next.getFields()) {
				for(int i=0; i<f.getSize(); i++) {
					if(packet.get(headerIdx + f.getSrcOffset() + i))
						packetByteVector.set(f.getDstOffset() + i);
				}
					
			}
			packetBitVector.set(next.getValidLoc());
			prevHeaderIdx = headerIdx;
			headerIdx += next.getHeaderSize();
			current = next.getName();
		}
		
		EmitUtils.emit(context, packetByteVector, packetBitVector);
	}
	
}
