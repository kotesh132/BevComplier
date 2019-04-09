package com.p4.cgen1.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.p4.p416.iface.IControl;

public class Control {

	@JsonIgnore
	private IControl control;
	private List<Parameter> parameters;
	private List<Action> actions;
	private List<Table> tables;
	private String controlBody;
	private Integer actionNumber;

	@JsonCreator
	public Control(IControl control, List<Parameter> parameters, List<Action> actions, List<Table> tables, String controlBody,Integer actionNumber) {
		this.control = control;
		this.parameters = parameters;
		this.actions = actions;
		this.tables = tables;
		this.controlBody = controlBody;
		this.actionNumber = actionNumber;
	}
	
	public String getName() {
		return control.getNameString();
	}
	
	public Integer getActionNumber() {
		return actionNumber;
	}

	public List<Parameter> getParameters(){
		return this.parameters;
	}

	public List<Action> getActions(){
		return this.actions;
	}

	public List<Table> getTables(){
		return this.tables;
	}

	public String getControlBody() {
		return this.controlBody;
	}

}
