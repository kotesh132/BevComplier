#include <stdio.h>
#include "control.hpp"
#include "enums.hpp"
#include "primitives.hpp"
#include "pd.hpp"
#include "iostream"

<#list control.actions as action>
void ${control.name}::${action.name}(<#list action.actionParameters as actionParameter> cppgen::P4BitSet<${actionParameter.size}> ${actionParameter.name}<#sep>, </#list>)${action.blockStatements}
    
void ${control.name}::${action.name}_action(cppgen::P4BitSet<160> data){
<#list action.actionParameters as actionParameter> 
cppgen::P4BitSet<${actionParameter.size}> ${actionParameter.name};
${actionParameter.name} = data["${actionParameter.size-1}:0"];
print_hex_from_bit("${actionParameter.name}: ", ${actionParameter.name}, ${actionParameter.size});
</#list>
${action.name}(<#list action.actionParameters as actionParameter> ${actionParameter.name}<#sep>, </#list>);
}

</#list>

<#list control.tables as table>
void ${control.name}::${table.name}_lookup(int tableId, cppgen::P4BitSet<160> *tableKey, ${control.name}::${control.name}_action_data* ${control.name}_action_data_obj){

    uint8_t tableData[21] = {0};

    bool hit = table_look_up(tableId, *tableKey, tableData);

    ${control.name}_action_data_obj->action_run = (action_enum)tableData[0];
    ${control.name}_action_data_obj->fn = ${control.name}_actions[tableData[0] - actionNumber];
    copyToBitvar(&(${control.name}_action_data_obj->data), tableData+1, 20 * 8);
    ${control.name}_action_data_obj->hit = hit;

    printf("Action to run: %s\n", action_map[tableData[0] - actionNumber]);
}


void ${control.name}::${table.name}_match(${control.name}::${control.name}_action_data* ${control.name}_action_data_obj){

    //Key generation
    cppgen::P4BitSet<160> tableKey;
    int bitOffset = 0;
    std::string slice = "";
    
    <#list table.keyElements as keyElement>
    //    copyBits(${keyElement.name}, &tableKey, bitOffset, ${keyElement.size});
    slice = std::to_string(bitOffset + ${keyElement.size} - 1) +":" + std::to_string(bitOffset);
    tableKey[slice] = ${keyElement.name};
    bitOffset = bitOffset + ${keyElement.size};
    
    </#list>
    //lookup
    print_hex_from_bit("key generated :", tableKey, bitOffset);
    ${table.name}_lookup(${table.tableId}, &tableKey, ${control.name}_action_data_obj);

}

${control.name}::${control.name}_action_data ${control.name}::${table.name}_apply(){
    printf("\nApplying match on table: ${table.name}\n");

    ${control.name}::${control.name}_action_data ${control.name}_action_data_obj;
    //initialization
    ${control.name}_action_data_obj.hit = false;
    ${control.name}_action_data_obj.fn = NULL;

    //perform match
    ${table.name}_match(&${control.name}_action_data_obj);

    //execute action
    (this->*${control.name}_action_data_obj.fn)(${control.name}_action_data_obj.data);

    return ${control.name}_action_data_obj;
}

</#list>

void ${control.name}::apply(<#list control.parameters as parameter>${parameter.type}* ${parameter.name}_ptr<#sep>, </#list>){
    //copyIn
    <#list control.parameters as parameter>this->${parameter.name} = *${parameter.name}_ptr;
    </#list>

	${control.controlBody}

    //copyOut
    <#list control.parameters as parameter>
    *${parameter.name}_ptr = ${parameter.name};
    </#list>
}

