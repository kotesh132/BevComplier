package com.p4.pd.config.common;

import java.util.List;

/**
 * Type Configurable should exhibits below props.
 * generate method implementation to help generating configurations.
 */

public interface Configurable {
    List<Bitset> summary();
    void generate();
}
