package com.p4.drmt.parser;

import com.p4.utils.Utils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

@Data
public class OutTransitions {
    private final String sourceNode;
    private final Map<String, Integer> destNodes; // Destination Node to DOFF value map. NOT TO BE CONFUSED WITH POSITIONS
    private final OutGoing outgoing;

    public static Map<String, OutTransitions> getMap(Map<String, Map<String, Integer>> outsDetailed, Map<String, OutGoing> outGoingMap){
        Map<String, OutTransitions> ret = new LinkedHashMap<>();
        for(Map.Entry<String, Map<String, Integer>> entry: outsDetailed.entrySet()){
            ret.put(entry.getKey(), new OutTransitions(entry.getKey(), entry.getValue(), outGoingMap.get(entry.getKey())));
        }
        return ret;
    }

    public int indexOfDest(String destNode){
        if(destNodes.containsKey(destNode)){
            return outgoing.getPosOfOffset(destNodes.get(destNode));
        }
        throw new UnsupportedOperationException();
    }
}
