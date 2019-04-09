package com.p4.pd.config.common;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {

    public static Bitset concat(int sizeOfEach,List<Bitset> bitsets) {
        String s = "";
        for(Bitset b : bitsets) {
            s = b.toString()+s;
        }
        Bitset ret = new Bitset(bitsets.size()*sizeOfEach,s);
        ret.alignToSize();
        return ret;
    }

    public static List<Bitset> get32SizeBitsets(Bitset in){
        String binaryString = in.toString();
        List<Bitset> ret = new ArrayList<Bitset>();
        for(int i=0;i<in.getSize();i+=32) {
            if(i+32 > in.getSize())
                ret.add(new Bitset(32,binaryString.substring(i)));
            else
                ret.add(new Bitset(32,binaryString.substring(i, i+32)));
        }
        return ret;
    }

}
