package com.p4.drmt.struct;

import com.p4.utils.Summarizable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public interface IMatchNode extends MANode, Summarizable{
    public ITable getTable();
    public IField getPredicate();
    public int getMatchDelay();

    void setOffset(int offset);
    int getOffset();

    default public int getKeyWidth(){
        int c = 0;
        for(IField keyField: getTable().getKeyFields()){
            c+=keyField.getSize();
        }
        return c;
    }

    default public int getNumFields(){
        int c = 0;
        for(IAction action: getTable().getActions()){
            if(action.getInstructions().size()>c){
                c = action.getInstructions().size();
            }
        }
        return c;
    }


    default public Set<IVariable> getProducers(){
        Set<IVariable> ret = new LinkedHashSet<>();
        for(IAction action: getTable().getActions()){
            for(IALUOperation op: action.getInstructions()){
                ret.add(op.getLhs());
            }
        }
        return ret;
    }

    default public Set<IVariable> getConsumers(){
        Set<IVariable> ret = new LinkedHashSet<>();
        ret.add(getPredicate());
        ret.addAll(getTable().getKeyFields());
        return ret;
    }

    @Override
    default Map<IALUType, Integer> getALUs() {
        Map<IALUType,Integer> ret = new HashMap<>();
        for(IAction action:getTable().getActions()){
            Map<IALUType,Integer> actionmap = action.getALUTyes();
            for(Map.Entry<IALUType,Integer> entry:actionmap.entrySet()){
                if(ret.get(entry.getKey())==null){
                    ret.put(entry.getKey(), entry.getValue());
                }else{
                    if(ret.get(entry.getKey()) < entry.getValue()){
                        ret.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return ret;
    }
}
