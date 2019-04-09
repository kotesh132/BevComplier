package com.p4.cgen1.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IActionDeclaration;

public class Action {

	@JsonIgnore
	private IActionDeclaration actionDeclaration;
	private List<ActionParameter> actionParameters;
	
	public Action(IActionDeclaration actionDeclaration, List<ActionParameter> actionParameters) {
		this.actionDeclaration = actionDeclaration;
		this.actionParameters = actionParameters;
	}

	public String getName() {
		return actionDeclaration.getNameString();
	}

	public List<ActionParameter> getActionParameters(){
		return this.actionParameters;
	}

	public String getBlockStatements() {
		return actionDeclaration.getBlockStatement().getFormattedText();
	}
	
	public Integer getActionId() {
		return actionDeclaration.getActionId();
	}

}
