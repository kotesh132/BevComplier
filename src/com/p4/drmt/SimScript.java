package com.p4.drmt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import com.p4.utils.FileUtils;

public class SimScript {

	public static final String content = 
			"#!/bin/bash\n"+ 
			"set -e\n"+
			"if [ $# -eq 0 ]\n"+
			"then\n"+
			"echo \"No arguments supplied\"\n"+
			"else\n"+
			"echo \"Running Simulation...\"\n"+
			"cfgdir=$1\n"+
  			"runscript=\"$cfgdir/run_cmd_sw\"\n"+
  			"if [ -x \"$runscript\" ]\n"+
  			"then\n"+
  			"echo \"Found script\"\n"+
  			"srciptpath=`pwd -P`\n"+
  			"cd $cfgdir\n"+
  			"echo \"CWD =  $srciptpath\"\n"+
  			"./run_cmd_sw \"$srciptpath\"\n"+
  			"fi\n"+
  			"fi\n"
; 
	public static void emitRunScript(String dir){
		File file = new File(dir+File.separator+"run_sims");
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		//add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        
		FileUtils.AppendToFile(file, content);
		
		try {
			Files.setPosixFilePermissions(Paths.get(file.getAbsolutePath()), perms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
