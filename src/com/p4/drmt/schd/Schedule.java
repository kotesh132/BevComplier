package com.p4.drmt.schd;

import com.p4.drmt.cfg.DRMTConstants;
import lombok.Data;

import java.util.List;
@Data
public class Schedule {
    private static final int MAX_SCHEDULE_LENGTH = DRMTConstants.MAXSCHDLNGTH;

    private final List<TimeQuanta> schedule;
    private final int actionDelay;
    private final int matchDelay;
    private final int packetRate;
    private final int numProcs;

    private int length(){
        int i = schedule.size()-1;
        for(; i>=0;i--){
            if(!(schedule.get(i) instanceof NOOPTimeQuanta))
                break;
        }
        //1 extra for 0 based index
        return i+1+actionDelay;
    }

    public void insertNOOPat(int index){
        if(length()== MAX_SCHEDULE_LENGTH){
            throw new IllegalStateException("Can't insert NOOP in current schedule as schedule length will exceed max length of"+ MAX_SCHEDULE_LENGTH);
        }

        schedule.add(index, NOOPTimeQuanta.NOOP);
        //TODO put condtions to see we don't
        if(schedule.get(schedule.size()-1) instanceof NOOPTimeQuanta)
            schedule.remove(schedule.size()-1);
    }

    public void adjust(){
        int numtries = 5;
        while (numtries>0){
            int index = firstConflictIndex();
            if(index>0){
                insertNOOPat(index);
            }else{
                break;
            }
        }
    }

    public int firstConflictIndex(){
        int modNumber = packetRate*numProcs;
        for(int i=0;i<modNumber;i++){
            int count =0;
            for(int j=i; j<schedule.size();j+=modNumber){
                if(schedule.get(j) instanceof MatchQuanta || schedule.get(j) instanceof ActionQuanta){
                    count++;
                }
                if(count>1){
                    System.out.println("Found conflict at index:"+j);
                    return j;
                }
            }
        }

        int modNumber2 = packetRate;
        for(int i=0;i<modNumber2;i++){
            int count =0;
            for(int j=i; j<schedule.size();j+=modNumber2){
                if(schedule.get(j) instanceof MatchQuanta || schedule.get(j) instanceof ActionQuanta){
                    count++;
                }
                if(count>1){
                    System.out.println("Found conflict at index between different procs:"+j);
                    return j;
                }
            }
        }


        return -1;
    }

}
