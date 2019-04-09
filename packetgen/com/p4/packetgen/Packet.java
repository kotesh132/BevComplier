package com.p4.packetgen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.P4KeySet;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.parser.SourceDestinationSize;
import com.p4.p416.expressions.ParserALUOp;
import com.p4.pktgen.Checksum;
import com.p4.pktgen.config.packet.PacketConfig;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

import lombok.Getter;

public class Packet {
	@Getter private BitSet packetVector;
	@Getter private BitSet bitVector; //for bit fields
	private Map<Integer, ArrayList<Pair<String,Pair<Integer,Integer>>>> allHeaderFieldOffsetsAndSizes;
	private Map<String, List<Pair<Integer,Integer>>> pktHdrOffsetsAndSizes;
	private Map<Integer,P4State> pktHdrs;
	private Map<String, Pair<Integer, Integer>> allFieldsOffsetsAndSize;
	private List<ParserALUOp> varFields;
	private List<Pair<BitSet, Integer>> randomPaddedBits;
	private Map<String, Integer> fixedValueFields;
	private Map<String, Pair<Integer,Pair<Integer, Integer>>> relativeOffsetsAndSizes;
	private Map<String, String> constraints;
	private PacketConfig packetConfig;
	private Map<String,Integer> headerStateIds;
	
	private int packetLen;
	private int byteVectorLen;
	private int bitVectorLen;
	private int totalBitsInPacket;
	
	//Hack for adding some debug info for test bench
	private boolean isPiePresent = false;
	private Pair<Integer,Integer> pieIndexAndSize = Pair.of(-1,0);
	private boolean isCurrentHeaderPie = false;
	private Integer pieDebugStartOffset = 64;
	private BitSet headersPresent = new BitSet(16);
	//End of hack variables
	
	private static int counter = 0;
	@Getter private int packetId;
	
	private Map<String,String> inputFields;
	private Map<String,String> outputFields;
	
	private final Logger L = LoggerFactory.getLogger(Packet.class);
	
	public Packet(List<P4State> path, Map<String,Integer> headerIds, P4State start, P4State end, Integer pktLen, 
			Integer bytVecLen, Integer bitVecLen, PacketConfig pktConfig, Map<String, String> pktConstraints, 
			Map<Integer, ArrayList<Pair<String,Pair<Integer,Integer>>>> allHdrFieldOffsetsAndSizes, 
			Integer maxByteVectorLength, Map<String,String> prefixedValues, Map<Integer,P4State> allheaders) {
		
		packetId = counter;
		counter++;
		packetLen = pktLen == null ? 1024 : pktLen;
		byteVectorLen = maxByteVectorLength;
		bitVectorLen = bitVecLen == null ? 32 : bitVecLen;
		packetConfig = pktConfig;
		totalBitsInPacket = 0;
		constraints = pktConstraints == null ? new HashMap<String, String>() : pktConstraints;
		allHeaderFieldOffsetsAndSizes = allHdrFieldOffsetsAndSizes;
		headerStateIds = headerIds;
		pktHdrs = allheaders;
		
		byte[] randomBytes = new byte[byteVectorLen];
		new Random().nextBytes(randomBytes);
		BitSet bs = BitSet.valueOf(randomBytes);
		bitVector = new BitSet(bitVectorLen);
		
		ArrayList<P4State> pathArray = new ArrayList<P4State>();
		pathArray.addAll(path);
		
		varFields = new ArrayList<ParserALUOp>(packetConfig.getBitVectorLength());
		randomPaddedBits = new ArrayList<Pair<BitSet, Integer>>(packetConfig.getBitVectorLength());
		fixedValueFields = new HashMap<String, Integer>();
		relativeOffsetsAndSizes = new HashMap<String, Pair<Integer,Pair<Integer, Integer>>>();
		
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			varFields.add(i, null);
			randomPaddedBits.add(i, null);
		}
		
