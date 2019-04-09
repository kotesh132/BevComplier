package com.p4.drmt.parser;

import com.p4.drmt.cfg.KeyMeta;
import com.p4.utils.FileUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

public class KeyRow {
	//List<FixedWidthUnsigned> ybyt = new ArrayList<>();
	@Row(type=RowType.List,output="ybyt.cfg", reverse=true)
	public List<FW> ybyt;
	
	@Row(type=RowType.List,output="ybit.cfg")
	public List<FW> ybit1;
	
	@Row(type=RowType.List,output="ymsk.cfg")
	public List<FW> ymsk1;
	
	@Row(type=RowType.List,output="ktbl.cfg", reverse = true)
	public List<FW> ktbl;
	
	@Row(type=RowType.List,output="kvld.cfg", reverse = true)
	public List<FW> kvld;

	@Row(type=RowType.List,output="kseg.cfg", reverse = true)
	public List<FW> kseg;

	/*@Row(type=RowType.List,output="kmap_vld.cfg", reverse = true)
	public List<FW> kmap_vld;

	@Row(type=RowType.List,output="ktbl_map.cfg", reverse = true)
	public List<FW> ktbl_map;

	@Row(type=RowType.List,output="kseg_map.cfg", reverse = true)
	public List<FW> kseg_map;*/


	private static Map<String, List<Field>> classmap;
	//Handler method for retrieving classmap
	public static Map<String, List<Field>> getClassMap(){
		return Collections.unmodifiableMap(classmap);
	}

	static{
		classmap = new LinkedHashMap<>();
		Field[] fields = KeyRow.class.getFields();
		for(Field f:fields){
			Annotation[] annos = f.getDeclaredAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof Row){
					Row r = (Row) anno;
					if(!classmap.containsKey(r.output())){
						classmap.put(r.output(), new ArrayList<Field>());
					}
					classmap.get(r.output()).add(f);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void writeExtractorConfig(File OutputDir, List<KeyRow> rows){
		File keygenConfigDir = new File( OutputDir.getAbsoluteFile()+File.separator+"key");
		if(!keygenConfigDir.exists()){
			keygenConfigDir.mkdirs();
		}
		
		if(OutputDir.isDirectory()){
			for(Entry<String, List<Field>> e: classmap.entrySet()){
				File opFile = new File(keygenConfigDir.getAbsolutePath()+File.separator+e.getKey());
				//System.out.println(opFile.getName());
				FileUtils.Delete(opFile, false);
				for(Field f:e.getValue()){
					for(KeyRow row:rows){
						try{
							Annotation a = f.getAnnotation(Row.class);
							if(a !=null){
								Row r  = (Row)a;
								if(r.type()==RowType.Single){
									FW val = (FW) f.get(row);
									FileUtils.AppendToFile(opFile, val.toHSizeNibbles());
									FileUtils.AppendToFile(opFile, "\n");
									//System.out.println(val.summary());
								}else if(r.type()==RowType.List){
									List<FW> val = (List<FW>)f.get(row);
									if(r.reverse())
										Collections.reverse(val);
									for(FW n:val){
										List<FW> vals = n.byteOrder();
										for(FW n1:vals){
											FW n2 = n1;
											if(r.alwaysByte()){
												n2 = n1.byteAlign();
											}
											FileUtils.AppendToFile(opFile, n2.toHSizeNibbles());
											FileUtils.AppendToFile(opFile, "\n");
										}
									}
									
								}
							}
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException();
						}
					}
				}
			}
		}
	}

	
	public static void WriteKeygenConfig(KeyMeta k, File outputDir){
		writeExtractorConfig(outputDir, k.getRowsWithSchedule());
	}
}
