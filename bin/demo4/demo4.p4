/* A test P4 program intended to test a relatively small parser, but
   one that does have several states and exercises a few things that a
   full-features parser must have. */

#include <core.p4>
#include <v1model.p4>

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
    bit<16> length_;
    bit<16> checksum;
}

struct headers {
    ethernet_t ethernet;
    ipv4_t ipv4;
    ipv6_t ipv6;
    tcp_t tcp;
    udp_t udp;
}

struct l3_metadata_t {
    bit<16> lkp_outer_l4_sport;
    bit<16> lkp_outer_l4_dport;
}

struct metadata {
    l3_metadata_t l3_metadata;
}


parser ParserImpl(packet_in packet,
                  out headers hdr,
                  inout metadata meta,
                  inout standard_metadata_t standard_metadata) {
   state parse_ethernet {
      packet.extract(hdr.ethernet);
      transition select(hdr.ethernet.etherType) {
         16w0x800: parse_ipv4;
         16w0x86dd: parse_ipv6;
         default: accept;
      }
   }
   state parse_ipv4 {
      packet.extract(hdr.ipv4);
      transition select(hdr.ipv4.fragOffset, hdr.ipv4.ihl, hdr.ipv4.protocol) {
         (13w0x0, 4w0x5, 8w0x6): parse_tcp;
         (13w0x0, 4w0x5, 8w0x11): parse_udp;
         default: accept;
      }
   }
   state parse_ipv6 {
      packet.extract(hdr.ipv6);
      transition select(hdr.ipv6.nextHdr) {
         8w6: parse_tcp;
         8w17: parse_udp;
         default: accept;
      }
   }
   state parse_tcp {
      packet.extract(hdr.tcp);
      meta.l3_metadata.lkp_outer_l4_sport = hdr.tcp.srcPort;
      meta.l3_metadata.lkp_outer_l4_dport = hdr.tcp.dstPort;
      transition accept;
   }
   state parse_udp {
      packet.extract(hdr.udp);
      meta.l3_metadata.lkp_outer_l4_sport = hdr.udp.srcPort;
      meta.l3_metadata.lkp_outer_l4_dport = hdr.udp.dstPort;
      transition accept;
   }
   state start {
      transition parse_ethernet;
   }

}

control DeparserI(packet_out packet,
                  in headers hdr) {
    apply {
        packet.emit(hdr.ethernet);
        packet.emit(hdr.ipv4);
        packet.emit(hdr.ipv6);
        packet.emit(hdr.tcp);
        packet.emit(hdr.udp);
    }
}

control cIngress(inout headers hdr,
                 inout metadata meta,
                 inout standard_metadata_t stdmeta) {
    apply {
    }
}

control cEgress(inout headers hdr,
                inout metadata meta,
                inout standard_metadata_t stdmeta) {
    apply { }
}

control vc(in headers hdr,
           inout metadata meta) {
    apply { }
}

control uc(inout headers hdr,
           inout metadata meta) {
    apply { }
}

V1Switch<headers, metadata>(ParserImpl(),
                            vc(),
                            cIngress(),
                            cEgress(),
                            uc(),
                            DeparserI()) main;
