header mpls_t {  
bit<4> version; 
bit<4> reserved; 
}
struct headers{
                mpls_t[2] mpls;
}
headers() hdr;
parser parserI(out headers hdr)
{
   state start {
        transition parse;
   }

state parse {
        transition parse_mpls;
   }

state parse_mpls { 
	 packet.extract(hdr.mpls.next); 
	 transition select(hdr.mpls.last.bos) { 
	      0: parse_mpls;
	      1: guess_mpls_payload; 
	  }
}
state guess_mpls_payload {
transition select(packet.lookahead<ip46_t>().version) {
	   4 : parse_dummy;
           }
}

state parse_dummy {
        transition select(hdr.ipv6.nextHdr) {
            default: accept;
        }
}
}
