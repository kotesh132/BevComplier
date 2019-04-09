package com.p4.drmt.parser;

import com.p4.utils.Utils;
import com.p4.utils.Utils.fn1;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExtractorAtom {

	private final FW y;
	private final FW x;
	private final FW c;
	private final FW v;
	
	public static ExtractorAtom EDEF = new ExtractorAtom(FW.ZERO_BYTES, 
			FW.ZERO_BYTES, 
			FW.ZERO, 
			FW.ZERO);
	
	
	public static List<ExtractorAtom> getAtoms(List<FW> y, List<FW> x, boolean c){
		List<ExtractorAtom> ret = new ArrayList<>();
		if(y.size()!=x.size()){
			throw new RuntimeException("Size mismatch");
		}
		for(int i=0; i<y.size(); i++){
			ret.add(new ExtractorAtom(y.get(i), x.get(i), c?FW.ONE:FW.ZERO, FW.ONE));
		}
		return ret;
	}
	
	public static List<FW> onlyY(List<ExtractorAtom> input){
		return Utils.map(new fn1<FW, ExtractorAtom>() {
			@Override
			public FW invoke(ExtractorAtom p1) {
				return p1.y;
			}
		}, input);
	}
	
	public static List<FW> onlyX(List<ExtractorAtom> input){
		return Utils.map(new fn1<FW, ExtractorAtom>() {
			@Override
			public FW invoke(ExtractorAtom p1) {
				return p1.x;
			}
		}, input);
	}
	
	public static List<FW> onlyC(List<ExtractorAtom> input){
		return Utils.map(new fn1<FW, ExtractorAtom>() {
			@Override
			public FW invoke(ExtractorAtom p1) {
				return p1.c;
			}
		}, input);
	}
	
	public static List<FW> onlyV(List<ExtractorAtom> input){
		return Utils.map(new fn1<FW, ExtractorAtom>() {
			@Override
			public FW invoke(ExtractorAtom p1) {
				return p1.v;
			}
		}, input);
	}
}
