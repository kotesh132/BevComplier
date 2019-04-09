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
    bit<1> validBit;
    bit oneBit;
    bool isValid;
}


struct headers {
    @name("tcp") 
    tcp_t      tcp;
}


headers() hdr;

parser ParserImpl(out headers hdr) {
    @name("parse_udp") state parse_udp {

		//Bit Width Truncation
		hdr.tcp.flags =  (bit<8>)(hdr.tcp.urgentPtr);//Valid
		hdr.tcp.flags =  (bit<8>)(hdr.tcp.ackNoInt);//inValid
		
		transition accept;
    }
    @name("start") state start {
        transition accept;
    }
}