package com.p4.quadpeaks;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P4programContextExt;
import com.p4.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class QPTransforms {

    private static final Logger L = LoggerFactory.getLogger(QPTransforms.class);

    public static void runStandard(P416Parser.P4programContext ctx, QPCommandLineParser cp, File f){
        AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
        scope.set(P4programContextExt.getExtendedContext(ctx));
        //Initial call for define Symnol. This may/maynot be needed
        ctx.extendedContext.defineSymbol(null);

        FileUtils.AppendToFile(cp.transformedFile(f), P4programContextExt.getExtendedContext(ctx).getFormattedText());

        L.info("Done with Transforms");
    }
}
