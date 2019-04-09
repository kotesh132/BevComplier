package com.p4.drmt.memoryManager;


public interface IAlignedMemoryAllocatorStrategy extends IMemoryAllocatorStrategy {


    int allocateBitMemory();

    int allocateByteMemory() throws InsufficientMemoryException;

    enum AlignmentType {
        ONE(1, 1),
        TWO(2, 2),
        WORD(3, 4);

        //byte/bit size
        int val;

        //boundary
        int offSet;

        AlignmentType(int val, int offSet) {
            this.val = val;
            this.offSet = offSet;
        }

        int getVal() {
            return val;
        }

        int getOffSet() {
            return offSet;
        }

        public static AlignmentType alignmentTypeOf(int bits) {
            int bytes = (int) Math.ceil(1.0 * bits / 8);

            switch (bytes) {
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                default:
                    return WORD;
            }
        }
    }

    /**
     * @param alignmentType define one/two or word length offsets
     * @param offset        inclusive
     * @return next available offset with alignment spacing
     */
    static int getAlignedSlot(AlignmentType alignmentType, int offset) {
        int alignmentOffset = alignmentType.getOffSet();
        return offset % alignmentOffset == 0 ? offset : offset + (alignmentOffset - offset % alignmentOffset);
    }
}
