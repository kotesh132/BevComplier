package com.p4.drmt.cfg;

import com.p4.utils.Utils;

public class DRMTConstants {

	/*public static final int BITDATA = 2048;
	public static final int BITWDTH = Utils.lg(BITDATA);
	public static final int BYTWDTH = BITWDTH-3;
	
	public static final int  NUMCBIT = 8;
	public static final int  NUMCBYT = 40;
	
	public static final int  NUMVBIT = 256;        //Number of bits in header vector
	public static final int  BITVBIT = Utils.lg(NUMVBIT);
	public static final int  NUMVBYT = 256;        //Number of bytes in header vector
	public static final int  BITVBYT = Utils.lg(NUMVBYT);*/

	public final static int BYTE = 8;
	//ALUMAP
	public final static int NUMIADR = 1024;
	public final static int  NUMALUS = 32;
	public final static int  NUMTBLE = 32;
	public final static int  NUMSSEG = 16;
	public final static int  NUMPSEG = 16;
	public final static int  NUMPBIT = 256;
	public final static int  NUMPOFF = 256;
	public final static int  NUMSBIT = 16;
	public final static int  NUMSBYT = 16;

	public final static int  BITIADR = Utils.lg(NUMIADR);
	public final static int  BITTBLE = Utils.lg(NUMTBLE);
	public final static int  BITPOFF = Utils.lg(NUMPOFF);
	public final static int  BITIPTR = (BITIADR>BITTBLE) ? BITIADR : BITTBLE;


	//KEYGEN
	public static int maxBytes = 10;
	public static int numkseg = 8;
	public static int numkbit = 16;


	//Scheduler DRMT
	public static final int NUMDRMTPROCS = 1;
	public static final int PACKETRATE = 4; //1 in 4
	public static final int NUMCONTXT = 16;
	public static final int MAXSCHDLNGTH = NUMCONTXT*NUMDRMTPROCS*PACKETRATE;
	public static final int BP_THR = 2;
	public static final int MEM_DELAY = 1;
	public static final int matchDelay = 29;
	public static final int actionDelay = 13;

	//Scheduler config
	public static final int NUMSCHD = 1024;
	public static final int MAXWAIT = 1024;
	public static final int NUMKADR = 32;
	//public static final int NUMIADR = 1024;


	public static final int BITSCHD = Utils.lg(NUMSCHD);
	public static final int BITWAIT = Utils.lg(MAXWAIT);
	public static final int BITKADR = Utils.lg(NUMKADR);
	//public static final int BITIADR = Utils.lg(NUMIADR);
	public static final int BITPBIT = Utils.lg(NUMPBIT);

	public static final int BITNIDX = (BITKADR > BITIADR) ? BITKADR : BITIADR;

	public static final int NUMVALS = 4;
	public static final int VALMTCH = 0;
	public static final int VALACTN = 1;
	public static final int VALCOND = 2;
	public static final int VALEOPP = 3;
	public static final int BITVALS = Utils.lg(NUMVALS);
	//Extractor
	public static final int NUMCBYT = 32;
	public static final int NUMCBIT = 8;
	public static final int EX_NUMROWS = 96;

	//PARSER
	public static final int NUMDOFF = 4;
	public static final int DPASHIFT = 9;
	public static final int START_STATE = 95;
	public static final int ACCEPT_STATE = 94;

	//XBAR
	public static int NUMSOMS = 2;
	public static int BITKSEG = Utils.lg(NUMSOMS);
	public static int NUMKSEG = DRMTConstants.numkseg;
	public static int NUMDSEG = DRMTConstants.numkseg;
	public static int NUMXTDM = 4;



}
