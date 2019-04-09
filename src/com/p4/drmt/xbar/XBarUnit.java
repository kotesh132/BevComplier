package com.p4.drmt.xbar;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XBarUnit extends ConfigUnitBase {

    private static final Logger L = LoggerFactory.getLogger(XBarUnit.class);


    public static String dirName = "key";

    private final ConfigEmitUnit kmap_vld = new ConfigEmitUnit(1, DRMTConstants.NUMKSEG*DRMTConstants.NUMXTDM, DRMTConstants.NUMSOMS, "kmap_vld.cfg");
    private final ConfigEmitUnit ktbl_map = new ConfigEmitUnit(DRMTConstants.BITTBLE, DRMTConstants.NUMKSEG*DRMTConstants.NUMXTDM, DRMTConstants.NUMSOMS, "ktbl_map.cfg");
    private final ConfigEmitUnit kseg_map = new ConfigEmitUnit(DRMTConstants.BITKSEG, DRMTConstants.NUMKSEG*DRMTConstants.NUMXTDM, DRMTConstants.NUMSOMS, "kseg_map.cfg");
    //2 for now
    private final ConfigEmitUnit dseg_map = new ConfigEmitUnit(DRMTConstants.BITTBLE, DRMTConstants.NUMSOMS*DRMTConstants.NUMDSEG*DRMTConstants.NUMXTDM, DRMTConstants.NUMDRMTPROCS, "dseg_map.cfg");

    public XBarUnit(){
        kmap_vld.pad2D();
        ktbl_map.pad2D();
        kseg_map.pad2D();
        dseg_map.pad2D();
    }

    @Override
    public List<ConfigEmitUnit> getAllConfigs() {
        return Utils.arrList(kmap_vld, ktbl_map, kseg_map, dseg_map);
    }

    public static void generateXBarConfig(Map<Integer, Utils.Pair<Integer, Set<Integer>>> tableSOMMap, Map<Integer, Integer> xtdms, int[][] dsegMap, String outputDir){
        XBarUnit xb = new XBarUnit();
        for(Map.Entry<Integer, Utils.Pair<Integer, Set<Integer>>>entry: tableSOMMap.entrySet()){
            int tableId = entry.getKey();
            int som = entry.getValue().first();
            Set<Integer> ksegs = entry.getValue().second();
            //som = 2;
            if(!xtdms.containsKey(tableId)){
                throw new IllegalStateException("Xtdm value for Table Id "+ tableId+" is not defined");
            }
            for(Integer kseg :ksegs){
                int rowIndex = kseg*DRMTConstants.NUMXTDM+ xtdms.get(tableId);
                xb.kmap_vld.setItem(1, som, rowIndex);
                xb.ktbl_map.setItem(tableId, som, rowIndex);
                L.info("Table Id = "+tableId+", SOM = "+som+", SEG ="+kseg);
            }
        }
        int count = 0;
        for(int i=0; i<DRMTConstants.NUMDRMTPROCS; i++){
            for(int j = 0; j< dsegMap.length; j++){
                for(int k = 0; k < dsegMap[j].length; k++){
                    xb.dseg_map.setItem(dsegMap[j][k], i, count);
                    count++;
                }
            }
            count = 0;
        }

        xb.emitAll(outputDir+ File.separator+dirName);
    }
}
