package com.p4.drmt.cnst;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.parser.FW;
import com.p4.p416.gen.AbstractBaseExt;

import lombok.Getter;


public class ConstConfig {

	private static final Logger L = LoggerFactory.getLogger(ConstConfig.class);
	
	public class ConstConfigEntry{
		@Getter
		private FW element;
		@Getter
		private int byteOffset;
		public ConstConfigEntry(FW element, int byteOffset)
		{
			this.element = element;
			this.byteOffset = byteOffset;
		}
	}
	private ConfigEmitUnit constConfig; 
	private List<ConstConfigEntry> constsList;
	public static String conddirName = "const";
	public ConstConfig(Map<String,AbstractBaseExt> ctxs)
	{
		constConfig = new ConfigEmitUnit(8, 64, "const.cfg");
		constConfig.pad();
		constsList = new ArrayList<ConstConfigEntry>();
		for( Entry<String,AbstractBaseExt> entry: ctxs.entrySet())
		{
			assert(entry.getValue().getFW() != null);
			constsList.add( new ConstConfigEntry(entry.getValue().getFW(),entry.getValue().getAlignedByteOffset()));
		}
		addConfigs();
	}
	
	private void addConfigs()
	{
		for(ConstConfigEntry entry: constsList)
		{
			L.debug("Constant =" + entry.getElement().summary() + " Offset="+entry.getByteOffset());
			List<FW> bytes = entry.getElement().byteOrder();
			int offset = entry.byteOffset;
			for(FW fw:bytes)
			{
				L.debug("Byte =" + fw.byteAlign().summary() + " Offset="+offset);
				constConfig.addItem(fw.byteAlign(), offset);
				offset++;
				
			}
		}
	}
	
	public List<ConfigEmitUnit> getAllConfigs(){
		List<ConfigEmitUnit> ret = new ArrayList<>();
		ret.add(constConfig);
		return ret;
	}
	
	public void emitAll(String source){
		File dirName = new File(source+File.separator+conddirName);
		dirName.mkdirs();
		for(ConfigEmitUnit c:getAllConfigs()){
			c.appendToFile(dirName.getAbsolutePath());
		}
	}
}
