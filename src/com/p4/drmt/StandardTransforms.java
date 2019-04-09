package com.p4.drmt;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.gen.StatementOrDeclarationContextExt;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

public class StandardTransforms {

    private static final Logger L = LoggerFactory.getLogger(StandardTransforms.class);

    public static void runStandard(P416Parser.P4programContext ctx, CommandLineParser cp, File f){
        AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
        scope.set(P4programContextExt.getExtendedContext(ctx));
        //Initial call for define Symnol. This may/maynot be needed
        ctx.extendedContext.defineSymbol(null);

        //remove switch
//        Stack<ExpressionContextExt> stacks = new Stack<>();
//        List<StatementOrDeclarationContextExt> lists = new ArrayList<>();
//        P4programContextExt.getExtendedContext(ctx).removeSwitch(lists, stacks);

        //Quadrupalize Instructions
        P4programContextExt.getExtendedContext(ctx).replaceVd();
        L.info("Starting Quadrupalizing");
        List<StatementOrDeclarationContextExt> ctxs = new ArrayList<StatementOrDeclarationContextExt>();
		//        P4programContextExt.getExtendedContext(ctx).quadrupalize(false,null);
        P4programContextExt.getExtendedContext(ctx).quadrupalize(false,ctxs );
        FileUtils.AppendToFile(cp.quadupalizedFile(f), P4programContextExt.getExtendedContext(ctx).getFormattedText());
        Stack<ExpressionContextExt> stack = new Stack<>();
        L.info("Starting Else Removal");
        P4programContextExt.getExtendedContext(ctx).removeElse(null, stack);
        L.debug(P4programContextExt.getExtendedContext(ctx).getFormattedText());
        stack.clear();

        L.info("Assigning Predicates");
        P4programContextExt.getExtendedContext(ctx).assignPredicate(stack);
        L.info("Done with Predicates");

        L.info("Done with valids");
        P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
        L.debug("*************************");
        L.debug("======================================");
        L.info(P4programContextExt.getExtendedContext(ctx).getFormattedText());
        FileUtils.AppendToFile(cp.transformedFile(f), P4programContextExt.getExtendedContext(ctx).getFormattedText());

    }
}
