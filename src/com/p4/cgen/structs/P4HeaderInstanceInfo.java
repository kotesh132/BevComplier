package com.p4.cgen.structs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrajarap on 10/08/17.
 * CISCO
 */
@Data
public class P4HeaderInstanceInfo {

    static List<P4HeaderInstanceInfo> infos = new ArrayList<>();

    private String name;
    private int bitWidth;
    private int byteOffsetPhv;
    private int numFields;
    private int isMetadata;
    private String firstField;
    private int byteWidthPhv;

    public P4HeaderInstanceInfo(String name, int bitWidth, int byteOffsetPhv, int numFields, int isMetadata, String firstField, int byteWidthPhv) {
        this.name = name;
        this.bitWidth = bitWidth;
        this.byteOffsetPhv = byteOffsetPhv;
        this.numFields = numFields;
        this.isMetadata = isMetadata;
        this.firstField = firstField;
        this.byteWidthPhv = byteWidthPhv;
    }

    public static void cleanInfos() {
        if (infos != null) {
            infos.clear();
        }
    }

    public static void addInfo(P4HeaderInstanceInfo info) {
        infos.add(info);
    }

    public static List<P4HeaderInstanceInfo> getInfos() {
        return infos;
    }
}


