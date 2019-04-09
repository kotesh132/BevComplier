package com.p4.drmt.parser;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.List;
@Data
public class DeparserConfigUnit extends ConfigUnitBase {
    public static String dirName = "new_dpa";

    private final ConfigEmitUnit sram_min_max = new ConfigEmitUnit(DRMTConstants.BYTE, 2, "sram_min_max.cfg");
    private final ConfigEmitUnit sram_done = new ConfigEmitUnit(1, 96,  "sram_done.cfg");
    private final ConfigEmitUnit sram_shift = new ConfigEmitUnit(DRMTConstants.DPASHIFT,      96, "sram_shift.cfg");
    private final ConfigEmitUnit sram_next = new ConfigEmitUnit(7,      96, "sram_next.cfg");
    private final ConfigEmitUnit sram_flag = new ConfigEmitUnit(DRMTConstants.BYTE,      96, "sram_flag.cfg");
    private final ConfigEmitUnit sres_alue = new ConfigEmitUnit(1,      96, "sres_alue.cfg");
    private final ConfigEmitUnit sres_alui_opcode = new ConfigEmitUnit(4,      96, "sres_alui_opcode.cfg");
    private final ConfigEmitUnit sres_alui_mask = new ConfigEmitUnit(16,      96, "sres_alui_mask.cfg");
    private final ConfigEmitUnit sres_alui_op0 = new ConfigEmitUnit(16,      96, "sres_alui_op0.cfg");
    private final ConfigEmitUnit sres_alui_op1 = new ConfigEmitUnit(16,      96, "sres_alui_op1.cfg");
    private final ConfigEmitUnit sres_alui_op2 = new ConfigEmitUnit(16,      96, "sres_alui_op2.cfg");


    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.asList(sram_min_max, sram_done, sram_shift, sram_next, sram_flag, sres_alue, sres_alui_opcode, sres_alui_mask, sres_alui_op0, sres_alui_op1, sres_alui_op2);
    }
}
