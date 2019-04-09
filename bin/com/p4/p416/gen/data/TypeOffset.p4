header ethernet_t {
    bit<48> dstAddr; //SizeInBytes=6 SizeInBits=48 TypeBitOffset=0 TypeByteOffset=0 BitOffset=0 ByteOffset=0
    bit<48> srcAddr; //SizeInBytes=6 SizeInBits=48 TypeBitOffset=48 TypeByteOffset=6 BitOffset=48 ByteOffset=6
    bit<16> etherType; //SizeInBytes=2 SizeInBits=16 TypeBitOffset=96 TypeByteOffset=12 BitOffset=96 ByteOffset=12
}

header ipv4_t {
    bit<4>  version; 		//SizeInBytes=1 SizeInBits=4 TypeBitOffset=0 TypeByteOffset=0 BitOffset=112 ByteOffset=14
    bit<4>  ihl;     		//SizeInBytes=1 SizeInBits=4 TypeBitOffset=4 TypeByteOffset=1 BitOffset=116 ByteOffset=15
    bit<8>  diffserv;		//SizeInBytes=1 SizeInBits=8 TypeBitOffset=8 TypeByteOffset=2 BitOffset=120 ByteOffset=16
    bit<16> totalLen;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=16 TypeByteOffset=3 BitOffset=128 ByteOffset=17
    bit<16> identification; //SizeInBytes=2 SizeInBits=16 TypeBitOffset=32 TypeByteOffset=5 BitOffset=144 ByteOffset=19
    bit<3>  flags;			//SizeInBytes=1 SizeInBits=3 TypeBitOffset=48 TypeByteOffset=7 BitOffset=160 ByteOffset=21
    bit<13> fragOffset;		//SizeInBytes=2 SizeInBits=13 TypeBitOffset=51 TypeByteOffset=8 BitOffset=163 ByteOffset=22
    bit<8>  ttl;			//SizeInBytes=1 SizeInBits=8 TypeBitOffset=64 TypeByteOffset=10 BitOffset=176 ByteOffset=24
    bit<8>  protocol;		//SizeInBytes=1 SizeInBits=8 TypeBitOffset=72 TypeByteOffset=11 BitOffset=184 ByteOffset=25
    bit<16> hdrChecksum;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=80 TypeByteOffset=12 BitOffset=192 ByteOffset=26
    bit<32> srcAddr;		//SizeInBytes=4 SizeInBits=32 TypeBitOffset=96 TypeByteOffset=14 BitOffset=208 ByteOffset=28
    bit<32> dstAddr;		//SizeInBytes=4 SizeInBits=32 TypeBitOffset=118 TypeByteOffset=18 BitOffset=240 ByteOffset=32
}

header ipv6_t {
    bit<4>   version;		//SizeInBytes=1 SizeInBits=4 TypeBitOffset=0 TypeByteOffset=0 BitOffset=272 ByteOffset=36
    bit<8>   trafficClass;	//SizeInBytes=1 SizeInBits=8 TypeBitOffset=4 TypeByteOffset=1 BitOffset=276 ByteOffset=37
    bit<20>  flowLabel;		//SizeInBytes=3 SizeInBits=20 TypeBitOffset=12 TypeByteOffset=2 BitOffset=284 ByteOffset=38
    bit<16>  payloadLen;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=32 TypeByteOffset=5 BitOffset=304 ByteOffset=41
    bit<8>   nextHdr;		//SizeInBytes=1 SizeInBits=8 TypeBitOffset=48 TypeByteOffset=7 BitOffset=320 ByteOffset=43
    bit<8>   hopLimit;		//SizeInBytes=1 SizeInBits=8 TypeBitOffset=56 TypeByteOffset=8 BitOffset=328 ByteOffset=44
    bit<128> srcAddr;		//SizeInBytes=16 SizeInBits=128 TypeBitOffset=64 TypeByteOffset=9 BitOffset=336 ByteOffset=45
    bit<128> dstAddr;		//SizeInBytes=16 SizeInBits=128 TypeBitOffset=192 TypeByteOffset=25 BitOffset=464 ByteOffset=61
}

