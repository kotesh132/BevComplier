package com.p4.drmt.schd;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.utils.Utils;

import java.io.File;
import java.util.List;

public class SchLengthUnit extends ConfigUnitBase {


    //private final ConfigEmitUnit sch_data = new ConfigEmitUnit(32, 1, "sch_data.cfg");
    private final ConfigEmitUnit sch_node = new ConfigEmitUnit(DRMTConstants.BITVALS, 1, "sch_node.cfg");
    private final ConfigEmitUnit sch_nidx = new ConfigEmitUnit(DRMTConstants.BITNIDX, 1, "sch_nidx.cfg");
    private final ConfigEmitUnit sch_flag = new ConfigEmitUnit(1,       1, "sch_flag.cfg");
    private final ConfigEmitUnit sch_wait = new ConfigEmitUnit(DRMTConstants.BITWAIT, 1, "sch_wait.cfg");


    public static SchLengthUnit NOOPSCH = new SchLengthUnit();
    static{
        NOOPSCH.sch_flag.addItem(0);
        NOOPSCH.sch_nidx.addItem(0);
        NOOPSCH.sch_node.addItem(0);
        NOOPSCH.sch_wait.addItem(0);
    }

    public static String schDirName(String baseDir){return baseDir+ File.separator+ "sch";}
    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.arrList(sch_node, sch_nidx,sch_flag, sch_wait);
    }

    public static SchLengthUnit constructScheduleConfigNodeMatch(int cycleIndex, int matchIndex, int flag, int wait){
        SchLengthUnit sc = new SchLengthUnit();
        sc.sch_flag.addItem(flag);
        sc.sch_node.addItem(1);
        sc.sch_nidx.addItem(matchIndex);
        sc.sch_wait.addItem(wait);
        return sc;
    }

    public static SchLengthUnit constructScheduleConfigNodeAction(int cycleIndex, int actinIndex, int flag, int wait){
        SchLengthUnit sc = new SchLengthUnit();
        sc.sch_flag.addItem(flag);
        sc.sch_node.addItem(2);
        sc.sch_nidx.addItem(actinIndex);
        sc.sch_wait.addItem(wait);
        return sc;
    }
}
