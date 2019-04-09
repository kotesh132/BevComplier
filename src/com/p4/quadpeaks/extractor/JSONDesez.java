package com.p4.quadpeaks.extractor;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONDesez {

    public static MemoryMap extractorMap(File path){
        MemoryMap m = new MemoryMap();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> obj = mapper.readValue(path, new TypeReference<LinkedHashMap>() {
            });


            for(Map.Entry<String, Object> key: obj.entrySet()){
                if(key.getKey().equals("qua_pxr_byte_vector")){
                    ExNode ex = unroll((LinkedHashMap)key.getValue(), key.getKey());
                    ex.calcOffset(0, m);
                    //System.out.println(ex.summary());
                    //System.out.println(ex.summary());
                }else if(key.getKey().equals("qua_pxr_bit_vector")){
                    ExNode ex = unroll((LinkedHashMap)key.getValue(), key.getKey());
                    ex.calcOffset(0,m);
                    //System.out.println(ex.summary());
                }
            }
            System.out.println(m.summary());

        }catch (Exception e){
           e.printStackTrace();
        }
        return m;
    }


    public static void main(String args[]) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> obj = mapper.readValue(new File("/Users/sambaner/ws/nt1/sw/p4/test_cases/quadpeaks_base/qua_pxr_extractor.json"), new TypeReference<LinkedHashMap>() {
            });

            MemoryMap m = new MemoryMap();
            for(Map.Entry<String, Object> key: obj.entrySet()){
                if(key.getKey().equals("qua_pxr_byte_vector")){
                    ExNode ex = unroll((LinkedHashMap)key.getValue(), key.getKey());
                    ex.calcOffset(0, m);
                    //System.out.println(ex.summary());
                    //System.out.println(ex.summary());
                }else if(key.getKey().equals("qua_pxr_bit_vector")){
                    ExNode ex = unroll((LinkedHashMap)key.getValue(), key.getKey());
                    ex.calcOffset(0,m);
                    //System.out.println(ex.summary());
                }
            }
            System.out.println(m.summary());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static ExNode unrollLeaf(LinkedHashMap map, String name){
        int size = (int) map.get("size");
        String source_header = (String) map.get("source_header");
        String header_field = (String) map.get("header_field");
        return new ExLeaf(name, size, source_header, header_field);
    }

    private static ExNode unroll(LinkedHashMap map, String name){
        boolean leaf = isLeaf(map);
        if(!leaf){
            boolean union  = map.get("type").equals("packed_union");
            ExNode ret = new ExIntNode(name, union);
            Object obj = map.get("members");
            LinkedHashMap<String, Object> members = (LinkedHashMap<String, Object>) obj;
            for(Map.Entry<String,Object> e: members.entrySet()){
                //System.out.println(e.getKey());
                ExNode m = unroll((LinkedHashMap) e.getValue(), e.getKey());
                ret.getMembers().add(m);
            }
            return ret;
        }else {
            //System.out.println(Utils.summary(map));
            return unrollLeaf(map, name);
        }
        /*for(Object e: map.entrySet()){
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) e;

            if(entry.getValue() instanceof Map){
                LinkedHashMap<String, Object> c = (LinkedHashMap<String, Object>) entry.getValue();
                if(c.containsKey("type")){
                    boolean union = c.get("type").equals("packed")
                }else{
                    throw new IllegalStateException();
                }
            }else{

            }
        }*/
    }

    private static boolean isLeaf(LinkedHashMap map){
        boolean leaf = true;
        for(Object e: map.entrySet()){
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) e;
            if(entry.getValue() instanceof Map){
                leaf = false;
            }
        }
        return leaf;
    }

}
