package com.p4.pd.config.beans;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.pd.config.common.Configurable;
import com.p4.pd.config.common.ForgeSpec;
import com.p4.pd.config.common.Utils;
import lombok.Data;
import java.util.*;
import com.p4.pd.config.common.*;
import java.util.stream.Collectors;

@Data
public class AluComplex implements Configurable{
    private List<FW> op0_type;
    private List<FW> op0_len;
    private List<FW> op0_off;
    private List<FW> op1_type;
    private List<FW> op1_len;
    private List<FW> op1_off;
    private List<FW> opcode;
    private List<FW> res_len;
    private List<FW> res_off;
    private List<FW> pt_step;
    private List<FW> cond_off;
    private List<FW> cond_en;
    //Source Path needs to be setup before invoking generate method.
    private String source;
    private final int NUM_ALUS = 32;

    private static AluComplex instance;
    static{
        instance = new AluComplex();
    }
    private AluComplex(){}

    public static AluComplex getInstance(){
        return instance;
    }

    @Override
    public String toString(){
        //TODO
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }


    @Override
    public List<Bitset> summary(){
        Bitset op0_typeList = ApiUtils.concat(ForgeSpec.ALU_OP0_TYPE_BIT_SIZE,Utils.listTokenizerToBitsetList(op0_type));
        Bitset op0_lenList = ApiUtils.concat(ForgeSpec.ALU_OP0_LEN_BIT_SIZE,Utils.listTokenizerToBitsetList(op0_len));
        Bitset op0_offList = ApiUtils.concat(ForgeSpec.ALU_OP0_OFF_BIT_SIZE,Utils.listTokenizerToBitsetList(op0_off));
        Bitset op1_typeList = ApiUtils.concat(ForgeSpec.ALU_OP1_TYPE_BIT_SIZE,Utils.listTokenizerToBitsetList(op1_type));
        Bitset op1_lenList = ApiUtils.concat(ForgeSpec.ALU_OP1_LEN_BIT_SIZE,Utils.listTokenizerToBitsetList(op1_len));
        Bitset op1_offList = ApiUtils.concat(ForgeSpec.ALU_OP1_OFF_BIT_SIZE,Utils.listTokenizerToBitsetList(op1_off));
        Bitset opcodeList = ApiUtils.concat(ForgeSpec.ALU_OP_CODE_BIT_SIZE,Utils.listTokenizerToBitsetList(opcode));
        Bitset res_lenList = ApiUtils.concat(ForgeSpec.ALU_RES_LEN_BIT_SIZE,Utils.listTokenizerToBitsetList(res_len));
        Bitset res_offList = ApiUtils.concat(ForgeSpec.ALU_RES_OFF_BIT_SIZE,Utils.listTokenizerToBitsetList(res_off));
        Bitset pt_stepList = ApiUtils.concat(ForgeSpec.ALU_PT_STEP_BIT_SIZE,Utils.listTokenizerToBitsetList(pt_step));
        Bitset cond_offList = ApiUtils.concat(ForgeSpec.ALU_COND_OFF_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_off));
        Bitset cond_enList = ApiUtils.concat(ForgeSpec.ALU_COND_EN_BIT_SIZE,Utils.listTokenizerToBitsetList(cond_en));
        return ConfigApis.concat_fields_drmt_dist_alu_0(op0_typeList,op0_lenList,op0_offList,op1_typeList, op1_lenList,
                op1_offList,opcodeList,res_lenList,res_offList,pt_stepList,cond_offList,cond_enList);
    }

    @Override
    public void generate(){
        update();
    }


    private void update(){
        com.p4.drmt.alu.AluComplex alucomplex = new com.p4.drmt.alu.AluComplex();
        List<ConfigEmitUnit> configs = alucomplex.getAllConfigs();
        //Get List of Configs from getAllConfigs and based on order.
        //For more Info look for getAllConfigs method.
        List<FW> cond_en_list = configs.get(0).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> cond_off_list = configs.get(1).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op0_type_list = configs.get(2).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op0_len_list = configs.get(3).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op0_off_list = configs.get(4).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op1_type_list = configs.get(5).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op1_len_list = configs.get(6).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> op1_off_list = configs.get(7).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> opcode_list = configs.get(8).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> res_len_list = configs.get(9).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> res_off_list = configs.get(10).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<FW> pt_step_list = configs.get(11).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());

        int words = (ForgeSpec.ALU_COND_EN_BIT_SIZE * cond_en_list.size())/ForgeSpec.ALU_COND_EN_WORDLENGTH;

        AluComplex record = AluComplex.getInstance();
        List<List<List<Bitset>>> records = new ArrayList<List<List<Bitset>>>();
        //Initializing record fields
        for(int i=0; i< NUM_ALUS; i++){
            records.add(new ArrayList<List<Bitset>>());
        }

        for(int word = 0; word<words;word++){
            List<FW> cond_en = Utils.listTokeinizer(cond_en_list, cond_en_list.size()/words, word);
            List<FW> cond_off = Utils.listTokeinizer(cond_off_list, cond_off_list.size()/words, word);
            List<FW> op0_type = Utils.listTokeinizer(op0_type_list, op0_type_list.size()/words, word);
            List<FW> op1_type = Utils.listTokeinizer(op1_type_list, op1_type_list.size()/words, word);
            List<FW> op0_len = Utils.listTokeinizer(op0_len_list, op0_len_list.size()/words, word);
            List<FW> op1_len = Utils.listTokeinizer(op1_len_list, op1_len_list.size()/words, word);
            List<FW> op0_off = Utils.listTokeinizer(op0_off_list, op0_off_list.size()/words, word);
            List<FW> op1_off = Utils.listTokeinizer(op1_off_list, op1_off_list.size()/words, word);
            List<FW> opcode = Utils.listTokeinizer(opcode_list, opcode_list.size()/words, word);
            List<FW> res_len = Utils.listTokeinizer(res_len_list, res_len_list.size()/words, word);
            List<FW> res_off = Utils.listTokeinizer(res_off_list, res_off_list.size()/words, word);
            List<FW> pt_step = Utils.listTokeinizer(pt_step_list, pt_step_list.size()/words, word);

            record.setCond_en(cond_en);
            record.setCond_off(cond_off);
            record.setOp0_len(op0_len);
            record.setOp0_off(op0_off);
            record.setOp1_len(op1_len);
            record.setOp1_off(op1_off);
            record.setOpcode(opcode);
            record.setOp0_type(op0_type);
            record.setOp1_type(op1_type);
            record.setRes_len(res_len);
            record.setRes_off(res_off);
            record.setPt_step(pt_step);

            int aluId = word % NUM_ALUS;
            records.get(aluId).add(record.summary());
            //List<List<Bitset>> tuple = records.get(aluId);
            //records.set(aluId, tuple);
        }

        Processor processor = Processor.getInstance();
        processor.setAlucomplex0(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(0)))));
        processor.setAlucomplex1(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(1)))));
        processor.setAlucomplex2(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(2)))));
        processor.setAlucomplex3(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(3)))));
        processor.setAlucomplex4(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(4)))));
        processor.setAlucomplex5(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(5)))));
        processor.setAlucomplex6(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(6)))));
        processor.setAlucomplex7(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(7)))));
        processor.setAlucomplex8(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(8)))));
        processor.setAlucomplex9(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(9)))));
        processor.setAlucomplex10(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(10)))));
        processor.setAlucomplex11(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(11)))));
        processor.setAlucomplex12(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(12)))));
        processor.setAlucomplex13(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(13)))));
        processor.setAlucomplex14(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(14)))));
        processor.setAlucomplex15(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(15)))));
        processor.setAlucomplex16(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(16)))));
        processor.setAlucomplex17(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(17)))));
        processor.setAlucomplex18(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(18)))));
        processor.setAlucomplex19(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(19)))));
        processor.setAlucomplex20(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(20)))));
        processor.setAlucomplex21(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(21)))));
        processor.setAlucomplex22(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(22)))));
        processor.setAlucomplex23(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(23)))));
        processor.setAlucomplex24(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(24)))));
        processor.setAlucomplex25(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(25)))));
        processor.setAlucomplex26(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(26)))));
        processor.setAlucomplex27(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(27)))));
        processor.setAlucomplex28(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(28)))));
        processor.setAlucomplex29(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(29)))));
        processor.setAlucomplex30(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(30)))));
        processor.setAlucomplex31(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records.get(31)))));

        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
        DrmtConfig.getInstance().setDrmt(drmt);
    }
}
