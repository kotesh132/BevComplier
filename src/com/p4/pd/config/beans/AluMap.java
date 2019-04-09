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
public class AluMap implements Configurable{

    private List<FW> en_off;
    private List<FW> en_vld;
    private List<FW> cond_tbl;
    private List<FW> cond_ptr;
    private List<FW> cond_vld;

    private static AluMap instance;
    private AluMap(){}
    static{
        instance = new AluMap();
    }
    public static AluMap getInstance(){
        return  instance;
    }

    @Override
    public List<Bitset> summary(){
        Bitset en_offList = ApiUtils.concat(ForgeSpec.ALUMAP_EN_OFF_BIT_SIZE,Utils.listTokenizerToBitsetList(en_off));
        Bitset en_vldList = ApiUtils.concat(ForgeSpec.ALUMAP_EN_VLD_BIT_SIZE,Utils.listTokenizerToBitsetList(en_vld));
        Bitset cond_tblList = ApiUtils.concat(ForgeSpec.ALUMAP_COND_TBL_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_tbl));
        Bitset cond_ptrList = ApiUtils.concat(ForgeSpec.ALUMAP_COND_PTR_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_ptr));
        Bitset cond_vldList = ApiUtils.concat(ForgeSpec.ALUMAP_COND_VLD_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_vld));
        return ConfigApis.concat_fields_drmt_dist_alumap_cond_en_0(cond_tblList, cond_ptrList, cond_vldList, en_offList, en_vldList);
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
        //Retrieve alu map configurations
        com.p4.drmt.alu.AluMap alumap = new com.p4.drmt.alu.AluMap();
        List<ConfigEmitUnit> configs = alumap.getAllConfigs();

        //Get List of Configs from getAllConfigs and based on order.
        //For more Info look for getAllConfigs method.
        // Index 2 determines en_off and Index 3 determines en_vld configs.

        List<FW> en_vld_list = configs.get(3).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> en_off_list = configs.get(2).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());

        int words = (en_off_list.size() * ForgeSpec.ALUMAP_EN_OFF_BIT_SIZE)/ForgeSpec.ALUMAP_EN_OFF_WORDLENGTH;

        AluMap mapRecord = AluMap.getInstance();
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();


        for(int word = 0; word < words; word++){
            List<FW> en_off = Utils.listTokeinizer(en_off_list,en_off_list.size()/words, word);
            List<FW> en_vld = Utils.listTokeinizer(en_vld_list,en_vld_list.size()/words, word);
            List<FW> cond_ptr = Utils.generateDefaultList(ForgeSpec.ALUMAP_COND_PTR_WORDLENGTH/ForgeSpec.ALUMAP_COND_PTR_BIT_SIZE, ForgeSpec.ALUMAP_COND_PTR_BIT_SIZE);
            List<FW> cond_tbl = Utils.generateDefaultList(ForgeSpec.ALUMAP_COND_TBL_WORDLENGTH/ForgeSpec.ALUMAP_COND_TBL_BIT_SIZE, ForgeSpec.ALUMAP_COND_TBL_BIT_SIZE);
            List<FW> cond_vld = Utils.generateDefaultList(ForgeSpec.ALUMAP_COND_VLD_WORDLENGTH/ForgeSpec.ALUMAP_COND_VLD_BIT_SIZE, ForgeSpec.ALUMAP_COND_VLD_BIT_SIZE);

            mapRecord.setCond_ptr(cond_ptr);
            mapRecord.setCond_tbl(cond_tbl);
            mapRecord.setCond_vld(cond_vld);
            mapRecord.setEn_off(en_off);
            mapRecord.setEn_vld(en_vld);

            List<Bitset> record = mapRecord.summary();
            records.add(record);
        }

        Processor processor = Processor.getInstance();
        processor.setAlumap(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records))));
        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
        DrmtConfig.getInstance().setDrmt(drmt);
    }
}
