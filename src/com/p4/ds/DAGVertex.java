package com.p4.ds;

import com.p4.ids.IDAGVertex;
import lombok.Getter;


public class DAGVertex implements IDAGVertex {


    @Getter
    private String name;
    @Getter
    private int id;

    DAGVertex(String name, int id) {
        this.name = name;
        this.id = id;
    }

}
