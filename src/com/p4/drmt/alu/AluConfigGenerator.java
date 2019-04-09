package com.p4.drmt.alu;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ActionDeclarationContextExt.AluMapEntryImpl;
import com.p4.p416.gen.AssignmentStatementContextExt;

public class AluConfigGenerator {
	
	private static final Logger L = LoggerFactory.getLogger(AluConfigGenerator.class);
	private  AluConfigGenerator(){
		actions = new ArrayList<>();
		AluComplex.init(); //fix the ConfigEmitUnit rather than patching here 
		//AluMap.init();
		
	}

	private static class SingletonHelper
	{
		private static final AluConfigGenerator INSTANCE = new AluConfigGenerator();
	}
	public static AluConfigGenerator getInstance(){
		return SingletonHelper.INSTANCE;
	}
	private List<Object> actions;;
	
	public void add(Object action){
		this.actions.add(action);
	}
	
	/*
	 * We Should not associate InstructionIndex  or the tableID to an ActionDeclaration, as 
	 *  more than one table can use that action and more than once the action can be scheduled
	 */
	public void add(int instructionIndex, int tableId,ActionDeclarationContextExt actionDeclarationContextExt, int actionIndex){
		AluMapEntryImpl mapEntry = (AluMapEntryImpl)actionDeclarationContextExt.getAluMapEntry(instructionIndex, actionIndex, tableId, 0);
		//mapEntry.setInstructionIndex(instructionIndex);
		//mapEntry.setTableId(tableId);
		L.debug("Adding at intstruction Ptr :"+ instructionIndex+", Action ID:"+actionDeclarationContextExt.getActionId());
		AluComplex.add(mapEntry);
		AluMap.add(mapEntry);
	}
	
	public void add(int instructionIndex, AssignmentStatementContextExt assignmentStatementContextExt, int actionIndex){
		AluComplex.add(instructionIndex,assignmentStatementContextExt);
		AluMap.add(instructionIndex,assignmentStatementContextExt,actionIndex);
	}
	

}
