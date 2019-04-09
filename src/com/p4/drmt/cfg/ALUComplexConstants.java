package com.p4.drmt.cfg;

import com.p4.utils.Utils;

public class ALUComplexConstants {
    public static final int NUMPBIT = 256;
    public static final int NUMPBYT = 256;
    public static final int NUMSBIT = 64;
    public static final int NUMSBYT = 80;
    public static final int NUMCBIT = 2;
    public static final int NUMCBYT = 32;

    public static final int DATWDTH = 32;
    public static final int NUMALUS = 32;
    public static final int NUMIADR = 256;
    public static final int NUMCNTX = 16;

    public static final int BYTE = 8;
    public static final int BITCNTX = Utils.lg(NUMCNTX);
    public static final int BITPBIT = Utils.lg(NUMPBIT);
    @SuppressWarnings("unused")
    public static final int NUMPOFF = NUMPBYT>NUMPBIT ? NUMPBYT : NUMPBIT;
    public static final int BITPOFF = Utils.lg(NUMPOFF);
    public static final int DATABYT = DATWDTH/BYTE;
    public static final int BITIADR = Utils.lg(NUMIADR);
    public static final int NUMTYPE = 3;
    public static final int BITTYPE = Utils.lg(NUMTYPE);
    public static final int TYPEPKT = 0;
    public static final int TYPESCR = 1;
    public static final int TYPECFG = 2;
    public static final int NUMOLEN = DATABYT+1;
    public static final int BITOLEN = Utils.lg(NUMOLEN);
    public static final int LEN1BIT = 0;
    public static final int LEN1BYT = 1;
    public static final int LEN2BYT = 2;
    public static final int LEN3BYT = 3;
    public static final int LEN4BYT = 4;
    public static final int BITOPCD = 5;

    //Instruction= {op0_type,op0_len,op0_off,op1_type,op1_len,op1_off,op_code,res_len,res_off}
    public static final int INSWDTH = 2*(BITTYPE+BITOLEN+BITPOFF) + BITOPCD + BITOLEN + BITPOFF;

}
