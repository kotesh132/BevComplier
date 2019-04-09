error {
    NoError,           
    PacketTooShort,    
    NoMatch,           
    StackOutOfBounds,  
    OverwritingHeader, 
    HeaderTooShort,    
    ParserTimeout      
}

extern packet_in {
    
    void extract<T>(out T hdr);
    
    void extract<T>(out T variableSizeHeader,
                    in bit<32> variableFieldSizeInBits);
    
    T lookahead<T>();
    
    void advance(in bit<32> sizeInBits);
    
    bit<32> length();
}

extern packet_out {
    void emit<T>(in T hdr);
    void emit<T>(in bool condition, in T data);
}

extern void verify(in bool check, in error toSignal);

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

header ipv4_up_to_ihl_only_h {
    bit<4> version;
    bit<4> ihl;
}

header pie_t {
    bit<32> pie_word0;
    bit<32> pie_word1;
    bit<32> pie_word2;
    bit<32> pie_word3;
    bit<32> pie_word4;
    bit<32> pie_word5;
    bit<32> pie_word6;
    bit<32> pie_word7;
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
    varbit<240> options;
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

// This header type is only intended to be used for lookahead() calls.
header ipv6exhdr_up_to_hdrextlen_only_t {
    bit<8>   nextHdr;
    bit<8>   hdrExtLen;
}

header ipv6exhdr_hopbyhop_t {
    bit<8>   nextHdr;
    bit<8>   hdrExtLen;
    varbit<240> options;
}

// A value of N for the next value allows a routing header large
// enough to hold N/2 IPv6 addresses in the routing header.

header ipv6exhdr_routing_t {
    bit<8>   nextHdr;
    bit<8>   hdrExtLen;
    varbit<240> options;
}

header ipv6exhdr_destopts_t {
    bit<8>   nextHdr;
    bit<8>   hdrExtLen;
    varbit<240> options;
}

header_union ipv6exthdr_t {
    ipv6exhdr_hopbyhop_t  hopbyhop;
    ipv6exhdr_routing_t   routing;
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
    @name("pie_header")
    pie_t pie_header;
@name("ethernet") 
    ethernet_t ethernet;
@name("ipv4") 
    ipv4_t     ipv4;
@name("ipv6") 
    ipv6_t     ipv6;
@name ("ipv6_ext_0") 
ipv6exthdr_t ipv6ext_0 ;
@name ("ipv6_ext_1") 
ipv6exthdr_t ipv6ext_1 ;
@name ("ipv6_ext_2") 
ipv6exthdr_t ipv6ext_2 ;
@name("tcp") 
    tcp_t      tcp;
@name("udp") 
    udp_t      udp;

}

headers() hdr;
metadata() meta;
standard_metadata_t() standard_metadata;

parser ParserImpl(packet_in packet, out headers hdr, inout metadata meta, inout standard_metadata_t standard_metadata) {
    @name("parse_pie")state parse_pie{ 
packet.extract(hdr.pie_header);
transition parse_ethernet;
} 
@name("parse_ethernet")state parse_ethernet{ 
packet.extract(hdr.ethernet);
transition select(hdr.ethernet.etherType) {
            16w0x800: parse_ipv4;
            16w0x86dd: parse_ipv6;
            default: accept;
        }
} 
@name("parse_ipv4")state parse_ipv4{ 
packet.extract(hdr.ipv4,(bit<32>) (4 * ((bit<9>) (packet.lookahead<ipv4_up_to_ihl_only_h>().ihl))));
transition select(hdr.ipv4.fragOffset, hdr.ipv4.protocol) {
            (13w0x0, 8w0x6): parse_tcp;
            (13w0x0, 8w0x11): parse_udp;
            default: accept;
        }
} 
@name("parse_ipv6")state parse_ipv6{ 
packet.extract(hdr.ipv6);
transition select(hdr.ipv6.nextHdr) {
	    8w0x0 : parse_ipv6ext_hopbyhop_0 ; 
    	    8w0x2b : parse_ipv6ext_routing_0 ; 
            8w0x6: parse_tcp;
            8w0x11: parse_udp;
            default: accept;
        }
} 
@name("parse_ipv6ext_hopbyhop")state parse_ipv6ext_hopbyhop_0{ 
packet.extract(hdr.ipv6ext_0.hopbyhop,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8 ));
transition select (hdr.ipv6ext_0.hopbyhop.nextHdr) {
            8w0x0 : parse_ipv6ext_hopbyhop_1 ;
    	    8w0x2b : parse_ipv6ext_routing_1 ;
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_ipv6ext_hopbyhop")state parse_ipv6ext_hopbyhop_1{ 
packet.extract(hdr.ipv6ext_1.hopbyhop,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8 ));
transition select (hdr.ipv6ext_1.hopbyhop.nextHdr) {
            8w0x0 : parse_ipv6ext_hopbyhop_2 ;
    	    8w0x2b : parse_ipv6ext_routing_2 ;
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_ipv6ext_hopbyhop")state parse_ipv6ext_hopbyhop_2{ 
packet.extract(hdr.ipv6ext_2.hopbyhop,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8 ));
transition select (hdr.ipv6ext_2.hopbyhop.nextHdr) {
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_ipv6ext_routing")state parse_ipv6ext_routing_0{ 
packet.extract(hdr.ipv6ext_0.routing,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8));
transition select (hdr.ipv6ext_0.routing.nextHdr) {
            8w0x0 : parse_ipv6ext_hopbyhop_1 ;
    	    8w0x2b : parse_ipv6ext_routing_1 ;
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_ipv6ext_routing")state parse_ipv6ext_routing_1{ 
packet.extract(hdr.ipv6ext_1.routing,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8));
transition select (hdr.ipv6ext_1.routing.nextHdr) {
            8w0x0 : parse_ipv6ext_hopbyhop_2 ;
    	    8w0x2b : parse_ipv6ext_routing_2 ;
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_ipv6ext_routing")state parse_ipv6ext_routing_2{ 
packet.extract(hdr.ipv6ext_2.routing,
            (bit<32>) (8 * (packet.lookahead<ipv6exhdr_up_to_hdrextlen_only_t>().hdrExtLen) + 8));
transition select (hdr.ipv6ext_2.routing.nextHdr) {
            8w0x6 : parse_tcp ;
            8w0x11 : parse_udp ;
            default : accept ;
        }
} 
@name("parse_tcp")state parse_tcp{ 
packet.extract(hdr.tcp);
        meta.l3_metadata.lkp_outer_l4_sport = hdr.tcp.srcPort;
        meta.l3_metadata.lkp_outer_l4_dport = hdr.tcp.dstPort;
transition accept;
} 
@name("parse_udp")state parse_udp{ 
packet.extract(hdr.udp);
        meta.l3_metadata.lkp_outer_l4_sport = hdr.udp.srcPort;
        meta.l3_metadata.lkp_outer_l4_dport = hdr.udp.dstPort;
transition accept;
} 
@name("start")state start{ 
transition parse_pie;
} 
@name("parse_ipv6")state parse_test{ 
packet.extract(hdr.ipv6);
transition select(hdr.ipv6.nextHdr) {
	    8w0x0 : parse_ipv6ext_hopbyhop_0 ; 
            8w0x6: parse_tcp;
            8w0x11: parse_udp;
            default: accept;
        }
} 

}

V1Switch(ParserImpl(), verifyChecksum(), ingress(), egress(), computeChecksum(), DeparserImpl()) main;

