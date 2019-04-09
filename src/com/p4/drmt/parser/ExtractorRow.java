package com.p4.drmt.parser;

import com.p4.utils.FileUtils;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

public class ExtractorRow implements Summarizable{
	@Row(type=RowType.List,output="ybyt.cfg")
	public List<FW> ybyt;//NUMCBYT (40) each of size BITVBYT(8)
	
	@Row(type=RowType.List,output="xbyt.cfg")
	public List<FW> xbyt;//NUMCBYT (40) each of size BITVBYT(8)
	
	@Row(type=RowType.List,output="vbyt.cfg")
	public List<FW> vbyt;//NUMCBYT (40) each of size 1
	
	@Row(type=RowType.List,output="cbyt.cfg")
	public List<FW> cbyt; //NUMCBYT (40) each of size 1
	
	
	@Row(type=RowType.List,output="ybit.cfg") //NUMCBIT(8) each of size BITVBIT(8)
	public List<FW> ybit;
	
	@Row(type=RowType.List,output="xbit.cfg", noByte = true) //NUMCBIT(8) each of size BITWDTH (11)
	public List<FW> xbit;
	
	@Row(type=RowType.List,output="vbit.cfg") //NUMCBIT(8) each of size 1
	public List<FW> vbit;
	
	@Row(type=RowType.List,output="cbit.cfg") ////NUMCBIT(8) each size 1
	public List<FW> cbit;
	
	private static Map<String, List<Field>> classmap;

	static{
		classmap = new LinkedHashMap<>();
		Field[] fields = ExtractorRow.class.getFields();
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

	public static void writeParserFile(File outputDir, String dirName, String FileName, String content){
		File outputFile = new File(ParserRow.getConfigDir(outputDir, dirName).getAbsolutePath()+File.separator+FileName);
		FileUtils.Delete(outputFile, false);
		FileUtils.WriteFile(outputFile, content);
	}
	
	
	@SuppressWarnings("unchecked")
	public static void writeExtractorConfig(File OutputDir, List<ExtractorRow> rows, String dirName){
		File extractorConfigDir = ParserRow.getConfigDir(OutputDir, dirName);
		if(OutputDir.isDirectory()){
			for(Entry<String, List<Field>> e: classmap.entrySet()){
				File opFile = new File(extractorConfigDir.getAbsolutePath()+File.separator+e.getKey());
				//System.out.println(opFile.getName());
				FileUtils.Delete(opFile, false);
				for(Field f:e.getValue()){
					for(ExtractorRow row:rows){
						try{
							Annotation a = f.getAnnotation(Row.class);
							if(a !=null){
								Row r  = (Row)a;
								//System.out.println(r.output());
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
										List<FW> vals1 = new ArrayList<>();
										vals1.add(n);
										List<FW> vals = r.noByte()? vals1:n.byteOrder();
										for(FW n1:vals){
											FW n2 = n1;
											if(r.alwaysByte()){
												n2 = n1.byteAlign();
											}
											FileUtils.AppendToFile(opFile, n2.toHSizeNibbles());
										}
										FileUtils.AppendToFile(opFile, "\n");
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

	@Override
	public String summary() {
		StringBuilder sb  =  new StringBuilder();
		sb.append("ybyt = "+Utils.summary(this.ybyt)+"\n");
		sb.append("xbyt = "+Utils.summary(this.xbyt)+"\n");
		sb.append("cbyt = "+Utils.summary(this.cbyt)+"\n");
		sb.append("vbyt = "+Utils.summary(this.vbyt)+"\n");
		
		//sb.append(Utils.summary(this.ybit)+"\n");
		//sb.append(Utils.summary(this.xbit)+"\n");
		//sb.append(Utils.summary(this.cbit)+"\n");
		//sb.append(Utils.summary(this.vbit)+"\n");
		return sb.toString();
	}

}
