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
    bit<1> validBit;
    bool isValid;
}


struct headers {
    @name("tcp") 
    tcp_t      tcp;
}


headers() hdr;

parser ParserImpl(out headers hdr) {
    @name("parse_udp") state parse_udp {
		
		hdr.tcp.res = hdr.tcp.res * (hdr.tcp.window-hdr.tcp.res);//Invalid Subtraction
		
		transition accept;
    }
    @name("start") state start {
        transition accept;
    }
}

//V1Switch(ParserImpl(), verifyChecksum(), ingress(), egress(), computeChecksum(), DeparserImpl()) main;