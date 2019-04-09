package com.p4.pktgen.p4blocks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.TypeRefContextExt;
import com.p4.p416.gen.P416Parser.BitSizeTypeContext;
import com.p4.p416.gen.P416Parser.ParameterContext;
import com.p4.p416.gen.P416Parser.ParameterListContext;

@Getter
public class ActionBlock {

	private ActionDeclarationContextExt action;
	private String actionName;
	private List<ActionParams> actionParams;
	private Map<String,Integer> paramOffsets;
	private Integer totalWidth;
	
	public ActionBlock(ActionDeclarationContextExt action) {
		this.action = action;
		actionName = action.getNameString();
		actionParams = new LinkedList<ActionParams>();
		paramOffsets = new HashMap<String,Integer>();
		totalWidth = null;
		
		ParameterListContext plc = action.getContext().parameterList();
		if(plc != null && plc.parameter() != null) {
			for(ParameterContext param : plc.parameter()) {
				String paramName = AbstractBaseExt.getExtendedContext(param.name()).getFormattedText();
				
				String num = AbstractBaseExt.getExtendedContext( ((BitSizeTypeContext)AbstractBaseExt.getExtendedContext(((TypeRefContextExt)AbstractBaseExt.getExtendedContext(param.typeRef())).getContext().baseType()).getContext()).number()).getFormattedText();
				Integer size = Integer.parseInt(num);
				actionParams.add(new ActionParams(paramName, size));
			}
		}
	}
	
	public Integer getTotalWidthOfParams() {
		if(totalWidth != null)
			return totalWidth;
		
		int width = 0;
		for(ActionParams param : actionParams) {
			int offset = MemoryManager.getOffset(param.getParamName(), (Type) action.resolve(param.getParamName())) * 8;
			paramOffsets.put(param.getParamName(), offset);
			width = offset + param.getParamWidth();
		}
		totalWidth = width;
		return totalWidth;
	}
	
	public Integer getParamOffset(String param) {
		return paramOffsets.get(param);
	}
}
