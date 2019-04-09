package com.p4.pd.config.common;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;


@Data
public class PIEntry {
    @JsonProperty
    private List<List<Integer>> records;
    @JsonProperty
    private int memId;
    @JsonProperty
    private int offset;
    @JsonProperty
    private String data;
    @JsonProperty
    private Integer id; //Indicates Instance Id for a module. A module for instance ALU can have 32 intances, this field is responsible for the same.
}
