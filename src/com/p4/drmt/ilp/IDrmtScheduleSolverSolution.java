package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.ds.ListMap;

import java.util.Map;

public interface IDrmtScheduleSolverSolution {

    ListMap<Integer, IUnitDAGVertex> getModuloSchedule();

    ListMap<Integer, IUnitDAGVertex> getSchedule();

    ListMap<Integer, IUnitDAGVertex> getDisjointMatches();

    Map<Integer, Integer> getModuloMatchUnitsConsumed();

    Map<Integer, Integer> getModuloActionFieldsConsumed();

    Map<IUnitDAGVertex, ALUType> getActionVertexToAluUseMap();

}
