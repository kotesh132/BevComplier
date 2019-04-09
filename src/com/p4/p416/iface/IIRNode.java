package com.p4.p416.iface;

import com.p4.p416.applications.GetFormattedText;
import com.p4.p416.applications.Scope;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IIRNode extends GetFormattedText, Scope {

    List<IIRNode> getChildren();

    List<IIRNode> getChildrenIncludingTerminalNodes();

    IIRNode getParentIRNode();

    void visitNode(IIRNodeVisitor visitor);

    boolean visitNode(Function<IIRNode, Boolean> function);

    void postVisitNode(IIRNodeVisitor visitor);

    void postVisitNode(IIRNodeVisitor visitor, Function<IIRNode, Boolean> canVisitChildren);

    void enterExitNode(Consumer<IIRNode> enterConsumer, Consumer<IIRNode> exitConsumer);

    boolean isTerminalNode();

    boolean visitFirstNode(Function<IIRNode, Boolean> function);
}
