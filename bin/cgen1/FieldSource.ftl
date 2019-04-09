#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include "fields.hpp"
#include "utility.hpp"

const char *table_names[] = {
<#list controls as control>
<#list control.tables as table>
    "${table.name}",
    </#list>
</#list>
};

const char *tableid_control_map[] = {
<#list controls as control>
<#list control.tables as table>
    "${control.name}",
    </#list>
</#list>
};

const void* control_action_map[] = {
<#list controls as control>
<#list control.actions as action>
    "${control.name}_${action.name}",
    </#list>
</#list>
    
};


int get_table_id(const char *table_name) {
    int i;
    int n = ARRAY_SIZE(table_names);
    for(i = 0; i< n; i++ )
        if(isEqual(table_name, table_names[i]))
            return i;
    return -1;
}

const char* get_control_name(const char *table_name){
    uint8_t table_id = get_table_id(table_name);
    return tableid_control_map[table_id];
}

int get_action_id(const char *action_name) {
    int i;
    int n = ARRAY_SIZE(control_action_map);
    for(i = 0; i< n; i++ )
        if(isEqual(action_name, (const char*)control_action_map[i]))
            return i;
    return -1;
}

int get_tables_count(){
    return ARRAY_SIZE(table_names);
}
