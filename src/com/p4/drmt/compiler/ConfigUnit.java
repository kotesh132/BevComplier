package com.p4.drmt.compiler;

import java.util.List;

import com.p4.utils.Summarizable;

public interface ConfigUnit extends Summarizable{
	public List<ConfigEmitUnit> getAllConfigs();
	public void padAll() ;
	public void emitAll(String source);
}
