package com.p4.pd.config.common;

public class ForgeSpec {
    //Keygen Field Width Hardware Layout
    public static final int KEYGEN_KSEG_WORDLENGTH = 24;
    public static final int KEYGEN_KVLD_WORDLENGTH = 8;
    public static final int KEYGEN_KTBL_WORDLENGTH = 40;
    public static final int KEYGEN_YBIT_WORDLENGTH = 1024;
    public static final int KEYGEN_YBYT_WORDLENGTH = 640;
    public static final int KEYGEN_YMSK_WORDLENGTH = 128;
    //Keygen Field BitSize Configuration
    public static final int KEYGEN_KSEG_BIT_SIZE = 3; //Initially 2, changed to 3
    public static final int KEYGEN_KVLD_BIT_SIZE = 1;
    public static final int KEYGEN_KTBL_BIT_SIZE = 5; //Initially 8, Generated Config size is not correct.
    public static final int KEYGEN_YBIT_BIT_SIZE = 8;
    public static final int KEYGEN_YBYT_BIT_SIZE = 8;
    public static final int KEYGEN_YMSK_BIT_SIZE = 1;

    //ALU MAP Width Hardware Layout
    public static final int ALUMAP_COND_TBL_WORDLENGTH = 160;
    public static final int ALUMAP_COND_PTR_WORDLENGTH = 256;
    public static final int ALUMAP_COND_VLD_WORDLENGTH = 32;
    public static final int ALUMAP_EN_OFF_WORDLENGTH = 256;
    public static final int ALUMAP_EN_VLD_WORDLENGTH = 32;
    //ALU MAP Field BitSize Configuration
    public static final int ALUMAP_COND_TBL_BIT_SIZE = 8;//
    public static final int ALUMAP_COND_PTR_BIT_SIZE = 8;//Initially 5, Generated Config is not correct. Although default values are used, as generated config are not used.
    public static final int ALUMAP_COND_VLD_BIT_SIZE = 1;//
    public static final int ALUMAP_EN_OFF_BIT_SIZE = 8;
    public static final int ALUMAP_EN_VLD_BIT_SIZE = 1;

    //ALU ACTION MAP Width Hardware Layout
    public static final int ALUMAPINST_AC_MAP_WORDLENGTH = 144;
    public static final int ALUMAPINST_INST_EN_WORDLENGTH = 16;
    //ALU ACTION MAP Field BitSize Configuration
    public static final int ALUMAPINST_AC_MAP_BIT_SIZE = 8; //Initially 10, Changed to 8, With generated values there seems to be no data loss.(As MSB nibble is 0)
    public static final int ALUMAPINST_INST_EN_BIT_SIZE = 1;

    //ALU COND COMPLEX Width Hardware Layout
    public static final int ALUCOND_PRIMOFF_WORDLENGTH = 128;
    public static final int ALUCOND_PRIMNEG_WORDLENGTH = 16;
    public static final int ALUCOND_PRIMVLD_WORDLENGTH = 16;
    public static final int ALUCOND_ALUINST_WORDLENGTH = 45;
    public static final int ALUCOND_CONDADR_WORDLENGTH = 40;
    public static final int ALUCOND_BRDATA_WORDLENGTH = 64;
    public static final int ALUCOND_BRMASK_WORDLENGTH = 64;
    public static final int ALUCOND_BRDEST_WORDLENGTH = 64;
    public static final int ALUCOND_BRVLD_WORDLENGTH = 8;
    //ALU COND COMPLEX Field BitSize Configuration
    public static final int ALUCOND_PRIMOFF_BIT_SIZE = 8;
    public static final int ALUCOND_PRIMNEG_BIT_SIZE = 1;
    public static final int ALUCOND_PRIMVLD_BIT_SIZE = 1;
    public static final int ALUCOND_ALUINST_BIT_SIZE = 3;
    public static final int ALUCOND_CONDADR_BIT_SIZE = 5;
    public static final int ALUCOND_BRDATA_BIT_SIZE = 8;
    public static final int ALUCOND_BRMASK_BIT_SIZE = 8;
    public static final int ALUCOND_BRDEST_BIT_SIZE = 8;
    public static final int ALUCOND_BRVLD_BIT_SIZE = 1;

