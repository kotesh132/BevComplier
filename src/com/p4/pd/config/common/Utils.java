package com.p4.pd.config.common;

import com.p4.drmt.parser.FW;
import com.p4.pd.config.beans.Drmt;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;


public class Utils {
    /**
     * Return BitSet from Binary String as input
     *
     * @param binary
     * @return
     */
    public static BitSet stringToBitSet(String binary){
        BitSet bitset = new BitSet(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bitset.set(i);

            }
        }
        return bitset;
    }

    public static void constructJSON(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("piconfig.json"), obj);
        } catch (IOException e) {
            throw new RuntimeException("error while writing to json file");
        }
    }

    public static String defaultHexValuByNibbleLen(int len){
        if (len <= 0) throw new IllegalArgumentException("Length of Hex Value cannot be less or equal to 0");
        int nums = len / 4 + (len % 4 != 0 ? 1 : 0);
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(len)).append("'").append("h");
        for (int n = 0; n < nums; n++) {
            sb.append("0");
        }
        return sb.toString();
    }

    public static FW defaultValueWithLen(int len){
        if (len <= 0) throw new IllegalArgumentException("Length of FW Value cannot be less or equal to 0");
        return new FW(0, len);
    }

    public static List<FW> generateDefaultList(int listSize, int entrySize){
        if (listSize < 1 || entrySize < 1) throw new IllegalArgumentException("List Size or Entry size is less than 1");
        List<FW> defaultList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            defaultList.add(defaultValueWithLen(entrySize));
        }
        return defaultList;
    }

    public static boolean insufficientItemsInConfigs(int r, int c, List<List<FW>> items){
        if (items == null || items.size() == 0) return true;
        else if (items.size() <= r) return true;
        int rows = items.size();
        int cols = items.get(r).size();
        return (r >= rows || c >= cols);
    }

    public static int placeHolderFunc(List<Integer> fields){
        if (fields == null || fields.size() == 0) return 0;
        int output = 0;
        for (Integer field : fields) {
            output += field;
        }
        return output;
    }

    /**
     * To retrieve max length and width from list of list of FW
     *
     * @param items
     * @return
     */
    public static int[] getMaxSize2D(List<List<FW>>... items){
        int rsize = 0, csize = 0;
        for (List<List<FW>> item : items){
            int size = item.size();
            if (rsize < size){
                rsize = size;
                csize = item.get(0).size();
            }
        }
        int[] sizes = new int[2];
        sizes[0] = rsize;
        sizes[1] = csize;
        return sizes;
    }


    /**
     * List Splitter based on Size.
     */

    public static List<FW> listTokeinizer(List<FW> list, int listSize, int startIdx){
        if (list == null) throw new NullPointerException("Input List cannot be Null");
        else if (listSize < 1 || startIdx < 0 || list.isEmpty())
            throw new IllegalArgumentException("List is Empty Or listSize is negative or 0 Or start Index < 0");
        List<FW> outputList = new ArrayList<FW>(list.subList(startIdx * listSize, (startIdx + 1) * listSize));
        return outputList;
    }

    /**
     * List FW to List String
     */
    public static List<String> listTokenizerToStringList(List<FW> inpList){
        if (inpList == null) throw new NullPointerException("Input List cannot be Null");
        else if (inpList.isEmpty()) throw new IllegalArgumentException("Input List cannot be Empty");
        List<String> outList = inpList.stream().map(obj -> new String(obj.toHSizeNibbles())).collect(Collectors.toList());
        return outList;
    }

    /**
     * List FW to Integer String
     */
    public static List<Integer> listTokenizerToIntList(List<FW> inpList){
        if (inpList == null) throw new NullPointerException("Input List is Null");
        else if (inpList.isEmpty()) throw new IllegalArgumentException("Input List cannot be Empty");
        List<Integer> outList = inpList.stream().map(obj -> new Integer(obj.getValue())).collect(Collectors.toList());
        return outList;
    }

    public static List<Bitset> listTokenizerToBitsetList(List<FW> inpList){
        if (inpList == null) throw new NullPointerException("Input List is Null");
        else if (inpList.isEmpty()) throw new IllegalArgumentException("Input List cannot be Empty");
        List<Bitset> outList = inpList.stream().map(obj -> new Bitset(obj.getSize(), obj.getValue())).collect(Collectors.toList());
        return outList;
    }

    public static List<Bitset> flattenNestedList(List<List<Bitset>> inplist){
        if (inplist == null) throw new NullPointerException("Input List cannpt be null");
        else if (inplist.isEmpty()) throw new IllegalArgumentException("Input list cannot be empty");
        List<Bitset> outlist = inplist.stream().flatMap(ele -> ele.stream()).collect(Collectors.toList());
        return outlist;
    }

    /**
     * Transform List of Bitset to List oF Long.
     */
    public static List<Long> transformBitsetToLong(List<Bitset> inplist){
        if (inplist == null) throw new NullPointerException("Input List cannpt be null");
        else if (inplist.isEmpty()) throw new IllegalArgumentException("Input list cannot be empty");
        List<Long> outputList = inplist.stream().map(obj -> new Long(obj.toLong())).collect(Collectors.toList());
        return outputList;
    }

    /**
     * List of Characters from List of Bitset ==> Only 4 LSB(maximum value can't be more than 32 bits).
     */
    public static List<Character> generateConfigCharsList(List<Long> inplist){
        if (inplist == null) throw new NullPointerException("Input List cannpt be null");
        else if (inplist.isEmpty()) throw new IllegalArgumentException("Input list cannot be empty");
        List<Character> chars = new ArrayList<Character>();
        for(Long item : inplist){
            List<Character> items = fromLongToBytes(item);
            chars.addAll(items);
        }
        return chars;
    }


    /**
     * List FW to Binary String
     */
