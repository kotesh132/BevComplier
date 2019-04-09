package com.p4.pktgen;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p4.drmt.parser.P4State;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Checksum {

	private BitSet packet;
	private Set<String> chksumHdrs;
	private Map<Integer,P4State> hdrs;
	private Map<String,List<Pair<Integer,Integer>>> pktFieldOffsetsAndSizes;
	
	private final String IPV4 = "ipv4";
	private final String IPV6 = "ipv6";
	private final String TCP = "tcp";
	private final String UDP = "udp";
	
	public void calculateChecksum() {
		int index = 0;
		for(Integer headerId : hdrs.keySet()) {
			P4State header = hdrs.get(headerId);
			if(chksumHdrs != null && chksumHdrs.contains(header.getName())) {
				calculateHeaderChecksum(header.getName(),index);
			}
			index++;
		}
	}
	
	private void calculateHeaderChecksum(String header, int index) {
		if(header.equals(IPV4)) {
			calcIpv4Checksum(index);
		}
		else if(header.equals(IPV4)) {
			calcTcpChecksum(index);
		}
		else if(header.equals(IPV4)) {
			calcUdpChecksum(index);
		}
	}
	
	private void calcIpv4Checksum(int index) {
		
		int checksumFieldIdx = 9;
		int totalSizeFieldIdx = 3;
		
		List<Pair<Integer,Integer>> ipv4Offsets = pktFieldOffsetsAndSizes.get(IPV4);
		int numFields = ipv4Offsets.size();
		int start = ipv4Offsets.get(0).first();
		int end = ipv4Offsets.get(numFields - 1).first() + ipv4Offsets.get(numFields - 1).second();
		int size = end - start;
		int checksumIdx = ipv4Offsets.get(checksumFieldIdx).first();
		int checksumSize = ipv4Offsets.get(checksumFieldIdx).second();
		
		//clear ipv4 checksum field
		packet.clear(checksumIdx, checksumIdx + checksumSize);
		
		//fix total length field
		int lastHdrNumFields = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).size();
		Pair<Integer,Integer> lastHdrLastField = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).get(lastHdrNumFields);
		long totalSize = (long) Math.ceil((lastHdrLastField.first() + lastHdrLastField.second())/8);
		int totalSizeIdx = ipv4Offsets.get(totalSizeFieldIdx).first();
		int totalSizeSize = ipv4Offsets.get(totalSizeFieldIdx).second();
		BitSet sizeInBits = Utils.longToBitset(totalSize, ipv4Offsets.get(totalSizeFieldIdx).second());
		for(int i=0;i<totalSizeSize;i++) {
			if(sizeInBits.get(i))
				packet.set(totalSizeIdx + i);
			else
				packet.clear(totalSizeIdx + i);
		}
		
		BitSet ipv4Hdr = packet.get(start, end);
		
		BitSet checksum = ipv4Hdr.get(0, 16);
		//boolean c = false;
		for(int i=16; i<size; i+=16) {
			BitSet slice = ipv4Hdr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		checksum.flip(0, 16);
		
		for(int i=0;i<16;i++) {
			if(checksum.get(i))
				ipv4Hdr.set(checksumIdx + i);
		}
	}
	
	private void calcTcpChecksum(int index) {
		
		int checksumFieldIdx = 8;
		int dataOffsetFieldIdx = 4;
		
		List<Pair<Integer,Integer>> tcpOffsets = pktFieldOffsetsAndSizes.get(TCP);
		
		int numFields = tcpOffsets.size();
		int start = tcpOffsets.get(0).first();
		int end = tcpOffsets.get(numFields - 1).first() + tcpOffsets.get(numFields - 1).second();
		int size = end - start;
		int checksumIdx = tcpOffsets.get(checksumFieldIdx).first();
		int checksumSize = tcpOffsets.get(checksumFieldIdx).second();
		
		//clear tcp checksum field
		packet.clear(checksumIdx, checksumIdx + checksumSize);
		
		//fix dataOffset field
		long dataOffset = (long) Math.ceil((tcpOffsets.get(tcpOffsets.size()-1).first() + tcpOffsets.get(tcpOffsets.size()-1).second() - tcpOffsets.get(0).first())/32);
		int dataOffsetIdx = tcpOffsets.get(dataOffsetFieldIdx).first();
		int dataOffsetSize = tcpOffsets.get(dataOffsetFieldIdx).second();
		BitSet dataOffsetInBits = Utils.longToBitset(dataOffset, dataOffsetSize);
		for(int i=0;i<dataOffsetSize;i++) {
			if(dataOffsetInBits.get(i))
				packet.set(dataOffsetIdx + i);
			else
				packet.clear(dataOffsetIdx + i);
		}
		
		//BitSet tcpHdr = packet.get(start, end);
		
		BitSet checksum = calcPseudoHeader(index);
		int lastHdrNumFields = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).size();
		Pair<Integer,Integer> lastHdrLastField = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).get(lastHdrNumFields);
		BitSet tcpLen = Utils.longToBitset((long)Math.ceil((lastHdrLastField.first() + lastHdrLastField.second() - start)/8),16);
		checksum = add((BitSet)checksum.clone(), tcpLen, false, 16);
		
		int tcpHdrAndPayloadLen = lastHdrLastField.first() + lastHdrLastField.second() - start;
		
		for(int i=start; i<tcpHdrAndPayloadLen; i+=16) {
			BitSet slice = packet.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		checksum.flip(0, 16);
	}
	
	private void calcUdpChecksum(int index) {
		
		int checksumFieldIdx = 3;
		int lenFieldIdx = 2;
		
		List<Pair<Integer,Integer>> udpOffsets = pktFieldOffsetsAndSizes.get(UDP);
		
		int numFields = udpOffsets.size();
		int start = udpOffsets.get(0).first();
		int end = udpOffsets.get(numFields - 1).first() + udpOffsets.get(numFields - 1).second();
		int size = end - start;
		int checksumIdx = udpOffsets.get(checksumFieldIdx).first();
		int checksumSize = udpOffsets.get(checksumFieldIdx).second();
		
		//clear tcp checksum field
		packet.clear(checksumIdx, checksumIdx + checksumSize);
		
		//fix length field
		int lastHdrNumFields = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).size();
		Pair<Integer,Integer> lastHdrLastField = pktFieldOffsetsAndSizes.get(hdrs.get(hdrs.size()-1)).get(lastHdrNumFields);
		BitSet udpLenBitSet = Utils.longToBitset((long)Math.ceil((lastHdrLastField.first() + lastHdrLastField.second() - start)/8),16);
		for(int i=0;i<16;i++) {
			if(udpLenBitSet.get(i))
				packet.set(lenFieldIdx + i);
			else
				packet.clear(lenFieldIdx + i);
		}
		
		
		BitSet checksum = calcPseudoHeader(index);
		checksum = add((BitSet)checksum.clone(), udpLenBitSet, false, 16);
		
		int udpHdrAndPayloadLen = lastHdrLastField.first() + lastHdrLastField.second() - start;
		
		for(int i=start; i<udpHdrAndPayloadLen; i+=16) {
			BitSet slice = packet.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		checksum.flip(0, 16);
	}
	
	private BitSet calcPseudoHeader(int index) {
		for(int i=index-1; i>=0; i--) {
			if(hdrs.get(i).equals(IPV4))
				return calcIpv4PseudoChecksum();
			else if(hdrs.get(i).equals(IPV6))
				return calcIpv6PseudoChecksum();
		}
		throw new RuntimeException("could not find ipv4/ipv6 before this header");
	}
	
	private BitSet calcIpv4PseudoChecksum() {
		
		int srcAddrFieldIdx = 10;
		int dstAddrFieldIdx = 11;
		int protoFieldIdx = 8;
		
		List<Pair<Integer,Integer>> ipv4Offsets = pktFieldOffsetsAndSizes.get(IPV4);
		
		BitSet checksum = new BitSet(16);
		
		BitSet srcAddr = packet.get(ipv4Offsets.get(srcAddrFieldIdx).first(), ipv4Offsets.get(srcAddrFieldIdx).first() + ipv4Offsets.get(srcAddrFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(srcAddrFieldIdx).second(); i+=16) {
			BitSet slice = srcAddr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		BitSet dstAddr = packet.get(ipv4Offsets.get(dstAddrFieldIdx).first(), ipv4Offsets.get(dstAddrFieldIdx).first() + ipv4Offsets.get(dstAddrFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(dstAddrFieldIdx).second(); i+=16) {
			BitSet slice = dstAddr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		BitSet proto = packet.get(ipv4Offsets.get(protoFieldIdx).first(), ipv4Offsets.get(protoFieldIdx).first() + ipv4Offsets.get(protoFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(protoFieldIdx).second(); i+=16) {
			BitSet slice = proto.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		return checksum;
	}
	
	private BitSet calcIpv6PseudoChecksum() {
		
		int srcAddrFieldIdx = 6;
		int dstAddrFieldIdx = 7;
		int nextHdrFieldIdx = 4;
		
		List<Pair<Integer,Integer>> ipv4Offsets = pktFieldOffsetsAndSizes.get(IPV4);
		
		BitSet checksum = new BitSet(16);
		
		BitSet srcAddr = packet.get(ipv4Offsets.get(srcAddrFieldIdx).first(), ipv4Offsets.get(srcAddrFieldIdx).first() + ipv4Offsets.get(srcAddrFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(srcAddrFieldIdx).second(); i+=16) {
			BitSet slice = srcAddr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		BitSet dstAddr = packet.get(ipv4Offsets.get(dstAddrFieldIdx).first(), ipv4Offsets.get(dstAddrFieldIdx).first() + ipv4Offsets.get(dstAddrFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(dstAddrFieldIdx).second(); i+=16) {
			BitSet slice = dstAddr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		BitSet nextHdr = packet.get(ipv4Offsets.get(nextHdrFieldIdx).first(), ipv4Offsets.get(nextHdrFieldIdx).first() + ipv4Offsets.get(nextHdrFieldIdx).second());
		for(int i=0; i<ipv4Offsets.get(nextHdrFieldIdx).second(); i+=16) {
			BitSet slice = nextHdr.get(i, i+16);
			checksum = add((BitSet)checksum.clone(), slice, false, 16);
		}
		
		return checksum;
	}
	
	public BitSet getPacketWithChecksum() {
		return (BitSet) packet.clone();
	}
	
	private BitSet add(BitSet bs1, BitSet bs2, boolean c, int size) {
		BitSet res = (BitSet) bs1.clone();
		for(int j=0; j<size; j++) {
			boolean a = res.get(j);
			boolean b = bs2.get(j);
			if(!a && !b && !c) {
				continue;
			}
			else if(!a && !b && c) {
				res.set(j);
				c = false;
			}
			else if(!a && b && !c) {
				res.set(j);
			}
			else if(!a && b && c) {
				continue;
			}
			else if(a && !b && !c) {
				continue;
			}
			else if(a && !b && c) {
				res.clear(j);
			}
			else if(a && b && !c) {
				res.clear(j);
				c = true;
			}
			else {
				continue;
			}
		}
		return c ? add(res, new BitSet(), true, size) : res;
	}
}
