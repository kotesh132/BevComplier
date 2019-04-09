package com.p4.drmt.alu;

import com.p4.drmt.struct.IALUOperation;
import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CAction implements IAction {
    private final String name;
    private final List<IField> parameters = new ArrayList<>();
    private final List<IALUOperation> instructions = new ArrayList<>();

    @Override
    public String summary() {
        return name;
    }
}
