package com.p4.drmt.parser;

import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Data
public class DeparserHint implements Summarizable{
    private final Map<P4State, List<Integer>> doffs = new LinkedHashMap<>();
    private final Map<P4State, Integer> position = new LinkedHashMap<>();

    @Override
    public String summary() {
        return Utils.summary("\n", doffs)+"\n"+Utils.summary(position);
    }
}
