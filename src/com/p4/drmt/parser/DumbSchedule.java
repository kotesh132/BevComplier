package com.p4.drmt.parser;

import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DumbSchedule {
	@Data
	public static class ScheduleNode{
		private final MacthActionType type;
		private final int index;
		private final List<Integer> ids;
	}

	public static List<ScheduleNode> M0A0M1A1M2A2 = Utils.arrList(
			new ScheduleNode(MacthActionType.OnlyMatch,  0, Utils.arrList(1)),
			new ScheduleNode(MacthActionType.OnlyAction, 0, Utils.arrList(1)),
			new ScheduleNode(MacthActionType.OnlyMatch,  1, Utils.arrList(2)),
			new ScheduleNode(MacthActionType.OnlyAction, 1, Utils.arrList(2)),
			new ScheduleNode(MacthActionType.OnlyMatch,  2, Utils.arrList(3)),
			new ScheduleNode(MacthActionType.ActionDone, 2, Utils.arrList(3))
			);

	public static List<ScheduleNode> M0A0MT1T2AT1T2 = Utils.arrList(
			new ScheduleNode(MacthActionType.OnlyMatch,  0, Utils.arrList(2)),
			new ScheduleNode(MacthActionType.OnlyAction, 0, Utils.arrList(2)),
			new ScheduleNode(MacthActionType.OnlyMatch,  1, Utils.arrList(1,3)),
			new ScheduleNode(MacthActionType.ActionDone, 1, Utils.arrList(1,3))
	);
	
	public static Map<Integer, List<Integer>> aluSchedule()
	{
		Map<Integer, List<Integer>> temp_actions = new LinkedHashMap<Integer, List<Integer>>();
		for(ScheduleNode s:schedule){
			if(s.type!=MacthActionType.OnlyMatch){
				temp_actions.put(s.index, s.ids);
			}
		}
		return temp_actions;
	}

	public static List<ScheduleNode> schedule = M0A0MT1T2AT1T2;


	public static Map<Integer, List<Integer>> keygenSchedule(){
		Map<Integer, List<Integer>> ret = new LinkedHashMap<>();
		for(ScheduleNode s:schedule){
			if(s.type==MacthActionType.OnlyMatch){
				if(ret.containsKey(s.index)){
					throw new RuntimeException("Schedule Conflict");
				}
				ret.put(s.index, s.ids);
			}
		}
		return ret;
	}

	public static List<FW> outputSchedule(){
		List<FW> ret = new ArrayList<>();
		for(ScheduleNode s:schedule){
			ret.add(FW.concat(s.type.getFWUS(), new FW(s.index, 4)));
		}
		return ret;
	}

	public static void writeScheduleDat(File outputDir){
		File sf = new File(outputDir+File.separator+"schedule.cfg");
		FileUtils.Delete(sf, false);
		for(FW n: outputSchedule()){
			FileUtils.AppendToFile(sf, n.summary());
			FileUtils.AppendToFile(sf, "\n");
		}

	}
}
