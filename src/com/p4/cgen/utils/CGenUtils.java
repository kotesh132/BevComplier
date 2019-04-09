package com.p4.cgen.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CGenUtils {

    public static String joinStrings(List<String> strings, String seperator){

        if (strings == null || seperator == null) {
            return null;
        }
        if (strings.isEmpty()) {
            return "";
        }

        return strings.stream().collect(Collectors.joining(seperator));
    }

    public static String joinStrings(String seperator, String... strings) {
        if (strings == null || seperator == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(seperator).append(s);
        }

        return sb.substring(seperator.length());
    }
}
