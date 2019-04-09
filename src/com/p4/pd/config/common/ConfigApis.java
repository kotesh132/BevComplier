package com.p4.pd.config.common;

import java.util.List;

public class ConfigApis{

    public static List<Bitset> concat_fields_parser_parser_tcam_ctrl_cfg_0(Bitset PARSER_KVLD,Bitset PARSER_FMSK,Bitset PARSER_CMSK,Bitset PARSER_DMSK,Bitset PARSER_FDAT,Bitset PARSER_CDAT,Bitset PARSER_DDAT){
        String PARSER_KVLD_ = PARSER_KVLD.toString().substring(PARSER_KVLD.getSize()-1);
        String PARSER_FMSK_ = PARSER_FMSK.toString().substring(PARSER_FMSK.getSize()-8);
        String PARSER_CMSK_ = PARSER_CMSK.toString().substring(PARSER_CMSK.getSize()-7);
        String PARSER_DMSK_ = PARSER_DMSK.toString().substring(PARSER_DMSK.getSize()-32);
        String PARSER_FDAT_ = PARSER_FDAT.toString().substring(PARSER_FDAT.getSize()-8);
        String PARSER_CDAT_ = PARSER_CDAT.toString().substring(PARSER_CDAT.getSize()-7);
        String PARSER_DDAT_ = PARSER_DDAT.toString().substring(PARSER_DDAT.getSize()-32);
        Bitset entry = new Bitset(95, PARSER_KVLD_ +PARSER_FMSK_ +PARSER_CMSK_ +PARSER_DMSK_ +PARSER_FDAT_ +PARSER_CDAT_ +PARSER_DDAT_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_parser_parser_sram_0(Bitset PARSER_DONE,Bitset PARSER_NEXT,Bitset PARSER_SHFT,Bitset PARSER_DOFF,Bitset PARSER_DVLD,Bitset PARSER_FLAG,Bitset PARSER_ALUE,Bitset PARSER_ALUI){
        String PARSER_DONE_ = PARSER_DONE.toString().substring(PARSER_DONE.getSize()-1);
        String PARSER_NEXT_ = PARSER_NEXT.toString().substring(PARSER_NEXT.getSize()-7);
        String PARSER_SHFT_ = PARSER_SHFT.toString().substring(PARSER_SHFT.getSize()-9);
        String PARSER_DOFF_ = PARSER_DOFF.toString().substring(PARSER_DOFF.getSize()-36);
        String PARSER_DVLD_ = PARSER_DVLD.toString().substring(PARSER_DVLD.getSize()-4);
        String PARSER_FLAG_ = PARSER_FLAG.toString().substring(PARSER_FLAG.getSize()-8);
        String PARSER_ALUE_ = PARSER_ALUE.toString().substring(PARSER_ALUE.getSize()-1);
        String PARSER_ALUI_ = PARSER_ALUI.toString().substring(PARSER_ALUI.getSize()-68);
        Bitset entry = new Bitset(134, PARSER_DONE_ +PARSER_NEXT_ +PARSER_SHFT_ +PARSER_DOFF_ +PARSER_DVLD_ +PARSER_FLAG_ +PARSER_ALUE_ +PARSER_ALUI_);
        return ApiUtils.get32SizeBitsets(entry);
    }

    public static List<Bitset> concat_fields_extractor_extractor_ctrl_cfg_0(Bitset VBIT,Bitset CBIT,Bitset XBIT,Bitset YBIT,Bitset VBYT,Bitset CBYT,Bitset XBYT,Bitset YBYT,Bitset YMSK,Bitset XSFT,Bitset XDIR){
        String VBIT_ = VBIT.toString().substring(VBIT.getSize()-8);
        String CBIT_ = CBIT.toString().substring(CBIT.getSize()-8);
        String XBIT_ = XBIT.toString().substring(XBIT.getSize()-96);
        String YBIT_ = YBIT.toString().substring(YBIT.getSize()-64);
        String VBYT_ = VBYT.toString().substring(VBYT.getSize()-60);
        String CBYT_ = CBYT.toString().substring(CBYT.getSize()-60);
        String XBYT_ = XBYT.toString().substring(XBYT.getSize()-540);
        String YBYT_ = YBYT.toString().substring(YBYT.getSize()-480);
        String YMSK_ = YMSK.toString().substring(YMSK.getSize()-480);
        String XSFT_ = XSFT.toString().substring(XSFT.getSize()-180);
        String XDIR_ = XDIR.toString().substring(XDIR.getSize()-60);
        Bitset entry = new Bitset(2036, VBIT_ +CBIT_ +XBIT_ +YBIT_ +VBYT_ +CBYT_ +XBYT_ +YBYT_ +YMSK_ +XSFT_ +XDIR_);
        return ApiUtils.get32SizeBitsets(entry);
    }

    public static List<Bitset> concat_fields_deparser_tcam_ctrl_cfg_0(Bitset KVLD,Bitset FMSK,Bitset CMSK,Bitset DMSK,Bitset FDAT,Bitset CDAT,Bitset DDAT,Bitset DONE,Bitset NEXT,Bitset SHFT,Bitset DOFF,Bitset DVLD,Bitset FLAG,Bitset ALUE,Bitset ALUI){
        String KVLD_ = KVLD.toString().substring(KVLD.getSize()-1);
        String FMSK_ = FMSK.toString().substring(FMSK.getSize()-8);
        String CMSK_ = CMSK.toString().substring(CMSK.getSize()-7);
        String DMSK_ = DMSK.toString().substring(DMSK.getSize()-32);
        String FDAT_ = FDAT.toString().substring(FDAT.getSize()-8);
        String CDAT_ = CDAT.toString().substring(CDAT.getSize()-7);
        String DDAT_ = DDAT.toString().substring(DDAT.getSize()-32);
        String DONE_ = DONE.toString().substring(DONE.getSize()-1);
        String NEXT_ = NEXT.toString().substring(NEXT.getSize()-7);
        String SHFT_ = SHFT.toString().substring(SHFT.getSize()-9);
        String DOFF_ = DOFF.toString().substring(DOFF.getSize()-36);
        String DVLD_ = DVLD.toString().substring(DVLD.getSize()-4);
        String FLAG_ = FLAG.toString().substring(FLAG.getSize()-8);
        String ALUE_ = ALUE.toString().substring(ALUE.getSize()-1);
        String ALUI_ = ALUI.toString().substring(ALUI.getSize()-68);
        Bitset entry = new Bitset(229, KVLD_ +FMSK_ +CMSK_ +DMSK_ +FDAT_ +CDAT_ +DDAT_ +DONE_ +NEXT_ +SHFT_ +DOFF_ +DVLD_ +FLAG_ +ALUE_ +ALUI_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_deparser_dextractor_ctrl_cfg_0(Bitset VBIT,Bitset CBIT,Bitset XBIT,Bitset YBIT,Bitset VBYT,Bitset CBYT,Bitset XBYT,Bitset YBYT,Bitset YMSK,Bitset XSFT,Bitset XDIR){
        String VBIT_ = VBIT.toString().substring(VBIT.getSize()-8);
        String CBIT_ = CBIT.toString().substring(CBIT.getSize()-8);
        String XBIT_ = XBIT.toString().substring(XBIT.getSize()-88);
        String YBIT_ = YBIT.toString().substring(YBIT.getSize()-64);
        String VBYT_ = VBYT.toString().substring(VBYT.getSize()-60);
        String CBYT_ = CBYT.toString().substring(CBYT.getSize()-60);
        String XBYT_ = XBYT.toString().substring(XBYT.getSize()-480);
        String YBYT_ = YBYT.toString().substring(YBYT.getSize()-480);
        String YMSK_ = YMSK.toString().substring(YMSK.getSize()-480);
        String XSFT_ = XSFT.toString().substring(XSFT.getSize()-180);
        String XDIR_ = XDIR.toString().substring(XDIR.getSize()-60);
        Bitset entry = new Bitset(1968, VBIT_ +CBIT_ +XBIT_ +YBIT_ +VBYT_ +CBYT_ +XBYT_ +YBYT_ +YMSK_ +XSFT_ +XDIR_);
        return ApiUtils.get32SizeBitsets(entry);
    }

    public static List<Bitset> concat_fields_drmt_dist_alumap_acmap_insten_0(Bitset AC_MAP,Bitset INST_EN){
        String AC_MAP_ = AC_MAP.toString().substring(AC_MAP.getSize()-144);
        String INST_EN_ = INST_EN.toString().substring(INST_EN.getSize()-16);
        Bitset entry = new Bitset(160, AC_MAP_ +INST_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alumap_cond_en_0(Bitset COND_TBL,Bitset COND_PTR,Bitset COND_VLD,Bitset EN_OFF,Bitset EN_VLD){
        String COND_TBL_ = COND_TBL.toString().substring(COND_TBL.getSize()-160);
        String COND_PTR_ = COND_PTR.toString().substring(COND_PTR.getSize()-256);
        String COND_VLD_ = COND_VLD.toString().substring(COND_VLD.getSize()-32);
        String EN_OFF_ = EN_OFF.toString().substring(EN_OFF.getSize()-256);
        String EN_VLD_ = EN_VLD.toString().substring(EN_VLD.getSize()-32);
        Bitset entry = new Bitset(768, COND_TBL_ +COND_PTR_ +COND_VLD_ +EN_OFF_ +EN_VLD_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_keygen_0(Bitset YBYT,Bitset YBIT,Bitset YMSK,Bitset KTBL,Bitset KSEG,Bitset KVLD){
        String YBYT_ = YBYT.toString().substring(YBYT.getSize()-640);
        String YBIT_ = YBIT.toString().substring(YBIT.getSize()-1024);
        String YMSK_ = YMSK.toString().substring(YMSK.getSize()-128);
        String KTBL_ = KTBL.toString().substring(KTBL.getSize()-40);
        String KSEG_ = KSEG.toString().substring(KSEG.getSize()-24);
        String KVLD_ = KVLD.toString().substring(KVLD.getSize()-8);
        Bitset entry = new Bitset(1936, YBYT_ +YBIT_ +YMSK_ +KTBL_ +KSEG_ +KVLD_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_0(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_1(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_2(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_3(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_4(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_5(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_6(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_7(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_8(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_9(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_10(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_11(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_12(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_13(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_14(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_alu_15(Bitset OP0_TYPE,Bitset OP0_LEN,Bitset OP0_OFF,Bitset OP1_TYPE,Bitset OP1_LEN,Bitset OP1_OFF,Bitset OP_CODE,Bitset RES_LEN,Bitset RES_OFF,Bitset PT_STEP,Bitset COND_OFF,Bitset COND_EN){
        String OP0_TYPE_ = OP0_TYPE.toString().substring(OP0_TYPE.getSize()-2);
        String OP0_LEN_ = OP0_LEN.toString().substring(OP0_LEN.getSize()-3);
        String OP0_OFF_ = OP0_OFF.toString().substring(OP0_OFF.getSize()-8);
        String OP1_TYPE_ = OP1_TYPE.toString().substring(OP1_TYPE.getSize()-2);
        String OP1_LEN_ = OP1_LEN.toString().substring(OP1_LEN.getSize()-3);
        String OP1_OFF_ = OP1_OFF.toString().substring(OP1_OFF.getSize()-8);
        String OP_CODE_ = OP_CODE.toString().substring(OP_CODE.getSize()-5);
        String RES_LEN_ = RES_LEN.toString().substring(RES_LEN.getSize()-3);
        String RES_OFF_ = RES_OFF.toString().substring(RES_OFF.getSize()-8);
        String PT_STEP_ = PT_STEP.toString().substring(PT_STEP.getSize()-1);
        String COND_OFF_ = COND_OFF.toString().substring(COND_OFF.getSize()-8);
        String COND_EN_ = COND_EN.toString().substring(COND_EN.getSize()-1);
        Bitset entry = new Bitset(52, OP0_TYPE_ +OP0_LEN_ +OP0_OFF_ +OP1_TYPE_ +OP1_LEN_ +OP1_OFF_ +OP_CODE_ +RES_LEN_ +RES_OFF_ +PT_STEP_ +COND_OFF_ +COND_EN_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_sch_0(Bitset TX_SCH_NODE,Bitset TX_SCH_WAIT,Bitset TX_SCH_NIDX,Bitset TX_SCH_FLAG){
        String TX_SCH_NODE_ = TX_SCH_NODE.toString().substring(TX_SCH_NODE.getSize()-2);
        String TX_SCH_WAIT_ = TX_SCH_WAIT.toString().substring(TX_SCH_WAIT.getSize()-8);
        String TX_SCH_NIDX_ = TX_SCH_NIDX.toString().substring(TX_SCH_NIDX.getSize()-9);
        String TX_SCH_FLAG_ = TX_SCH_FLAG.toString().substring(TX_SCH_FLAG.getSize()-1);
        Bitset entry = new Bitset(20, TX_SCH_NODE_ +TX_SCH_WAIT_ +TX_SCH_NIDX_ +TX_SCH_FLAG_);
        return ApiUtils.get32SizeBitsets(entry);
    }
    public static List<Bitset> concat_fields_drmt_dist_ccomplex_0(Bitset PRIMOFF,Bitset PRIMNEG,Bitset PRIMVLD,Bitset ALUINST,Bitset CONDADR,Bitset BRDATA,Bitset BRMASK,Bitset BRDEST,Bitset BRVLD){
        String PRIMOFF_ = PRIMOFF.toString().substring(PRIMOFF.getSize()-128);
        String PRIMNEG_ = PRIMNEG.toString().substring(PRIMNEG.getSize()-16);
        String PRIMVLD_ = PRIMVLD.toString().substring(PRIMVLD.getSize()-16);
        String ALUINST_ = ALUINST.toString().substring(ALUINST.getSize()-45);
        String CONDADR_ = CONDADR.toString().substring(CONDADR.getSize()-40);
        String BRDATA_ = BRDATA.toString().substring(BRDATA.getSize()-64);
        String BRMASK_ = BRMASK.toString().substring(BRMASK.getSize()-64);
        String BRDEST_ = BRDEST.toString().substring(BRDEST.getSize()-64);
        String BRVLD_ = BRVLD.toString().substring(BRVLD.getSize()-8);
        Bitset entry = new Bitset(445, PRIMOFF_ +PRIMNEG_ +PRIMVLD_ +ALUINST_ +CONDADR_ +BRDATA_ +BRMASK_ +BRDEST_ +BRVLD_);
        return ApiUtils.get32SizeBitsets(entry);
    }


}
