package com.p4.pd.config.beans;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class Drmt {
    @JsonProperty
    private Processor processor;

    //Only one Instance of Drmt.
    private static Drmt instance;
    static{
        instance = new Drmt();
    }
    private Drmt(){}
    //Handler for retrieving Drmt instance
    public static Drmt getInstance(){
        return instance;
    }
}
