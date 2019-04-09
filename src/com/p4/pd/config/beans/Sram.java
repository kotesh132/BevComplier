package com.p4.pd.config.beans;

import com.p4.drmt.parser.FW;
import com.p4.drmt.parser.ParserRow;
import com.p4.drmt.parser.Row;
import com.p4.drmt.parser.RowType;
import com.p4.pd.config.common.*;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Data
public class Sram implements Configurable {
    private List<FW> sram_done; //DONE
    private List<FW> sram_next; //NEXT
    private List<FW> sram_shift; //SHFT
    private List<FW> sram_doff; //DOFF
    private List<FW> sram_dvld; //DVLD
    private List<FW> sram_flag; //FLAG
    private List<FW> sres_alue; //ALUE
    private List<FW> sres_alui_opcode; //ALUI
    private List<FW> sres_alui_op0; //ALUI
    private List<FW> sres_alui_mask; //ALUI
    private List<FW> sres_alui_op1; //ALUI
    private List<FW> sres_alui_op2; //ALUI


    //Needs to be Set, before Invoking Parser Configuration generate method.
    private List<ParserRow> rows;

    //Retrieved from KeyRow
    private static Map<String, List<FW>> configcache = new HashMap<String, List<FW>>();


    private static Sram instance;

    private Sram() {
    }

    static {
        instance = new Sram();
    }

    public static Sram getInstance() {
        return instance;
    }

