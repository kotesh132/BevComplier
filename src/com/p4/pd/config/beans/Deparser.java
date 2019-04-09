package com.p4.pd.config.beans;

import com.p4.pd.config.common.Bitset;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
@Data
public class Deparser {
    @JsonProperty
    private List<Bitset> tcam;

    @JsonProperty
    private List<Bitset> dex;

    private static Deparser instance;
    private Deparser(){}
    static{
        instance = new Deparser();
    }

    public static Deparser getInstance(){
        return instance;
    }

}
