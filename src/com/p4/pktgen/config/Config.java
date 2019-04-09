package com.p4.pktgen.config;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.p4.pktgen.config.cache.CacheConfig;
import com.p4.pktgen.config.packet.PacketConfig;
import com.p4.pktgen.config.som.SOMConfig;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Config {
	private List<SOMConfig> somConfig;
	private CacheConfig cacheConfig;
	private PacketConfig pktConfig;
	private String bmvServerIp;
	private Integer bmvServerPort;
	
	public Config(Config.UnNormalized un) {
		somConfig = new ArrayList<SOMConfig>();
		for(SOMConfig.UnNormalized sUn: un.somConfig){
			somConfig.add(new SOMConfig(sUn));
		}
		if(un.cacheConfig != null)
			cacheConfig = new CacheConfig(un.cacheConfig);
		pktConfig = new PacketConfig(un.pktConfig);
		bmvServerIp = un.bmvServerIp;
		bmvServerPort = un.bmvServerPort;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public List<SOMConfig.UnNormalized> somConfig;
		public CacheConfig.UnNormalized cacheConfig;
		public PacketConfig.UnNormalized pktConfig;
		public String bmvServerIp;
		public Integer bmvServerPort;
	}
	
	public Integer getNumSom() {
		return somConfig != null ? somConfig.size() : 0;
	}
}
