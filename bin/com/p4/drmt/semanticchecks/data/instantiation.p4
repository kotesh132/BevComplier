control computeChecksum(inout headers hdr, inout metadata meta) {
    Counter(32w1024, CounterType.Both) ctr;
    apply {
    c() c_inst;
    }
}