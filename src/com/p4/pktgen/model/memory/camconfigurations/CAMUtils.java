package com.p4.pktgen.model.memory.camconfigurations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class CAMUtils {
	
	private static TCAMConfigurations tcamConfigurations = null;
	
	static {
		
		try {
			InputStream is = CAMUtils.class.getResourceAsStream("tcamconfigurations.json");
			if(is != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				tcamConfigurations = new TCAMConfigurations(mapper.readValue(is, TCAMConfigurations.UnNormalized.class));
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final List<RowConfiguration> CAM8 = tcamConfigurations.getTCAM_COL8();
	private static final List<RowConfiguration> CAM4 = tcamConfigurations.getTCAM_COL4();
	
	private static RowConfiguration getClonedConfiguration(RowConfiguration originalConfig) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(originalConfig);
			objectOutputStream.flush();
			objectOutputStream.close();
			byteArrayOutputStream.close();
			byte[] byteData = byteArrayOutputStream.toByteArray();
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteData);
			return (RowConfiguration) new ObjectInputStream(byteInputStream).readObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private static List<RowConfiguration> getClonedConfiguration(List<RowConfiguration> originalConfig) {
		List<RowConfiguration> clone = new ArrayList<RowConfiguration>(originalConfig.size());
		for(RowConfiguration rc : originalConfig) {
			clone.add(getClonedConfiguration(rc));
		}
		return clone;
	}
	
	public static List<RowConfiguration> getSuitableConfiguration(Integer numTcamsInRow, List<Integer> tableWidths, Integer configInstancesRequired) {
		
		for(RowConfiguration rc : getRowConfigsList(numTcamsInRow)) {

			boolean configSelected = true;
			for(Integer width : tableWidths) {
				if(rc.getWidths().get(width) == 0) {
					configSelected = false;
					break;
				}
			}
			if(configSelected) {
				List<RowConfiguration> configInstances = new ArrayList<RowConfiguration>();
				for(int i=0; i<configInstancesRequired; i++)
					configInstances.add(getClonedConfiguration(rc));
				return configInstances;
			}
		}
		
		throw new RuntimeException("No tcam configuration from the list is suitable");
	}
	
	private static List<RowConfiguration> getRowConfigsList(Integer numTcams) {
		switch(numTcams) {
			case 8: return getClonedConfiguration(CAM8);
			case 4: return getClonedConfiguration(CAM4);
			default: throw new RuntimeException("Only row of 8 tcams is supported currently");
		}
	}
	
	public static List<List<RowConfiguration>> getAllPossibleRowConfigurations(Integer numCols, Integer numRows) {
		List<RowConfiguration> configList = getRowConfigsList(numCols);
		List<List<RowConfiguration>> fullConfigList = new ArrayList<List<RowConfiguration>>();
		for(int i=0; i<numRows; i++) {
			fullConfigList.add(getClonedConfiguration(configList));
		}
		
		List<List<RowConfiguration>> combs = allCombinationsOfRowConfigs(fullConfigList, 0);
		return combs;
	}
	
	
	
	private static List<List<RowConfiguration>> allCombinationsOfRowConfigs(List<List<RowConfiguration>> allConfigs, int index){
		if(index == allConfigs.size()) {
			List<List<RowConfiguration>> allRowConfigs = new ArrayList<List<RowConfiguration>>();
			allRowConfigs.add(new LinkedList<RowConfiguration>());
			return allRowConfigs;
		}
		
		List<List<RowConfiguration>> allRowConfigurations = new ArrayList<List<RowConfiguration>>();
		List<RowConfiguration> configList = allConfigs.get(index);
	    // Get combination from next index
		List<List<RowConfiguration>> remainingList = allCombinationsOfRowConfigs(allConfigs, index+1);
	    for (int i=0; i<configList.size(); i++) {
	    	RowConfiguration action = configList.get(i);
	        if (remainingList != null) {
	            for (List<RowConfiguration> remaining : remainingList) {
	            	List<RowConfiguration> nextCombination = new ArrayList<RowConfiguration>();
	                nextCombination.add(action);
	                nextCombination.addAll(remaining);
	                allRowConfigurations.add(nextCombination);
	            }
	        }
	    }
	    return allRowConfigurations;
	}
	
	public static void main(String[] args) {
		//getAllPossibleCamConfigurations(8, 4);
		getAllPossibleRowConfigurations(4, 4);
	}
}
