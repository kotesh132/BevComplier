package com.p4.drmt.schd;

import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IMatchNode;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class MatchQuanta implements TimeQuanta{
    private final List<IMatchNode> matchNodes = new ArrayList<>();
    private final boolean action;
    @Override
    public boolean isMatchType() {
        return true;
    }

    public List<Integer> tableIds(){
        List<Integer> ret = new ArrayList<>();
        for(IMatchNode m:matchNodes){
            ret.add(m.getTable().getTableId());
        }
        return ret;
    }

    public Map<Integer, List<Integer>> aluAssigns(){
        Map<Integer, List<Integer>> ret = new LinkedHashMap<>(); //table_id => {alus}
        int count = 0;
        for(IMatchNode m:matchNodes){
            ret.put(m.getTable().getTableId(), new ArrayList<>());
            for(IAction action :m.getTable().getActions()){
                for(IALUOperation ialuOperation:action.getInstructions())
                    ret.get(m.getTable().getTableId()).add(count++);
            }
        }
        return ret;
    }

    @Override
    public int size() {
        if(!action)
            return matchNodes.size();
        else{
            int size = 0;
            for(IMatchNode m: matchNodes){
                for(IAction a:m.getTable().getActions()){
                 size+=a.getInstructions().size();
                }
            }
            return size;
        }
    }

    public MatchQuanta getActionOf(){
        MatchQuanta m = new MatchQuanta(true);
        m.matchNodes.addAll(this.matchNodes);
        return m;
    }

    public List<MatchQuanta> split(int maxSize) {
        List<MatchQuanta> ret = new ArrayList<>();
        int count = 0;
        MatchQuanta m = new MatchQuanta(action);
        for(IMatchNode mn: matchNodes){
            m.matchNodes.add(mn);
            count++;
            if(count==maxSize){
                count = 0;
                ret.add(m);
                m = new MatchQuanta(action);
            }
        }
        if(m.matchNodes.size()>0){
            ret.add(m);
        }
        return ret;
    }

    @Override
    public String summary() {
        String prefix = isAction()? "A:":"M:";
        return prefix+ Utils.summary(tableIds());
    }
}
