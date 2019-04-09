package com.p4.drmt.schd;

public class NOOPTimeQuanta implements TimeQuanta{
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isMatchType() {
        return false;
    }

    @Override
    public String summary() {
        return "NOOP";
    }

    public static NOOPTimeQuanta NOOP = new NOOPTimeQuanta();
}
