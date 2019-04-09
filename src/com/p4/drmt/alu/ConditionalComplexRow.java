package com.p4.drmt.alu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.compiler.ConfigUnitBase;
import com.p4.utils.Utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
public class ConditionalComplexRow extends ConfigUnitBase{
	public static int NUMPBIT = 256;
	public static int NUMPRIM = 16;   //Number of primitive inputs
	public static int BITPRIM = Utils.lg(NUMPRIM);   //Number of primitive inputs
	public static int NUMCNOD = 8;    //Number of conditional nodes
	public static int NUMBRCH = 8;    //Number of branches
	public static int NUMIADR = 256;
	public static int NUMCNTX = 16;
	public static int BITPOFF = Utils.lg(NUMPBIT);
	public static int NUMALUS = NUMPRIM-1;
	public static int NUMOPS = 7;
	public static int BIT_OPS = Utils.lg(NUMOPS);
	public static int BITCAND = Utils.lg(NUMPRIM + NUMALUS);
	
	public static String conddirName(String baseDir){return baseDir+File.separator+ "cond";}
	
	private final ConfigEmitUnit prim_vld = new ConfigEmitUnit(1, NUMPRIM, NUMIADR, "prim_vld.cfg"); //Elements = NUMPRIM, Size = 1
	private final ConfigEmitUnit prim_offset = new ConfigEmitUnit(BITPOFF, NUMPRIM, NUMIADR, "prim_off.cfg"); //Elements = NUMPRIM, Size = BITPOFF
	private final ConfigEmitUnit prim_neg = new ConfigEmitUnit(1, NUMPRIM, NUMIADR, "prim_neg.cfg");//Elements = NUMPRIM, Size = 1
	private final ConfigEmitUnit alu_inst = new ConfigEmitUnit(BIT_OPS, NUMALUS, NUMIADR, "alu_inst.cfg");//Elements = NUMALUS, Size = BIT_OPS
	private final ConfigEmitUnit cond_adr = new ConfigEmitUnit(BITCAND, NUMCNOD, NUMIADR,"cond_adr.cfg");//Elements = NUMCNOD, Size = BITCAND
	private final ConfigEmitUnit br_vld = new ConfigEmitUnit(1, NUMBRCH, NUMIADR, "br_vld.cfg");//Elements = NUMBRCH, Size = 1
	private final ConfigEmitUnit br_data = new ConfigEmitUnit(NUMCNOD, NUMBRCH, NUMIADR,"br_data.cfg");//Elements = NUMBRCH, Size = NUMCNOD
	private final ConfigEmitUnit br_mask = new ConfigEmitUnit(NUMCNOD, NUMBRCH, NUMIADR, "br_mask.cfg");//Elements = NUMBRCH, Size = NUMCNOD
	private final ConfigEmitUnit br_dest = new ConfigEmitUnit(BITPOFF, NUMBRCH, NUMIADR, "br_dest.cfg");//Elements = NUMBRCH, Size = BITPOFF
	@Override
	public List<ConfigEmitUnit> getAllConfigs(){
		List<ConfigEmitUnit> ret = new ArrayList<>();
		ret.add(prim_vld);
		ret.add(prim_offset);
		ret.add(prim_neg);
		ret.add(alu_inst);
		ret.add(cond_adr);
		ret.add(br_vld);
		ret.add(br_data);
		ret.add(br_mask);
		ret.add(br_dest);
		return ret;
	}
	
	public static ConditionalComplexRow DEFAULT_NOP = new ConditionalComplexRow();
	static{
//		DEFAULT_NOP.padAll();
	}
	
}
