package com.p4.drmt.schd;

import java.util.ArrayList;
import java.util.List;

public class ScheduleUtils {

    public static <E> List<List<E>> segregateMod(List<E> items, int mod){
        List<List<E>> ret = new ArrayList<>();
        for(int i=0; i<mod; i++){
            ret.add(new ArrayList<>());
        }
        for(int i=0; i< items.size();i++){
            ret.get(i%mod).add(items.get(i));
        }
        return ret;
    }
}
