package com.p4.pktgen.cuckoo;

import java.util.BitSet;

import com.p4.utils.Utils;

public class CRC {

	private static Integer[][] CRC0 = new Integer[][]{
						{0,3,9,10,12,13,14,15,16,18,19,23,27,29,30,32,33,34,36,37,39,41,42,46,47,48,50,53,54,56,57,58,59,60,63,64,68,72,76},
						{0,1,3,4,9,11,12,17,18,20,23,24,27,28,29,31,32,35,36,38,39,40,41,43,46,49,50,51,53,55,56,61,63,65,68,69,72,73,76,77},
						{0,1,2,3,4,5,9,14,15,16,21,23,24,25,27,28,34,40,44,46,48,51,52,53,58,59,60,62,63,66,68,69,70,72,73,74,76,77,78},
						{79,78,77,75,74,73,71,70,69,67,64,63,61,60,59,54,53,52,49,47,45,41,35,29,28,26,25,24,22,17,16,15,10,6,5,4,3,2,1},
						{79,78,76,75,74,72,71,70,68,65,64,62,61,60,55,54,53,50,48,46,42,36,30,29,27,26,25,23,18,17,16,11,7,6,5,4,3,2},
						{79,77,75,73,71,69,68,66,65,64,62,61,60,59,58,57,55,53,51,50,49,48,46,43,42,41,39,36,34,33,32,31,29,28,26,24,23,17,16,15,14,13,10,9,8,7,6,5,4,0},
						{78,76,74,72,70,69,67,66,65,63,62,61,60,59,58,56,54,52,51,50,49,47,44,43,42,40,37,35,34,33,32,30,29,27,25,24,18,17,16,15,14,11,10,9,8,7,6,5,1},
						{79,77,75,73,71,70,68,67,66,64,63,62,61,60,59,57,55,53,52,51,50,48,45,44,43,41,38,36,35,34,33,31,30,28,26,25,19,18,17,16,15,12,11,10,9,8,7,6,2},
						{78,76,74,72,71,69,68,67,65,64,63,62,61,60,58,56,54,53,52,51,49,46,45,44,42,39,37,36,35,34,32,31,29,27,26,20,19,18,17,16,13,12,11,10,9,8,7,3},
						{79,77,76,75,73,70,69,66,65,62,61,60,58,56,55,52,48,45,43,42,41,40,39,38,35,34,29,28,23,21,20,17,16,15,11,8,4,3,0},
						{78,77,76,74,71,70,67,66,63,62,61,59,57,56,53,49,46,44,43,42,41,40,39,36,35,30,29,24,22,21,18,17,16,12,9,5,4,1},
						{79,78,77,75,72,71,68,67,64,63,62,60,58,57,54,50,47,45,44,43,42,41,40,37,36,31,30,25,23,22,19,18,17,13,10,6,5,2},
						{79,78,73,69,65,61,60,57,56,55,54,53,51,50,47,45,44,43,39,38,36,34,33,31,30,29,27,26,24,20,16,15,13,12,11,10,9,7,6,0},
						{79,74,70,66,62,61,58,57,56,55,54,52,51,48,46,45,44,40,39,37,35,34,32,31,30,28,27,25,21,17,16,14,13,12,11,10,8,7,1},
						{75,71,67,63,62,59,58,57,56,55,53,52,49,47,46,45,41,40,38,36,35,33,32,31,29,28,26,22,18,17,15,14,13,12,11,9,8,2}
	};

