#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
#include "primitives.h"
#include "enums.h"
#include <stdbool.h>

#define BIT_MASK(type, bitWidth) ((((type)(1)) << (bitWidth)) - (type)1)
#define BYTES(bitWidth) ((bitWidth) / 8)
#define BYTE_ROUND_UP(x) ((x + 7) >> 3)

/* Extracts bits of specific width from src at an offset into dst*/
void EXTRACT_BITS(uint8_t *src, uint8_t *dst, int bitOffsetStart, int width){
	int bitOffsetEnd = bitOffsetStart + width;
	int byteStart = BYTES(bitOffsetStart);
	int byteEnd   = BYTES(bitOffsetEnd-1);

	int rightShift = bitOffsetStart - (byteStart << 3);  //bitOffsetStart - byteStart*8
	int noOfBytesToRead = byteEnd - byteStart + 1;

	uint8_t *temp = malloc(noOfBytesToRead);
	memcpy(temp, src, noOfBytesToRead);

	if (rightShift) {
		int i = 0;
		while(i < noOfBytesToRead -1) {
			temp[i] = temp[i] >> rightShift | temp[i + 1] << (8 - rightShift);
			i++;
		}
		temp[i] = temp[i] >> rightShift;
	}

	int widthInBytes = BYTES(width);
	int widthReminder = width % 8;

	if (widthInBytes < noOfBytesToRead) {
			temp[widthInBytes] = temp[widthInBytes] & BIT_MASK(uint8_t, widthReminder);
	}

	memcpy(dst, temp, widthInBytes);
	free(temp);

}

/* Extract specific bit from src into dst */
void EXTRACT_BIT(uint8_t *src, uint8_t *dst, int bitOffset){
	int byteOffset = BYTES(bitOffset);
	int bitPosition = bitOffset%8;

	if (src[byteOffset] & (1 << bitPosition)){
		*dst = (uint8_t)1;
	}else {
		*dst = (uint8_t)0;
	}

}

/* Emit bits of specific width from src into dst at an offset */
void EMIT_BITS(uint8_t *dst, uint8_t *src, int bitOffsetStart, int width) {
	if (width <= 0) {
		return;
	}
	int bitOffsetEnd = bitOffsetStart + width;
	int byteStart = BYTES(bitOffsetStart);
	int byteEnd = BYTES(bitOffsetEnd - 1);

	int leftShift = bitOffsetStart - (byteStart << 3);//byteStart*8 - bitOffsetStart
	int noOfBytesToEmit = byteEnd - byteStart + 1;

	int widthInBytes = BYTE_ROUND_UP(width);

	uint8_t *temp = malloc(noOfBytesToEmit);

	memcpy(temp, src, widthInBytes);

	if (leftShift) {
		int i = noOfBytesToEmit - 1;
		while (i >= 1) {
			temp[i] = temp[i] << leftShift | temp[i + 1] >> (8 - leftShift);
			i--;
		}
		temp[i] = temp[i] << leftShift;
	}

	int bitOffsetStartReminder = bitOffsetStart % 8;
	int bitOffsetEndReminder = (bitOffsetEnd) % 8;
	int i = 0;


	if (bitOffsetStartReminder != 0 || bitOffsetEndReminder != 0) {
		if (bitOffsetEndReminder == 0) {
			bitOffsetEndReminder = 8;
		}

		int frontMask = ~BIT_MASK(uint8_t, bitOffsetStartReminder);
		int backMask = BIT_MASK(uint8_t, bitOffsetEndReminder);

		if (noOfBytesToEmit == 1) {
			int mask = frontMask & backMask;
			temp[0] = temp[0] & mask;

			dst[byteStart] = dst[byteStart] & ~mask;
			dst[byteStart] = dst[byteStart] | temp[0];
		} else {
			temp[0] = temp[0] & frontMask;
			dst[byteStart] = dst[byteStart] & ~frontMask;
			dst[byteStart] = dst[byteStart] | temp[0];

			temp[noOfBytesToEmit - 1] = temp[noOfBytesToEmit - 1] & backMask;
			dst[byteEnd] = dst[byteEnd] & ~backMask;
			dst[byteEnd] = dst[byteEnd] | temp[noOfBytesToEmit - 1];

			for (i = 1; i < noOfBytesToEmit - 1; i++) {
				dst[byteStart + i] = temp[i];
			}
		}
	} else {
		for (i = 0; i < noOfBytesToEmit; i++) {
			dst[byteStart + i] = temp[i];
		}
	}

	free(temp);
	return;

}

