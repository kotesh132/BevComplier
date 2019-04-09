#ifndef primitives_hpp
#define primitives_hpp

#include <stdio.h>
#include "P4BitSet.h"
#include "utility.hpp"



#define BIT_MASK(type, bitWidth) ((((type)(1)) << (bitWidth)) - (type)1)
#define BYTES(bitWidth) ((bitWidth) / 8)
#define BYTE_ROUND_UP(x) ((x + 7) >> 3)

template<int N>
void copyToBitvar(cppgen::P4BitSet<N> *bitData, uint8_t *src, int bitWidth){
        
    for(int i = 0; i < bitWidth; i++){
        int bytePos = i/8;
        int bitPosInByte = i%8;
        bool isSetBit = src[bytePos] & (1 << bitPosInByte);
        if (isSetBit) {
            (*bitData)[i] = 1;
        }
        else {
            (*bitData)[i] = 0;
        }
    }
}


template<int N>
void copyFromBitvar(cppgen::P4BitSet<N> bitData, uint8_t *dst, int bitWidth){
    
    for(int i = 0; i < bitWidth; i++){
        int bytePos = i/8;
        int bitPosInByte = i%8;
        uint8_t setBit = 1 << bitPosInByte;
        if (bitData[i] == 1) {
            dst[bytePos] = dst[bytePos] | setBit;
        }else {
            dst[bytePos] = dst[bytePos] & ~setBit;
        }
    }
}

template<int N>
void print_hex_from_bit(char* prefix, cppgen::P4BitSet<N> s, int len) {
    
//    uint8_t dst[BYTE_ROUND_UP(len)] = {0};
    uint8_t *dst = new uint8_t[BYTE_ROUND_UP(len)];
    copyFromBitvar(s, dst, len);
    print_hex(prefix, dst, BYTE_ROUND_UP(len));
    delete[] dst;
}

template<int N, int K>
void copyBits(cppgen::P4BitSet<N> src, cppgen::P4BitSet<K> *dst, int dstBitOffset, int bitWidth){
    
    for(int i =0; i<bitWidth; i++ ){
//        dst->setdata(dstBitOffset+i, src.getdata(i));
        (*dst)[dstBitOffset+i] = src[i];
    }
}

template<int N, int K>
void extractAndLoad(cppgen::P4BitSet<N> *src, cppgen::P4BitSet<K> *dst, int srcByteOffsetStart, int bitLength) {
    
    int srcBitOffsetStart = srcByteOffsetStart * 8;
    for (int i = 0; i < bitLength ; i++) {
//        dst->setdata(i, src->getdata(srcBitOffsetStart + i));
        (*dst)[i] = (*src)[srcBitOffsetStart + i];
    }
    
}

template<int N, int K>
void emitToPacketVector(cppgen::P4BitSet<N> *dst, cppgen::P4BitSet<K> *src, int dstByteOffsetStart, int bitLength) {
    
    int dstBitOffsetStart = dstByteOffsetStart * 8;
    for (int i = dstBitOffsetStart; i < dstBitOffsetStart+bitLength ; i++) {
//        dst->setdata(i, src->getdata(i-dstBitOffsetStart));
        (*dst)[i] = (*src)[i-dstBitOffsetStart];
    }
    
}

#endif /* primitives_hpp */
