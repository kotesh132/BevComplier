package com.p4.pktgen.config.som;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TCAMConfig {

	private Integer numRow;
	private Integer numCol;
	private Integer words;
	private Integer bits;
	
	public TCAMConfig(TCAMConfig.UnNormalized un) {
		numRow = un.numRow;
		numCol = un.numCol;
		words = un.words;
		bits = un.bits;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer numRow;
		public Integer numCol;
		public Integer words;
		public Integer bits;
	}
}
