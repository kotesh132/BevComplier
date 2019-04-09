package com.p4.drmt.memoryManager;

import com.p4.drmt.memoryManager.IAlignedMemoryAllocatorStrategy.AlignmentType;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Target.MemoryType;
import com.p4.p416.applications.Type;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class AlignedMemoryAllocatorStrategyTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void testGetNextAvailableSlot() {

        int nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.ONE, 0);
        Assert.assertEquals(0, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.ONE, 1);
        Assert.assertEquals(1, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.TWO, 0);
        Assert.assertEquals(0, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.TWO, 1);
        Assert.assertEquals(2, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.WORD, 0);
        Assert.assertEquals(0, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.WORD, 1);
        Assert.assertEquals(4, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.WORD, 2);
        Assert.assertEquals(4, nextAvailableSlot);

        nextAvailableSlot = IAlignedMemoryAllocatorStrategy.getAlignedSlot(AlignmentType.WORD, 3);
        Assert.assertEquals(4, nextAvailableSlot);

    }


    @Test
    public void testAllocatePktBitMemory() {

        List<IDisjointSet<IMemoryInstance>> memoryInstanceDisjointSets = MemoryInstanceFilterTest.createSampleDisjointSetData();


        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = new LinkedHashMap<>();

        AlignedMemoryAllocatorStrategy allocator = new AlignedMemoryAllocatorStrategy(memoryInstanceDisjointSets, MemoryType.TypePktBit, memoryInstanceToOffsetMap, 256);

        try {
            int bitsUsed = allocator.allocate();
            Assert.assertEquals(1, bitsUsed);
        } catch (InsufficientMemoryException e) {
            Assert.fail("InsufficientMemoryException not expected");
        }

    }


    @Test
    public void testAllocatePktByteMemory() {

        List<IDisjointSet<IMemoryInstance>> memoryInstanceDisjointSets = MemoryInstanceFilterTest.createSampleDisjointSetData();


        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = new LinkedHashMap<>();

        AlignedMemoryAllocatorStrategy allocator = new AlignedMemoryAllocatorStrategy(memoryInstanceDisjointSets, MemoryType.TypePktByte, memoryInstanceToOffsetMap, 2048);

        String expectedOutput = "{hdr.ip1.ipv4.version:3:1=0, hdr.ip1.ipv4.ihl:4:1=1, hdr.ip1.ipv6.version:10:1=0, hdr.ip1.ipv6.trafficClass:11:1=1, hdr.ipv4.ihl:23:1=2, " +
                "hdr.ipv6.trafficClass:26:1=3, " +
                "hdr.ip1.ipv4.totalLen:5:2=4, hdr.ip1.ipv4.identification:6:2=6, hdr.ip1.ipv6.payloadLen:13:2=4, hdr.ipv4.totalLen:24:2=8, hdr.ipv4.identification:25:2=10, " +
                "hdr.ipv6.payloadLen:28:2=12, " +
                "hdr.ip1.ipv4.srcAddr:1:4=16, hdr.ip1.ipv4.dstAddr:2:4=20, hdr.ip1.ipv6.srcAddr:8:16=16, hdr.ip1.ipv6.dstAddr:9:16=32, hdr.ip1.ipv6.flowLabel:12:3=48, " +
                "hdr.ip2.ipv4.srcAddr:15:4=52, hdr.ip2.ipv4.dstAddr:16:4=56, hdr.ip2.ipv6.srcAddr:17:16=52, hdr.ip2.ipv6.dstAddr:18:16=68, " +
                "hdr.ipv4.srcAddr:19:4=84, hdr.ipv4.dstAddr:20:4=88, hdr.ipv6.srcAddr:21:16=92, hdr.ipv6.dstAddr:22:16=108, hdr.ipv6.flowLabel:27:3=124}";

        try {
            int bytesUsed = allocator.allocate();
            Assert.assertEquals(128, bytesUsed);
            String actualOutput = memoryInstanceToOffsetMap.toString();
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (InsufficientMemoryException e) {
            Assert.fail("InsufficientMemoryException not expected");
        }

    }

    @Test
    public void testAllocatePktByteMemoryForException() throws InsufficientMemoryException {

        List<IDisjointSet<IMemoryInstance>> memoryInstanceDisjointSets = MemoryInstanceFilterTest.createSampleDisjointSetData();

        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = new LinkedHashMap<>();
        AlignedMemoryAllocatorStrategy allocator = new AlignedMemoryAllocatorStrategy(memoryInstanceDisjointSets, MemoryType.TypePktByte, memoryInstanceToOffsetMap, 127);
        exception.expect(InsufficientMemoryException.class);
        allocator.allocate();

    }


    @Test
    public void testAllocateCfgByteMemory() {

        List<IDisjointSet<IMemoryInstance>> memoryInstanceDisjointSets = MemoryInstanceFilterTest.createSampleDisjointSetData();


        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = new LinkedHashMap<>();

        AlignedMemoryAllocatorStrategy allocator = new AlignedMemoryAllocatorStrategy(memoryInstanceDisjointSets, MemoryType.TypeCfgByte, memoryInstanceToOffsetMap, 2048);

        String expectedOutput = "{1023:30:2=0, 1024:31:2=2, 1025:32:2=4, 123456:33:4=8, 123456789:34:6=12}";

        try {
            int bytesUsed = allocator.allocate();
            Assert.assertEquals(20, bytesUsed);
            String actualOutput = memoryInstanceToOffsetMap.toString();
            Assert.assertEquals(expectedOutput, actualOutput);
        } catch (InsufficientMemoryException e) {
            Assert.fail("InsufficientMemoryException not expected");
        }

    }




    @AllArgsConstructor
    @EqualsAndHashCode
    public static class TestMemoryInstance implements IMemoryInstance {

        int idOfType;
        String instanceName;
        int size;
        MemoryType memoryType;

        @Override
        public String getInstanceName() {
            return instanceName;
        }

        @Override
        public MemoryType getMemoryType() {
            return memoryType;
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public String toString() {
            return instanceName + ":" + idOfType + ":" + size;
        }
    }

}