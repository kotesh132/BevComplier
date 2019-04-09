package com.p4.p416.iface;

import java.util.LinkedHashMap;

public interface ICPPTransformable {

    void transformToCPPCode(LinkedHashMap<String, String> map);
}
