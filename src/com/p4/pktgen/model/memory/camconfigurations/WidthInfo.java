package com.p4.pktgen.model.memory.camconfigurations;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WidthInfo implements Serializable {

	private Integer tableWidth;
	private List<Integer> camIds;
	
	public WidthInfo(WidthInfo.UnNormalized wUn) {
		tableWidth = wUn.tableWidth;
		camIds = wUn.camIds;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer tableWidth;
		public List<Integer> camIds;
	}
}
