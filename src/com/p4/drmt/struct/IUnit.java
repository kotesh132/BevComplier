package com.p4.drmt.struct;

import com.p4.utils.Summarizable;

import java.util.Set;

public interface IUnit extends Summarizable {
    public Set<IVariable> getProducers();
    public Set<IVariable> getConsumers();
}