    //SCHEDULER Width Hardware Layout
    public static final int SCHEDULER_SCH_NODE_WORDLENGTH = 2;
    public static final int SCHEDULER_SCH_WAIT_WORDLENGTH = 8;
    public static final int SCHEDULER_SCH_NIDX_WORDLENGTH = 9;
    public static final int SCHEDULER_SCH_FLAG_WORDLENGTH = 1;
    //SCHEDULER Field BitSize Configuration
    public static final int SCHEDULER_SCH_NODE_BIT_SIZE = 2;
    public static final int SCHEDULER_SCH_WAIT_BIT_SIZE = 8;
    public static final int SCHEDULER_SCH_NIDX_BIT_SIZE = 9; //Initially 10, changed to 9. With generated values there seems to be no data loss.(As MSB nibble is 0)
    public static final int SCHEDULER_SCH_FLAG_BIT_SIZE = 1;


    //ALU COMPLEX Width Hardware Layout
    public static final int ALU_OP0_TYPE_WORDLENGTH = 2;
    public static final int ALU_OP0_LEN_WORDLENGTH = 3;
    public static final int ALU_OP0_OFF_WORDLENGTH = 8;
    public static final int ALU_OP1_TYPE_WORDLENGTH = 2;
    public static final int ALU_OP1_LEN_WORDLENGTH = 3;
    public static final int ALU_OP1_OFF_WORDLENGTH = 8;
    public static final int ALU_OP_CODE_WORDLENGTH = 5;
    public static final int ALU_RES_LEN_WORDLENGTH = 3;
    public static final int ALU_RES_OFF_WORDLENGTH = 8;
    public static final int ALU_PT_STEP_WORDLENGTH = 1;
    public static final int ALU_COND_OFF_WORDLENGTH = 8;
    public static final int ALU_COND_EN_WORDLENGTH = 1;
    //ALU COMPLEX Field BitSize Configuration
    public static final int ALU_OP0_TYPE_BIT_SIZE = 2;
    public static final int ALU_OP0_LEN_BIT_SIZE = 3;
    public static final int ALU_OP0_OFF_BIT_SIZE = 8;
    public static final int ALU_OP1_TYPE_BIT_SIZE = 2;
    public static final int ALU_OP1_LEN_BIT_SIZE = 3;
    public static final int ALU_OP1_OFF_BIT_SIZE = 8;
    public static final int ALU_OP_CODE_BIT_SIZE = 5;
    public static final int ALU_RES_LEN_BIT_SIZE = 3;
    public static final int ALU_RES_OFF_BIT_SIZE = 8;
    public static final int ALU_PT_STEP_BIT_SIZE = 1;
    public static final int ALU_COND_OFF_BIT_SIZE = 8;
    public static final int ALU_COND_EN_BIT_SIZE = 1;


    //PARSER EXTRACTOR Width Hardware Layout
    public static final int EXTRACTOR_VBIT_WORDLENGTH = 8;
    public static final int EXCTRACTOR_CBIT_WORDLENGTH = 8;
    public static final int EXCTRACTOR_XBIT_WORDLENGTH = 96;
    public static final int EXCTRACTOR_YBIT_WORDLENGTH = 64;
    public static final int EXCTRACTOR_VBYT_WORDLENGTH = 60;
    public static final int EXCTRACTOR_CBYT_WORDLENGTH = 60;
    public static final int EXCTRACTOR_XBYT_WORDLENGTH = 540;
    public static final int EXCTRACTOR_YBYT_WORDLENGTH = 480;
    public static final int EXCTRACTOR_YMSK_WORDLENGTH = 480;
    public static final int EXCTRACTOR_XSFT_WORDLENGTH = 160;
    public static final int EXCTRACTOR_XDIR_WORDLENGTH = 60;

