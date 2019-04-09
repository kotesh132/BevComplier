#ifndef control_hpp
#define control_hpp

#include <stdio.h>
#include "headers.hpp"
#include "enums.hpp"

class ingress{
private:
    headers  hdr ;
    const int actionNumber = 0;
public:

	typedef void (ingress::*ingress_ActionFn)(cppgen::P4BitSet<160> data);
	typedef struct ingress_action_data {

        cppgen::P4BitSet<160> data;
        ingress::ingress_ActionFn fn;
        bool hit;
        action_enum action_run;
    } ingress_action_data;
    
    const ingress_ActionFn ingress_actions[1] = {
    	&ingress::set_bd_dmac_intf_action,
    };
    
    const void* action_map[1] = {
    	"set_bd_dmac_intf",
    };
    void apply(headers*  hdr_ptr);
    void ipv4_da_lpm_lookup(int tableId, cppgen::P4BitSet<160> *tableKey, ingress_action_data* ingress_action_data_obj);
    void ipv4_da_lpm_match(ingress_action_data* ingress_action_data_obj);
    ingress_action_data ipv4_da_lpm_apply();
    
    
    void set_bd_dmac_intf( cppgen::P4BitSet<24> bd,  cppgen::P4BitSet<48> dmac,  cppgen::P4BitSet<2> intf);
    void set_bd_dmac_intf_action(cppgen::P4BitSet<160> data);
    
};

#endif /* control_hpp */