void EMIT_BITS_SLICED(uint8_t *dst, uint8_t *src, int bitOffsetStart, int from, int to){
    int width = to - from + 1;
    int byteWidth = BYTE_ROUND_UP(width);

    uint8_t *temp = calloc(byteWidth, 1);

    EXTRACT_BITS(src, temp, from, width);
    EMIT_BITS(dst, temp, bitOffsetStart, width);
    free(temp);
}

/* Emit lsb bit from src into dst at a offset*/
void EMIT_BIT(uint8_t *dst, uint8_t *src, int bitOffset) {
	if (bitOffset < 0) {
		return;
	}
	int byteOffset = BYTES(bitOffset);
	int leftShift = bitOffset - (byteOffset << 3);

	if (src[0] & (uint8_t)1) {
		dst[byteOffset] |= 1<<leftShift;
	}else {
		dst[byteOffset] &= ~(1<<leftShift);
	}
}

/* Extract specific width of bytes from src at an offset into dst */
void extractAndLoad(uint8_t *src, uint8_t *dst, int byteOffset, int byteWidth){

	memcpy(dst, src+byteOffset, byteWidth);
	//pad zero for uint32_t elements
	if (byteWidth == 3) {
		dst[3] = 0;
	}
}

/* Emit specific width of bytes from src into dst at an offset */
void emitToPacketVector(uint8_t *dst, uint8_t *src, int byteOffset, int byteWidth) {
	memcpy(dst+byteOffset, src, byteWidth);
	//pad zero for uint32_t elements
	if (byteWidth == 3) {
		dst[byteOffset+3] = 0;
	}

}

/* Assumption, anything greater than bitsize are zeros. We still probably need to handle it */
static int isEqualOp(uint8_t* op1, uint8_t* op2, int bitSize){
	int byteWidth = (bitSize+7)/8;

	int i = 0;
	for (i = 0; i < byteWidth; i++) {
		if(op1[i] == op2[i]) {
			//do nothing

		}else if(op1[i] > op2 [i]) {
			return 1;
		}else {
			return -1;
		}
	}
	return 0;

}

static uint8_t* doPlus(uint8_t* op1, uint8_t* op2, int bitSize){
	int byteWidth = (bitSize+7)/8;
	uint8_t* result = malloc(byteWidth);
	uint16_t temp = 0;
	uint8_t carry = 0;
	int i =0;
	for (i = 0; i < byteWidth; i++) {
		temp = op1[i] + op2[i] + carry;
		result[i] = temp%256;
		carry = temp/256;
	}

	//further carry flag is neglected as per p4 spec
	return result;

}

static uint8_t* doMinus(uint8_t* op1, uint8_t* op2, int bitSize){
	int byteWidth = (bitSize+7)/8;
	uint8_t* result = malloc(byteWidth);
	uint16_t temp = 0;
	uint8_t borrow = 0;
	int i = 0;
	//assumption op1 >= op2; If op1 < op2 then 6 byte -1 will be stored as 0xffffffffffff ie., in the reverse direction
	for (i = 0; i < byteWidth; i++) {
		if (op1[i] < op2[i]) {
			borrow = 1;
			int j = i+1;
			temp = op1[i] + 256;
			while ( borrow == 1){
				if(op1[j] >= 1){
					op1[j]--;
					borrow = 0;
				}else {
					op1[j++] = 255;
					borrow = 1;
				}
			}
			result[i] = temp - op2[i];
		}else {
			result[i] = op1[i] - op2[i];
		}
	}

	return result;
}

