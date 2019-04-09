#include <stdio.h>
#include "control.hpp"
#include "enums.hpp"
#include "primitives.hpp"
#include "pd.hpp"
#include "iostream"

void ingress::set_bd_dmac_intf( cppgen::P4BitSet<24> bd,  cppgen::P4BitSet<48> dmac,  cppgen::P4BitSet<2> intf){
        meta.fwd_metadata.out_bd = bd;
        hdr.ethernet.dstAddr = dmac;
        standard_metadata.egress_port = intf;
    }
    
void ingress::set_bd_dmac_intf_action(cppgen::P4BitSet<160> data){
cppgen::P4BitSet<24> bd;
bd = data["23:0"];
print_hex_from_bit("bd: ", bd, 24);
cppgen::P4BitSet<48> dmac;
dmac = data["47:0"];
print_hex_from_bit("dmac: ", dmac, 48);
cppgen::P4BitSet<2> intf;
intf = data["1:0"];
print_hex_from_bit("intf: ", intf, 2);
set_bd_dmac_intf( bd,  dmac,  intf);
}


void ingress::ipv4_da_lpm_lookup(int tableId, cppgen::P4BitSet<160> *tableKey, ingress::ingress_action_data* ingress_action_data_obj){

    uint8_t tableData[21] = {0};

    bool hit = table_look_up(tableId, *tableKey, tableData);

    ingress_action_data_obj->action_run = (action_enum)tableData[0];
    ingress_action_data_obj->fn = ingress_actions[tableData[0] - actionNumber];
    copyToBitvar(&(ingress_action_data_obj->data), tableData+1, 20 * 8);
    ingress_action_data_obj->hit = hit;

    printf("Action to run: %s\n", action_map[tableData[0] - actionNumber]);
}


void ingress::ipv4_da_lpm_match(ingress::ingress_action_data* ingress_action_data_obj){

    //Key generation
    cppgen::P4BitSet<160> tableKey;
    int bitOffset = 0;
    std::string slice = "";
    
    //    copyBits(hdr.ipv4.dstAddr, &tableKey, bitOffset, 32);
    slice = std::to_string(bitOffset + 32 - 1) +":" + std::to_string(bitOffset);
    tableKey[slice] = hdr.ipv4.dstAddr;
    bitOffset = bitOffset + 32;
    
    //    copyBits(hdr.tcp.dstPort, &tableKey, bitOffset, 16);
    slice = std::to_string(bitOffset + 16 - 1) +":" + std::to_string(bitOffset);
    tableKey[slice] = hdr.tcp.dstPort;
    bitOffset = bitOffset + 16;
    
    //lookup
    print_hex_from_bit("key generated :", tableKey, bitOffset);
    ipv4_da_lpm_lookup(1, &tableKey, ingress_action_data_obj);

}

ingress::ingress_action_data ingress::ipv4_da_lpm_apply(){
    printf("\nApplying match on table: ipv4_da_lpm\n");

    ingress::ingress_action_data ingress_action_data_obj;
    //initialization
    ingress_action_data_obj.hit = false;
    ingress_action_data_obj.fn = NULL;

    //perform match
    ipv4_da_lpm_match(&ingress_action_data_obj);

    //execute action
    (this->*ingress_action_data_obj.fn)(ingress_action_data_obj.data);

    return ingress_action_data_obj;
}


void ingress::apply(headers* hdr_ptr){
    //copyIn
    this->hdr = *hdr_ptr;

	{
        if( hdr.ethernet.srcAddr==123456 && hdr.ethernet.dstAddr > 2){ 
        	if (hdr.ethernet.srcAddr !=hdr.ethernet.dstAddr) 
            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr;        
			else if ((hdr.ipv4.isValid()) && !(hdr.ipv6.isValid())) 
            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 2;        
        	else
            	hdr.ethernet.dstAddr = hdr.ethernet.srcAddr + 3;        
		}
        ipv4_da_lpm_apply();
    }

    //copyOut
    *hdr_ptr = hdr;
}

