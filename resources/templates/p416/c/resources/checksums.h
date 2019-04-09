#ifndef CHECKSUMS_H_
#define CHECKSUMS_H_

#include "enums.h"
#include <stdbool.h>

void crc16_fn(uint8_t *buf, int len, uint8_t *result);
void crc32_fn(uint8_t *buf, int len, uint8_t *result);
void crcCCITT_fn(uint8_t *buf, int len, uint8_t *result);
void identity_fn(uint8_t *buf, int len, uint8_t *result);

void verify_checksum(bool condition, uint8_t *data, uint8_t *checksum, int length, HashAlgorithm algo);
void update_checksum(bool condition, uint8_t *data, uint8_t *checksum, int length, HashAlgorithm algo);

#endif
