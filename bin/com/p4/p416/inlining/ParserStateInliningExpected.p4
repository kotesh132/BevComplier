header mpls_t {  
bit<4> version; 
bit<4> reserved; 
}
struct headers{
                mpls_t mpls_0 ;
mpls_t mpls_1 ;

}
headers() hdr;
parser parserI(out headers hdr)
{
   state start{ 
transition parse;
} 
state parse{ 
transition parse_mpls_0 ;
} 
state parse_mpls_0{ 
packet.extract(hdr.mpls_0);
transition select(hdr.mpls_0.bos) { 
	      0 : parse_mpls_1 ;
	      1 : guess_mpls_payload ; 
	  }
} 
state parse_mpls_1{ 
packet.extract(hdr.mpls_1);
transition select(hdr.mpls_1.bos) {
	      1 : guess_mpls_payload ; 
	  }
} 
state guess_mpls_payload{ 
transition select(packet.lookahead<ip46_t>().version) {
	   4 : parse_dummy;
           }
} 
state parse_dummy{ 
transition select(hdr.ipv6.nextHdr) {
            default: accept;
        }
} 

}
