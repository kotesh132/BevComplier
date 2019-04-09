package com.p4.drmt.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.p4.drmt.parser.ByteUtils;
import com.p4.drmt.parser.FW;
import com.p4.utils.FileUtils;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;

import lombok.Data;
@Data
public class ConfigEmitUnit implements Summarizable{
	private final List<List<FW>> items;
	private final int width;
	private final int rowSize;
	private final int colSize;
	private final String fileName;
	
	public ConfigEmitUnit(int dataWidth, int rowSize, String fileName)
	{
		this.width = dataWidth;
		this.rowSize = rowSize;
		this.colSize = 1;
		this.fileName = fileName;
		this.items = new ArrayList<List<FW>>(this.colSize);
		this.items.add(0, new ArrayList<FW>(this.rowSize));
		
	}
	
	public ConfigEmitUnit(int dataWidth, int rowSize, int colSize, String fileName)
	{
		this.width = dataWidth;
		this.rowSize = rowSize;
		this.colSize = colSize;
		this.fileName = fileName;
		this.items = new ArrayList<List<FW>>();
		for(int i=0; i<colSize; i++)
		{
			this.items.add(i, new ArrayList<FW>());
		}
	}
	
	public void addItem(FW item){
		assert(item.getSize() == width);
		assert(items.get(0).size()<rowSize);
		items.get(0).add(item);
	}

	public void addItems(List<FW> its){
		for(FW it:its)
			addItem(it);
	}
	
	public void addItem(FW item, int index){
		assert(item.getSize() == width);
		//assert(items.get(0).size()<rowSize);
		assert(items.get(0).size()>index);
		assert(index>=0);
		items.get(0).set(index,item);
	}
	
	public void addItem(FW item, int colIndex, int rowIndex){
		assert(item.getSize() == width);
		if ( items.get(colIndex) == null)
		{
			items.add(colIndex, new ArrayList<FW>() );
		}
		items.get(colIndex).add(rowIndex,item);
	}
	
	
	
	public void addItem(int item, int colIndex, int rowIndex){
		if ( items.get(colIndex) == null)
		{
			items.add(colIndex, new ArrayList<FW>() );
		}
		items.get(colIndex).add(rowIndex,new FW(item, this.width));
	}
	
	public void setItem(FW item, int colIndex, int rowIndex){
		assert(item.getSize() == width);
		if ( items.get(colIndex) == null)
		{
			items.add(colIndex, new ArrayList<FW>() );
		}
		items.get(colIndex).set(rowIndex,item);
	}
	public void setItem(int item, int colIndex, int rowIndex){
		if ( items.get(colIndex) == null)
		{
			items.add(colIndex, new ArrayList<FW>() );
		}
		items.get(colIndex).set(rowIndex,new FW(item, this.width));
	}
	
	
	public void addItem(int val){
		addItem(new FW(val, this.width));
	}
	
	public void addItem(int val, int index){
		addItem(new FW(val,this.width), index);
	}
	
	public void pad(){
		//VALID only for 1D unit
		assert(items.size()==1);
		this.items.get(0).addAll(ByteUtils.repeat(new FW(0, width), rowSize-items.get(0).size()));
	}
	
	public void pad(int rowIndex){
		this.items.get(rowIndex).addAll(ByteUtils.repeat(new FW(0, width), rowSize-items.get(rowIndex).size()));
	}
	
	public void pad2D(){
		for(List<FW> row:items){
			row.addAll(ByteUtils.repeat(new FW(0, width), rowSize -row.size()));
		}
	}

	public void pad2D(int c){
		for(List<FW> row:items){
			row.addAll(ByteUtils.repeat(new FW(c, width), rowSize -row.size()));
		}
	}


	public void appendToFile(String diName){
		File absoluteFile = new File(diName+File.separator+fileName);
		StringBuilder sb = new StringBuilder();
		for(List<FW> row : items ){
			assert(row.size()==rowSize);	
			for(FW item:row){
				sb.append(""+item.toHSizeNibbles()+"\n");
				//FileUtils.AppendToFile(absoluteFile, item.toHSizeNibbles());
				//FileUtils.AppendToFile(absoluteFile, "\n");
			}
		}
		FileUtils.AppendToFile(absoluteFile, sb.toString());
	}

	@Override
	public String summary() {
		return Utils.summary(this.items);		
	}
}
