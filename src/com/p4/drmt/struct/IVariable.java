package com.p4.drmt.struct;

import com.p4.utils.Summarizable;

public interface IVariable extends Summarizable{
    public boolean intersects(IVariable other);
}