header tcp_t {
    bit<16> srcPort;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=0 TypeByteOffset=0 BitOffset=592 ByteOffset=77
    bit<16> dstPort;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=16 TypeByteOffset=2 BitOffset=608 ByteOffset=79
    bit<32> seqNo;			//SizeInBytes=4 SizeInBits=32 TypeBitOffset=32 TypeByteOffset=4 BitOffset=624 ByteOffset=81
    bit<32> ackNo;			//SizeInBytes=4 SizeInBits=32 TypeBitOffset=64 TypeByteOffset=8 BitOffset=656 ByteOffset=85
    bit<4>  dataOffset;		//SizeInBytes=1 SizeInBits=4 TypeBitOffset=96 TypeByteOffset=12 BitOffset=688 ByteOffset=89
    bit<4>  res;			//SizeInBytes=1 SizeInBits=4 TypeBitOffset=100 TypeByteOffset=13 BitOffset=692 ByteOffset=90
    bit<8>  flags;			//SizeInBytes=1 SizeInBits=8 TypeBitOffset=104 TypeByteOffset=14 BitOffset=696 ByteOffset=91
    bit<16> window;			//SizeInBytes=2 SizeInBits=16 TypeBitOffset=112 TypeByteOffset=15 BitOffset=704 ByteOffset=92
    bit<16> checksum;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=128 TypeByteOffset=17 BitOffset=720 ByteOffset=94
    bit<16> urgentPtr;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=144 TypeByteOffset=19 BitOffset=736 ByteOffset=96
}

header udp_t {
    bit<16> srcPort;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=0 TypeByteOffset=0 BitOffset=752 ByteOffset=98
    bit<16> dstPort;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=16 TypeByteOffset=2 BitOffset=768 ByteOffset=100
    bit<16> length;		//SizeInBytes=2 SizeInBits=16 TypeBitOffset=32 TypeByteOffset=4 BitOffset=784 ByteOffset=102
    bit<16> checksum;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=48 TypeByteOffset=6 BitOffset=800 ByteOffset=104
}


struct headers {
    ethernet_t ethernet; //SizeInBytes=14 SizeInBits=112 TypeBitOffset=0 TypeByteOffset=0 BitOffset=0 ByteOffset=0 
    ipv4_t     ipv4; //SizeInBytes=23 SizeInBits=160 TypeBitOffset=112 TypeByteOffset=14 BitOffset=112 ByteOffset=14
    ipv6_t     ipv6; //SizeInBytes=41 SizeInBits=320 TypeBitOffset=272 TypeByteOffset=37 BitOffset=272 ByteOffset=36
    tcp_t      tcp; //SizeInBytes=21 SizeInBits=160 TypeBitOffset=586 TypeByteOffset=78 BitOffset=592 ByteOffset=77
    udp_t      udp; //SizeInBytes=8 SizeInBits=64 TypeBitOffset=746 TypeByteOffset=99 BitOffset=752 ByteOffset=98
}



struct fwd_metadata_t {
    bit<24> l2ptr;		//SizeInBytes=3 SizeInBits=24 TypeBitOffset=0 TypeByteOffset=0 BitOffset=816 ByteOffset=106
    bit<24> out_bd;		//SizeInBytes=3 SizeInBits=24 TypeBitOffset=24 TypeByteOffset=3 BitOffset=840 ByteOffset=109
}

struct l3_metadata_t {
    bit<16> lkp_outer_l4_sport;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=0 TypeByteOffset=0 BitOffset=864 ByteOffset=112
    bit<16> lkp_outer_l4_dport; //SizeInBytes=2 SizeInBits=16 TypeBitOffset=16 TypeByteOffset=2 BitOffset=880 ByteOffset=114
}

struct metadata {
    fwd_metadata_t fwd_metadata; //SizeInBytes=6 SizeInBits=48 TypeBitOffset=0 TypeByteOffset=0 BitOffset=816 ByteOffset=106
    l3_metadata_t  l3_metadata;	//SizeInBytes=4 SizeInBits=32 TypeBitOffset=48 TypeByteOffset=6 BitOffset=864 ByteOffset=112
}


struct standard_metadata_t {
    bit<8>  ingress_port;	//SizeInBytes=1 SizeInBits=8 TypeBitOffset=0 TypeByteOffset=0 BitOffset=896 ByteOffset=116
    bit<8>  egress_port;
}


headers() hdr;	//SizeInBytes=106 SizeInBits=816 TypeBitOffset=0 TypeByteOffset=0 BitOffset=0 ByteOffset=0

metadata() meta;	//SizeInBytes=10 SizeInBits=80 TypeBitOffset=816 TypeByteOffset=106 BitOffset=816 ByteOffset=106

standard_metadata_t() std_meta;	//SizeInBytes=2 SizeInBits=16 TypeBitOffset=896 TypeByteOffset=116 BitOffset=896 ByteOffset=116
