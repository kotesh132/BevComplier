package com.p4.drmt.parser;

import com.p4.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data

public class DPH {
    private final Map<String, Set<Integer>> doffPos;// = new LinkedHashMap<>();
    private final Map<String, Integer> dpos;// = new LinkedHashMap<>();
    private final Map<String, Set<Integer>> dposmask;// = new LinkedHashMap<>();

    public static DPH CANDIDATE = new DPH(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
    static{
        CANDIDATE.doffPos.put("parse_dummy", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_pie", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_ethernet", Utils.linkedHashSet(0,1));
        CANDIDATE.doffPos.put("parse_ipv4", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_ipv6", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_tcp", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_udp", Utils.linkedHashSet(0));
        CANDIDATE.doffPos.put("parse_inner_ethernet", Utils.linkedHashSet(0,1));
        CANDIDATE.doffPos.put("parse_inner_ipv4", Utils.linkedHashSet(1));
        CANDIDATE.doffPos.put("parse_inner_ipv6", Utils.linkedHashSet(1));
        CANDIDATE.doffPos.put("parse_inner_tcp", Utils.linkedHashSet(1));
        CANDIDATE.doffPos.put("parse_inner_udp", Utils.linkedHashSet(1));

        //CANDIDATE.dpos.put("parse_dummy", 0);
        CANDIDATE.dpos.put("parse_pie", 0);
        CANDIDATE.dpos.put("parse_ethernet", 0);
        CANDIDATE.dpos.put("parse_ipv4", 0);
        CANDIDATE.dpos.put("parse_ipv6", 0);
        CANDIDATE.dpos.put("parse_tcp", 0);
        CANDIDATE.dpos.put("parse_udp", 0);
        CANDIDATE.dpos.put("parse_inner_ethernet", 1);
        CANDIDATE.dpos.put("parse_inner_ipv4", 0);
        CANDIDATE.dpos.put("parse_inner_ipv6", 0);

        CANDIDATE.dposmask.put("parse_ethernet", Utils.linkedHashSet(1));
        CANDIDATE.dposmask.put("parse_inner_ethernet", Utils.linkedHashSet(0));


    }


}
