package com.p4.p416.applications;

import java.util.List;
import java.util.Map;

public interface AluMapEntry {
		public List<? extends AluInstruction> getAluInstructions();
		public int getInstructionIndex();
		public int getActionIndex();
		public Map<AluInstruction,Integer> getCpus();
		public  int getTableId();
		public int getInstEn();
}
