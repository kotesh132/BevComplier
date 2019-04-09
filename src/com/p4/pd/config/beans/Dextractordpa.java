package com.p4.pd.config.beans;

import com.p4.drmt.parser.ConfigUtil;
import com.p4.drmt.parser.ExtractorConfigUnit;
import com.p4.drmt.parser.FW;
import com.p4.pd.config.common.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BharatJ
 */
@Data
public class Dextractordpa implements Configurable {
    private List<FW> vbit;
    private List<FW> cbit;
    private List<FW> xbit;
    private List<FW> ybit;
    private List<FW> vbyt;
    private List<FW> cbyt;
    private List<FW> xbyt;
    private List<FW> ybyt;
    private List<FW> ymsk;
    private List<FW> xsft;
    private List<FW> xdir;


    private static Dextractordpa instance;

    private Dextractordpa() {
    }

    static {
        instance = new Dextractordpa();
    }

    public static Dextractordpa getInstance() {
        return instance;
    }

    @Override
    public List<Bitset> summary() {

        Bitset vbitList = ApiUtils.concat(ForgeSpec.DEPARSER_VBIT_BIT_SIZE, Utils.listTokenizerToBitsetList(vbit));
        Bitset cbitList = ApiUtils.concat(ForgeSpec.DEPARSER_CBIT_BIT_SIZE, Utils.listTokenizerToBitsetList(cbit));
        Bitset xbitList = ApiUtils.concat(ForgeSpec.DEPARSER_XBIT_BIT_SIZE, Utils.listTokenizerToBitsetList(xbit));
        Bitset ybitList = ApiUtils.concat(ForgeSpec.DEPARSER_YBIT_BIT_SIZE, Utils.listTokenizerToBitsetList(ybit));
        Bitset vbytList = ApiUtils.concat(ForgeSpec.DEPARSER_VBYT_BIT_SIZE, Utils.listTokenizerToBitsetList(vbyt));
        Bitset cbytList = ApiUtils.concat(ForgeSpec.DEPARSER_CBYT_BIT_SIZE, Utils.listTokenizerToBitsetList(cbyt));
        Bitset xbytList = ApiUtils.concat(ForgeSpec.DEPARSER_XBYT_BIT_SIZE, Utils.listTokenizerToBitsetList(xbyt));
        Bitset ybytList = ApiUtils.concat(ForgeSpec.DEPARSER_YBYT_BIT_SIZE, Utils.listTokenizerToBitsetList(ybyt));
        Bitset ymskList = ApiUtils.concat(ForgeSpec.DEPARSER_YMSK_BIT_SIZE, Utils.listTokenizerToBitsetList(ymsk));
        Bitset xsftList = ApiUtils.concat(ForgeSpec.DEPARSER_XSFT_BIT_SIZE, Utils.listTokenizerToBitsetList(xsft));
        Bitset xdirList = ApiUtils.concat(ForgeSpec.DEPARSER_XDIR_BIT_SIZE, Utils.listTokenizerToBitsetList(xdir));
        return ConfigApis.concat_fields_deparser_dextractor_ctrl_cfg_0(vbitList, cbitList, xbitList, ybitList,
                vbytList, cbytList, xbytList, ybytList, ymskList, xsftList, xdirList);

    }

    @Override
    public String toString() {
        //TODO
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public void generate() {

        List<ExtractorConfigUnit> allConfigs;
        allConfigs = ConfigUtil.getDexList();

        for (ExtractorConfigUnit configs : allConfigs) {
            //Get List of Configs from getAllConfigs and based on order.
            //For more Info look for getAllConfigs method.
            List<FW> ybyt_list = configs.getYbyt().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> xbyt_list = configs.getXbyt().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> ymsk_list = configs.getYmsk().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> xsft_list = configs.getXsft().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> xdir_list = configs.getXdir().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> vbyt_list = configs.getVbyt().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> cbyt_list = configs.getCbyt().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> ybit_list = configs.getYbit().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> xbit_list = configs.getXbit().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> vbit_list = configs.getVbit().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;
            List<FW> cbit_list = configs.getCbit().getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            ;

            int words = (ForgeSpec.DEPARSER_YBIT_BIT_SIZE * ybit_list.size()) / ForgeSpec.DEPARSER_YBIT_WORDLENGTH;

            List<List<Bitset>> records = new ArrayList<>();
            Dextractordpa record = getInstance();

            for (int word = 0; word < words; word++) {
                List<FW> ybyt = Utils.listTokeinizer(ybyt_list, ybyt_list.size() / words, word);
                List<FW> xbyt = Utils.listTokeinizer(xbyt_list, xbyt_list.size() / words, word);
                List<FW> ymsk = Utils.listTokeinizer(ymsk_list, ymsk_list.size() / words, word);
                List<FW> xsft = Utils.listTokeinizer(xsft_list, xsft_list.size() / words, word);
                List<FW> xdir = Utils.listTokeinizer(xdir_list, xdir_list.size() / words, word);
                List<FW> vbyt = Utils.listTokeinizer(vbyt_list, vbyt_list.size() / words, word);
                List<FW> cbyt = Utils.listTokeinizer(cbyt_list, cbyt_list.size() / words, word);
                List<FW> ybit = Utils.listTokeinizer(ybit_list, ybit_list.size() / words, word);
                List<FW> xbit = Utils.listTokeinizer(xbit_list, xbit_list.size() / words, word);
                List<FW> vbit = Utils.listTokeinizer(vbit_list, vbit_list.size() / words, word);
                List<FW> cbit = Utils.listTokeinizer(cbit_list, cbit_list.size() / words, word);


                record.setCbit(cbit);
                record.setCbyt(cbyt);
                record.setVbit(vbit);
                record.setVbyt(vbyt);
                record.setXbit(xbit);
                record.setXbyt(xbyt);
                record.setXdir(xdir);
                record.setYbit(ybit);
                record.setYbyt(ybyt);
                record.setYmsk(ymsk);
                record.setXsft(xsft);
                records.add(record.summary());

            }

            DrmtConfig config = DrmtConfig.getInstance();
            Deparser deparser = Deparser.getInstance();
            deparser.setDex(Utils.flattenNestedList(records));
            config.setDpa(deparser);
        }
    }


}
