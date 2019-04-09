package com.p4.drmt.parser;

import com.p4.tools.graph.Graph;
import com.p4.utils.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class DeparserAtom {
	protected static final Logger L = LoggerFactory.getLogger(DeparserAtom.class);
	private final List<FW> tcamDDat = new ArrayList<>();
	private final List<FW> tcamDMsk = new ArrayList<>();
	private final List<FW> sramDOff = new ArrayList<>();
	private final List<FW> sramDVld = new ArrayList<>();

	public static DeparserHint deparserStat(Graph<P4State> g, Map<P4State, StateMeta> map){

		DeparserHint dpH = new DeparserHint();

		Map<P4State, Map<Integer,Set<P4State>>> ogMap = new LinkedHashMap<>();
		for(P4State s:g.getNodes()){
			for(P4State d: g.adj(s)){
				L.info("Edge "+ s.getName()+" -> "+ d.getName());
				if(map.get(d).getTransitionExtracts().size()>0) {
					int validLoc = map.get(d).getTransitionExtracts().get(0).getValidLoc();
					int doff = validLoc/8;
					if(!ogMap.containsKey(s)){
						ogMap.put(s, new LinkedHashMap<>());
					}
					if(!ogMap.get(s).containsKey(doff)){
						ogMap.get(s).put(doff, new LinkedHashSet<>());
					}
					ogMap.get(s).get(doff).add(d);
					if(!dpH.getDoffs().containsKey(s)){
						dpH.getDoffs().put(s, new ArrayList<>());
					}
					dpH.getDoffs().get(s).add(doff);
				}
			}
		}
		for(Map.Entry<P4State, Map<Integer, Set<P4State>>> entryOuter: ogMap.entrySet()){
			int count = 0;
			for(Map.Entry<Integer,Set<P4State>> entryInner: entryOuter.getValue().entrySet()){
				for(P4State ps: entryInner.getValue()){
					if(dpH.getPosition().containsKey(ps)){
						if(dpH.getPosition().get(ps)!=count){
							L.info("Node "+ps.getName() + " is already put in position "+dpH.getPosition().get(ps)+" , cant put it at "+ count);
							throw new IllegalStateException();

						}
					}else {
						dpH.getPosition().put(ps, count);
						L.info("Node "+ps.getName() + " is put in position "+count);
					}
				}
				count++;
			}
		}

		return dpH;
	}
	
	public static DeparserAtom getRows(P4State s, P4State d, Map<P4State, StateMeta> map, Graph<P4State> g, DPH dph){
		//DeparserHint dph = deparserStat(g,map);
		//DPH dph = DPH.CANDIDATE;
		//L.info(dph.summary());
		List<P4State> dAdjs = g.adj(d);
		LinkedHashSet<Integer> dadjset = new LinkedHashSet<>();
		for(P4State dadj:dAdjs){
			if(map.get(dadj).getTransitionExtracts().size()>0) {
				int off = map.get(dadj).getTransitionExtracts().get(0).getValidLoc();
				dadjset.add(off / 8);
			}else{
				dadjset.add(0);
			}
		}
		/*if(dadjset.size()>1){
			throw new UnsupportedOperationException("Deparser transition can't be identified in 1 byte "+ d.getName());
		}*/
		if(dadjset.size()==0)
			dadjset.add(0);

		DeparserAtom da = new DeparserAtom();
		if(d.getName().equals("accept")){
			da.tcamDDat.addAll(ByteUtils.repeat(FW.ZERO_BYTES, StateMeta.numdoff));
			da.tcamDMsk.addAll(ByteUtils.repeat(FW.FF_BYTES, StateMeta.numdoff));
			//Next State
			da.sramDOff.addAll(ByteUtils.repeat(new FW(0, 9), StateMeta.numdoff));
			da.sramDVld.addAll(ByteUtils.repeat(FW.ZERO, StateMeta.numdoff));
		}else{
			if(map.get(d).getTransitionExtracts().isEmpty()){
				da.tcamDDat.addAll(ByteUtils.repeat(FW.ZERO_BYTES, StateMeta.numdoff));
				da.tcamDMsk.addAll(ByteUtils.repeat(FW.FF_BYTES, StateMeta.numdoff));

			}else {
				//int num = map.get(d).getStateID();
				int num = map.get(d).getTransitionExtracts().get(0).getValidLoc();
				int x = num % 8;
				int y = num / 8;
				assert (x < 8 && x >= 0);
				FW n = new FW(1 << x, 8);
				FW m = new FW(~n.getValue() & 255, 8);
				if (s.getName().equals("start")) {
					da.tcamDDat.addAll(ByteUtils.repeat(FW.ZERO_BYTES, StateMeta.numdoff));
					da.tcamDMsk.addAll(ByteUtils.repeat(FW.FF_BYTES, StateMeta.numdoff));
				} else {
					//if(dph.getDpos().containsKey(s.getName())) {
					int index = dph.getDpos().get(d.getName());
					da.tcamDDat.addAll(ByteUtils.repeat(FW.ZERO_BYTES, StateMeta.numdoff));
					da.tcamDDat.set(index, n);

					da.tcamDMsk.addAll(ByteUtils.repeat(FW.ZERO_BYTES, StateMeta.numdoff));
					da.tcamDMsk.set(index, m);
					if (dph.getDposmask().containsKey(d.getName())) {
						for (int mpos : dph.getDposmask().get(d.getName())) {
							da.tcamDMsk.set(mpos, FW.FF_BYTES);
						}

					}
					//}
					//da.tcamDMsk.add(dph.getPosition().get(s), m);
				}
			}
			//System.out.println(d.getName());
			for(Integer pos: dph.getDoffPos().get(d.getName())){
				da.sramDOff.add(new FW(pos, 9));
				da.sramDVld.add(FW.ONE);
			}
			da.sramDOff.addAll(ByteUtils.repeat(new FW(0,9), StateMeta.numdoff-dph.getDoffPos().get(d.getName()).size()));
			da.sramDVld.addAll(ByteUtils.repeat(FW.ZERO, StateMeta.numdoff-dph.getDoffPos().get(d.getName()).size()));
			//da.sramDOff.add(new FW((Integer) dadjset.toArray()[0], 9));
			//da.sramDOff.addAll(ByteUtils.repeat(new FW(0, 9), StateMeta.numdoff-1));
			//da.sramDVld.add(FW.ONE);
			//da.sramDVld.addAll(ByteUtils.repeat(FW.ZERO, StateMeta.numdoff-1));
		}
		if(da.tcamDDat.size()!=StateMeta.numdoff){
			L.info(s.getName()+"->"+d.getName());
			throw new RuntimeException();
		}
		return da;
	}

	public static void deparserHelper(Graph<P4State> g, Map<P4State, StateMeta> map){

		List<Utils.Pair<String, String>> edges = new ArrayList<>();
		List<NodeProps> nodeProps = new ArrayList<>();
		Map<String, Integer> doffMap = new LinkedHashMap<>();
		for(P4State s:g.topologicalSort()) {
			List<String> adjacents = g.adj(s).stream().map( x -> {return x.getName();}).collect(Collectors.toList());
			Set<Integer> doffs = new LinkedHashSet<>();

			for(P4State destination: g.adj(s)){
				if (!map.get(destination).getTransitionExtracts().isEmpty()) {
					int doff = map.get(destination).getTransitionExtracts().get(0).getValidLoc() / 8;
					doffs.add(doff);
					doffMap.put(destination.getName(), doff);
				}
				edges.add(Utils.Pair.of(s.getName(), destination.getName()));
			}
			List<String> incidents = g.adjTo(s).stream().map( x -> {return x.getName();}).collect(Collectors.toList());
			nodeProps.add(new NodeProps(s.getName(), incidents, adjacents, doffs));
		}

		L.info(Utils.summary("\n", nodeProps));
		L.info(Utils.summary("\n", edges));
		NodeProps.consolidateIncomings(nodeProps, edges, doffMap);
		L.info("****************");
		L.info(Utils.summary("\n", nodeProps));
		//throw new RuntimeException();

	}

	public static DPH deparserAnalysis(Graph<P4State> g, Map<P4State, StateMeta> map) {
		Map<String, Set<Integer>> outs = new LinkedHashMap<>();
		Map<String, Map<String, Integer>> outsDetailed = new LinkedHashMap<>();
		for(P4State s:g.getNodes()) {
			List<P4State> outgoings = g.adj(s);
			Set<Integer> locDoff = new LinkedHashSet<>();
			for (P4State outgoing : outgoings) {
				if (!map.get(outgoing).getTransitionExtracts().isEmpty()) {
					int doff = map.get(outgoing).getTransitionExtracts().get(0).getValidLoc() / 8;
					locDoff.add(doff);
					if(!outsDetailed.containsKey(s.getName())){
						outsDetailed.put(s.getName(), new LinkedHashMap<>());
					}
					outsDetailed.get(s.getName()).put(outgoing.getName(), doff);
				}
			}
			outs.put(s.getName(), locDoff);
		}
		L.info(Utils.summary(outs));
		L.info(Utils.summary(outsDetailed));
		Map<String, Set<String>> incomings = new LinkedHashMap<>();
		for(P4State s:g.getNodes()){
			for(P4State d: g.adj(s)){
				if(!incomings.containsKey(d.getName())){
					incomings.put(d.getName(), new LinkedHashSet<>());
				}
				incomings.get(d.getName()).add(s.getName());
			}
		}
		Map<String, Set<Integer>> doffs1 = new LinkedHashMap<>();
		for(Map.Entry<String, Set<String>> posEntry: incomings.entrySet()){
			for(String ic: posEntry.getValue()){
				if(!doffs1.containsKey(posEntry.getKey())){
					doffs1.put(posEntry.getKey(), new LinkedHashSet<>());
				}
				doffs1.get(posEntry.getKey()).addAll(outs.get(ic));
			}
		}
		L.info(Utils.summary(doffs1));
		L.info(Utils.summary(incomings));
		Map<String, Set<Integer>> filteredPossibleLocations = doffs1.entrySet()
				.stream()
				.filter(x -> x.getValue().size()>1 && !x.getKey().equals("accept"))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		Map<String, Set<Integer>> otherLocations = doffs1.entrySet()
				.stream()
				.filter(x -> x.getValue().size()<=1 && !x.getKey().equals("accept"))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

		Map<String, Integer> dPos = new LinkedHashMap<>();
		Map<String, Set<Integer>> dMaskpos = new LinkedHashMap<>();
		for (Map.Entry<String, Set<Integer>> entry : otherLocations.entrySet()) {
			if(entry.getValue().iterator().hasNext())
				dPos.put(entry.getKey(), 0);//entry.getValue().iterator().next());
		}

		if(filteredPossibleLocations.size()>0) {
			L.info(Utils.summary(filteredPossibleLocations));
			List<List<Integer>> allPositions = new ArrayList<>();
			List<String> positionNames = new ArrayList<>();
			for (Map.Entry<String, Set<Integer>> entry : filteredPossibleLocations.entrySet()) {
				List<Integer> list = new ArrayList<>();
				list.addAll(entry.getValue());
				allPositions.add(list);
				positionNames.add(entry.getKey());
			}
			Utils.CartesianProductor cp = new Utils.CartesianProductor(allPositions);
			List<Integer> finalChoice = new ArrayList<>();
			outer:
			while (cp.hasNext()) {
				List<Integer> perm = cp.next();
				//L.info(Utils.summary(perm));

				for(int i=0; i<perm.size();i++){
					String dest = positionNames.get(i);
					int destpostLoc = perm.get(i);
					for(String source: incomings.get(dest)){
						if(outsDetailed.containsKey(source) && outsDetailed.get(source).containsKey(dest)){
							//L.info(outsDetailed.get(source).get(dest)+"");
							int p = outsDetailed.get(source).get(dest);
							if(p!=destpostLoc){
								continue outer;
							}
						}
					}

					finalChoice = perm;
					break outer;
				}
			}
			if(finalChoice.size()!=0) {
				L.info(Utils.summary(finalChoice));
				for(int i=0; i<finalChoice.size();i++){
					dPos.put(positionNames.get(i), finalChoice.get(i));
					Set<Integer> dmask = new LinkedHashSet<>(filteredPossibleLocations.get(positionNames.get(i)));
					dmask.removeAll(Utils.newLinkedHashSet(Utils.arrList(finalChoice.get(i))));
					dMaskpos.put(positionNames.get(i), dmask);
				}
			}else {
				throw new UnsupportedOperationException("Couldn't construct deparser configs. Use compiler hints");
			}
		}
		L.info("Final Deparser Hint");
		L.info(Utils.summary(doffs1));
		L.info(Utils.summary(dPos));
		L.info(Utils.summary(dMaskpos));
		System.out.println("Done");
		return new DPH(outs, dPos, dMaskpos);
	}
}
