package com.p4.pktgen.model.memory.camconfigurations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TCAMConfigurations implements Serializable {

	private List<RowConfiguration> TCAM_COL8;
	private List<RowConfiguration> TCAM_COL4;
	
	public TCAMConfigurations(TCAMConfigurations.UnNormalized tUn) {
		TCAM_COL4 = new ArrayList<RowConfiguration>();
		for(RowConfiguration.UnNormalized rUn : tUn.TCAM_COL4) {
			TCAM_COL4.add(new RowConfiguration(rUn));
		}
		TCAM_COL8 = new ArrayList<RowConfiguration>();
		for(RowConfiguration.UnNormalized rUn : tUn.TCAM_COL8) {
			TCAM_COL8.add(new RowConfiguration(rUn));
		}
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public List<RowConfiguration.UnNormalized> TCAM_COL8;
		public List<RowConfiguration.UnNormalized> TCAM_COL4;
	}
	
	private List<RowConfiguration> getConfiguration(Integer numTcamCols) {
		switch (numTcamCols) {
			case 4 : return TCAM_COL4;
			case 8 : return TCAM_COL8;
			default : throw new RuntimeException("Only row of 4/8 tcams is supported currently");
		}
	}
	
}
