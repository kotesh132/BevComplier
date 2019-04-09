package com.p4.p416.ex;

import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Data
public class PathHops {
    private final List<String> path;
    private final Map<String, Integer> map;

    public List<String> compressPath(int maxSize){
        int i=0;
        List<String> ret = new ArrayList<>();
        ret.add(path.get(i));
        i++;
        while(i<path.size()){
            int count = 0;
            List<String> nextHop = new ArrayList<>();
            if(getkeyHop(path.get(i))>maxSize){
                throw new IllegalStateException(""+path.get(i));
            }
            while(count<maxSize && i < path.size()){
                int s = getkeyHop(path.get(i));
                if(s+count<=maxSize){
                    count+=s;
                    nextHop.add(path.get(i));
                    i++;
                }else{
                    break ;
                }

            }
            ret.add(Utils.join(",", nextHop));
        }
        //System.out.println("Compressed original size of "+ path.size() +" to "+ ret.size());
        return ret;
    }

    private int getkeyHop(String s) {
        if(map.containsKey(s))
            return Utils.ceil(map.get(s), 8);
        return 0;
    }
    public static void addToMap(Map<Integer, Integer> map, int val){
        if(!map.containsKey(val)){
            map.put(val, 0);
        }
        map.put(val, map.get(val)+1);
    }
}
