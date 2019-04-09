@name("NoAction")
	action NoAction() {}

	match_kind {
	    exact,
	    ternary,
	    lpm
	}

	struct standard_metadata_t {
	    bit<8>  ingress_port;
	    bit<8>  egress_port;
	}

	struct fwd_metadata_t {
	    bit<24> l2ptr;
	    bit<24> out_bd;
	}

	struct l3_metadata_t {
	    bit<16> lkp_outer_l4_sport;
	    bit<16> lkp_outer_l4_dport;
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

	header ipv6_t {
	    bit<4>   version;
	    bit<8>   trafficClass;
	    bit<20>  flowLabel;
	    bit<16>  payloadLen;
	    bit<8>   nextHdr;
	    bit<8>   hopLimit;
	    bit<128> srcAddr;
	    bit<128> dstAddr;
	}

	header tcp_t {
	    bit<16> srcPort;
	    bit<16> dstPort;
	    bit<32> seqNo;
	    bit<32> ackNo;
	    bit<4>  dataOffset;
	    bit<4>  res;
	    bit<8>  flags;
	    bit<16> window;
	    bit<16> checksum;
	    bit<16> urgentPtr;
	}

	header udp_t {
	    bit<16> srcPort;
	    bit<16> dstPort;
	    bit<16> length;
	    bit<16> checksum;
	}

	struct metadata {
	    @name("fwd_metadata") 
	    fwd_metadata_t fwd_metadata;
	    @name("l3_metadata") 
	    l3_metadata_t  l3_metadata;
	}

	struct headers {
	    @name("ethernet") 
	    ethernet_t ethernet;
	    @name("ipv4") 
	    ipv4_t     ipv4;
	    @name("ipv6") 
	    ipv6_t     ipv6;
	    @name("tcp") 
	    tcp_t      tcp;
	    @name("udp") 
	    udp_t      udp;
	}

	headers() hdr;
	metadata() meta;
	standard_metadata_t() standard_metadata;

			
	control egress(inout headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {

    @name("_nop") action _nop() {
    }
    @name("NoAction") action NoAction(bit<16> smac) {
    }
    @name("rewrite_mac") action rewrite_mac(bit<48> smac) {
        hdr.ethernet.srcAddr = smac;
    }
    @name("_drop") action _drop() {
        mark_to_drop();
    }
    @name("send_frame") table send_frame {
        actions = {
            rewrite_mac;
	        NoAction;
            @default_only NoAction;
        }
        key = {
            hdr.ipv4.srcAddr: exact;
	    hdr.ipv4.hdrChecksum: exact;
        }
        size = 256;
        default_action = NoAction();
    }
    @name("mac_da") table dummy {
        actions = {
	        NoAction;
            @default_only NoAction;
        }
        key = {
            hdr.ipv4.srcAddr: exact;
	        hdr.ipv4.hdrChecksum: exact;
        }
        size = 128;
        default_action = NoAction();
    }
    apply {
        send_frame.apply();
    }
}
    control egress1(inout headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {

    @name("_nop") action _nop() {
    }
    @name("NoAction") action NoAction(bit<16> smac) {
    }
    @name("rewrite_mac") action rewrite_mac(bit<48> smac) {
        hdr.ethernet.srcAddr = smac;
    }
    @name("_drop") action _drop() {
        mark_to_drop();
    }
    @name("send_frame1") table send_frame1 {
        actions = {
	        NoAction;
            @default_only NoAction;
        }
        key = {
            hdr.ipv4.srcAddr: lpm;
	    hdr.ipv4.hdrChecksum: exact;
        }
        size = 128;
        default_action = NoAction();
    }
    @name("mac_da") table dummy1 {
        actions = {
	        rewrite_mac;
            @default_only NoAction;
        }
        key = {
            hdr.ipv4.srcAddr: exact;
	        hdr.ipv4.hdrChecksum: lpm;
        }
        size = 128;
        default_action = NoAction();
    }
    apply {
        send_frame.apply();
    }
}