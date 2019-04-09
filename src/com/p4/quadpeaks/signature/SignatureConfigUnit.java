package com.p4.quadpeaks.signature;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.quadpeaks.parameters.QPConstants;
import com.p4.utils.Utils;
import lombok.Data;

import java.io.File;
import java.util.List;
@Data
public class SignatureConfigUnit extends ConfigUnitBase {
    public static String schDirName(String baseDir, String cdir){return baseDir+ File.separator+ cdir;}

    private final ConfigEmitUnit outr = new ConfigEmitUnit(QPConstants.STATEID_SIZE, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "outr.cfg");
    private final ConfigEmitUnit innr = new ConfigEmitUnit(QPConstants.STATEID_SIZE, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "innr.cfg");
    private final ConfigEmitUnit outrsz = new ConfigEmitUnit(QPConstants.BITBYTW, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "outr_len.cfg");
    private final ConfigEmitUnit innrsz = new ConfigEmitUnit(QPConstants.BITBYTW, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "innr_len.cfg");
    private final ConfigEmitUnit outrcnt = new ConfigEmitUnit(8, 1, QPConstants.SIGDPTH, "outr_cnt.cfg");
    private final ConfigEmitUnit innrcnt = new ConfigEmitUnit(8, 1, QPConstants.SIGDPTH, "innr_cnt.cfg");
    private final ConfigEmitUnit outrvbit =  new ConfigEmitUnit(1, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "outr_vbit.cfg");
    private final ConfigEmitUnit innrvbit =  new ConfigEmitUnit(1, QPConstants.ARRDPTH, QPConstants.SIGDPTH, "innr_vbit.cfg");
    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.arrList(outr,innr, outrsz, innrsz, outrcnt, innrcnt, outrvbit, innrvbit);
    }
}
