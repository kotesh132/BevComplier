package com.p4.drmt.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.p4.drmt.alu.CField;
import com.p4.drmt.keygen.KeyGenUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.DumbSchedule;
import com.p4.drmt.parser.FW;
import com.p4.drmt.parser.KeyRow;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import lombok.Data;
import lombok.Getter;

import static com.p4.drmt.cfg.DRMTConstants.*;

public class KeyMeta implements Summarizable{

	private static final Logger L = LoggerFactory.getLogger(KeyMeta.class);
	@Getter Map<Integer, List<CField>> map = new LinkedHashMap<>();
	@Getter Map<Integer, String> tableMap = new LinkedHashMap<>();
	private static int bytePos = 0;

	public static Map<Integer, List<Integer>> schedule = DumbSchedule.keygenSchedule();

	@Override
	public String summary() {
		return Utils.summary(map)+"\n"+Utils.summary(tableMap)+"\n";
	}

	/*
	= new LinkedHashMap<>();
	static{
		schedule.put(0, Utils.arrList(2));
		schedule.put(1, Utils.arrList(3,1));
		
	}
	*/

	@Data
	private static class KEYSEG{
		private final FW tableID;
		private final List<FW> byteOffsets=new ArrayList<>();
		private final List<FW> bitOffsets=new ArrayList<>();
		private final FW mask;
	}

	private static void groupsSegments(List<FW> byteFields, List<FW> bitFields){
		int maxBytesPerSegment = 10;
		int maxBitsPerSegment = 16;

		List<Integer> byetesReservedforBitsegments = sizes(bitFields);
		int numbytes = byteFields.size();
		List<Integer> byteSegments = new ArrayList<>();




	}

	private static List<Integer> sizes(List<FW> bitFields) {
		List<Integer> ret = new ArrayList<>();
		int numBitsSegment = 0;
		if(bitFields.size()!=0){
			int numSegmentsforbits = Utils.ceil(bitFields.size(), 16);
			ret.addAll(ByteUtils.repeat(2, bitFields.size()/16));
			if(bitFields.size()%16!=0){
				ret.add(Utils.ceil(bitFields.size()%16,8));
			}
		}
		return ret;
	}

	@Data
	public static class AF implements Summarizable{
		private final int  startingAddress;
		private final int size;
		private final AbstractBaseExt key;
		@Override
		public String summary() {
			return "["+startingAddress+","+size+"]";
		}
		
		public String getKey(){
			return key.getFormattedText();
		}
		
	}
	
	public void addKeyMap(int tableId, CField key){
		if(!map.containsKey(tableId)){
			map.put(tableId, new ArrayList<CField>());
		}
		map.get(tableId).add(key);
	}
	
	public void addTableMap(int tableId, String tableName) {
		tableMap.put(tableId, tableName);
	}
	
	public int currentKeyId = Integer.MIN_VALUE;
	public String tableName = "";
	
	public static List<FW> getRow(List<CField> r,int maxSize){
		List<FW> ret = getNums(r);
		ret = ByteUtils.padMsbDefault(ret, maxSize, FW.ZERO_BYTES);
		return ret;
	}
	

	public static List<FW> getBitFields(List<CField> cfields){
		List<FW> ret = new ArrayList<>();
		List<CField> bitFields =  cfields.stream().filter(x -> x.getSize()==1).collect(Collectors.toList());
		for(CField bitField:bitFields){
			ret.add(new FW(bitField.getOffset(), DRMTConstants.BYTE));
		}
		return ret;
	}

	public static List<FW> getByteFields(List<CField> cfields){
		List<FW> ret = new ArrayList<>();
		List<CField> byteFields =  cfields.stream().filter(x -> x.getSize()!=1).collect(Collectors.toList());
		for(CField byteField:byteFields){
			if (byteField.getSize() % 8 != 0) {
				throw new UnsupportedOperationException("field is not in byte alignment. not yet supported");
			}
			int startingByteAddr = byteField.getOffset();
			for (int i = 0; i < byteField.getSize() / 8; i++) {
				ret.add(new FW(startingByteAddr + i, 8));
			}
		}
		return ret;
	}


