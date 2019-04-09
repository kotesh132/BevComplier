#ifndef phvdeparser_hpp
#define phvdeparser_hpp

#include <stdio.h>
#include "headers.hpp"
#include "primitives.hpp"

template <int M, int N>
void deParsePacketVector(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit,headers* hdr){
	emit_hdr(packet, packetBit, hdr);
}

template <int M, int N>
void emit_hdr(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, headers* hdr){
	emit_ethernet(packet, packetBit, &(hdr->ethernet));
	emit_ipv4(packet, packetBit, &(hdr->ipv4));
	emit_ipv6(packet, packetBit, &(hdr->ipv6));
	emit_tcp(packet, packetBit, &(hdr->tcp));
	emit_udp(packet, packetBit, &(hdr->udp));
}

template <int M, int N>
void emit_tcp(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, tcp_t* tcp){
	(*packetBit)[3] = tcp->isValid() ? 1 : 0;
    
    if (tcp->isValid()) {
	(*packet)["631:616"] = tcp->srcPort;
	(*packet)["647:632"] = tcp->dstPort;
	(*packet)["679:648"] = tcp->seqNo;
	(*packet)["711:680"] = tcp->ackNo;
	(*packet)["715:712"] = tcp->dataOffset;
	(*packet)["723:720"] = tcp->res;
	(*packet)["735:728"] = tcp->flags;
	(*packet)["751:736"] = tcp->window;
	(*packet)["767:752"] = tcp->checksum;
	(*packet)["783:768"] = tcp->urgentPtr;
	}
}
template <int M, int N>
void emit_udp(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, udp_t* udp){
	(*packetBit)[4] = udp->isValid() ? 1 : 0;
    
    if (udp->isValid()) {
	(*packet)["799:784"] = udp->srcPort;
	(*packet)["815:800"] = udp->dstPort;
	(*packet)["831:816"] = udp->length;
	(*packet)["847:832"] = udp->checksum;
	}
}
template <int M, int N>
void emit_ethernet(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ethernet_t* ethernet){
	(*packetBit)[0] = ethernet->isValid() ? 1 : 0;
    
    if (ethernet->isValid()) {
	(*packet)["47:0"] = ethernet->dstAddr;
	(*packet)["95:48"] = ethernet->srcAddr;
	(*packet)["111:96"] = ethernet->etherType;
	}
}
template <int M, int N>
void emit_ipv4(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ipv4_t* ipv4){
	(*packetBit)[1] = ipv4->isValid() ? 1 : 0;
    
    if (ipv4->isValid()) {
	(*packet)["115:112"] = ipv4->version;
	(*packet)["123:120"] = ipv4->ihl;
	(*packet)["135:128"] = ipv4->diffserv;
	(*packet)["151:136"] = ipv4->totalLen;
	(*packet)["167:152"] = ipv4->identification;
	(*packet)["170:168"] = ipv4->flags;
	(*packet)["188:176"] = ipv4->fragOffset;
	(*packet)["199:192"] = ipv4->ttl;
	(*packet)["207:200"] = ipv4->protocol;
	(*packet)["223:208"] = ipv4->hdrChecksum;
	(*packet)["255:224"] = ipv4->srcAddr;
	(*packet)["287:256"] = ipv4->dstAddr;
	}
}
template <int M, int N>
void emit_ipv6(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ipv6_t* ipv6){
	(*packetBit)[2] = ipv6->isValid() ? 1 : 0;
    
    if (ipv6->isValid()) {
	(*packet)["291:288"] = ipv6->version;
	(*packet)["303:296"] = ipv6->trafficClass;
	(*packet)["323:304"] = ipv6->flowLabel;
	(*packet)["343:328"] = ipv6->payloadLen;
	(*packet)["351:344"] = ipv6->nextHdr;
	(*packet)["359:352"] = ipv6->hopLimit;
	(*packet)["487:360"] = ipv6->srcAddr;
	(*packet)["615:488"] = ipv6->dstAddr;
	}
}

#endif /* phvdeparser_hpp */