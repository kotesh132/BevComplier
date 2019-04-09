#include "utility.h"
#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>


void print_hex(char* prefix, uint8_t *s, int len) {
	printf("%s 0x", prefix);
	int i = 0;
	for(i = len-1; i >= 0; i--) {
		printf("%02x", s[i]);
	}
	printf("\n");
}

int ascii_to_hex(char c) {
	int num = (int) c;
	if (num < 58 && num > 47) {
		return num - 48;
	}
	if (num < 103 && num > 96) {
		return num - 87;
	}
	return num;
}

void loadFromFile(const char *fileName, uint8_t *buffer, int byteSize){
	FILE *fptr;

	/*  open the file for reading */
	fptr = fopen(fileName, "r");

	if (fptr == NULL) {
		printf("Cannot open file %s\n", fileName);
		exit(0);
	}
	unsigned char c1, c2;
	int i = 0;
	uint8_t sum;
	char ch;

	do {
		c1 = ascii_to_hex(fgetc(fptr));
		c2 = ascii_to_hex(fgetc(fptr));
		sum = c1 << 4 | c2;
		buffer[i] = sum;
		i++;
        if (i >= byteSize) {
            break;
        }
		ch = fgetc(fptr);
	} while (ch != EOF);

	fclose(fptr);
}

void loadBitFromFile(const char *fileName, uint8_t *buffer, int byteSize){
	FILE *fptr;

	/*  open the file for reading */
	fptr = fopen(fileName, "r");

	if (fptr == NULL) {
		printf("Cannot open file %s\n", fileName);
		exit(0);
	}
	unsigned char c1, c2;
	int i = byteSize-1;
	uint8_t sum;
	char ch;

	do {
		ch = fgetc(fptr);
		if (ch == EOF) {
			break;
		}
		c1 = ascii_to_hex(ch);

		ch = fgetc(fptr);
		if (ch == EOF) {
			break;
		}
		c2 = ascii_to_hex(ch);
		sum = c1 << 4 | c2;
		buffer[i] = sum;

		i--;

		if (i < 0){
			break;
		}
	} while (1);

	fclose(fptr);

}

void writeToFile(const char *fileName, uint8_t *phv, int byteSize) {
	FILE *fptr = fopen(fileName, "w");

	if (fptr == NULL) {
		printf("Cannot open file %s\n", fileName);
		exit(0);
	}

    int i = 0;
	for (i = 0; i < byteSize; i++) {
//		if (i == byteSize - 1) {
//			fprintf(fptr, "%02x", phv[i]);
//			break;
//		}
		fprintf(fptr, "%02x\n", phv[i]);
	}
	printf("\n");

	fclose(fptr);

}

void writeBitToFile(const char *fileName, uint8_t *phv, int byteSize) {
	FILE *fptr = fopen(fileName, "w");

	if (fptr == NULL) {
		printf("Cannot open file %s\n", fileName);
		exit(0);
	}

	int i = 0;
	for (i = byteSize-1; i >= 0; i--) {
		fprintf(fptr, "%02x", phv[i]);
	}
	printf("\n");

	fclose(fptr);

}

static int filecmp(FILE *fp1, FILE *fp2)
{
	int f1, f2;
	int i =0;
	while (1) {
		i++;
		f1 = getc(fp1);
		f2 = getc(fp2);

		if (f1 != f2) {
			printf("File comparison failed at index %d", i);
			return 1;
		}
		if (f1 == EOF && f2 == EOF) {
			printf("File comparison successful\n");
			printf("FINAL TEST_PASS\n");
			return 0;
		} else if (f1 == EOF || f2 == EOF) {
			return 1;
		}
	}
}

void compareFiles(const char *fileName1, const char *fileName2) {
	FILE *fp1, *fp2;
	if ((fp1 = fopen(fileName1, "r")) == NULL) {
		printf("cat: can't open %s\n", fileName1);
	}

	if ((fp2 = fopen(fileName2, "r")) == NULL) {
		printf("cat: can't open %s\n", fileName2);
	}
	filecmp(fp1, fp2);
	fclose(fp1);
	fclose(fp2);
	return;
}

int isEqual(const char *key1, const char *key2){
	int i;
	for(i=0; key1[i] != '\0' && key2[i] != '\0'; i++){
		if((key1[i] - key2[i]) != 0)
			return 0;
	}
	if(key1[i] == '\0' && key2[i] == '\0')
		return 1;
	else return 0;
}