package com.p4.pd.config.common;

import com.p4.drmt.cfg.KeyMeta;
import com.p4.pd.config.beans.*;
import com.p4.drmt.parser.*;

public class DrmtConfigGenerator {

    private KeyMeta keyMeta;
    private Keygen keygen;
    private AluComplex complex;
    private AluCondComplex condComplex;
    private AluMap map;
    private AluMapActionMapInst acmapInst;
    private Scheduler sch;
    private Extractor ex;
    private Tcam tcam;
    private Sram sram;
    private Tcamdpa tcamdpa;
    private Dextractordpa dexdpa;

    public DrmtConfigGenerator(KeyMeta keymeta){
        this.keyMeta = keymeta;
        this.keygen = Keygen.getInstance();
        this.complex = AluComplex.getInstance();
        this.condComplex = AluCondComplex.getInstance();
        this.map = AluMap.getInstance();
        this.acmapInst = AluMapActionMapInst.getInstance();
        this.sch = Scheduler.getInstance();
        this.ex = Extractor.getInstance();
        this.tcam = Tcam.getInstance();
        this.sram = Sram.getInstance();
        this.dexdpa = Dextractordpa.getInstance();
        this.tcamdpa = Tcamdpa.getInstance();

    }


    public void start(){
        System.out.println("[Scheduler] Generate Configuration [Start]");
        this.sch.generate();
        System.out.println("[Scheduler] Generate Configuration [Done]");

        System.out.println("[Keygen] Generate Configuration [Start]");
        keygen.setRows(keyMeta.getRowsWithSchedule());
        keygen.generate();
        System.out.println("[Keygen] Generate Configuration [Done]");

        System.out.println("[AluComplex] Generate Configuration [Start]");
        complex.generate();
        System.out.println("[AluComplex] Generate Configuration [Done]");

        System.out.println("[AluConditionalComplex] Generate Configuration [Start]");
        condComplex.generate();
        System.out.println("[AluConditionalComplex] Generate Configuration [Done]");

        System.out.println("[AluMap] Generate Configuration [Start]");
        map.generate();
        System.out.println("[AluMap] Generate Configuration [Done]");

        System.out.println("[AluMapActionMapInstance] Generate Configuration [Start]");
        acmapInst.generate();
        System.out.println("[AluMapActionMapInstance] Generate Configuration [Done]");

//        System.out.println("[Tcam-Parser] Generate Configuration [Start]");
//        tcam.setRows(ConfigUtil.getParserRows());
//        tcam.generate();
//        System.out.println("[Tcam-Parser] Generate Configuration [Done]");
//
//        System.out.println("[SRAM-Parser] Generate Configuration [Start]");
//        sram.setRows(ConfigUtil.getParserRows());
//        sram.generate();
//        System.out.println("[SRAM-Parser] Generate Configuration [Done]");
//
//        System.out.println("[Extractor] Generate Configuration [Start]");
//        ex.generate();
//        System.out.println("[Extractor] Generate Configuration [Done]");
//
//        System.out.println("[Tcam-DeParser] Generate Configuration [Done]");
//
//        System.out.println("[SRAM-Parser] Generate Configuration [Start]");
//        tcamdpa.setRows(ConfigUtil.getParserRows());
//        sram.generate();
//        System.out.println("[SRAM-Parser] Generate Configuration [Done]");
//
//        System.out.println("[DeExtractor] Generate Configuration [Start]");
//        dexdpa.generate();
//        System.out.println("[DeExtractor] Generate Configuration [Done]");

        //Serialize in JSON
      //  Utils.constructJSON(DrmtConfig.getInstance());
    }

}
