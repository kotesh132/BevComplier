package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import com.p4.utils.Utils.fn1;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
@Data
public class ExtractorLogic {

	private static final Logger L = LoggerFactory.getLogger(ExtractorLogic.class);
	public static final int BYTE = 8;
	
	@Data
	public static class SMD implements Summarizable{
		public final FW shift;
		public final FW mask;
		public final FW dir;
		@Override
		public String summary() {
			return Utils.summary(Utils.asList(shift.summary(),mask.toBinary(),dir.toBinary()));
		}
	}
	@Data
	public static class EXB implements Summarizable{
		public final FW ybyt;
		public final FW xbyt;
		public final FW vld;
		public final SMD smd;
		@Override
		public String summary() {
			
			return Utils.summary(Utils.asList(ybyt.summary(), xbyt.summary(), vld.summary(), smd.summary()));
		}
		
		public static List<FW> onlyYbyt(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.ybyt;
				}
			}, in);
		}
		
		public static List<FW> onlyXbyt(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.xbyt;
				}
			}, in);
		}
		
		public static List<FW> onlyVld(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.vld;
				}
			}, in);
		}
		
		public static List<FW> onlySft(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.smd.shift;
				}
			}, in);
		}
		
		public static List<FW> onlyDir(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.smd.dir;
				}
			}, in);
		}
		
		public static List<FW> onlyMask(List<EXB> in){
			return Utils.map(new fn1<FW, EXB>() {
				@Override
				public FW invoke(EXB p1) {
					return p1.smd.mask;
				}
			}, in);
		}
	}
	
	public static List<EXB> getSMDOffsets(int src, int dst, int size){
		List<EXB> extracts = new ArrayList<>();
		while(size>0){			
			int nextSrcBound = src+(BYTE-src%BYTE);
			int nextDstBound = dst+(BYTE-dst%BYTE);
			
			int bitsToextract = (nextDstBound-dst) <(nextSrcBound-src)?
						(nextDstBound-dst):
						(nextSrcBound-src);
			bitsToextract = bitsToextract<size?bitsToextract:size;
			int xByt = src/BYTE;
			int yByt = dst/BYTE;
			int xBitInByte = src-xByt*BYTE;
			int yBitInByte = dst-yByt*BYTE;
			extracts.add(new EXB(new FW(yByt,BYTE), new FW(xByt,BYTE), FW.ONE,getSMDforByte(xBitInByte, yBitInByte, bitsToextract)));
			src = src+bitsToextract;
			dst = dst+bitsToextract;
			size = size-bitsToextract;
		}
		return extracts;
	}
	
	public static SMD getSMDforByte(int srcOffset, int dstOffset, int size){
		assert(validate(srcOffset, dstOffset, size));
		FW dir = srcOffset - dstOffset >0? FW.ONE:FW.ZERO;
		FW shift = new FW(Math.abs(srcOffset - dstOffset), 3);
		int mask = 0;
		int numLeftOnes = dstOffset;
		int numMiddleZeros = size;
		int numRightOnes = BYTE-(numLeftOnes+numMiddleZeros);
		for(int i=0; i<BYTE; i++){
			if(numLeftOnes>0){
				mask = (mask>>1);
				numLeftOnes--;
			}else if(numMiddleZeros>0){
				mask = (mask>>1)|128;
				numMiddleZeros--;
			}else if(numRightOnes>0){
				mask = (mask>>1);
				numRightOnes--;
			}
		}
		return new SMD(shift, new FW(mask,BYTE),dir);
	}

	public static List<EXB> getSMDOffsetsBE(int src, int dst, int size, boolean isExtractor){
		List<EXB> extracts = new ArrayList<>();
		src+=size-1;
		while(size>0){
			int nextSrcBound = src-(src%BYTE);
			int nextDstBound = dst+(BYTE-dst%BYTE);

			int bitsToextract = (nextDstBound-dst) <(src-nextSrcBound+1)?
					(nextDstBound-dst):
					(src-nextSrcBound+1);
			bitsToextract = bitsToextract<size?bitsToextract:size;
			int xByt = src/BYTE;
			int yByt = dst/BYTE;
			int xBitInByte = (src-xByt*BYTE)-(bitsToextract-1);
			if(isExtractor)
				xBitInByte = BYTE - bitsToextract - xBitInByte;
			int yBitInByte = dst-yByt*BYTE;
			if(!isExtractor)
				yBitInByte = BYTE - bitsToextract -yBitInByte;
			extracts.add(new EXB(new FW(yByt,BYTE), new FW(xByt,BYTE), FW.ONE,getSMDforByteBE(xBitInByte, yBitInByte, bitsToextract)));
			src = src-bitsToextract;
			dst = dst+bitsToextract;
			size = size-bitsToextract;
		}
		return extracts;
	}

	public static SMD getSMDforByteBE(int srcOffset, int dstOffset, int size){
		assert(validate(srcOffset, dstOffset, size));
		FW dir = srcOffset - dstOffset >0? FW.ONE:FW.ZERO;
		FW shift = new FW(Math.abs(srcOffset - dstOffset), 3);
		int mask = 0;
		int numLeftOnes = dstOffset;
		int numMiddleZeros = size;
		int numRightOnes = BYTE-(numLeftOnes+numMiddleZeros);
		for(int i=0; i<BYTE; i++){
			if(numLeftOnes>0){
				mask = (mask>>1);
				numLeftOnes--;
			}else if(numMiddleZeros>0){
				mask = (mask>>1)|128;
				numMiddleZeros--;
			}else if(numRightOnes>0){
				mask = (mask>>1);
				numRightOnes--;
			}
		}
		return new SMD(shift, new FW(mask,BYTE),dir);
	}
	
	public static boolean validate(int srcOffset, int dstOffset, int size){
		if(size>BYTE) return false;
		if(dstOffset+size>BYTE) return false;
		return true;
	}
	
	
	public static void main(String[] args) {
		System.out.println(Utils.summary( getSMDOffsetsBE(4, 0, 4, true)));
	}
	
}