static uint8_t* doXor(uint8_t* op1, uint8_t* op2, int bitSize){
    int byteWidth = (bitSize+7)/8;
    uint8_t* result = malloc(byteWidth);
    int i = 0;
    for (i = 0; i < byteWidth; i++) {
        result[0] = op1[0] ^ op2[0];
    }

    return result;
}

static uint8_t* doOr(uint8_t* op1, uint8_t* op2, int bitSize){
    int byteWidth = (bitSize+7)/8;
    uint8_t* result = malloc(byteWidth);
    int i = 0;
    for (i = 0; i < byteWidth; i++) {
        result[0] = op1[0] | op2[0];
    }

    return result;
}

static uint8_t* doAnd(uint8_t* op1, uint8_t* op2, int bitSize){
    int byteWidth = (bitSize+7)/8;
    uint8_t* result = malloc(byteWidth);
    int i = 0;
    for (i = 0; i < byteWidth; i++) {
        result[0] = op1[0] & op2[0];
    }

    return result;
}

/* converts char string like '0xE1AB123' to its actual hex value oXE1AB123 */
uint8_t*  hexCharToHex(char* src, int bitSize){

    //Assumption src[0] = 0 and (src[1] = x or src[1]= X). Later will intruduce hard check
	int byteWidth = (bitSize+7)/8;
	int padSize = byteWidth * 2;
	uint8_t *padded_src = (uint8_t *) calloc (padSize, sizeof(uint8_t));
	int size = (int)strlen(src) - 2;
	memcpy(padded_src + padSize - size, src + 2 , size);
	memset(padded_src, '0', (padSize - size ) * sizeof(uint8_t));

	uint8_t* temp = padded_src;
	unsigned int u1;

	int i = byteWidth - 1;;
	uint8_t* buffer = malloc(byteWidth);

	while (i>=0 && sscanf((const char*)temp, "%2x", &u1) == 1)
	{
		buffer[i--] = u1;
		temp += sizeof(uint8_t)*2;
	}
	free(padded_src);
	return buffer;
}


bool Fn_V_B_V(uint8_t* op1, int op, uint8_t* op2, int bitSize){
	int ret = 0;
	switch (op) {
		case EQ:
			ret = isEqualOp(op1, op2, bitSize);
			return ret == 0 ? true : false;
			break;
		case NE:
			ret = isEqualOp(op1, op2, bitSize);
			return ret == 0 ? false : true;
		case GT:
			ret = isEqualOp(op1, op2, bitSize);
			return ret == 1 ? true : false;
		case LT:
			ret = isEqualOp(op1, op2, bitSize);
			return ret == -1 ? true : false;
		default: return false; //Ideally throw some exception that operator is not handled
			break;
	}

	return false;
}


bool Fn_V_B_C(uint8_t* op1, int op, char* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op2, bitSize);
	return Fn_V_B_V(op1, op, op3, bitSize);
}


bool Fn_C_B_C(char* op1, int op, char* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op1, bitSize);
	uint8_t* op4 = hexCharToHex(op2, bitSize);

	return Fn_V_B_V(op3, op, op4, bitSize);

}

bool Fn_C_B_V(char* op1, int op, uint8_t* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op1, bitSize);
	return Fn_V_B_V(op3, op, op2, bitSize);
}


uint8_t* Fn_V_V(uint8_t* op1, int op, uint8_t* op2, int bitSize){
	uint8_t* ret = NULL;
	switch (op) {
		case PLUS:
			ret = doPlus(op1, op2, bitSize);
			break;
		case MINUS:
			ret = doMinus(op1, op2, bitSize);
			break;
		case XOR:
			ret = doXor(op1, op2, bitSize);
			break;
		case OR:
			ret = doOr(op1, op2, bitSize);
			break;
		case AND:
			ret = doAnd(op1, op2, bitSize);
			break;
		default: return false; //Ideally throw some exception that operator is not handled
			break;
	}

	return ret;

}

uint8_t* Fn_V_C(uint8_t* op1, int op, char* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op2, bitSize);
	return Fn_V_V(op1, op, op3, bitSize);
}