//    public static List<BitSet> listTokenizerToBitSetList(List<FW> inpList){
//        if(inpList == null) throw new NullPointerException("Input List is Null");
//        else if(inpList.isEmpty()) throw new IllegalArgumentException("Input List cannot be Empty");
//        List<BitSet> outList = inpList.stream().map(obj->fromStringToBitset(obj.toBinary().toString().split("'b")[1], obj.getSize())).collect(Collectors.toList());
//        return outList;
//    }

    /**
     * Binary String to Bitset
     */
    private static BitSet fromStringToBitset(String binary, int size){
        BitSet bitset = new BitSet(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bitset.set(i);
            } else bitset.clear(i);
        }
        //  System.out.println(bitSetToString(bitset, size));
        return bitset;
    }

    /**
     * BitSet to String
     */
    private static String bitSetToString(BitSet b, int size){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(b.get(i) ? "1" : "0");
        }
        return sb.toString();
    }

    /**
     * Long to Byte array ==> considering only LSB 4 Bytes.
     */
    public static List<Character> fromLongToBytes(Long input){
        if (input == null) throw new NullPointerException("Conversion from Long to array of bytes not possible " +
                "due to input value is NULL");
        List<Character> bytes = new ArrayList<Character>();
        bytes.add((char) ((input & (0XFF000000)) >> 24));
        bytes.add((char) ((input & (0XFF0000)) >> 16));
        bytes.add((char) ((input & (0XFF00)) >> 8));
        bytes.add((char) ((input & (0XFF))));
        return bytes;
    }

    /**
     * Sample File generator.
     */

    public static void writeConfigs()throws IOException{
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("config_keygen.txt"), "UTF-8"));
        List<Character> keygen_configs = Drmt.getInstance().getProcessor().getKeygen();
        char size = (char)(keygen_configs.size() >> 2 & 0XFF);
        out.write(size);
        out.flush();
        for(Character entry : keygen_configs){
            out.write(entry);
            out.flush();
        }


    }

//    public static void main(String[] args) throws IOException {
//
//        Writer out = new BufferedWriter(new OutputStreamWriter(
//                new FileOutputStream("pt.txt"), "UTF-8"));
//        // BufferedWriter out = new BufferedWriter(new FileWriter("pt.txt", "UTF-8"));
////        Long[] longs = {128L, 233L, 460L};
////        for (int n = 0; n < 3; n++) {
////
////            char[] bytes = fromLongToBytes(longs[n]);
////            for (int i = 0; i <= 3; i++) {
////                System.out.println(bytes[i] + " : " + (int) bytes[i]);
////            }
////
////            out.write(bytes);
////            out.flush();
////        }
//
//        List<Long> items = new ArrayList<Long>();
//        items.add(128L);
//        items.add(233L);
//        items.add(460L);
//
//        List<Character> chars = generateConfigCharsList(items);
//        for(Character ch : chars){
//            out.write(ch);
//            out.flush();
//        }
//    }
}
