#ifndef utility_hpp
#define utility_hpp

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

void print_hex(char* prefix, uint8_t *s, int len);
void loadFromFile(const char *fileName, uint8_t *buffer, int byteSize);
void loadBitFromFile(const char *fileName, uint8_t *buffer, int byteSize);
void writeToFile(const char *fileName, uint8_t *phv, int byteSize);
void writeBitToFile(const char *fileName, uint8_t *phv, int byteSize);

void compareFiles(const char *fileName1, const char *fileName2);

int isEqual(const char *key1, const char *key2);

#endif /* utility_hpp */
