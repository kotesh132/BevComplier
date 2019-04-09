#ifndef enums_hpp
#define enums_hpp

#include <stdio.h>

typedef enum action_enum {
    ingress_set_bd_dmac_intf_enum
} action_enum;

typedef enum match_kind {
    MATCH_KIND_exact,
    MATCH_KIND_ternary,
    MATCH_KIND_lpm,
    
} match_kind;

#endif /* enums_hpp */