    //PARSER EXTRACTOR  Field BitSize Configuration
    public static final int EXCTRACTOR_VBIT_BIT_SIZE = 1;
    public static final int EXCTRACTOR_CBIT_BIT_SIZE = 1;
    public static final int EXCTRACTOR_XBIT_BIT_SIZE = 12;//making it 12, adding an extra bit
    public static final int EXCTRACTOR_YBIT_BIT_SIZE = 8;
    public static final int EXCTRACTOR_VBYT_BIT_SIZE = 1;
    public static final int EXCTRACTOR_CBYT_BIT_SIZE = 1;
    public static final int EXCTRACTOR_XBYT_BIT_SIZE = 9;//making it 9, adding an extra bit
    public static final int EXCTRACTOR_YBYT_BIT_SIZE = 8;
    public static final int EXCTRACTOR_YMSK_BIT_SIZE = 8;
    public static final int EXCTRACTOR_XSFT_BIT_SIZE = 3;
    public static final int EXCTRACTOR_XDIR_BIT_SIZE = 1;


    //PARSER TCAM Width Hardware Layout
    public static final int PARSER_KVLD_WORDLENGTH = 1;
    public static final int PARSER_FMSK_WORDLENGTH = 8;
    public static final int PARSER_CMSK_WORDLENGTH = 7;
    public static final int PARSER_DMSK_WORDLENGTH = 32;
    public static final int PARSER_FDAT_WORDLENGTH = 8;
    public static final int PARSER_CDAT_WORDLENGTH = 7;
    public static final int PARSER_DDAT_WORDLENGTH = 32;

    //PARSER TCAM  Field BitSize Configuration
    public static final int PARSER_KVLD_BIT_SIZE = 1;
    public static final int PARSER_FMSK_BIT_SIZE = 8;
    public static final int PARSER_CMSK_BIT_SIZE = 7;//8 in config, but only have to consider 7 as the hardware suggests
    public static final int PARSER_DMSK_BIT_SIZE = 8;//
    public static final int PARSER_FDAT_BIT_SIZE = 8;//
    public static final int PARSER_CDAT_BIT_SIZE = 7;//8 in config, but only have to consider 7 as the hardware suggests
    public static final int PARSER_DDAT_BIT_SIZE = 8;

    //PARSER SRAM Width Hardware Layout
    public static final int PARSER_DONE_WORDLENGTH = 1;
    public static final int PARSER_NEXT_WORDLENGTH = 7;
    public static final int PARSER_SHFT_WORDLENGTH = 9;
    public static final int PARSER_DOFF_WORDLENGTH = 36;
    public static final int PARSER_DVLD_WORDLENGTH = 4;
    public static final int PARSER_FLAG_WORDLENGTH = 8;
    public static final int PARSER_ALUE_WORDLENGTH = 1;
    public static final int PARSER_ALUI_WORDLENGTH = 68;

    //PARSER SRAM  Field BitSize Configuration
    public static final int PARSER_DONE_BIT_SIZE = 1;//
    public static final int PARSER_NEXT_BIT_SIZE = 7;//8 in config, but only have to consider 7 as the hardware suggests
    public static final int PARSER_SHFT_BIT_SIZE = 9;//8 in config, to consider 9 as the hardware suggests
    public static final int PARSER_DOFF_BIT_SIZE = 9; //###
    public static final int PARSER_DVLD_BIT_SIZE = 1;
    public static final int PARSER_FLAG_BIT_SIZE = 8;
    public static final int PARSER_ALUE_BIT_SIZE = 3;
    public static final int PARSER_ALUI_BIT_SIZE = 8;


