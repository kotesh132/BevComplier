package com.p4.pktgen.p4blocks;

import com.p4.pktgen.enums.KeyType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableKey {

	private String keyName;
	private KeyType keyType;
	private Integer offset;
	private Integer size;
	private boolean isBit;
	
}
