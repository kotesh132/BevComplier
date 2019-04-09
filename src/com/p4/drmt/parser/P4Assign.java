package com.p4.drmt.parser;

import com.p4.drmt.alu.CField;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.cfg.KeyMeta.AF;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class P4Assign implements Summarizable{
	private final String lhs;
	private final CField lhsaf;
	private final String rhs;
	private final CField rhsaf;
	private final FW rhsNum;
	private final boolean constant;
	@Override
	public String summary() {
			return lhs+"["+lhsaf.summary() +"] = " + (constant? rhsNum.summary() : (rhs+"["+rhsaf.summary()+"]"));
	}
	
	public List<ExtractorAtom> getExtractorAtoms(){
		List<ExtractorAtom> atoms = new ArrayList<>();
		List<FW> y = KeyMeta.getNums(Utils.arrList(lhsaf));
		Collections.reverse(y);
		List<FW> x;
		if(!constant)
			x = KeyMeta.getNums(Utils.arrList(rhsaf));
		else
			x = rhsNum.byteOrder();
		
		Collections.reverse(x);
		if(y.size()!=x.size())
			throw new IllegalStateException("Size Mismatch");
		for(int i=0;i<y.size();i++){
			atoms.add(new ExtractorAtom(y.get(i), x.get(i), constant?FW.ONE:FW.ZERO, FW.ONE));
		}
		return atoms;
	}
}
