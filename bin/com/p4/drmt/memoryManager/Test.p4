header udp_t {
bit<16> srcPort;
bit<16> dstPort;
}
struct headers {
    udp_t udp;
}

headers() hdr;
control ingress(inout headers hdr){
apply {
hdr.udp.srcPort = 1;
}
}