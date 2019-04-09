package com.p4.packetgen;

import java.util.BitSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.p4.utils.Utils.Pair;

@AllArgsConstructor
@Getter
public class PacketMeta {

	private Integer pktLen;
	private BitSet drmtFormatPkt;
	private BitSet endianFormatPkt;
	private String packetData;
	private BitSet nwFormatPkt;
	private List<Pair<Integer,Integer>> headerIdAndOffsets; 
}
