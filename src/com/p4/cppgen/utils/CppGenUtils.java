package com.p4.cppgen.utils;

import com.p4.p416.gen.ArrayIndexContextExt;
import com.p4.p416.gen.ArrayIndexLvalueContextExt;
import com.p4.p416.gen.RangeIndexContextExt;
import com.p4.p416.gen.RangeIndexLvalueContextExt;
import com.p4.p416.iface.ICPPTransformable;
import com.p4.p416.iface.IIRNode;

import java.util.LinkedHashMap;

public class CppGenUtils {


    public static void transformIntegersToString(IIRNode node, LinkedHashMap<String, String> map) {
        //x > 5              to x > "5"
        //x > 0x5            to x > "0x5"
        //x[1] > 5           to x > "5"
        //x[1] > (bit<32>)5  to x[1] > (bit<32>)"5"
        //etc..

        node.postVisitNode(anode -> {
                    if (anode instanceof ICPPTransformable) {
                        ((ICPPTransformable) anode).transformToCPPCode(map);
                    }
                },
                anode -> !(anode instanceof ArrayIndexContextExt) &&
                        !(anode instanceof ArrayIndexLvalueContextExt) &&
                        !(anode instanceof RangeIndexContextExt) &&
                        !(anode instanceof RangeIndexLvalueContextExt));
    }

}
