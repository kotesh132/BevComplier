package com.p4.drmt.alu;

import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.ITable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CTable implements ITable{
    private final String name;
    private final int tableId;
    private final List<IField> keyFields = new ArrayList<>();
    private final List<IAction> actions = new ArrayList<>();

    @Override
    public String summary() {
        return name;
    }


}
