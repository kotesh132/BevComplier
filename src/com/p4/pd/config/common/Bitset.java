package com.p4.pd.config.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bitset {

    private static final int BITS_PER_WORD = 63;
    private static final long WORD_MASK = 0x7FFFFFFFFFFFFFFFL;
    private long[] words;
    @JsonIgnore
    private int size;

    private static int wordIndex(int bitIndex) {
        return (int) Math.floor(bitIndex/63);
    }

    public int getSize() {
        return size;
    }

    private long mask(int bitIndex) {
        if(bitIndex > 63)
            throw new IndexOutOfBoundsException("bitIndex > 63");
        return 1L << bitIndex;
    }

    private void rangeCheck(int beingIndex,int endIndex) {
        if (beingIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex < 0: " + beingIndex);
        if (endIndex < 0)
            throw new IndexOutOfBoundsException("toIndex < 0: " + endIndex);
        if (beingIndex > endIndex)
            throw new IndexOutOfBoundsException("fromIndex: " + beingIndex +
                    " > toIndex: " + endIndex);
    }

    public void alignToSize() {
        words[wordIndex(size)] = words[wordIndex(size)] & (WORD_MASK >> (BITS_PER_WORD - (size%BITS_PER_WORD)));
    }

    public Bitset(int size) {
        this.size = size;
        words = new long[wordIndex(size)+1];
    }

    public Bitset(int size,int value) {
        this(size);
        words[0] = value;
    }

    public Bitset(int size,long value) {
        this(size);
        words[0] = value;
    }

    public Bitset(int size,String value) {
        this(size);
        String binaryString = binaryString(size,value);
        if(size < 64) {
            words[0] = Long.parseLong(binaryString,2)&WORD_MASK;
        } else {
            for(int i=size,j=0;i>0;i-=BITS_PER_WORD,j++) {
                if(i-BITS_PER_WORD < 0) {
                    words[j] = Long.parseLong(binaryString.substring(0, i),2)&WORD_MASK;
                } else {
                    words[j] = Long.parseLong(binaryString.substring(i-BITS_PER_WORD,i),2)&WORD_MASK;
                }
            }
        }
    }

    public static String binaryString(int length, String text){
        String binaryValue = "";
        Pattern p = Pattern.compile("([01]*)");
        Matcher m = p.matcher(text);
        if(m.matches()){
            binaryValue = m.group(1);
        } else {
            p = Pattern.compile("([1-9][0-9]*)'[sS]?[hH]([0-9a-fA-F][0-9a-fA-F]*)");
            m = p.matcher(text);
            if(m.matches()){
                String size = m.group(1);
                String value = m.group(2).replaceAll("_", "");
                Integer size_i = null;
                try{
                    size_i = Integer.parseInt(size);
                } catch(NumberFormatException e){
                    throw new RuntimeException("Size of the hex number "+text+" isnt parsable");
                }
                for(int i=0;i<value.length();i+=7) {
                    if(value.charAt(i) == '0') {
                        binaryValue += "0000";
                        i++;
                    }
                    if(i+7 > value.length())
                        binaryValue += Long.toBinaryString(Long.parseLong(value.substring(i),16));
                    else
                        binaryValue += Long.toBinaryString(Long.parseLong(value.substring(i, i+7),16));
                }
                if(!(binaryValue.length() <= size_i)){
                    String value_subStr = new StringBuilder(binaryValue).reverse().toString().substring(0, size_i);
                    String val_rev = new StringBuilder(value_subStr).reverse().toString();
                    binaryValue = val_rev;
                }
            }
        }
        int binaryValueLength = binaryValue.length();
        if(binaryValueLength > length) {
            return binaryValue.substring(binaryValueLength-length);
        } else if(length > binaryValueLength) {
            for(int i = 0;i<length-binaryValueLength;i++) {
                binaryValue = "0"+binaryValue;
            }
            return binaryValue;
        } else if(binaryValueLength == length)
            return binaryValue;
        throw new RuntimeException("String " + text + " not supported");
    }

    public Bitset or(Bitset rhs) {
        Bitset max = this.size > rhs.size ? this : rhs;
        Bitset ret = new Bitset(max.size);
        int minWords = wordIndex(this.size) > wordIndex(rhs.size) ? wordIndex(rhs.size) :  wordIndex(this.size);
        for(int i = 0;i<minWords;i++) {
            ret.words[i] = words[i] | rhs.words[i];
        }
        for(int i = minWords;i<ret.words.length;i++) {
            ret.words[i] = max.words[i];
        }
        ret.alignToSize();
        return ret;
    }

    public Bitset and(Bitset rhs) {
        Bitset max = this.size > rhs.size ? this : rhs;
        Bitset ret = new Bitset(max.size);
        int minWords = wordIndex(this.size) > wordIndex(rhs.size) ? wordIndex(rhs.size) :  wordIndex(this.size);
        for(int i = 0;i<minWords;i++) {
            ret.words[i] = words[i] & rhs.words[i];
        }
        for(int i = minWords;i<ret.words.length;i++) {
            ret.words[i] = max.words[i];
        }
        ret.alignToSize();
        return ret;
    }

    public Bitset xor(Bitset rhs) {
        Bitset max = this.size > rhs.size ? this : rhs;
        Bitset ret = new Bitset(max.size);
        int minWords = wordIndex(this.size) > wordIndex(rhs.size) ? wordIndex(rhs.size) :  wordIndex(this.size);
        for(int i = 0;i<minWords;i++) {
            ret.words[i] = words[i] ^ rhs.words[i];
        }
        for(int i = minWords;i<ret.words.length;i++) {
            ret.words[i] = max.words[i];
        }
        ret.alignToSize();
        return ret;
    }

    public Bitset leftShift(int number) {
        Bitset ret = new Bitset(this.size);
        int wordsToMove = number/63;
        if(wordsToMove > wordIndex(ret.size)+1)
            return ret;
        for(int i=0;i<=wordIndex(ret.size)-wordsToMove;i++)
            ret.words[i+wordsToMove] = words[i];
        number %= 63;
        long overflow = 0L;
        for(int i = 0;i<wordIndex(this.size)+1;i++) {
            long word = ret.words[i];
            ret.words[i] = ((word << number) | overflow) & WORD_MASK;
            overflow = (word >> (BITS_PER_WORD - number))&WORD_MASK;
        }
        ret.alignToSize();
        return ret;

    }

    public Bitset rightShift(int number) {
        Bitset ret = new Bitset(this.size);
        int wordsToMove = number/63;
        if(wordsToMove > wordIndex(ret.size)+1)
            return ret;
        for(int i = wordIndex(ret.size);i>=wordsToMove;i--) {
            ret.words[i-wordsToMove] = words[i];
        }
        number %= 63;
        long overflow = 0L;
        for(int i = wordIndex(ret.size);i>= 0;i--) {
            long word = ret.words[i];
            ret.words[i] = ((word >> number) | overflow) & WORD_MASK;
            overflow = (word << (BITS_PER_WORD - number))&WORD_MASK;
        }
        ret.alignToSize();
        return ret;
    }

    //@JsonValue
    public String toString() {
        String ret = "";
        for(int i=0;i<wordIndex(this.size)+1;i++) {
            String word = Long.toBinaryString(words[i]);
            for(int j=word.length();j<BITS_PER_WORD;j++) {
                word = "0"+word;
            }
            ret = word + ret;
        }
        return ret.substring(ret.length()-size);
    }

    @JsonValue
    public long toLong() {
        if(words.length ==1)
            return words[0];
        throw new RuntimeException("Bitset is too long for toLong()");
    }

//	public static void main(String[] args) {
//
//
//		Bitset b = new Bitset(5,"101");
//		System.out.println(b.toString());
////		System.out.println(b.words[0]);
////		System.out.println(b.words[1]);
////
////		Bitset ls = b.leftShift(99);
////		System.out.println(ls.words[0]);
////		System.out.println(ls.words[1]);
//	}
//

    //	public void flip() {
    //		for(int i=0;i<wordIndex(size);i++)
    //			words[i] = ~words[i] & WORD_MASK;
    //	}
    //
    //	public void flip(int bitIndex) {
    //		long mask = mask(bitIndex+1%BITS_PER_WORD);
    //		words[wordIndex(bitIndex)-1] ^= mask;
    //	}
    //
    //	public void flip(int beginBitIndex,int endBitIndex) {
    //		rangeCheck(beginBitIndex,endBitIndex);
    //		if(beginBitIndex == endBitIndex)
    //			return;
    //		int beingWordIndex = wordIndex(beginBitIndex);
    //		int endWordIndex = wordIndex(endBitIndex);
    //
    //		beginBitIndex %= 63;
    //		endBitIndex %= 63;
    //
    //		if(beingWordIndex == endWordIndex) {
    //			words[beingWordIndex] ^= WORD_MASK >> (BITS_PER_WORD-endBitIndex) << beginBitIndex-1;
    //		} else {
    //			words[beingWordIndex] ^= WORD_MASK << beginBitIndex-1;
    //
    //			for(int i = beingWordIndex+1;i<endWordIndex;i++)
    //				words[i] ^= WORD_MASK;
    //
    //			words[endWordIndex] ^= WORD_MASK >> (BITS_PER_WORD-endBitIndex);
    //
    //		}
    //	}
    //
    //	public void set(int bitIndex) {
    //		if (bitIndex < 0)
    //			throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
    //		int wordIndex = wordIndex(bitIndex);
    //		bitIndex %= 63;
    //		words[wordIndex] |= 1L << bitIndex;
    //	}
    //
    //	public void set(int beginIndex, int endIndex) {
    //
    //	}
    //
    //	public void clear(int bitIndex) {
    //		if (bitIndex < 0)
    //			throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
    //		int wordIndex = wordIndex(bitIndex);
    //		bitIndex %= 63;
    //		words[wordIndex] &= ~(1L << bitIndex);
    //	}
    //
    //	public void clear(int beginIndex, int endIndex) {
    //
    //	}
    //
    //	public void set(int bitIndex, boolean value) {
    //		if(value)
    //			set(bitIndex);
    //		else
    //			clear(bitIndex);
    //	}
    //
    //	public void set(int bitIndex, int endIndex, boolean value) {
    //		if(value)
    //			set(bitIndex,endIndex);
    //		else
    //			clear(bitIndex,endIndex);
    //	}
    //
    //	public boolean get(int bitIndex) {
    //		return false;
    //	}
    //
    //	public BitSet get(int beingIndex, int endIndex) {
    //		return null;
    //	}
}

