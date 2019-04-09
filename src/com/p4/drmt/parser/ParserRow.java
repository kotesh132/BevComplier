package com.p4.drmt.parser;

import com.p4.utils.FileUtils;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
@Data
public class ParserRow implements Summarizable{
	//{1, {0, 0, [0, 0x07, 0xf0, 0]}, {0, 1, [8w0x0, 8w0x0, 8w0x5, 8w0x06]}}  {1, 3, 20, [0,0,0,0], [0,0,0,0], 0}
	//TCAM
	@Row(type=RowType.Single,output="tcam_vld.cfg")
	public FW tcam_vld;
	
	@Row(type=RowType.Single,output="tcam_mask_flag.cfg")
	public FW tcam_mask_flag;
	
	@Row(type=RowType.Single,output="tcam_mask_curr.cfg")
	public FW tcam_mask_curr;
	
	@Row(type=RowType.List,output="tcam_mask_key.cfg")
	public List<FW> tcam_mask_key;
	
	@Row(type=RowType.Single,output="tcam_data_flag.cfg")
	public FW tcam_data_flag;
	
	@Row(type=RowType.Single,output="tcam_data_curr.cfg")
	public FW tcam_data_curr;
	
	@Row(type=RowType.List,output="tcam_data_key.cfg")
	public List<FW> tcam_data_key;
	//SRAM
	
	@Row(type=RowType.Single,output="sram_done.cfg")
	public FW sram_done;
	
	@Row(type=RowType.Single,output="sram_next.cfg")
	public FW sram_next;

	@Row(type=RowType.Single,output="sram_outr.cfg")
	public FW sram_outr;

	@Row(type=RowType.Single,output="sram_shift.cfg")
	public FW sram_shift;
	
	@Row(type=RowType.List,output="sram_doff.cfg", noByte = true)
	public List<FW> sram_doff;
	
	@Row(type=RowType.List,output="sram_dvld.cfg")
	public List<FW> sram_dvld;
	
	@Row(type=RowType.Single,output="sram_flag.cfg")
	public FW sram_flag;

	//ALU INSTRUCTIONS
	@Row(type=RowType.Single,output="sres_alue.cfg")
	public FW sres_alue;

	@Row(type=RowType.Single,output="sres_alui_opcode.cfg")
	public FW sres_alui_opcode;

	@Row(type=RowType.Single,output="sres_alui_op0.cfg")
	public FW sres_alui_op0;

	@Row(type=RowType.Single,output="sres_alui_mask.cfg")
	public FW sres_alui_mask;

	@Row(type=RowType.Single,output="sres_alui_op1.cfg")
	public FW sres_alui_op1;

	@Row(type=RowType.Single,output="sres_alui_op2.cfg")
	public FW sres_alui_op2;

	//Scratch Config
	@Row(type=RowType.Single,output="sres_svld.cfg")
	public FW sres_svld;

	@Row(type=RowType.Single,output="sres_soff.cfg")
	public FW sres_soff;

	@Row(type=RowType.Single,output="sres_woff.cfg")
	public FW sres_woff;

	@Row(type=RowType.Single,output="sres_smsk.cfg")
	public FW sres_smsk;

	@Row(type=RowType.List,output="sres_rvld.cfg")
	public List<FW> sres_rvld;

	@Row(type=RowType.Single,output="sres_sconst.cfg")
	public FW sres_sconst;

	private static Map<String, List<Field>> classmap;
	public static Map<String, List<Field>> getClassmap(){
		return Collections.unmodifiableMap(classmap);
	}

	static{
		classmap = new LinkedHashMap<>();
		Field[] fields = ParserRow.class.getFields();
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
	
	public static File getConfigDir(File OutputDir, String dirName){
		File parserConfigDir = new File( OutputDir.getAbsoluteFile()+File.separator+dirName);
		if(!parserConfigDir.exists()){
			parserConfigDir.mkdirs();
		}
		return parserConfigDir;
	}
	
	public static void writeParserFile(File outputDir, String dirName, String FileName, String content){
		File outputFile = new File(getConfigDir(outputDir, dirName).getAbsolutePath()+File.separator+FileName);
		FileUtils.Delete(outputFile, false);
		FileUtils.WriteFile(outputFile, content);
	}
	
	@SuppressWarnings("unchecked")
	public static void writeParserConfig(File OutputDir, List<ParserRow> rows, String dirName){
		File parserConfigDir = getConfigDir(OutputDir, dirName);
		if(OutputDir.isDirectory()){
			for(Entry<String, List<Field>> e: classmap.entrySet()){
				File opFile = new File(parserConfigDir.getAbsolutePath()+File.separator+e.getKey());
				//System.out.println(opFile.getName());
				FileUtils.Delete(opFile, false);
				for(Field f:e.getValue()){
					for(ParserRow row:rows){
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

	@Override
	public String summary() {
		StringBuilder sb =  new StringBuilder();
		sb.append("tcam_vld="+tcam_vld.summary()+"\n");
		sb.append("tcam_mask="+tcam_mask_flag.summary()+","+tcam_mask_curr.summary()+","+Utils.summary(tcam_mask_key)+"\n");
		sb.append("tcam_data="+tcam_data_flag.summary()+","+tcam_data_curr.summary()+","+Utils.summary(tcam_data_key)+"\n");
		sb.append("tcam_done="+sram_done.summary()+"\n");
		sb.append("tcam_next="+sram_next.summary()+"\n");
		sb.append("tcam_shft="+sram_shift.summary()+"\n");
		sb.append("tcam_doff="+Utils.summary(sram_doff)+"\n");
		sb.append("tcam_dvld="+Utils.summary(sram_dvld)+"\n");
		sb.append("tcam_flag="+sram_flag.summary()+"\n");
		return sb.toString();
	}

}
