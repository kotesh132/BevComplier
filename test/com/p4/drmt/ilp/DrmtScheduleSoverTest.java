package com.p4.drmt.ilp;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.p4.ids.IDAGEdge;
import com.p4.ids.IDAGGraph;
import com.p4.utils.Utils.Pair;

public class DrmtScheduleSoverTest {
	
	protected IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph;
	protected InputSpec inputSpec;
	protected int period = 0;
	protected int Q_MAX = 0;

	@Before
	public void setup() {
		setUpSolve();
	}

	protected void setUpSolve() {
		DAGBuilder dagBuilder = new DAGBuilder();
		inputSpec = new InputSpec(32, 4, 80, 8, 80, 1, 1, 22, 2, 0, false, 4, 16, 4);
		dagGraph = (IDAGGraph<IUnitDAGVertex, IDAGEdge>) dagBuilder.getSampleDAG(inputSpec);

		period = inputSpec.getNumOfProcessors() * inputSpec.getPacketRate();
		Pair<Integer, List<IUnitDAGVertex>> criticalPath = dagGraph.getCriticalPath();
		Q_MAX = (int) Math.ceil((1.5 * criticalPath.first()) / period);
	}


	@Test
	public void DrmtScheduleSolverTest() {
		DrmtScheduleSolver solver = new DrmtScheduleSolver(dagGraph, inputSpec, period, Q_MAX);

		if(solver.solve()) {
			solver.printSolution();
		} else {
			Assert.fail("Scheduler problem could not be solved with ILP");
		}

	}
}
