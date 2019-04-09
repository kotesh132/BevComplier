package com.p4.pd.config.beans;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.drmt.schd.NewScheduler;
import com.p4.drmt.schd.SchLengthUnit;
import com.p4.pd.config.common.*;
import com.p4.pktgen.config.Config;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Data
public class Scheduler implements Configurable{
    private List<FW> sch_node;
    private List<FW> sch_wait;
    private List<FW> sch_nidx;
    private List<FW> sch_flag;

    private static Scheduler instance;
    private Scheduler(){}
    static{
        instance = new Scheduler();
    }
    public static Scheduler getInstance(){
        return instance;
    }

    @Override
    public List<Bitset> summary(){
        Bitset sch_nodeList = ApiUtils.concat(ForgeSpec.SCHEDULER_SCH_NODE_BIT_SIZE, Utils.listTokenizerToBitsetList(sch_node));
        Bitset sch_waitList = ApiUtils.concat(ForgeSpec.SCHEDULER_SCH_WAIT_BIT_SIZE, Utils.listTokenizerToBitsetList(sch_wait));
        Bitset sch_nidxList = ApiUtils.concat(ForgeSpec.SCHEDULER_SCH_NIDX_BIT_SIZE, Utils.listTokenizerToBitsetList(sch_nidx));
        Bitset sch_flagList = ApiUtils.concat(ForgeSpec.SCHEDULER_SCH_FLAG_BIT_SIZE, Utils.listTokenizerToBitsetList(sch_flag));
        return ConfigApis.concat_fields_drmt_dist_sch_0(sch_nodeList, sch_waitList, sch_nidxList, sch_flagList);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public void generate(){
        update();
    }


    private void update(){

        List<SchLengthUnit> allConfigs = NewScheduler.getSchList();

        Scheduler record = Scheduler.getInstance();
        List<List<Bitset>> records  = new ArrayList<List<Bitset>>();
        //As every element in all Configs emit one single Word, so no need to compute sizes and retrieve words
        for(SchLengthUnit schObj: allConfigs) {
            //SchLengthUnit schObj = new SchLengthUnit();
            List<ConfigEmitUnit> configs = schObj.getAllConfigs();
            //Get List of Configs from getAllConfigs and based on order.
            //For more Info look for getAllConfigs method.
            List<FW> sch_node_list = configs.get(0).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<FW> sch_nidx_list = configs.get(1).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<FW> sch_flag_list = configs.get(2).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<FW> sch_wait_list = configs.get(3).getItems().stream().flatMap(Collection::stream).collect(Collectors.toList());

            record.setSch_flag(sch_flag_list);
            record.setSch_nidx(sch_nidx_list);
            record.setSch_node(sch_node_list);
            record.setSch_wait(sch_wait_list);

            records.add(record.summary());
        }
        Processor processor = Processor.getInstance();
        processor.setSch(Utils.generateConfigCharsList(Utils.transformBitsetToLong(Utils.flattenNestedList(records))));
        Drmt drmt = Drmt.getInstance();
        drmt.setProcessor(processor);
    }
}
