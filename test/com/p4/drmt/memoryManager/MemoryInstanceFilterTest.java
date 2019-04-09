package com.p4.drmt.memoryManager;

import com.p4.drmt.memoryManager.AlignedMemoryAllocatorStrategy.MemoryInstanceFilter;
import com.p4.drmt.memoryManager.AlignedMemoryAllocatorStrategyTest.TestMemoryInstance;
import com.p4.ds.DisjointSet;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Target.MemoryType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


public class MemoryInstanceFilterTest {


    private static List<IDisjointSet<IMemoryInstance>> memoryInstances = null;

    @BeforeClass
    public static void setup(){
        memoryInstances = createSampleDisjointSetData();
    }


    @Test
    public void testFilterBySizeOneAndMemoryTypePktBit() {

        testFilterBySizeAndMemoryType(1, MemoryType.TypePktBit, 1, 2, 2);

    }

    @Test
    public void testFilterBySizeOneAndMemoryTypePktByte() {

        testFilterBySizeAndMemoryType(1, MemoryType.TypePktByte, 2, 3, 6);

    }

    @Test
    public void testFilterBySizeTwoAndMemoryTypePktByte() {

        testFilterBySizeAndMemoryType(2, MemoryType.TypePktByte, 2, 3, 6);

    }

    @Test
    public void testFilterBySizeThreeAndMemoryTypePktByte() {

        testFilterBySizeAndMemoryType(3, MemoryType.TypePktByte, 3, 5, 14);

    }



    @Test
    public void testFilterBySizeOneAndMemoryTypeCfgBit() {

        testFilterBySizeAndMemoryType(1, MemoryType.TypeCfgBit, 1, 1, 1);

    }

    @Test
    public void testFilterBySizeOneAndMemoryTypeCfgByte() {

        testFilterBySizeAndMemoryType(1, MemoryType.TypeCfgByte, 0, 0, 0);

    }

    @Test
    public void testFilterBySizeTwoAndMemoryTypeCfgByte() {

        testFilterBySizeAndMemoryType(2, MemoryType.TypeCfgByte, 1, 1, 3);

    }

    @Test
    public void testFilterBySizeThreeAndMemoryTypeCfgByte() {

        testFilterBySizeAndMemoryType(3, MemoryType.TypeCfgByte, 1, 1, 2);

    }





    private void testFilterBySizeAndMemoryType(int size, MemoryType memoryType,
                                               int expectedDisjointSetsCount,
                                               int expectedEquivalenceSetsCount,
                                               int expectedMemoryInstancesCount) {

        List<IDisjointSet<IMemoryInstance>> filteredMemoryInstancesOfSizeOne = MemoryInstanceFilter.filterBySizeAndMemoryType(memoryInstances, size, memoryType);


        int actualDisjointSetsCount = filteredMemoryInstancesOfSizeOne.size();
        int actualEquivalenceSetsCount = 0;
        int actualMemoryInstancesCount = 0;
        for (IDisjointSet<IMemoryInstance> disjointSet : filteredMemoryInstancesOfSizeOne) {

            for (Set<IMemoryInstance> memoryInstanceSet : disjointSet.getEquivalenceSets()) {
                actualEquivalenceSetsCount++;
                for (IMemoryInstance memoryInstance : memoryInstanceSet) {
                    actualMemoryInstancesCount++;
                    if (size <=2) {
                        Assert.assertEquals("Memory Instance size should be one", size, memoryInstance.getSize());
                    }else {
                        Assert.assertTrue("Memory Instance size should be one", size <= memoryInstance.getSize());
                    }
                }
            }
        }

        Assert.assertEquals("Mismatch in Disjoint Sets Count", expectedDisjointSetsCount, actualDisjointSetsCount);
        Assert.assertEquals("Mismatch in Equivalence Sets Count", expectedEquivalenceSetsCount, actualEquivalenceSetsCount);
        Assert.assertEquals("Mismatch in Memory Instances Count", expectedMemoryInstancesCount, actualMemoryInstancesCount);


    }


