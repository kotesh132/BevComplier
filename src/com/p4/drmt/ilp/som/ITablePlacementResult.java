package com.p4.drmt.ilp.som;


import com.p4.drmt.ilp.som.SomSpec.SomStore;

import java.util.List;

public interface ITablePlacementResult {



    List<SomStore> getSomStores();

    void printSolution();

    boolean containsSolution();
}