uint8_t* Fn_C_C(char* op1, int op, char* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op1, bitSize);
	uint8_t* op4 = hexCharToHex(op2, bitSize);

	return Fn_V_V(op3, op, op4, bitSize);
}

uint8_t* Fn_C_V(char* op1, int op, uint8_t* op2, int bitSize){
	uint8_t* op3 = hexCharToHex(op1, bitSize);
	return Fn_V_V(op3, op, op2, bitSize);
}


//Unused from below

//uint8_t* EXTRACT_BYTE(uint8_t *src, int bitOffsetFromStart) {
//	int byteOffset = BYTES(bitOffsetFromStart);
//	uint8_t *dst = (uint8_t *) malloc(1);
//	dst[0] = src[byteOffset];
//	return dst;
//}
//
//uint16_t* EXTRACT_HALF(uint8_t *src, int bitOffsetFromStart) {
//	int byteOffset = BYTES(bitOffsetFromStart);
//	uint8_t *dst = (uint8_t *) malloc(2);
//	dst[0] = src[byteOffset + 1];
//	dst[1] = src[byteOffset];
//	return (uint16_t *)dst;
//}
//
//uint32_t* EXTRACT_WORD(uint8_t *src, int bitOffsetFromStart) {
//	int byteOffset = BYTES(bitOffsetFromStart);
//	uint8_t *dst = (uint8_t *) malloc(4);
//	dst[0] = src[byteOffset + 3];
//	dst[1] = src[byteOffset + 2];
//	dst[2] = src[byteOffset + 1];
//	dst[4] = src[byteOffset];
//	return (uint32_t *)dst;
//}
//
//uint16_t* EXTRACT_HALF_OF_WIDTH(uint8_t *src, int bitOffsetFromStart, int leftShiftedInByte, int width) {
//	uint16_t* dst = EXTRACT_HALF(src, bitOffsetFromStart);
//	*dst = (*dst >> leftShiftedInByte);
//	*dst = *dst & BIT_MASK(uint16_t, width);
//	return dst;
//}
//
//uint32_t* EXTRACT_WORD_OF_WIDTH(uint8_t *src, int bitOffsetFromStart, int leftShiftedInByte, int width) {
//	uint32_t* dst = EXTRACT_WORD(src, bitOffsetFromStart);
//	*dst = (*dst >> leftShiftedInByte);
//	*dst = *dst & BIT_MASK(uint32_t, width);
//	return dst;
//}
//
//uint8_t* EXTRACT_BYTES_OF_WIDTH(uint8_t *src, int byteOffset, int byteWidth){
//
//	uint8_t* temp = src;
//
//	*temp += byteOffset;
//	uint8_t *retValue = malloc(byteWidth);
//
//	memcpy(retValue, temp, byteWidth);
//	return retValue;
//}
//
//void* EXTRACT_BITS_FRWD(uint8_t *src, int bitOffsetStart, int width){
//	if (width <= 0) {
//		return NULL;
//	}
//	int bitOffsetEnd = bitOffsetStart + width;
//	int byteStart = BYTES(bitOffsetStart);
//	int byteEnd   = BYTES(bitOffsetEnd-1);
//
//	int rightShift = ((byteEnd+1) << 3)  - bitOffsetEnd;  //(byteEnd+1)*8 - bitOffsetEnd
//	int noOfBytesToRead = byteEnd - byteStart + 1;
//
//	uint8_t *dst = malloc(noOfBytesToRead);
//
//	int i = 0;
//
//	for (i =0; i<noOfBytesToRead; i++) {
//		dst[i] = src[byteEnd - i];
//	}
//
//	if (rightShift) {
//		i = 0;
//		while(i < noOfBytesToRead -1) {
//			dst[i] = dst[i] >> rightShift | dst[i + 1] << (8 - rightShift);
//			i++;
//		}
//		dst[i] = dst[i] >> rightShift;
//	}
//
//	int widthInBytes = BYTES(width);
//	int widthReminder = width % 8;
//	dst[widthInBytes] = dst[widthInBytes] & BIT_MASK(uint8_t, widthReminder);
//
//
//	return (void *)dst;
//
//}
//
//void EMIT_BITS_FRWD(uint8_t *src, uint8_t *dst, int bitOffsetStart, int width) {
//	if (width <= 0) {
//		return;
//	}
//	int bitOffsetEnd = bitOffsetStart + width;
//	int byteStart = BYTES(bitOffsetStart);
//	int byteEnd = BYTES(bitOffsetEnd - 1);
//
//	int leftShift = ((byteEnd + 1) << 3) - bitOffsetEnd; //(byteEnd+1)*8 - bitOffsetEnd
//	int noOfBytesToEmit = byteEnd - byteStart + 1;
//
//	int widthInBytes = BYTE_ROUND_UP(width);
//
//	uint8_t *temp = malloc(noOfBytesToEmit);
//
//	int j = noOfBytesToEmit;
//	int i = 0;
//	for (i = 0; i < widthInBytes; i++) {
//		temp[--j] = src[i];
//	}
//
//	if (leftShift) {
//		i = noOfBytesToEmit - 1;
//		while (i >= 1) {
//			temp[i] = temp[i] << leftShift | dst[i + 1] >> (8 - leftShift);
//			i--;
//		}
//		temp[i] = temp[i] << leftShift;
//	}
//
//	int bitOffsetStartReminder = bitOffsetStart % 8;
//	int bitOffsetEndReminder = (bitOffsetEnd) % 8;
//
//
//	if (bitOffsetStartReminder != 0 || bitOffsetEndReminder != 0) {
//		if (bitOffsetEndReminder == 0) {
//			bitOffsetEndReminder = 8;
//		}
//
//		int frontMask = BIT_MASK(uint8_t, 8 - bitOffsetStartReminder);
//		int backMask = ~BIT_MASK(uint8_t, 8 - bitOffsetEndReminder);
//
//		if (noOfBytesToEmit == 1) {
//			int mask = frontMask & backMask;
//			temp[0] = temp[0] & mask;
//
//			dst[byteStart] = dst[byteStart] & ~mask;
//			dst[byteStart] = dst[byteStart] | temp[0];
//		} else {
//			temp[0] = temp[0] & frontMask;
//			dst[byteStart] = dst[byteStart] & ~frontMask;
//			dst[byteStart] = dst[byteStart] | temp[0];
//
//			temp[noOfBytesToEmit - 1] = temp[noOfBytesToEmit - 1] & backMask;
//			dst[byteEnd] = dst[byteEnd] & ~backMask;
//			dst[byteEnd] = dst[byteEnd] | temp[noOfBytesToEmit - 1];
//
//			for (i = 1; i < noOfBytesToEmit - 1; i++) {
//				dst[byteStart + i] = temp[i];
//			}
//		}
//	} else {
//		for (i = 0; i < noOfBytesToEmit; i++) {
//			dst[byteStart + i] = temp[i];
//		}
//	}
//
//	return;
//
//}
//
//void extractAndLoadFrwd(uint8_t *src, uint8_t *dst, int byteOffset, int byteWidth){
//	uint8_t *retValue = EXTRACT_BYTES_OF_WIDTH(src, byteOffset, byteWidth);
//	retValue += (byteWidth-1);
//	int i = 0;
//	for(i = 0; i< byteWidth; i++){
//		dst[i] = *retValue;
//		--retValue;
//	}
//	free(retValue);
//}
//
//void extractAndLoadBit(uint8_t *src, uint8_t *dst, int bitOffset){
//	uint8_t *retValue = EXTRACT_BIT(src, bitOffset);
//	dst[0] = *retValue;
//	free(retValue);
//}
//
//void emitToPacketVectorFrwd(uint8_t *dst, uint8_t *src, int byteOffset, int byteWidth) {
//
//	uint8_t* temp = src;
//	temp += (byteWidth-1);
//	int i = 0;
//	for(i = 0; i< byteWidth; i++){
//		dst[byteOffset+i] = *temp;
//		--temp;
//	}
//
//}