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

struct headers {
    @name("ipv4") 
    ipv4_t     ipv4;
}

headers() hdr;

control DeparserImpl(headers hdr) {
    apply {
		if	(hdr.ipv4.isValid()){      //It's a valid check and is expected to be of bool type. Thus, the O/P shouldn't have any ERROR
		//Do something
    	}
    }
}
