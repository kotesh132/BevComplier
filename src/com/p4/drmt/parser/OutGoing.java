package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.*;

@Data
public class OutGoing implements Summarizable{
    private final String name;
    private final List<Integer> doffIndices;
    private final List<Integer> doffPositions;

    public OutGoing(String name, List<Integer> doffIndices){
        this.name = name;
        this.doffIndices = doffIndices;
        this.doffPositions = positions(doffIndices);
    }

    public static Map<String, OutGoing> getMap(Map<String, Set<Integer>> outs ){
        Map<String, OutGoing> ret = new LinkedHashMap<>();
        for(Map.Entry<String, Set<Integer>> entry: outs.entrySet()){
            ret.put(entry.getKey(), new OutGoing(entry.getKey(), new ArrayList<>(entry.getValue())));
        }
        return ret;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("["+name+":");
        sb.append(Utils.summary(doffIndices)+",");
        sb.append(Utils.summary(doffPositions)+"]");
        return sb.toString();
    }

    public static List<Integer> positions(List<Integer> input){
        List<Integer> ret = new ArrayList<>();
        int count = 0;
        for(Integer ignored : input){
            ret.add(count++);
        }
        return ret;
    }

    public int getPosOfOffset(int doff) {
        int pos = doffIndices.indexOf(doff);
        return doffPositions.get(pos);
    }
}
