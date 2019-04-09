package com.p4.quadpeaks.extractor;

import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Data
public class ExIntNode implements ExNode {
    private final String name;
    private final List<ExNode> members = new ArrayList<>();
    private final boolean union;
    private int offset = 0;

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public int getSize() {
        if(union){
            return members.stream()
                    .max(Comparator.comparingInt(ExNode::getSize))
                    .get().getSize();
        }else{
            int sum = 0;
            for(ExNode m:members){
                sum+=m.getSize();
            }
            return sum;
        }
    }

    @Override
    public void calcOffset(int off, MemoryMap mm) {
        this.offset = off;
        if(!union){
            int runningOffset = off;
            for(ExNode m:members){
                m.calcOffset(runningOffset,mm);
                runningOffset+=m.getSize();
            }
        }else{
            for(ExNode m:members){
                m.calcOffset(off,mm);
            }
        }
    }

    @Override
    public String summary() {
        return name+", "+ getSize()+":\n" + Utils.summary("\n", members);
    }
}
