package com.p4.pd.config.beans;

import com.p4.pd.config.common.Bitset;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Data
public class Parser {
    @JsonProperty
    private List<Bitset> tcam;

    @JsonProperty
    private List<Bitset> sram;

    @JsonProperty
    private List<Bitset> ex;

    private static Parser instance;
    private Parser(){}
    static {
        instance = new Parser();
    }
    public static Parser getInstance(){
        return instance;
    }
}
