package com.p4.quadpeaks.extractor;

import com.p4.utils.Summarizable;

import java.util.List;

public interface ExNode extends Summarizable{
    List<ExNode> getMembers();
    boolean isUnion();
    boolean isLeaf();
    int getSize();
    String getName();
    void calcOffset(int off, MemoryMap m);
}
