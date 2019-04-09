#ifndef PRIMITIVES_H_
#define PRIMITIVES_H_

#include <stdbool.h>

uint8_t*  hexCharToHex(char* src, int bitSize);

void extractAndLoad(uint8_t *src, uint8_t *dst, int byteOffset, int byteWidth);

void emitToPacketVector(uint8_t *dst, uint8_t *src, int byteOffset, int byteWidth);

void EMIT_BITS(uint8_t *dst, uint8_t *src, int bitOffsetStart, int width);

void EMIT_BITS_SLICED(uint8_t *dst, uint8_t *src, int bitOffsetStart, int from, int to);

void EMIT_BIT(uint8_t *dst, uint8_t *src, int bitOffset);

void EXTRACT_BITS(uint8_t *src, uint8_t* dst, int bitOffsetStart, int width);

void EXTRACT_BIT(uint8_t *src, uint8_t* dst, int bitOffsetStart);

bool Fn_V_B_V(uint8_t* op1, int op, uint8_t* op2, int bitSize);

bool Fn_V_B_C(uint8_t* op1, int op, char* op2, int bitSize);

bool Fn_C_B_C(char* op1, int op, char* op2, int bitSize);

bool Fn_C_B_V(char* op1, int op, uint8_t* op2, int bitSize);

uint8_t* Fn_V_V(uint8_t* op1, int op, uint8_t* op2, int bitSize);

uint8_t* Fn_V_C(uint8_t* op1, int op, char* op2, int bitSize);

uint8_t* Fn_C_C(char* op1, int op, char* op2, int bitSize);

uint8_t* Fn_C_V(char* op1, int op, uint8_t* op2, int bitSize);

#endif