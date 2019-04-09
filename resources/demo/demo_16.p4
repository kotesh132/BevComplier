#include <core.p4>
#include <v1model.p4>

struct fwd_metadata_t {
    bit<20> l2ptr;
    bit<24> out_bd;
    bit<48> out_dmac;
    bit<22> out_intf;
}

header ethernet_t {
    bit<48> dstAddr;
    bit<48> srcAddr;
    bit<16> etherType;
}

header ipv4_t {
    bit<4>  version;
    bit<4>  ihl;
    bit<8>  diffserv;
    bit<16> totalLen;
    bit<16> identification;
    bit<3>  flags;
    bit<13> fragOffset;
    bit<8>  ttl;
    bit<8>  protocol;
    bit<16> hdrChecksum;
    bit<32> srcAddr;
    bit<32> dstAddr;
}

struct metadata {
    @name("fwd_metadata") 
    fwd_metadata_t fwd_metadata;
}

struct headers {
    @name("ethernet") 
    ethernet_t ethernet;
    @name("ipv4") 
    ipv4_t     ipv4;
}

parser ParserImpl(packet_in packet, out headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {
    @name("parse_ethernet") state parse_ethernet {
        packet.extract(hdr.ethernet);
        transition select(hdr.ethernet.etherType) {
            16w0x800: parse_ipv4;
            default: accept;
        }
    }
    @name("parse_ipv4") state parse_ipv4 {
        packet.extract(hdr.ipv4);
        transition accept;
    }
    @name("start") state start {
        transition parse_ethernet;
    }
}

control egress(inout headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {
    @name("rewrite_mac") action rewrite_mac(bit<48> smac) {
        hdr.ethernet.srcAddr = smac;
    }
    @name("_drop") action _drop() {
        mark_to_drop();
    }
    @name("send_frame") table send_frame() {
        actions = {
            rewrite_mac;
            _drop;
            @default_only NoAction;
        }
        key = {
            standard_metadata.egress_port: exact;
        }
        size = 256;
        default_action = NoAction();
    }
    apply {
        send_frame.apply();
    }
}

control ingress(inout headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {
    @name("set_l2ptr") action set_l2ptr(bit<20> l2ptr) {
        meta.fwd_metadata.l2ptr = l2ptr;
    }
    @name("_drop") action _drop() {
        mark_to_drop();
    }
    @name("ipv4_drop") action ipv4_drop() {
        meta.fwd_metadata.l2ptr = 20w0;
        _drop();
    }
    @name("set_bd_dmac_intf") action set_bd_dmac_intf(bit<24> bd, bit<48> dmac, bit<9> intf) {
        meta.fwd_metadata.out_bd = bd;
        hdr.ethernet.dstAddr = dmac;
        standard_metadata.egress_spec = intf;
    }
    @name("mac_drop") action mac_drop() {
        standard_metadata.egress_spec = 9w511;
    }
    @name("ipv4_da_lpm") table ipv4_da_lpm() {
        actions = {
            set_l2ptr;
            ipv4_drop;
            @default_only NoAction;
        }
        key = {
            hdr.ipv4.dstAddr: lpm;
        }
        default_action = NoAction();
    }
    @name("mac_da") table mac_da() {
        actions = {
            set_bd_dmac_intf;
            mac_drop;
            @default_only NoAction;
        }
        key = {
            meta.fwd_metadata.l2ptr: exact;
        }
        default_action = NoAction();
    }
    apply {
        ipv4_da_lpm.apply();
        mac_da.apply();
    }
}

control DeparserImpl(packet_out packet, in headers hdr) {
    apply {
        packet.emit(hdr.ethernet);
        packet.emit(hdr.ipv4);
    }
}

control verifyChecksum(in headers hdr, inout metadata meta) {
    apply {
    }
}

control computeChecksum(inout headers hdr, inout metadata meta) {
    apply {
    }
}

V1Switch(ParserImpl(), verifyChecksum(), ingress(), egress(), computeChecksum(), DeparserImpl()) main;
