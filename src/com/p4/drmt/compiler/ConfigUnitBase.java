package com.p4.drmt.compiler;

import java.io.File;

public abstract class ConfigUnitBase implements ConfigUnit{

	@Override
	public void padAll() {
		for(ConfigEmitUnit c:getAllConfigs()){
			c.pad();
		}
	}
	@Override
	public String summary() {
		StringBuilder sb = new StringBuilder();
		for(ConfigEmitUnit c: getAllConfigs()){
			sb.append(c.summary());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public void emitAll(String source){
		File dirName = new File(source);
		dirName.mkdirs();
		for(ConfigEmitUnit c:getAllConfigs()){
			c.appendToFile(dirName.getAbsolutePath());
		}
	}

	public void pad2D(){
		for(ConfigEmitUnit c: getAllConfigs()){
			c.pad2D();
		}
	}
}
