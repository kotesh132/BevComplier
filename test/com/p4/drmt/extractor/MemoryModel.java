package com.p4.drmt.extractor;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.FW;
import com.p4.p416.gen.TableDeclarationContextExt;

import lombok.Data;

@Data
public class MemoryModel {
	private static final Logger L = LoggerFactory.getLogger(MemoryModel.class);
	private final int size;
	private final int width;
	private final FW[] mem;
	
	public MemoryModel(int size, int width){
		assert(size>0);
		this.size = size;
		this.width = width;
		this.mem =  new FW[size];
	}
	
	public FW read(int adr){
		return this.mem[adr];
	}
	
	public FW read(int bitOffSet, int size){
		assert (size <32);
		int xbitOffset = bitOffSet;
		int xSize = size;
		int acc = 0;
		int xadr = xbitOffset/this.width;
		int xoffset = xbitOffset%this.width;
		int accSize = 0;
		while(xSize>0){
			FW word  = mem[xadr];
			//|<--(w-xo)-->|<--xo-->|
			//|<---------w--------->|
			//Calculate how many bits are needed in this iteration
			int xbits = (width-xoffset)<xSize?(width-xoffset):xSize;
			//MASK the rest properly
			int cand = word.getValue()>>xoffset & ((1<<(width-xoffset))-1);
			//Store the accumulated Result
			acc = acc |(cand<<accSize);
			L.info(word.summary()+","+Integer.toHexString(acc)+","+Integer.toHexString(cand)+", "+xbits);
			accSize+=xbits;
			xSize-=xbits;
			xbitOffset+=xbits;
			xadr = xbitOffset/this.width;
			xoffset = xbitOffset%this.width;
		}
		return new FW(acc,size);
	}
	
	public void write(int adr, int value){
		this.mem[adr] = new FW(value, width);
	}
	
	public void randomize(){
		Random r = new Random();
		for(int i=0;i<size;i++){
			this.write(i, r.nextInt((1<<width)-1));
		}
	}
	
	public static void main(String[] args) {
		MemoryModel m = new MemoryModel(256, 8);
		m.randomize();
		for(int i = 0; i<256;i++){
			System.out.println(m.read(i).summary());
		}
		
		System.out.println("********");
		System.out.println(m.read(4,16).summary());
	}
	
	
}
