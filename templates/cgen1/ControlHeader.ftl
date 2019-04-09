#ifndef control_hpp
#define control_hpp

#include <stdio.h>
#include "headers.hpp"
#include "enums.hpp"

<#list controls as control>
class ${control.name}{
private:
	<#list control.parameters as parameter>
    ${parameter.type}  ${parameter.name} ;
    </#list>
    const int actionNumber = ${control.actionNumber};
public:

	typedef void (${control.name}::*${control.name}_ActionFn)(cppgen::P4BitSet<160> data);
	typedef struct ${control.name}_action_data {

        cppgen::P4BitSet<160> data;
        ${control.name}::${control.name}_ActionFn fn;
        bool hit;
        action_enum action_run;
    } ${control.name}_action_data;
    
    const ${control.name}_ActionFn ${control.name}_actions[${control.actions?size}] = {
    	<#list control.actions as action>
    	&${control.name}::${action.name}_action,
    	</#list>
    };
    
    const void* action_map[${control.actions?size}] = {
    <#list control.actions as action>
    	"${action.name}",
    </#list>
    };
    void apply(<#list control.parameters as parameter>${parameter.type}*  ${parameter.name}_ptr<#sep>, </#list>);
    <#list control.tables as table>
    void ${table.name}_lookup(int tableId, cppgen::P4BitSet<160> *tableKey, ${control.name}_action_data* ${control.name}_action_data_obj);
    void ${table.name}_match(${control.name}_action_data* ${control.name}_action_data_obj);
    ${control.name}_action_data ${table.name}_apply();
    
    </#list>
    
    <#list control.actions as action>
    void ${action.name}(<#list action.actionParameters as actionParameter> cppgen::P4BitSet<${actionParameter.size}> ${actionParameter.name}<#sep>, </#list>);
    void ${action.name}_action(cppgen::P4BitSet<160> data);
    
    </#list>
};

</#list>
#endif /* control_hpp */