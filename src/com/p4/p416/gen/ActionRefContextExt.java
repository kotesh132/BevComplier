package com.p4.p416.gen;

import com.p4.p416.applications.Symbol;
import com.p4.p416.iface.*;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class ActionRefContextExt extends AbstractBaseExt implements IActionRef {

	public ActionRefContextExt(ParserRuleContext context) {
		super(context);
	}
	public abstract String getActionName();
	public abstract boolean isDefaultAction();

    @Override
    public IActionDeclaration getActionDeclaration() {
        IIRNode parent = getParentIRNode();
        while (parent != null && !(parent instanceof IControl)) {
            parent = parent.getParentIRNode();
        }

        if (parent != null) {
            Symbol symbol = parent.resolve(getActionName());
            if (symbol instanceof IActionDeclaration) {
                return (IActionDeclaration) symbol;
            }
        }
        return null;
    }

    @Override
    public List<IArgument> getArguments() {
        //do not want to return null by default.
        return new ArrayList<>();
    }
}

