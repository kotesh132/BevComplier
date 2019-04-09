package com.p4.p416.gen;

import com.p4.p416.gen.P416Parser.*;

/**
 * Created by rrajarap on 23/08/17.
 * CISCO
 */
public class FactoryContext {

    public static P4programContextExt createP4Program(P4programContext ctx) {
        return new P4programContextExt(ctx);
    }
}
