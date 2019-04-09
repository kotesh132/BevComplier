package com.p4.drmt.keygen;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.utils.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@Data
public class KeyGenUnit extends ConfigUnitBase {

    private static final Logger L = LoggerFactory.getLogger(KeyGenUnit.class);
    public static String dirName = "new_key";

    private final ConfigEmitUnit ybyt = new ConfigEmitUnit(DRMTConstants.BYTE, DRMTConstants.NUMKSEG*DRMTConstants.maxBytes, "ybyt.cfg");
    private final ConfigEmitUnit ybit = new ConfigEmitUnit(DRMTConstants.BYTE, DRMTConstants.NUMKSEG*DRMTConstants.numkbit,  "ybit.cfg");
    private final ConfigEmitUnit ymsk = new ConfigEmitUnit(DRMTConstants.numkbit,      DRMTConstants.NUMKSEG,                        "ymsk.cfg");
    private final ConfigEmitUnit ktbl = new ConfigEmitUnit(DRMTConstants.BITTBLE,      DRMTConstants.NUMKSEG,                        "ktbl.cfg");
    private final ConfigEmitUnit kvld = new ConfigEmitUnit(1,      DRMTConstants.NUMKSEG,                        "kvld.cfg");
    private final ConfigEmitUnit kseg = new ConfigEmitUnit(3,      DRMTConstants.NUMKSEG,                        "kseg.cfg");


    public KeyGenUnit(){
        ybit.pad();
        ybyt.pad();
        ymsk.pad();
        ktbl.pad();
        kvld.pad();
        kseg.pad();
    }


    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.asList(ybyt, ybit, ymsk, ktbl, kvld, kseg);
    }


}
