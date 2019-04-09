package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class FW implements Summarizable {
	
    // memoizing powers of 2 till 2 ^ 64
    static final double[] powerOf2 = new double[65];
    static {
    	powerOf2[0] = 1;
        for (int i=1; i<powerOf2.length; i++) powerOf2[i] = 2*powerOf2[i-1];
    }

	public static FW ONE = new FW(1, 1);
	public static FW ONE_BYTES = new FW(1, 8);
	public static FW ZERO = new FW(0, 1);
	public static FW ZERO_BYTES = new FW(0, 8);
	public static FW ZERO_2BYTES = new FW(0, 16);
	public static FW FF_BYTES = new FW(255, 8);
	public static int maxSize = 32;
	private final int value;
	private final int size;
	
	public FW(int value, int size) {
		if(size<1){
			throw new IllegalStateException("Width needs to be non-zero");
		}
		if(value<0){
			throw new IllegalStateException("Value needs to be non-zero");
		}
//		if(value>(1<<size)-1){
//		using the momoized value of power of 2 instead of left shift as the shift is failing (for size >= 31) because of sign change)
		if(value>(powerOf2[size]-1)){
			throw new IllegalStateException("Size "+size +" is insufficient to capture value:"+value+", Needs to be less than or equal to : "+(powerOf2[size]-1));
		}
		this.value=value;
		this.size=size;
	}
	
	@Override
	public String summary() {
		//return "["+size+",0x"+Integer.toHexString(value)+"]";
		return toHSizeNibbles();
		//return ""+size+"d'"+value;
	}
	
	public List<FW> byteOrder(){
		return byteOrder(false);
	}
	
	public List<FW> byteOrder(boolean little){
		List<FW> ret = new ArrayList<>();
		Integer[] parts = ByteUtils.calcByteParts(size);
		List<Integer> list = Arrays.asList(parts);
		int temp = value;
		Collections.reverse(list);
		for(Integer i: list){
			ret.add(new FW(temp & 0xFF, i));
			temp = temp>>8;
		}
		if(little)
			Collections.reverse(ret);
		return ret;
	}

	public List<FW> byteOrderBE(){
		List<FW> ret = new ArrayList<>();
		Integer[] parts = ByteUtils.calcBytePartsBE(size);
		List<Integer> list = Arrays.asList(parts);
		int temp = value;
		//Collections.reverse(list);
		for(Integer i: list){
			int mask = (1<<i)-1;
			ret.add(new FW(temp & mask, i));
			temp = temp>>8;
		}
		//Collections.reverse(ret);
		return ret;
	}
	
	public FW byteAlign(){
		if(size>8)
			throw new IllegalStateException("size >8");
		return new FW(value, 8);
	}
	
	public String toNibbles(){
		int numNibbles = Utils.ceil(size, 4);
		StringBuilder sb = new StringBuilder();
		int v = value;
		for(int i=0;i<numNibbles;i++){
			int num = v & 0xf;
			sb.append(Integer.toHexString(num));
			v>>=4;
		}
		return sb.reverse().toString();
	}
	
	public String toBinary(){
		String s = Integer.toBinaryString(value);
		String s1 = Utils.repeat("0", size-s.length());
		return ""+size+"'b"+s1+s;
	}
	
	public String toHSizeNibbles(){
		return ""+size+"'h"+toNibbles();
	}
	
	public boolean isSet(int pos){
		if(pos>=size){
			throw new ArrayIndexOutOfBoundsException();
		}
		return (value & (1<<pos))!=0;
	}
	public boolean isSetBE(int pos){
		if(pos>=size){
			throw new ArrayIndexOutOfBoundsException();
		}
		List<FW> items = byteOrderBE();
		Collections.reverse(items);
		FW temp = concat(items);
		return temp.isSet(pos);
	}
	public static FW getVof(boolean[] b){
		int v=0;
		for(int i=0;i<b.length;i++){
			v+=b[i]?1<<i:0;
		}
		return new FW(v, b.length);
	}
	
	public static FW getMof(boolean[] b){
		boolean[] m = new boolean[b.length];
		
		for(int i=0;i<b.length;i++){
			m[i] = !b[i];
		}
		return getVof(m);
	}

	public static FW getMofBE(boolean[] b){
		boolean[] m = new boolean[b.length];

		for(int i=0;i<b.length;i++){
			m[i] = !b[i];
		}
		//REVERSE
		for(int i=0;i<m.length/2;i++){
			boolean temp = m[i];
			m[i] = m[m.length-1-i];
			m[m.length-1-i] = temp;
		}
		return getVof(m);
	}

	public static FW concat(FW w1, FW w2){
		if(w1.size+w2.size>32){
			throw new IllegalArgumentException("can't concatenate "+w1.summary()+" & "+w2.summary());
		}
		int num  = w1.value<<w2.size;
		num=num|w2.value;
		return new FW(num, w1.size+w2.size);
	}

	public static FW concat(List<FW> items){
		if(items.size()>0) {
			FW ret = items.get(0);
			for (int i = 1; i<items.size();i++){
				ret = concat(items.get(i), ret);
			}
			return ret;
		}
		throw new IllegalStateException("Can't concatenate items");
	}
}
