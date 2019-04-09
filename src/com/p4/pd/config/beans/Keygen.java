package com.p4.pd.config.beans;

import com.p4.drmt.parser.FW;
import com.p4.drmt.parser.KeyRow;
import com.p4.drmt.parser.Row;
import com.p4.drmt.parser.RowType;
import com.p4.pd.config.common.*;
import lombok.Data;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


@Data
public class Keygen implements Configurable {
    private List<FW> ybyt;
    private List<FW> ybit;
    private List<FW> ymsk;
    private List<FW> ktbl;
    private List<FW> kseg;
    private List<FW> kvld;
    //Needs to be Set, before Invoking Keygen Configuration generate method.
    private List<KeyRow> rows;
    //Retrieved from KeyRow
    private static Map<String, List<FW>> configcache = new HashMap<String, List<FW>>();
    //Instance reference pointing to Single Keygen Object
    private static Keygen instance;
    //Private Constructor
    private Keygen() {}
    //Eager Instantiation with Class Loader.
    static {
        instance = new Keygen();
    }
    //Handler to get Instance of Keygen Object
    public static Keygen getInstance() {
        return instance;
    }

    @Override
    public List<Bitset> summary(){
        //Invocation to low level apis and return List of Integers with reference pointing to info.
        Bitset ybitList = ApiUtils.concat(ForgeSpec.KEYGEN_YBIT_BIT_SIZE,Utils.listTokenizerToBitsetList(ybit));
        Bitset ybytList = ApiUtils.concat(ForgeSpec.KEYGEN_YBYT_BIT_SIZE,Utils.listTokenizerToBitsetList(ybyt));
        Bitset ymskList = ApiUtils.concat(ForgeSpec.KEYGEN_YMSK_BIT_SIZE,Utils.listTokenizerToBitsetList(ymsk));
        Bitset kvldList = ApiUtils.concat(ForgeSpec.KEYGEN_KVLD_BIT_SIZE,Utils.listTokenizerToBitsetList(kvld));
        Bitset ksegList = ApiUtils.concat(ForgeSpec.KEYGEN_KSEG_BIT_SIZE,Utils.listTokenizerToBitsetList(kseg));
        Bitset ktblList = ApiUtils.concat(ForgeSpec.KEYGEN_KTBL_BIT_SIZE,Utils.listTokenizerToBitsetList(ktbl));
        return ConfigApis.concat_fields_drmt_dist_keygen_0(ybytList,ybitList,ymskList,ktblList,ksegList,kvldList);
    }

    @Override
    public String toString() {
        //TODO
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public void generate(){
        update();
    }

    private void update() {
        if (rows == null || rows.isEmpty())
            throw new RuntimeException("KeyRows needs to be set before invoking keygen generate method");
        Map<String, List<Field>> classmap = KeyRow.getClassMap();
        populateConfigCache(rows, classmap);
        generateConfig();
    }

    private void populateConfigCache(List<KeyRow> rows, Map<String, List<Field>> classmap) {
        for (Map.Entry<String, List<Field>> e : classmap.entrySet()) {
            List<FW> conf = new ArrayList<FW>();
            for (Field f : e.getValue()) {
                for (KeyRow row : rows) {
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
                                    List<FW> vals = n.byteOrder();
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

    /**
     * Config to generate JSON File with Row wise Layout for Keygen
     */
    private static void generateConfig(){
        //Referring Lists for access.
        final List<FW> KSEG_LIST = configcache.get("kseg.cfg");
        final List<FW> KVLD_LIST = configcache.get("kvld.cfg");
        final List<FW> KTBL_LIST = configcache.get("ktbl.cfg");
        final List<FW> YBIT_LIST = configcache.get("ybit.cfg");
        final List<FW> YBYT_LIST = configcache.get("ybyt.cfg");
        final List<FW> YMSK_LIST = configcache.get("ymsk.cfg");

        final int KSEG_LEN = KSEG_LIST.size();
        final int KVLD_LEN = KVLD_LIST.size();
        final int KTBL_LEN = KTBL_LIST.size();
        final int YBIT_LEN = YBIT_LIST.size();
        final int YBYT_LEN = YBYT_LIST.size();
        final int YMSK_LEN = YMSK_LIST.size();


        int numWords = (YBYT_LEN * ForgeSpec.KEYGEN_YBYT_BIT_SIZE)/ ForgeSpec.KEYGEN_YBYT_WORDLENGTH;

        //Get Hold of Keygen Object.
        Keygen keygenRecord = Keygen.getInstance();
        List<List<Bitset>> records = new ArrayList<List<Bitset>>();

        for(int word =0; word < numWords; word++){
            List<FW> kseg = Utils.listTokeinizer(KSEG_LIST,KSEG_LEN/numWords, word);
            List<FW> ktbl = Utils.listTokeinizer(KTBL_LIST,KTBL_LEN/numWords, word);
            List<FW> kvld = Utils.listTokeinizer(KVLD_LIST,KVLD_LEN/numWords, word);
            List<FW> ybit = Utils.listTokeinizer(YBIT_LIST,YBIT_LEN/numWords, word);
            List<FW> ybyt = Utils.listTokeinizer(YBYT_LIST,YBYT_LEN/numWords, word);
            List<FW> ymsk = Utils.listTokeinizer(YMSK_LIST,YMSK_LEN/numWords, word);

            keygenRecord.setKseg(kseg);
            keygenRecord.setKtbl(ktbl);
            keygenRecord.setKvld(kvld);
            keygenRecord.setYbit(ybit);
            keygenRecord.setYbyt(ybyt);
            keygenRecord.setYmsk(ymsk);

            List<Bitset> record = keygenRecord.summary();
            records.add(record);
        }
        Processor processor = Processor.getInstance();
        processor.setKeygen(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records))));
        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
        DrmtConfig.getInstance().setDrmt(drmt);
//        try {
//            Utils.writeConfigs();
//        }catch (IOException e){
//            System.err.print("File cannot be written");
//        }
    }
}
