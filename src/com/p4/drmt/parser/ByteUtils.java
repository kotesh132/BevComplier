package com.p4.drmt.parser;

import com.p4.drmt.parser.StateMeta.DM;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteUtils {

	private static final Logger L = LoggerFactory.getLogger(ByteUtils.class);
	public static List<DM> getDataMaskValid(String val){
		List<DM> ret = new ArrayList<>();
		//System.out.println(val);
		FW num = parseP4Number(val);
		List<FW> nums = num.byteOrder();
		List<FW> masks = maskbytes(nums);
		for(int i=0; i<nums.size();i++){
			ret.add(new DM(nums.get(i).byteAlign(), masks.get(i), FW.ONE));
		}
		Collections.reverse(ret);
		//System.out.println("masks="+Utils.summary(ret));
		return ret;
	}
	
	public static Integer[] calcByteParts(int s) {
		int size = 
		Utils.ceil(s, 8);
		Integer[] parts = new Integer[size];
		for(int i = size-1;i>=0;i--){
			if(s<8){
				parts[i] = s;
				s = 0;
			}else{
				parts[i] = 8;
				s-=8;
			}
		}
		return parts;
	}

	public static Integer[] calcBytePartsBE(int s) {
		int size =
				Utils.ceil(s, 8);
		Integer[] parts = new Integer[size];
		for(int i = 0;i<s;i++){
			if(s<8){
				parts[i] = s;
				s = 0;
			}else{
				parts[i] = 8;
				s-=8;
			}
		}
		return parts;
	}

	
	public static List<FW> maskbytes(List<FW> parts){
		List<FW> ret = new ArrayList<>();
		for(FW num:parts){
			
			ret.add(maskByte(num));
		}
		return ret;
	}
	
	public static FW maskByte(FW num) {
		if(num.getSize()>8){
			throw new IllegalArgumentException("Can't calculate mask Byte for "+num.summary()+ " as size is >8");
		}
		int maskbits = 8-num.getSize();
		int mask;
		if(maskbits==0){
			mask = 0;
		}else{
			mask = ((1<<maskbits)-1)<<num.getSize();
		}
		return new FW(mask, 8);
	}

	public static FW parseP4Number(String number){
		String real = "(([0-9]+)[ws])?([0-9_]+)";
		String hex = "(([0-9]+)[ws])?0[xX]([0-9a-fA-F_]+)";
		if(number.matches(real)){
			Pattern p = Pattern.compile(real);
			Matcher m = p.matcher(number);
			if(m.find()){
				if(m.group(1) == null){
					return new FW(Integer.parseInt(m.group(3)), 31);
				}else{
					return new FW(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(2)));
				}
			}
		}else if(number.matches(hex)){
			Pattern p = Pattern.compile(hex);
			Matcher m = p.matcher(number);
			if(m.find()){
				if(m.group(1) == null){
					return new FW(Integer.parseInt(m.group(3),16), 32);
				}else{
					return new FW(Integer.parseInt(m.group(3),16), Integer.parseInt(m.group(2)));
				}
			}
		}
		throw new NumberFormatException("Unrecognized p4 numeral "+number);
	}
	
	public static List<Integer> order(int size, int max, int start){
		List<Integer> ret = new ArrayList<>();
		if(size>max){
			throw new IllegalArgumentException("Size > max");
		}
		for(int i=0;i<size;i++){
			ret.add(i+start);
		}
		Collections.reverse(ret);
		return ret;		
	}
	
	public static List<FW> mapToFixedWidth(List<Integer> l, int size){
		if(size>FW.maxSize){
			throw new IllegalArgumentException("size is too big");
		}
		List<FW> ret = new ArrayList<>();
		for(Integer i:l){
			if(((1<<size+1)-1)<i){//TODO: OVerflow
				throw new IllegalArgumentException(i+" can't be fit in "+size);
			}
			ret.add(new FW(i, size));
		}
		return ret;
	}
	
	public static<T> List<T> padMsbDefault (List<T> input, int max, T def){
		List<T> ret = new ArrayList<>();
		if(max>input.size()){
			ret.addAll(repeat(def, max-input.size()));
		}else if(max<input.size()){
			throw new IllegalStateException("Can't pad as size > maxSize");
		}
		ret.addAll(input);
		return ret;
	}
	
	public static<T> List<T> padLsbDefault (List<T> input, int max, T def){
		List<T> ret = new ArrayList<>();
		ret.addAll(input);
		if(max>input.size()){
			ret.addAll(repeat(def, max-input.size()));
		}else if(max<input.size()){
			throw new IllegalStateException("Can't pad as size > maxSize");
		}
		return ret;
	}
	
	public static<T> List<T> repeat(T val, int times){
		List<T> ret = new ArrayList<>();
		if(times>0){
			for(int i=0;i<times;i++){
				ret.add(val);
			}
		}
		return ret;
	}

	public static FW createMsbMask(int size, int startPos, int endPos){
		assert (startPos<=size && endPos< size && endPos<startPos);
		int ret = 0;
		for(int i=endPos;i<=startPos; i++){
			ret|=(1<<i);
		}
		return new FW(ret, size);
	}

	public static int nBit2sComplement(int size, int num){
		assert(size<32);
		num = -num;
		int mask = (1<<size) - 1;
		int ret = num & mask;
		FW r = new FW(ret, size);
		//System.out.println(r.toBinary());
		return num & mask;
	}

	public static void main(String[] args) {
		boolean[] b = {false, false, false, false, false, true, false, false};
		FW b1 = FW.getMofBE(b);
		System.out.println(b1.toBinary());
		System.out.println(b1.summary());
		System.out.println(b1.getValue());

	}
}
