package com.p4.quadpeaks.extractor;


import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.quadpeaks.parameters.QPConstants;
import com.p4.utils.Utils;
import lombok.Data;

import java.io.File;
import java.util.List;
@Data
public class QPExtractorConfigUnit extends ConfigUnitBase{
    public static String schDirName(String baseDir, String cdir){return baseDir+ File.separator+ cdir;}

    private final ConfigEmitUnit ybyt = new ConfigEmitUnit(8, QPConstants.NUMCBYT, "ybyt.cfg");
    private final ConfigEmitUnit xbyt = new ConfigEmitUnit(8, QPConstants.NUMCBYT, "xbyt.cfg");
    private final ConfigEmitUnit ymsk = new ConfigEmitUnit(8, QPConstants.NUMCBYT, "ymsk.cfg");
    private final ConfigEmitUnit xsft = new ConfigEmitUnit(3, QPConstants.NUMCBYT, "xsft.cfg");
    private final ConfigEmitUnit xdir = new ConfigEmitUnit(1, QPConstants.NUMCBYT, "xdir.cfg");
    private final ConfigEmitUnit vbyt = new ConfigEmitUnit(1, QPConstants.NUMCBYT, "vbyt.cfg");
    private final ConfigEmitUnit cbyt = new ConfigEmitUnit(1, QPConstants.NUMCBYT, "cbyt.cfg");
    private final ConfigEmitUnit ybit = new ConfigEmitUnit(8, QPConstants.NUMCBIT, "ybit.cfg");
    private final ConfigEmitUnit xbit = new ConfigEmitUnit(11, QPConstants.NUMCBIT, "xbit.cfg");
    private final ConfigEmitUnit vbit = new ConfigEmitUnit(1, QPConstants.NUMCBIT,"vbit.cfg");
    private final ConfigEmitUnit cbit = new ConfigEmitUnit(1, QPConstants.NUMCBIT, "cbit.cfg");

    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.asList(ybyt,xbyt,ymsk, xsft,xdir,vbyt, cbyt,
                ybit, xbit,vbit,cbit);
    }

    @Override
    public String summary(){
        StringBuilder sb = new StringBuilder();
        sb.append("===>Byte Extracts<===\n");
        sb.append("ybyt="+ybyt.summary()+"\n");
        sb.append("xbyt="+xbyt.summary()+"\n");
        sb.append("ymsk="+ymsk.summary()+"\n");
        sb.append("xsft="+xsft.summary()+"\n");
        sb.append("xdir="+xdir.summary()+"\n");
        sb.append("vbyt="+vbyt.summary()+"\n");
        sb.append("cbyt="+cbyt.summary()+"\n");
        sb.append("===>Bit Extracts<===\n");
        sb.append("ybit="+ybit.summary()+"\n");
        sb.append("xbit="+xbit.summary()+"\n");
        sb.append("vbit="+vbit.summary()+"\n");
        sb.append("cbit="+cbit.summary()+"\n");

        return sb.toString();
    }
}
