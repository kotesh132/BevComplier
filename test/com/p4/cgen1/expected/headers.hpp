#ifndef headers_hpp
#define headers_hpp


#include "P4BitSet.h"

class ethernet_t{
public:
    cppgen::P4BitSet<48> dstAddr;
    cppgen::P4BitSet<48> srcAddr;
    cppgen::P4BitSet<16> etherType;
	bool valid;

    bool isValid();
    void setValid(bool valid);
};

class ipv4_t{
public:
    cppgen::P4BitSet<4> version;
    cppgen::P4BitSet<4> ihl;
    cppgen::P4BitSet<8> diffserv;
    cppgen::P4BitSet<16> totalLen;
    cppgen::P4BitSet<16> identification;
    cppgen::P4BitSet<3> flags;
    cppgen::P4BitSet<13> fragOffset;
    cppgen::P4BitSet<8> ttl;
    cppgen::P4BitSet<8> protocol;
    cppgen::P4BitSet<16> hdrChecksum;
    cppgen::P4BitSet<32> srcAddr;
    cppgen::P4BitSet<32> dstAddr;
	bool valid;

    bool isValid();
    void setValid(bool valid);
};

class ipv6_t{
public:
    cppgen::P4BitSet<4> version;
    cppgen::P4BitSet<8> trafficClass;
    cppgen::P4BitSet<20> flowLabel;
    cppgen::P4BitSet<16> payloadLen;
    cppgen::P4BitSet<8> nextHdr;
    cppgen::P4BitSet<8> hopLimit;
    cppgen::P4BitSet<128> srcAddr;
    cppgen::P4BitSet<128> dstAddr;
	bool valid;

    bool isValid();
    void setValid(bool valid);
};

class tcp_t{
public:
    cppgen::P4BitSet<16> srcPort;
    cppgen::P4BitSet<16> dstPort;
    cppgen::P4BitSet<32> seqNo;
    cppgen::P4BitSet<32> ackNo;
    cppgen::P4BitSet<4> dataOffset;
    cppgen::P4BitSet<4> res;
    cppgen::P4BitSet<8> flags;
    cppgen::P4BitSet<16> window;
    cppgen::P4BitSet<16> checksum;
    cppgen::P4BitSet<16> urgentPtr;
	bool valid;

    bool isValid();
    void setValid(bool valid);
};

class udp_t{
public:
    cppgen::P4BitSet<16> srcPort;
    cppgen::P4BitSet<16> dstPort;
    cppgen::P4BitSet<16> length;
    cppgen::P4BitSet<16> checksum;
	bool valid;

    bool isValid();
    void setValid(bool valid);
};


class standard_metadata_t{
public:
	cppgen::P4BitSet<8> ingress_port;
	cppgen::P4BitSet<8> egress_port;
};

class fwd_metadata_t{
public:
	cppgen::P4BitSet<24> l2ptr;
	cppgen::P4BitSet<24> out_bd;
};

class l3_metadata_t{
public:
	cppgen::P4BitSet<16> lkp_outer_l4_sport;
	cppgen::P4BitSet<16> lkp_outer_l4_dport;
};

class metadata{
public:
	fwd_metadata_t fwd_metadata;
	l3_metadata_t l3_metadata;
};

class headers{
public:
	ethernet_t ethernet;
	ipv4_t ipv4;
	ipv6_t ipv6;
	tcp_t tcp;
	udp_t udp;
};

#endif
