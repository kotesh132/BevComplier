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
    int<16> checksumInt;
    int<8> urgentPointerInt;
    bit<16> urgentPtr;
    int<32> ackNoInt;
    bit<1> flagBit;
    bit oneBit;
    bit secondBit;
    bool isValid;
}


struct headers {
    @name("tcp") 
    tcp_t      tcp;
}


headers() hdr;



control ingress(out headers hdr) {
	@name("action1") action action1(bit<24> ptr) {
	
			hdr.tcp.urgentPointerInt = hdr.tcp.urgentPointerInt+(((32+88)));//valid statement
			hdr.tcp.urgentPointerInt = 8s10 + (32+88);//valid statement
			
			hdr.tcp.urgentPointerInt = 8s10 + (hdr.tcp.checksumInt);//invalid statement as Non Integer Literal can't be implicitly typecast

    }
    apply{
    }
}