		for(int i=0; i<pathArray.size(); i++) {
			P4State state = pathArray.get(i);
			if (state.equals(start) || state.equals(end) || state.getExtracts().isEmpty())
				continue;
			if(state.getExtracts().get(0).getHeader().equals("pie_header")) {
				isPiePresent = true;
				isCurrentHeaderPie = true;
			} else {
				isCurrentHeaderPie = false;
			}
			
			P4State nextState = pathArray.get(i+1);
			L.debug("state : "+state.getName() + " next state : "+nextState.getName());
			bitVector.set(state.getExtracts().get(0).getValidLoc());
				
			if (state.getTransition() != null && state.getTransition().getSelect() != null && state.getTransition().getSelect().getTransitions() != null) {
				
				for(P4KeySet keyset: state.getTransition().getSelect().getTransitions()) {
					if(keyset.getState().equals(nextState.getName())) {
						List<String> expressions = state.getTransition().getSelect().getExpressions();
						for(int j=0; j<expressions.size(); j++) {
							String caseString = keyset.getCasest().get(j);
							if(caseString.contains("default")) {
								
								List<List<BitSet>> allCaseStValues = new ArrayList<List<BitSet>>();
								for(P4KeySet pks: state.getTransition().getSelect().getTransitions()) {
									if(expressions.size() == pks.getCasest().size() && !pks.getCasest().get(0).contains("default")) {
										List<BitSet> csList = new ArrayList<BitSet>();
										for(int k=0; k<expressions.size(); k++) {
											String csStr = pks.getCasest().get(k);
											csStr = csStr.replaceAll(".*w", "");
											BitSet csBs = Utils.stringToBitSet(csStr, getfieldOffsetAndSize().get(expressions.get(k)).second(), 16, true);
											csList.add(k,csBs);
										}
										allCaseStValues.add(csList);
									}
								}
								
								for(int k=0; k<expressions.size(); k++) {
									int size = getfieldOffsetAndSize().get(expressions.get(k)).second();
									while(true) {
										BitSet randomBitSet = Utils.randomBitSet(size);
										boolean isUnique = true;
										for(int l=0; l<allCaseStValues.size(); l++) {
											if(allCaseStValues.get(l).get(k).equals(randomBitSet)) {
												isUnique = false;
												break;
											}
										}
										if(isUnique) {
											int offset = getfieldOffsetAndSize().get(expressions.get(k)).first();
											for(int index=0; index<size; index++) {
												if(randomBitSet.get(index))
													bs.set(offset + index);
												else
													bs.clear(offset + index);
											}
											break;
										}
									}
								}
								break;
							}
							else{
								fixedValueFields.put(expressions.get(j), 1);
								caseString = caseString.replaceAll(".*w", "");
								if(getfieldOffsetAndSize().get(expressions.get(j)) != null) {
									int offset = getfieldOffsetAndSize().get(expressions.get(j)).first();
									int size = getfieldOffsetAndSize().get(expressions.get(j)).second();
									BitSet caseBitset = Utils.stringToBitSet(caseString, size, 16, true);
									for(int index=0; index<size; index++) {
										if(caseBitset.get(index))
											bs.set(offset + index);
										else
											bs.clear(offset + index);
									}
									prefixedValues.put(expressions.get(j), Utils.bitSetToHex(bs.get(offset, offset+size)));
								}
								else {
									throw new RuntimeException("Unable to resolve offset/size for the select expression " + expressions.get(j));
								}
							}
						}
					}
				}
			}
			int headerSize = 0;
			for(SourceDestinationSize sds: state.getExtracts().get(0).getFields()) {
				headerSize += getfieldOffsetAndSize().get(sds.getFullName()).second();
				totalBitsInPacket += getfieldOffsetAndSize().get(sds.getFullName()).second();
				if(constraints.containsKey(sds.getFullName()) && !fixedValueFields.containsKey(sds.getFullName())) {
					if(!constraints.get(sds.getFullName()).equals("*")) {
						if(!constraints.get(sds.getFullName()).toLowerCase().startsWith("0x"))
							throw new RuntimeException("Contraints have to be specified in hex");
						Utils.setValueInBitSet(bs, getfieldOffsetAndSize().get(sds.getFullName()).first(), getfieldOffsetAndSize().get(sds.getFullName()).first() + getfieldOffsetAndSize().get(sds.getFullName()).second(), Utils.stringToBitSet(constraints.get(sds.getFullName()), getfieldOffsetAndSize().get(sds.getFullName()).second(), 16, true));
					}
				}
				relativeOffsetsAndSizes.put(sds.getFullName(), Pair.of(state.getExtracts().get(0).getValidLoc(),Pair.of(sds.getSourceBit(), sds.getSize())));
			}
			if(state.getExtracts().get(0).isALUneeded()) {
				varFields.set(state.getExtracts().get(0).getValidLoc(), state.getExtracts().get(0).getParserALUOp());
			}
			if(isCurrentHeaderPie)
				pieIndexAndSize = Pair.of(state.getExtracts().get(0).getValidLoc(), headerSize);
		}
		this.packetVector = bs;
	}
	
	public PacketMeta getDpktFromBitset(BitSet bs) {
		BitSet dpkt = new BitSet(packetLen);
		BitSet tpkt = new BitSet(packetLen);
		int tidx = packetLen - 1;
		int sidx = 0;
		int eidx = 0;
		int index = 0;
		int totalBitsIndex = 0;
		Map<Integer, Integer> headerStartIndexMap = new HashMap<Integer, Integer>();
		Map<Integer, List<Pair<Integer,Integer>>> offsetsNSizes = new HashMap<Integer, List<Pair<Integer,Integer>>>();
		List<Pair<Integer,Integer>> headerIdAndOffsets = new ArrayList<Pair<Integer,Integer>>();
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			List<Pair<Integer,Integer>> hdrOffsets = new ArrayList<Pair<Integer,Integer>>();
			int headerSize = 0;
			int length = varFields.get(i) != null ? allHeaderFieldOffsetsAndSizes.get(i).size() - 1 : allHeaderFieldOffsetsAndSizes.get(i).size();
			int varfieldSize = allHeaderFieldOffsetsAndSizes.get(i).get(allHeaderFieldOffsetsAndSizes.get(i).size() - 1).second().second();
			int headerStartIdx = index;
			headerStartIndexMap.put(i, headerStartIdx);
			headerIdAndOffsets.add(Pair.of(headerStateIds.get(pktHdrs.get(i).getName()), headerStartIdx));
			for(int j=0; j<length; j++) {
				Pair<Integer,Integer> fieldBound = allHeaderFieldOffsetsAndSizes.get(i).get(j).second();
				int end = fieldBound.first()+fieldBound.second();
				hdrOffsets.add(Pair.of(index, index+fieldBound.second()-1));
				for(int k=fieldBound.first(); k<end; k++) {
					if(bs.get(k))
						dpkt.set(index);
					index++;
					headerSize++;
				}
			}
			if(varFields.get(i) != null) {
				if(randomPaddedBits.get(i) == null) {
					int a = (int) Math.pow(2,varFields.get(i).getA().getValue());
					int b = varFields.get(i).getB().getValue();
					int xOffset = getfieldOffsetAndSize().get(varFields.get(i).getFieldName()).first();
					int xSize = varFields.get(i).getSize();
					int opcode = varFields.get(i).getOpCode().getValue();
					int totalBitsToExtract = 0;

					if(!fixedValueFields.containsKey(varFields.get(i).getFieldName()) && (!constraints.containsKey(varFields.get(i).getFieldName())
							|| (constraints.containsKey(varFields.get(i).getFieldName()) && constraints.get(varFields.get(i).getFieldName()).equals("*")))) {
						int lowerLimit = (int) Math.ceil(((int) Math.ceil(headerSize/8) - b)/a);
						int upperLimit = (int) Math.floor(((int) Math.ceil((headerSize + varfieldSize)/8) - b)/a);
						int randomValue = (new Random()).nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
						
						BitSet randomBitset = Utils.longToBitset(randomValue, xSize);
						
						for(int k=0,j=xOffset,l=headerStartIndexMap.get(relativeOffsetsAndSizes.get(varFields.get(i).getFieldName()).first())+relativeOffsetsAndSizes.get(varFields.get(i).getFieldName()).second().first(); j<(xOffset+xSize); j++, k++, l++) {
							if(randomBitset.get(k)) {
								bs.set(j);
								dpkt.set(l);
							}
							else {
								bs.clear(j);
								dpkt.clear(l);
							}
						}
					}
					
					if(opcode == 13) {
						totalBitsToExtract = (int) (a * Utils.bitSetToLong(bs.get(xOffset, xOffset + xSize)) + b) * 8;
					}
					else {
						throw new RuntimeException("Currently only opcode 13 is supported");
					}
					int bitsToPad = totalBitsToExtract - headerSize;

					Random random = new Random();
					byte[] randomBytes = new byte[(int) Math.ceil(bitsToPad/8)];
					random.nextBytes(randomBytes);
					BitSet paddedBits = BitSet.valueOf(randomBytes);
					randomPaddedBits.set(i, Pair.of(paddedBits, bitsToPad));
				}
				hdrOffsets.add(Pair.of(index, index+randomPaddedBits.get(i).second()-1));
				sidx = index;
				for(int j=0; j<randomPaddedBits.get(i).second(); j++) {
					if(randomPaddedBits.get(i).first().get(j))
						dpkt.set(index);
					index++;
				}
				eidx = index-1;
			}
			
			if(!(isPiePresent && bitVector.get(pieIndexAndSize.first()) && pieIndexAndSize.first() == i)) {
				int fieldIdx = headerStartIdx;
				for(int j=0; j<length; j++) {
					Pair<Integer,Integer> fieldBound = allHeaderFieldOffsetsAndSizes.get(i).get(j).second();
					BitSet hdrField = dpkt.get(fieldIdx, fieldIdx + fieldBound.second());
					for(int k=fieldBound.second()-1; k>=0; k--) {
						if(hdrField.get(k))
							tpkt.set(tidx);
						tidx--;
					}
					fieldIdx += fieldBound.second();
				}
				if(varFields.get(i) != null) {
					for(int k=eidx; k>=sidx; k--) {
						if(dpkt.get(k))
							tpkt.set(tidx);
						tidx--;
					}
				}
			}
			
			if(index > packetLen)
				throw new RuntimeException("Unable the construct the packet with the size specified. Increase the packet length in the input json file");
			
			//Hack for adding some debug info for test bench
			if(isPiePresent && bitVector.get(pieIndexAndSize.first()) && pieIndexAndSize.first() == i) {
				//reset everything to 0
				//update fields from bits 64 to 96 with required info
				int idx=headerStartIdx;
				for(Pair<String,Pair<Integer,Integer>> headerField : allHeaderFieldOffsetsAndSizes.get(pieIndexAndSize.first())) {
					if(fixedValueFields.get(headerField.first()) == null) 
						dpkt.clear(idx, idx + headerField.second().second());
					idx += headerField.second().second();
				}
				int pidx = headerStartIdx + pieDebugStartOffset;
				BitSet pid = Utils.longToBitset(packetId, 16);
				for(int j=0; j<16; j++) {
					if(pid.get(j))
						dpkt.set(pidx);
					pidx++;
				}
				for(int j=0; j<16; j++) {
					if(headersPresent.get(j))
						dpkt.set(pidx);
					pidx++;
				}
				totalBitsIndex = pidx;
			}
			//End of hack
			
			offsetsNSizes.put(i, hdrOffsets);
		}
		BitSet totBytes = Utils.longToBitset((long) Math.ceil(index/8), 16);
		for(int j=0; j<16; j++) {
			if(totBytes.get(j))
				dpkt.set(totalBitsIndex);
			totalBitsIndex++;
		}
		
		Checksum chksum = new Checksum((BitSet)dpkt.clone(), packetConfig.getChecksumHdrs(), pktHdrs, getPacketFieldsOffsetAndSize());
		chksum.calculateChecksum();
		dpkt = chksum.getPacketWithChecksum();
		
		BitSet endianPkt = new BitSet(packetLen);
		int pktIdx = 0;
		if(isPiePresent && bitVector.get(pieIndexAndSize.first())) {
			for(int j=0; j<offsetsNSizes.get(pieIndexAndSize.first()).size(); j++) {
				Pair<Integer,Integer> pair = offsetsNSizes.get(pieIndexAndSize.first()).get(j);
				for(int k=pair.first(); k<=pair.second(); k++) {
					if(dpkt.get(k))
						endianPkt.set(pktIdx);
					pktIdx++;
				}
			}
		}
		
		int bidx = packetLen;
		if(bidx < 256)
			throw new RuntimeException("Something went wrong. check the indexes of temporary bitset");
		while(true) {
			BitSet beat = tpkt.get(bidx-256,bidx);
			for(int j=0; j<256; j++) {
				if(beat.get(j))
					endianPkt.set(pktIdx);
				pktIdx++;
			}
			bidx -= 256;
			if(bidx < 0 || bidx < tidx)
				break;
		}
		
		L.debug("Length of packet : " + dpkt.length());
		return new PacketMeta(packetLen,dpkt,endianPkt,getHeaderFieldsData(dpkt).first(),getHeaderFieldsData(dpkt).second(),headerIdAndOffsets);
	}
	
	private Map<String,List<Pair<Integer,Integer>>> getPacketFieldsOffsetAndSize() {
		int index = 0;
		Map<String,List<Pair<Integer,Integer>>> pktFieldsOffsetsAndSizes = new HashMap<String,List<Pair<Integer,Integer>>>();
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			int localIndex = 0;
			List<Pair<Integer,Integer>> fieldOffsetAndSize = new ArrayList<Pair<Integer,Integer>>();
			int length = varFields.get(i) != null ? allHeaderFieldOffsetsAndSizes.get(i).size() - 1 : allHeaderFieldOffsetsAndSizes.get(i).size();
			
			int j=0;
			for(; j<length; j++) {
				Pair<String, Pair<Integer, Integer>> pair = allHeaderFieldOffsetsAndSizes.get(i).get(j);
				fieldOffsetAndSize.add(j,Pair.of(index,pair.second().second()));
				index += pair.second().second();
				localIndex += pair.second().second();
			}
			if(randomPaddedBits.get(i) != null) {
				fieldOffsetAndSize.add(j,Pair.of(index,randomPaddedBits.get(i).second()));
				index += randomPaddedBits.get(i).second();
			}
			pktFieldsOffsetsAndSizes.put(pktHdrs.get(i).getExtracts().get(0).getHeader(),fieldOffsetAndSize);
		}
		return pktFieldsOffsetsAndSizes;
	}
	
	private Pair<String,BitSet> getHeaderFieldsData(BitSet pkt) {
		int index = 0;
		int nwIdx = packetLen-1;
		StringBuilder packetData = new StringBuilder();
		BitSet nwpkt = new BitSet(packetLen);
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			int length = varFields.get(i) != null ? allHeaderFieldOffsetsAndSizes.get(i).size() - 1 : allHeaderFieldOffsetsAndSizes.get(i).size();
			
			for(int j=0; j<length; j++) {
				Pair<String, Pair<Integer, Integer>> pair = allHeaderFieldOffsetsAndSizes.get(i).get(j);
				packetData.append(pair.first() + " : " + pair.second().second() + "'h" + Utils.bitSetToHex(pkt.get(index, index+pair.second().second())) + "\n");
				
				BitSet field = pkt.get(index, index+pair.second().second());
				for(int k=pair.second().second()-1; k>=0; k--) {
					if(field.get(k))
						nwpkt.set(nwIdx);
					else
						nwpkt.clear(nwIdx);
					nwIdx--;
				}
				
				index += pair.second().second();
			}
			if(randomPaddedBits.get(i) != null) {
				packetData.append("padded bits : " + randomPaddedBits.get(i).second() + "'h" + Utils.bitSetToHex(randomPaddedBits.get(i).first()) + "\n");
				
				BitSet field = randomPaddedBits.get(i).first();
				for(int k=randomPaddedBits.get(i).second()-1; k>=0; k--) {
					if(field.get(k))
						nwpkt.set(nwIdx);
					else
						nwpkt.clear(nwIdx);
					nwIdx--;
				}
				
				index += randomPaddedBits.get(i).second();
			}
		}
		return Pair.of(packetData.toString(),nwpkt);
	}
	
	public Map<String,String> getPacketFieldValues(BitSet pkt) {
		Map<String,String> fieldVals = new HashMap<String,String>();
		int index = 0;
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			int length = varFields.get(i) != null ? allHeaderFieldOffsetsAndSizes.get(i).size() - 1 : allHeaderFieldOffsetsAndSizes.get(i).size();

			for(int j=0; j<length; j++) {
				Pair<String, Pair<Integer, Integer>> pair = allHeaderFieldOffsetsAndSizes.get(i).get(j);
				fieldVals.put(pair.first(),Utils.bitSetToHex(pkt.get(index, index+pair.second().second())));
				index += pair.second().second();
			}
			
			if(randomPaddedBits.get(i) != null) {
				Pair<String, Pair<Integer, Integer>> pair = allHeaderFieldOffsetsAndSizes.get(i).get(length);
				fieldVals.put(pair.first(),Utils.bitSetToHex(randomPaddedBits.get(i).first()));
				index += randomPaddedBits.get(i).second();
			}
		}
		return fieldVals;
	}
	
	public String bitSetToHex(BitSet bs) {
		StringBuilder sb = new StringBuilder();
		for(int i=packetLen - 1; i>=0; i -= 4) {
			BitSet b = bs.get(i-3,i+1);
			sb.append(Utils.bitSetToHex(b));
		}
		return sb.toString();
	}
	
	public PacketMeta getPacket() {
		return getDpktFromBitset(packetVector);
	}
	
	public void setValue(BitSet value, int offset, int size) {
		for(int index=0; index<size; index++) {
			if(value.get(index))
				packetVector.set(offset + index);
			else
				packetVector.clear(offset + index);
		}
	}
	
	public BitSet getValue(int offset, int size) {
		return packetVector.get(offset, offset+size);
	}
	
	public boolean isHeaderVariable(int offset) {
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			for(Pair<String, Pair<Integer,Integer>> headerPairs : allHeaderFieldOffsetsAndSizes.get(i)) {
				Pair<Integer,Integer> fieldBound = headerPairs.second();
				if(fieldBound.first() == offset)
					return true;
			}
		}
		return false;
	}
	
	public List<ArrayList<Pair<Integer,Integer>>> getHeaderFieldOffsetsAndSizes() {
		List<ArrayList<Pair<Integer,Integer>>> offsetsAndSizes = new ArrayList<ArrayList<Pair<Integer,Integer>>>();
		for(int i=0; i<packetConfig.getBitVectorLength(); i++) {
			if(!bitVector.get(i)) continue;
			ArrayList<Pair<Integer, Integer>> pairsList = new ArrayList<Pair<Integer, Integer>>();
			for(Pair<String, Pair<Integer,Integer>> headerPairs : allHeaderFieldOffsetsAndSizes.get(i)) {
				pairsList.add(headerPairs.second());
			}
			offsetsAndSizes.add(pairsList);
		}
		return offsetsAndSizes;
	}
	
	public Map<String, Pair<Integer, Integer>> getfieldOffsetAndSize() {
		if(allFieldsOffsetsAndSize == null) {
			allFieldsOffsetsAndSize = new HashMap<String, Pair<Integer, Integer>>();
			for(Integer headerLocation : allHeaderFieldOffsetsAndSizes.keySet()) {
				for(Pair<String, Pair<Integer,Integer>> headerPairs : allHeaderFieldOffsetsAndSizes.get(headerLocation)) {
					allFieldsOffsetsAndSize.put(headerPairs.first(), headerPairs.second());
				}
			}
		}
		return allFieldsOffsetsAndSize;
	}
	
	public boolean isHeaderPartOfPacket(int validLocation) {
		return bitVector.get(validLocation);
	}
}
