package com.p4.quadpeaks.extractor;

import lombok.Data;

import java.util.List;
@Data
public class ExLeaf implements ExNode {
    private final String name;
    private final int size;
    private final String source_header;
    private final String header_field;
    private int offset = 0;

    public String getFullName(){
        return "hdr."+header_field;
    }

    @Override
    public List<ExNode> getMembers() {
        return null;
    }

    @Override
    public boolean isUnion() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public void calcOffset(int off, MemoryMap m) {
        this.offset = off;
        m.add(getFullName(), this.offset);
    }

    @Override
    public String summary() {
        return name;
    }
}
