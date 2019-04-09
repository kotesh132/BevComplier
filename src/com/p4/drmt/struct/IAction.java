package com.p4.drmt.struct;

import com.p4.utils.Summarizable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IAction extends Summarizable{
    public String getName();
    public List<IField> getParameters();
    public List<IALUOperation> getInstructions();

    //TODO This is currently assuming all the action steps can be executed in one go. ie. No dependent operation
    //TODO We should Ideally use a DFG for each action.
    default public int getDelay(){
        if(getInstructions().size()>0)
            return getInstructions().get(0).getActionDelay();
        else
            return 0;
    }

    default public Map<IALUType, Integer> getALUTyes(){
        Map<IALUType,Integer> ret = new HashMap<>();
        for(IALUOperation operation:getInstructions()){
            IALUType type = operation.getDefaultALUtype();
            if(ret.get(type)==null){
                ret.put(type,1);
            }else{
                int count = ret.get(type);
                ret.put(type, count+1);
            }
        }
        return ret;
    }
}
