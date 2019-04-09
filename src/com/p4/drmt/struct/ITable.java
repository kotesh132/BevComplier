package com.p4.drmt.struct;

import com.p4.utils.Summarizable;

import java.util.List;

public interface ITable extends Summarizable{
    public String getName();
    public List<IField> getKeyFields();
    public List<IAction> getActions();
    public int getTableId();
}
