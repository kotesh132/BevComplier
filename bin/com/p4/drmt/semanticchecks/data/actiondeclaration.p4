@name("action4") action action4(bit<32> src_adr) {
	hdr.ipv4.srcAddr = src_adr;
	 switch (int_insert.apply().action_run) {
            int_transit: {
                if (meta.int_metadata.insert_cnt != 8w0) {
                    int_inst_0003.apply();
                    int_bos.apply();
                }
                int_meta_header_update.apply();
            }
        }
        switch (int_insert.apply().action_run) {
            int_transit: {
                if (meta.int_metadata.insert_cnt != 8w0) {
                    int_inst_0003.apply();
                    int_bos.apply();
                }
                int_meta_header_update.apply();
            }
        }
}