package com.p4.pktgen.mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.registers.CAM;
import com.p4.pktgen.enums.registers.Hash;
import com.p4.pktgen.enums.registers.Read;
import com.p4.pktgen.enums.registers.SRAM;
import com.p4.pktgen.enums.registers.TCAM;
import com.p4.pktgen.enums.registers.TCAMRow;
import com.p4.pktgen.enums.registers.TCAMRowAnd;
import com.p4.utils.FileUtils;

public class Mapper {
	
	private static Map<String, String> registerOffsetsAndSizes = new HashMap<String, String>();
	
	static {
		try {
			Path tempFile = Files.createTempFile("temp", ".vh");
			Files.copy(Mapper.class.getResourceAsStream("som_core.vh"), tempFile, StandardCopyOption.REPLACE_EXISTING);
			for(String line : FileUtils.ReadLines(tempFile.toFile())) {
				if(line.contains("define")) {
					String[] lineParts = line.split("\\s+");
					registerOffsetsAndSizes.put(lineParts[1], lineParts[2]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getMemory(ControllerType controllerType, String logicalName, Integer colId, Integer rowId) {
		switch(controllerType) {
			//case tcam: return getTCAMMemory(logicalName, colId, rowId);
			case sram: return getSRAMMemory(logicalName, colId, rowId);
		}
		throw new RuntimeException("Not a valid memory type");
	}
	
	private static String getSRAMMemory(String logicalName, Integer colId, Integer rowId) {
		
		switch(SRAM.valueOf(logicalName)) {
			case SRAM_MEMORY_NUMROWS:        return "SRAM_"+ colId +"_NUMROWS";
			case SRAM_MEMORY_WIDTH:          return "SRAM_"+ colId +"_WIDTH";
			case SRAM_MEMORY_OFFSET:         return "SRAM_"+ colId +"_OFFSET";
			case SRAM_MEMORY_WIDTH_IN_WORDS: return "SRAM_"+ colId +"_WIDTH_IN_WORDS";
		}
		throw new RuntimeException("Not a valid name");
	}

	public static String getRegister(ControllerType controllerType, String logicalName, Integer colId, Integer rowId) {
		switch(controllerType) {
			case read: return getReadRegister(logicalName, colId, rowId);
			case write: break;
			case hash: return getHashRegister(logicalName, colId, rowId);
			case cam: return getCAMRegister(logicalName, colId, rowId);
			case tcam: return getTCAMRegister(logicalName, colId, rowId);
			case sram: return getSRAMRegister(logicalName, colId, rowId);
			case tcamRowAnd: return getTCAMRowAndRegister(logicalName, colId, rowId);
			case tcamRow: return getTCAMRowRegister(logicalName, colId, rowId);
		}
		throw new RuntimeException("Not a valid controller type");
	}
	
	private static String getReadRegister(String logicalName, Integer colId, Integer rowId) {
		
		switch(Read.valueOf(logicalName)) {
			case READ_CTLR_OFFSET:              return "READ_CTLR_CFG_"+ colId +"_OFFSET";
			case READ_CTLR_SIZE: 	            return "READ_CTLR_CFG_"+ colId +"_SIZE";
			case READ_CTLR_SIZE_IN_WORDS:       return "READ_CTLR_CFG_"+ colId +"_SIZE_IN_WORDS";
			case READ_CTLR_FIELD_ENABLE_SIZE:   return "READ_CTLR_CFG_"+ colId +"_MRD_CTR_EN_SIZE";
			case READ_CTLR_FIELD_ENABLE_OFFSET: return "READ_CTLR_CFG_"+ colId +"_MRD_CTR_EN_START_OFFSET";
			case READ_CTLR_FIELD_TABLE_SIZE:    return "READ_CTLR_CFG_"+ colId +"_MRD_CUR_TBL_SIZE";
			case READ_CTLR_FIELD_TABLE_OFFSET:  return "READ_CTLR_CFG_"+ colId +"_MRD_CUR_TBL_START_OFFSET";
			case READ_CTLR_FIELD_BMP_SIZE:      return "READ_CTLR_CFG_"+ colId +"_MRD_BMP_SIZE";
			case READ_CTLR_FIELD_BMP_OFFSET:    return "READ_CTLR_CFG_"+ colId +"_MRD_BMP_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getHashRegister(String logicalName, Integer colId, Integer rowId) {
		
		switch(Hash.valueOf(logicalName)) {
		    case HASH_CTLR_OFFSET:            return "HASH_CTLR_CFG_"+ colId +"_OFFSET";
		    case HASH_CTLR_SIZE:              return "HASH_CTLR_CFG_"+ colId +"_SIZE";
		    case HASH_CTLR_SIZE_IN_WORDS:     return "HASH_CTLR_CFG_"+ colId +"_SIZE_IN_WORDS";
		    case HASH_CTLR_ENABLE_SIZE:       return "HASH_CTLR_CFG_"+ colId +"_HRD_CTR_EN_SIZE";
		    case HASH_CTLR_ENABLE_OFFSET:     return "HASH_CTLR_CFG_"+ colId +"_HRD_CTR_EN_START_OFFSET";
		    case HASH_CTLR_TABLE_SIZE:        return "HASH_CTLR_CFG_"+ colId +"_HRD_CUR_TBL_SIZE";
		    case HASH_CTLR_TABLE_OFFSET:      return "HASH_CTLR_CFG_"+ colId +"_HRD_CUR_TBL_START_OFFSET";
		    case HASH_CTLR_KEY_BMP_SIZE:      return "HASH_CTLR_CFG_"+ colId +"_HRD_KEY_BMP_SIZE";
		    case HASH_CTLR_KEY_BMP_OFFSET:    return "HASH_CTLR_CFG_"+ colId +"_HRD_KEY_BMP_START_OFFSET";
		    case HASH_CTLR_READ_BMP_SIZE:     return "HASH_CTLR_CFG_"+ colId +"_HRD_BMP_SIZE";
		    case HASH_CTLR_READ_BMP_OFFSET:   return "HASH_CTLR_CFG_"+ colId +"_HRD_BMP_START_OFFSET";
		    case HASH_CTLR_IND_SEG_SIZE:      return "HASH_CTLR_CFG_"+ colId +"_HRD_IND_SEG_SIZE";
		    case HASH_CTLR_IND_SEG_OFFSET:    return "HASH_CTLR_CFG_"+ colId +"_HRD_IND_SEG_START_OFFSET";
		    case HASH_CTLR_ADDR_MASK_SIZE:    return "HASH_CTLR_CFG_"+ colId +"_HRD_ADR_MSK_SIZE";
		    case HASH_CTLR_ADDR_MASK_OFFSET:  return "HASH_CTLR_CFG_"+ colId +"_HRD_ADR_MSK_START_OFFSET";
		    case HASH_CTLR_MISS_IND_SIZE:     return "HASH_CTLR_CFG_"+ colId +"_HRD_MIS_IND_SIZE";
		    case HASH_CTLR_MISS_IND_OFFSET:   return "HASH_CTLR_CFG_"+ colId +"_HRD_MIS_IND_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getSRAMRegister(String logicalName, Integer colId, Integer rowId) {
		switch(SRAM.valueOf(logicalName)) {
			case SRAM_CTLR_OFFSET:                   return "SRAM_CTLR_CFG_"+ colId +"_OFFSET";
			case SRAM_CTLR_SIZE:                     return "SRAM_CTLR_CFG_"+ colId +"_SIZE";
			case SRAM_CTLR_SIZE_IN_WORDS:            return "SRAM_CTLR_CFG_"+ colId +"_SIZE_IN_WORDS";
			case SRAM_CTLR_FIELD_HONOR_READ_SIZE:    return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_READ_SIZE";
			case SRAM_CTLR_FIELD_HONOR_READ_OFFSET:  return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_READ_START_OFFSET";
			case SRAM_CTLR_FIELD_HONOR_WRITE_SIZE:   return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_WRITE_SIZE";
			case SRAM_CTLR_FIELD_HONOR_WRITE_OFFSET: return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_WRITE_START_OFFSET";
			case SRAM_CTLR_FIELD_HONOR_HASH_SIZE:    return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_HASH_SIZE";
			case SRAM_CTLR_FIELD_HONOR_HASH_OFFSET:  return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HONOR_HASH_START_OFFSET";
			case SRAM_CTLR_FIELD_TABLE_BMP_SIZE:         return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_TID_BMP_SIZE";
			case SRAM_CTLR_FIELD_TABLE_BMP_OFFSET:       return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_TID_BMP_START_OFFSET";
			case SRAM_CTLR_FIELD_RSEG_SIZE:          return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_RSEG_SIZE";
			case SRAM_CTLR_FIELD_RSEG_OFFSET:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_RSEG_START_OFFSET";
			case SRAM_CTLR_FIELD_WSEG_SIZE:          return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_WSEG_SIZE";
			case SRAM_CTLR_FIELD_WSEG_OFFSET:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_WSEG_START_OFFSET";
			case SRAM_CTLR_FIELD_HSEG_SIZE:          return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HSEG_SIZE";
			case SRAM_CTLR_FIELD_HSEG_OFFSET:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HSEG_START_OFFSET";
			case SRAM_CTLR_FIELD_HWAY_SIZE:          return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HWAY_SIZE";
			case SRAM_CTLR_FIELD_HWAY_OFFSET:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HWAY_START_OFFSET";
			case SRAM_CTLR_FIELD_HASPTR_SIZE:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HAS_PTR_SIZE";
			case SRAM_CTLR_FIELD_HASPTR_OFFSET:      return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HAS_PTR_START_OFFSET";
			case SRAM_CTLR_FIELD_HASIND_SIZE:        return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HAS_IND_SIZE";
			case SRAM_CTLR_FIELD_HASIND_OFFSET:      return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_HAS_IND_START_OFFSET";
			case SRAM_CTLR_FIELD_START_ADDR_SIZE:    return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_START_ADR_SIZE";
			case SRAM_CTLR_FIELD_START_ADDR_OFFSET:  return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_START_ADR_START_OFFSET";
			case SRAM_CTLR_FIELD_END_ADDR_SIZE:      return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_END_ADR_SIZE";
			case SRAM_CTLR_FIELD_END_ADDR_OFFSET:    return "SRAM_CTLR_CFG_"+ colId +"_SRAM_CTL_END_ADR_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getTCAMRowRegister(String logicalName, Integer colId, Integer rowId) {
		switch(TCAMRow.valueOf(logicalName)) {
			case TCAM_ROW_CTRL_OFFSET:                return "TCAM_RCTL_"+ rowId +"_"+ colId +"_OFFSET";
			case TCAM_ROW_CTRL_SIZE:                  return "TCAM_RCTL_"+ rowId +"_"+ colId +"_SIZE";
			case TCAM_ROW_CTRL_SIZE_IN_WORDS:         return "TCAM_RCTL_"+ rowId +"_"+ colId +"_SIZE_IN_WORDS";
			case TCAM_ROW_CTRL_FIELD_LVL_SEL_SIZE:    return "TCAM_RCTL_"+ rowId +"_"+ colId +"_LVL_SEL_SIZE";
			case TCAM_ROW_CTRL_FIELD_LVL_SEL_OFFSET:  return "TCAM_RCTL_"+ rowId +"_"+ colId +"_LVL_SEL_START_OFFSET";
			case TCAM_ROW_CTRL_FIELD_NODE_SEL_SIZE:   return "TCAM_RCTL_"+ rowId +"_"+ colId +"_NODE_SEL_SIZE";
			case TCAM_ROW_CTRL_FIELD_NODE_SEL_OFFSET: return "TCAM_RCTL_"+ rowId +"_"+ colId +"_NODE_SEL_START_OFFSET";
			case TCAM_ROW_CTRL_FIELD_OFFSET_SIZE:     return "TCAM_RCTL_"+ rowId +"_"+ colId +"_OFFSET_SIZE";
			case TCAM_ROW_CTRL_FIELD_OFFSET_OFFSET:   return  "TCAM_RCTL_"+ rowId +"_"+ colId +"_OFFSET_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getTCAMRowAndRegister(String logicalName, Integer colId, Integer rowId) {
		switch(TCAMRowAnd.valueOf(logicalName)) {
			case TCAM_ROW_CTRL_AND_OFFSET:                      return "TCAM_ROWCTL_"+ rowId +"_"+ colId +"_OFFSET";
			case TCAM_ROW_CTRL_AND_SIZE:                        return "TCAM_ROWCTL_"+ rowId +"_"+ colId +"_SIZE";
			case TCAM_ROW_CTRL_AND_SIZE_IN_WORDS:               return "TCAM_ROWCTL_"+ rowId +"_"+ colId +"_SIZE_IN_WORDS";
			case TCAM_ROW_CTRL_AND_FIELD_AND_ENABLE_BMP_SIZE:   return "TCAM_ROWCTL_"+ rowId +"_"+ colId +"_AND_EN_BMP_SIZE";
			case TCAM_ROW_CTRL_AND_FIELD_AND_ENABLE_BMP_OFFSET: return "TCAM_ROWCTL_"+ rowId +"_"+ colId +"_AND_EN_BMP_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getTCAMRegister(String logicalName, Integer colId, Integer rowId) {
		switch(TCAM.valueOf(logicalName)) {
			case TCAM_CTRL_OFFSET:              return "TCAM_"+ rowId +"_"+ colId +"_OFFSET";
			case TCAM_CTRL_SIZE:                return "TCAM_"+ rowId +"_"+ colId +"_SIZE";
			case TCAM_CTRL_SIZE_IN_WORDS:       return "TCAM_"+ rowId +"_"+ colId +"_SIZE_IN_WORDS";
			case TCAM_CTRL_FIELD_ENABLE_SIZE:   return "TCAM_"+ rowId +"_"+ colId +"_TCAM_EN_SIZE";
			case TCAM_CTRL_FIELD_ENABLE_OFFSET: return "TCAM_"+ rowId +"_"+ colId +"_TCAM_EN_START_OFFSET";
			case TCAM_CTRL_FIELD_TABLE_BMP_SIZE:    return "TCAM_"+ rowId +"_"+ colId +"_TCAM_TID_BMP_SIZE";
			case TCAM_CTRL_FIELD_TABLE_BMP_OFFSET:  return "TCAM_"+ rowId +"_"+ colId +"_TCAM_TID_BMP_START_OFFSET";
			case TCAM_CTRL_FIELD_KSEG_SIZE:     return "TCAM_"+ rowId +"_"+ colId +"_TCAM_KSEG_SIZE";
			case TCAM_CTRL_FIELD_KSEG_OFFSET:   return "TCAM_"+ rowId +"_"+ colId +"_TCAM_KSEG_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	private static String getCAMRegister(String logicalName, Integer colId, Integer rowId) {
		switch(CAM.valueOf(logicalName)) {
			case CAM_CTLR_OFFSET:                return "CAM_CTL_"+ colId +"_OFFSET";
			case CAM_CTLR_SIZE:                  return "CAM_CTL_"+ colId +"_SIZE";
			case CAM_CTLR_SIZE_IN_WORDS:         return "CAM_CTL_"+ colId +"_SIZE_IN_WORDS";
			case CAM_CTLR_FIELD_ENABLE_SIZE:     return "CAM_CTL_"+ colId +"_CRD_CTR_EN_SIZE";
			case CAM_CTLR_FIELD_ENABLE_OFFSET:   return "CAM_CTL_"+ colId +"_CRD_CTR_EN_START_OFFSET";
			case CAM_CTLR_FIELD_TABLE_SIZE:      return "CAM_CTL_"+ colId +"_CRD_CUR_TBL_SIZE";
			case CAM_CTLR_FIELD_TABLE_OFFSET:    return "CAM_CTL_"+ colId +"_CRD_CUR_TBL_START_OFFSET";
			case CAM_CTLR_FIELD_BMP_SIZE:        return "CAM_CTL_"+ colId +"_CRD_BMP_SIZE";
			case CAM_CTLR_FIELD_BMP_OFFSET:      return "CAM_CTL_"+ colId +"_CRD_BMP_START_OFFSET";
			case CAM_CTLR_FIELD_MISS_IND_SIZE:   return "CAM_CTL_"+ colId +"_CRD_MIS_IND_SIZE";
			case CAM_CTLR_FIELD_MISS_IND_OFFSET: return "CAM_CTL_"+ colId +"_CRD_MIS_IND_START_OFFSET";
			case CAM_CTLR_FIELD_SR_EN_SIZE:      return "CAM_CTL_"+ colId +"_CRD_SR_EN_SIZE";
			case CAM_CTLR_FIELD_SR_EN_OFFSET:    return "CAM_CTL_"+ colId +"_CRD_SR_EN_START_OFFSET";
		}
		throw new RuntimeException("Not a valid name");
	}
	
	public static String getValue(String registerName) {
		return registerOffsetsAndSizes.get(registerName);
	}
	
	/*public static void main(String[] args) {
		System.out.println();
	}*/
}
