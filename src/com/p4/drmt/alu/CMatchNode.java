package com.p4.drmt.alu;

import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IMatchNode;
import com.p4.drmt.struct.ITable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CMatchNode implements IMatchNode {
    private final ITable table;
    private final IField predicate;
    private final int matchDelay;

    @Getter
    @Setter
    private int offset;

    @Override
    public int getNodeDelay() {
        int maxActionDelay = 0;
        for(IAction iAction:table.getActions()){
            if(iAction.getDelay()>maxActionDelay)
                maxActionDelay = iAction.getDelay();
        }
        return matchDelay+maxActionDelay;
    }

    @Override
    public String getId() {
        return table.summary();
    }

    @Override
    public String summary() {
        return "MATCH NODE: "+table.summary();
    }
}
