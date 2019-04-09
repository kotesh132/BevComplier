package com.p4.drmt.parser;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.utils.Summarizable;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;
import com.p4.utils.Utils.fn1;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class StateMeta implements Summarizable{

	private static final Logger L = LoggerFactory.getLogger(StateMeta.class);
	public static final int numdoff = DRMTConstants.NUMDOFF;
	public static final int byt = DRMTConstants.BYTE;
	//public static AtomicInteger count=new AtomicInteger(0);

	private String stateName = "";
	private int stateID = Integer.MIN_VALUE;
	//private int totalTransitionBytes = 0;
	private List<P4Extract> transitionExtracts = new ArrayList<>();
	private List<DM> extracts = new ArrayList<>();
	private Map<String,List<DM>> transitions = new LinkedHashMap<>();
	
	public int getTotalTransitionsInBytes(){
		int ret = 0;
		for(P4Extract p: transitionExtracts){
			ret+=p.sizeInBytes();
		}
		return ret;
	}

	public P4Extract getALUExtract(){
		return P4Extract.filterALUneeded(transitionExtracts);
	}

	@Data
	public static class DM implements Summarizable{
		public final FW data;
		public final FW mask;
		public final FW valid;
		public static DM def = new DM(new FW(0, byt),
									  new FW(0, byt),
									  FW.ZERO);
		public static DM defaultMask = new DM(new FW(0, byt),
			    new FW(255, byt),
			    FW.ONE);
		public List<DM> repeat(int n){
			List<DM> ret = new ArrayList<>();
			if(n>0){
				for(int i=0; i<n;i++){
					ret.add(this);
				}
			}
			return ret;
		}

		@Override
		public String summary() {
			return "{"+data.summary()+","+mask.summary()+","+valid.summary()+"}";
		}
		
		public static List<FW> onlyData(List<DM> in){
			return Utils.map(new fn1<FW, DM>() {
				@Override
				public FW invoke(DM p1) {
					return p1.data;
				}
			}, in);
		}

		public static List<FW> onlyMask(List<DM> in){
			return Utils.map(new fn1<FW, DM>() {
				@Override
				public FW invoke(DM p1) {
					return p1.mask;
				}
			}, in);
		}
		
		public static List<FW> onlyValid(List<DM> in){
			return Utils.map(new fn1<FW, DM>() {
				@Override
				public FW invoke(DM p1) {
					return p1.valid;
				}
			}, in);
		}


	}
	
	public static void assignStateIds(Map<P4State, StateMeta> map ){
		int count = 1;
		
		for(Entry<P4State, StateMeta> e:map.entrySet()){
			e.getValue().stateName = e.getKey().getName();
			if(e.getKey().getName().equals("start")){
				e.getValue().setStateID(DRMTConstants.START_STATE);
			}else if(e.getKey().getName().equals("accept")){
				e.getValue().setStateID(DRMTConstants.ACCEPT_STATE);
			}else{
				e.getValue().setStateID(count++);
			}
		}
	}

	public static void assignExtractBytes(Map<P4State, StateMeta> map, P4Headers header) {
		for(Entry<P4State, StateMeta> e:map.entrySet()){
			e.getValue().transitionExtracts.addAll(e.getKey().getExtracts()); 
		}
		
	}

	@Override
	public String summary() {
		StringBuilder sb = new StringBuilder();
		sb.append("========================\n");
		sb.append(stateName+"["+stateID+"]\n");
		sb.append("tb = "+Utils.summary(this.transitionExtracts)+"\n");
		sb.append(Utils.summary(extracts)+"\n");
		sb.append("------------------------\n");
		for(Entry<String,List<DM>> e:transitions.entrySet()){
			sb.append(Utils.summary(e.getValue())+"\n");
		}
		sb.append("========================\n");
		return sb.toString();
	}

	public static void assignSelectsAndTransitions(Map<P4State, StateMeta> map, P4Headers header) {
		for(Entry<P4State, StateMeta> e:map.entrySet()){
			P4State state = e.getKey();
			L.info(state.getName());
			if(state.getTransition()!=null){
				List<DM> ets = null;
				if(state.getTransition().getSelect()!=null){
					P4Select ps = state.getTransition().getSelect();
					List<Pair<P4Header, BitStringType>> l = new ArrayList<>();
					for(String exp:ps.getExpressions()){
						Pair<P4Header, BitStringType> p = header.resolveHeaderField(exp);
						l.add(p);
					}
					ets = calcByteOffsets(l, numdoff);
					//Calculate Transitions for Select
					for(P4KeySet ks:ps.getTransitions()){
						List<DM> trans = new ArrayList<>();
						
						if(ps.getExpressions().size()==ks.getCasest().size()){
							for(int i=0;i<ps.getExpressions().size();i++){
								L.info(ps.getExpressions().get(i)+"["+l.get(i).first().offSetOf(l.get(i).second().getIdentifier())+","+l.get(i).second().getLength()+"],"+ks.getCasest().get(i));
								String numVal = ks.getCasest().get(i);
								if(numVal.equals("default")){
									trans.addAll(calcDMDataMaskValidsDefault(l.get(i)));
								}else{
									if(!numVal.contains("&&&")) {
										FW val = ByteUtils.parseP4Number(numVal);
										trans.addAll(calcDMDataMaskValids(l.get(i), val));
									}else{
										throw new IllegalStateException();

									}
								}
							}
						}else if(ks.getCasest().size()==1 && ks.getCasest().get(0).equals("default")){
							for(int i=0;i<ps.getExpressions().size();i++){
								trans.addAll(calcDMDataMaskValidsDefault(l.get(i)));
							}
						}else{
							throw new IllegalStateException();
						}
						int size = trans.size();
						if(size>numdoff){
							throw new IllegalArgumentException("NUMDOFF "+numdoff+" is too small :"+size);
						}
						//trans = ByteUtils.padMsbDefault(trans, numdoff, DM.def);
						trans.addAll(DM.def.repeat(numdoff-size));
						e.getValue().transitions.put(ks.getState(), trans);
					}
					
				}else if(state.getTransition().getNextState()!=null && !state.getTransition().getNextState().isEmpty()){
					ets = calcByteOffsets(null, numdoff);
				}
				//System.out.println("0=0=>"+Utils.summary(ets));
				e.getValue().extracts.addAll(ets);
			}
		}
	}
	
	private static Collection<? extends DM> calcDMDataMaskValidsDefault(Pair<P4Header, BitStringType> p) {
		Pair<Integer,Integer> se= getStartEnd(p);
		int start = se.first();
		int end = se.second();
		List<DM> ret = new ArrayList<>();
		int sb = Utils.floor(start, byt);int startBytePos = sb*byt;
		int eb = Utils.floor(end, byt);int endBytePos = eb*byt+byt-1;
		for(int i=startBytePos; i<=endBytePos;i+=byt){
			ret.add(DM.defaultMask);
		}
		return ret;
	}

	/*
	 * 
	 */
	private static List<DM> calcByteOffsets(List<Pair<P4Header, BitStringType>> l, int numdoff){
		List<DM> ret = new ArrayList<>();
		if(l==null){
			//Nothing
		}else{
			for(Pair<P4Header, BitStringType> p:l){
				List<DM> x = calcDMOffsetMaskValids(p);
				ret.addAll(x);
			}
		}
		if(ret.size()>numdoff){
			throw new IllegalArgumentException("NUMDOFF "+numdoff+" is too small");
		}
		ret.addAll(DM.def.repeat(numdoff-ret.size()));
		return ret;
	}
	
	private static Pair<Integer,Integer> getStartEnd(Pair<P4Header, BitStringType> p){
		P4Header h = p.first();
		BitStringType b = p.second();
		
		int start = h.offSetOf(b.getIdentifier());
		int end = start+b.getLength()-1;
		return Pair.of(start, end);
	}
	
	private static List<DM> calcDMDataMaskValids(Pair<P4Header, BitStringType> p, FW val){
		Pair<Integer,Integer> se= getStartEnd(p);
		return getFieldBytesMasksBE(se.first(), se.second(), val,false);
	}
	
	private static List<DM> calcDMOffsetMaskValids(Pair<P4Header, BitStringType> p) {
		Pair<Integer,Integer> se= getStartEnd(p);
		int start = se.first();
		int end = se.second();
		return getFieldBytesMasksBE(start, end, new FW(0, end-start+1),true);
	}

	private static List<DM> getFieldBytesMasksBE(int start, int end, FW val, boolean isOffset){
		if(start>end){
			throw new IllegalArgumentException("start=["+start+"]>=end=["+end+"]");
		}
		List<DM> ret = new ArrayList<>();
		int sb = Utils.floor(start, byt);int startBytePos = sb*byt;
		int eb = Utils.floor(end, byt);int endBytePos = eb*byt+byt-1;
		if((endBytePos-startBytePos+1)%byt!=0){
			throw new IllegalStateException();
		}
		int k=0;
		for(int i=startBytePos; i<=endBytePos;i+=byt){
			boolean[] b = new boolean[byt];
			boolean[] v1 = new boolean[byt];
			for(int j=0;j<byt;j++){
				if(i+j>=start&&i+j<=end){
					b[j]=true;
					v1[j] = val.isSetBE(k++);
				}else{
					b[j]=false;
					v1[j]=false;
				}
			}
			FW m = FW.getMofBE(b);
			FW d = isOffset?new FW(i/byt, byt):FW.getVof(v1);
			FW v = FW.ONE;
			ret.add(new DM(d, m, v));
		}
		//System.out.println(Utils.summary(DM.onlyData(ret)));
		//System.out.println(Utils.summary(DM.onlyMask(ret)));
		return ret;
	}
	
	private static List<DM> getFieldBytesMasks(int start, int end, FW val, boolean isOffset){
		if(start>=end){
			throw new IllegalArgumentException("start=["+start+"]>=end=["+end+"]");
		}
		List<DM> ret = new ArrayList<>();
		int sb = Utils.floor(start, byt);int startBytePos = sb*byt;
		int eb = Utils.floor(end, byt);int endBytePos = eb*byt+byt-1;
		if((endBytePos-startBytePos+1)%byt!=0){
			throw new IllegalStateException();
		}
		
		//System.out.println(start+","+end);
		//System.out.println(startBytePos+","+(endBytePos));
		int k=0;
		for(int i=startBytePos; i<=endBytePos;i+=byt){
			boolean[] b = new boolean[byt];
			boolean[] v1 = new boolean[byt];
			for(int j=0;j<byt;j++){
				if(i+j>=start&&i+j<=end){
					b[j]=true;
					v1[j] = val.isSet(k++);
				}else{
					b[j]=false;
					v1[j]=false;
				}
			}
			FW m = FW.getMof(b);
			FW d = isOffset?new FW(i/byt, byt):FW.getVof(v1);
			FW v = FW.ONE;
			ret.add(new DM(d, m, v));
		}
		//System.out.println(Utils.summary(DM.onlyData(ret)));
		//System.out.println(Utils.summary(DM.onlyMask(ret)));
		return ret;
	}

	public static List<DM> getDefaultTransition() {
		return DM.def.repeat(numdoff);
	}
	
}
