package com.p4.pktgen.config.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketConfig {

	private Integer pktsPerPath;
	@Getter private Integer packetLength;
	@Getter private Integer byteVectorLength;
	@Getter private Integer bitVectorLength;
	private Boolean useBmv;
	private Boolean fullCoverage;
	@Getter private Boolean parserOnlyPkts;
	@Getter private Map<String, Integer> headerIds;
	@Getter private Set<String> checksumHdrs;
	private List<String> includePaths;
	private List<String> excludePaths;
	@Getter private List<Map<String, String>> constraints;
	@Getter private List<String> enums;
	@Getter private Map<String, String> enumToP4;
	
	public PacketConfig(PacketConfig.UnNormalized un) {
		pktsPerPath = un.pktsPerPath;
		packetLength = un.packetLength;
		byteVectorLength = un.byteVectorLength;
		bitVectorLength = un.bitVectorLength;
		useBmv = un.useBmv;
		fullCoverage = un.fullCoverage;
		parserOnlyPkts = un.parserOnlyPkts;
		headerIds = un.headerIds;
		checksumHdrs = un.checksumHdrs;
		includePaths = un.includePaths;
		excludePaths = un.excludePaths;
		constraints = new ArrayList<Map<String,String>>();
		if(un.constraints != null) 
			for(Map<String, String> cons : un.constraints)
				constraints.add(cons);
		enums = un.enums;
		enumToP4 = un.p4ToEnum;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer pktsPerPath;
		public Integer packetLength;
		public Integer byteVectorLength;
		public Integer bitVectorLength;
		public Boolean useBmv;
		public Boolean fullCoverage;
		public Boolean parserOnlyPkts;
		public Map<String, Integer> headerIds;
		public Set<String> checksumHdrs;
		public List<String> includePaths;
		public List<String> excludePaths;
		public List<Map<String, String>> constraints;
		public List<String> enums;
		public Map<String, String> p4ToEnum;
	}
	
	public boolean isPathValid(String path) {
		boolean incMatched = false;
		if(includePaths != null) {
			for(String inc : includePaths) {
 				if(path.matches(inc)){
					incMatched = true;
					break;
				}
			}
			if(!incMatched)
				return false;
		}
		if(excludePaths != null) {
			for(String exc : excludePaths) {
				if(path.matches(exc))
					return false;
			}
		}
		return true;
	}
	
	public Integer getPacketsPerPath() {
		return pktsPerPath == null ? 1 : pktsPerPath;
	}
	
	public boolean isFullCoverage() {
		return fullCoverage == null || fullCoverage == true; 
	}
}
