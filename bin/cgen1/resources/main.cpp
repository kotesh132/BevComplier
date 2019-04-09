#include <stdio.h>
#include "headers.hpp"
#include "control.hpp"
#include "utility.hpp"
#include "pd.hpp"
#include "phvparser.hpp"
#include "phvdeparser.hpp"
#include "primitives.hpp"
#include "iostream"


char dataFile[128];
char dataDir[128];

char* getFilePath(char* fileName){
    strcpy(dataFile, dataDir);
    strcat(dataFile, fileName);
    return dataFile;
}

void loadPacketVector(uint8_t *phv, int byteSize){
    printf("\nLoad packet vector from iPhv.data\n");
    char *fileName = strdup(getFilePath("iPhv.data"));
    loadFromFile(fileName, phv, byteSize);
    free(fileName);
}

void loadBitPacketVector(uint8_t *phv, int byteSize){
    printf("\nLoad packet bit vector from iPhv.data\n");
    char *fileName = strdup(getFilePath("iPhvBit.data"));
    loadBitFromFile(fileName, phv, byteSize);
    free(fileName);
}

void emitPacketVector(uint8_t *phv, int byteSize){
    printf("\nEmit Packet Vector to oPhv.data\n");
    char *fileName = strdup(getFilePath("oPhv.data"));
    writeToFile(fileName, phv, byteSize);
    free(fileName);
}

void emitBitPacketVector(uint8_t *phv, int byteSize){
    printf("Emit Packet Bit Vector to oPhvBit.data\n");
    char *fileName = strdup(getFilePath("oPhvBit.data"));
    writeBitToFile(fileName, phv, byteSize);
    free(fileName);
}

void validateOutput() {
    char *fileName1 = strdup(getFilePath("vPhv.data"));
    char *fileName2 = strdup(getFilePath("oPhv.data"));
    compareFiles(fileName1, fileName2);
    free(fileName1);
    free(fileName2);
}


void fromPhv(){
    
    cppgen::P4BitSet<256*8> packet = {0};
    cppgen::P4BitSet<32*8> packetBit = {0};
    
    uint8_t packetData[256] = {0};
    uint8_t packetBitData[32] = {0};

    
    headers hdr;
    metadata meta;
    standard_metadata_t standard_metadata;
    
    loadPacketVector(packetData, 256);
    loadBitPacketVector(packetBitData, 32);
    
    copyToBitvar(&packet, packetData, 256*8);
    copyToBitvar(&packetBit, packetBitData, 32*8);
    
    parsePacketVector(&packet, &packetBit, &hdr, &meta, &standard_metadata);
    
    ingress ingress;
    egress egress;
    
    ingress.apply(&hdr, &meta, &standard_metadata);
    egress.apply(&hdr, &meta, &standard_metadata);
    
    deParsePacketVector(&packet, &packetBit, &hdr, &meta, &standard_metadata);

    copyFromBitvar(packet, packetData, 256*8);
    copyFromBitvar(packetBit, packetBitData, 32*8);
    emitPacketVector(packetData, 256);
    emitBitPacketVector(packetBitData, 32);
    
}

int main(int argc, char** argv) {
    if (argc == 1){
        printf("Using current directory to find commands.txt, iPhv.data, vPhv.data, iPhvBit.data, vPhvBit.data\n");
        strcpy(dataDir, "/Users/pvelumul/workspace/pooja_perforce/sw/p4/p4cpp/cppgen-workspace/cppgen/p4demos/demo8/");
    }
    else if (argc == 2) {
        printf("Using %s directory to find commands.txt, iPhv.data, vPhv.data, iPhvBit.data, vPhvBit.data\n", argv[1]);
        strcpy(dataDir, argv[1]);
    }
    else {
        printf("Please provide correct either 0 or 1 parameters");
        exit(1);
    }
    
    loadTables(getFilePath("commands.txt"));
    fromPhv();
    validateOutput();
}