    @Override
    public List<Bitset> summary() {
        Bitset doneList = ApiUtils.concat(ForgeSpec.PARSER_DONE_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_done));
        Bitset nextList = ApiUtils.concat(ForgeSpec.PARSER_NEXT_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_next));
        Bitset shftList = ApiUtils.concat(ForgeSpec.PARSER_SHFT_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_shift));

        Bitset doffList = ApiUtils.concat(ForgeSpec.PARSER_DOFF_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_doff));
        Bitset dvldList = ApiUtils.concat(ForgeSpec.PARSER_DVLD_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_dvld));
        Bitset flagList = ApiUtils.concat(ForgeSpec.PARSER_FLAG_BIT_SIZE, Utils.listTokenizerToBitsetList(sram_flag));
        Bitset alueList = ApiUtils.concat(ForgeSpec.PARSER_ALUE_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alue));
        Bitset aluiList = ApiUtils.concat(ForgeSpec.PARSER_ALUI_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alui_opcode));
        Bitset alui1List = ApiUtils.concat(ForgeSpec.PARSER_ALUI_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alui_op0));
        Bitset alui2List = ApiUtils.concat(ForgeSpec.PARSER_ALUI_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alui_mask));
        Bitset alui3List = ApiUtils.concat(ForgeSpec.PARSER_ALUI_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alui_op1));
        Bitset alui4List = ApiUtils.concat(ForgeSpec.PARSER_ALUI_BIT_SIZE, Utils.listTokenizerToBitsetList(sres_alui_op2));
        //Bitset KVLD,Bitset FMSK,Bitset CMSK,Bitset DMSK,Bitset FDAT,Bitset CDAT,Bitset DDAT,Bitset DONE,Bitset NEXT,Bitset SHFT,Bitset DOFF,Bitset DVLD,Bitset FLAG,Bitset ALUE,Bitset ALUI){
        Bitset alui = new Bitset(ForgeSpec.PARSER_ALUI_WORDLENGTH, aluiList.toString().concat(alui1List.toString()).concat(alui2List.toString()).
                concat(alui3List.toString().concat(alui4List.toString())));
        return ConfigApis.concat_fields_parser_parser_sram_0(doneList, nextList, shftList, doffList, dvldList, flagList, alueList, alui);

    }

    @Override
    public String toString() {
        //TODO
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public void generate() {
        update();
    }

    private void update() {
        if (rows == null || rows.isEmpty())
            throw new RuntimeException("ParserRow needs to be set before invoking parser generate method");
        Map<String, List<Field>> classmap = ParserRow.getClassmap();
        populateConfigCache(rows, classmap);
        generateConfig();
    }

    private void populateConfigCache(List<ParserRow> rows, Map<String, List<Field>> classmap) {
        for (Map.Entry<String, List<Field>> e : classmap.entrySet()) {
            List<FW> conf = new ArrayList<FW>();
            for (Field f : e.getValue()) {
                for (ParserRow row : rows) {
                    try {
                        Annotation a = f.getAnnotation(Row.class);
                        if (a != null) {
                            Row r = (Row) a;
                            if (r.type() == RowType.Single) {
                                FW val = (FW) f.get(row);
                                conf.add(val);
                            } else if (r.type() == RowType.List) {
                                List<FW> val = (List<FW>) f.get(row);
                                if (r.reverse())
                                    Collections.reverse(val);
                                for (FW n : val) {
                                    List<FW> vals1 = new ArrayList<>();
                                    vals1.add(n);
                                    List<FW> vals = r.noByte() ? vals1 : n.byteOrder();
                                    for (FW n1 : vals) {
                                        FW n2 = n1;
                                        if (r.alwaysByte()) {
                                            n2 = n1.byteAlign();
                                        }
                                        conf.add(n2);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException();
                    }
                }
            }
            configcache.put(e.getKey(), conf);
        }
    }

    private void generateConfig() {
        //Referring Lists for access.
        List<FW> sram_done_vals = configcache.get("sram_done.cfg");
        List<FW> sram_next_vals = configcache.get("sram_next.cfg");
        List<FW> sram_shift_vals = configcache.get("sram_shift.cfg");
        List<FW> sram_doff_vals = configcache.get("sram_doff.cfg");
        List<FW> sram_dvld_vals = configcache.get("sram_dvld.cfg");
        List<FW> sram_flag_vals = configcache.get("sram_flag.cfg");
        List<FW> sres_alue_vals = configcache.get("sres_alue.cfg");
        List<FW> sres_alui_opcode_vals = configcache.get("sres_alui_opcode.cfg");
        List<FW> sres_alui_op0_vals = configcache.get("sres_alui_op0.cfg");
        List<FW> sres_alui_mask_vals = configcache.get("sres_alui_mask.cfg");
        List<FW> sres_alui_op1_vals = configcache.get("sres_alui_op1.cfg");
        List<FW> sres_alui_op2_vals = configcache.get("sres_alui_op2.cfg");

        //Size of Config Lists
        int sram_done_len = sram_done_vals.size();
        int sram_next_len = sram_next_vals.size();
        int sram_shift_len = sram_shift_vals.size();
        int sram_doff_len = sram_doff_vals.size();
        int sram_dvld_len = sram_dvld_vals.size();
        int sram_flag_len = sram_flag_vals.size();
        int sres_alue_len = sres_alue_vals.size();
        int sres_alui_opcode_len = sres_alui_opcode_vals.size();
        int sres_alui_op0_len = sres_alui_op0_vals.size();
        int sres_alui_mask_len = sres_alui_mask_vals.size();
        int sres_alui_op1_len = sres_alui_op1_vals.size();
        int sres_alui_op2_len = sres_alui_op2_vals.size();

        int words =
                (ForgeSpec.DEPARSER_DONE_BIT_SIZE * sram_done_len) / ForgeSpec.DEPARSER_DONE_WORDLENGTH;
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();
        Sram record = Sram.getInstance();

        for (int word = 0; word < words; word++) {
            List<FW> sram_done = Utils.listTokeinizer(sram_done_vals, sram_done_len / words, word);
            List<FW> sram_next = Utils.listTokeinizer(sram_next_vals, sram_next_len / words, word);
            List<FW> sram_shift = Utils.listTokeinizer(sram_shift_vals, sram_shift_len / words, word);
            List<FW> sram_doff = Utils.listTokeinizer(sram_doff_vals, sram_doff_len / words, word);
            List<FW> sram_dvld = Utils.listTokeinizer(sram_dvld_vals, sram_dvld_len / words, word);
            List<FW> sram_flag = Utils.listTokeinizer(sram_flag_vals, sram_flag_len / words, word);
            List<FW> sres_alue = Utils.listTokeinizer(sres_alue_vals, sres_alue_len / words, word);
            List<FW> sres_alui_opcode = Utils.listTokeinizer(sres_alui_opcode_vals, sres_alui_opcode_len / words, word);
            List<FW> sres_alui_op0 = Utils.listTokeinizer(sres_alui_op0_vals, sres_alui_op0_len / words, word);
            List<FW> sres_alui_mask = Utils.listTokeinizer(sres_alui_mask_vals, sres_alui_mask_len / words, word);
            List<FW> sres_alui_op1 = Utils.listTokeinizer(sres_alui_op1_vals, sres_alui_op1_len / words, word);
            List<FW> sres_alui_op2 = Utils.listTokeinizer(sres_alui_op2_vals, sres_alui_op2_len / words, word);

            record.setSram_doff(sram_doff);
            record.setSram_done(sram_done);
            record.setSram_dvld(sram_dvld);
            record.setSram_flag(sram_flag);
            record.setSram_next(sram_next);
            record.setSram_shift(sram_shift);
            record.setSres_alue(sres_alue);
            record.setSres_alui_mask(sres_alui_mask);
            record.setSres_alui_op0(sres_alui_op0);
            record.setSres_alui_op1(sres_alui_op1);
            record.setSres_alui_op2(sres_alui_op2);
            record.setSres_alui_opcode(sres_alui_opcode);

            records.add(record.summary());

        }

        DrmtConfig config = DrmtConfig.getInstance();
        Parser parser = Parser.getInstance();
        parser.setTcam(Utils.flattenNestedList(records));
        config.setPa(parser);

    }
}
