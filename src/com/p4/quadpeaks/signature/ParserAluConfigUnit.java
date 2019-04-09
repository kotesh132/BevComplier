package com.p4.quadpeaks.signature;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.quadpeaks.parameters.QPConstants;
import com.p4.utils.Utils;
import lombok.Data;

import java.io.File;
import java.util.List;
@Data
public class ParserAluConfigUnit extends ConfigUnitBase{
    public static String schDirName(String baseDir, String cdir){return baseDir+ File.separator+ cdir;}

    private final ConfigEmitUnit sres_alui_opcode = new ConfigEmitUnit(QPConstants.OPCODESIZE, 1, QPConstants.NUMSTATES, "sres_alui_opcode.cfg");
    private final ConfigEmitUnit sres_alui_op0 = new ConfigEmitUnit(QPConstants.TWOBYTES, 1, QPConstants.NUMSTATES, "sres_alui_op0.cfg");
    private final ConfigEmitUnit sres_alui_mask = new ConfigEmitUnit(QPConstants.TWOBYTES, 1, QPConstants.NUMSTATES, "sres_alui_mask.cfg");
    private final ConfigEmitUnit sres_alui_op1 = new ConfigEmitUnit(QPConstants.TWOBYTES, 1, QPConstants.NUMSTATES, "sres_alui_op1.cfg");
    private final ConfigEmitUnit sres_alui_op2 = new ConfigEmitUnit(QPConstants.TWOBYTES, 1, QPConstants.NUMSTATES, "sres_alui_op2.cfg");



    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.arrList(sres_alui_opcode, sres_alui_op0, sres_alui_mask, sres_alui_op1, sres_alui_op2);
    }
}
