// crc32c.c
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

// CRC32C (Castagnoli) polynomial in reversed bit order
#define CRC32C_POLYNOMIAL 0x82F63B78

// Function to update CRC32C checksum with a single byte
static uint32_t crc32c_update(uint32_t crc, uint8_t data) {
    crc ^= data;
    for (int i = 0; i < 8; i++) {
        crc = (crc >> 1) ^ (CRC32C_POLYNOMIAL & (-(int32_t)(crc & 1)));
    }
    return crc;
}

// Function to calculate CRC32C checksum of an array of bytes
uint32_t crc32c(const uint8_t *data, size_t length) {
    uint32_t crc = ~0U; // Initial CRC value
    for (size_t i = 0; i < length; i++) {
        crc = crc32c_update(crc, data[i]);
    }
    return ~crc; // Final XOR value
}

// API function to calculate CRC32C checksum of a string
uint32_t crc32c_checksum(const char *input) {
    return crc32c((const uint8_t *)input, strlen(input));
}
