#ifndef pd_hpp
#define pd_hpp

#include <stdio.h>
#include <stdint.h>
#include "enums.hpp"
#include "P4BitSet.h"

#define KEY_LENGTH 40 /*multiplied by 2, as each char is stored as 2 hex char */
#define DATA_LENGTH 40 /*multiplied by 2, as each char is stored as 2 hex char */
#define TABLE_ENTRY_LENGTH 80 /*multiplied by 2, as each char is stored as 2 hex char */
#define MAX_TABLE_SIZE 10

typedef struct drmt_table_entry {
    uint8_t *key;
    /*
     * First byte of action data is always action id and is actual action data.
     */
    uint8_t action_id;
    uint8_t *action_data;
} drmt_table_entry;

typedef struct drmt_table {
    uint8_t table_id;
    drmt_table_entry *table_data;
    uint8_t *key;
    int max_size;
    int size;
    match_kind mkind;
    
} drmt_table;

bool table_look_up(uint8_t table_id, cppgen::P4BitSet<160> key, uint8_t *data);

void add_table_entry(uint8_t table_id, uint8_t *key, uint8_t action_id ,uint8_t *action_data);

void loadTables(const char *fileName);

#endif /* pd_hpp */
