#include <stdint.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "pd.h"
#include "fields.h"
#include "utility.h"


drmt_table *tables;

bool table_look_up(uint8_t table_id, uint8_t *key, uint8_t *data){

	uint8_t int_table_id = table_id -1;
	switch (tables[int_table_id].mkind)
	{
		case MATCH_KIND_exact :
		{
			uint8_t size = tables[int_table_id].size;
			uint8_t table_itr = 0;
			drmt_table_entry *dte = tables[int_table_id].table_data;
			for(table_itr = 0; table_itr<size; table_itr++) {
				if(!strcmp((const char*)dte[table_itr].key, (const char*)key)){
					data[0] = dte[table_itr].action_id;
					memcpy(data+1, dte[table_itr].action_data, 20);
					return true ;
				}
			}
			break;
		}
		case MATCH_KIND_lpm:
		{
			break;
		}
		case MATCH_KIND_ternary:
		{
			break;
		}
		default:
		{
			printf("Error: %d kind of search is not support\n", table_id);
			return false;
		}
	}
	return false;
}

void show_table_content(int noOfTable){

	//drmt_table *tables = *tables_REF;
	int i=0;
	while(i < noOfTable){
		printf("\nTable: %d Max_size = %d",i, tables[i].max_size);
		printf("\nTable: %d Current_size = %d", i , tables[i].size);
		printf("\nTable: %d Match Kind = %d", i, tables[i].mkind);

		int table_itr, size = tables[i].size;

		drmt_table_entry *dte = tables[i].table_data;
		for(table_itr = 0; table_itr<size; table_itr++) {
			printf("\nkey = %s\n", dte[table_itr].key);
			printf("action_data = %s\n", dte[table_itr].action_data);
			printf("action_id = %d\n", dte[table_itr].action_id);
		}
		i++;
	}
}

void add_table_entry(uint8_t table_id, uint8_t *key, uint8_t action_id ,uint8_t *action_data){
	if(tables[table_id].size < tables[table_id].max_size){
		tables[table_id].table_data[tables[table_id].size].key = (uint8_t *)strdup((const char*)key);
		tables[table_id].table_data[tables[table_id].size].action_data = (uint8_t *)strdup((const char*)action_data);
		tables[table_id].table_data[tables[table_id].size].action_id = action_id;
		tables[table_id].size += 1;
	} else {
		//show_table_content(table_id);
		printf("error: %d Table is overflow ", -1);
	}
}

void table_init(drmt_table *tables, int noOfTable){

	//drmt_table *tables = *tables_REF;
	int i=0;
	while(i < noOfTable){
		tables[i].max_size = MAX_TABLE_SIZE;
		tables[i].size = 0;
		tables[i].mkind = MATCH_KIND_exact; /* can be replaced later with proper logic to get type of match*/
		tables[i].table_data  = (drmt_table_entry*)malloc(sizeof(drmt_table_entry) * tables[i].max_size);
		i++;
	}
}

static uint8_t*  hexCharToHex(uint8_t* src, int size){
	uint8_t* buffer = malloc(size);
	uint8_t* temp = src;
	unsigned int u1;

	int i = size - 1;;

	while (i>=0 && sscanf((const char*)temp, "%2x", &u1) == 1)
	{
		buffer[i--] = u1;
		temp += sizeof(uint8_t)*2;
	}
	return buffer;
}

void loadTableUtil(const char *fileName){

	FILE *fptr = fopen(fileName, "r");
	char line [128];

	if (fptr == NULL) {
		printf("Cannot open file %s\n", fileName);
		exit(0);
	}

	char *op_code;
	char *table_name;
	char *action_name;
	char *control_name;
	char *token[10];
	uint8_t action_id;
	uint8_t table_id;

	while (fgets(line, sizeof(line), fptr) != NULL) {
		int i = 0;
		token[i] = strtok(line, " ");
		while( token[i] != NULL ) {
			token[++i] = strtok(NULL, " ");
		}

		op_code = token[0];

		table_name = token[1];
		action_name = token[2];

		table_id = get_table_id(table_name);

		control_name = strdup(get_control_name(table_name));
		control_name = strcat(control_name, "_");
		control_name = strcat(control_name , action_name);

		action_id = get_action_id(control_name);

		//formulate action data
		char *action_data = (char *) calloc (DATA_LENGTH , sizeof(char));
		action_data = token[i-1];

		int len = (int)strlen(action_data);
		if (len > 0 && action_data[len-1] == '\n')
			action_data[len-1] = '\0';

		uint8_t *padded_action_data = (uint8_t *) calloc (DATA_LENGTH, sizeof(uint8_t));
		int data_length = (int)strlen(action_data)-2 ;
		memcpy(padded_action_data + DATA_LENGTH - data_length, action_data + 2 , data_length);
		memset(padded_action_data, '0', (DATA_LENGTH - data_length ) * sizeof(char));

		uint8_t *action_data_buffer = hexCharToHex(padded_action_data, KEY_LENGTH/2);

		//formulate key
		int j, key_length = 0;
		uint8_t *keys = (uint8_t *) calloc (KEY_LENGTH + 1 , sizeof(uint8_t));
		uint8_t *concatenated_keys = (uint8_t *) calloc (KEY_LENGTH + 1 , sizeof(uint8_t));
		for(j=i-3; j> 2; j--){
			memcpy(keys+key_length, token[j]+2, strlen(token[j])-2); // -2 to skip 0x
			key_length += strlen(token[j])-2 ;
		}

		memcpy(concatenated_keys + KEY_LENGTH - key_length  , keys , key_length);
		memset(concatenated_keys, '0', (KEY_LENGTH - key_length)* sizeof(uint8_t));

		uint8_t* keys_buffer = hexCharToHex(concatenated_keys, KEY_LENGTH/2);

		add_table_entry(table_id, keys_buffer ,action_id, action_data_buffer);
		free(keys);
		//free(keys_buffer);
		//free(action_data_buffer);
	}
	fclose(fptr);
}

void loadTables(const char *fileName){

	int noOfTable = get_tables_count();

	tables = (drmt_table*) malloc (sizeof(drmt_table)*noOfTable);
	table_init(tables, noOfTable);
	loadTableUtil(fileName);
	//show_table_content(noOfTable);
}