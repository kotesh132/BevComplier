package com.p4.p416.iface;

import com.p4.p416.applications.*;
import org.antlr.v4.runtime.ParserRuleContext;

public interface IExtendedContext extends GetFormattedText, Symbol, CFGNode, DerivedType, TargetSymbol, IIRNode {

    <T extends ParserRuleContext> T getContext();

    <T extends ParserRuleContext> T getContext(String str);

    <T extends ParserRuleContext> void setContext(T ctx);

}