	public static List<FW> getNums(List<CField> r){
		List<FW> ret = new ArrayList<>();

		List<CField> bitFields =  r.stream().filter(x -> x.getSize()==1).collect(Collectors.toList());
		if(bitFields.size()>0 && bitFields.size()<=DRMTConstants.numkbit){
			bytePos = bitFields.size()/DRMTConstants.BYTE+1;
		}

		List<CField> byteFileds = r.stream().filter(x -> x.getSize()!=1).collect(Collectors.toList());
		for(CField af:r){
			if(af.getSize()==1){

			}else {
				if (af.getSize() % 8 != 0) {
					throw new UnsupportedOperationException("field is not in byte alignment. not yet supported");
				}
				int startingByteAddr = af.getOffset();

				for (int i = 0; i < af.getSize() / 8; i++) {
					ret.add(new FW(startingByteAddr + i, 8));
				}
			}
		}
		Collections.reverse(ret);
		return ret;
	}
	
	public List<KeyRow> getRowsWithSchedule(){
		L.debug(Utils.summary(map));
		List<KeyRow> ret = new ArrayList<>();
		for(Entry<Integer, List<Integer>> e: schedule.entrySet()){
			KeyRow k = new KeyRow();
			List<FW> ybyt = new ArrayList<>();
			List<FW> ktbl = new ArrayList<>();
			List<FW> kvld = new ArrayList<>();
			List<FW> kseg = new ArrayList<>();
 			List<FW> kmap_vld = new ArrayList<>();
			List<FW> ktbl_map = new ArrayList<>();
			List<FW> kseg_map = new ArrayList<>();
			if(e.getValue().size()>numkseg){
				throw new RuntimeException("Unable to match schedule");
			}
			for(int tbl: e.getValue()){
				List<CField> afs = map.get(tbl);
				ybyt.addAll(getRow(afs, maxBytes));
				ktbl.add(new FW(tbl, 8));
				ktbl_map.add(new FW(tbl, 8));
				kvld.add(FW.ONE);
				kmap_vld.add(FW.ONE);
			}
			ybyt = ByteUtils.padMsbDefault(ybyt, numkseg*maxBytes, FW.ZERO_BYTES);
			ktbl = ByteUtils.padMsbDefault(ktbl, numkseg, FW.ZERO_BYTES);
			kseg = ByteUtils.repeat(new FW(0,2), numkseg );

			kvld = ByteUtils.padMsbDefault(kvld, numkseg, FW.ZERO);

			k.ybyt = ybyt;
			k.ktbl = ktbl;
			k.kseg = kseg;
			k.kvld = kvld;
			k.ybit1 = ByteUtils.repeat(FW.ZERO_BYTES, numkbit*numkseg);
			k.ymsk1 = ByteUtils.repeat(FW.ZERO, numkbit*numkseg);
			//k.kmap_vld = kmap_vld;
			//k.ktbl_map = ktbl_map;
			//k.kseg_map = kseg_map;
			ret.add(k);
		}
		return ret;
	}
	
	public void getKeysSchedule(Map<Integer, List<Integer>> sch){
		for(Entry<Integer, List<Integer>> e: schedule.entrySet()){
			KeyGenUnit ku = new KeyGenUnit();
			ku.padAll();
			int segment = 0;
			for(int tbl: e.getValue()){
				List<CField> afs = map.get(tbl);
				List<FW> bitFields = getBitFields(afs);
				if(bitFields.size()>8){
					throw new IllegalStateException("Can't have more than 8 bit fields in Key definition");
				}
				List<FW> byteFields = getByteFields(afs);
				int index = 0;
				int numsegs = bitFields.size()>0? Utils.ceil(byteFields.size()+1, maxBytes): Utils.ceil(byteFields.size()+1, maxBytes);

				for(int i= 0; i<numsegs;i++){
					//VALIDS
					ku.getKvld().addItem(1,i);
					//TABLE ID
					ku.getKtbl().addItem(tbl, i);
				}
				if(bitFields.size()>0){
					index = 1;
					//ADD all bit fields
					int count = 0;
					for(FW b: bitFields){
						ku.getYbit().addItem(b, segment+(count++));
					}
					//ADD the mask
					int mask = (1<<bitFields.size()) - 1;
					ku.getYmsk().addItem(mask, segment);

				}
				//YBYT
				int s = segment;
				for (FW f: byteFields){
					if(index > maxBytes){
						index = 0;
						s++;
					}
					int idx = maxBytes*s+index;
					ku.getYbyt().addItem(f, idx);
				}
				segment += numsegs;
			}
			ku.emitAll(KeyGenUnit.dirName);
		}
	}
	
	
}
