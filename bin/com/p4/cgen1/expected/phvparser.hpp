#ifndef phvparser_hpp
#define phvparser_hpp

#include <stdio.h>
#include "P4BitSet.h"
#include "headers.hpp"
#include "primitives.hpp"

template <int M, int N>
void parsePacketVector(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit,headers* hdr){
	extract_hdr(packet, packetBit, hdr);
}

template <int M, int N>
void extract_hdr(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, headers* hdr){
	extract_ethernet(packet, packetBit, &(hdr->ethernet));
	extract_ipv4(packet, packetBit, &(hdr->ipv4));
	extract_ipv6(packet, packetBit, &(hdr->ipv6));
	extract_tcp(packet, packetBit, &(hdr->tcp));
	extract_udp(packet, packetBit, &(hdr->udp));
}

template <int M, int N>
void extract_tcp(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, tcp_t* tcp){
	tcp->setValid((*packetBit)[3] == 1);
    
    if (tcp->isValid()) {
	tcp->srcPort = (*packet)["631:616"];
	tcp->dstPort = (*packet)["647:632"];
	tcp->seqNo = (*packet)["679:648"];
	tcp->ackNo = (*packet)["711:680"];
	tcp->dataOffset = (*packet)["715:712"];
	tcp->res = (*packet)["723:720"];
	tcp->flags = (*packet)["735:728"];
	tcp->window = (*packet)["751:736"];
	tcp->checksum = (*packet)["767:752"];
	tcp->urgentPtr = (*packet)["783:768"];
	}
}
template <int M, int N>
void extract_udp(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, udp_t* udp){
	udp->setValid((*packetBit)[4] == 1);
    
    if (udp->isValid()) {
	udp->srcPort = (*packet)["799:784"];
	udp->dstPort = (*packet)["815:800"];
	udp->length = (*packet)["831:816"];
	udp->checksum = (*packet)["847:832"];
	}
}
template <int M, int N>
void extract_ethernet(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ethernet_t* ethernet){
	ethernet->setValid((*packetBit)[0] == 1);
    
    if (ethernet->isValid()) {
	ethernet->dstAddr = (*packet)["47:0"];
	ethernet->srcAddr = (*packet)["95:48"];
	ethernet->etherType = (*packet)["111:96"];
	}
}
template <int M, int N>
void extract_ipv4(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ipv4_t* ipv4){
	ipv4->setValid((*packetBit)[1] == 1);
    
    if (ipv4->isValid()) {
	ipv4->version = (*packet)["115:112"];
	ipv4->ihl = (*packet)["123:120"];
	ipv4->diffserv = (*packet)["135:128"];
	ipv4->totalLen = (*packet)["151:136"];
	ipv4->identification = (*packet)["167:152"];
	ipv4->flags = (*packet)["170:168"];
	ipv4->fragOffset = (*packet)["188:176"];
	ipv4->ttl = (*packet)["199:192"];
	ipv4->protocol = (*packet)["207:200"];
	ipv4->hdrChecksum = (*packet)["223:208"];
	ipv4->srcAddr = (*packet)["255:224"];
	ipv4->dstAddr = (*packet)["287:256"];
	}
}
template <int M, int N>
void extract_ipv6(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ipv6_t* ipv6){
	ipv6->setValid((*packetBit)[2] == 1);
    
    if (ipv6->isValid()) {
	ipv6->version = (*packet)["291:288"];
	ipv6->trafficClass = (*packet)["303:296"];
	ipv6->flowLabel = (*packet)["323:304"];
	ipv6->payloadLen = (*packet)["343:328"];
	ipv6->nextHdr = (*packet)["351:344"];
	ipv6->hopLimit = (*packet)["359:352"];
	ipv6->srcAddr = (*packet)["487:360"];
	ipv6->dstAddr = (*packet)["615:488"];
	}
}

#endif /* phvparser_hpp */