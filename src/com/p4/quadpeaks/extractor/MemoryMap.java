package com.p4.quadpeaks.extractor;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import java.util.LinkedHashMap;

public class MemoryMap implements Summarizable{
    private final LinkedHashMap<String,Integer> offsets = new LinkedHashMap<>();
    public boolean containsField(String name){
        return offsets.containsKey(name);
    }
    public void add(String name, int offset){
        offsets.put(name,offset);
    }
    public int getOffset(String name){
        return offsets.get(name);
    }


    @Override
    public String summary() {
        return Utils.summary("\n", offsets);
    }
}
