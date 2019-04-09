#ifndef enums_hpp
#define enums_hpp

#include <stdio.h>

typedef enum action_enum {
<#list controls as control>
	<#list control.actions as action>
    ${control.name}_${action.name}_enum<#sep>,
    </#list>
<#sep>,
</#list>

} action_enum;

typedef enum match_kind {
    MATCH_KIND_exact,
    MATCH_KIND_ternary,
    MATCH_KIND_lpm,
    
} match_kind;

#endif /* enums_hpp */