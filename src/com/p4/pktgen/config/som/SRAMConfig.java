package com.p4.pktgen.config.som;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SRAMConfig {

	private Integer numSram;
	private Integer words;
	private Integer bits;
	
	public SRAMConfig(SRAMConfig.UnNormalized un) {
		numSram = un.numSram;
		words = un.words;
		bits = un.bits;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer numSram;
		public Integer words;
		public Integer bits;
	}
}
