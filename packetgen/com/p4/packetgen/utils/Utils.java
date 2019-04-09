package com.p4.packetgen.utils;

import org.stringtemplate.v4.STGroupFile;

public class Utils {

    private static Integer countForLookahead = 0;
    private static STGroupFile grp = new STGroupFile("packetgen/com/memoir/parser/p416/runner/templateGroupFile.stg");

    public static String getLookaheadVar(){
        return "lookahead"+(new Integer(countForLookahead));
    }

    public static STGroupFile getStgGrp(){
        return grp;
    }

    public static void increment(){
        countForLookahead++;
    }

}

