package com.p4.pd.config.beans;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
public class Processor {
    @JsonProperty
    private List<Character> keygen;
    @JsonProperty
    private List<Character> alumap;
    @JsonProperty
    private List<Character> aluactionmapinstance;
    @JsonProperty
    private List<Character> alucondcomplex;
    @JsonProperty
    private List<Character> sch;
    @JsonProperty
    private List<Character> alucomplex0;
    @JsonProperty
    private List<Character> alucomplex1;
    @JsonProperty
    private List<Character> alucomplex2;
    @JsonProperty
    private List<Character> alucomplex3;
    @JsonProperty
    private List<Character> alucomplex4;
    @JsonProperty
    private List<Character> alucomplex5;
    @JsonProperty
    private List<Character> alucomplex6;
    @JsonProperty
    private List<Character> alucomplex7;
    @JsonProperty
    private List<Character> alucomplex8;
    @JsonProperty
    private List<Character> alucomplex9;
    @JsonProperty
    private List<Character> alucomplex10;
    @JsonProperty
    private List<Character> alucomplex11;
    @JsonProperty
    private List<Character> alucomplex12;
    @JsonProperty
    private List<Character> alucomplex13;
    @JsonProperty
    private List<Character> alucomplex14;
    @JsonProperty
    private List<Character> alucomplex15;
    @JsonProperty
    private List<Character> alucomplex16;
    @JsonProperty
    private List<Character> alucomplex17;
    @JsonProperty
    private List<Character> alucomplex18;
    @JsonProperty
    private List<Character> alucomplex19;
    @JsonProperty
    private List<Character> alucomplex20;
    @JsonProperty
    private List<Character> alucomplex21;
    @JsonProperty
    private List<Character> alucomplex22;
    @JsonProperty
    private List<Character> alucomplex23;
    @JsonProperty
    private List<Character> alucomplex24;
    @JsonProperty
    private List<Character> alucomplex25;
    @JsonProperty
    private List<Character> alucomplex26;
    @JsonProperty
    private List<Character> alucomplex27;
    @JsonProperty
    private List<Character> alucomplex28;
    @JsonProperty
    private List<Character> alucomplex29;
    @JsonProperty
    private List<Character> alucomplex30;
    @JsonProperty
    private List<Character> alucomplex31;

    private static Processor instance;
    static{
        instance = new Processor();
    }
    private Processor(){}
    //Handler for Processor Obj.
    public static Processor getInstance(){
        return instance;
    }
}
