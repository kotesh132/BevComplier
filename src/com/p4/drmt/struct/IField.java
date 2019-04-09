package com.p4.drmt.struct;

import com.p4.p416.applications.Target;
import com.p4.p416.applications.Type;
import com.p4.utils.Summarizable;

public interface IField extends Summarizable,IVariable{


    /**
     * @return Offset Depends on type of Memory can be bits or Bytes depending on type
     */
    public int getOffset();

    /**
     * @return Always returns size in bits
     */
    public int getSize();
    public Target.MemoryType getType();
    public String getProgName();
    public String getUniqName();
    public IField addOffset(int addOff, int size);
    public Type getFieldType();
    public int getLocalOffset();
    public String offsetSummary();
    //void setOffset(int offset);

    default public boolean intersects(IVariable o){
        /*if(o instanceof IField) {
            IField other  = (IField) o;
            if (getType().equals(other.getType())) {
                int thisOffset = getOffset();
                int otherOffset = other.getOffset();
                if(!getType().bitType){
                    thisOffset = getOffset()*8;
                    otherOffset = other.getOffset()*8;
                }
                if ((otherOffset >= thisOffset && otherOffset < (thisOffset + getSize())) ||
                        (thisOffset >= otherOffset && thisOffset < (otherOffset + other.getSize()))) {
                    return true;
                }
            }
        }
        return false;*/
        if(o instanceof IField){
            if(getProgName().equals(((IField) o).getProgName()) && getFieldType().equals(((IField) o).getFieldType()))
                return true;
        }
        return false;
    }
}