    //DE-PARSER DE-EXTRACTOR Width Hardware Layout
    public static final int DEPARSER_VBIT_WORDLENGTH = 8;
    public static final int DEPARSER_CBIT_WORDLENGTH = 8;
    public static final int DEPARSER_XBIT_WORDLENGTH = 88;
    public static final int DEPARSER_YBIT_WORDLENGTH = 64;
    public static final int DEPARSER_VBYT_WORDLENGTH = 60;
    public static final int DEPARSER_CBYT_WORDLENGTH = 60;
    public static final int DEPARSER_XBYT_WORDLENGTH = 480;
    public static final int DEPARSER_YBYT_WORDLENGTH = 480;
    public static final int DEPARSER_YMSK_WORDLENGTH = 480;
    public static final int DEPARSER_XSFT_WORDLENGTH = 180;
    public static final int DEPARSER_XDIR_WORDLENGTH = 60;

    //DE-PARSER DE-EXTRACTOR  Field BitSize Configuration
    public static final int DEPARSER_VBIT_BIT_SIZE = 1;
    public static final int DEPARSER_CBIT_BIT_SIZE = 1;
    public static final int DEPARSER_XBIT_BIT_SIZE = 12;//adding extra bit
    public static final int DEPARSER_YBIT_BIT_SIZE = 8;
    public static final int DEPARSER_VBYT_BIT_SIZE = 1;
    public static final int DEPARSER_CBYT_BIT_SIZE = 1;
    public static final int DEPARSER_XBYT_BIT_SIZE = 8;
    public static final int DEPARSER_YBYT_BIT_SIZE = 8;
    public static final int DEPARSER_YMSK_BIT_SIZE = 8;
    public static final int DEPARSER_XSFT_BIT_SIZE = 3;
    public static final int DEPARSER_XDIR_BIT_SIZE = 1;

    //DE-PARSER TCAM Width Hardware Layout
    public static final int DEPARSER_KVLD_WORDLENGTH = 1;
    public static final int DEPARSER_FMSK_WORDLENGTH = 8;
    public static final int DEPARSER_CMSK_WORDLENGTH = 7;
    public static final int DEPARSER_DMSK_WORDLENGTH = 32;
    public static final int DEPARSER_FDAT_WORDLENGTH = 8;
    public static final int DEPARSER_CDAT_WORDLENGTH = 7;
    public static final int DEPARSER_DDAT_WORDLENGTH = 32;
    public static final int DEPARSER_DONE_WORDLENGTH = 1;
    public static final int DEPARSER_NEXT_WORDLENGTH = 7;
    public static final int DEPARSER_SHFT_WORDLENGTH = 9;
    public static final int DEPARSER_DOFF_WORDLENGTH = 36;
    public static final int DEPARSER_DVLD_WORDLENGTH = 4;
    public static final int DEPARSER_FLAG_WORDLENGTH = 8;
    public static final int DEPARSER_ALUE_WORDLENGTH = 1;
    public static final int DEPARSER_ALUI_WORDLENGTH = 68;


    //DE-PARSER TCAM  Field BitSize Configuration
    public static final int DEPARSER_KVLD_BIT_SIZE = 1;//
    public static final int DEPARSER_FMSK_BIT_SIZE = 8;//
    public static final int DEPARSER_CMSK_BIT_SIZE = 7;//8 in config, 7 in HW
    public static final int DEPARSER_DMSK_BIT_SIZE = 2;
    public static final int DEPARSER_FDAT_BIT_SIZE = 8;//
    public static final int DEPARSER_CDAT_BIT_SIZE = 7;//8 in config, 7 in HW
    public static final int DEPARSER_DDAT_BIT_SIZE = 8;
    public static final int DEPARSER_DONE_BIT_SIZE = 1;//
    public static final int DEPARSER_NEXT_BIT_SIZE = 7;//8 in config, 7 in HW
    public static final int DEPARSER_SHFT_BIT_SIZE = 9;//
    public static final int DEPARSER_DOFF_BIT_SIZE = 9;//changing it to 9, adding a bit extra
    public static final int DEPARSER_DVLD_BIT_SIZE = 1;
    public static final int DEPARSER_FLAG_BIT_SIZE = 8;//
    public static final int DEPARSER_ALUE_BIT_SIZE = 1;
    public static final int DEPARSER_ALUI_BIT_SIZE = 68;//

}
