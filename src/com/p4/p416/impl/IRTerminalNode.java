package com.p4.p416.impl;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.iface.IIRTerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by rrajarap on 28/08/17.
 * CISCO
 */
public class IRTerminalNode extends AbstractBaseExt implements IIRTerminalNode {

    private String terminalNodeText = null;

    public IRTerminalNode(String text) {
    	super(null);
        this.terminalNodeText = text;
    }

    @Override
    public <T extends ParserRuleContext> T getContext() {
        return null;
    }

    @Override
    public <T extends ParserRuleContext> T getContext(String str) {
        return null;
    }

    @Override
    public <T extends ParserRuleContext> void setContext(T ctx) {
    }

    @Override
    public String getFormattedText() {
        return terminalNodeText;
    }

    @Override
    public boolean isTerminalNode() {
        return true;
    }
}
