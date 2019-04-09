package com.p4.pd.config.beans;

import com.p4.drmt.alu.ConditionalComplexRow;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.pd.config.common.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AluCondComplex implements Configurable{
    private List<FW> prim_off;
    private List<FW> prim_neg;
    private List<FW> prim_vld;
    private List<FW> alu_inst;
    private List<FW> cond_adr;
    private List<FW> br_data;
    private List<FW> br_mask;
    private List<FW> br_dest;
    private List<FW> br_vld;

    private static AluCondComplex instance;
    private AluCondComplex(){}
    static{
        instance = new AluCondComplex();
    }
    public static AluCondComplex getInstance(){
        return instance;
    }

    @Override
    public List<Bitset> summary(){
        Bitset prim_offList = ApiUtils.concat(ForgeSpec.ALUCOND_PRIMOFF_BIT_SIZE,Utils.listTokenizerToBitsetList(prim_off));
        Bitset prim_negList = ApiUtils.concat(ForgeSpec.ALUCOND_PRIMNEG_BIT_SIZE,Utils.listTokenizerToBitsetList(prim_neg));
        Bitset prim_vldList = ApiUtils.concat(ForgeSpec.ALUCOND_PRIMVLD_BIT_SIZE,Utils.listTokenizerToBitsetList(prim_vld));
        Bitset alu_instList = ApiUtils.concat(ForgeSpec.ALUCOND_ALUINST_BIT_SIZE,Utils.listTokenizerToBitsetList(alu_inst));
        Bitset cond_adrList = ApiUtils.concat(ForgeSpec.ALUCOND_CONDADR_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_adr));
        Bitset br_dataList = ApiUtils.concat(ForgeSpec.ALUCOND_BRDATA_BIT_SIZE,Utils.listTokenizerToBitsetList(br_data));
        Bitset br_maskList = ApiUtils.concat(ForgeSpec.ALUCOND_BRMASK_BIT_SIZE,Utils.listTokenizerToBitsetList(br_mask));
        Bitset br_destList = ApiUtils.concat(ForgeSpec.ALUCOND_BRDEST_BIT_SIZE,Utils.listTokenizerToBitsetList(br_dest));
        Bitset br_vldList = ApiUtils.concat(ForgeSpec.ALUCOND_BRVLD_BIT_SIZE,Utils.listTokenizerToBitsetList(br_vld));
        return ConfigApis.concat_fields_drmt_dist_ccomplex_0(prim_offList, prim_negList, prim_vldList,
                alu_instList, cond_adrList, br_dataList, br_maskList, br_destList, br_vldList);
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


    private void update() {
        ConditionalComplexRow conditionalComplex = new ConditionalComplexRow();
        conditionalComplex.pad2D();
        List<ConfigEmitUnit> configs = conditionalComplex.getAllConfigs();
        //Get List of Configs from getAllConfigs and based on order.
        //For more Info look for getAllConfigs method.
        List<FW> prim_vld_list = configs.get(0).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> prim_offset_list = configs.get(1).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> prim_neg_list = configs.get(2).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> alu_inst_list = configs.get(3).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> cond_adr_list = configs.get(4).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> br_vld_list = configs.get(5).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> br_data_list = configs.get(6).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> br_mask_list = configs.get(7).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> br_dest_list = configs.get(8).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());

        int words = (br_data_list.size() * ForgeSpec.ALUCOND_BRDATA_BIT_SIZE)/ForgeSpec.ALUCOND_BRDATA_WORDLENGTH;

        AluCondComplex record = AluCondComplex.getInstance();
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();

        for(int word = 0; word< words; word++){
            List<FW> prim_vld = Utils.listTokeinizer(prim_vld_list, prim_vld_list.size()/words, word);
            List<FW> prim_offset = Utils.listTokeinizer(prim_offset_list, prim_offset_list.size()/words, word);
            List<FW> prim_neg = Utils.listTokeinizer(prim_neg_list, prim_neg_list.size()/words, word);
            List<FW> alu_inst = Utils.listTokeinizer(alu_inst_list, alu_inst_list.size()/words, word);
            List<FW> cond_adr = Utils.listTokeinizer(cond_adr_list, cond_adr_list.size()/words, word);
            List<FW> br_vld = Utils.listTokeinizer(br_vld_list, br_vld_list.size()/words, word);
            List<FW> br_data = Utils.listTokeinizer(br_data_list, br_data_list.size()/words, word);
            List<FW> br_mask = Utils.listTokeinizer(br_mask_list, br_mask_list.size()/words, word);
            List<FW> br_dest = Utils.listTokeinizer(br_dest_list, br_dest_list.size()/words, word);

            record.setPrim_vld(prim_vld);
            record.setPrim_off(prim_offset);
            record.setPrim_neg(prim_neg);
            record.setAlu_inst(alu_inst);
            record.setCond_adr(cond_adr);
            record.setBr_vld(br_vld);
            record.setBr_data(br_data);
            record.setBr_mask(br_mask);
            record.setBr_dest(br_dest);

            records.add(record.summary());
        }

        Processor processor = Processor.getInstance();
        processor.setAlucondcomplex(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records))));
        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
        DrmtConfig.getInstance().setDrmt(drmt);
    }
}
