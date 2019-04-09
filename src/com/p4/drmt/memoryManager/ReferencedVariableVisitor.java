package com.p4.drmt.memoryManager;

import java.util.LinkedHashMap;
import java.util.Map;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ExprMemberAccessContextExt;
import com.p4.p416.gen.ExtendedContextGetVisitor;
import lombok.Getter;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.HeaderTypeDeclarationContextExt;
import com.p4.p416.gen.HeaderUnionDeclarationContextExt;
import com.p4.p416.gen.IntegerContextExt;
import com.p4.p416.gen.NonTypeContextExt;
import com.p4.p416.gen.StructTypeDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;

public class ReferencedVariableVisitor extends P416BaseVisitor<ParserRuleContext> {

    private static final Logger L = LoggerFactory.getLogger(ReferencedVariableVisitor.class);
    private ExtendedContextGetVisitor extendedContextGetVisitor;

    public ReferencedVariableVisitor() {
        super();
        extendedContextGetVisitor = new ExtendedContextGetVisitor();
    }

    @Getter
    //get all the referenced/used symbols from p4 program
    public Map<IMemoryInstance, Symbol> referencedSymbols = new LinkedHashMap<>();

    @Override
    public ParserRuleContext visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx) {
        addToReferenceSymbols(AssignmentStatementContextExt.getExtendedContext(ctx.lvalue()));
        super.visitAssignmentStatement(ctx);
        return ctx;
    }

    @Override
    public ParserRuleContext visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx) {
        if (!ctx.getText().contains("emit"))
            super.visitCallWithoutTypeArgs(ctx);
        return ctx;
    }

    @Override
    public ParserRuleContext visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx) {
        addToReferenceSymbols(ExprMemberAccessContextExt.getExtendedContext(ctx));
        return ctx;
    }

    @Override
    public ParserRuleContext visitInteger(P416Parser.IntegerContext ctx) {
        addToReferenceSymbols(IntegerContextExt.getExtendedContext(ctx));
        return ctx;
    }

    @Override
    public ParserRuleContext visitNonType(P416Parser.NonTypeContext ctx) {
        addToReferenceSymbols(NonTypeContextExt.getExtendedContext(ctx));
        return ctx;
    }

    @Override
    public ParserRuleContext visitParameterList(P416Parser.ParameterListContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitFunctionCall(P416Parser.FunctionCallContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitParserDeclaration(P416Parser.ParserDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitErrorDeclaration(P416Parser.ErrorDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx) {
        return ctx;
    }

    @Override
    public ParserRuleContext visitInstantiation(P416Parser.InstantiationContext ctx) {
        return ctx;
    }

    private void addToReferenceSymbols(Symbol symbol) {
        Type declarationSymbol = (Type) symbol.resolve(symbol.getSymbolName());
        if ((declarationSymbol.getType() instanceof HeaderTypeDeclarationContextExt) | (declarationSymbol.getType() instanceof HeaderUnionDeclarationContextExt) | (declarationSymbol.getType() instanceof StructTypeDeclarationContextExt)) {
            L.warn("not adding instance of headerUnion, should be unrolled " + symbol.getSymbolName());
        } else {
            L.debug("adding to references symbols " + symbol.getSymbolName());
            MemoryInstance memoryInstance = new MemoryInstance(symbol.getSymbolName(), declarationSymbol);
            referencedSymbols.putIfAbsent(memoryInstance, symbol);
        }
    }

    @Override
    public ParserRuleContext visitChildren(RuleNode node) {
        //get latest context of node
        AbstractBaseExt ext = extendedContextGetVisitor.visit(node);

        return super.visitChildren(ext.getContext());
    }
}
