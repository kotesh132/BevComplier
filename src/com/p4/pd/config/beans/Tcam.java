package com.p4.pd.config.beans;

import com.p4.drmt.parser.FW;
import com.p4.drmt.parser.ParserRow;
import com.p4.drmt.parser.Row;
import com.p4.drmt.parser.RowType;
import com.p4.pd.config.common.*;
import com.p4.pktgen.config.Config;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Data
public class Tcam implements Configurable {

    private List<FW> tcam_vld; //KVLD
    private List<FW> tcam_mask_flag; //FMSK
    private List<FW> tcam_mask_curr; //CMSK
    private List<FW> tcam_mask_key; //DMSK
    private List<FW> tcam_data_flag; //FDAT
    private List<FW> tcam_data_curr; //CDAT
    private List<FW> tcam_data_key; //DDAT

    //Needs to be Set, before Invoking Parser Configuration generate method.
    private List<ParserRow> rows;

    //Retrieved from KeyRow
    private static Map<String, List<FW>> configcache = new HashMap<String, List<FW>>();


    private static Tcam instance;

    private Tcam() {
    }

    static {
        instance = new Tcam();
    }

    public static Tcam getInstance() {
        return instance;
    }

    @Override
    public List<Bitset> summary() {
        Bitset kvldList = ApiUtils.concat(ForgeSpec.PARSER_KVLD_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_vld));
        Bitset fmskList = ApiUtils.concat(ForgeSpec.PARSER_FMSK_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_mask_flag));
        Bitset cmskList = ApiUtils.concat(ForgeSpec.PARSER_CMSK_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_mask_curr));
        Bitset dmskList = ApiUtils.concat(ForgeSpec.PARSER_DMSK_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_mask_key));
        Bitset fdatList = ApiUtils.concat(ForgeSpec.PARSER_FDAT_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_data_flag));

        Bitset cdatList = ApiUtils.concat(ForgeSpec.PARSER_CDAT_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_data_curr));
        Bitset ddatList = ApiUtils.concat(ForgeSpec.PARSER_DDAT_BIT_SIZE, Utils.listTokenizerToBitsetList(tcam_data_key));
        //Bitset KVLD,Bitset FMSK,Bitset CMSK,Bitset DMSK,Bitset FDAT,Bitset CDAT,Bitset DDAT,Bitset DONE,Bitset NEXT,Bitset SHFT,Bitset DOFF,Bitset DVLD,Bitset FLAG,Bitset ALUE,Bitset ALUI){
        return ConfigApis.concat_fields_parser_parser_tcam_ctrl_cfg_0(kvldList, fmskList, cmskList, dmskList, fdatList,
                cdatList, ddatList);

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
        List<FW> tcam_vld_vals = configcache.get("tcam_vld.cfg");
        List<FW> tcam_mask_flag_vals = configcache.get("tcam_mask_flag.cfg");
        List<FW> tcam_mask_curr_vals = configcache.get("tcam_mask_curr.cfg");
        List<FW> tcam_mask_key_vals = configcache.get("tcam_mask_key.cfg");
        List<FW> tcam_data_flag_vals = configcache.get("tcam_data_flag.cfg");
        List<FW> tcam_data_curr_vals = configcache.get("tcam_data_curr.cfg");
        List<FW> tcam_data_key_vals = configcache.get("tcam_data_key.cfg");
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
        int tcam_vld_len = tcam_vld_vals.size();
        int tcam_mask_flag_len = tcam_mask_flag_vals.size();
        int tcam_mask_curr_len = tcam_mask_curr_vals.size();
        int tcam_mask_key_len = tcam_mask_key_vals.size();
        int tcam_data_flag_len = tcam_data_flag_vals.size();
        int tcam_data_curr_len = tcam_data_curr_vals.size();
        int tcam_data_key_len = tcam_data_key_vals.size();
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

        int words = (ForgeSpec.DEPARSER_KVLD_BIT_SIZE * tcam_vld_len) / ForgeSpec.DEPARSER_KVLD_WORDLENGTH;
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();
        Tcam record = Tcam.getInstance();

        for (int word = 0; word < words; word++) {
            List<FW> tcam_vld = Utils.listTokeinizer(tcam_vld_vals, tcam_vld_len / words, word);
            List<FW> tcam_mask_flag = Utils.listTokeinizer(tcam_mask_flag_vals, tcam_mask_flag_len / words, word);
            List<FW> tcam_mask_curr = Utils.listTokeinizer(tcam_mask_curr_vals, tcam_mask_curr_len / words, word);
            List<FW> tcam_mask_key = Utils.listTokeinizer(tcam_mask_key_vals, tcam_mask_key_len / words, word);
            List<FW> tcam_data_flag = Utils.listTokeinizer(tcam_data_flag_vals, tcam_data_flag_len / words, word);
            List<FW> tcam_data_curr = Utils.listTokeinizer(tcam_data_curr_vals, tcam_data_curr_len / words, word);
            List<FW> tcam_data_key = Utils.listTokeinizer(tcam_data_key_vals, tcam_data_key_len / words, word);
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

            record.setTcam_data_curr(tcam_data_curr);
            record.setTcam_data_flag(tcam_data_flag);
            record.setTcam_data_key(tcam_data_key);
            record.setTcam_mask_curr(tcam_mask_curr);
            record.setTcam_mask_key(tcam_mask_key);
            record.setTcam_vld(tcam_vld);
            record.setTcam_mask_flag(tcam_mask_flag);

            records.add(record.summary());

        }

        DrmtConfig config = DrmtConfig.getInstance();
        Parser parser = Parser.getInstance();
        parser.setTcam(Utils.flattenNestedList(records));
        config.setPa(parser);

    }
}
