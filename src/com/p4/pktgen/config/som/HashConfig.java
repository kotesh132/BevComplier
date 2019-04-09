package com.p4.pktgen.config.som;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HashConfig {
	private Integer numHway;
	private Integer crcLength;
	private Integer maskLength;
	
	public HashConfig(HashConfig.UnNormalized un) {
		numHway = un.numHway;
		crcLength = un.crcLength;
		maskLength = un.maskLength;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer numHway;
		public Integer crcLength;
		public Integer maskLength;
	}
}
