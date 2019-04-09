package com.p4.pd.config.beans;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class DrmtConfig {
    @JsonProperty
    private Drmt drmt;

    @JsonProperty
    private Parser pa;

    @JsonProperty
    private Deparser dpa;

    private static DrmtConfig instance;
    private DrmtConfig(){}
    static{
        instance = new DrmtConfig();
    }
    public static DrmtConfig getInstance(){
        return instance;
    }
}