    protected static List<IDisjointSet<IMemoryInstance>> createSampleDisjointSetData() {

        int index = 0;

        //DisjointSet1
        //set
        IMemoryInstance hdr_ip1_ipv4_srcAddr = new TestMemoryInstance(++index, "hdr.ip1.ipv4.srcAddr", 4, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_dstAddr = new TestMemoryInstance(++index, "hdr.ip1.ipv4.dstAddr", 4, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_version = new TestMemoryInstance(++index, "hdr.ip1.ipv4.version", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_ihl = new TestMemoryInstance(++index, "hdr.ip1.ipv4.ihl", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_totalLen = new TestMemoryInstance(++index, "hdr.ip1.ipv4.totalLen", 2, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_identification = new TestMemoryInstance(++index, "hdr.ip1.ipv4.identification", 2, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv4_x = new TestMemoryInstance(++index, "hdr.ip1.ipv4.x", 1, MemoryType.TypePktBit); //dummy
        //set
        IMemoryInstance hdr_ip1_ipv6_srcAddr = new TestMemoryInstance(++index, "hdr.ip1.ipv6.srcAddr", 16, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_dstAddr = new TestMemoryInstance(++index, "hdr.ip1.ipv6.dstAddr", 16, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_version = new TestMemoryInstance(++index, "hdr.ip1.ipv6.version", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_trafficClass = new TestMemoryInstance(++index, "hdr.ip1.ipv6.trafficClass", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_flowLabel = new TestMemoryInstance(++index, "hdr.ip1.ipv6.flowLabel", 3, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_payloadLen = new TestMemoryInstance(++index, "hdr.ip1.ipv6.payloadLen", 2, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip1_ipv6_x = new TestMemoryInstance(++index, "hdr.ip1.ipv6.x", 1, MemoryType.TypePktBit); //dummy

        //DisjointSet2
        //set
        IMemoryInstance hdr_ip2_ipv4_srcAddr = new TestMemoryInstance(++index, "hdr.ip2.ipv4.srcAddr", 4, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip2_ipv4_dstAddr = new TestMemoryInstance(++index, "hdr.ip2.ipv4.dstAddr", 4, MemoryType.TypePktByte);
        //set
        IMemoryInstance hdr_ip2_ipv6_srcAddr = new TestMemoryInstance(++index, "hdr.ip2.ipv6.srcAddr", 16, MemoryType.TypePktByte);
        IMemoryInstance hdr_ip2_ipv6_dstAddr = new TestMemoryInstance(++index, "hdr.ip2.ipv6.dstAddr", 16, MemoryType.TypePktByte);

        //DisjointSet3
        //set
        IMemoryInstance hdr_ipv4_srcAddr = new TestMemoryInstance(++index, "hdr.ipv4.srcAddr", 4, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv4_dstAddr = new TestMemoryInstance(++index, "hdr.ipv4.dstAddr", 4, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv6_srcAddr = new TestMemoryInstance(++index, "hdr.ipv6.srcAddr", 16, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv6_dstAddr = new TestMemoryInstance(++index, "hdr.ipv6.dstAddr", 16, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv4_ihl = new TestMemoryInstance(++index, "hdr.ipv4.ihl", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv4_totalLen = new TestMemoryInstance(++index, "hdr.ipv4.totalLen", 2, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv4_identification = new TestMemoryInstance(++index, "hdr.ipv4.identification", 2, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv6_trafficClass = new TestMemoryInstance(++index, "hdr.ipv6.trafficClass", 1, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv6_flowLabel = new TestMemoryInstance(++index, "hdr.ipv6.flowLabel", 3, MemoryType.TypePktByte);
        IMemoryInstance hdr_ipv6_payloadLen = new TestMemoryInstance(++index, "hdr.ipv6.payloadLen", 2, MemoryType.TypePktByte);

        IMemoryInstance var_1 = new TestMemoryInstance(++index, "1", 1, MemoryType.TypeCfgBit);
        IMemoryInstance var_1023 = new TestMemoryInstance(++index, "1023", 2, MemoryType.TypeCfgByte);
        IMemoryInstance var_1024 = new TestMemoryInstance(++index, "1024", 2, MemoryType.TypeCfgByte);
        IMemoryInstance var_1025 = new TestMemoryInstance(++index, "1025", 2, MemoryType.TypeCfgByte);
        IMemoryInstance var_123456 = new TestMemoryInstance(++index, "123456", 4, MemoryType.TypeCfgByte);
        IMemoryInstance var_123456789 = new TestMemoryInstance(++index, "123456789", 6, MemoryType.TypeCfgByte);



        List<IDisjointSet<IMemoryInstance>> disjointSets = new ArrayList<>();

        //disjointSet1
        IDisjointSet<IMemoryInstance> disjointSet1 = new DisjointSet<>();
        List<IMemoryInstance> set1 = new ArrayList<>();
        set1.add(hdr_ip1_ipv4_srcAddr);
        set1.add(hdr_ip1_ipv4_dstAddr);
        set1.add(hdr_ip1_ipv4_version);
        set1.add(hdr_ip1_ipv4_ihl);
        set1.add(hdr_ip1_ipv4_totalLen);
        set1.add(hdr_ip1_ipv4_identification);
        set1.add(hdr_ip1_ipv4_x);

        List<IMemoryInstance> set2 = new ArrayList<>();
        set2.add(hdr_ip1_ipv6_srcAddr);
        set2.add(hdr_ip1_ipv6_dstAddr);
        set2.add(hdr_ip1_ipv6_version);
        set2.add(hdr_ip1_ipv6_trafficClass);
        set2.add(hdr_ip1_ipv6_flowLabel);
        set2.add(hdr_ip1_ipv6_payloadLen);
        set2.add(hdr_ip1_ipv6_x);

        disjointSet1.addEquivalenceSet(set1);
        disjointSet1.addEquivalenceSet(set2);
        disjointSets.add(disjointSet1);


        //disjointSet2
        IDisjointSet<IMemoryInstance> disjointSet2 = new DisjointSet<>();
        List<IMemoryInstance> set3 = new ArrayList<>();
        set3.add(hdr_ip2_ipv4_srcAddr);
        set3.add(hdr_ip2_ipv4_dstAddr);

        List<IMemoryInstance> set4 = new ArrayList<>();
        set4.add(hdr_ip2_ipv6_srcAddr);
        set4.add(hdr_ip2_ipv6_dstAddr);

        disjointSet2.addEquivalenceSet(set3);
        disjointSet2.addEquivalenceSet(set4);
        disjointSets.add(disjointSet2);


        //disjointSet3
        IDisjointSet<IMemoryInstance> disjointSet3 = new DisjointSet<>();
        List<IMemoryInstance> set5 = new ArrayList<>();
        set5.add(hdr_ipv4_srcAddr);
        set5.add(hdr_ipv4_dstAddr);
        set5.add(hdr_ipv6_srcAddr);
        set5.add(hdr_ipv6_dstAddr);
        set5.add(hdr_ipv4_ihl);
        set5.add(hdr_ipv4_totalLen);
        set5.add(hdr_ipv4_identification);
        set5.add(hdr_ipv6_trafficClass);
        set5.add(hdr_ipv6_flowLabel);
        set5.add(hdr_ipv6_payloadLen);
        set5.add(var_1);
        set5.add(var_1023);
        set5.add(var_1024);
        set5.add(var_1025);
        set5.add(var_123456);
        set5.add(var_123456789);

        disjointSet3.addEquivalenceSet(set5);
        disjointSets.add(disjointSet3);


        return disjointSets;
    }

    @AfterClass
    public static void tearDown(){
        memoryInstances = null;
    }

}