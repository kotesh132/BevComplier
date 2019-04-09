package com.p4.pd.config.beans;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.pd.config.common.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AluMapActionMapInst implements Configurable{

    private List<FW> acmap;
    private List<FW> inst_en;

    private static AluMapActionMapInst instance;
    private AluMapActionMapInst(){}
    static{
        instance = new AluMapActionMapInst();
    }
    public static AluMapActionMapInst getInstance(){
        return  instance;
    }

    @Override
    public List<Bitset> summary(){
        //TODO
        List<Bitset> fields = new ArrayList<Bitset>();
        Bitset acmapList = ApiUtils.concat(ForgeSpec.ALUMAPINST_AC_MAP_BIT_SIZE,Utils.listTokenizerToBitsetList(acmap));
        Bitset inst_enList = ApiUtils.concat(ForgeSpec.ALUMAPINST_INST_EN_BIT_SIZE,Utils.listTokenizerToBitsetList(inst_en));
        return ConfigApis.concat_fields_drmt_dist_alumap_acmap_insten_0(acmapList,inst_enList);
    }

    @Override
    public String toString(){
        //TODO
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public void generate(){
        update();
    }

    private void update(){
        com.p4.drmt.alu.AluMap alumap = new com.p4.drmt.alu.AluMap();
        List<ConfigEmitUnit> configs = alumap.getAllConfigs();

        //Get List of Configs from getAllConfigs and based on order.
        //For more Info look for getAllConfigs method.
        // Index 0 determines acmap and Index 1 determines inst_en configs.

        List<FW> acmap_list = configs.get(0).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> inst_en_list = configs.get(1).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());

        AluMapActionMapInst record = AluMapActionMapInst.getInstance();
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();

        int words = (acmap_list.size() * ForgeSpec.ALUMAPINST_AC_MAP_BIT_SIZE)/ForgeSpec.ALUMAPINST_AC_MAP_WORDLENGTH;
        for(int word = 0; word < words; word++){
            List<FW> acmap = Utils.listTokeinizer(acmap_list,acmap_list.size()/words, word);
            List<FW> inst_en = Utils.listTokeinizer(inst_en_list,inst_en_list.size()/words, word);

            record.setAcmap(acmap);
            record.setInst_en(inst_en);

            records.add(record.summary());
        }

        Processor processor = Processor.getInstance();
        processor.setAluactionmapinstance(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records))));
        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
        DrmtConfig.getInstance().setDrmt(drmt);
    }
}