	private static Integer[][] CRC1 = new Integer[][]{
						{78,76,75,72,70,66,64,60,59,58,57,49,48,47,46,45,43,40,37,36,35,34,33,32,31,30,28,26,24,22,21,20,16,15,14,11,7,6,5,3,2,1,0},
						{79,78,77,75,73,72,71,70,67,66,65,64,61,57,50,45,44,43,41,40,38,30,29,28,27,26,25,24,23,20,17,14,12,11,8,5,4,0},
						{79,75,74,73,71,70,68,67,65,64,62,60,59,57,51,49,48,47,44,43,42,41,40,39,37,36,35,34,33,32,29,27,25,22,20,18,16,14,13,12,11,9,7,3,2,0},
						{78,74,71,70,69,68,65,64,63,61,59,57,52,50,47,46,44,42,41,38,32,31,24,23,22,20,19,17,16,13,12,11,10,8,7,6,5,4,2,0},
						{79,75,72,71,70,69,66,65,64,62,60,58,53,51,48,47,45,43,42,39,33,32,25,24,23,21,20,18,17,14,13,12,11,9,8,7,6,5,3,1},
						{78,75,73,71,67,65,64,63,61,60,58,57,54,52,47,45,44,37,36,35,32,31,30,28,25,20,19,18,16,13,12,11,10,9,8,5,4,3,1,0},
						{79,76,74,72,68,66,65,64,62,61,59,58,55,53,48,46,45,38,37,36,33,32,31,29,26,21,20,19,17,14,13,12,11,10,9,6,5,4,2,1},
						{77,75,73,69,67,66,65,63,62,60,59,56,54,49,47,46,39,38,37,34,33,32,30,27,22,21,20,18,15,14,13,12,11,10,7,6,5,3,2},
						{78,76,74,70,68,67,66,64,63,61,60,57,55,50,48,47,40,39,38,35,34,33,31,28,23,22,21,19,16,15,14,13,12,11,8,7,6,4,3},
						{79,78,77,76,72,71,70,69,68,67,66,65,62,61,60,59,57,56,51,47,46,45,43,41,39,37,33,31,30,29,28,26,23,21,17,13,12,11,9,8,6,4,3,2,1,0},
						{79,78,77,73,72,71,70,69,68,67,66,63,62,61,60,58,57,52,48,47,46,44,42,40,38,34,32,31,30,29,27,24,22,18,14,13,12,10,9,7,5,4,3,2,1},
						{79,76,75,74,73,71,69,68,67,66,63,62,61,60,57,53,46,41,40,39,37,36,34,26,25,24,23,22,21,20,19,16,13,10,8,7,4,1,0},
						{77,76,75,74,72,70,69,68,67,64,63,62,61,58,54,47,42,41,40,38,37,35,27,26,25,24,23,22,21,20,17,14,11,9,8,5,2,1},
						{78,77,76,75,73,71,70,69,68,65,64,63,62,59,55,48,43,42,41,39,38,36,28,27,26,25,24,23,22,21,18,15,12,10,9,6,3,2},
						{79,77,75,74,71,69,65,63,59,58,57,56,48,47,46,45,44,42,39,36,35,34,33,32,31,30,29,27,25,23,21,20,19,15,14,13,10,6,5,4,2,1,0}
	};
	
	private static Integer[][] getCRCEquation(int hway) {
		switch(hway) {
			case 0: return CRC0;
			case 1: return CRC1;
			default: throw new RuntimeException("hway is not valid");
		}
	}
	
	public static BitSet getCRCResult(BitSet key, int hway, int crclen) {
		if(key.length() > 80)
			throw new RuntimeException("Key length cannot be greater than 80");
		
		Integer[][] crc = getCRCEquation(hway);
		BitSet result = new BitSet(crclen);
		for(int i=0; i<crclen; i++) {
			boolean bitresult = key.get(crc[i][0]);
			for(int j=1; j<crc[i].length; j++) 
				bitresult ^= key.get(crc[i][j]);
			if(bitresult)
				result.set(i);
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String k = "c7f8350e98c8d041ca24";
		BitSet kb = new BitSet(80);
		int index=0;
		for(int i=k.length()-1; i>=0; i--) {
			BitSet bs = Utils.stringToBitSet(k.substring(i, i+1), 4, 16, false);
			for(int j=0; j<4; j++) {
				if(bs.get(j))
					kb.set(index);
				index++;
			}
		}
		BitSet crc = getCRCResult(kb, 0, 15);
		BitSet mask = new BitSet(15);
		mask.set(0, 11);
		System.out.println("crc : " + Utils.bitSetToHex(crc));
		System.out.println("msk : " + Utils.bitSetToHex(mask));
		crc.and(mask);
		System.out.println("crc after mask : " + Utils.bitSetToHex(crc));
		index = (int) Utils.bitSetToLong(crc);
		System.out.println("index : " + index);
		System.out.println("sram : " + (index/1024));
		System.out.println("offset : " + (index%1024));
		crc = getCRCResult(kb, 1, 15);
		System.out.println("crc : " + Utils.bitSetToHex(crc));
		System.out.println("msk : " + Utils.bitSetToHex(mask));
		crc.and(mask);
		System.out.println("crc after mask : " + Utils.bitSetToHex(crc));
		index = (int) Utils.bitSetToLong(crc);
		System.out.println("index : " + index);
		System.out.println("sram : " + (index/1024));
		System.out.println("offset : " + (index%1024));
	}
}
